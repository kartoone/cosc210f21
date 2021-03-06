package basics;

import java.util.Scanner;

public class HelloWorld {

	public static void main(String[] args) {
		System.out.println("hello world");
		System.out.print("What is your name? ");
		Scanner in = new Scanner(System.in);
		String name = in.nextLine();
		System.out.println("hi there " + name);
		System.out.print("How old are you? ");
		int age = in.nextInt();
		System.out.println("you will be " + ageAtTurnOfTheCentury(age) + " at the turn of the century.");
		System.out.println(recursive(1));
		System.out.println(recursive(2));
		System.out.println(recursive(3));
		System.out.println(recursive(4));
		System.out.println(recursive(5));
	}
	
	public static int ageAtTurnOfTheCentury(int age) {
		int yearsUntil2100 = 2100-2021;
		return age + yearsUntil2100;
	}

	// fibonacci yo
	public static int recursive(int n) {
		  if (n<=2)
		    return 1;
		  else 
		    return recursive(n-2) + recursive(n-1);
	}

}
