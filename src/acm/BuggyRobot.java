package acm;

import java.util.Scanner;
import java.io.*;

public class BuggyRobot {

	static char map[][];
	static int rows;
	static int cols;
	static int robot_r;
	static int robot_c;
	static String buggycode;
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("buggy.txt"));
		// read in the dimensions from the first line of the file
		rows = in.nextInt();
		cols = in.nextInt();
		in.nextLine(); // consume the newline character that is still in the buffer
		map = new char[rows][cols];
		for (int r=0; r<rows; r++) {
			String row = in.nextLine();
			for (int c=0; c<cols; c++) {
				map[r][c] = row.charAt(c);
				if (map[r][c]=='R') {
					robot_r = r;
					robot_c = c;
				}
			}
		}		
		buggycode = in.nextLine();
		printMap();
		System.out.println(buggycode);
		System.out.println();
		followCode(buggycode);
	}

	// strategy: go ahead and follow the direction no matter what ... see where you land ... and then UNDO if invalid position (either off the edge of the map or on top of an internal wall)
	static void followCode(String code) {
		int r = robot_r; // current robot r position
		int c = robot_c; // current robot c position
		
		for (int i=0; i<code.length(); i++) {
			char d = code.charAt(i);
			System.out.println("Step " + i + ": " + d);
			int old_r = r;
			int old_c = c;
			switch (d) {
			case 'L':
				c--;
				break;
			case 'R':
				c++;
				break;
			case 'U':
				r--;
				break;
			case 'D':
				r++;
				break;
			}
			// check for invalid move ... i.e., we are off the edge of the map or sitting on top of an internal wall
			if (c<0 || r<0 || c>=cols || r>=rows || map[r][c]=='#') {
				r = old_r; // restore the old position
				c = old_c;
			} else {
				if (map[r][c]=='E') {
					System.out.println("hit exit in " + i + " move(s).");
					break; // kicks us completely out of for loop
				} else {
					// update the map to reflect the robot's new position
					map[r][c] = 'R';
					map[old_r][old_c] = '.';
				}
			}
			printMap();
			System.out.println();
		}		
	}

	static void printMap() {
		for (int r=0; r<rows; r++) {
			for (int c=0; c<cols; c++) {
				System.out.print(map[r][c]);
			}
			System.out.println();
		}				
	}
	
}
