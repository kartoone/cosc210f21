package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay6 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1(80);
		puzzle2(80);
	}

	private static void puzzle1(int d) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day6.txt"));
		ArrayList<Integer> fish = new ArrayList<>();
		filein.useDelimiter(",");
		while (filein.hasNext()) {
			fish.add(Integer.parseInt(filein.next()));
		}
		filein.close();

		// procreate for d days
		for (int i=0; i<d; i++) {
			ArrayList<Integer> swaparoo = new ArrayList<>();
			for (int fi=0; fi<fish.size(); fi++) {
				Integer f = fish.get(fi);
				if (f==0) {
					swaparoo.add(8);
					swaparoo.add(6);
				} else {
					swaparoo.add(f-1);
				}
			}
			fish = swaparoo;
		}
		System.out.println(fish.size());
	}
	
	// can't use same super easy to write approach as puzzle1 b/c you'd run out of memory trying to store a list that large
	// in this approach we will caclulate at the time of first reproduction how many fish that first fish AND its offspring will reproduce 
	private static void puzzle2(int d) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day6.txt"));
		ArrayList<int[]> fish = new ArrayList<int[]>();
		filein.useDelimiter(",");
		while (filein.hasNext()) {
			fish.add(new int[] {Integer.parseInt(filein.next()), 80});
		}
		filein.close();

		// procreate for d days
		long total = 0;
		for (int i=0; i<d; i++) {
			
		}
	}	
	
}
