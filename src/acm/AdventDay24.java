package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class AdventDay24 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day24.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}
	
	static long w=0; 
	static long x=0;
	static long y=0;
	static long z=0;
	static long parts3val = 0;
	static int debugcnt = 0;
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		optimized();
//		bruteforce(lines);
	}
	
	// based on https://github.com/kemmel-dev/AdventOfCode2021/blob/master/day24/AoC%20Day%2024.pdf
	// our modified input is simply going to be the "checks" and "offsets"
	// checks:  11	11	14	11	-8	-5	11	-13	12	-1	14	-5	-4	-8
	// offsets: 1	11	1	11	2	9	7	11	6	15	7	1	8	6
	// stack.push(digit[0]+1)
	// stack.push(digit[1]+11)
	// stack.push(digit[2]+1)
	// stack.push(digit[3]+11)
	// stack.pop() // digit[4] == stack.pop() -8
	// stack.pop() // digit[5] == stack.pop() -5
	// stack.push(digit[6]+7)
	// stack.pop() // digit[7] == stack.pop() -13
	// stack.push(digit[8]+6)
	// stack.pop() // digit[9] == stack.pop() -1
	// stack.push(digit[10]+7)
	// stack.pop() // digit[11] == stack.pop() -5
	// stack.pop() // digit[12] == stack.pop() -4
	// stack.pop() // digit[13] == stack.pop() -8'
	// simplifying...
	// digit[4] = digit[3]+11 - 8 		= digit[3] + 3
	// digit[5] = digit[2]+1 - 5		= digit[2] - 4
	// digit[7] = digit[6]+7 - 13		= digit[6] - 6
	// digit[9] = digit[8]+6 - 1		= digit[8] + 5
	// digit[11] = digit[10]+7 - 5		= digit[10] + 2
	// digit[12] = digit[1] + 11 - 4	= digit[1] + 7
	// digit[13] = digit[0] + 1 - 8		= digit[0] - 7
	private static void optimized() {
		int greatest[] = new int[14];
		greatest[0] = 9; // these are caclulated by filling in the equations above with a 9 on right for subtractions and on left for additions
		greatest[1] = 2;
		greatest[2] = 9;
		greatest[3] = 6;
		greatest[4] = 9;
		greatest[5] = 5;
		greatest[6] = 9;
		greatest[7] = 3;
		greatest[8] = 4;
		greatest[9] = 9;
		greatest[10] = 7;
		greatest[11] = 9;
		greatest[12] = 9;
		greatest[13] = 2;
		for (int i : greatest) {
			System.out.print(i);
		}
		System.out.println();
		int smallest[] = new int[14];
		smallest[0] = 8; // these are caclulated by filling in the equations above with a 1 on right for additions and on left for subtractions
		smallest[1] = 1;
		smallest[2] = 5;
		smallest[3] = 1;
		smallest[4] = 4;
		smallest[5] = 1;
		smallest[6] = 7;
		smallest[7] = 1;
		smallest[8] = 1;
		smallest[9] = 6;
		smallest[10] = 1;
		smallest[11] = 3;
		smallest[12] = 8;
		smallest[13] = 1;
		for (int i : smallest) {
			System.out.print(i);
		}
	}
	
	private static void bruteforce(ArrayList<String> lines) throws FileNotFoundException {
		long modelnum = 11111111111111L; 
		while (modelnum<=99999999999999L) {
			String modelno = ""+modelnum;
			int zerop = modelno.indexOf("0");
			if (zerop==13) {
				modelnum = modelnum + 1L;
				continue;
			} else if (zerop==12) {
				modelnum += 11L;
				continue;
			} else if (zerop==11) {
				modelnum += 111L;
			} else if (zerop==10) {
				modelnum += 1111L;
			} else if (zerop==9) {
				modelnum += 11111L;
			} else if (zerop==8) {
				modelnum += 111111L;
			} else if (zerop==7) {
				modelnum += 1111111L;
			} else if (zerop==6) {
				modelnum += 11111111L;
			} else if (zerop==5) {
				modelnum += 111111111L;
			} else if (zerop==4) {
				modelnum += 1111111111L;
			} else if (zerop==3) {
				modelnum += 11111111111L;
			} else if (zerop==2) {
				modelnum += 111111111111L;
			} else if (zerop==1) {
				modelnum += 1111111111111L;
			} else {
				modelnum = modelnum + 1L;
			}
			if (evalmodel(modelno,lines)) {
				System.out.println(modelno); // puzzle 1 solution
				break;
			}
			if (modelnum % 111111 == 0)
				System.out.println(modelnum + " " + w + " " + x + " " + y + " " + z);
		}
	}

	// dp1 caches w,x,y,z values to use and what line number to start on
	private static Map<Integer,Map<String, long[]>> dp1 = new HashMap<>();
	static {
		for (int i=4; i<=12; i++)
			dp1.put(i, new HashMap<String, long[]>());
	}

	// dp2 stores suffixes we don't need to check when arriving with given w x y z starting point before seeing that suffix
	private static Map<String,Boolean> dp2 = new HashMap<>();
	
	static int storei = 12;
	static String storekey = "";
	private static boolean evalmodel(String modelno, ArrayList<String> lines) throws FileNotFoundException {
		int linei = 0;
		int inpi = 0;
		storekey = "";
		// now check each potential starting point "dp"
		for (storei=12; storei>=4; storei--) {
			if (dp1.get(storei).containsKey(modelno.substring(0, storei))) {
				long[] vars = dp1.get(storei).get(modelno.substring(0,storei));
				w = vars[0];
				x = vars[1];
				y = vars[2];
				z = vars[3];
				inpi = storei;
				linei = (int)vars[4]; // line number
				break;
			}
		}
		if (storei<4) {
			System.out.println(modelno);
			linei = 0;
			inpi = 0;
			w=0;
			x=0;
			y=0;
			z=0;
		}			
		parts3val=0;
		int numlines = lines.size();
		for (; linei<numlines; linei++) {
			String line = lines.get(linei);
			String parts[] = line.split(" ");
			switch (parts[0]) {
			case "inp":
//				System.out.println("w x y z: " + w + " " + x + " " + y + " " + z);
				// see if we can break out early b/c we have already made it this far given a suffix "left to go" 
				if (inpi==6 && dp2.containsKey(w + " " + x + " " + y + " " + z + " " + modelno.substring(inpi))) {
					System.out.println("stopped early: " + w + " " + x + " " + y + " " + z + " " + modelno.substring(inpi));
					return false;
				} else if (inpi==6)
					storekey = w + " " + x + " " + y + " " + z + " " + modelno.substring(inpi);
				if (inpi>=storei && inpi >= 4 && inpi<=12) {
//					System.out.println(linei + " " + inpi);
//					if (true)
//						return true;
					dp1.get(inpi).put(modelno.substring(0,inpi), new long[] {w, x, y, z, linei});
				}
				// target variable
				switch (parts[1]) {
				case "w": w= modelno.charAt(inpi++)-48; break;
				case "x": x= modelno.charAt(inpi++)-48; break;
				case "y": y= modelno.charAt(inpi++)-48; break;
				case "z": z= modelno.charAt(inpi++)-48; break;
				}
				break;
			case "add":
				getPart3(parts[2]);
				switch (parts[1]) {
				case "w": w= w + parts3val; break;
				case "x": x= x + parts3val; break;
				case "y": y= y + parts3val; break;
				case "z": z= z + parts3val; break;
				}
				break;
			case "mod":
				getPart3(parts[2]);
				if (parts3val<=0)
					return false;
				switch (parts[1]) {
				case "w": w= w % parts3val; break;
				case "x": x= x % parts3val; break;
				case "y": y= y % parts3val; break;
				case "z": z= z % parts3val; break;
				}
				break;
			case "div":
				getPart3(parts[2]);
				if (parts3val==0)
					return false;
				switch (parts[1]) {
				case "w": w= w / parts3val; break;
				case "x": x= x / parts3val; break;
				case "y": y= y / parts3val; break;
				case "z": z= z / parts3val; break;
				}
				break;
			case "mul":
				getPart3(parts[2]);
				switch (parts[1]) {
				case "w": w= w * parts3val; break;
				case "x": x= x * parts3val; break;
				case "y": y= y * parts3val; break;
				case "z": z= z * parts3val; break;
				}
				break;
			case "eql":
				getPart3(parts[2]);
				switch (parts[1]) {
				case "w": w= w==parts3val?1:0; break;
				case "x": x= x==parts3val?1:0; break;
				case "y": y= y==parts3val?1:0; break;
				case "z": z= z==parts3val?1:0; break;
				}
				break;
			}
		}
//		System.out.println(w + " " + x + " " + y + " " + z);
//		System.out.println();
//		if (debugcnt++ > 10)
//			return true;
//		else
		if (storekey.length()>0)
			dp2.put(storekey, z==0);
		return z==0;
	}

	private static void getPart3(String str) {
		try {
			parts3val = Long.parseLong(str);
		} catch (Exception ex) {
			switch (str) {
			case "w": parts3val=w; break;
			case "x": parts3val=x; break;
			case "y": parts3val=y; break;
			case "z": parts3val=z; break;
			}
		}
	}
	
}
