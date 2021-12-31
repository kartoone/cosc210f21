package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class AdventDay25 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1(); // there is no puzzle 2 for day 25
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day25.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	static char grid[][];
	private static void puzzle1() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		grid = new char[lines.size()][lines.get(0).length()];
		for (int r=0; r<grid.length; r++) {
			String line = lines.get(r);
			for (int c=0; c<line.length(); c++)
				grid[r][c] = line.charAt(c);
		}
		boolean stationary1 = false;
		boolean stationary2 = false;
		int i = 0;
		while (!stationary1 || !stationary2) {
			stationary1 = moveEast();
			stationary2 = moveSouth();
			i++;
		}
		System.out.println(i);
	}

	private static boolean moveEast() {
		ArrayList<int[]> moves = new ArrayList<>();	
		// queue up all the moves for our "simultaneous" movement
		for (int r=0; r<grid.length; r++)
			for (int c=0; c<grid[0].length; c++)
				if (grid[r][c] == '>' && grid[r][(c+1)%grid[0].length] == '.')
					moves.add(new int[] {r, c});
		// now go ahead and do all the moves "at once"
		for (int[] move: moves) {
			int r = move[0];
			int c = move[1];
			grid[r][c] = '.'; // empty out the space they were on before
			grid[r][(c+1)%grid[0].length] = '>'; // move the cucumber to the new space
		}
		return moves.size()==0;
	}
	
	private static boolean moveSouth() {
		ArrayList<int[]> moves = new ArrayList<>();		
		// queue up all the moves for our "simultaneous" movement
		for (int r=0; r<grid.length; r++)
			for (int c=0; c<grid[0].length; c++)
				if (grid[r][c] == 'v' && grid[(r+1)%grid.length][c] == '.')
					moves.add(new int[] {r, c});
		// now go ahead and do all the moves "at once"
		for (int[] move: moves) {
			int r = move[0];
			int c = move[1];
			grid[r][c] = '.'; // empty out the space they were on before
			grid[(r+1)%grid.length][c] = 'v'; // move the cucumber to the new space
		}
		return moves.size()==0;
	}

	// original thought was to return two temp grids and then compare
	// but no need to do that, we can simply count number of moves made
	private static boolean charEquals(char[][] grid1, char[][] grid2) {
		for (int r=0; r<grid1.length; r++)
			for (int c=0; c<grid1.length; c++)
				if (grid1[r][c]!=grid2[r][c])
					return false;
		return true;
	}
	
}
