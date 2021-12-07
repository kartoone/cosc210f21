package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay7 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1();
	}

	private static void puzzle1() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day7.txt"));
		ArrayList<Integer> crabs = new ArrayList<>();
		filein.useDelimiter(",");
		while (filein.hasNext()) {
			crabs.add(Integer.parseInt(filein.next()));
		}
		filein.close();
		
		// determine min/max horizontal position
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (Integer crab : crabs) {
			if (crab<min) {
				min = crab;
			}
			if (crab>max) {
				max = crab;
			}
		}
		System.out.println(min + " " + max);
		
		// brute force: try each position from min to max and calculate fuel cost to move all the crabs to that position
		int minfuel = Integer.MAX_VALUE;
		int minfuelp = -1;
		for (int p = min; p<=max; p++) {
			int fuelcost = calculateFuelcost(p, crabs);
			if (fuelcost < minfuel) {
				minfuel = fuelcost;
				minfuelp = p;
			}
		}
		System.out.println(minfuel + " " + minfuelp);
		
	}

	private static int calculateFuelcost(int p, ArrayList<Integer> crabs) {
		int totalfuelcost = 0;
		for (Integer crab: crabs) {
//			totalfuelcost += Math.abs(crab-p); // uncomment for solution to puzzle 1
			totalfuelcost += crabFuel(crab,p); // uncomment for solution to puzzle 2
		}
		return totalfuelcost;
	}
	
	private static int crabFuel(int crab, int p) {
		int totalfuelcost = 0;
		// swap these so crab is the smaller value so we don't have to deal with negative numbers
		if (crab > p) {
			int tmp = p;
			p = crab;
			crab = tmp;
		}
		int currentfuelcost = 1;
		for (int i=0; i<p-crab; i++) {
			totalfuelcost = totalfuelcost + currentfuelcost++;
		}
		return totalfuelcost;
	}
	
}
