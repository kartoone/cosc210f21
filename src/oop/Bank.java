package oop;

public class Bank {
	
	final int MAXACCOUNTS = 50;
	private CheckingAccount checkingAccounts[];
	private SavingsAccount savingsAccounts[];
	private int numCheckingAccounts;
	private int numSavingAccounts;
	
	// constructor
	//   go ahead and create the first two checking accounts
	//   as well as the first savings account
	public Bank() {
		checkingAccounts = new CheckingAccount[MAXACCOUNTS];
		savingsAccounts = new SavingsAccount[MAXACCOUNTS];
		addCheckingAccount("Zeus", 100, "1");
		addCheckingAccount("Hades", 200, "2");
		addSavingsAccount("ZeusSavings", 10000, "1");
	}
	
	protected void addCheckingAccount(String name, int initialBalance, String id) {
		checkingAccounts[numCheckingAccounts++] = new CheckingAccount(name, initialBalance, id);
	}

	protected void addSavingsAccount(String name, int initialBalance, String id) {
		savingsAccounts[numSavingAccounts++] = new SavingsAccount(name, initialBalance, id);
	}
	
	public static void main(String[] args) {
		Bank bankOfGods = new Bank();
		System.out.println(bankOfGods.checkingAccounts[0].name);
		bankOfGods.checkingAccounts[0].deposit(5);
		bankOfGods.checkingAccounts[0].getInfo(); 
		bankOfGods.savingsAccounts[0].deposit(25);
		bankOfGods.savingsAccounts[0].getInfo(); 
		bankOfGods.savingsAccounts[0].withdraw(15);
		bankOfGods.savingsAccounts[0].getInfo(); 
	}

}