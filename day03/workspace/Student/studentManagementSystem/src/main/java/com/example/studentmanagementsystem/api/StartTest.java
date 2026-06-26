package com.example.studentmanagementsystem.api;

import com.example.studentmanagementsystem.domain.StudentDTO;
import com.example.studentmanagementsystem.domain.StudentVO;
import com.example.studentmanagementsystem.repository.StudentDAOImpl;
import com.example.studentmanagementsystem.service.StudentService;
import com.example.studentmanagementsystem.service.StudentServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class StartTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentServiceImpl(new StudentDAOImpl());

        while (true) {
            System.out.println();
            System.out.println("==== 학생 관리 시스템 ====");
            System.out.println("번호를 선택하세요.");
            System.out.println("1. 학생 등록");
            System.out.println("2. 학생 정보 변경");
            System.out.println("3. 학생 삭제");
            System.out.println("4. 전체 학생 조회");
            System.out.println("5. 학생 단건 조회");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int input = sc.nextInt();
            sc.nextLine();

            if (input == 0) {
                System.out.println("종료합니다.");
                break;
            }

            try {
                if (input == 1) {
                    System.out.print("이름: ");
                    String name = sc.nextLine();
                    System.out.print("이메일: ");
                    String email = sc.nextLine();
                    System.out.print("나이: ");
                    int age = sc.nextInt();
                    sc.nextLine();
                    System.out.print("전화번호: ");
                    String phone = sc.nextLine();
                    System.out.print("주소: ");
                    String address = sc.nextLine();

                    StudentVO studentVO = StudentVO.builder()
                            .studentName(name)
                            .studentEmail(email)
                            .studentAge(age)
                            .studentPhone(phone)
                            .studentAddress(address)
                            .build();

                    boolean result = studentService.register(studentVO);
                    log.info(result ? "등록 성공" : "등록 실패");

                } else if (input == 2) {
                    System.out.print("변경할 학생 ID: ");
                    long id = sc.nextLong();
                    sc.nextLine();
                    System.out.print("이름: ");
                    String name = sc.nextLine();
                    System.out.print("이메일: ");
                    String email = sc.nextLine();
                    System.out.print("나이: ");
                    int age = sc.nextInt();
                    sc.nextLine();
                    System.out.print("전화번호: ");
                    String phone = sc.nextLine();
                    System.out.print("주소: ");
                    String address = sc.nextLine();

                    StudentVO studentVO = StudentVO.builder()
                            .studentId(id)
                            .studentName(name)
                            .studentEmail(email)
                            .studentAge(age)
                            .studentPhone(phone)
                            .studentAddress(address)
                            .build();

                    studentService.update(studentVO);
                    log.info("변경 완료");

                } else if (input == 3) {
                    System.out.print("삭제할 학생 ID: ");
                    long id = sc.nextLong();
                    sc.nextLine();

                    studentService.delete(id);
                    log.info("삭제 완료");

                } else if (input == 4) {
                    List<StudentDTO> list = studentService.findAll();

                    if (list.isEmpty()) {
                        System.out.println("학생 기록이 없습니다.");
                    } else {
                        list.forEach(dto -> log.info(dto.toString()));
                    }

                } else if (input == 5) {
                    System.out.print("조회할 학생 ID: ");
                    long id = sc.nextLong();
                    sc.nextLine();

                    StudentDTO dto = studentService.findById(id);
                    log.info(dto.toString());

                } else {
                    System.out.println("잘못된 번호입니다.");
                }
            } catch (RuntimeException e) {
                log.warn("오류 발생: {}", e.getMessage());
            }
        }

        sc.close();
    }
}
