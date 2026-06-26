package com.example.calc;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calc {

    private Long calcId;
    private int calcNumber1;
    private int calcNumber2;
    private String calcOperator;
    private int calcResult;

}