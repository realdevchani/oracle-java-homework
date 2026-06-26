# 학생 관리 시스템 (JDBC)

### Reference Documentation

Class Explain :

## BEFORE BEGINNING
```sql
SQL 구문

CREATE SEQUENCE SEQ_STUDENT;

CREATE TABLE TBL_STUDENT(
    STUDENT_ID NUMBER CONSTRAINT PK_STUDENT PRIMARY KEY,
    STUDENT_NAME VARCHAR2(255),
    STUDENT_EMAIL VARCHAR2(255) UNIQUE,
    STUDENT_PHONE VARCHAR2(255) UNIQUE,
    STUDENT_AGE NUMBER,
    STUDENT_ADDRESS VARCHAR2(255)
);
```

1. StudentVO
```java
해당 객체는 엔티티 담는 용도로만 사용
StudentVO{
    Long studentId => Sequence 객체로 자동 증가
    String studentName -> 학생 이름
    Integer studentAge -> 학생 나이
    String studentAddress -> 학생 주소
    String studentEmail -> 학생 이메일 (UNIQUE)
    String studentPhone -> 학생 전화번호 (UNIQUE)

    voToStudentDTO(StudentVO vo){} : StudentDTO
        => VO 를 DTO 로 변환하는 메서드
}
```

2. StudentDTO
```java
외부 노출용 데이터 전달 객체
StudentDTO{
    String studentName -> 학생 이름
    String studentEmail -> 학생 이메일
    String studentPhone -> 학생 전화번호
}
```

3. StudentDAO / StudentDAOImpl
```java
DB 조작 담당 Repository 계층
StudentDAO (interface) : DB 조작 메서드 정의
StudentDAOImpl (구현체) : 실제 JDBC 로 쿼리 실행

StudentDAOImpl{
    getConnection(){} : Connection
        => DriverManager 로 Oracle DB 연결 생성
    resultSetToStudent(ResultSet rs){} : StudentVO
        => ResultSet 한 행을 StudentVO 객체로 변환하는 매핑 함수

    save(StudentVO vo){} : void
        => 학생 정보를 TBL_STUDENT 에 INSERT (SEQ_STUDENT.NEXTVAL 로 ID 자동 생성)
    update(StudentVO vo){} : void
        => STUDENT_ID 조건으로 학생 정보 UPDATE
    delete(Long studentId){} : void
        => STUDENT_ID 조건으로 학생 정보 DELETE
    findById(Long studentId){} : Optional<StudentVO>
        => STUDENT_ID 조건으로 단일 행 조회
    findAll(){} : List<StudentVO>
        => 전체 학생 조회 (STUDENT_ID 오름차순)
}
```

4. StudentService / StudentServiceImpl
```java
비즈니스 로직 담당 Service 계층
StudentService (interface) : 서비스 메서드 정의
StudentServiceImpl (구현체) : DAO 호출 후 VO -> DTO 변환

StudentServiceImpl{
    register(StudentVO vo){} : boolean
        => 학생 등록, 성공 시 true / 실패 시 false
    update(StudentVO vo){} : void
        => 학생 정보 변경
    delete(Long id){} : void
        => 학생 삭제
    findById(Long id){} : StudentDTO
        => ID 로 학생 조회 후 DTO 변환하여 리턴, 없으면 RuntimeException
    findAll(){} : List<StudentDTO>
        => 전체 학생 조회 후 DTO 리스트로 변환하여 리턴
}
```

5. StartTest
```java
해당 클래스는 실제로 run 하는 콘솔 컨트롤러 역할
메뉴 선택 후 Scanner 로 입력받아 Service 호출

    1. 학생 등록 : 이름, 이메일, 나이, 전화번호, 주소 입력 -> register()
    2. 학생 정보 변경 : ID + 변경할 정보 입력 -> update()
    3. 학생 삭제 : ID 입력 -> delete()
    4. 전체 학생 조회 : findAll() -> DTO 리스트 출력
    5. 학생 단건 조회 : ID 입력 -> findById() -> DTO 출력
    0. 종료
```

6. Tips - update 할 때 findById 재사용하기

```
[문제] update 할 때 모든 필드를 다 입력받아야 할까?

이름만 바꾸고 싶은데 이메일, 나이, 전화번호, 주소까지 전부 다시 입력하는 건 비효율적

[해결] 기존에 만든 findById() 를 재사용하면 된다
```

```java
// === BAD: findById 안 쓰는 경우 ===
// 이름만 바꾸고 싶은데 나머지 값을 모르면?
// -> null 이나 빈 값이 들어가서 기존 데이터가 날아감

StudentVO studentVO = StudentVO.builder()
        .studentId(1L)
        .studentName("홍길동")    // 이것만 바꾸고 싶은데
        .studentEmail(???)       // 기존 값을 모름
        .studentAge(???)         // 기존 값을 모름
        .studentPhone(???)       // 기존 값을 모름
        .studentAddress(???)     // 기존 값을 모름
        .build();

studentService.update(studentVO);
// -> email, age, phone, address 가 전부 null 로 덮어씌워짐
```

```java
// === GOOD: findById 로 기존 데이터 먼저 가져오기 ===
// 1) 먼저 기존 객체를 DB 에서 꺼낸다
StudentVO target = studentDAO.findById(1L)
        .orElseThrow(() -> new RuntimeException("학생을 찾지 못함"));

// target 에는 기존 값이 전부 들어있음
// target.studentName = "강감찬"
// target.studentEmail = "chan@naver.com"
// target.studentAge = 26
// target.studentPhone = "010-1234-5678"
// target.studentAddress = "안산시"

// 2) 바꾸고 싶은 필드만 덮어쓴다
target.setStudentName("홍길동");

// 나머지 필드는 기존 값 그대로 유지됨!

// 3) update 호출
studentService.update(target);
// -> 이름만 "홍길동" 으로 바뀌고, 나머지는 기존 값 유지
```

```
[핵심] 왜 재사용이 좋은가?

1. findById() 를 한 번 만들어두면 조회 뿐만 아니라 update 에서도 쓸 수 있다
   -> 같은 코드를 두 번 안 짜도 됨

2. 사용자가 바꾸고 싶은 값만 입력하면 됨
   -> 안 바꿀 필드는 기존 값이 그대로 남아있으니까

3. 나중에 findById() 로직이 바뀌어도 (ex: 캐시 추가, 검증 추가)
   -> update 쪽 코드는 안 건드려도 자동 반영됨
```

7. 사실상 StartTest == Controller , Service, Repository, VO 등 3티어로 최대한 나눠놓은 프로젝트임.