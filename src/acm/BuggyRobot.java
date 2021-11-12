package acm;

import java.util.Scanner;

import data.EmptyQueueException;
import data.ListQueue;
import data.Queue;

import java.io.*;

public class BuggyRobot {

	static char map[][];	 // stores the map data
	static int rows;         // # rows in the map
	static int cols;		 // # cols in the map
	static int robot_r;		 // current robot location (row)
	static int robot_c;      // current robot location (column)
	static String buggycode; // the "buggy" program controlling the robot (e.g., LRDD)
	
	public static void main(String[] args) throws FileNotFoundException, EmptyQueueException {
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
		buggycode = in.nextLine();
		printMap();
		System.out.println(buggycode);
		System.out.println();
		followCode(buggycode);
	}

	// strategy: go ahead and follow the direction no matter what ... see where you land ... and then UNDO if invalid position (either off the edge of the map or on top of an internal wall)
	static void followCode(String code) throws EmptyQueueException {
		// queue up all the commands for processing later
		Queue<Character> cmds = new ListQueue<>();
		for (int i=0; i<code.length(); i++) {
			cmds.enqueue(code.charAt(i));
		}		
	
		int r = robot_r; // current robot r position
		int c = robot_c; // current robot c position
		
		// it's now later, so let's go ahead and process all the commands from the queue
		int i=0; // keep tracks of how many "steps" it has taken to solve the map
		while(!cmds.isEmpty()) {
			i++;
			char cmd = cmds.dequeue();
			System.out.println("Step " + i + ": " + cmd);
			int old_r = r; // save the current location in case we should not have followed the command
			int old_c = c;
			
			// follow the command no matter what...
			switch (cmd) {
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
				map[old_r][old_c] = '.';
				if (map[r][c]=='E') {
					map[r][c] = 'R';
					printMap();
					System.out.println("hit exit in " + i + " move(s).");
					break; // kicks us completely out of for loop
				} else {
					// update the map to reflect the robot's new position
					map[r][c] = 'R';
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