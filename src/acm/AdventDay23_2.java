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
import java.util.SortedSet;
import java.util.TreeSet;

public class AdventDay23_2 {

	static class Pod implements Comparable<Pod> {
		int r;
		int c;
		char name;
		int cm;
		public Pod(int r, int c, char name) {
			this.r=r;
			this.c=c;
			this.name=name;
			switch (name) {
			case 'A': cm=1; break;
			case 'B': cm=10; break;
			case 'C': cm=100; break;
			case 'D': cm=1000; break;
			}
		}
		public Pod(Pod other) {
			r= other.r;
			c= other.c;
			name= other.name;
			cm = other.cm;
		}
		@Override
		public String toString() {
			return name+":"+r+","+c;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Pod) {
				Pod other = (Pod) o;
				return r==other.r && c==other.c && name==other.name;
			}
			return false;
		}
		
		@Override
		public int compareTo(Pod other) {
			if (name==other.name) {
				return r-other.r;
			} else {
				return ((Character)name).compareTo(other.name);
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		puzzle2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day23.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	// game tree search space!
	// may not be optimal solution, but I've always wanted to write one
	// so here goes ...
	// keep track of desired goal state: all pods in their correct hallway
	// repeat until goal state met ... 
	// breadth first takes too much memory ... and depth first (without any heurstic) take too long to run ... you have to "prune" the tree somehow. 
	
	static char maze[][];
	static long mazecnt = 0;
	static Pod[] goal = new Pod[] {
			new Pod(2,3,'A'), 
			new Pod(3,3,'A'),
			new Pod(4,3,'A'), 
			new Pod(5,3,'A'),
			new Pod(2,5,'B'),
			new Pod(3,5,'B'),
			new Pod(4,5,'B'),
			new Pod(5,5,'B'),
			new Pod(2,7,'C'),
			new Pod(3,7,'C'),
			new Pod(4,7,'C'),
			new Pod(5,7,'C'),
			new Pod(2,9,'D'),
			new Pod(3,9,'D'),
			new Pod(4,9,'D'),
			new Pod(5,9,'D')
	};

	// rather than reading our input from a file, we are hardcoding initial and example to make it easier to switch between the two
	static Pod[] initial = new Pod[] {
			new Pod(2,3,'C'), 
			new Pod(3,3,'D'),
			new Pod(4,3,'D'), 
			new Pod(5,3,'D'),
			new Pod(2,5,'C'),
			new Pod(3,5,'C'),
			new Pod(4,5,'B'),
			new Pod(5,5,'A'),
			new Pod(2,7,'B'),
			new Pod(3,7,'B'),
			new Pod(4,7,'A'),
			new Pod(5,7,'B'),
			new Pod(2,9,'D'),
			new Pod(3,9,'A'),
			new Pod(4,9,'C'),
			new Pod(5,9,'A')
	};
	static Pod[] example = new Pod[] {
			new Pod(2,3,'B'), 
			new Pod(3,3,'D'),
			new Pod(4,3,'D'), 
			new Pod(5,3,'A'),
			new Pod(2,5,'C'),
			new Pod(3,5,'C'),
			new Pod(4,5,'B'),
			new Pod(5,5,'D'),
			new Pod(2,7,'B'),
			new Pod(3,7,'B'),
			new Pod(4,7,'A'),
			new Pod(5,7,'C'),
			new Pod(2,9,'D'),
			new Pod(3,9,'A'),
			new Pod(4,9,'C'),
			new Pod(5,9,'A')
	};

	// this was used by the BFS and DFS algorithm ... but isn't currently used by the djikstra algo
	static ArrayList<String> solution;
	static int solutioncost = Integer.MAX_VALUE;
	
	private static void puzzle2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		maze = new char[lines.size()][lines.get(0).length()];
		for (int r=0; r<maze.length; r++) {
			String line = lines.get(r);
			for (int c=0; c<line.length(); c++) {
				char cc= line.charAt(c);
				if (cc=='A'||cc=='B'||cc=='C'||cc=='D') {
					maze[r][c] = '.';
				} else {
					maze[r][c] = cc;
				}
			}
		}
		displayConfig(initial);
		System.out.println();
		displayConfig(example);
		System.out.println();
		displayConfig(goal);

		solveMaze(new ArrayList<String>(),initial,1);
	}

	// this is the depth first approach 
	// assumes path already has the move that got us to the given config of Pods
	private static void solveMaze(ArrayList<String> path, Pod[] config, int depth) {
		// see if we found a solution

		// find all possible moves - this is probably the hardest part
		char cmaze[][] = new char[maze.length][maze[0].length];
		for (int r=0; r<maze.length; r++)
			for (int c=0; c<maze[0].length; c++)
				cmaze[r][c] = maze[r][c];
		for (Pod pod: config) {
			cmaze[pod.r][pod.c] = pod.name;
		}
				
		if (++mazecnt % 100000L == 0) {
			System.out.println(solution);
			System.out.println(solutioncost);
			displayMaze(cmaze);
			System.out.println(mazecnt);
		} else if (mazecnt > 1000000000L) {
			System.out.println(solution);
			System.out.println(solutioncost);
			System.exit(0);
		}

		if (configmatch(config, goal)) {
			if (cost(path)<solutioncost) {
				solutioncost = cost(path);
				solution = path;
			}
			return;
		} else {					
//			System.out.println(depth);		
			// strategy - pod must move out into the hallway and either left or right until it stops ... then it can't move again until it's ready to move into a room
			//          - also pod can't move into any room other than its own and only if there aren't any other pods (not of the same kind) currently in that room
			for (Pod pod: config) {
				int dc = 3+(pod.name-65)*2;
				if (pod.r==1) {
					// pod already in hallway
					// cannot move anywhere other than its destination room
					// and even then - only if it only has another of its own kind in it
					// so we just need to look at all the spaces between here and its hallway home
					// only ONE possible move can come out of this b/c we should never stop early in our room, we should always go as far as possible
					int mc = pod.c>dc?-1:1;
					int c2 = pod.c;
					boolean valid = true;
					while (c2!=dc) {
						c2+=mc;
						if (cmaze[pod.r][c2]!='.') {
							valid=false;
							break;
						}
					}
					int r2 = pod.r+1;
					while (r2<6) {
						if (cmaze[r2][c2]!='.'&&cmaze[r2][c2]!=pod.name) {
							valid = false;
							break;
						} else if (cmaze[r2][c2]==pod.name) {
							// in the first puzzle, we could just break if we saw another pod with the same name since there can only be two pods in the room
							// but in this puzzle we've got to make sure that ALL the spots in the room have the same name or we can't move back in
							// the loop below checks to make sure all pods are correct
							for (int r3=r2;r3<6; r3++) {
								if (cmaze[r3][c2]!=pod.name) {
									valid=false;
									break;
								}
							}
							break; // if we bump into another pod in our hallway with our same name just go ahead and stop early
						}
						r2++;
					}
					if (valid && r2>pod.r+1) { // so r2 should be past the destination
						//yay we found a valid move
						//create entirely new pod config for the new config
						Pod[] newconfig = new Pod[config.length];
						for (int i=0; i<config.length; i++) {
							if (config[i]==pod)
								newconfig[i] = new Pod(r2-1,c2,pod.name);
							else
								newconfig[i] = new Pod(config[i]);
						}
						ArrayList<String> newpath = (ArrayList<String>)path.clone();
						int oldcost = newpath.size()>0?Integer.parseInt(newpath.get(newpath.size()-1).split("-")[5]):0;
						int newcost = oldcost+pod.cm*(Math.abs(r2-1-pod.r)+Math.abs(c2-pod.c));
						if (newcost<solutioncost) {
							newpath.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+(r2-1)+"-"+c2+"-"+newcost);
							java.util.Arrays.sort(newconfig);
							solveMaze(newpath,newconfig,depth+1);
						}
					}
				} else {
					// must be in a room ... so this can lead to LOTS of possible
					// moves. must move into hallway and can go left or right
					// and can stop anywhere other than outside another room
					// let's make sure we can get out into the hallway
					
					// first let's see if the pod is already in the right spot (then don't move it!)
					// the four room setup is a bit trickier to know whether the pod is in the right spod ... the entire room must be filled with pods of the same name
					if (pod.c==dc) {
						if (pod.r==5)
							continue;
						if (pod.r==4 && cmaze[5][dc]==pod.name)
							continue;
						if (pod.r==3 && cmaze[4][dc]==pod.name && cmaze[5][dc]==pod.name)
							continue;
						if (pod.r==2 && cmaze[3][dc]==pod.name && cmaze[4][dc]==pod.name && cmaze[5][dc]==pod.name)
							continue;
					}
					
					int r2 = pod.r;
					boolean valid = true;
					while (r2>1) {
						r2--;
						if (cmaze[r2][pod.c]!='.') {
							valid=false;
							break;
						}
					}
					if (!valid) {
						// we couldn't even get out of the room
						// simply continue onto the next pod
						// no valid move at all for this pod
						continue;
					}
					// let's handle going left first ... completely separately from going right
					int c2 = pod.c - 1;
					while (c2>=1) {
						if (c2==3||c2==5||c2==7)
							c2--; // these are GUARANTEED to be empty so simply decrement again
						if (cmaze[r2][c2]=='.') {
							// found a valid spot!
							Pod[] newconfig = new Pod[config.length];
							for (int i=0; i<config.length; i++) {
								if (config[i]==pod)
									newconfig[i] = new Pod(r2,c2,pod.name);
								else
									newconfig[i] = new Pod(config[i]);
							}
							ArrayList<String> newpath = (ArrayList<String>)path.clone();
							int oldcost = newpath.size()>0?Integer.parseInt(newpath.get(newpath.size()-1).split("-")[5]):0;
							int newcost = oldcost+pod.cm*(Math.abs(r2-pod.r)+Math.abs(c2-pod.c));
							if (newcost<solutioncost) {
								newpath.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+r2+"-"+c2+"-"+newcost);
								java.util.Arrays.sort(newconfig);
								solveMaze(newpath,newconfig,depth+1);
							}
							c2--;
						} else {
							break; // we are done finding all the valid left moves
						}
					}
					
					// now let's try going right
					c2 = pod.c + 1;
					while (c2<=11) {
						if (c2==5||c2==7||c2==9)
							c2++; // these are GUARANTEED to be empty so simply increment again
						if (cmaze[r2][c2]=='.'||cmaze[r2][c2]==' ') {
							// found a valid spot!
							Pod[] newconfig = new Pod[config.length];
							for (int i=0; i<config.length; i++) {
								if (config[i]==pod)
									newconfig[i] = new Pod(r2,c2,pod.name);
								else
									newconfig[i] = new Pod(config[i]);
							}
							ArrayList<String> newpath = (ArrayList<String>)path.clone();
							int oldcost = newpath.size()>0?Integer.parseInt(newpath.get(newpath.size()-1).split("-")[5]):0;
							int newcost = oldcost+pod.cm*(Math.abs(r2-pod.r)+Math.abs(c2-pod.c));
							if (newcost<solutioncost) {
								newpath.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+r2+"-"+c2+"-"+newcost);
								java.util.Arrays.sort(newconfig);
								solveMaze(newpath,newconfig,depth+1);
							}
							c2++;
						} else {
							break; // we are done finding all the valid 
						}
					}
				}
			}
		}
	}
	
	private static boolean configmatch(Pod[] config1, Pod[] config2) {
		for (int i=0; i<config1.length; i++)
			if (!config1[i].equals(config2[i]))
				return false;
		return true;
	}

	private static Integer cost(ArrayList<String> path) {
		return Integer.parseInt(path.get(path.size()-1).split("-")[5]);
	}

	private static void displayMaze(char maze[][]) {
		for (int r=0; r<maze.length; r++) {
			for (int c=0; c<maze[r].length; c++)
				System.out.print(maze[r][c]);
			System.out.println();
		}
	}
	
	private static void displayConfig(Pod[] config) {
		char cmaze[][] = new char[maze.length][maze[0].length];
		for (int r=0; r<maze.length; r++)
			for (int c=0; c<maze[0].length; c++)
				cmaze[r][c] = maze[r][c];
		for (Pod pod: config) {
			cmaze[pod.r][pod.c] = pod.name;
		}
		displayMaze(cmaze);
	}
	
}
