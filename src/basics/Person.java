package basics;

public abstract class Person {	
	protected String name;	
	public Person(String name) {
		this.name = name;
	}	
	protected abstract String getClassification();	
	public void displayNametag() {
	     System.out.println("Hi my name is " + name + ". I am a " + getClassification() + ".");	     
	}	
	public static void main(String args[]) {
		Person persons[] = new Person[] {
				new Faculty("Brian Toone"),
				new Staff("John Smith"),
				new Student("Jane Doe"),
				new Student("Carlton Lassiter")
		};
		for (Person p: persons) {
			p.displayNametag();
		}		
	}
	
}

class Student extends Person {
	public Student(String name) {
		super(name);
	}
	@Override
	protected String getClassification() {
		return "student";
	}	
}

class Faculty extends Person {
	public Faculty(String name) {
		super(name);
	}
	@Override
	protected String getClassification() {
		return "faculty member";
	}	
}

class Staff extends Person {
	public Staff(String name) {
		super(name);
	}
	@Override
	protected String getClassification() {
		return "staff associate";
	}	
}
