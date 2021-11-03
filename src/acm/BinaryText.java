package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class BinaryText {

	public static void main(String[] args) throws Exception {
		byte[] version2bytes = parseBytesText();
	}
	
	private static byte[] convertBytelist(ArrayList<Byte> bytelist) {
		byte[] bytearray = new byte[bytelist.size()]; 
		System.out.println("binary decimal hexadecimal ascii");
		for (int i=0; i<bytelist.size(); i++) {
			// display first 10 bytes in variety of formats	
			if (i<10) {
				Byte b = bytelist.get(i);		
				System.out.printf("%8s",Integer.toBinaryString(b));
				System.out.print(" ");
				System.out.printf("%3d",(int)bytelist.get(i));
				System.out.print(" ");
				System.out.print("0x"+Integer.toHexString(b));
				System.out.print(" ");
				System.out.print((char)(int)bytelist.get(i));
				System.out.println();
			}
			bytearray[i] = bytelist.get(i);
		}
		return bytearray;
	}

	// MUCH, MUCH cleaner way to do it
	private static byte[] parseBytesText() throws Exception {
		Scanner in = new Scanner(new File("ocean.txt"));
		in.useDelimiter("!");
		ArrayList<Byte> bytelist = new ArrayList<>();
		while (in.hasNext()) {
			byte b = Byte.parseByte(in.next(),2);
			bytelist.add(b);
		}
		return convertBytelist(bytelist);
	}
	
}
