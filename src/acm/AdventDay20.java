package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay20 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day20.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		String enhancer = lines.get(0);
		// go ahead and determine initial grid size
		int rows = lines.size()-2;
		int cols = lines.get(2).length();
		int puzzle[][] = new int[rows][cols];
		
		// read in the initial puzzle (converting to 1s and 0s)
		for (int r=0; r<puzzle.length; r++) {
			String line = lines.get(r+2); // offset by 2 since the first two lines already consumed 
			for (int c=0; c<puzzle[0].length; c++) {
				puzzle[r][c] = line.charAt(c)=='#' ? 1 : 0;
			}
		}

		displaypuzzle(puzzle);
		System.out.println();
		
		// enhance the image twice
		for (int i=0; i<50; i++) {
			puzzle = enhance(puzzle, enhancer,i);
			displaypuzzle(puzzle);
			System.out.println();
		}
		
		// puzzle1 answer: total number of "lit" cells in final enhanced image
		int litcnt = 0;
		for (int r=0; r<puzzle.length; r++)
			for (int c=0; c<puzzle[0].length; c++)
				if (puzzle[r][c]==1)
					litcnt++;
		
		System.out.println(litcnt);
	}

	private static int[][] enhance(int[][] puzzle, String enhancer,int step) {
		// expand the puzzle by expanding out by 1 row and 1 col on all sides
		int newpuzzle[][] = new int[puzzle.length+2][puzzle[0].length+2];
		
		// copy current puzzle into the expanded newpuzzle
		for (int r=1; r<newpuzzle.length-1; r++)
			for (int c=1; c<newpuzzle[0].length-1; c++)
				newpuzzle[r][c] = puzzle[r-1][c-1];
		
		displaypuzzle(newpuzzle);
		System.out.println();

		// apply the algorithm to every cell (injecting 0s for the cells that are out of bounds ... i.e., infinite direction
		for (int r=0; r<newpuzzle.length; r++)
			for (int c=0; c<newpuzzle[0].length; c++)
				newpuzzle[r][c] = enhancepixel(r,c,puzzle,enhancer,step%2);
		
		// finally return the new puzzle
		return newpuzzle;
	}

	// r, c - coords in the new puzzle (must remember to offset them by 1 into the original puzzle)
	private static int enhancepixel(int r, int c, int[][] puzzle, String enhancer, int step) {
		int hood[][] = new int[3][3];
		for (int hr=-1; hr<=1; hr++) {
			for (int hc=-1; hc<=1; hc++) {
				if (hr+r<=0 || hc+c<=0 || hr+r-1>=puzzle.length || hc+c-1>=puzzle[0].length)
					hood[hr+1][hc+1] = step;
				else
					hood[hr+1][hc+1] = puzzle[hr+r-1][hc+c-1];
//				if (r==3&&c==3)
//					System.out.print(hood[hr+1][hc+1]);
			}
//			if (r==3&&c==3)
//				System.out.println();
		}		
		String hoodstr = "";
		for (int hr=0; hr<3; hr++)
			for (int hc=0; hc<3; hc++)
				hoodstr += hood[hr][hc];		
		int eindex = Integer.parseInt(hoodstr,2);
//		if (r==3&&c==3)
//			System.out.println(eindex);
		return enhancer.charAt(eindex)=='#'?1:0;
	}

	private static void displaypuzzle(int[][] puzzle) {
		for (int r=0; r<puzzle.length; r++) {
			for (int c=0; c<puzzle[0].length; c++)
				System.out.print(puzzle[r][c]==0?'.':'#');
			System.out.println();
		}
	}

}
