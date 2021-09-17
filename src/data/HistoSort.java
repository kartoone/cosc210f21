package data;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

// Histo - short for histogram 
public class HistoSort {

	// remember, all java programs start with a main() method inside a class
	// also, the FileNotFoundException must either be handled or thrown, it's less code if we just "throw" it, but it makes our program less robust because it doesn't handle the error where the specified file is not found
	public static void main(String[] args) throws FileNotFoundException {

		// create a Scanner object named filein that we can use to read through a file
		Scanner filein = new Scanner(new File("test.txt"));
		
		// create a native java array named counts[] because we know exactly how many elements we need and what to initialize them to
		int counts[] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

		// we DON'T know how many numbers are going to be in the file, so it makes sense to use
		// an ArrayList instead of a native java array. The ArrayList named allTheDigits will grow
		// dynamically as we add items to it
		ArrayList<Integer> allTheDigits = new ArrayList<>();
		ArrayList<Integer> allTheDigits2 = new ArrayList<>();
		
		// note the while loop here ... remember that scanners almost always use while loops
		while (filein.hasNextLine()) {
			int digit = filein.nextInt();
			allTheDigits.add(digit);
			allTheDigits2.add(digit);
			counts[digit] = counts[digit] + 1;
		}
		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " occurred " + counts[i] + " times.");
		}
		
		// note that ArrayList has a very handy "toString()" method that gets called automatically
		// by the println method and displays a nicely formatted list of all its data without us
		// having to use a for loop
//		System.out.println(allTheDigits);
		
	
		// let's create a native array that is the exact size so we can run our timing comparisons
		int allTheDigitsArray[] = new int[allTheDigits.size()];
		for (int i=0; i<allTheDigits.size(); i++) {
			allTheDigitsArray[i] = allTheDigits.get(i);
		}		
//		int[] allTheDigitsArray = allTheDigits.stream().mapToInt(i -> i).toArray();
		
		// sort the list ... null means use "natural" ordering for the elements in the list
		long start = System.currentTimeMillis();		
		allTheDigits.sort(null);
		long finish = System.currentTimeMillis();
		long elapsed = finish - start;
        
		start = System.currentTimeMillis();
		insertionSort(allTheDigits2);
		finish = System.currentTimeMillis();
		elapsed = finish - start;
		
		start = System.currentTimeMillis();
        insertionSort(allTheDigitsArray);
        finish = System.currentTimeMillis();
        elapsed = finish - start;
        
		// display our sorted list
		//System.out.println(allTheDigits);				
	}
	
	static void insertionSort(ArrayList<Integer> list) {
		// i: the index of the sorted part of our list
		for (int i=0; i<list.size()-1; i++) {
			int cur = list.remove(i+1); // cur is the current number that we are trying to insert into the right location in the sorted part of our list
			int j = i;
			while(j>=0 && cur<list.get(j)) {
				j--; 
			}			
			list.add(j+1, cur);
		}
	}
	
	static void insertionSort(int arr[]) {
		// i: the index of the sorted part of our list
		for (int i=1; i<arr.length-1; i++) {
			int cur = arr[i];
			int j = i-1;
			while (j>=0 && arr[j] > cur) {
				arr[j+1] = arr[j];
				j--;
			}
			arr[j+1] = cur;
		}
	}
	
	// the list MUST BE SORTED ALREADY for this to work
	// HINT: use the sublist method provided by ArrayList
	static ArrayList<Integer> searchSorted(ArrayList<Integer> sortedlist) {
		return null;
	}
	
	// add search methods here
	static ArrayList<Integer> searchAll(ArrayList<Integer> list) {
		// to do: finish!
		return null;
	}
	
	// add search methods here
	static int[] searchAll(int arr[]) {
		// to do: finish!
		return null;
	}
	
}