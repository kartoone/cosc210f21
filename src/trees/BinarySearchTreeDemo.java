package trees;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class BinarySearchTreeDemo {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner filein = new Scanner(new File("FOLDOC.txt"));
		ArrayList<Term> terms = new ArrayList<>();
		
		String currentTerm = "";
		String currentDefinition = "";
		while (filein.hasNextLine()) {
			String line = filein.nextLine();
			if (line.length()==0 || line.charAt(0)=='\t') {
				currentDefinition += line + "\n";
			} else {
				if (currentTerm.length()>0) {
					terms.add(new Term(currentTerm, currentDefinition));
				}
				currentTerm = line;
				currentDefinition = "";
			}
		}
		
		// Display a random term!
		System.out.println(terms.get((int)Math.floor(Math.random()*terms.size())));
		
		// Search for a term
		System.out.print("Enter a term: ");
		Scanner in = new Scanner(System.in);
		String search = in.nextLine();
		String hit = "";
		long start = System.nanoTime();
		for (Term term : terms) {
			if (term.equals(search)) {
				hit = term.definition;
				break;
			}
		}
		long end = System.nanoTime();
		if (hit.length()==0) {
			System.out.println("That term does not exist in the dictionary.");
		} else {
			System.out.println(hit);
		}
		System.out.println(end-start + " ns");
	}

}
