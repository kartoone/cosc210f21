package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay21 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day21.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	private static Map<String, long[]> dp = new HashMap<>(); // store recursive results so you don't have to recurse again 

	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		String parts1[] = lines.get(0).split(":");
		int p1 = Integer.parseInt(parts1[1].trim())-1; // spots are really 0-9 but they are numbered 1-10
		String parts2[] = lines.get(1).split(":");
		int p2 = Integer.parseInt(parts2[1].trim())-1;
		int p1score = 0;
		int p2score = 0;
		int dieroll = 1;
		int movecount = 0;
		while (true) {
			// break out once one of the scores exceeds 1000
			int m1 = dieroll++ + dieroll++ + dieroll++;
			movecount += 3;
			p1 = (p1 + m1)%10;
			p1score += (p1+1);
			if (p1score>=1000)
				break;
			int m2 = dieroll++ + dieroll++ + dieroll++;
			movecount += 3;
			p2= (p2 + m2)%10;
			p2score += (p2+1);
			if (p2score>=1000)
				break;
		}
		System.out.println(movecount*Math.min(p1score, p2score)); // puzzle 1 solution
		
		// simply reset for puzzle 2 since it is pretty much completely different
		p1 = Integer.parseInt(parts1[1].trim())-1; // spots are really 0-9 but they are numbered 1-10
		p2 = Integer.parseInt(parts2[1].trim())-1;
		p1score = 0;
		p2score = 0;
		long[] wins = countWins(p1, p2, p1score, p2score);
		System.out.println(Math.max(wins[0], wins[1])); // puzzle 2 solution
	}
	
	// top level recursion before anyone has moved ... this is called after all six helper recursive calls
	private static long[] countWins(int p1, int p2, int p1score, int p2score) {		
		if (dp.containsKey(p1+"-"+p2+"-"+p1score+"-"+p2score)) {
			return dp.get(p1+"-"+p2+"-"+p1score+"-"+p2score);
		}
		long wins[] = new long[] {0,0};
		for (int d1=1; d1<=3; d1++)
			for (int d2=1; d2<=3; d2++)
				for (int d3=1; d3<=3; d3++) 
					if (p1score+1+(p1+d1+d2+d3)%10<21) // only make player2 play if player 1 didn't just win
						for (int d4=1; d4<=3; d4++)
							for (int d5=1; d5<=3; d5++)
								for (int d6=1; d6<=3; d6++)
									if (p2score+1+(p2+d4+d5+d6)%10<21) {
										long[] recwins = countWins((p1+d1+d2+d3)%10, (p2+d4+d5+d6)%10, p1score+1+(p1+d1+d2+d3)%10, p2score+1+(p2+d4+d5+d6)%10); // only keep going if neither helper was a winner
										wins[0] = wins[0] + recwins[0];
										wins[1] = wins[1] + recwins[1];
									}
									else
										wins[1] = wins[1] + 1;
					else
						wins[0] = wins[0] + 1;		
		dp.put(p1+"-"+p2+"-"+p1score+"-"+p2score, wins);
		return wins;
		// 1+1+1=3		
//		countWins((p1+3)%10,p2, p1score+1+(p1+3)%10, p2score);
//		
//		// 1+1+2=4
//		countWins((p1+4)%10,p2, p1score+1+(p1+4)%10, p2score);
//		
//		// 1+1+3=5
//		countWins((p1+5)%10,p2, p1score+1+(p1+5)%10, p2score);
//		
//		// 1+2+1=4
//		countWins((p1+4)%10,p2, p1score+1+(p1+4)%10, p2score);
//
//		// 1+2+2=5
//		countWins((p1+5)%10,p2, p1score+1+(p1+5)%10, p2score);
//
//		// 1+2+3=6
//		countWins((p1+6)%10,p2, p1score+1+(p1+6)%10, p2score);
//
//		// 1+3+1=5
//		countWins((p1+5)%10,p2, p1score+1+(p1+5)%10, p2score);
//
//		// 1+3+2=6
//		countWins((p1+6)%10,p2, p1score+1+(p1+6)%10, p2score);
//
//		// 1+3+3=7
//		countWins((p1+7)%10,p2, p1score+1+(p1+7)%10, p2score);
//
//		// 2+1+1=4
//		countWins((p1+4)%10,p2, p1score+1+(p1+4)%10, p2score);
//
//		// 2+1+2=5
//		countWins((p1+5)%10,p2, p1score+1+(p1+5)%10, p2score);
//
//		// 2+1+3=6
//		countWins((p1+6)%10,p2, p1score+1+(p1+6)%10, p2score);
//
//		// 2+2+1=5
//		countWins((p1+5)%10,p2, p1score+1+(p1+5)%10, p2score);
//
//		// 2+2+2=6
//		countWins((p1+6)%10,p2, p1score+1+(p1+6)%10, p2score);
//
//		// 2+2+3=7
//		countWins((p1+7)%10,p2, p1score+1+(p1+7)%10, p2score);
//
//		// 2+3+1=6
//
//		// 2+3+2=7
//		countWins((p1+7)%10,p2, p1score+1+(p1+7)%10, p2score);
//
//		// 2+3+3=8
//		
//		// 3+1+1=5
//		
//		// 3+1+2=6
//		
//		// 3+1+3=7
//		
//		// 3+2+1=6
//		
//		// 3+2+2=7
//		
//		// 3+2+3=8
//		
//		// 3+3+1=7
//		
//		// 3+3+2=8
//		
//		// 3+3+3=9		
	}
	
}
