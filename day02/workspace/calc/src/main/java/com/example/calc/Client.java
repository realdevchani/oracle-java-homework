package com.example.calc;

public class Client {
	Cal c;
	Client(String[] args) {
		c = new Cal(args);
	}
	
	void doService() {
		c.doService();
	}

}
