package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay16 {

	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day16.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	// Source: https://newbedev.com/java-hex-to-binary-conversion-java-code-example
	// Modified by me to make sure hex digits always represented by 4 bits
	protected static String parseHexBinary(String hex) {
		String digits = "0123456789ABCDEF";
  		hex = hex.toUpperCase();
		String binaryString = "";		
		for(int i = 0; i < hex.length(); i++) {
			char c = hex.charAt(i);
			int d = digits.indexOf(c);
			String dstr = Integer.toBinaryString(d);
			while (dstr.length()<4)
				dstr = "0" + dstr;
			binaryString += dstr;
		}
		return binaryString;
	}
	
	private static void puzzle1and2() throws FileNotFoundException {
//		// test to verify that an arraylist can properly sort longs
//		ArrayList<Long> test = new ArrayList<>();
//		test.add(9451234567890L);
//		test.add(1234567890L);
//		test.add(3451234567890L);
//		test.add(2451234567890L);
//		Collections.sort(test);
//		System.out.println(test);
		
		ArrayList<String> lines = extractLines();
		// only one line in this program ... let's convert it to binary before we do anything else
		String puzzle = lines.get(0);
    	String bits = parseHexBinary(puzzle);
    	System.out.println("op#\tevaluation");
    	long result[] = packet(bits);
    	System.out.println();
    	System.out.println(globalversionsum + " <---- part 1 solution (correct!)"); // puzzle 1 solution
    	System.out.println(result[0] + " <---- part 2 solution (supposedly wrong?)"); // puzzle 2 solution
    	System.out.println(bits.length() + " <---- length of bit string");
    	System.out.println((result[1]) + " <---- total number of bits consumed");
	}

	private static int globalversionsum = 0;
	private static int opcount = 1;
	private static long[] packet(String bits) {
		// all packets start with 3 bits that are the version number
		globalversionsum += Integer.parseInt(bits.substring(0,3), 2);
		// next is the packet "type" which guides we do next
		String id = bits.substring(3,6);
		if (id.equals("100")) {
			// found a "literal value"
			long[] litret = literal(bits.substring(6)); 
			return new long[] { litret[0], litret[1]+6 }; 
		} else {
			// found an "operator"
			long[] opret = operator(bits.substring(6), id); 
			return new long[] { opret[0], opret[1]+6 }; 
		}
	}

	// str should start immediately after version number and packet type id
	private static long[] literal(String str) {
//		System.out.println("literal");
		int ci = 0;
		// simply break out of this loop after we process the last number
		String litstr = ""; 
		while (true) {
			litstr += str.substring(ci+1,ci+5);
			if (str.charAt(ci)=='0')
				break;
			ci = ci + 5;
		}
		return new long[] { Long.parseLong(litstr, 2), ci+5 };
	}

	// substring should start immediately after version number and packet type id
	private static long[] operator(String opstr, String id) {
//		System.out.println("operator");
		ArrayList<Long> operands = new ArrayList<>();
		int consumed = 0;
		if (opstr.charAt(0)=='0') {
			// bit length
			int pktlength = Integer.parseInt(opstr.substring(1,16),2);
			int ci = 1 + 15;
			while (ci<pktlength+16) {
				long[] packetret = packet(opstr.substring(ci));
				operands.add(packetret[0]); // packetret[0] should have the "next" operand for this op
				ci += packetret[1];
			}
			consumed = 1 + 15 + pktlength;
		} else {
			int numpkts = Integer.parseInt(opstr.substring(1,12),2);
			int ci = 1 + 11;
			for (int i=0; i<numpkts; i++) {
				long[] packetret = packet(opstr.substring(ci));
				operands.add(packetret[0]); // packetret[0] should have the "next" operand for this op
				ci += packetret[1];
			}
			consumed = ci;
		}
		// now calculate the result
		String debugstr = "";
		long result = 0L;
		if (id.equals("000")) {
			// sum
			for (long l : operands) {
				debugstr += (l+"+");
				result = result + l;
			}
			debugstr = debugstr.substring(0,debugstr.length()-1);
			debugstr += "="+result;
			if (operands.size()==1)
				debugstr += " one operand addition";
		} else if (id.equals("001")) {
			// product
			result = 1L;
			for (long l : operands) {
				debugstr += (l+"*");
				result = result * l;		
			}
			debugstr = debugstr.substring(0,debugstr.length()-1);
			debugstr += "="+result;
			if (operands.size()==1)
				debugstr += " one operand multiplication";
		} else if (id.equals("010")) {
			Collections.sort(operands);
			result = operands.get(0);
			debugstr = operands + " min=" + result;
		} else if (id.equals("011")) {
			Collections.sort(operands);
			result = operands.get(operands.size()-1);
			debugstr = operands + " max=" + result;
		} else if (id.equals("101")) {
			result = operands.get(0)>operands.get(1) ? 1 : 0;
			debugstr = operands.get(0) + ">" + operands.get(1) + " ... " + result;;
		} else if (id.equals("110")) {
			result = operands.get(0)<operands.get(1) ? 1 : 0;
			debugstr = operands.get(0) + "<" + operands.get(1) + " ... " + result;;
		} else if (id.equals("111")) {
			result = operands.get(0)==operands.get(1) ? 1 : 0;
			debugstr = operands.get(0) + "==" + operands.get(1) + " ... " + result;;
		}
		System.out.println(opcount++ + "\t" + debugstr);
		return new long[] {result, consumed};
		// 158135827514  // using >= and <=  too high
		// 158135419991  // using > and <    too low
	}
}
