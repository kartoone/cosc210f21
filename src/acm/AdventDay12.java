package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AdventDay12 {

	static class Cave implements Comparable<Cave> {
		String name;
		ArrayList<Cave> neighbors = new ArrayList<Cave>();
		Cave(String name) {
			this.name = name;
			neighbors = new ArrayList<Cave>();
		}
		void addNeighbor(Cave c) {
			if (!neighbors.contains(c))
				neighbors.add(c);
		}
		// compare just on the name
		@Override
		public int compareTo(Cave o) {
			return name.compareTo(o.name);
		}
		@Override
		public boolean equals(Object o) {
			Cave ocave = (Cave) o;
			return name.equals(ocave.name);
		}		
		@Override
		public String toString() {
			String ret = name + ": ";
			for (Cave c: neighbors)
				ret += c.name + " ";
			return ret;
		}
		public boolean isBig() {
			return name.toUpperCase().equals(name);
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day12.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		ArrayList<Cave> allcaves = new ArrayList<Cave>(); // for adding neighbors
		Cave start = null;
		Cave end = null;
		
		for (String line: lines) {
			String cavenames[] = line.split("-");
			Cave cave1 = new Cave(cavenames[0]);
			Cave cave2 = new Cave(cavenames[1]);
			int c1index = allcaves.indexOf(cave1);
			if (c1index>=0) {
				cave1 = allcaves.get(c1index);
			} else {
				allcaves.add(cave1);
				if (cave1.name.equals("start"))
					start = cave1;
				if (cave1.name.equals("end"))
					end = cave1;
			}
			int c2index = allcaves.indexOf(cave2);
			if (c2index>=0) {
				cave2 = allcaves.get(c2index);
			} else {
				allcaves.add(cave2);
				if (cave2.name.equals("start"))
					start = cave2;
				if (cave2.name.equals("end"))
					end = cave2;
			}
			cave1.addNeighbor(cave2); // will be ignored if already neighbors
			cave2.addNeighbor(cave1);
		}
				
		if (start==null || end==null) {
			System.err.println("Didn't find start or end or both!");
			System.exit(1);
		}

		// debugging the initial parsing
//		for (Cave c: allcaves)
//			System.out.println(c);
		
		// now kick-start the recursive algo starting with the start cave
		ArrayList<String> allpaths = new ArrayList<>();
		findPaths1(start, new ArrayList<String>(), allpaths); 
		System.out.println(allpaths.size()); // puzzle1 solution
		allpaths = new ArrayList<>();
		findPaths2(start, false, new ArrayList<String>(), allpaths); 
		System.out.println(allpaths.size()); // puzzle2 solution
	}

	private static void findPaths1(Cave c, ArrayList<String> currentpath, ArrayList<String> allpaths) {
		// base case ... we reached the end
		if (c.name.equals("end")) {
			currentpath.add(c.name);
			allpaths.add(currentpath.toString());				
		}			
		// base case ... trying to add a small cave again ...
		else if (currentpath.contains(c.name) && !c.isBig()) {
			return;
		}
		// recursive step ... try continuing the currentpath in the direction of each of the neighbors 
		else {
			currentpath.add(c.name);
			for (Cave n: c.neighbors) {
				ArrayList<String> newpath = (ArrayList<String>)currentpath.clone();
				findPaths1(n, newpath, allpaths);
			}
		}
	}

	// smallUsedup keeps track of whether or not we have already used up our ability to visit a single small cave a second time
	private static void findPaths2(Cave c, boolean smallUsedup, ArrayList<String> currentpath, ArrayList<String> allpaths) {
		// base case ... we reached the end
		if (c.name.equals("end")) {
			currentpath.add(c.name);
			allpaths.add(currentpath.toString());				
		}
		// another base case ... tried to revisit start again ... kill that path
		else if (c.name.equals("start") && currentpath.size()>0) {
			return; 
		}
		// another base case ... trying to add a small cave again after already visiting our "single" small cave ... 
		else if (currentpath.contains(c.name) && !c.isBig() && smallUsedup) {
			return;
		}
		// recursive step ... try continuing the currentpath in the direction of each of the neighbors 
		else {
			smallUsedup = smallUsedup || !c.isBig() && !c.name.equals("start") && currentpath.contains(c.name);
			currentpath.add(c.name);
			for (Cave n: c.neighbors) {
				ArrayList<String> newpath = (ArrayList<String>)currentpath.clone();
				findPaths2(n, smallUsedup, newpath, allpaths);
			}
		}
	}

}
