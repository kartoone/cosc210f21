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
		
		Collections.sort(lowpoints);
		
		int lptotal = 0;
		for (Lowpoint lp : lowpoints) {
			System.out.println(lp);
			lptotal = lptotal + lp.lpdata[2] + 1;
		}
		System.out.println(lptotal);		
		System.out.println(lowpoints.get(0).lpdata[3]*lowpoints.get(1).lpdata[3]*lowpoints.get(2).lpdata[3]); 
	}

	// returns the size of the basin b/c that's all we need
	private static int findBasin(int r, int c, int[][] grid) {
		// first ... expand the grid and surround it completely with 9s so that we don't have to worry about going out of bounds
		int newgrid[][][] = new int[grid.length+2][grid[0].length+2][1];
		for (int i=0; i<newgrid.length; i++) {
			for (int j=0; j<newgrid[0].length; j++) {
				if (i==0 || j==0 || i==newgrid.length-1 || j==newgrid[0].length-1)
					newgrid[i][j] = new int[] {9, 0};
				else
					newgrid[i][j] = new int[] {grid[i-1][j-1], 0};
			}
		}		
		// start from basin and head north until we hit a wall and then start applying right-hand rule to completely traverse wall
		// counter-clockwise with right-hand always on wall facing: 'n', 'w', 's', 'e'
		// if we get stuck jump back to beginning and repeat but this time go clockwise using the lefthand rule
		// if we get stuck again, jump back to beginning and repeat process (if not stuck)
		
		ArrayList<int[]> basinCoords = new ArrayList<>();
		basinCoords.add(new int[] {r,c});
		char facing = 'n';
		int locr = r+1;
		int locc = c+1;
		int state = 0; 		// the idea behind state is ... 
							// 0 means get as far north as possible
							// 1 means we have started to move counterclockwise tracing the edges of the wall ... allowed to backtrack over spaces behind us if necessary ... but if run over first spot where we hit wall switch to state 2
							// 2 means we are still counterclockwise tracing, but this time if we hit the original spot again we increment our "wallcounter" and are no longer allowed to step on walls with a counter<wallcounter but go ahead and switch back to state 1
		int nwr = -1;
		int nwc = -1;
		int wallcount = 1;
		printNearbygrid(10, state, wallcount, r+1, c+1, newgrid);
		while (true) { 
			// we will break out of the loop at necessary point ... so we have to label the loop since we are also using a switch stmt ... could avoid label by using if ... else if ...
			if (state==0) { // the "go north" state
				// if we are in state 0 and back at the beginning where we started ... we have marked everything
				if (completelySurrounded(wallcount, locr, locc, newgrid))
					break; // this breaks out ... supposedly we should have marked everything by now
				
				if (!isItReallyWall(wallcount, locr-1, locc, newgrid)) {
					locr--;
				} else {
					// we hit the north wall! ... save the spot where we hit and transition to state 1
					nwr=locr;
					nwc=locc;
					state = 1;
					facing = 'w'; // saves a step
				}
			} else if (state==2) { // the "go south" state
					// if we are in state 0 and back at the beginning where we started ... we have marked everything
					if (completelySurrounded(wallcount, locr, locc, newgrid))
						break; // this breaks out ... supposedly we should have marked everything by now
					
					if (!isItReallyWall(wallcount, locr+1, locc, newgrid)) {
						locr++;
					} else {
						// we hit the north wall! ... save the spot where we hit and transition to state 1
						nwr=locr;
						nwc=locc;
						state = 1;
						facing = 'e'; // saves a step
					}
			} else if (state==1) { // the "counterclockwise" "right-hand rule" traversal
				switch(facing) {
					case 'w':
						// facing west ... only move west if there is not a wall there AND if there is still a wall to our right (north)
						if (!isItReallyWall(wallcount, locr, locc-1, newgrid) && isItReallyWall(wallcount, locr-1, locc, newgrid)) {
							// go west!
							locc--;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
						} else if (!isItReallyWall(wallcount, locr-1, locc, newgrid)) {
							// move north if we find an empty spot there
							locr--;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
							facing='n';
						} else {
							// we only make it here if we can't go west ... so let's turn south
							facing='s';
						}
						break;
					case 'n':
						// facing north ... only move north if there is not a wall there AND if there is still a wall to our right (east)
						if (!isItReallyWall(wallcount, locr-1, locc, newgrid) && isItReallyWall(wallcount, locr, locc+1, newgrid)) {
							// go north!
							locr--;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
						} else if (!isItReallyWall(wallcount, locr, locc+1, newgrid)) {
							// move east if we find an empty spot there
							locc++;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
							facing='e';
						} else {
							// we only make it here if we can't go east ... so let's turn back west
							facing='w';
						}
						break;
					case 'e':
						// facing east ... only move east if there is not a wall there AND if there is still a wall to our right (south)
						if (!isItReallyWall(wallcount, locr, locc+1, newgrid) && isItReallyWall(wallcount, locr+1, locc, newgrid)) {
							// go east!
							locc++;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
						} else if (!isItReallyWall(wallcount, locr+1, locc, newgrid)) {
							// move south if we find an empty spot there
							locr++;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
							facing='s';
						} else {
							// we only make it here if we can't go south ... so let's turn back north
							facing='n';
						}
						break;
					case 's':
						// facing south ... only move south if there is not a wall there AND if there is still a wall to our right (west)
						if (!isItReallyWall(wallcount, locr+1, locc, newgrid) && isItReallyWall(wallcount, locr, locc-1, newgrid)) {
							// go south!
							locr++;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
						} else if (!isItReallyWall(wallcount, locr, locc-1, newgrid)) {
							// move west if we find an empty spot there
							locc--;
							addCoord(wallcount, locr, locc, newgrid, basinCoords);
							facing='w';
						} else {
							// we only make it here if we can't go south ... so let's turn back east
							facing='e';
						}
						break;
				}
				// see if we are back at where we first hit the wall
				if (locr == nwr && locc==nwc) {
					wallcount++;
					state = 2;
				}
				
			} else {
			}
			printNearbygrid(10, state, wallcount, r+1, c+1, newgrid);
		}
		System.exit(1);
		
		// since we have list of visited coords and can "jump" ... use this strategy
		// 
		return basinCoords.size();
	}
	
	private static void printNearbygrid(int sq, int state, int wallcount, int r, int c, int[][][]grid) {
		System.out.println(r + " " + c + " " + state + " " + wallcount);
		for (int i=r-20; i<r+sq; i++) {
			for (int j=c-20; j<c+sq; j++) {
				if (i>=0 && j>=0 && i<grid.length && j<grid.length)
					System.out.print(grid[i][j][0]);
			}
			if (i>=0 && i<grid.length)
				System.out.println();
		}
		System.out.println();
	}

	private static void addCoord(int wallcount, int r, int c, int[][][] newgrid, ArrayList<int[]> basinCoords) {
		newgrid[r][c] = new int[] {9, wallcount};
		boolean alreadythere = false;
		for (int[] coord: basinCoords) {
			if (coord[0]==r && coord[1]==c) {
				alreadythere = true;
				break;
			}
		}
		if (!alreadythere) {
			basinCoords.add(new int[] {r, c});
		}
	}

	private static boolean completelySurrounded(int wallcount, int locr, int locc, int[][][] newgrid) {
		return isItReallyWall(wallcount, locr-1, locc, newgrid)&&
				isItReallyWall(wallcount, locr, locc-1, newgrid)&&
				isItReallyWall(wallcount, locr+1, locc, newgrid)&&
				isItReallyWall(wallcount, locr, locc+1, newgrid);
	}

	// marking the "walls" in phases ... so that you can backtrack over a wall once, but not twice
	private static boolean isItReallyWall(int wc, int r, int c, int[][][] newgrid) {
		return newgrid[r][c][0]==9 && newgrid[r][c][1]<wc;
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
