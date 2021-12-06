package trees;

public class Term {

	protected String term;
	protected String definition;
	public Term(String term, String definition) {
		this.term = term;
		this.definition = definition;
	}
	public boolean equals(String searchphrase) {
		return term.equalsIgnoreCase(searchphrase);
	}
	public int compare(String otherterm) {
		return term.compareTo(otherterm);
	}
	@Override
	public String toString() {
		return term + definition;
	}
	
}
