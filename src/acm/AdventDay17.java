package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay17 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day17.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		// only one line in this program ... let's convert it to binary before we do anything else
		String puzzle = lines.get(0);
    	String parts[] = puzzle.substring(13).split(",");
    	int minx;
    	int maxx;
    	int miny;
    	int maxy;
    	String parts0[] = parts[0].split("=");
    	String parts1[] = parts[1].split("=");
		int minmax0[] = getminmax(parts0[1]);
		int minmax1[] = getminmax(parts1[1]);
    	if (parts0[0].equals("x")) {
    		// normal case: target area x encoded first
    		minx = minmax0[0];
    		maxx = minmax0[1];
    		miny = minmax1[0];
    		maxy = minmax1[1];
    	} else {
    		// in case target area encodes the y coord first for some reason
    		minx = minmax1[0];
    		maxx = minmax1[1];
    		miny = minmax0[0];
    		maxy = minmax0[1];
    	}
    	
    	// first idea is to determine min possible x velocity to even make it to the target
    	// since it is going to slow down by 1 each time ... also need to be able to handle "-minx"
    	// thinking about it a bit more, need to hand several distance scenarios
    	//    target entirely to left (negative minx and maxx)
    	//    target entirely to right (postivie minx and maxx)
    	//    target entirely above or below (honestly doesn't matter which!)
    	//      - the key with target entirely above or below is that if we shooot really high
    	//        then the veolocity by time it reaches target area could be going so fast that it is going to skip right over ... but no matter what the x velocity will be 0 (not that it matters)
    	//    target entirely to left can be handled same way as target to right b/c y-velocity won't change in either scenario ... so for part1 at least, we should just abolute value them if they are both negative
    	if (minx<0&&maxx<0) {
    		minx = Math.abs(minx);
    		maxx = Math.abs(maxx);
    	} else if (minx<=0&& maxx>=0) {
    		// target is directly above (or below us) since this if statement would include x==0
    		System.out.println("ERROR! directly above or below - not handled yet");
    	}
    	
    	// alright - our x can't go above maxx so max possible xvel = maxx ... this would get us to the target in one step
    	// we could keep decrementing until we get to the smallest possibel xvel that will get us over there
    	
    	// i'm leaving the situational checking above as is, but this problem didn't have all the weird edge cases it could have had
    	// there was only one target area and it was to the right and below the probe.
    	
   	
    	// for puzzle1 we don't need to calculate xvel at all ... simply yvel (and corresponding max height)
    	// but for puzzle2 we did need that 
    	// consider scenario where the target is immediately above you ... you need to shoot at high enough velocity such that
    	// the target gets missed on the way up and then hit on the way back down?
    	System.out.println(minx + " " + maxx + " " + miny + " " + maxy);
    	int x = 0;
    	int y = 0;
    	int velos=0;
    	
    	for (int yvel=1000; yvel>=-1000; yvel--) {
	    	for (int xvel=1000; xvel>0; xvel--) {
	    		x = 0;
	    		y = 0;
	    		int yv = yvel;
	    		int xv = xvel;
	    		int my = Integer.MIN_VALUE;
	    		for (int step=0; step<1000; step++) {
	    			y = y + yv;
		    		x = x + xv;
		    		if (y>my)
		    			my=y;
		    		if (x>=minx && x<=maxx && y>=miny && y<=maxy) {
		    			System.out.println(x + " " + y + " " + xvel + " " + my);  // puzzle 1 solution is the my that is FIRST printed out
		    			velos++; // puzzle2 solution (once we have stepped through all possible yvel, xvels)
		    			break;
		    		}
		    		xv--;
		    		yv--;
		    		if (xv<0)
		    			xv=0;
		    	}
	    	}
		}
    	
    	// for puzzle2, print out the count we were keeping track of for each new possible xvel,yvel pair
    	System.out.println(velos);
    	
	}

	// this was also pretty unnecessary b/c the problem was only ever run against one input ... we could have literally looked at the input and hard-coded the target area! but you didn't know that when you were looking at just puzzle 1 ... perhaps puzzle2 would have been to run the algorithm on a 1,000,000 different target areas ... but that wasn't what it eneded up being ... oh well!
	private static int[] getminmax(String string) {
		String parts[] = string.split("\\.\\.");
		int nums[] = new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
		if (nums[0] > nums[1]) {
			// oops swap order 
			int tmp = nums[0];
			nums[0] = nums[1];
			nums[1] = tmp;
		}
		return nums;
	}

}
