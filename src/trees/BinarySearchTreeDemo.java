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
		Term randomTerm = terms.get((int)Math.floor(Math.random()*terms.size())); 
		System.out.println(randomTerm.term + "\n" +randomTerm.definition);
		
		// Search for a term
		System.out.print("Enter a term: ");
		Scanner in = new Scanner(System.in);
		String search = in.nextLine();
		Term searchTerm = new Term(search,"");
		String hit = "";
		long start = System.nanoTime();
		for (Term term : terms) {
			if (term.compareTo(searchTerm)==0) {
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
		
		BinarySearchTree<Term> bst = new BinarySearchTree<>();
		for (Term t: terms) {
			bst.addData(t);
		}
		start = System.nanoTime();
		Node<Term> hitTerm = bst.searchTree(searchTerm);
		boolean hitflag = true;
		if(hitTerm.getElement()==null) {
			hitflag = false;
		}
		end = System.nanoTime();
		if (!hitflag) {
			System.out.println("That term is not in FOLDOC");
		} else {
			System.out.println(hitTerm.getElement().term + "\n" + hitTerm.getElement().definition);
		}
		System.out.println(end-start + " ns");
		System.out.println(bst);
	}

}
