package oop;

abstract public class Account {
	
	protected String name; 
	protected int balance;
	protected String id;

	public Account(String inputName, int inputBalance, String inputId) {
		name = inputName;
		balance = inputBalance;
		id = inputId;
	}

	public void deposit(int amount) {
		balance += amount;
	}

	public void withdraw(int amount) {
		balance -= amount;
	}
	
	public String getName() {
		return name;
	}
	
	public void getInfo() {
		System.out.println("This " + getType() + " account belongs to " + name + ". It has " + balance + " dollars in it.");
	}
	
	abstract String getType();

	
	
	
}
