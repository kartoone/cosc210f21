package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay4 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	// Use an ArrayList of 3D arrays to keep track of all the boards
	// The meaning of the 3D array for a single board is the rows/columns are the first two dimensions and then the 3rd dimension is whether or not that cell has been marked yet
	private static void puzzle1and2() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day4.txt"));
		String nums[] = filein.nextLine().split(",");
		ArrayList<String[][][]> boards = new ArrayList<>(); // list of all boards
		ArrayList<Boolean> alreadywon = new ArrayList<>();  // list of which boards have already won so that we don't continue to mark up alreadywon boards
		String[][][] firstwinner = null;
		String[][][] lastwinner = null;
		int firstwinnernum = 0;								// gotta keep track of what number made the firstwinning board the winner ... could update checkWinner to return the score and negative value means didn't win yet ... oh well
		int lastwinnernum = 0;								// same for the last winning board
		String[][][] board = null;
		int boardi=0;
		while (filein.hasNextLine()) {
			String line = filein.nextLine();
			if (line.length()==0) {
				// empty string, must be getting ready to start a new board ... add the old board (if it exists) to the list of boards first
				if (board != null)
					boards.add(board);
				board = new String[5][][];
				boardi = 0;
			} else {
				board[boardi++] = new String[5][2];
				Scanner linescan = new Scanner(line);
				int bi=0;
				while(linescan.hasNext()) {
					String n = linescan.next();
					if (n.length()>0)
						board[boardi-1][bi++] = new String[] {n, "0"}; // 0 means unmarked						
				}
				linescan.close();
			}
		}
		filein.close();
		
		// populate alreadywon with a bunch of falses
		for (int i=0; i<boards.size(); i++)
			alreadywon.add(false);		

		// now let's start drawing numbers!
		String[][][] winningboard = null;
		for (String num: nums) {
			boardi=0;
			for (String[][][] b : boards) {
				if (!alreadywon.get(boardi++)) {
					winningboard = markAndCheckBoard(num, b);
					if (winningboard!=null) {
						if (firstwinner==null) {
							firstwinner = winningboard;
							firstwinnernum = Integer.parseInt(num);
						}
						lastwinner = winningboard;
						lastwinnernum = Integer.parseInt(num);
						alreadywon.set(boardi-1, true);
					}
				}
			}
		}		
		// puzzle1: the answer is the score of the first winner
		// puzzle2: the answer is the score of the last winner
		// print out the final board score
		displayBoard(firstwinner);
		System.out.println(scoreBoard(firstwinner, firstwinnernum));
		displayBoard(lastwinner);
		System.out.println(scoreBoard(lastwinner, lastwinnernum));
	}
	private static String[][][] markAndCheckBoard(String num, String[][][] b) {
		for (int r=0; r<b.length; r++) {
			for (int c=0; c<b[r].length; c++) {
				if (b[r][c][0].equals(num))
					b[r][c][1]="1";
			}
		}
		return checkWinner(b);
	}
	private static String[][][] checkWinner(String[][][] b) {
		// first check rows
		for (int r=0; r<b.length; r++) {
			boolean winner = true;
			for (int c=0; c<b[r].length; c++) {
				if (b[r][c][1].equals("0")) {
					winner = false;
					break;
				}
			}
			if (winner)
				return b;
		}
		// now check cols
		for (int r=0; r<b.length; r++) {
			boolean winner = true;
			for (int c=0; c<b[r].length; c++) {
				if (b[c][r][1].equals("0")) {
					winner = false;
					break;
				}
			}
			if (winner)
				return b;
		}
		return null;
	}
	private static int scoreBoard(String[][][] b, int num) {
		int sumunmarked = 0;
		for (int r=0; r<b.length; r++) {
			for (int c=0; c<b[r].length; c++) {
				if (b[r][c][1].equals("0")) {
					sumunmarked += Integer.parseInt(b[r][c][0]);
				}
			}
		}
		return sumunmarked*num;
	}
	private static void displayBoard(String[][][] b) {
		for (int r=0; r<b.length; r++) {
			for (int c=0; c<b[r].length; c++) {
				System.out.print("{"+b[r][c][0]+","+b[r][c][1]+"} ");
			}
			System.out.println();
		}
		System.out.println();
	}	
	
}
