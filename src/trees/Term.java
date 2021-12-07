package trees;

public class Term implements Comparable<Term> {

	protected String term;
	protected String definition;
	public Term(String term, String definition) {
		this.term = term;
		this.definition = definition;
	}
	
	@Override
	public int compareTo(Term otherterm) {
		return (term.compareTo(otherterm.term));
	}
	
	@Override
	public String toString() {
		return term;
	}
	
}
