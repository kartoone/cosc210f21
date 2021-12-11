package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AdventDay11 {
	
	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day11.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	// total flashes
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		int grid[][] = new int[10][10];
		for (int r=0; r<grid.length; r++) {
			String line = lines.get(r);
			for (int c=0; c<grid[0].length; c++)
				grid[r][c] = line.charAt(c)-48;  // convert from ascii to decimal
		}
		int totalflashes = 0;
		for (int i=0; i<1000; i++) { // change this back to 100 for puzzle 1 ... based on the example, it looked like 1000 would be more than enough steps to get them all to synchronize
			totalflashes += advance(grid);
			
			// puzzle 2 check for entire "zeroed-out" grid
			// none of the code in the rest of the "100" for loop needs to exist for puzzle 1, but it will work even if you leave it in
			boolean allsynced = true;
			for (int r=0; r<grid.length; r++)
				for (int c=0; c<grid[0].length; c++)
					allsynced = allsynced && grid[r][c]==0;
			if (allsynced) {
				System.out.println(i+1);
				break;
			}
			// end of puzzle2 code
		}
		
	
		System.out.println(totalflashes);
	}

	private static int advance(int grid[][]) {
		// create a new temporary flash grid to keep track of who has/hasn't flashed yet (can't flash multiple times in one step)
		int flashgrid[][][] = new int[grid.length][grid[0].length][2];

		// first increment everybody by 1
		for (int r=0; r<grid.length; r++)
			for (int c=0; c<grid[0].length; c++) {
				grid[r][c] = grid[r][c] + 1;
				flashgrid[r][c] = new int[] {grid[r][c], 0};
			}

		// now use the recursive helper to potentially flash a bunch of cells
		int total = 0;
		for (int r=0; r<grid.length; r++) 
			for (int c=0; c<grid[0].length; c++)
				total+=flash(flashgrid, r, c);
		
		// at the very end ... copy all the final flashed cell values back into the original grid
		for (int r=0; r<grid.length; r++) 
			for (int c=0; c<grid[0].length; c++)
				if (flashgrid[r][c][0]>9) {
					grid[r][c]=0;
				} else {
					grid[r][c]=flashgrid[r][c][0];
				}
		return total;				
	}

	// base case: already flashed ... OR ... the grid cell doesn't have enough energy
	// recursive step: increment all the neighbor cells including the diagonals and attempt to flash them all (won't work if they don't have enough energy after being incremented) 	
	private static int flash(int[][][] flashgrid, int r, int c) {
		if (r<0||c<0||r==flashgrid.length||c==flashgrid[r].length||flashgrid[r][c][1]==1||flashgrid[r][c][0]<=9)
			return 0; // simply ignore illegal checks or if we've already been flashed to easily handle the borders
		// go ahead and mark ourselves as flashed if we haven't been flashed yet
		flashgrid[r][c] = new int[] { 10, 1 }; // it doesn't matter what number we put for our cell energy b/c we are going to get reset back to 0  
		int count = 1;
		incrementcell(flashgrid, r-1, c+1);
		incrementcell(flashgrid, r-1, c-1);
		incrementcell(flashgrid, r-1, c);
		incrementcell(flashgrid, r, c-1);
		incrementcell(flashgrid, r, c+1);
		incrementcell(flashgrid, r+1, c-1);		
		incrementcell(flashgrid, r+1, c);		
		incrementcell(flashgrid, r+1, c+1);

		// now attempt to flash all our neighbors ... increment the count of any that might have gotten flashed
		for (int nr=-1; nr<=1; nr++) 
			for (int nc=-1; nc<=1; nc++)
				count+=flash(flashgrid, r+nr, c+nc);
		
		return count;
	}

	// increment the cell energy no matter what (even if already flashed) as long as it is valid cell index
	private static void incrementcell(int[][][] grid, int r, int c) {
		if (r<0||c<0||r==grid.length||c==grid[r].length)
			return; // simply ignore illegal checks to easily handle the borders
		grid[r][c][0] = grid[r][c][0]+1;
	}

	// helpful for debugging ... deleted all the lines that used this while i was developing the solution
	private static void display(int grid[][]) {
		for (int r=0; r<grid.length; r++) {
			for (int c=0; c<grid[0].length; c++)
				System.out.print(grid[r][c]<10?grid[r][c]:0);
			System.out.println();
		}	
	}	

}
