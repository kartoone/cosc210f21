package oop;

public class SavingsAccount extends Account {

	public SavingsAccount(String inputName, int inputBalance, String inputId) {
		super(inputName, inputBalance, inputId);
	}
	
	@Override
	protected String getType() {
		return "savings";
	}

}