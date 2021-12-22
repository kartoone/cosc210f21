package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay19 {
	
	static class Subscanner {
		protected ArrayList<Subscanner> partners = new ArrayList<>();
		protected ArrayList<int[]> partnerorientation = new ArrayList<>();
		protected ArrayList<int[]> beacons = new ArrayList<>();
		protected int[] coords; // always relative to scanner 0 ... so scanner0 will always have coords [0,0,0]
		protected int scanneri;
		
		public Subscanner(int scanneri) {
			this.scanneri = scanneri;
		}
		
		@Override
		public String toString() {
			String ret = "";
			for (int[] beacon: beacons) {
				ret += Arrays.toString(beacon) + "\n";				
			}
			return ret;
		}
		// skip ourselves during this search
		public void findPartners(ArrayList<Subscanner> scanners) {
			for (Subscanner other:scanners) {
				System.out.println("finding partners for " + this.scanneri);
				if (this!=other && !partners.contains(other)) {
					int[] retmatch = isMatch(other);
					int orientationi = retmatch[3];
					if (orientationi<48){
//						System.out.println("found match: " + other.scanneri);
//						System.out.println(java.util.Arrays.toString(retmatch));
						partners.add(other);
						other.partners.add(this); // tell the newlyfound partner that we are its partner
						partnerorientation.add(retmatch);
						// calculate coords all the way back to scanner0 (hopefully)
//						other.coords = new int[] {retmatch[0]*orientation[retmatch[3]][3]-coords[0], retmatch[1]*orientation[retmatch[3]][4]-coords[1], retmatch[2]*orientation[retmatch[3]][5]-coords[2]};
						int multiply[] = new int[] {orientation[retmatch[3]][3], orientation[retmatch[3]][4], orientation[retmatch[3]][5]};
						System.out.println("multiply:" + Arrays.toString(multiply));	
						if (scanneri==0)
							other.coords = new int[] {coords[0]-retmatch[0], coords[1]-retmatch[1], coords[2]-retmatch[2]};
						else if (other.coords==null)
							other.coords = new int[] {coords[0]-retmatch[0]*multiply[0], coords[1]-retmatch[1]*multiply[1], coords[2]-retmatch[2]*multiply[2]};
						else if (coords==null) {
							coords = new int[] {other.coords[0]+retmatch[0]*-multiply[0], other.coords[1]+retmatch[1]*-multiply[1], other.coords[2]+retmatch[2]*-multiply[2]};
						}
//						System.out.println("othercoords:" + Arrays.toString(other.coords));	
					}
				}
			}			
		}
		
		// the way we are going to determine if we have a match is by rotating (by swapping x, y, z) and translating the other scanner
		// if there is a match, then we will update the other scanner by making it facing the same direction and orientation as "this" scanner ... if we try all the orientations and no match, there is no need to restore it to its original state since we will be rotating it again through all the orientations when comparing to other scanners
		// which means updating the beacon coords for the other partner.
		// if we find all of scanner0's partners first (updating their beacon coords along the way) and then find all 
		// the partners of scanner0's partners and so on ... we should end up knowing the locations of all scanners
		// and furthermore, their coords should be updated so that they all have the same x,y,z orientation as scanner0		
		public int[] isMatch(Subscanner other) {
			// pairwise find the offset from each beacon in this beacon to each of the other beacons
			// (but have to assume that the other beacon "readings" could have different x,y,z orientation (facing forwards or backwards may not matter ... the offesets should still be the same?)
			int orientationi = 48;
			for (int[] beacon: beacons)
				for (int[] otherbeacon : other.beacons)
					for (orientationi = 0; orientationi < 48; orientationi++) {
						int[] matchret = countMatches(beacon, otherbeacon, beacons, other.beacons, orientationi);
						if (matchret[3]>=12) {
							matchret[3] = orientationi;
							return matchret;
						}
					}
			return new int[] {0,0,0,orientationi};
		}
		
		static int[][] orientation = new int[][] {
				new int[] {0,1,2,1,1,1},
				new int[] {0,2,1,1,1,1},
				new int[] {1,0,2,1,1,1},
				new int[] {2,0,1,1,1,1},
				new int[] {1,2,0,1,1,1},
				new int[] {2,1,0,1,1,1},

				new int[] {0,1,2,-1,1,1},
				new int[] {0,2,1,-1,1,1},
				new int[] {1,0,2,-1,1,1},
				new int[] {2,0,1,-1,1,1},
				new int[] {1,2,0,-1,1,1},
				new int[] {2,1,0,-1,1,1},

				new int[] {0,1,2,1,-1,1},
				new int[] {0,2,1,1,-1,1},
				new int[] {1,0,2,1,-1,1},
				new int[] {2,0,1,1,-1,1},
				new int[] {1,2,0,1,-1,1},
				new int[] {2,1,0,1,-1,1},

				new int[] {0,1,2,1,1,-1},
				new int[] {0,2,1,1,1,-1},
				new int[] {1,0,2,1,1,-1},
				new int[] {2,0,1,1,1,-1},
				new int[] {1,2,0,1,1,-1},
				new int[] {2,1,0,1,1,-1},

				new int[] {0,1,2,-1,-1,1},
				new int[] {0,2,1,-1,-1,1},
				new int[] {1,0,2,-1,-1,1},
				new int[] {2,0,1,-1,-1,1},
				new int[] {1,2,0,-1,-1,1},
				new int[] {2,1,0,-1,-1,1},

				new int[] {0,1,2,-1,1,-1},
				new int[] {0,2,1,-1,1,-1},
				new int[] {1,0,2,-1,1,-1},
				new int[] {2,0,1,-1,1,-1},
				new int[] {1,2,0,-1,1,-1},
				new int[] {2,1,0,-1,1,-1},

				new int[] {0,1,2,1,-1,-1},
				new int[] {0,2,1,1,-1,-1},
				new int[] {1,0,2,1,-1,-1},
				new int[] {2,0,1,1,-1,-1},
				new int[] {1,2,0,1,-1,-1},
				new int[] {2,1,0,1,-1,-1},

				new int[] {0,1,2,-1,-1,-1},
				new int[] {0,2,1,-1,-1,-1},
				new int[] {1,0,2,-1,-1,-1},
				new int[] {2,0,1,-1,-1,-1},
				new int[] {1,2,0,-1,-1,-1},
				new int[] {2,1,0,-1,-1,-1},
		};
		
		public int[] countMatches(int[] beacon, int[] otherbeacon, ArrayList<int[]> beacons, ArrayList<int[]> otherbeacons, int orientationi) {
			int[] offset = new int[] {0, 0, 0};
			int[] other = new int[] {0, 0, 0};
			int xindex = orientation[orientationi][0];
			int yindex = orientation[orientationi][1];
			int zindex = orientation[orientationi][2];
			int multiply[] = new int[] {orientation[orientationi][3],orientation[orientationi][4],orientation[orientationi][5]};
			
			other[0] = otherbeacon[xindex]*multiply[xindex];
			other[1] = otherbeacon[yindex]*multiply[yindex];
			other[2] = otherbeacon[zindex]*multiply[zindex];

			// now we've updated other based on otherbeacon being in one of 24 different orientations
			offset[0] = other[0]-beacon[0];
			offset[1] = other[1]-beacon[1];
			offset[2] = other[2]-beacon[2];
			
			// now apply this offset to the other beacons and count number of "hits" to see if we are in the same location
			int matchcount = 0;
			for (int[] b: beacons)
				for (int[] ob: otherbeacons) {
					if (b[0]+offset[0]==ob[xindex]*multiply[xindex] && b[1]+offset[1]==ob[yindex]*multiply[yindex] && b[2]+offset[2]==ob[zindex]*multiply[zindex])
						matchcount++;
				}			
			
			// if match>=12 then we found a match! 
			if (matchcount >= 12) {
				return new int[] {offset[0],offset[1],offset[2],matchcount};
			}
			return new int[] {0,0,0,0};
		}
		
		public void updatebeacons(ArrayList<int[]> otherbeacons, int orientationi) {
			int xindex = orientation[orientationi][0];
			int yindex = orientation[orientationi][1];
			int zindex = orientation[orientationi][2];
			int multiply[] = new int[] {orientation[orientationi][3],orientation[orientationi][4],orientation[orientationi][5]};			
			for (int[] otherbeacon: otherbeacons) {
				otherbeacon[0] = otherbeacon[xindex]*multiply[xindex];
				otherbeacon[1] = otherbeacon[yindex]*multiply[yindex];
				otherbeacon[2] = otherbeacon[zindex]*multiply[zindex];
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day19.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		ArrayList<Subscanner> scanners = new ArrayList<>();
		Subscanner current = null;
		for (String line: lines) {
			if (line.length()==0) // only happens after we've created the first scanner
				scanners.add(current);
			else if (line.charAt(0)=='-'&&line.charAt(1)=='-') {
				current = new Subscanner(scanners.size());
			} else {
				String coordparts[] = line.split(",");
				int coords[] = new int[] {Integer.parseInt(coordparts[0]), Integer.parseInt(coordparts[1]), Integer.parseInt(coordparts[2])};
				current.beacons.add(coords);
			}
		}
		scanners.add(current); // add the final scanner
		
		// tell the scanners to find all of their matches (recursively, excluding themselves, of course)
		scanners.get(0).coords = new int[] {0,0,0};
		for (Subscanner scanner: scanners) {
			if (!matched.contains(scanner)) {
				scanner.findPartners(scanners);
				matched.add(scanner);
			}
		}
		
		// all beacons from perspective of scanner0 - pre-seeed with all of scanner 0s beacons, then branch out from its partners
		ArrayList<int[]> allbeacons = new ArrayList<>();
		for (int[] beacon: scanners.get(0).beacons)
			allbeacons.add(beacon);
		
		for (Subscanner scanner: scanners)
			System.out.println(Arrays.toString(scanner.coords));
		
		//68,-1246,-43
		//88,113,-1104
		
		
	}
	
	protected static ArrayList<Subscanner> matched = new ArrayList<>();
}
