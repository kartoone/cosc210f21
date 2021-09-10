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
		System.out.println(ages.length);
		System.out.println(ages[0]);
		
		int total = 0;
		for (int i = 0; i < ages.length; i++) {
			System.out.println(ages[i]);
			total = total + ages[i];
		}
		System.out.println("average age: " + ((double)total/ages.length));
		// python equivalent to the loop above:
		// for i,age in ages.enumerate():
		//	  print(i)
		//    print(age)
	
		// python equivalent to the loop below:
		// for age in ages:
		//    print(age)
		for (int age : ages) {
			System.out.println(age);
		}
		
		ArrayList<Integer> ageList = new ArrayList<>();
		ageList.add(25);
		ageList.add(36);
		ageList.add(45);
		ageList.add(50);
		ageList.add(35);
		ageList.add(65);		
		ageList.set(0, 26); // replace the "25" that is currently in position 0 with a new value 26
		ages[0] = 26;		// this is equivalent to the previous line when using a native java array
		System.out.println(ageList.get(0));
		System.out.println(ages[0]); // this is equivalent to the previous line when using a native java array
		ageList.add(2, 38); // insert the value "38" at position 2
		ageList.remove(0);  // remove the first item in the ArrayList
		
		// print all the ages
		for (Integer age : ageList) {
			System.out.println(age);
		}
	}

}
