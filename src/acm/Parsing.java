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
		
	private static void serializationDemo() throws Exception {
		String records[][] = new String[2][];
		records[0] = new String[] { "Brian Toone", "Grad", "Hoover, Alabama" };
		records[1] = new String[] { "JaKia Hood", "Junior", "Fairfield, Alabama" };
		FileOutputStream fout = new FileOutputStream("records.bin");
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(records);
		out.close();
		
		// Two hours later ...
		FileInputStream fin = new FileInputStream("records.bin");
		ObjectInputStream in = new ObjectInputStream(fin);
		
		String records2[][] = (String[][]) in.readObject();
		System.out.println(records[1][2]);
		out.close();
		
		
		
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
//			System.out+.println(line);
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
