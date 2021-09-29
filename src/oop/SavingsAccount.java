package oop;

public class SavingsAccount extends Account {

	public SavingsAccount(String inputName, int inputBalance, String inputId) {
		super(inputName, inputBalance, inputId);
	}
	
	@Override
	String getType() {
		// TODO Auto-generated method stub
		return "savings";
	}

}