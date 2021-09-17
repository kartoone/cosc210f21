package data;

import java.io.*;
import java.util.Random;

public class HistoGen {

	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("test.txt"));
		Random r = new Random();
		for (int i=0; i<1000000; i++) {
			if (i>0) {
				out.println();
			}
			out.print(r.nextInt(10));
		}
		out.close();
	}

}
