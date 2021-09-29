package oop;

public class CheckingAccount extends Account {

	// constructor
	//   initialize all our protected data
	public CheckingAccount(String inputName, int inputBalance, String inputId) {
		super(inputName, inputBalance, inputId);
	}

	@Override
	String getType() {
		// TODO Auto-generated method stub
		return "checking";
	}


}