package com.example.calc;

public class Cal {
	int op1;
	String op;
	int op2;	
	int result;

	Cal(String[] args) {
		this.op1 = Integer.parseInt(args[0]);
		this.op = args[1];
		this.op2 = Integer.parseInt(args[2]);		
	}
	
	public void doService() {
        switch (op) {
            case "add" ->
                    result = op2 == 0 ?  op1 : op2 + op1;
            case "sub" ->
                    result = op2 == 0 ?  op1 : op2 - op1;
            case "mul" ->
                    result = op2 == 1 ?  op1 : op1 * op2;
            case "div" -> System.out.println(op1 / op2);
            default -> throw new CalException("Invalid operation");
        }
	}	
}
