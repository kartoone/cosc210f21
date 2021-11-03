package acm;

import java.util.ArrayList;
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
		// Create the data we want to store
		String records[][] = new String[2][];
		records[0] = new String[] { "Brian Toone", "Grad", "Hoover, Alabama" };
		records[1] = new String[] { "JaKia Hood", "Junior", "Fairfield, Alabama" };

		// Setup the object output stream for serialization
		FileOutputStream fout = new FileOutputStream("records.bin");
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(records);
		out.close();

		// At some point later, you can then read the data back directly into Java objects
		FileInputStream fin = new FileInputStream("records.bin");
		ObjectInputStream in = new ObjectInputStream(fin);
		
		// You've got to know exactly what type of object (and in what order) you stored the data into the file
		String records2[][] = (String[][]) in.readObject();
		System.out.println(records[1][2]);
		in.close();
		
		// Another example with different types of data
		String name = "Brian Toone";
		int age = 45;
		double height = 5.75; // 5 foot, 9 inches
		boolean isFaculty = true;
		
		// write to a file
		FileOutputStream fout2 = new FileOutputStream("objects.ser");
		ObjectOutputStream out2 = new ObjectOutputStream(fout2);
		out2.writeObject(name);
		out2.writeObject(age);
		out2.writeObject(height);
		out2.writeObject(isFaculty);
		out2.close();

		// read back from the same file
		FileInputStream fin2 = new FileInputStream("objects.ser");
		ObjectInputStream in2 = new ObjectInputStream(fin2);
		System.out.println(in2.readObject());
		System.out.println(in2.readObject());
		System.out.println(in2.readObject());
		System.out.println(in2.readObject());
		in2.close();
	}
	
	// parsing a text file
	//	- Example: https://api.weather.gov/gridpoints/TOP/31,80/forecast
	//  - parsing a JSON file	
	private static void parseTextDemo() throws Exception {
		// TEXT data
		Scanner in = new Scanner(new File("nws.txt"));
		String jsonData = "";
		while (in.hasNextLine()) {
			String line = in.nextLine();
//			System.out.println(line);
			jsonData += line;
		}
		// PARSE the JSON string into a JSON object
		JSONObject json = new JSONObject(jsonData);
		int afternoonTemp = json.getJSONObject("properties").getJSONArray("periods").getJSONObject(0).getInt("temperature");
		System.out.println(afternoonTemp);
	}

	// parsing a binary file
	// 		- Example: 2021-10-29-07-13-16.fit
	//		- https://developer.garmin.com/fit/protocol/
	private static void parseBinaryDemo() throws Exception {
		byte[] bytes = Files.readAllBytes(Paths.get("2021-10-29-07-13-16.fit"));
		System.out.println(bytes.length);
		System.out.println(bytes[0]);
		System.out.println(bytes[1]);
		System.out.println(bytes[9]);
	}

}
