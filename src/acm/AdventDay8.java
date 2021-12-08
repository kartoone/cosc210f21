package acm;

import java.io.File;
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
		//puzzle1();
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
		
		int grandtotal = 0;
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
			positions[0] = diffstr(decoded[7], decoded[1]).charAt(0); 

			// Determine position 6 (bottom position) as follows:
			//  ALL the digits except 1, 4, and 7 have the bottom row filled in
			//  Figure out which letter is in common to all the other digits and isn't position 0, 1 or 3, 2 or 5 ... i.e., isn't any of the chars in 4 and also isn't the char we have determined is in position 0, essentially UNION of digit 4 and digit 7
			//  Sometimes there will be two letters different and sometimes only one ... in EACH of the cases where there is only one letter diff it's always the bottom one
			String p01325 = union(decoded[4],decoded[7]);
			for (String p : e.patterns) {
				if (!p.equals(decoded[1])&&!p.equals(decoded[4])&&!p.equals(decoded[7])) {
					String bdiff = diffstr(p, p01325);					
					if (bdiff.length()==1) {
						positions[6] = bdiff.charAt(0);
					} else if (bdiff.length()==2 && positions[6]!=0) {
						// the two letter differences also are always just "e" and "g" ... so any two letter differences will reveal "e" if we take out "g"
						positions[4] = diffstr(bdiff, ""+positions[6]).charAt(0);
					}
				}
			}
			
			// 9 is the only 6 char string that doesn't have position[4]
			for (String p : e.patterns) {
				if (p.length()==6 && p.indexOf(positions[4])<0)
					decoded[9] = p;					
			} 
			
			// At this point there are only TWO six char strings left ... 0 and 6
			// If you diff the chars from 1 ... the string length for the digit that is 0 should be 4 ... but for the other it will be 5
			System.out.println("Finding 0");
			System.out.println("decoded[9]: " + decoded[9]);
			for (String p : e.patterns) {
				if (p.length()==6 && !p.equals(decoded[9])) {
					System.out.println("p: " + p);
					System.out.println(diffstr(p, decoded[1]));
					if (diffstr(p, decoded[1]).length()==4) {
						decoded[0] = p;					
					}
				}
			}
			
			// Now there is only one six char string left - the "6" digit
			for (String p : e.patterns) {
				if (p.length()==6 && !p.equals(decoded[9]) && !p.equals(decoded[0]))
					decoded[6] = p;					
			}
			
			// Finally we can figure out the top right char (i.e., position 2) by diffing decoded[0] and decoded[6]
			positions[2] = diffstr(decoded[8], decoded[6]).charAt(0);
			
			// Digit 3 is the only digit that has position 2 and NOT position 4 is 5 chars long
			for (String p : e.patterns) {
				if (p.length()==5 && p.indexOf(positions[4])<0 && p.indexOf(positions[2])>=0)
					decoded[3] = p;					
			}
			
			// Digit 2 is the only digit that has positions 2 and position 4 and is also 5 chars long
			for (String p : e.patterns) {
				if (p.length()==5 && p.indexOf(positions[4])>=0 && p.indexOf(positions[2])>=0)
					decoded[2] = p;					
			}
			
			// Digit 5 is the only digit that is missing positions 2 and position 4 and is also 5 chars long
			for (String p : e.patterns) {
				if (p.length()==5 && p.indexOf(positions[4])<0 && p.indexOf(positions[2])<0)
					decoded[5] = p;					
			}

			// all strings should be decoded at this point
			// now determine the digits in the puzzle
			String fourdigit = "";
			for (String p : e.puzzle) {
				fourdigit += getDigit(p,decoded);
			}
			grandtotal += Integer.parseInt(fourdigit);
		}
		System.out.println(grandtotal);
	}

	private static String getDigit(String p, String[] decoded) {
		for (int i=0; i<decoded.length; i++)
			if (p.equals(decoded[i]))
				return ""+i;
		return "not found";
	}

	static String union(String str1, String str2) {
		String unionstr = str1;
		for (int i=0; i<str2.length(); i++) {
			if (!unionstr.contains(""+str2.charAt(i)))
				unionstr += str2.charAt(i);
		}
		return unionstr;
	}
	
	static String diffstr(String str1, String str2) {
		String diffstr = "";
		for (int i=0; i<str1.length(); i++)
			if (!str2.contains(""+str1.charAt(i)))
				diffstr += str1.charAt(i);
		return diffstr;
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
