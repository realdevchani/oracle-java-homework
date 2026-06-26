package com.example.calc;


public class Consumer {

    //    request :: "1 + 3"
    public Calc doService(String requestInput) {
        Calculator calculator = new Calculator();

        String[] arr = requestInput.trim().split("\\s+");

        Calc calc = Calc.builder()
                .calcNumber1(Integer.parseInt(arr[0]))
                .calcNumber2(Integer.parseInt(arr[2]))
                .calcOperator(arr[1])
                .build();

        Calc calculate = calculator.calculate(calc);
        return calculate;
    }
}
