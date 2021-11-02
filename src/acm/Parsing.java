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
		
	// reading/writing Java objects directly from a file without having to worry about parsing
	// 		- https://www.tutorialspoint.com/java/java_serialization.htm
	//		- writeObject() and readObject()
	//		- the object you are trying to save MUST implement the Serializable interface.
	private static void serializationDemo() throws Exception {	
		// prep some data
		String name = "Brian Toone";
		int age = 45;
		double height = 5.75; // 5 foot, 9 inches
		boolean isFaculty = true;
		
		// write to a file
		FileOutputStream fout = new FileOutputStream("objects.ser");
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(name);
		out.writeObject(age);
		out.writeObject(height);
		out.writeObject(isFaculty);
		out.close();
		
		// read back from the same file
		FileInputStream fin = new FileInputStream("objects.ser");
		ObjectInputStream in = new ObjectInputStream(fin);
		System.out.println(in.readObject());
		System.out.println(in.readObject());
		System.out.println(in.readObject());
		System.out.println(in.readObject());
		in.close();
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
