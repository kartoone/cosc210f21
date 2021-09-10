package data;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Histo {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("test.txt"));
		int counts[] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		ArrayList<Integer> allTheDigits = new ArrayList<>();
		while (filein.hasNextLine()) {
			int digit = filein.nextInt();
			allTheDigits.add(digit);
			counts[digit] = counts[digit] + 1;
		}
		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " occurred " + counts[i] + " times.");
		}
		System.out.println(allTheDigits);
		
	}

}
