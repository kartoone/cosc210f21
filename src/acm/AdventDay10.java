package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AdventDay10 {
	
	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day10.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		ArrayList<String> incompletelines = new ArrayList<>(); // puzzle2 is interested in the "score" require to complete all the incomplete lines
		int totalscore = 0; // puzzle1 is just interested in the total score of the lines with syntax errors... 
		for (String line: lines) {
			int score = stackDetector(line);
			if (score==0)
				// we are told that if the line doesn't have a syntax error then it IS incomplete (but we should build it so we could actually work with correct lines)
				incompletelines.add(line);
			else
				totalscore += score;
		}
		System.out.println(totalscore);
		ArrayList<Long> incompletescores = new ArrayList<>();
		for (String incomplete: incompletelines) {
			incompletescores.add(scoreIncomplete(incomplete));
		}
		Collections.sort(incompletescores);
		System.out.println();
		System.out.println(incompletescores.get(incompletescores.size()/2)); // print out the "median" score for the answer to puzzle 2
	}

	// puzzle2 - we can use same approach with stack ... simply use the weird scoring system on whatever is left in the stack
	// the scores get HUGE so you have to use a long to make sure you don't have overflow
	private static long scoreIncomplete(String line) {
		ArrayList<Character> openstack = stackDetector2(line);
		long score = 0;
		for (int ci=openstack.size()-1; ci>=0; ci--) {
			score = score * 5;
			char nc = openstack.get(ci);
			switch (nc) {
				case '(': score += 1; break;
				case '[': score += 2; break;
				case '{': score += 3; break;
				case '<': score += 4; break;
				default: System.err.println("unexpected next char: " + nc);			
			}
		}
		return score;
	}
	
	// basic approach: go char by char ... if opening character push onto stack ... if closing character it should match whatever is on top of stack
	private static ArrayList<Character> stackDetector2(String line) {
		ArrayList<Character> openstack = new ArrayList<>();
		for (int i=0; i<line.length(); i++) { 
			char next = line.charAt(i);
			if (isOpening(next))
				openstack.add(next);
			else if (!closingMatch(openstack.remove(openstack.size()-1), next)) {
				return null;			
			}
		}
		return openstack;
	}

	// basic approach: go char by char ... if opening character push onto stack ... if closing character it should match whatever is on top of stack
	private static int stackDetector(String line) {
		ArrayList<Character> openstack = new ArrayList<>();
		for (int i=0; i<line.length(); i++) { 
			char next = line.charAt(i);
			if (isOpening(next))
				openstack.add(next);
			else if (!closingMatch(openstack.remove(openstack.size()-1), next)) {
				return scoreError(next);			
			}
		}
		// no errors
		return 0;
	}

	private static boolean isOpening(char c) {
		return c=='('||c=='['||c=='{'||c=='<';
	}
	
	private static boolean closingMatch(char opening, char next) {
		switch (next) {
		case ')': return opening=='(';
		case ']': return opening=='[';
		case '}': return opening=='{';
		case '>': return opening=='<';
		default:
			System.err.println("unexpected next char: " + next);			
		}
		return false;
	}

	private static int scoreError(char err) {
		switch (err) {
		case ')': return 3;
		case ']': return 57;
		case '}': return 1197;
		case '>': return 25137;
		}
		return 0;			
	}

	// not working ... not being used right now
	private static int linei;
	private static int parseLine(String line) {
		linei=0;
		// kick up the recursion with the first opening character
		char opening = line.charAt(linei++);
		char errchar = parseChunk(opening, line);
		if (errchar!=0)
			System.out.println("Found error char: " + errchar);
		// if we made it to here, then we successfully parsed the entire string and there were no errors
		return 0;
	}	
	private static char parseChunk(char opening, String line) {
		while(linei < line.length() ) {
			// go ahead and consume the next character and see if it is the proper closing
			char next = line.charAt(linei++);
			if (next == '(' || next == '[' || next == '{' || next == '<') {
				// recursively parse if it was an opening
				char errchar = parseChunk(next, line);
				if (errchar!=0)
					return errchar;
			} else {
				if (!closingMatch(opening, next))
					return next; // we found the error char ... return it
			}
			// do nothing otherwise, except look for the next char to see if we need to parse another chunk			
		}
		return 0;		
	}


	
}
