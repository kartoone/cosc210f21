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

public class AdventDay23 {

	static class Pod implements Comparable {
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
		public int compareTo(Object o) {
			if (o instanceof Pod) {
				Pod other = (Pod) o;
				if (name==other.name) {
					return r-other.r;
				} else {
					return ((Character)name).compareTo(other.name);
				}
			}
			return 1;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
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
	// repeat until goal state met ... since breadth first ... likely that first goal state is cheapest, but not necessarily so continue on out until all paths have "died" or reached goal state ... report the lowest energy goal state path	
	static char maze[][];
	static long mazecnt = 0;
	static Pod[] goal = new Pod[] {
			new Pod(2,3,'A'), 
			new Pod(3,3,'A'),
			new Pod(2,5,'B'),
			new Pod(3,5,'B'),
			new Pod(2,7,'C'),
			new Pod(3,7,'C'),
			new Pod(2,9,'D'),
			new Pod(3,9,'D')
	};
	static ArrayList<String> solution;
	static int solutioncost = Integer.MAX_VALUE;
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		maze = new char[lines.size()][lines.get(0).length()];
		Pod initialConfig[] = new Pod[8];
		int podi = 0;
		for (int r=0; r<maze.length; r++) {
			String line = lines.get(r);
			for (int c=0; c<line.length(); c++) {
				char cc= line.charAt(c);
				if (cc=='A'||cc=='B'||cc=='C'||cc=='D') {
					initialConfig[podi++] = new Pod(r, c, cc);
					maze[r][c] = '.';
				} else {
					maze[r][c] = cc;
				}
			}
		}
		System.out.println(Arrays.toString(initialConfig));
		java.util.Arrays.sort(initialConfig);
		ArrayList<Solution> initial = new ArrayList<>();
		initial.add(new Solution(initialConfig));
		solveMazeBFS(initial, 1);
	}

	static class Solution {
		ArrayList<String> path = new ArrayList<>();
		ArrayList<Pod[]> configs = new ArrayList<>();
		Pod[] config; // our current config
		int cost; // our current path cost
		public Solution (Pod[] config) {
			this.config = config;
		}
		public Solution(Solution other, Pod[] config) {
			this.path = (ArrayList<String>) other.path.clone();
			this.cost = other.cost;
			this.config = config;
			this.configs = duplicateConfigs(other.configs);
		}
		
		// returns arraylist of next solutions to try to indicate this solution should keep going
		// returns empty arraylist if this potential solution either solved the maze
		// or reached a dead end
		public ArrayList<Solution> step() {
			if (configmatch(config, goal)) {
				if (cost(path)<solutioncost) {
					solutioncost = cost(path);
					solution = path;
				}
				return new ArrayList<>(); // empty arraylist to remove this solution from the list of potential solutions
			}
			
			// now let's look for potential moves - if we made it to here
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
			ArrayList<Solution> branches = new ArrayList<>();
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
					while (r2<4) {
						if (cmaze[r2][c2]!='.'&&cmaze[r2][c2]!=pod.name) {
							valid = false;
							break;
						} else if (cmaze[r2][c2]==pod.name)
							break; // if we bump into another pod in our hallway with our same name just go ahead and stop early
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
						java.util.Arrays.sort(newconfig);
						if (!alreadyHaveit(newconfig, configs)) {
							Solution newsol = new Solution(this, newconfig);
							newsol.path.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+(r2-1)+"-"+c2);
							newsol.cost += pod.cm*(Math.abs(r2-1-pod.r)+Math.abs(c2-pod.c));
							if (newsol.cost<solutioncost) {
								newsol.configs.add(newconfig);
								branches.add(newsol);
							}
						}
					}
				} else {
					// must be in a room ... so this can lead to LOTS of possible
					// moves. must move into hallway and can go left or right
					// and can stop anywhere other than outside another room
					// let's make sure we can get out into the hallway
					
					// first let's see if the pod is already in the right spot (then don't move it!)
					if (pod.c==dc) {
						if (pod.r==3)
							continue;
						if (pod.r==2 && cmaze[3][dc]==pod.name)
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
							java.util.Arrays.sort(newconfig);
//							System.out.println(configs.size());
							if (!alreadyHaveit(newconfig, configs)) {
								Solution newsol = new Solution(this, newconfig);
								newsol.path.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+r2+"-"+c2);
								newsol.cost += pod.cm*(Math.abs(r2-pod.r)+Math.abs(c2-pod.c));
								if (newsol.cost<solutioncost) {
									newsol.configs.add(newconfig);
									branches.add(newsol);
								}
							}
							c2--;
						} else {
							break; // we are done finding all the valid left moves
						}
					}
					
					// now let's try going right
					c2 = pod.c + 1;
					while (c2<=11) {
						if (c2==3||c2==5||c2==7)
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
							java.util.Arrays.sort(newconfig);
//							System.out.println(configs.size());
							if (!alreadyHaveit(newconfig, configs)) {
								Solution newsol = new Solution(this, newconfig);
								newsol.path.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+r2+"-"+c2);
								newsol.cost += pod.cm*(Math.abs(r2-pod.r)+Math.abs(c2-pod.c));
								if (newsol.cost<solutioncost) {
									newsol.configs.add(newconfig);
									branches.add(newsol);
								}
							}
							c2++;
						} else {
							break; // we are done finding all the valid 
						}
					}
				}
			}
			return branches;
		}
	}

	// breadth first approach ... have to pass the list of current solutions we are trying 
	// through the recursion. Remove items from the list as they either work (solved) or fail
	// not going to make it, but base case is when there are no more potential solutions to try
	// Always remove the solution from the list and then "re-add" the list of branched solutions returned from step
	// This is good place to check for duplicates
	// Uh-oh not going to work b/c most solutions are at least 15 moves ... and at a depth of 8 we are already into the millions of
	// potential solutions to check. This will indeed find the optimal solution, BUT we essentially need maybe 1TB of RAM.
	// This could work if there is a heuristic to eliminate vast majority of potential solutions
	// 
	private static void solveMazeBFS(ArrayList<Solution> current, int depth) {			
		System.out.println(depth + ": " + current.size());

		if (current.isEmpty())
			return; // base case ... 
		else if (depth==10)
			return; // stop after we make it to a certain depth (10 is already billions)
		
		ArrayList<Solution> newcurrent = new ArrayList<>();
		Iterator<Solution> it = current.iterator();
		while (it.hasNext()) {
			Solution s = it.next();
			it.remove(); // remove the current solution... we will be adding the branches for the next step(s) of s back into "current" on next recursive call
			newcurrent.addAll(s.step());
		}
		solveMazeBFS(newcurrent, depth+1);			
	}
	
	// this is a depth first approach and it doesn't reach solution in reasonable amount of time for my input (or even more likely for puzzle 2)
	// assumes path already has the move that got us to the given config of Pods
	private static void solveMaze(ArrayList<String> path, Pod[] config, ArrayList<Pod[]> configs, int depth) {
		// see if we found a solution

		// find all possible moves - this is probably the hardest part
		char cmaze[][] = new char[maze.length][maze[0].length];
		for (int r=0; r<maze.length; r++)
			for (int c=0; c<maze[0].length; c++)
				cmaze[r][c] = maze[r][c];
		for (Pod pod: config) {
			cmaze[pod.r][pod.c] = pod.name;
		}
		if (++mazecnt % 1000000L == 0) {
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
//			displayMaze(cmaze);
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
					while (r2<4) {
						if (cmaze[r2][c2]!='.'&&cmaze[r2][c2]!=pod.name) {
							valid = false;
							break;
						} else if (cmaze[r2][c2]==pod.name)
							break; // if we bump into another pod in our hallway with our same name just go ahead and stop early
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
						newpath.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+(r2-1)+"-"+c2+"-"+newcost);
						java.util.Arrays.sort(newconfig);
//						System.out.println(configs.size());
						if (!alreadyHaveit(newconfig, configs)) {
							ArrayList<Pod[]> newconfigs = duplicateConfigs(configs);
							newconfigs.add(newconfig);
							solveMaze(newpath,newconfig,newconfigs,depth+1);
						}
					}
				} else {
					// must be in a room ... so this can lead to LOTS of possible
					// moves. must move into hallway and can go left or right
					// and can stop anywhere other than outside another room
					// let's make sure we can get out into the hallway
					
					// first let's see if the pod is already in the right spot (then don't move it!)
					if (pod.c==dc) {
						if (pod.r==3)
							continue;
						if (pod.r==2 && cmaze[3][dc]==pod.name)
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
							newpath.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+r2+"-"+c2+"-"+newcost);
							java.util.Arrays.sort(newconfig);
//							System.out.println(configs.size());
							if (!alreadyHaveit(newconfig, configs)) {
								ArrayList<Pod[]> newconfigs = duplicateConfigs(configs);
								newconfigs.add(newconfig);
								solveMaze(newpath,newconfig,newconfigs,depth+1);
							}
							c2--;
						} else {
							break; // we are done finding all the valid left moves
						}
					}
					
					// now let's try going right
					c2 = pod.c + 1;
					while (c2<=11) {
						if (c2==3||c2==5||c2==7)
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
							newpath.add(pod.name+"-"+pod.r+"-"+pod.c+"-"+r2+"-"+c2+"-"+newcost);
							java.util.Arrays.sort(newconfig);
//							System.out.println(configs.size());
							if (!alreadyHaveit(newconfig, configs)) {
								ArrayList<Pod[]> newconfigs = duplicateConfigs(configs);
								newconfigs.add(newconfig);
								solveMaze(newpath,newconfig,newconfigs,depth+1);
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
	
	private static ArrayList<Pod[]> duplicateConfigs(ArrayList<Pod[]> configs) {
		ArrayList<Pod[]> newconfigs = new ArrayList<>();
		for (Pod[] config : configs) {
			Pod[] newconfig = new Pod[config.length];
			for (int i=0; i<config.length; i++) {
				newconfig[i] = new Pod(config[i]);
			}
			newconfigs.add(newconfig);
		}
		return newconfigs;
	}
	
	private static boolean alreadyHaveit(Pod[] config, ArrayList<Pod[]> configs) {
		for (Pod[] c: configs)
			if (configmatch(config, c))
				return true;
		return false;
	}

	private static boolean configmatch(Pod[] config1, Pod[] config2) {
//		System.out.println(Arrays.toString(config1));
//		System.out.println(Arrays.toString(config2));
		for (int i=0; i<config1.length; i++)
			if (!config1[i].equals(config2[i]))
				return false;
		return true;
	}

	private static Integer cost(ArrayList<String> path) {
//		return Integer.parseInt(path.get(path.size()-1).split("-")[5]);
		int totalcost = 0;
		for (String move: path) {
			String parts[] = move.split("-");
			char c = parts[0].charAt(0);
			int cm = 0;
			switch (c) {
			case 'A': cm=1; break;
			case 'B': cm=10; break;
			case 'C': cm=100; break;
			case 'D': cm=1000; break;
			}
			int r1 = Integer.parseInt(parts[1]);
			int c1 = Integer.parseInt(parts[2]);
			int r2 = Integer.parseInt(parts[3]);
			int c2 = Integer.parseInt(parts[4]);
			totalcost += cm*(Math.abs(r2-r1)+Math.abs(c2-c1));
		}
		return totalcost;
	}

	private static void displayMaze(char maze[][]) {
		for (int r=0; r<maze.length; r++) {
			for (int c=0; c<maze[r].length; c++)
				System.out.print(maze[r][c]);
			System.out.println();
		}
	}
	
}
