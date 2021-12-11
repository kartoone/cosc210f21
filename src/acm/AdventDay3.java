package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay3 {

	public static void main(String[] args) throws FileNotFoundException {
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
	
	
}
