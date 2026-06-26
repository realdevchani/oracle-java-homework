package com.example.calc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class Calculator {


//    기존에 있는 넘버 + 연산자 에 해당 실행 결과값만을 담아주고 해당 Calc 객체를 리턴하는 매서드
    public Calc calculate(Calc calc) {
        int num1 = calc.getCalcNumber1();
        int num2 = calc.getCalcNumber2();
        String operator = calc.getCalcOperator();

        int result = switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> {
                if (num2 == 0) {
                    log.warn("0으로 나눌 수 없음");
                    throw new ArithmeticException("0으로 나눌 수 없습니다.");
                }
                yield num1 / num2;
            }
            default -> throw new IllegalArgumentException("지원하지 않는 연산자입니다: " + operator);
        };

        calc.setCalcResult(result);
        return calc;
    }


    public List<Calc> getListFromResultSet(ResultSet rs) throws SQLException {
        List<Calc> list = new ArrayList<>();

        while (rs.next()) {
            Calc calc = Calc.builder()
                    .calcId(rs.getLong("CALC_ID"))
                    .calcNumber1(rs.getInt("CALC_NUM1"))
                    .calcNumber2(rs.getInt("CALC_NUM2"))
                    .calcOperator(rs.getString("CALC_OPERATOR"))
                    .calcResult(rs.getInt("CALC_RESULT"))
                    .build();

            list.add(calc);
        }

        return list;
    }

}
