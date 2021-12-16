package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay14 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day14.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		String puzzle = lines.get(0);
		Map<String, Character> rules = new HashMap<>();
		for (int i=2; i<lines.size(); i++) {
			String[] parts = lines.get(i).split(" -> ");
			rules.put(parts[0],parts[1].charAt(0));
		}

		// puzzle 1 - iterate the puzzle 10 steps, then subtract # of times least common element occurs from # of times most common element occurs
		//		this was simulation approach 
		for (int i=0; i<10; i++) {
			puzzle = step(puzzle, rules);
		}
		int[] maxmin = findmaxmin(puzzle);
		System.out.println(maxmin[0]-maxmin[1]);		

		// puzzle 2 - can't iterate 40 steps using simulation method from puzzle1 because 
		// Java cannot represent strings as long as they would grow to, so we need new strategy:
		//     all we really need is a count of how many times each character occurs ... which means counting how many "new" instances of each character are introduced each step
		//     the strings are going to get wayyy too long, but SOME of the rules (perhaps all?) seem to loop ... for example see the output of the code below (you will need to uncomment it)
//		puzzle = "NB";
//		for (int i=0; i<7; i++) {
//			System.out.println(puzzle);
//			puzzle = step(puzzle, rules);
//		}
		// once we "hit" one of these repeats ... we only want to calculate how many of the repeating characters get added after X number of steps ... but DON'T add those huge strings to the puzzle

		// alternative for puzzle2: recursion yet again ... passing along the data structure with the global list of counts
		puzzle = lines.get(0); // reset the puzzle for the step 2 solution
		Map<Character, Long> counts = new HashMap<>();
		// let's pre-load all the counts so we don't have to do an "IF" statement anywhere in the recursion (should speed things up a bit)
		for (String line : lines)
			for (int ci=0; ci<line.length(); ci++) {
				char c = line.charAt(ci);
				if (c != ' ' && c != '-' && c != '>')
					counts.put(c, 0L);
			}
		
		puzzle = lines.get(0); // reset for puzzle2
		
		// count the initial puzzle letters b/c afterwards we will just be counting the new letters that get added
		for (int i=0; i<puzzle.length(); i++)
			counts.put(puzzle.charAt(i), 1L);
		
		for (int i=0; i<puzzle.length()-1; i++) {
			Map<Character, Long> localcounts = countcharsR(puzzle.charAt(i)+""+puzzle.charAt(i+1), 0, 40, rules);
			// update global counts with the local counts
			for (Map.Entry<Character,Long> entry : localcounts.entrySet())
	        	counts.put(entry.getKey(), counts.get(entry.getKey())+entry.getValue());

			for (Map.Entry<Character,Long> entry : counts.entrySet()) {
				System.out.println(entry.getKey() + " - " + entry.getValue());
	        }
			System.out.println();
		}
		
		long maxval = Long.MIN_VALUE;
		long minval = Long.MAX_VALUE;
        for (Map.Entry<Character,Long> entry : counts.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
			if (entry.getValue()>maxval)
				maxval = entry.getValue();
			if (entry.getValue()<minval)
				minval = entry.getValue();
        }
        System.out.println(maxval-minval);		
		
	}
	
	// dynamic programming data structure
	// 	  key is a string which is concatenation of pairstr + "_" + depth + "_" + maxdepth
	//    value is the character counts that will be obtained by recursing to the maxdepth
	private static Map<String, Map<Character, Long>> dp = new HashMap<>();
	
	// dynamic programming approach revisited ... this one is a bit more complex b/c we have to keep track of multiple values
	//    our strategy of passing a shared data structure won't work then ... so we need to ... but we need to "cache" the result of a given recursive call
	private static Map<Character, Long> countcharsR(String pairstr, int depth, int maxdepth, Map<String, Character> rules) {
		// base case plus skip recursion completely case (dp "hit")
		if (depth==maxdepth || dp.containsKey(pairstr + "_" + depth + "_" + maxdepth))
			return dp.get(pairstr + "_" + depth + "_" + maxdepth);
		
		Character rule = rules.get(pairstr);
		Map<Character, Long> localcounts = new HashMap<>();
		localcounts.put(rule, 1L);

		// now recurse on the left and right pairs that will be created by the rule
		Map<Character, Long> localcounts1 = countcharsR(new String(new char[] {pairstr.charAt(0),rule}), depth+1, maxdepth, rules);
		Map<Character, Long> localcounts2 = countcharsR(new String(new char[] {rule,pairstr.charAt(1)}), depth+1, maxdepth, rules);
		
		// add the returns from localcounts1 and localcounts2 into our counts
		if (localcounts1 != null)
	        for (Map.Entry<Character,Long> entry : localcounts1.entrySet()) {
	        	Long initialcount = localcounts.get(entry.getKey());
	        	long initcount = (initialcount==null) ? 0 : initialcount;
				localcounts.put(entry.getKey(), initcount + entry.getValue());
	        }
				
		// add the returns from localcounts1 and localcounts2 into our counts
		if (localcounts2 != null)
	        for (Map.Entry<Character,Long> entry : localcounts2.entrySet()) {
	        	Long initialcount = localcounts.get(entry.getKey());
	        	long initcount = (initialcount==null) ? 0 : initialcount;
				localcounts.put(entry.getKey(), initcount + entry.getValue());
	        }
		
		// store the total local counts into the DP table
		dp.put(pairstr + "_" + depth + "_" + maxdepth, localcounts);
		
		// return the total local counts
		return localcounts;
	}

	private static int count(char c, String puzzle) {
		int cnt = 0;
		for (int i=0; i<puzzle.length(); i++)
			if (puzzle.charAt(i)==c)
				cnt++;
		return cnt;
	}
	
	private static int[] findmaxmin(String puzzle) {
		int[] maxmin = new int[2];
		maxmin[0] = Integer.MIN_VALUE; // init max position to min possible value to ease the loop
		maxmin[1] = Integer.MAX_VALUE; // init min position to max possible value to ease the loop
		ArrayList<Character> chars = new ArrayList<>();
		ArrayList<Integer> charcnts = new ArrayList<>();
		for (int i=0; i<puzzle.length(); i++) {
			char c = puzzle.charAt(i);
			int loc = chars.indexOf(c);
			if (loc>=0)
				charcnts.set(loc, charcnts.get(loc)+1);
			else {
				chars.add(c);
				charcnts.add(1);
			}				
		}
		for (int cnt : charcnts) {
			if (cnt>maxmin[0])
				maxmin[0] = cnt;
			if (cnt<maxmin[1])
				maxmin[1] = cnt;
		}
		return maxmin;
	}
	
	// Pair contains the pair String and the location of this occurrence of the Pair ... we can't store all locations for each Pair b/c we need to process the locations in order later 
	private static ArrayList<String> findpairs(String puzzle) {
		ArrayList<String> pairs = new ArrayList<>();
		for (int i=0; i<puzzle.length()-1; i++) {
			String p = puzzle.charAt(i) + "" + puzzle.charAt(i+1);
			pairs.add(p);
		}
		return pairs;
	}

	private static String step(String puzzle, Map<String,Character> rules) {
		ArrayList<String> pairs = findpairs(puzzle);
		int erri = 0;
		for (int i=0; i<pairs.size(); i++) {
			String p = pairs.get(i);
			Character rule = rules.get(p);
			puzzle = insertstr(puzzle, i+erri+1, rule); // the +1 is b/c technically the char nees to be inserted after the first char in the pair
			erri++; // every insert will mess up the original locations by 1
		}
		return puzzle;
	}

	private static String insertstr(String puzzle, int i, char str) {
		return puzzle.substring(0, i) + str + puzzle.substring(i);
	}

}
