package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdventDay2 {

	public static void main(String[] args) throws FileNotFoundException {
		day2a();
		day2b();
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
	
}
