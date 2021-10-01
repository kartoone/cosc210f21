package oop;

abstract public class Account {
	
	// Attributes (i.e., data) inherited by all subclasses (CheckingAccount, SavingsAccount)
	protected String name; 
	protected int balance;
	protected String id;

	public Account(String inputName, int inputBalance, String inputId) {
		name = inputName;
		balance = inputBalance;
		id = inputId;
	}

	// Methods inherited by all subclasses
	public void deposit(int amount) {
		balance += amount;
	}

	public void withdraw(int amount) {
		balance -= amount;
	}
	
	public String getName() {
		return name;
	}
	
	// POLYMORPHISM - comes from the root words meaning "many" and "change"
	//    the call to getType() is a polymorphic call because that one method call could cause us to jump to one of "many" places and it could "change" each time!
	public void getInfo() {
		System.out.println("This " + getType() + " account belongs to " + name + ". It has " + balance + " dollars in it.");
	}
	
	// This method is abstract because there is no notion of a default account type in this imaginary banking system.
	// It is up to each specialized subclass to provide an implementation for it 
	abstract protected String getType();
	
}
