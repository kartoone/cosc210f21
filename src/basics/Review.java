package basics;

import java.util.ArrayList;

public class Review {
	
	public static void main(String args[]) {
		// examples of primitive variables
		int num = 3;
		boolean b = false;
		char c = 'a';
		double d = 3.14159;
		
		// examples of objects
		Newsfeed conservative = new Newsfeed(null, null, null);
		Newsfeed liberal = new Newsfeed(null, null, null);
		conservative.viewArticle(3);
		liberal.viewArticle(3);
		String name = "Brian Toone";
		System.out.println(name.length());
		
		// examples of arrays
		Newsfeed feeds[] = { 
				new Newsfeed(null, null, null), 
				new Newsfeed(null, null, null),
				new Newsfeed(null, null, null)
		};
		feeds[0].viewArticle(3);
		feeds[1].viewArticle(3);
		int numbersArray[] = new int[] {3, 2, 1};
		for (int i=0; i<numbersArray.length; i++) {
			System.out.println(i + ": " + numbersArray[i]);
		}
		for (int v : numbersArray) {
			System.out.println(v);
		}
		
		// examples of ArrayLists
		ArrayList<Newsfeed> feedsList = new ArrayList<>();
		feedsList.add(new Newsfeed(null,null,null));
		feedsList.add(new Newsfeed(null,null,null));
		feedsList.add(new Newsfeed(null,null,null));
		feedsList.remove(1);
		feedsList.get(0).viewArticle(3);
		
		ArrayList<Integer> numbers = new ArrayList<>();
		numbers.add(3);
		numbers.add(2);
		numbers.add(1);
		System.out.println(numbers.get(0));
		for (int i = 0; i < numbers.size(); i++) {
			System.out.println(numbers.get(i));
		}
		for (Integer value : numbers) {
			System.out.println(value);
		}
	}

}
