package acm;

import java.util.Scanner;
import data.ListStack;
import data.Stack;
import java.io.*;

/**
 * Interactive version of the buggy robot problem with an "undo" stack
 * @author brtoone
 *
 */
public class BuggyRobot2 {

	static char map[][];	 // stores the map data
	static int rows;         // # rows in the map
	static int cols;		 // # cols in the map
	static int robot_r;		 // current robot location (row)
	static int robot_c;      // current robot location (column)
    static Stack<Character> undoStack = new ListStack<>();
	static int movecount=0;  // keep track of how many moves the user has made
    
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
					// we found the robot! set its location
					robot_r = r;
					robot_c = c;
				}
			}
		}		
		printMap();
		System.out.println();
		interactiveMode();
	}

	// strategy: go ahead and follow the direction no matter what ... see where you land ... and then UNDO if invalid position (either off the edge of the map or on top of an internal wall)
	static void interactiveMode() {
		Scanner in = new Scanner(System.in);
		// enter the infinite menu loop
		while(true) {
			int choice = displayMenu();
			switch (choice) {
				case 1:
					printMap();
					break;
				case 2:
					processMove();
					break;
				case 3:
					undoMove();
					break;
			}
			// escape out of the infinite loop
			if (choice==4) {
				break;
			}
		}
		System.out.println("Good-bye!");
	}
	
	private static int displayMenu() {
		Scanner in = new Scanner(System.in);
		System.out.println("1. Print map");
		System.out.println("2. Make move");
		System.out.println("3. Undo move");
		System.out.println("4. Quit");
		System.out.print("Choice? ");
		return Integer.parseInt(in.nextLine());
	}

	private static void undoMove() {
		char cmd = undoStack.pop();
		movecount++; // penalize the user for undoing
		map[robot_r][robot_c] = '.';
		switch (cmd) {
		case 'L':
			robot_c++;
			break;
		case 'R':
			robot_c--;
			break;
		case 'U':
			robot_r++;
			break;
		case 'D':
			robot_r--;
			break;
		}
		map[robot_r][robot_c] = 'R';
	}

	static void processMove() {
		Scanner in = new Scanner(System.in);
		boolean validCmd = true;
		do {
			System.out.print("Move (U, D, L, R, Q)? ");
			String usercmd = in.nextLine();
			if (usercmd.length()==0) {
				continue;
			}
			char cmd = usercmd.trim().toUpperCase().charAt(0);
			
			int old_r = robot_r; // save the current location in case we should not have followed the command
			int old_c = robot_c;
			// follow the command no matter what...
			switch (cmd) {
			case 'L':
				robot_c--;
				movecount++;
				validCmd=true;
				break;
			case 'R':
				robot_c++;
				movecount++;
				validCmd=true;
				break;
			case 'U':
				robot_r--;
				movecount++;
				validCmd=true;
				break;
			case 'D':
				robot_r++;
				movecount++;
				validCmd=true;
				break;
			case 'Q':
				validCmd=true;
				break;
			default:
				validCmd = false;
			}
			// check for invalid move ... i.e., we are off the edge of the map or sitting on top of an internal wall
			if (robot_c<0 || robot_r<0 || robot_c>=cols || robot_r>=rows || map[robot_r][robot_c]=='#') {
				robot_r = old_r; // restore the old position
				robot_c = old_c;
			} else if (validCmd) {
				if (cmd!='Q') {
					undoStack.push(cmd);
				}
				map[old_r][old_c] = '.';
				if (map[robot_r][robot_c]=='E') {
					map[robot_r][robot_c] = 'R';
					printMap();
					System.out.println("hit exit in " + movecount + " move(s).");
					break; // kicks us completely out of for loop
				} else {
					// update the map to reflect the robot's new position
					map[robot_r][robot_c] = 'R';
				}
			}
		} while (!validCmd);	
		printMap();
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