package data;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.Map;

// Histo - short for histogram 
// new and improved to demonstrate the Java version of a Python dictionary (which is TreeMap)
public class HistoDictionary {

	// remember, all java programs start with a main() method inside a class
	// also, the FileNotFoundException must either be handled or thrown, it's less code if we just "throw" it, but it makes our program less robust because it doesn't handle the error where the specified file is not found
	public static void main(String[] args) throws FileNotFoundException {

		// create a Scanner object named filein that we can use to read through a file
		Scanner filein = new Scanner(new File("test.txt"));
		
		// create a TreeMap (i.e., java dictionary) named counts because we DON'T know exactly how many elements we need and what to initialize them to
		TreeMap<Integer, Integer> counts = new TreeMap<>();

		// we DON'T know how many numbers are going to be in the file, so it makes sense to use
		// an ArrayList instead of a native java array. The ArrayList named allTheDigits will grow
		// dynamically as we add items to it
		ArrayList<Integer> allTheDigits = new ArrayList<>();
		
		// note the while loop here ... remember that scanners almost always use while loops
		while (filein.hasNextLine()) {
			int digit = filein.nextInt();
			allTheDigits.add(digit);
			if (counts.containsKey(digit)) {
				int oldcount = counts.get(digit);
				counts.put(digit, oldcount+1);
			} else {
				counts.put(digit, 1);
			}
		}

		System.out.println("NOT user friendly display of the counts");
		System.out.println(counts);
		
		System.out.println("User friendly display of the counts");
		for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
			System.out.println(entry.getKey() + " occured " + entry.getValue() + " time(s)");
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

}
