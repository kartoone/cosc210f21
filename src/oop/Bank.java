package oop;

public class Bank {
	final int MAXACCOUNTS = 50;
	private CheckingAccount checkingAccounts[];
	private SavingsAccount savingsAccounts[];
	private int numCheckingAccounts;
	private int numSavingAccounts;
	public Bank() {
		checkingAccounts = new CheckingAccount[MAXACCOUNTS];
		savingsAccounts = new SavingsAccount[MAXACCOUNTS];
		System.out.println(checkingAccounts.length);
		checkingAccounts[0] = new CheckingAccount("Zeus", 100, "1");
		checkingAccounts[1] = new CheckingAccount("Hades", 200, "2");
		savingsAccounts[0] = new SavingsAccount("ZeusSavings", 10000, "1");
		numCheckingAccounts = 2;
		numSavingAccounts = 1;
	}

	public static void main(String[] args) {
		Bank bankOfGods = new Bank();
		System.out.println(bankOfGods.checkingAccounts[0].name);
		bankOfGods.checkingAccounts[0].deposit(5);
		bankOfGods.checkingAccounts[0].getInfo(); 

	}

}