package basics;

public class Initials {
	  
	public static void main(String[] args) {
    
    // Add a first name and a last name:
    String firstName = "Brian";  
    String lastName = "Toone";
    
    // What are the initials?
    // incorrect: (String type is wrong!)
    //String firstInitial = firstName.charAt(0);
    
    // correct: (char type is correct) 
    char firstInitial = firstName.charAt(0);


    System.out.println(firstName.charAt(0));
    System.out.println(lastName.charAt(0));
    
  }
  
}