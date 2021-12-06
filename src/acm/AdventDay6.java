package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay6 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1(80);
		puzzle2(256);
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
			ArrayList<Integer> swaparoo = new ArrayList<>(); // need to do this to avoid concurrent modification (i.e., adding to the same list while still iterating through it)
			for (int fi=0; fi<fish.size(); fi++) {
				Integer f = fish.get(fi);
				if (f==0) {
					swaparoo.add(8); 	// create the new fish
					swaparoo.add(6); 	// put the original fish back into the "pool" but with its cycle count reset to 6
				} else {
					swaparoo.add(f-1); 	// put the original fish back into the "pool" with its cycle count decremented by 1 day
				}
			}
			fish = swaparoo; // perform the "swap" after we have populated a new generation of fish
		}
		System.out.println(fish.size());
	}
	
	// can't use same super easy to write approach as puzzle1 b/c you'd run out of memory trying to store a list that large
	// in this approach we use dynamic programming to store answers once they are calculated (i.e., given fish f with d number of days left)
	// so that we don't have to recursively do the calculation again
	private static void puzzle2(int d) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day6.txt"));
		ArrayList<Integer> fish = new ArrayList<>();
		filein.useDelimiter(",");
		while (filein.hasNext()) {
			fish.add(Integer.parseInt(filein.next()));
		}
		filein.close();

		// dynamic programming storage array
		//  1st dimension (rows) is 0-8 (i.e., the cycle day)
		//  2nd dimension (cols) is 0-d (i.e., how many days left)
		// the way to interpret answers[f][d] is that whatever answer is at that location is 
		// how many total fish will spawn from that one fish and its offspring 
		// given the initial cycle day of "f" and "d" days left in the simulation
		long answers[][] = new long[9][d+1];		
		long total = 0;
		for (Integer f: fish) {
			total += countOffspring(f, d, answers);			
		}
		System.out.println(total);

	}
	
	// recursive function
	//	base case: days == 0 (i.e., we have run out of days)
	//  recursive steps:
	//		special: fish is at day 0 of its cycle and reproduces a new fish
	//		normal:  fish is NOT at day 0 yet, so just keeps swimming along one day closer to reproducing
	//  progression:
	//		note the "d-1" in all the recursive calls so that we are progressing towards d==0
	// 		the meaning of "d-1" is we have advanced another day and have one less day until the end of the simulation
	public static long countOffspring(int f, int d, long answers[][]) {
		// not exactly a base case ... simply avoid recursion altogether
		if (answers[f][d]!=0)
			return answers[f][d];

		long ans = 0;
		if (d==0) 
			ans = 1; // technically, the fish doesn't get counted until the very end of days
		else if (f==0) {
			// special recursive step where we not only keep going with "f" resetting to 6 ... we also create a new fish with "f" set to 8
			ans += countOffspring(8, d-1, answers);
			ans += countOffspring(6, d-1, answers);
		} else {
			// normal recursive step where we just keep going one day closer "f-1" to reproducing with "d-1" days left overall
			ans += countOffspring(f-1, d-1, answers);			
		}
		answers[f][d] = ans; // update the answer in our answers array before returning it
		return ans;

	}
	
}
