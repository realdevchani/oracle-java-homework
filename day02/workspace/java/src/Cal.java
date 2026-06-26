public class Cal {
	int op1;
	String op;
	int op2;	
	
	Cal(String[] args) {
		this.op1 = Integer.parseInt(args[0]);
		this.op = args[1];
		this.op2 = Integer.parseInt(args[2]);		
	}
	
	void doService() {
		if(op.equals("add")) {
			System.out.println(op1 + op2);
		} else if(op.equals("sub")) {
			System.out.println(op1 - op2);
		} else if(op.equals("mul")) {
			System.out.println(op1 * op2);
		} else if(op.equals("div")) {
			System.out.println(op1 / op2);
		} 
	}	
}
