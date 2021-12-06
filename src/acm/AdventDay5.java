package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay5 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	// Simplest approach (but not fastest) - two pass algorithm
	// First pass - determine number of rows and columns for the grid
	// Second pass - increment the number of overlapping lines in each grid cell as each new line is re-read from the file
	private static void puzzle1and2() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day5.txt"));
		// pass 1: determine grid size
		int rows = 0;
		int cols = 0;
		while (filein.hasNextLine()) {
			String line = filein.nextLine();
			String parts[] = line.split("->");
			String scoords1[] = parts[0].trim().split(",");
			String scoords2[] = parts[1].trim().split(",");
			int coords1[] = new int[] {Integer.parseInt(scoords1[0]),Integer.parseInt(scoords1[1])};
			int coords2[] = new int[] {Integer.parseInt(scoords2[0]),Integer.parseInt(scoords2[1])};
			if (coords1[1]>rows)
				rows = coords1[1];
			if (coords2[1]>rows)
				rows = coords2[1];
			if (coords1[0]>cols)
				cols = coords1[0];
			if (coords2[0]>cols)
				cols = coords2[0];
		}
		filein.close();

		// allocate the grid data structure to be the correct size
		rows++; // the input is inclusive and starts from 0 so total number of rows and cols is 1 greater than calculated by pass 1
		cols++; 
		int grid[][] = new int[rows][cols];

		// pass 2:
		filein = new Scanner(new File("day5.txt"));
		while (filein.hasNextLine()) {
			String line = filein.nextLine();
			String parts[] = line.split("->");
			String scoords1[] = parts[0].trim().split(",");
			String scoords2[] = parts[1].trim().split(",");
			int coords1[] = new int[] {Integer.parseInt(scoords1[0]),Integer.parseInt(scoords1[1])};
			int coords2[] = new int[] {Integer.parseInt(scoords2[0]),Integer.parseInt(scoords2[1])};
			// now update the grid for the line
			// avoid multiple for loops by putting the smaller coordinate into smaller and the larger into larger
			int smaller[] = null;
			int larger[] = null;
			int index = 1;
			if (coords1[0]==coords2[0]) {
				if (coords1[1]<=coords2[1]) {
					smaller = coords1;
					larger = coords2;
				} else {
					smaller = coords2;
					larger = coords1;
				}
			}
			if (coords1[1]==coords2[1]) {
				if (coords1[0]<=coords2[0]) {
					smaller = coords1;
					larger = coords2;
				} else {
					smaller = coords2;
					larger = coords1;
				}
				index = 0;
			}
			if (smaller!=null)
				for (int s = smaller[index]; s<=larger[index]; s++)
					if (index==1)
						grid[smaller[0]][s] += 1;
					else
						grid[s][smaller[1]] += 1;
			else {
				// the entirety of puzzle1 happens in the if clause above
				// so to get the puzzle1 answer you need to comment out this else clause!
				// this else clause accounts for the additional diagonal lines
				// let's just work directly with coords1 and coords2 and determine the increments
				// since 45 degree angle diff will be the same in both directions (x and y)
				int diff = Math.abs(coords2[0]-coords1[0])+1; // don't forget the +1 b/c it's inclusive on both ends
				int xinc = coords1[0]<coords2[0] ? 1 : -1;
				int yinc = coords1[1]<coords2[1] ? 1 : -1;
				int x=coords1[0];
				int y=coords1[1];
				for (int cnt=0; cnt<diff; cnt++) {
					grid[x][y] += 1;
					x += xinc;
					y += yinc;
				}
			}
		}
		
		// output: count the total number of squares which have at least two overlapping lines
		int count = 0;
		for (int r=0; r<rows; r++) {
			for (int c=0; c<cols; c++) {
				if (grid[r][c]>1)
					count++;
			}
		}
		System.out.println(count);		
	}
	
	
}
