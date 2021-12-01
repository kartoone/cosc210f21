package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay1 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1();
		puzzle2(3);
	}

	private static void puzzle1() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day1a.txt"));
		Integer last = null;
		int increasedcnt = 0;
		while (filein.hasNextLine()) {
			int current = Integer.parseInt(filein.nextLine());
			if (last!=null && current>last) {
				increasedcnt++;
			}
//			System.out.print(last + " " + current);
			last = current;
			
		}
		filein.close();
		System.out.println(increasedcnt);		
	}
		
	private static void puzzle2(int windowsize) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day1a.txt"));
		ArrayList<Integer> nums = new ArrayList<>();
		while (filein.hasNextLine()) {
			nums.add(Integer.parseInt(filein.nextLine()));
		}
		filein.close();
		Integer lastsum = null;
		int increasedcnt = 0;
		for (int i=2; i<nums.size(); i++) {
			int currentsum = nums.get(i-2) + nums.get(i-1) + nums.get(i);
			if (lastsum != null && currentsum>lastsum) {
				increasedcnt++;
			}
			lastsum = currentsum;
		}
		System.out.println(increasedcnt);		
	}
	
	
}
