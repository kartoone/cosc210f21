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

	static class Onrange {
		int[] range;						// this is the original onrange  as follows: range[0]:minx, range[1]:maxx, range[2]:miny, range[3]:maxy, range[4]:minz, range[5]:maxz
		ArrayList<int[]> dontreallycount;	// rather than trying to preserve all the crazy shapes and pieces, we are simply going to have a list of regions not to really count
											// then when we go to count at the very end, we will simply ONLY count the coords within range that don't appear within any of the ranges within dontreallycount
		public Onrange(int[] range) {
			this.range = range;
			dontreallycount = new ArrayList<>();
		}
		
		// this should only be called after we have finished parsing the entire input file
		// idea is that the size of each "dontreallycount" should be subtracted from the total range size ... BUT the intersection of each dontreallycount with each other should be added back
		public long count() {
			long mycount = (range[1]-range[0]+1L)*(range[3]-range[2]+1L)*(range[5]-range[4]+1L);
			System.out.print("mycount: " + mycount);
			for (int i=0; i<dontreallycount.size(); i++) {
				int[] dontcount = dontreallycount.get(i);
				long dontcountsize = (dontcount[1]-dontcount[0]+1L)*(dontcount[3]-dontcount[2]+1L)*(dontcount[5]-dontcount[4]+1L);
				System.out.print(" " + dontcountsize);
				mycount = mycount - dontcountsize;
				// this inner for loop won't execute on the last one ... which is fine and perfectly handles the case where only have ONE dontreallycount range
				for (int j=i+1;j<dontreallycount.size(); j++) {
					int[] doubledontcount = dontreallycount.get(j);
					if (overlap(dontcount, doubledontcount)) {
						int[] doubledontcountintersection = intersect(dontcount, doubledontcount);
						long doubledontcountsize = (doubledontcountintersection[1]-doubledontcountintersection[0]+1L)*(doubledontcountintersection[3]-doubledontcountintersection[2]+1L)*(doubledontcountintersection[5]-doubledontcountintersection[4]+1L);
						System.out.print("["+doubledontcountsize+"]");
						mycount = mycount + doubledontcountsize; // add back the intersection of each dontreallycount with the other dontreallycounts 
					}
				}
			}				
			return mycount;
		}
		
		// Correct answer for example:
		// 2758514936282235
		
		// this should only be called after we have finished parsing the entire input file
		// naive approach ... still takes too long ... check every coordinate
		public long naivecount() {
			long mycount = 0L;
			
			for (int x=range[0];x<range[1];x++)
				for (int y=range[2];y<range[3];y++)
					for (int z=range[4];z<range[5];z++)
						if (IShouldBeCountedRightQuestionMark(x,y,z))
							mycount = mycount + 1L;
			return mycount;
		}
		
		public boolean IShouldBeCountedRightQuestionMark(int x, int y, int z) {
			for (int[] dontrange: dontreallycount)
				if (withinRange(x, y, z, dontrange))
					return false;
			// if we made it to here, then the x,y,z coord wasn't within any of the "dont count" ranges
			return true;
		}
		
		public boolean withinRange(int x, int y, int z, int[] range) {
			return x>=range[0]&&x<=range[1]&&y>=range[2]&&y<=range[3]&&z>=range[4]&&z<=range[5];
		}
	}
	
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
	
	private static int cube[][][] = new int[101][101][101];				// puzzle1 ... oh to just be able to do puzzle2 like this ... but we'd need exabytes of RAM
	private static ArrayList<int[]> posranges = new ArrayList<>();		// puzzle2 final approach
	private static ArrayList<int[]> negranges = new ArrayList<>();		// puzzle2 final approach
	private static ArrayList<Onrange> onranges = new ArrayList<>();		// puzzle2 next to last approach
//	private static ArrayList<int[]> onranges = new ArrayList<>();		// puzzle2 original approach
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
		
		// uggh, failed again ... due to doublecounting overload ... simpler approach is to handle global "positive cuboids" and global "negative cuboids"
		// got help from here ... realizing we could just do postive and negative volumes ... still wrote all the code myself, though! - https://www.reddit.com/r/adventofcode/comments/rlxhmg/comment/hqxczc4/?utm_source=share&utm_medium=web2x&context=3		
		// positive cuboid - essentially "on"
		// negative cuboid - not exactly "off" ... instead it's subtracting out (negative volume) to account for double counting
		//   when processing a new "on range", the intersecting cuboid with anything from the positive cuboids should be added to the negative cuboid ranges, likewise the intersecting cuboid with anything from the negative cuboids should be added back to the positive range
		//   when processing a new "off range", the intersecting cuboid with anything from the negative cuboid range should be added back to the positive cuboid range and vice versa 
		for (String line: lines) {
			if (line.charAt(2)==' ') {
				turnOn(line.substring(3)); // handle #1 here
			} else {
				turnOff(line.substring(4)); // handle #2 here
			}
		}
		
		long totalcount = 0L;
		for (int[] posrange: posranges) {
			totalcount += (posrange[1]-posrange[0]+1L)*(posrange[3]-posrange[2]+1L)*(posrange[5]-posrange[4]+1L);
		}
		for (int[] negrange: negranges) {
			totalcount -= (negrange[1]-negrange[0]+1L)*(negrange[3]-negrange[2]+1L)*(negrange[5]-negrange[4]+1L);
		}
		System.out.println(totalcount);
			
		// i tried to do this the optimal way, but I'm resorting to my original "intersection" instinct b/c the details of everything commented out below is far too complicated
		// #1 when turning on a range, you are actually turning all of them on ... it's the ones that are previously turned on that you need to add to the list of "don't really count" these to avoid double counting
		// #2 when turning off a range, you are going to add the intersection with each onranges to the list of don't really count these
//		for (String line: lines) {
//			if (line.charAt(2)==' ') {
//				turnOn0(line.substring(3)); // handle #1 here
//			} else {
//				turnOff0(line.substring(4)); // handle #2 here
//			}
//		}
//		
//		long totalcount = 0L;
//		int rangei = 1;
//		for (Onrange onrange: onranges) {
//			System.out.print("Counting range " + rangei++ + " out of " + onranges.size() + ": ");
//			long rangecount = onrange.count();
//			totalcount += rangecount;
//			System.out.println();
////			System.out.println(rangecount + " " + totalcount);
//		}
//		System.out.println(totalcount);
			
		// puzzle2 would take too long to run and no way to store that large a cube ... so instead just store
		// a list of ranges that are called the "onranges" ... as we parse in the procedure do the following:
		//  1) if the new range is an "on range", then we will be adding this "on range" as is no matter what!
		//		this is very important that none of the on ranges overlap at all or when we tally up the total at the end it will be too big
		//      the idea is that all the current "on ranges" are distinct.
		//      so adding this "on range" should cause some of the other "on ranges" to shrink ... or split ... or completely disappear (i.e., this new range completely engulfs it)
		//      the other approach is to figure out exactly which part(s) of this new "on range" are entirely new and only add those part(s)
		//      the reason why i am leaning towards the first approach is because I didn't want to deal with splitting up the new cube in a million ways and it seems less likely that adding a whole entire cube would cause as many splits? that could be a bad assumption, but I think it should work with either approach and the first will be faster ... possibly .... but probably not
		//  2) if the new range is an "off range", then iterate through current list of "on ranges" and shrink, split, or completely remove as appropriate
//		for (String line: lines) {
//			if (line.charAt(2)==' ') {
//				turnOn1(line.substring(3)); // handle #1 here
//			} else {
//				turnOff1(line.substring(4)); // handle #2 here
//			}
//		}

		// "overlapping" cubes approach - taking way too long to figure out and get details right ... abandonded (all the code commented out below)
		// now that we have all the "unique" on ranges, simply count them up ... while this approach would work by itself ... the details of making sure the onranges are distinct (splitting, shrinking, removing, etc...) is nasty and feels very error prone after accounting for just one dimension ... the though of two additional dimensions was daunting
		// so in approach number 2 we still have a set of "onranges" ... but we also keep track of "double counting" by inserting the intersection of any new onrange with any of the existing ranges into the double counting array
		// at the very end, count up all the "ons" but then subtract all the "double counts"
		// what about the "offs" ... each time an "off" is processed ... remove the intersection from both the "ons" and "double counts" ... which brings us back to original scenario of approach 1 where removing an intersection is HARD ...
		//
		// in any case, I wasn't getting the correct answer using approach 2 so I'm switching back to approach 1. My instinct with what is wrong with approach 2 is that when you remove something from doublecount by readding the intersection back into
		// onranges ... this doesn't work out right for future turn ons turn offs ... somehow ... although it seemes logically sound. IN any case I'm confident about approach 2 - just  a little concerened with the sheer amount of code involved and the 
		// likelihood of having one or more small errors somewhere in it!
//		long oncount2 = 0L;
//		for (int[] onrange: onranges)
//			oncount2 = oncount2 + (long)(onrange[1]-onrange[0]+1)*(long)(onrange[3]-onrange[2]+1)*(long)(onrange[5]-onrange[4]+1); // note we don't need absolute value here as long as we make sure second coord greater than first for each dimension (which it is for all the inputs, so just have to make sure when we are splitting we don't mess things up!)
//
//		System.out.println(oncount2);
	}
	
	private static void turnOff(String str) {
		int coords[] = parseCoords2(str);
		if (coords[1]<coords[0]||coords[3]<coords[2]||coords[5]<coords[4])
			return;	
		ArrayList<int[]> negAdd = new ArrayList<>();
		Iterator<int[]> it = posranges.iterator();
		while (it.hasNext()) {
			int[] posrange = it.next();
			if (overlap(posrange, coords)) {
				negAdd.add(intersect(posrange, coords));
			}
		}
		ArrayList<int[]> posAdd = new ArrayList<>();
		it = negranges.iterator();
		while (it.hasNext()) {
			int[] negrange = it.next();
			if (overlap(negrange, coords)) {
				posAdd.add(intersect(negrange, coords));
			}
		}
		negranges.addAll(negAdd);
		posranges.addAll(posAdd);
	}
	
	private static void turnOn(String str) {
		int coords[] = parseCoords2(str);
		if (coords[1]<coords[0]||coords[3]<coords[2]||coords[5]<coords[4])
			return;	
		ArrayList<int[]> negAdd = new ArrayList<>();
		Iterator<int[]> it = posranges.iterator();
		while (it.hasNext()) {
			int[] posrange = it.next();
			if (overlap(posrange, coords)) {
				negAdd.add(intersect(posrange, coords));
			}
		}
		ArrayList<int[]> posAdd = new ArrayList<>();
		it = negranges.iterator();
		while (it.hasNext()) {
			int[] negrange = it.next();
			if (overlap(negrange, coords)) {
				posAdd.add(intersect(negrange, coords));
			}
		}
		negranges.addAll(negAdd);
		posranges.addAll(posAdd);
		posranges.add(coords);
	}

	// pretty straightforward ... just add the intersection with any onrange to its dontreally count list
	private static void turnOff0(String str) {
		int coords[] = parseCoords1(str);
		if (coords[1]<coords[0]||coords[3]<coords[2]||coords[5]<coords[4])
			return;
		Iterator<Onrange> it = onranges.iterator();
		while (it.hasNext()) {
			Onrange onrange = it.next();
			if (overlap(onrange.range, coords)) {
				onrange.dontreallycount.add(intersect(onrange.range, coords));
			}
		}
	}
	
	private static void turnOn0(String str) {
		int coords[] = parseCoords1(str);
		if (coords[1]<coords[0]||coords[3]<coords[2]||coords[5]<coords[4])
			return;
		Onrange newrange = new Onrange(coords); // we will be unconditionally adding this "on range" ... but we won't add it until after we have adjusted all the previous "on ranges" to make sure we don't count anything that intersects with this new on range
		Iterator<Onrange> it = onranges.iterator();
		while (it.hasNext()) {
			Onrange onrange = it.next();
			if (overlap(onrange.range, coords)) {
				onrange.dontreallycount.add(intersect(onrange.range, coords));
			}
		}
		onranges.add(newrange);
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
	
	private static int[] intersect(int[] coords, int[] onrange) {
		// we already know these overlap so the key is figuring out which parts of coords are within onrange
		return new int[] {Math.max(coords[0],onrange[0]), Math.min(coords[1], onrange[1]), Math.max(coords[2], onrange[2]), Math.min(coords[3], onrange[3]), Math.max(coords[4], onrange[4]), Math.min(coords[5], onrange[5])};
	}

//	private static void turnOff1(String str) {
//		int coords[] = parseCoords2(str);
//		ArrayList<int[]> newsplits = new ArrayList<>(); // let's keep track of any new splits we need to add here ... and then add them after the iteration is done (so that we don't mess with the onranges list while we are iterating)
//		Iterator<int[]> it = onranges.iterator();
//		while (it.hasNext()) {
//			int[] onrange = it.next();
//			if (within(onrange, coords)) {
//				it.remove(); // if onrange entirely within coords ... remove entire range
//			} else if (within(coords, onrange)) {
//				// oh boy, this one is tough we are essentially taking "bite" out of middle of the onrange
//			}
//		}
//	}
//	
//	private static void turnOn1(String str) {
//		int coords[] = parseCoords2(str);
////		System.out.println("on: " + Arrays.toString(coords));
//		ArrayList<int[]> newsplits = new ArrayList<>(); // let's keep track of any new splits we need to add here ... and then add them after the iteration is done (so that we don't mess with the onranges list while we are iterating)
//		Iterator<int[]> it = onranges.iterator();
//		while (it.hasNext()) {
//			int[] onrange = it.next();
//			// shrink, split, or remove onrange based on the coords of the current on range we are going to add
//			if (within(onrange, coords)) {
//				it.remove(); // if the existing range is entirely within the new coords ... remove the existing range
//			} else if (within(coords, onrange)) {				
//				return;    // if the new range is entirely within an existing range ... fastest thing to do is just simply ignore the new range altogether ... note that this is ONLY POSSIBLE if the new range doesn't intersect at all with any other ranges
//			} else if (overlap(coords, onrange)) {
//				// let's handle the "shrink cases first" ... this is where two dimensions are both fully covered by the new range
//				// in this case the other dimension needs to be shrunk by whatever amount that third dimension is not covered
//				int xamt = shrinkx(coords, onrange);
//				if (xamt!=0) {
//					// negative xamt means shrink from the right
//					// positive xamt means shrink from the left
//					if (xamt>0)
//						onrange[0] = onrange[0]+xamt;
//					else
//						onrange[1] = onrange[1]+xamt; // (xamt will be negative so just add it to subtract ... note that this works for negative ranges too!);
//					continue; // we are done with this onrange if it was determined it only needed to shrink in one dimension
//				}
//				int yamt = shrinky(coords, onrange);
//				if (yamt!=0) {
//					// negative amt means shrink from the top
//					// positive amt means shrink from the bottom
//					if (yamt>0)
//						onrange[2] = onrange[2]+yamt;
//					else
//						onrange[3] = onrange[3]+yamt; // (amt will be negative so just add it to subtract ... note that this works for negative ranges too!);
//					continue; // we are done with this onrange if it was determined it only needed to shrink in one dimension
//				}
//				int zamt = shrinkz(coords, onrange);
//				if (zamt!=0) {
//					// negative amt means shrink from the inside (i.e., the part hidden from you, away from you)
//					// positive amt means shrink from the outside (i.e., the part visible to you, closer to you)
//					if (zamt>0)
//						onrange[4] = onrange[4]+zamt;
//					else
//						onrange[5] = onrange[5]+zamt; // (amt will be negative so just add it to subtract ... note that this works for negative ranges too!);
//					continue; // we are done with this onrange if it was determined it only needed to shrink in one dimension
//				}
//
//				// no matter what we are going to have to split the existing range into two or more pieces
//				// worst case is 5 pieces: the part "above" the bite is one big flat piece ... the part to the left (but not above), to the right (but not above), the part in front (but not left or right or above), the part in back (but not left or right or above)   
//				int bitesplits[] = new int[5];
//				if (bitex(coords, onrange, bitesplits)) {
//					it.remove(); // no matter what we are splitting the original range into pieces and going to add them back individually, so remove the original giant piece
//					// split with the bite coming from the x direction (i.e., from the left or right)
//					// this means we will have one big chunk with full y and z dimensions on the opposite side that the bite is coming from ... this is determined by coord0 ... if negative bite came from the right
//					if (bitesplits[0]<0) {
//						// coming from the right - let's add the "outside" chunk which would be the long deep chunk to the left ... but only if we didn't completely consume it with our bite 
//						if (onrange[0]<onrange[1]+bitesplits[0])
//							newsplits.add(new int[] {onrange[0],onrange[1]+bitesplits[0], onrange[2], onrange[3], onrange[4], onrange[5]});
//						
//						// now let's handle the chunk in front of the bite ... this chunk won't exist if the minz<=onrange[4]
//						if (bitesplits[3]>=0) {
//							newsplits.add(new int[] {Math.max(onrange[0],onrange[1]+bitesplits[0]),onrange[1], onrange[2], onrange[3], onrange[4], onrange[4]+bitesplits[3]});
//						}
//						// now let's handle the chunk behind the bite ... this chunk won't exist if the maxz>=onrange[5]
//						if (bitesplits[4]>=0) {
//							newsplits.add(new int[] {Math.max(onrange[0],onrange[1]+bitesplits[0]),onrange[1], onrange[2], onrange[3], onrange[5]-bitesplits[4], onrange[5]});
//						}
//						// now let's handle the chunk below the bite ... this chunk won't exist if the miny<=onrange[2]
//						if (bitesplits[1]>=0) {
//							newsplits.add(new int[] {Math.max(onrange[0],onrange[1]+bitesplits[0]),onrange[1], onrange[2], onrange[2]+bitesplits[1], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
//						}
//						// now let's handle the final potential chunk above the bite ... this chunk won't exist if the maxy>=onrange[3]
//						if (bitesplits[2]>=0) {
//							newsplits.add(new int[] {Math.max(onrange[0],onrange[1]+bitesplits[0]),onrange[1], onrange[3]-bitesplits[2], onrange[3], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
//						}						
//					} else {
//						// coming from the left ... this will be the chunk to the right if it didn't completely bite through the onrange (the if statement below handles that situation)
//						if (onrange[0]+bitesplits[0]<onrange[1])
//							newsplits.add(new int[] {onrange[0]+bitesplits[0],onrange[1], onrange[2], onrange[3], onrange[4], onrange[5]});
//						
//						// now let's handle the chunk in front of the bite ... this chunk won't exist if the minz<=onrange[4]
//						if (bitesplits[3]>=0) {
//							newsplits.add(new int[] {onrange[0],Math.min(onrange[1],onrange[0]+bitesplits[0]), onrange[2], onrange[3], onrange[4], onrange[4]+bitesplits[3]});
//						}
//						// now let's handle the chunk behind the bite ... this chunk won't exist if the maxz>=onrange[5]
//						if (bitesplits[4]>=0) {
//							newsplits.add(new int[] {onrange[0],Math.min(onrange[1],onrange[0]+bitesplits[0]), onrange[2], onrange[3], onrange[5]-bitesplits[4], onrange[5]});
//						}
//						// now let's handle the chunk below the bite ... this chunk won't exist if the miny<=onrange[2]
//						if (bitesplits[1]>=0) {
//							newsplits.add(new int[] {onrange[0],Math.min(onrange[1],onrange[0]+bitesplits[0]), onrange[2], onrange[2]+bitesplits[1], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
//						}
//						// now let's handle the final potential chunk above the bite ... this chunk won't exist if the maxy>=onrange[3]
//						if (bitesplits[2]>=0) {
//							newsplits.add(new int[] {onrange[0],Math.min(onrange[1],onrange[0]+bitesplits[0]), onrange[3]-bitesplits[2], onrange[3], onrange[4]+bitesplits[3], onrange[5]-bitesplits[4]});
//						}												
//					}
//					
//				} else if (bitey(coords, onrange, bitesplits)) {
//					it.remove(); // no matter what we are splitting the original range into pieces and going to add them back individually, so remove the original giant piece
//					// split with the bite coming from the y direction (i.e., from the top or bottom)
//					if (bitesplits[0]<0) { // coming from the top
//
//						// check to make sure we didn't completely slice through from the top
//						if (onrange[2]<onrange[3]+bitesplits[0]) 
//							newsplits.add(new int[] {onrange[0], onrange[1], onrange[2], onrange[3]+bitesplits[0], onrange[4], onrange[5]});
//						
//						// now handle chunk to left (oriented facing in z direction)
//						if (bitesplits[1]>0) {
//							newsplits.add(new int[] {onrange[0], onrange[1], Math.max(onrange[2], onrange[3]+bitesplits[0]), onrange[3], onrange[4], onrange[5]});							
//						}
//						
//					}
//				} else if (bitez(coords, onrange, bitesplits)) {
//					it.remove(); // no matter what we are splitting the original range into pieces and going to add them back individually, so remove the original giant piece
//					// split with the bite coming from the y direction (i.e., from the top or bottom)
//
//				}
//				
//				
//			} else {
//				// nothing to do if there is no overlap at all!
//			}
//		}
//		// if we made it to here than all existing ranges should have been split, shrunk, or removed as necessary to allow us to add the new range in its entirety
//		onranges.add(coords);
//		
//		// let's add any new splits we created along the way
//		for (int[] split:newsplits)
//			onranges.add(split);
//	}
//	
//	// return true if the bite is coming from the left or right ... bitesplits[0] is the amount and direction (- = left, + = right) ... don't mess with bitesplits[0] if the bite is not coming from left or right simply return false
//	// bitesplits[0] - the "x" bite ... negative (from right) positive (from left)
//	// bitesplits[1] - the chunk below the bite ... positive means there really is a chunk below the bite ... 0 means the bite extends all the way to the bottom
//	// bitesplits[2] - the chunk above the bite ... positive means there really is a chunk above the bite ... 0 means the bite extends all the way to the top 
//	// bitesplits[3] - the chunk in front of the bite (to the left if you are looking from the direction of the bite coming from the right)
//	// bitesplits[4] - the chunk behind the bite (to the right if you are looking from directions of the bite coming from the right)
//	private static boolean bitex(int[] coords, int[] onrange, int[] bitesplits) {
//		// first verify that this really is a horizontal bite ... (left or right)
//		// this means that the coords maxx is greater than the onrange maxx and the coords minx is less than the onrange maxx (coming from the right)
//		// OR the coords maxx is greater than the onrange minx and the coords minx is less than the onrange minx (coming from the left)
//		boolean flagx = false;
//		if (coords[1]>onrange[1] && coords[0]<onrange[1]) { // coming from the right
//			bitesplits[0] = coords[0]-onrange[1];
//			flagx = true;
//		} else if (coords[1]>onrange[0] && coords[0]<onrange[0]) { // coming from the left
//			bitesplits[0] = coords[1]-onrange[0];
//			flagx = true;
//		}
//		
//		// now let's handle the other splits -- but only if we aren't returning false
//		if (flagx) {
//			if (coords[2]>onrange[2]) { // coords miny > onrange miny means that there should still be some onrange below this "bite" 
//				bitesplits[1] = Math.min(coords[2]-onrange[2], onrange[3]-onrange[2]);
//			} else {
//				bitesplits[1] = 0;
//			}
//			if (coords[3]<onrange[3]) { // coords maxy < onrange maxy means that there should still be some onrange above this "bite"
//				bitesplits[2] =  Math.min(onrange[3]-coords[3], onrange[3]-onrange[2]);
//			} else {
//				bitesplits[2] = 0;
//			}
//			if (coords[4]>onrange[4]) {
//				bitesplits[3] =  Math.min(coords[4]-onrange[4], onrange[5]-onrange[4]);				
//			} else {
//				bitesplits[3] = 0;
//			}
//			if (coords[5]<onrange[5]) {
//				bitesplits[4] = onrange[5]-coords[5];				
//			} else {
//				bitesplits[4] = 0;
//			}
//		}
//			
//		return flagx;
//	}
//
//	// return true if the bite is coming from the top or bottom ... bitesplits[0] is the amount and direction (- = top, + = bottom) ... don't mess with bitesplits[0] if the bite is not coming from top or bottom simply return false
//	// bitesplits[0] - the "y" bite ... negative (from top) positive (from bottom)
//	// bitesplits[1] - the chunk to left of the bite ... positive means there really is a chunk left of the bite ... 0 means the bite extends all the way to the left
//	// bitesplits[2] - the chunk to right of the bite ... positive means there really is a chunk right of the bite ... 0 means the bite extends all the way to the right
//	// bitesplits[3] - the chunk in front of the bite (z direction)
//	// bitesplits[4] - the chunk behind the bite (z direction)
//	private static boolean bitey(int[] coords, int[] onrange, int[] bitesplits) {
//		// first verify that this really is a vertical bite ... (top or bottom)
//		// this means that the coords maxy is greater than the onrange maxy and the coords miny is less than the onrange maxy (coming from the top)
//		// OR the coords maxy is greater than the onrange miny and the coords miny is less than the onrange miny (coming from the bottom)
//		boolean flagy = false;
//		if (coords[3]>onrange[3] && coords[2]<onrange[3]) { // coming from the top
//			bitesplits[0] = coords[2]-onrange[3];
//			flagy = true;
//		} else if (coords[3]>onrange[2] && coords[2]<onrange[2]) { // coming from the bottom
//			bitesplits[0] = coords[3]-onrange[2];
//			flagy = true;
//		}
//		// now let's handle the other splits -- but only if we aren't returning false
//		if (flagy) {
//			if (coords[0]>onrange[0]) {  
//				bitesplits[1] = coords[0]-onrange[0];
//			} else {
//				bitesplits[1] = 0;
//			}
//			if (coords[1]<onrange[1]) { 
//				bitesplits[2] = onrange[1]-coords[1];
//			} else {
//				bitesplits[2] = 0;
//			}
//			if (coords[4]>onrange[4]) {
//				bitesplits[3] = coords[4]-onrange[4];				
//			} else {
//				bitesplits[3] = 0;
//			}
//			if (coords[5]<onrange[5]) {
//				bitesplits[4] = onrange[5]-coords[5];				
//			} else {
//				bitesplits[4] = 0;
//			}
//		}
//			
//		return flagy;
//	}
//	private static boolean bitez(int[] coords, int[] onrange, int[] bitesplits) {
//		boolean flagz = false;
//		if (coords[5]>onrange[5] && coords[4]<onrange[5]) { // coming from the top
//			bitesplits[0] = coords[4]-onrange[5];
//			flagz = true;
//		} else if (coords[5]>onrange[4] && coords[4]<onrange[4]) { // coming from the bottom
//			bitesplits[0] = coords[5]-onrange[4];
//			flagz = true;
//		}
//		// now let's handle the other splits -- but only if we aren't returning false
//		if (flagz) {
//			if (coords[0]>onrange[0]) {  
//				bitesplits[1] = coords[0]-onrange[0];
//			} else {
//				bitesplits[1] = 0;
//			}
//			if (coords[1]<onrange[1]) { 
//				bitesplits[2] = onrange[1]-coords[1];
//			} else {
//				bitesplits[2] = 0;
//			}
//			if (coords[2]>onrange[2]) {
//				bitesplits[3] = coords[2]-onrange[2];				
//			} else {
//				bitesplits[3] = 0;
//			}
//			if (coords[3]<onrange[3]) {
//				bitesplits[4] = onrange[3]-coords[3];				
//			} else {
//				bitesplits[4] = 0;
//			}
//		}			
//		return flagz;
//	}
//
//	private static int shrinkx(int[] range1, int[] range2) {
//		if (range2[2]>=range1[2] &&
//			range2[3]<=range1[3] &&
//			range2[4]>=range1[4] &&
//			range2[5]<=range1[5]) {
//			// yep we need to shrink range2 in the x direction ... two situations we are overtaking range2 from the left (range1 minx < range2 minx) or we are overtaking it from the right (range1 maxx > range2 maxx)
//			if (range1[0]<range2[0])
//				return range1[1]-range2[0]; // should be positive
//			else
//				return range1[0]-range2[1]; // should be negative
//		}
//		return 0; // if we make it to here then it didn't fully overlap in the other dimensions
//	}
//	
//	private static int shrinky(int[] range1, int[] range2) {
//		if (range2[0]>=range1[0] &&
//			range2[1]<=range1[1] &&
//			range2[4]>=range1[4] &&
//			range2[5]<=range1[5]) {
//			// yep we need to shrink range2 in the y direction ... two situations we are overtaking range2 from the left (range1 miny < range2 miny) or we are overtaking it from the right (range1 maxy > range2 maxy)
//			if (range1[2]<range2[2])
//				return range1[3]-range2[2]; // should be positive
//			else
//				return range1[2]-range2[3]; // should be negative
//		}
//		return 0; // if we make it to here then it didn't fully overlap in the other dimensions
//	}
//	
//	private static int shrinkz(int[] range1, int[] range2) {
//		if (range2[2]>=range1[2] &&
//			range2[3]<=range1[3] &&
//			range2[0]>=range1[0] &&
//			range2[1]<=range1[1]) {
//			// yep we need to shrink range2 in the z direction ... two situations we are overtaking range2 from the left (range1 minx < range2 minx) or we are overtaking it from the right (range1 maxz > range2 maxz)
//			if (range1[4]<range2[4])
//				return range1[5]-range2[4]; // should be positive
//			else
//				return range1[4]-range2[5]; // should be negative
//		}
//		return 0; // if we make it to here then it didn't fully overlap in the other dimensions
//	}
//		
	
}
