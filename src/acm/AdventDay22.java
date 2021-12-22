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

public class AdventDay22 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day22.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	private static int cube[][][] = new int[101][101][101];
	private static ArrayList<int[]> onranges = new ArrayList<>();
	private static ArrayList<int[]> doublecounts = new ArrayList<>(); // approach 2 (not using in approach 1)
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		for (String line: lines) {
			if (line.charAt(2)==' ') {
				toggle(line.substring(3),1);
			} else {
				toggle(line.substring(4),0);
			}
		}
		int oncount = 0;
		for (int x=0; x<101; x++)
			for (int y=0; y<101; y++)
				for (int z=0; z<101; z++)
					if (cube[x][y][z]==1)
						oncount++;
		System.out.println(oncount); // puzzle 1 solution
		
		// puzzle2 would take too long to run and no way to store that large a cube ... so instead just store
		// a list of ranges that are called the "onranges" ... as we parse in the procedure do the following:
		//  1) if the new range is an "on range", then we will be adding this "on range" as is no matter what!
		//		this is very important that none of the on ranges overlap at all or when we tally up the total at the end it will be too big
		//      the idea is that all the current "on ranges" are distinct.
		//      so adding this "on range" should cause some of the other "on ranges" to shrink ... or split ... or completely disappear (i.e., this new range completely engulfs it)
		//      the other approach is to figure out exactly which part(s) of this new "on range" are entirely new and only add those part(s)
		//      the reason why i am leaning towards the first approach is because I didn't want to deal with splitting up the new cube in a million ways and it seems less likely that adding a whole entire cube would cause as many splits? that could be a bad assumption, but I think it should work with either approach and the first will be faster ... possibly .... but probably not
		//  2) if the new range is an "off range", then iterate through current list of "on ranges" and shrink, split, or completely remove as appropriate
		for (String line: lines) {
			if (line.charAt(2)==' ') {
				turnOn(line.substring(3)); // handle #1 here
			} else {
				turnOff(line.substring(4)); // handle #2 here
			}
		}

		// now that we have all the "unique" on ranges, simply count them up ... while this approach would work by itself ... the details of making sure the onranges are distinct (splitting, shrinking, removing, etc...) is nasty and feels very error prone after accounting for just one dimension ... the though of two additional dimensions was daunting
		// so in approach number 2 we still have a set of "onranges" ... but we also keep track of "double counting" by inserting the intersection of any new onrange with any of the existing ranges into the double counting array
		// at the very end, count up all the "ons" but then subtract all the "double counts"
		// what about the "offs" ... each time an "off" is processed ... remove the intersection from both the "ons" and "double counts" ... which brings us back to original scenario of approach 1 where removing an intersection is HARD ...
		//
		// in any case, I wasn't getting the correct answer using approach 2 so I'm switching back to approach 1. My instinct with what is wrong with approach 2 is that when you remove something from doublecount by readding the intersection back into
		// onranges ... this doesn't work out right for future turn ons turn offs ... somehow ... although it seemes logically sound. IN any case I'm confident about approach 2 - just  a little concerened with the sheer amount of code involved and the 
		// likelihood of having one or more small errors somewhere in it!
		long oncount2 = 0L;
		for (int[] onrange: onranges)
			oncount2 = oncount2 + (long)(onrange[1]-onrange[0]+1)*(long)(onrange[3]-onrange[2]+1)*(long)(onrange[5]-onrange[4]+1);

//		for (int[] dcrange: doublecounts)
//			oncount2 = oncount2 - (long)(dcrange[1]-dcrange[0]+1)*(long)(dcrange[3]-dcrange[2]+1)*(long)(dcrange[5]-dcrange[4]+1);

//		for (int[] offrange: offranges)
//			oncount2 = oncount2 - (long)(offrange[1]-offrange[0]+1)*(long)(offrange[3]-offrange[2]+1)*(long)(offrange[5]-offrange[4]+1);
//
//		for (int[] nodcrange: nodoublecounts)
//			oncount2 = oncount2 + (long)(nodcrange[1]-nodcrange[0]+1)*(long)(nodcrange[3]-nodcrange[2]+1)*(long)(nodcrange[5]-nodcrange[4]+1);

		System.out.println(oncount2);
	}
	
	private static void turnOff(String str) {
		int coords[] = parseCoords2(str);
	}
	
	private static void turnOn(String str) {
		int coords[] = parseCoords2(str);
//		System.out.println("on: " + Arrays.toString(coords));
		ArrayList<int[]> newsplits = new ArrayList<>(); // let's keep track of any new splits we need to add here ... and then add them after the iteration is done (so that we don't mess with the onranges list while we are iterating)
		Iterator<int[]> it = onranges.iterator();
		while (it.hasNext()) {
			int[] onrange = it.next();
			// shrink, split, or remove onrange based on the coords of the current on range we are going to add
			if (within(onrange, coords)) {
				it.remove(); // if the existing range is entirely within the new coords ... remove the existing range
			} else if (within(coords, onrange)) {				
				return;    // if the new range is entirely within an existing range ... fastest thing to do is just simply ignore the new range altogether ... note that this is ONLY POSSIBLE if the new range doesn't intersect at all with any other ranges
			} else if (overlap(coords, onrange)) {
				// let's handle the "shrink cases first" ... this is where two dimensions are both fully covered by the new range
				// in this case the other dimension needs to be shrunk by whatever amount that third dimension is not covered
				int xamt = shrinkx(coords, onrange);
				if (xamt!=0) {
					// negative xamt means shrink from the right
					// positive xamt means shrink from the left
					if (xamt>0)
						onrange[0] = onrange[0]+xamt;
					else
						onrange[1] = onrange[1]+xamt; // (xamt will be negative so just add it to subtract ... note that this works for negative ranges too!);
					continue; // we are done with this onrange if it was determined it only needed to shrink in one dimension
				}
				int yamt = shrinky(coords, onrange);
				if (yamt!=0) {
					// negative amt means shrink from the top
					// positive amt means shrink from the bottom
					if (yamt>0)
						onrange[2] = onrange[2]+yamt;
					else
						onrange[3] = onrange[3]+yamt; // (amt will be negative so just add it to subtract ... note that this works for negative ranges too!);
					continue; // we are done with this onrange if it was determined it only needed to shrink in one dimension
				}
				int zamt = shrinkz(coords, onrange);
				if (zamt!=0) {
					// negative amt means shrink from the inside (i.e., the part hidden from you, away from you)
					// positive amt means shrink from the outside (i.e., the part visible to you, closer to you)
					if (zamt>0)
						onrange[4] = onrange[4]+zamt;
					else
						onrange[5] = onrange[5]+zamt; // (amt will be negative so just add it to subtract ... note that this works for negative ranges too!);
					continue; // we are done with this onrange if it was determined it only needed to shrink in one dimension
				}

				// no matter what we are going to have to split the existing range into two or more pieces
				// worst case is 5 pieces: the part "above" the bite is one big flat piece ... the part to the left (but not above), to the right (but not above), the part in front (but not left or right or above), the part in back (but not left or right or above)   
				int bitesplits[] = new int[5];
				if (bitex(coords, onrange, bitesplits)) {
					it.remove(); // no matter what we are splitting the original range into pieces and going to add them back individually, so remove the original giant piece
					// split with the bite coming from the x direction (i.e., from the left or right)
					// this means we will have one big chunk with full y and z dimensions on the opposite side that the bite is coming from ... this is determined by coord0 ... if negative bite came from the right
					if (bitesplits[0]<0) {
						// coming from the right - let's add the "outside" chunk which would be the long deep chunk to the left ... but only if we didn't completely consume it with our bite 
						if (onrange[0]<onrange[1]+bitesplits[0])
							newsplits.add(new int[] {onrange[0],onrange[1]+bitesplits[0], onrange[2], onrange[3], onrange[4], onrange[5]});
						
						// now let's handle the chunk in front of the bite ... this chunk won't exist if the minz<=onrange[4]
						if (bitesplits[3]>0) {
							newsplits.add(new int[] {onrange[1]+bitesplits[0],onrange[1], onrange[2], onrange[3], onrange[4], onrange[4]+bitesplits[3]});
						}
						// now let's handle the chunk behind the bite ... this chunk won't exist if the maxz>=onrange[5]
						if (bitesplits[4]>0) {
							newsplits.add(new int[] {onrange[1]+bitesplits[0],onrange[1], onrange[2], onrange[3], onrange[5]-bitesplits[4], onrange[5]});
						}
						// now let's handle the chunk below the bite ... this chunk won't exist if the miny<=onrange[2]
						if (bitesplits[1]>0) {
							newsplits.add(new int[] {onrange[1]+bitesplits[0],onrange[1], onrange[2], onrange[2]+bitesplits[1], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
						}
						// now let's handle the final potential chunk above the bite ... this chunk won't exist if the maxy>=onrange[3]
						if (bitesplits[2]>0) {
							newsplits.add(new int[] {onrange[1]+bitesplits[0],onrange[1], onrange[3]-bitesplits[2], onrange[3], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
						}						
					} else {
						if (onrange[0]+bitesplits[0]<onrange[1])
							newsplits.add(new int[] {onrange[0]+bitesplits[0],onrange[1], onrange[2], onrange[3], onrange[4], onrange[5]});
						
						// now let's handle the chunk in front of the bite ... this chunk won't exist if the minz<=onrange[4]
						if (bitesplits[3]>0) {
							newsplits.add(new int[] {onrange[0],onrange[0]+bitesplits[0], onrange[2], onrange[3], onrange[4], onrange[4]+bitesplits[3]});
						}
						// now let's handle the chunk behind the bite ... this chunk won't exist if the maxz>=onrange[5]
						if (bitesplits[4]>0) {
							newsplits.add(new int[] {onrange[0],onrange[0]+bitesplits[0], onrange[2], onrange[3], onrange[5]-bitesplits[4], onrange[5]});
						}
						// now let's handle the chunk below the bite ... this chunk won't exist if the miny<=onrange[2]
						if (bitesplits[1]>0) {
							newsplits.add(new int[] {onrange[0],onrange[0]+bitesplits[0], onrange[2], onrange[2]+bitesplits[1], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
						}
						// now let's handle the final potential chunk above the bite ... this chunk won't exist if the maxy>=onrange[3]
						if (bitesplits[2]>0) {
							newsplits.add(new int[] {onrange[0],onrange[0]+bitesplits[0], onrange[3]-bitesplits[2], onrange[3], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
						}												
					}
					
				} else if (bitey(coords, onrange, bitesplits)) {
					it.remove(); // no matter what we are splitting the original range into pieces and going to add them back individually, so remove the original giant piece
					// split with the bite coming from the y direction (i.e., from the top or bottom)

				} else if (bitez(coords, onrange, bitesplits)) {
					it.remove(); // no matter what we are splitting the original range into pieces and going to add them back individually, so remove the original giant piece
					// split with the bite coming from the y direction (i.e., from the top or bottom)

				}
				
				
			} else {
				// nothing to do if there is no overlap at all!
			}
		}
		// if we made it to here than all existing ranges should have been split, shrunk, or removed as necessary to allow us to add the new range in its entirety
		onranges.add(coords);
		
		// let's add any new splits we created along the way
		for (int[] split:newsplits)
			onranges.add(split);
	}
	
	// return true if the bite is coming from the left or right ... bitesplits[0] is the amount and direction (- = left, + = right) ... don't mess with bitesplits[0] if the bite is not coming from left or right simply return false
	// to finish out approach 1, need to finish this plus add bitey, and bitez
	private static boolean bitex(int[] coords, int[] onrange, int[] bitesplits) {
		return true;
	}
	private static boolean bitey(int[] coords, int[] onrange, int[] bitesplits) {
		return true;
	}
	private static boolean bitez(int[] coords, int[] onrange, int[] bitesplits) {
		return true;
	}

	private static int shrinkx(int[] range1, int[] range2) {
		if (range2[2]>=range1[2] &&
			range2[3]<=range1[3] &&
			range2[4]>=range1[4] &&
			range2[5]<=range1[5]) {
			// yep we need to shrink range2 in the x direction ... two situations we are overtaking range2 from the left (range1 minx < range2 minx) or we are overtaking it from the right (range1 maxx > range2 maxx)
			if (range1[0]<range2[0])
				return range1[1]-range2[0]; // should be positive
			else
				return range1[0]-range2[1]; // should be negative
		}
		return 0; // if we make it to here then it didn't fully overlap in the other dimensions
	}
	
	private static int shrinky(int[] range1, int[] range2) {
		if (range2[0]>=range1[0] &&
			range2[1]<=range1[1] &&
			range2[4]>=range1[4] &&
			range2[5]<=range1[5]) {
			// yep we need to shrink range2 in the x direction ... two situations we are overtaking range2 from the left (range1 minx < range2 minx) or we are overtaking it from the right (range1 maxx > range2 maxx)
			if (range1[2]<range2[2])
				return range1[3]-range2[2]; // should be positive
			else
				return range1[2]-range2[3]; // should be negative
		}
		return 0; // if we make it to here then it didn't fully overlap in the other dimensions
	}
	
	private static int shrinkz(int[] range1, int[] range2) {
		if (range2[2]>=range1[2] &&
			range2[3]<=range1[3] &&
			range2[0]>=range1[0] &&
			range2[1]<=range1[1]) {
			// yep we need to shrink range2 in the x direction ... two situations we are overtaking range2 from the left (range1 minx < range2 minx) or we are overtaking it from the right (range1 maxx > range2 maxx)
			if (range1[4]<range2[4])
				return range1[5]-range2[4]; // should be positive
			else
				return range1[4]-range2[5]; // should be negative
		}
		return 0; // if we make it to here then it didn't fully overlap in the other dimensions
	}
		
	private static boolean overlap(int[] range1, int[] range2) {
		return 	(range1[1]>=range2[0] && range1[0]<=range2[1]) &&
				(range1[3]>=range2[2] && range1[2]<=range2[3]) &&
				(range1[5]>=range2[4] && range1[4]<=range2[5]);				
	}

	// returns true if range1 is entirely within range2
	private static boolean within(int[] range1, int[] range2) {
		return 	range1[0]>=range2[0] &&
				range1[1]<=range2[1] &&
				range1[2]>=range2[2] &&
				range1[3]<=range2[3] &&
				range1[4]>=range2[4] &&
				range1[5]<=range2[5];				
	}
	private static void toggle(String str, int onoff) {
		int coords[] = parseCoords1(str);
		System.out.println("onoff: " + onoff + " " + Arrays.toString(coords));
		for (int x=coords[0]+50; x<=coords[1]+50; x++)
			for (int y=coords[2]+50; y<=coords[3]+50; y++)
				for (int z=coords[4]+50; z<=coords[5]+50; z++)
					cube[x][y][z]=onoff;		
	}
	private static int[] parseCoords1(String str) {
		int[] coords = new int[6];
		String[] parts = str.split(",");
		String[] xparts = parts[0].substring(2).split("\\.\\.");
		coords[0] = Integer.parseInt(xparts[0]);
		coords[0] = Math.max(coords[0], -50);
		coords[1] = Integer.parseInt(xparts[1]);
		coords[1] = Math.min(coords[1], 50);
		String[] yparts = parts[1].substring(2).split("\\.\\.");
		coords[2] = Integer.parseInt(yparts[0]);
		coords[2] = Math.max(coords[2], -50);
		coords[3] = Integer.parseInt(yparts[1]);
		coords[3] = Math.min(coords[3], 50);
		String[] zparts = parts[2].substring(2).split("\\.\\.");
		coords[4] = Integer.parseInt(zparts[0]);
		coords[4] = Math.max(coords[4], -50);
		coords[5] = Integer.parseInt(zparts[1]);
		coords[5] = Math.min(coords[5], 50);
		return coords;
	}

	// same as parseCoords1 ... except without the clamping to -50, 50
	private static int[] parseCoords2(String str) {
		int[] coords = new int[6];
		String[] parts = str.split(",");
		String[] xparts = parts[0].substring(2).split("\\.\\.");
		coords[0] = Integer.parseInt(xparts[0]);
		coords[1] = Integer.parseInt(xparts[1]);
		String[] yparts = parts[1].substring(2).split("\\.\\.");
		coords[2] = Integer.parseInt(yparts[0]);
		coords[3] = Integer.parseInt(yparts[1]);
		String[] zparts = parts[2].substring(2).split("\\.\\.");
		coords[4] = Integer.parseInt(zparts[0]);
		coords[5] = Integer.parseInt(zparts[1]);
		return coords;
	}
	
	private static void turnOn2(String str) {
		int coords[] = parseCoords2(str);		
		for (int[] onrange: onranges)
			if (overlap(coords, onrange)) {
				int intersection[] = intersect(coords, onrange);
				doublecounts.add(intersection);
			}
		onranges.add(coords);
	}

	// this was never implemented for approach 1, this is the approach 2 implementation
	private static void turnOff2(String str) {
		int coords[] = parseCoords2(str);
		for (int[] onrange: onranges)
			if (overlap(coords, onrange)) {
				// if overlap occurs then we are going to be double counting something (i.e, the intersection between coords and onrange) ... that intersection MUST itself be a cube (rectangular volume)
				int intersection[] = intersect(coords, onrange);
				doublecounts.add(intersection);
			}
		for (int[] dcrange: doublecounts)
			if (overlap(coords, dcrange)) {
				// if overlap occurs then we are going to be double counting something (i.e, the intersection between coords and onrange) ... that intersection MUST itself be a cube (rectangular volume)
				int intersection[] = intersect(coords, dcrange);
				onranges.add(intersection);
			}
	}
	
	private static int[] intersect(int[] coords, int[] onrange) {
		// we already know these overlap so the key is figuring out which parts of coords are within onrange
		return new int[] {Math.max(coords[0],onrange[0]), Math.min(coords[1], onrange[1]), Math.max(coords[2], onrange[2]), Math.min(coords[3], onrange[3]), Math.max(coords[4], onrange[4]), Math.min(coords[5], onrange[5])};
	}
	
}
