package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class contains all of the data structures we have learned this semester.
 * @author brtoone
 * 
 */
public class DataStructureSurvey {

	public static void main(String[] args) {
		// simple primitive variable - only one value at a time
		int stockprice = 2717;
		System.out.println("stockprice: " + stockprice);
		
		// arrays and ArrayLists - Java's solution to allow us to store multiple values in just one variable
		// native java array - must know the size ahead of time and very difficult to resize
		// ArrayList - size not as important b/c the ArrayList can resize itself easily, BUT you can't use the square bracket notation
		int stockprices[] = { 2800, 2750, 2650, 2700, 2717 };
		int stockprices2[] = new int[5];
		stockprices2[0] = 2800; // imagine this 2800 coming from a web service
		stockprices2[1] = 2750;
		stockprices2[2] = 2650;
		stockprices2[3] = 2700;
		stockprices2[4] = 2717;
		System.out.println(stockprices2[0]);
		// storing the same data using an arraylist 
		ArrayList<Integer> stockprices3 = new ArrayList<>();
		stockprices3.add(2800);
		stockprices3.add(2750);
		stockprices3.add(2650);
		stockprices3.add(2700);
		stockprices3.add(2717);
		System.out.println(stockprices3.get(0));

		System.out.println(stockprices);  // will print out default string representation, which is memory related
		System.out.println(stockprices2);
		// "old-school" for loop that works with an index variable
		System.out.println("stockprices using 'old-school' for loop");
		for (int i = 0; i < stockprices.length; i++) {
			System.out.println("stockprices[" + i + "] = " + stockprices[i]);
		}
		// modern "enhanced" for loop that works without an index variable
		System.out.println("stockprices using 'enhanced' for loop");
		for (int price : stockprices) {
			System.out.println("stockprices[?] = " + price);
		}
		System.out.println("stockprices3 using 'old-school' for loop");
		for (int i = 0; i < stockprices3.size(); i++) {
			System.out.println("stockprices3 @ position " + i + " = " + stockprices3.get(i));
		}
		System.out.println("stockprices3 using 'enhanced' for loop");
		for (Integer price : stockprices3) {
			System.out.println("stockprices3 @ position ??? = " + price);
		}
		
		// closest (tie) thing to a dictionary in Java
		Map<String, Integer> weekdayprices = new TreeMap<>();
		weekdayprices.put("Monday", 2800); // equivalent to weekdayprices["Monday"] = 2800 in Python
		weekdayprices.put("Tuesday", 2750); 
		weekdayprices.put("Wednesday", 2650); 
		weekdayprices.put("Thursday", 2700); 
		weekdayprices.put("Friday", 2717); 

		System.out.println(weekdayprices.get("Monday"));
		System.out.println("weekdayprices");		
		// print out everything
		for (Map.Entry<String, Integer> entry: weekdayprices.entrySet()) {
			System.out.println("weekdayprices @ position " + entry.getKey() + " = " + entry.getValue());
		}
		// print out the keys
		for (String key: weekdayprices.keySet()) {
			System.out.println(key);
		}
		// print out the values
		for (Integer value: weekdayprices.values()) {
			System.out.println(value);
		}
		// print out both
		for (String key: weekdayprices.keySet()) {
			System.out.println(key + ": " + weekdayprices.get(key));
		}
		Map<String, Integer> weekdayprices2 = new HashMap<>();
		weekdayprices2.put("Monday", 2800); // equivalent to weekdayprices["Monday"] = 2800 in Python
		weekdayprices2.put("Tuesday", 2750); 
		weekdayprices2.put("Wednesday", 2650); 
		weekdayprices2.put("Thursday", 2700); 
		weekdayprices2.put("Friday", 2717); 
		for (Map.Entry<String, Integer> entry: weekdayprices2.entrySet()) {
			System.out.println("weekdayprices2 @ position " + entry.getKey() + " = " + entry.getValue());
		}
		
		// Stack example
		Stack<String> airportStack = new ListStack<>();
		airportStack.push("BHM");
		System.out.println("size after adding BHM: " + airportStack.size());
		airportStack.push("ATL");
		System.out.println("size after adding ATL: " + airportStack.size());
		airportStack.push("MSP");
		System.out.println("size after adding MSP: " + airportStack.size());
		String airport = airportStack.pop();
		System.out.println("first item popped: " + airport);
		airport = airportStack.pop();
		System.out.println("next item popped: " + airport);
		airport = airportStack.pop();
		System.out.println("next item popped: " + airport);
		System.out.println("is the stack empty? " + airportStack.isEmpty());
		if (!airportStack.isEmpty()) {
			// would crash the program b/c the stack should now be empty
			airportStack.pop();
		}
			
	}

}
