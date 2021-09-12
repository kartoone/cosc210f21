package data;

import java.util.ArrayList;

public class ArraysDemo {
	public static void main(String[] args) {
		System.out.println("arrays demo");
//		int ages[] = new int[50];
//		int agecount = 0;
//		ages[0] = 25;
//		agecount++;
//		ages[1] = 36;
//		agecount++;
//		System.out.println(ages.length); // will print 50
		
		int ages[] = new int[] { 25, 36, 45, 50, 35, 65 };
		System.out.println(ages.length);	// display the size of our array (note that since our array is in fact fully populated, this is the correct number of items we have put into the array)
		System.out.println(ages[0]); // display a single age

		System.out.println("Display all the ages on separate lines");
		for (int age : ages) {
			System.out.println(age);
		}
		// python equivalent to the loop above:
		// for age in ages:
		//    print(age)
		
		// calculate the average of all the ages
		System.out.println("Calculate and display the average age.");
		int total = 0;
		for (int i = 0; i < ages.length; i++) {
			System.out.println(ages[i]);
			total = total + ages[i];
		}
		// python equivalent to the loop above:
		// for i,age in ages.enumerate():
		//    print(i)
		//    print(age)
		System.out.println("average age: " + ((double)total/ages.length));
		
		ArrayList<Integer> ageList = new ArrayList<>();
		ageList.add(25);
		ageList.add(36);
		ageList.add(45);
		ageList.add(50);
		ageList.add(35);
		ageList.add(65);	
		
		// the rest of our program below demonstrates the methods we can use to manipulate our ArrayList
		
		// replace the "25" that is currently in position 0 with a new value 26
		ageList.set(0, 26); 
		ages[0] = 26; // this is native java array equivalent to "set"
		
		// access a specific position within the list
		System.out.println("Display the item at the first position in both our ArrayList and in our native array");
		System.out.println(ageList.get(0)); 
		System.out.println(ages[0]); // this is native java array equivalent to "get"
		
		// insert the value "38" at position 2 in the ArrayList
		ageList.add(2, 38); // note that there is no native java array equivalent, you simply cannot do this without using a helper class
		
		// remove the first item in the ArrayList
		ageList.remove(0);  // note that there is no native java array equivalent, you simply cannot do this without using a helper class
				
		System.out.println("Print all the ages from our ArrayList manually on separate lines after we have inserted a 38 at position 2 and removed the first element (which bumps 38 up to position 1)");
		for (Integer age : ageList) {
			System.out.println(age);
		}
		System.out.println("Print all the ages in our native java array");
		for (int age: ages) {
			System.out.println(age);
		}
		
		System.out.println("Print all the ages in our ArrayList without using a for loop");
		System.out.println(ageList); // note that there is no native java array equivalent, you MUST use a for loop or a help class (java.util.Arrays) if you want a one-liner that will display the entire contents of the array
	}

}
