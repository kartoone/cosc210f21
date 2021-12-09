package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AdventDay9 {
	
	static class Lowpoint implements Comparable {
		
		protected int[] lpdata;
		
		public Lowpoint(int[] lpdata) {
			this.lpdata = lpdata;
		}
		
		@Override
		public int compareTo(Object o) {
			Lowpoint other = (Lowpoint) o;
			return 0-((Integer)lpdata[3]).compareTo(other.lpdata[3]);
		}
		
		@Override
		public String toString() {
			return java.util.Arrays.toString(lpdata);
		}
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1();
		puzzle2();
	}

	private static void puzzle1() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		// first determine the grid dimensions and allocate 2D int
		int rows = lines.size();
		int cols = lines.get(0).length();
		int grid[][] = new int[rows][cols];
		for (int i=0; i<rows; i++) {
			String line = lines.get(i);
			for (int j=0; j<cols; j++) {
				grid[i][j] = line.charAt(j)-48;
			}
		}
		
		// now find all the low points AND go ahead and calculate and add the low point score to the total
		int lptotal = 0;
		for (int r=0; r<rows; r++) {
			for (int c=0; c<cols; c++) {
				int tr=r-1;
				int tc=c;
				int lr=r;
				int lc=c-1;
				int rr=r;
				int rc=c+1;
				int br=r+1;
				int bc=c;
				int depth = grid[r][c];
				if (isLower(depth, tr, tc, grid) &&
					isLower(depth, lr, lc, grid) &&
					isLower(depth, rr, rc, grid) &&
					isLower(depth, br, bc, grid)) {
					lptotal = lptotal + depth + 1;
				}
			}
		}
		System.out.println(lptotal);
	}

	private static void puzzle2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		// first determine the grid dimensions and allocate 2D int
		int rows = lines.size();
		int cols = lines.get(0).length();
		int grid[][] = new int[rows][cols];
		for (int i=0; i<rows; i++) {
			String line = lines.get(i);
			for (int j=0; j<cols; j++) {
				grid[i][j] = line.charAt(j)-48;
			}
		}
		
		// now find all the low points and their basins first ... we will sort the basins later
		ArrayList<Lowpoint> lowpoints = new ArrayList<>();
		for (int r=0; r<rows; r++) {
			for (int c=0; c<cols; c++) {
				int tr=r-1;
				int tc=c;
				int lr=r;
				int lc=c-1;
				int rr=r;
				int rc=c+1;
				int br=r+1;
				int bc=c;
				int depth = grid[r][c];
				if (
						isLower(depth, tr, tc, grid) &&
						isLower(depth, lr, lc, grid) &&
						isLower(depth, rr, rc, grid) &&
						isLower(depth, br, bc, grid) 
				   ) {					
					lowpoints.add(new Lowpoint(new int[] {r,c, grid[r][c], findBasin(r,c,grid)}));
				}
			}
		}
		
		// answer to puzzle 2 is the product of the SIZE of the three largest basins ... so sort by basin size 
		Collections.sort(lowpoints);
		
//		for (Lowpoint lp : lowpoints) {
//			System.out.println(lp);
//		}
		
		// print out the product of the sizes
		System.out.println(lowpoints.get(0).lpdata[3]*lowpoints.get(1).lpdata[3]*lowpoints.get(2).lpdata[3]); 
	}

	// recursive approach ... the key to solving this one is doing it recursively AND checking that you haven't already included a given coordinate as part of the basin
	//   my first thought was to try the "walking" approach but we don't need to do that b/c we are already INSIDE the basin
	//   since we are already inside, we can just recursively check the neighbors and add them to the basin if the aren't already there
	//   avoid infinite recursion by making sure they aren't already there. when a given branch hits a dead end, it simply stops
	//   meanwhile there are a bunch of other recursive branches that are going to end up finding all the members of the basin!
	private static int findBasin(int r, int c, int[][] grid) {
		// first ... expand the grid and surround it completely with 9s so that we don't have to worry about going out of bounds
		int newgrid[][] = new int[grid.length+2][grid[0].length+2];
		for (int i=0; i<newgrid.length; i++) {
			for (int j=0; j<newgrid[0].length; j++) {
				if (i==0 || j==0 || i==newgrid.length-1 || j==newgrid[0].length-1)
					newgrid[i][j] = 9;
				else
					newgrid[i][j] = grid[i-1][j-1];
			}
		}		
		ArrayList<int[]> basinCoords = new ArrayList<>();
		basinCoords.add(new int[] {r,c});
//		printNearbygrid(10, r+1, c+1, newgrid);
		fbHelper(r+1, c+1, newgrid, basinCoords);
//		System.out.println(basinCoords.size());
		return basinCoords.size();
	}
	
	private static void fbHelper(int r, int c, int[][] grid, ArrayList<int[]> basinCoords) {
		// try north first
		if (grid[r-1][c]!=9 && !containsArray(basinCoords, new int[] {r-1,c})) {
			basinCoords.add(new int[] {r-1, c});
			fbHelper(r-1, c, grid, basinCoords);
		} 
		// try west next
		if (grid[r][c-1]!=9 && !containsArray(basinCoords, new int[] {r,c-1})) {
			basinCoords.add(new int[] {r, c-1});
			fbHelper(r, c-1, grid, basinCoords);
		}  
		// try east next
		if (grid[r][c+1]!=9 && !containsArray(basinCoords, new int[] {r,c+1})) {
			basinCoords.add(new int[] {r, c+1});
			fbHelper(r, c+1, grid, basinCoords);
		}  
		// try south last 
		if (grid[r+1][c]!=9 && !containsArray(basinCoords, new int[] {r+1,c})) {
			basinCoords.add(new int[] {r+1, c});
			fbHelper(r+1, c, grid, basinCoords);
		}  
	}
	
	private static boolean containsArray(ArrayList<int[]> list, int[] coords) {
	    return list.stream().anyMatch(a -> java.util.Arrays.equals(a, coords));
	}

	
	private static void printNearbygrid(int sq, int r, int c, int[][]grid) {
		System.out.println(r + " " + c + " " );
		for (int i=r-sq; i<r+sq; i++) {
			for (int j=c-sq; j<c+sq; j++) {
				if (i>=0 && j>=0 && i<grid.length && j<grid.length)
					System.out.print(grid[i][j]);
			}
			if (i>=0 && i<grid.length)
				System.out.println();
		}
		System.out.println();
	}

	private static boolean isLower(int depth, int r, int c, int[][] grid) {
		if (r>=0 && r<grid.length && c>=0 && c<grid[0].length)
			return depth<grid[r][c];
		else
			return true;
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day9.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
}
