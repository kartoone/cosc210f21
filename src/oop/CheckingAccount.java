package oop;

public class CheckingAccount extends Account {

	// constructor
	//   initialize all our protected data
	public CheckingAccount(String inputName, int inputBalance, String inputId) {
		super(inputName, inputBalance, inputId);
	}

	@Override
	protected String getType() {
		return "checking";
	}


}