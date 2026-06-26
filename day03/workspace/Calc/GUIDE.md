# JDBC 로 DB 연결하기

### Reference Documentation

Class Explain :

## BEFOR BEGINNING
```sql
SQL 구문

CREATE SEQUENCE SEQ_CALC;

CREATE TABLE TBL_CALC (
    CALC_ID NUMBER CONSTRAINT PK_CALC PRIMARY KEY,
    CALC_NUM1 NUMBER,
    CALC_NUM2 NUMBER,
    CALC_OPERATOR VARCHAR2(1) CONSTRAINT CHECK_CALC_OPERATOR
    CHECK (CALC_OPERATOR IN ('+', '-', '*', '/')),
    CALC_RESULT NUMBER
);
```



1. 
``` java
해당 객체는 엔티티 담는 용도로만 사용
Calc{ 
    Long calcId => Sequence 객체로 자동 증가
    int CalcNumber1 -> 피연산자 (좌변)
    int CalcNumber2 -> 피연산자 (우변)
    String calcOperator -> 연산자 (constraint :: check In('+', '-', '*', '/')
    int calcResult -> 계산 결과값 담기
}
 ```
2. Calculator
``` java
Calculator{
    public Calc calculate(Calc calc){} 
    => Calc 객체에 있는 number1, number2 를 가져와 calc 객체의 operator 구분 후 계산하는 계산식
    :  사용자가 입력한 값이 number1,2 operator 만 존재해 해당 결과 값을 Calc 에 추가 후 해당 객체 리턴
}
    
```
3. Consumer 
```java
계산하는 calculator 를 사용하는 사용 객체
Consumer{
    doService(requestInput){} : Calc
            => 실제로 위 calculator 를 사용하는 함수 내 Calc calc = Calc.builder().build() 는 객체 생성 패턴 중 1개
}
```
4. RunTest
```java
해당 클래스는 실제로 run 하는 객체
        
        INSERT 작업 : ln80~ln96
        SELECT 작업 : ln97~ln150
            1)  원하는 rows 중 하나의 행만 조회 ID 조건 조회 ( 97~121)
            2)  tabl 내에 있는 전체 rows 조회 (124~136) -> calculator.getListFromResultSet(); 
                -> ResultSet 으로 가져온 데이터를 자바의 List collection 으로 변환  
            3)  가장 마지막 행 조회 (137~)

        150줄 이후 catch 구문
```

5. CalculationRepo / CalculationRepoImpl
```java
RunTest 에서 직접 하던 DB 쿼리 실행을 분리한 Repository 계층
CalculationRepo (interface) : DB 조작 메서드 정의
CalculationRepoImpl (구현체) : 실제 JDBC 로 쿼리 실행

CalculationRepoImpl{
    getConnection(){} : Connection
            => DriverManager 로 Oracle DB 연결 생성
    resultSetToCalc(ResultSet rs){} : Calc
            => ResultSet 한 행을 Calc 객체로 변환하는 매핑 함수

    findById(String id){} : Optional<Calc>
            => CALC_ID 조건으로 단일 행 조회
    findAll(){} : List<Calc>
            => 전체 계산 기록 조회 (CALC_ID 오름차순)
    save(Calc calc){} : void
            => 계산 결과를 TBL_CALC 에 INSERT (SEQ_CALC.NEXTVAL 로 ID 자동 생성)
    findLastCalculate(){} : Optional<Calc>
            => 가장 마지막 계산 기록 1건 조회 (CALC_ID 내림차순 FETCH 1)
}
```
