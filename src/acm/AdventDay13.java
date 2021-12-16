package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AdventDay13 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day13.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		int maxx = Integer.MIN_VALUE;
		int maxy = Integer.MIN_VALUE;
		
		// Pass 1 - figure out how big to make our paper grid
		for (String line: lines) {
			String parts[] = line.split(",");
			if (parts.length<2)
				break; // must have reached the fold instructions ... we will deal with that later
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			if (x>maxx)
				maxx = x;
			if (y>maxy)
				maxy = y;
		}
		
		int grid[][] = new int[maxy+1][maxx+1]; // y is technically the "row" and x is technically the "col"
		
		// Pass 2 - populate the correct grid cells with a "1" ... 
		for (String line: lines) {
			String parts[] = line.split(",");
			if (parts.length<2)
				break; // must have reached the fold instructions ... we will deal with that later
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			grid[y][x] = 1;			
		}
		
		displaygrid(grid);
		
		// Pass 3 - start folding! ... puzzle 1 only do the first fold
		boolean firstfold = true;
		for (String line: lines) {
			if (line.equals(""))
				continue;
			String parts[] = line.split(",");
			if (parts.length==2)
				continue; // skip all the grid coords again to get to the folds
			
			grid = processFoldline(line, grid);
			if (firstfold) {
				System.out.println(countSquares(grid));
				firstfold = false;
			}
		}
		
		// print the answer 
		displaygrid(grid);
	}
	
	private static int countSquares(int[][] grid) {
		int total = 0;
		for (int y=0; y<grid.length; y++)
			for (int x=0; x<grid[0].length; x++)
				total += (grid[y][x]>0?1:0);			
		return total;
	}

	private static int[][] processFoldline(String line, int[][] grid) {
		String realline = line.substring(11);
		String parts[] = realline.split("=");
		char fold = parts[0].charAt(0);
		int amount = Integer.parseInt(parts[1]);
		// lol, the line that gets folded gets completely removed from the grid
		// easiest way to do this is return a new grid
		// if folding on "y" then number of columns (i.e., x) will stay the same otherwise number of rows will stay same (better just to handle everything in two separate branches)
		if (fold == 'y') {
			int newgrid[][] = new int[amount][grid[0].length];
			// simply copy everything from the old grid into the new grid up to the fold line
			for (int y=0; y<amount; y++)
				for (int x=0; x<grid[0].length; x++)
					newgrid[y][x] = grid[y][x];
			
			// starting after the fold line, calc distance away from fold line and subtract that from amt and then add original grid cell to that spot
			for (int y=amount+1; y<grid.length; y++)
				for (int x=0; x<grid[0].length; x++)
					newgrid[amount-(y-amount)][x] = newgrid[amount-(y-amount)][x] + grid[y][x];
			
			return newgrid;
		} else {
			int newgrid[][] = new int[grid.length][amount];
			// simply copy everything from the old grid into the new grid up to the fold line
			for (int y=0; y<grid.length; y++)
				for (int x=0; x<amount; x++)
					newgrid[y][x] = grid[y][x];
			
			// starting after the fold line, calc distance away from fold line and subtract that from amt and then add original grid cell to that spot
			for (int y=0; y<grid.length; y++)
				for (int x=amount+1; x<grid[0].length; x++)
					newgrid[y][amount-(x-amount)] = newgrid[y][amount-(x-amount)] + grid[y][x];
			
			return newgrid;
		}
	}

	public static void displaygrid(int grid[][]) {
		for (int y=0; y<grid.length; y++) {
			for (int x=0; x<grid[0].length; x++)
				System.out.print(grid[y][x]==0?'.':'#');
			System.out.println();
		}
	}
	
}
