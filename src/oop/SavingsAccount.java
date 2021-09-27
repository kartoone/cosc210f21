package oop;

public class SavingsAccount {
	protected String name;
	protected int balance;
	protected String id;

	public SavingsAccount(String inputName, int inputBalance, String inputId) {
		name = inputName;
		balance = inputBalance;
		id = inputId;
	}

	public String getName() {
		return name;
	}
	
	public void deposit(int amount) {
		balance += amount;
	}

	public void withdraw(int amount) {
		balance -= amount;
	}
	
	public void getInfo() {
		System.out.println("This checking account belongs to " + name + ". It has " + balance + " dollars in it.");
	}

}