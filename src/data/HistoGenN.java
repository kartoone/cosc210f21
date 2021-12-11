package data;

import java.io.*;
import java.util.Random;

public class HistoGenN {

	public static void main(String[] args) throws FileNotFoundException {
		// This object below allows us to write all our random numbers to a file using println statements
		PrintWriter out = new PrintWriter(new File("testsort.txt"));
		PrintWriter out2 = new PrintWriter(new File("testsearch.txt"));
		
		// This object below allows us to easily generate random numbers between 0 and the specified upper bound (10), exclusive
		Random r = new Random();
		
		// 1,000,000 was way too slow for our insertion sort algorithm, so I trimmed it back to 50,000
		for (int i=1; i<50000; i++) {
			out.println(r.nextInt(10));
		}
		// the purpose of line 15 is to address the hanging comma problem ... note that it is "print" instead of "println"
		out.print(r.nextInt(10));
		out.close();

		// 1,000,000 should be no problem for searching
		// plus we've expanded the range of possible random numbers all the way out to 250000 (exclusive)
		for (int i=1; i<1000000; i++) {
			out2.println(r.nextInt(250000));
		}
		out2.print(r.nextInt(250000));
		out2.close();	
}
	
	// Another UNRELATED example of the hanging comma problem ... SOLVED
	public static void hangingComma() {
		int nums[] = new int[] {3, 4, 5, 6, 7, 8, 0};
		for (int i=0; i<nums.length-1; i++) {
			System.out.print(nums[i] + ",");   // Notice for these numbers within the array that we want to include a comma after each number
		}
		System.out.print(nums[nums.length-1]); // Note how we use print (or println) here to display the last number of the array without a comma
	}

}