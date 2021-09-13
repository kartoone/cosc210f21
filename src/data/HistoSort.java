package data;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

// Histo - short for histogram 
public class HistoSort {

	// remember, all java programs start with a main() method inside a class
	// also, the FileNotFoundException must either be handled or thrown, it's less code if we just "throw" it, but it makes our program less robust because it doesn't handle the error where the specified file is not found
	public static void main(String[] args) throws FileNotFoundException {

		// create a Scanner object named filein that we can use to read through a file
		Scanner filein = new Scanner(new File("test.txt"));
		
		// create a native java array named counts[] because we know exactly how many elements we need and what to initialize them to
		int counts[] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

		// we DON'T know how many numbers are going to be in the file, so it makes sense to use
		// an ArrayList instead of a native java array. The ArrayList named allTheDigits will grow
		// dynamically as we add items to it
		ArrayList<Integer> allTheDigits = new ArrayList<>();
		
		// note the while loop here ... remember that scanners almost always use while loops
		while (filein.hasNextLine()) {
			int digit = filein.nextInt();
			allTheDigits.add(digit);
			counts[digit] = counts[digit] + 1;
		}
		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " occurred " + counts[i] + " times.");
		}
		
		// note that ArrayList has a very handy "toString()" method that gets called automatically
		// by the println method and displays a nicely formatted list of all its data without us
		// having to use a for loop
		System.out.println(allTheDigits);
		
		// sort the list ... null means use "natural" ordering for the elements in the list
		allTheDigits.sort(null);

		// display our sorted list
		System.out.println(allTheDigits);
		
	}
	
	// we will add two static sort methods below... one for the native array
	// and one for an ArrayList during class on Wednesday!

}
