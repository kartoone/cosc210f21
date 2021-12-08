package acm;

import java.io.File;
import org.apache.commons.lang3.StringUtils;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay8 {
	
	static class Entry {
		public String[] patterns;
		public String[] puzzle;
		public Entry(String line) {
			String[] parts = line.split("\\|");
			patterns = parts[0].trim().split(" ");
			puzzle = parts[1].trim().split(" ");
			for (int i=0; i<patterns.length; i++) {
				patterns[i] = sortString(patterns[i]);
			}
			for (int i=0; i<puzzle.length; i++) {
				puzzle[i] = sortString(puzzle[i]);
			}
			System.out.println(java.util.Arrays.toString(patterns));
			System.out.println(java.util.Arrays.toString(puzzle));
		}
		public static String sortString(String inputString) {
	        // Converting input string to character array
	        char tempArray[] = inputString.toCharArray();
	 
	        // Sorting temp array using
	        java.util.Arrays.sort(tempArray);
	 
	        // Returning new sorted string
	        return new String(tempArray);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1();
		puzzle2();
	}

	private static void puzzle1() throws FileNotFoundException {
		ArrayList<Entry> entries = extractEntries();
		System.out.println("Processing " + entries.size() + " entry(s).");
		// for puzzle 1 ... we are only looking at the "puzzles" and we are only looking for occurrences of 1, 4, 7, 8
		// which are unique digits in that 1 takes 2 chars, 4 takes 4 chars, 7 takes 3 chars, and 8 takes all 7 chars
		int uniquedigits = 0;
		for (Entry e : entries) {
			for (String p : e.puzzle) {
				if (p.length()==2 || p.length()==4 || p.length()==3 || p.length()==7)
					uniquedigits++;
			}
		}
		System.out.println(uniquedigits);
	}

	private static void puzzle2() throws FileNotFoundException {
		ArrayList<Entry> entries = extractEntries();
		System.out.println("Processing " + entries.size() + " entry(s).");
		// for puzzle 2 ... we go all the way and use the 10 patterns to figure out which digit
		// let's start by updating our Entry class so that it alphabetizes each entry
		
		
		for (Entry e : entries) {
			String decoded[] = new String[10];
			// Let's store the decoded patterns in the array for EACH entry since it changes from entry to entry
			// Digit 0 corresponds to decoded[0], 1 corresponds to decoded[1], etc..
			for (String p : e.patterns) {
				if (p.length()==2)
					decoded[1] = p;
				else if (p.length()==4)
					decoded[4] = p;
				else if (p.length()==3)
					decoded[7] = p;
				else if (p.length()==7)
					decoded[8] = p;
			}
			
			// Now we can use the known patterns to start to determine the others
			char positions[] = new char[7];
			
			// First position we can determine is the char that represents top
			//  0000
			// 1    2
			// 1    2
			//  3333
			// 4    5
			// 4    5
			//  6666			
			
			// The "extra" char in decoded[7] has to be the top position
			positions[0] = StringUtils.difference(decoded[1], decoded[7]).charAt(0);

			// Determine position 6 (bottom position) by looking at digit 4 and 
			
		}
	}

	protected static ArrayList<Entry> extractEntries() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day8.txt"));
		ArrayList<Entry> entries = new ArrayList<>();
		while (filein.hasNextLine()) {
			entries.add(new Entry(filein.nextLine()));
		}
		filein.close();
		return entries;
	}
	
}
