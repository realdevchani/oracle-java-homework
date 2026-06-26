package com.example.calc;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class RunTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Consumer consumer = new Consumer();
        CalculationRepo repo = new CalculationRepoImpl();

        try {
            while (true) {
                System.out.println();
                System.out.println("==== 계산기 ====");
                System.out.println("번호를 선택하세요.");
                System.out.println("1. 계산하기");
                System.out.println("2. ID로 계산 기록 찾기");
                System.out.println("3. 전체 계산 기록 보기");
                System.out.println("4. 마지막 계산 기록 보기");
                System.out.println("0. 종료");
                System.out.print("선택: ");

                int menu = sc.nextInt();
                sc.nextLine();

                if (menu == 0) {
                    System.out.println("종료");
                    break;
                }

                try {
                    if (menu == 1) {
                        System.out.print("계산식을 입력하세요 (예: 1 + 3): ");
                        String input = sc.nextLine();

                        Calc calc = consumer.doService(input);
                        repo.save(calc);

                        System.out.println("결과: " + calc);

                    } else if (menu == 2) {
                        System.out.println("조회할 ID를 입력하세요.");
                        System.out.print("CALC_ID: ");

                        String calcId = sc.nextLine();

                        Optional<Calc> result = repo.findById(calcId);

                        Calc calc = result.orElseThrow(() -> new ClassNotFoundException("Calc class 조회 실패"));

                        log.info(calc.toString());

                    } else if (menu == 3) {
                        List<Calc> list = repo.findAll();

                        if (list.isEmpty()) {
                            System.out.println("계산 기록이 없습니다.");
                        } else {
                            System.out.println(list);
                        }

                    } else if (menu == 4) {
                        Optional<Calc> result = repo.findLastCalculate();

                        Calc calc = result.orElseThrow(() -> new ClassNotFoundException("Calc 클래스 조회 실패"));

                        log.info(calc.toString());

                    } else {
                        System.out.println("잘못된 번호입니다.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("숫자 형식이 잘못되었습니다.");
                } catch (ArithmeticException e) {
                    System.out.println("계산 오류: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("입력 오류: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("알 수 없는 오류가 발생했습니다.");
                    e.printStackTrace();
                }
            }
        } finally {
            sc.close();
        }
    }
}
