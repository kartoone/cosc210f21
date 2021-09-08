package basics;

// note that we need two imports here (if yours only shows one, make sure you expand the + sign to see the other one

import java.util.Scanner;
import java.io.*;

public class CenturyCalculator {

	// typical main method with one addition: the "throws FileNotFoundException"
	// we will cover Exceptions in more detail soon, but for now, just know that
	// if something goes wrong with opening a File, this takes care of properly handling that
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Welcome to the Age Calculator");
		System.out.print("Enter the filename: ");
		Scanner in = new Scanner(System.in);  	// our FIRST scanner
											  	//	- its name is "in"
												//	- it reads from the keyboard (System.in)
		String filename = in.nextLine(); // use the first scanner to read the filename from the keyboard
		
		Scanner filein = new Scanner(new File(filename));	// our SECOND scanner
															// 	- its name is "filein"
															//  - it read from the specified file
		
		// our goal here is to read in a bunch of ages and calculate how old those people will be at the turn of the century
		// ALSO, we are calculating the average age out of all the ages.
		// The simplest way to calculate an average value is to sum up all the values and divide by the number of values
		int total = 0;	// the sum of all our ages
		int count = 0;  // the count of all our ages 
		while (filein.hasNextLine()) {
			count++; // same as count = count + 1;
			int age = filein.nextInt();
			total = total + age;  // total += age;
			System.out.println("you will be " + ageAtTurnOfTheCentury(age) + " at the turn of the century.");
		}
		
		// calculate the average ... note that total and count are both integers, so we need to include (double) to force the division to preserve the remainder as a fraction example 3.25
		double avg = (double) total / count;
		System.out.println("average age: " + avg);
	}

	// this function must be "static" because it is called from main() which itself is a static method
	// note the return type of "int" ... also note the parameter "age" is also an int
	public static int ageAtTurnOfTheCentury(int age) {
		int yearsUntil2100 = 2100-2021;
		return age + yearsUntil2100;
	}
	
	

}
