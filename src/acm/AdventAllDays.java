package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventAllDays {

	public static void main(String[] args) throws FileNotFoundException {
		day1a();
		day1b(3);
		day2a();
		day2b();
		day3a();
		day3b();
	}

	private static void day3a() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day3a.txt"));
		ArrayList<String> readings = new ArrayList<>();
		while (filein.hasNextLine()) {
			readings.add(filein.nextLine());
		}
		String gamma = "";
		String epsilon = "";
		for (int p=0; p<readings.get(0).length(); p++) {
			int zeros = 0;
			int ones = 0;
			for (String reading : readings) {
				if (reading.charAt(p)=='0')
					zeros++;
				else
					ones++;
			}
			if (zeros>=ones) {
				gamma+="0";
				epsilon+="1";
			} else {
				gamma+="1";
				epsilon+="0";
			}
		}
		int gammareading = Integer.parseInt(gamma,2);
		int epsilonreading = Integer.parseInt(epsilon,2);
		System.out.println(gammareading*epsilonreading);
	}
	private static void day3b() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day3a.txt"));
		ArrayList<String> origreadings = new ArrayList<>();
		while (filein.hasNextLine()) {
			origreadings.add(filein.nextLine());
		}
		ArrayList<String> readings = (ArrayList<String>)origreadings.clone();
		int p=0;
		while(readings.size()>1) {
			int zeros = 0;
			int ones = 0;
			for (String reading : readings) {
				if (reading.charAt(p)=='0')
					zeros++;
				else
					ones++;
			}
			if (zeros>ones) {
				readings = filterReadings(readings, p, '0');
			} else {
				readings = filterReadings(readings, p, '1');
			}
			p++;
		}
		int oxy = Integer.parseInt(readings.get(0),2);
		readings = (ArrayList<String>)origreadings.clone();
		p=0;
		while(readings.size()>1) {
			int zeros = 0;
			int ones = 0;
			for (String reading : readings) {
				if (reading.charAt(p)=='0')
					zeros++;
				else
					ones++;
			}
			if (zeros>ones) {
				readings = filterReadings(readings, p, '1');
			} else {
				readings = filterReadings(readings, p, '0');
			}
			p++;
		}
		int co2 = Integer.parseInt(readings.get(0),2);
		System.out.println(oxy*co2);
	}
	// KEEP readings which have character "filter" at position "p"
	private static ArrayList<String> filterReadings(ArrayList<String> readings, int p, char filter) {
		ArrayList<String> filtered = new ArrayList<>();
		for (String reading : readings) {
			if (reading.charAt(p)==filter)
				filtered.add(reading);
		}
		return filtered;
	}
	
	private static void day2a() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day2a.txt"));
		int horizontal = 0;
		int depth = 0;
		while (filein.hasNextLine()) {
			String parts[] = filein.nextLine().split(" ");
			switch (parts[0]) {
			case "forward": 
				horizontal += Integer.parseInt(parts[1]);
				break;
			case "down": 
				depth += Integer.parseInt(parts[1]);
				break;
			case "up": 
				depth -= Integer.parseInt(parts[1]);
				break;
			}
		}
		System.out.println(horizontal*depth);
	}
	private static void day2b() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day2a.txt"));
		int horizontal = 0;
		int aim = 0;
		int depth = 0;
		while (filein.hasNextLine()) {
			String parts[] = filein.nextLine().split(" ");
			switch (parts[0]) {
			case "forward": 
				horizontal += Integer.parseInt(parts[1]);
				depth += aim*Integer.parseInt(parts[1]);
				break;
			case "down": 
				aim += Integer.parseInt(parts[1]);
				break;
			case "up": 
				aim -= Integer.parseInt(parts[1]);
				break;
			}
		}
		System.out.println(horizontal*depth);
	}
	
	private static void day1a() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day1a.txt"));
		Integer last = null;
		int increasedcnt = 0;
		while (filein.hasNextLine()) {
			int current = Integer.parseInt(filein.nextLine());
			if (last!=null && current>last) {
				increasedcnt++;
			}
//			System.out.print(last + " " + current);
			last = current;
			
		}
		filein.close();
		System.out.println(increasedcnt);		
	}
	private static void day1b(int windowsize) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day1a.txt"));
		ArrayList<Integer> nums = new ArrayList<>();
		while (filein.hasNextLine()) {
			nums.add(Integer.parseInt(filein.nextLine()));
		}
		filein.close();
		Integer lastsum = null;
		int increasedcnt = 0;
		for (int i=2; i<nums.size(); i++) {
			int currentsum = nums.get(i-2) + nums.get(i-1) + nums.get(i);
			if (lastsum != null && currentsum>lastsum) {
				increasedcnt++;
			}
			lastsum = currentsum;
		}
		System.out.println(increasedcnt);		
	}
	
	
}
