package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay15 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day15.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		int rows = lines.size();
		int cols = lines.get(0).length();
		int grid[][] = new int[rows][cols];
		for (int i=0; i<rows; i++) {
			String line = lines.get(i);
			for (int j=0; j<cols; j++) {
				grid[i][j] = line.charAt(j)-48; // convert from ascii to integer
			}
		}
		
		// djikstras shortest path algorithm to build minimum spanning tree ... and calculate distance along the way to each node
		int spt[][][] = djikstra(grid);
		System.out.println(spt[rows-1][cols-1][0]);
		
		// puzzle 2 is simply the shortest path again using a "weirdly" expanded grid
		int expgrid[][] = expandGrid(grid);
		spt = djikstra(expgrid); // this takes about 5 minutes to run (I'm glad I didn't give up on it!)
		System.out.println(spt[spt.length-1][spt[0].length-1][0]);		
	}

	private static int[][] expandGrid(int[][] grid) {		
		int rowsubgrid[][] = new int[grid.length][grid[0].length];
		for (int r=0; r<grid.length; r++)
			for (int c=0; c<grid[0].length; c++)
				rowsubgrid[r][c] = grid[r][c];

		int newgrid[][] = new int[grid.length*5][grid[0].length*5];
		for (int ro=0; ro<5; ro++) {
			int colsubgrid[][] = new int[grid.length][grid[0].length];
			for (int r=0; r<grid.length; r++)
				for (int c=0; c<grid[0].length; c++)
					colsubgrid[r][c] = rowsubgrid[r][c];
			for (int co=0; co<5; co++) {
				for (int r=0; r<grid.length; r++)
					for (int c=0; c<grid[0].length; c++)
						newgrid[r+ro*grid.length][c+co*grid[0].length] = colsubgrid[r][c];
				// now update the colsubgrid by 1
				updateSubgrid(colsubgrid);
			}
			// now update the rowsubgrid by 1
			updateSubgrid(rowsubgrid);
		}
		return newgrid;
	}

	private static void updateSubgrid(int[][] grid) {
		for (int r=0; r<grid.length; r++)
			for (int c=0; c<grid[0].length; c++)
				grid[r][c] = (grid[r][c]==9?1:grid[r][c]+1);						
	}

	// djikstra's shortest path algorithm ... frequent example for greedy alogrithm
	// basically you are building a minimum spanning tree ... always choosing the next node to add to the tree as the one with the shortest "distance" from existing node in tree
	private static int[][][] djikstra(int[][] grid) {
		int spt[][][] = new int[grid.length][grid[0].length][2];
		
		// init our spanning tree ... this is massive graph with connections between all adjacent nodes so
		// rather than trying to keep track of edges ... simply store the "risk level" (i.e., cost) in position[0] of 3rd dimension and whether or not it is a member of the spanning tree yet at a 0 (no) or 1 (yes) in position 2 of the third dimension
		for (int r=0; r<spt.length; r++)
			for (int c=0; c<spt[0].length; c++) {
				spt[r][c][0] = Integer.MAX_VALUE;
				spt[r][c][1] = 0;
			}
				
		spt[0][0][0] = 0; // this will end up being first min dist vertex for alg below
		
		for (int count=0; count<spt.length*spt[0].length; count++) {
			// we just have to repeat this algorithm COUNT number of times (which is total number of vertices)
			// we don't actually use the "count" index
			int[] coord = findMin(spt); // returns row in position 0 and col in position 1
			int r = coord[0];
			int c = coord[1];
			spt[r][c][1] = 1;
			
			// update the neighbors of (i, j) - which should have already been updated (either from previous recursive call ... or from bootstrap before first call)
			// use helper method so we don't have to do crazy bounds checking here on each direction
			update(spt[r][c][0], r-1, c, spt, grid);
			update(spt[r][c][0], r, c-1, spt, grid);
			update(spt[r][c][0], r, c+1, spt, grid);
			update(spt[r][c][0], r+1, c, spt, grid);
		}
		return spt;		
	}

	private static int[] findMin(int[][][] spt) {
		int min = Integer.MAX_VALUE;
		int coords[] = new int[2];
		for (int r=0; r<spt.length; r++)
			for (int c=0; c<spt[0].length; c++)
				if (spt[r][c][1]==0 && spt[r][c][0]<min) {
					coords[0] = r;
					coords[1] = c;
					min = spt[r][c][0];
				}
		return coords;
	}

	private static void update(int pathcost, int r, int c, int[][][] spt, int[][] grid) {
		if (r>=0 && c>=0 && r<spt.length && c<spt[0].length && pathcost+grid[r][c] < spt[r][c][0]) {
			spt[r][c][0] = pathcost + grid[r][c];		
		}
	}
	
}
