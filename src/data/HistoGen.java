package data;

import java.io.*;
import java.util.Random;

public class HistoGen {

	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("test.txt"));
		Random r = new Random();
		for (int i=1; i<1000000; i++) {
			out.println(r.nextInt(10));
		}
		// the purpose of line 15 is to address the hanging comma problem
		out.print(r.nextInt(10));
		out.close();
	}
	
	public static void hangingComma() {
		int nums[] = new int[] {3, 4, 5, 6, 7, 8, 0};
		for (int i=0; i<nums.length-1; i++) {
			System.out.print(nums[i] + ",");
		}
		System.out.println(nums[nums.length-1]);
	}

}
