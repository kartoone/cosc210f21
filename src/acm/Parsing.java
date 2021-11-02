package acm;

// serialization vs parsing
//   serialization - Java objects encoded as binary files when serialized and then automatically parsed when "de"-serialized
//   parsing - importing text or binary data manually into the target programming language
// 
import java.util.Scanner; 		// Scanner: low-level text parsing
import java.io.*; 				// LOTS of utility classes for reading/writing files
import org.json.*; 				// one of a HUGE long list of libraries for parsing JSON 
import java.nio.file.*;			// convenience methods since JDK7.0 that make it easier to work with binary files

/**
 * Demonstrates basic parsing strategies plus reading/writing from text files
 * and binary files
 * 
 * @author brtoone
 *
 */
public class Parsing {

	public static void main(String[] args) throws Exception {
		parseTextDemo();
		parseBinaryDemo();
		serializationDemo();
	}
		
	private static void serializationDemo() {

		
	}
	
	// parsing a text file
	//	- Example: https://api.weather.gov/gridpoints/TOP/31,80/forecast
	//  - parsing a JSON file	
	private static void parseTextDemo() throws Exception {
		// TEXT data
		Scanner in = new Scanner(new File("nws.txt"));
		while (in.hasNextLine()) {
			System.out.println(in.nextLine());
		}		
	}

	// parsing a binary file
	// 		- Example: 2021-10-29-07-13-16.fit
	//		- https://developer.garmin.com/fit/protocol/
	private static void parseBinaryDemo() throws Exception {
		byte[] bytes = Files.readAllBytes(Paths.get("2021-10-29-07-13-16.fit"));
		System.out.println(bytes.length);
		System.out.println(bytes[0]);
	}

}
