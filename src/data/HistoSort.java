package data;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Histo - short for histogram 
public class HistoSort {

	public static ArrayList<Integer> readFile(String filename) throws FileNotFoundException {
		// Create the Scanner for reading the file
		Scanner filein = new Scanner(new File(filename));
		
		// Create our ArrayList of Integers that we will be returning
		ArrayList<Integer> list = new ArrayList<>();
		
		// note the while loop here ... remember that scanners almost always use while loops
		while (filein.hasNextLine()) {
			int digit = filein.nextInt();
			list.add(digit);
		}
		
		filein.close();
		return list;
	}
	
	public static int[] convertList(ArrayList<Integer> list) {
		int arr[] = new int[list.size()];
		for (int i=0; i<list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	// remember, all java programs start with a main() method inside a class
	// also, the FileNotFoundException must either be handled or thrown, it's less code if we just "throw" it, but it makes our program less robust because it doesn't handle the error where the specified file is not found
	public static void main(String[] args) throws FileNotFoundException {
		// create a PrintWriter we can use to output our results to a CSV file using println() methods!
		PrintWriter fileout = new PrintWriter(new File("results.csv"));
		
		// create a native java array named counts[] because we know exactly how many elements we need and what to initialize them to
		int counts[] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

		// we DON'T know how many numbers are going to be in the file, so it makes sense to use
		// an ArrayList instead of a native java array. The ArrayList named allTheDigits will grow
		// dynamically as we add items to it ... note that we have moved the code for reading
		// all the integers in the file to a separate method above called "readFile"		
		
		ArrayList<Integer> digits = readFile("testsort.txt");    // not very efficient, but least amount of coding is just to read the file twice
		ArrayList<Integer> digits2 = readFile("testsort.txt");
		
		// Count the digits for our histogram
		for (Integer digit : digits) {
			counts[digit] = counts[digit] + 1;
		}

		// user friendly output AND output for our CSV
		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " occurred " + counts[i] + " times.");
			fileout.println(i + "," + counts[i]);
		}
			
		// let's create a native array that is the exact same size as our ArrayList so we can run our timing comparisons of native array for ArrayList
		int digitsArray[] = convertList(digits);
		
		// Note the dual output below ... the first println displays in the console ... the second println writes to the file in a comma separated format for easy graphing in Excel
		// TIMING MEASURMENT #1: the built-in ArrayList sort() method
		long start = System.currentTimeMillis();		
		digits.sort(null); // sort the list using the built-in sort method provided by the ArrayList class... null means use "natural" ordering for the elements in the list
		long finish = System.currentTimeMillis();
		long elapsed = finish - start;
		System.out.println("built-in ArrayList sort()," + elapsed);
		fileout.println("built-in ArrayList sort()," + elapsed);		
		
		// TIMING MEASUREMENT #2: our insertionSort(ArrayList) method
		start = System.currentTimeMillis();
		insertionSort(digits2);
		finish = System.currentTimeMillis();
		elapsed = finish - start;
		System.out.println("our ArrayList insertionSort()," + elapsed);		
		fileout.println("our ArrayList insertionSort()," + elapsed);		
		
		// TIMING MEASUREMENT #3: our insertionSort(int[]) method
		start = System.currentTimeMillis();
		insertionSort(digitsArray);
		finish = System.currentTimeMillis();
		elapsed = finish - start;
		System.out.println("our native array insertionSort()," + elapsed);		
		fileout.println("our native array insertionSort()," + elapsed);		
        
		// read in our much bigger "testsearch.txt" file
		ArrayList<Integer> digits3 = readFile("testsearch.txt");		
		int digitsArray2[] = convertList(digits3);  // we need this for our native array search methods
		
		// TO DO!!!! This is the section where you need to start writing code. Add the appropriate code after each of my numbered comments.		
		// 1. Time the searchFirst methods (this will be a lot of code ... roughly the same amount of code as Timing Measurements 1, 2, and 3)
		// searchFirstBuiltin(digits3, 768);
		// searchFirst(digits3, 768);
		// searchFirst(digitsArray2, 768);

				
		// 2. Time the searchAll methods (but NOT searchSorted)
		// searchAll(digits3, 855);
		// searchAll(digitsArray2, 855);
		
		// 3. Sort the list so you can time the optimized searchSorted method
		// Already done for you on the next line:
		digits3.sort(null);
				
		// 4. Time the searchSorted method:
		// searchSorted(digits3, 855);			
		
		// close out the CSV file so it gets saved to disk
		fileout.close();
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
	
	// these searchFirst methods return the index of the FIRST "hit"
	//   -1 if not found
	//   nothing to do here, already implemented for you
	static int searchFirstBuiltin(ArrayList<Integer> list, int needle) {
		return list.indexOf(needle);
	}	

	// Make the method work the same as the previous method, but instead of using indexOf
	// you should loop through all the elements in the list until you get a hit ... and return the index of that position
	static int searchFirst(ArrayList<Integer> list, int needle) {
		// 5. TO DO: finish this method!
		
		return -1;  // return -1 if not found
	}
	
	// same as previous method, but on a native array instead
	static int searchFirst(int arr[], int needle) {
		// 6. TO DO: finish this method!
		
		return -1;  // return -1 if not found
	}

	// Create an empty ArrayList<Integer> called "hits" and add each "hit" to hits
	static ArrayList<Integer> searchAll(ArrayList<Integer> list, int needle) {
		ArrayList<Integer> hits = new ArrayList<>();		
		return hits;
	}
	
	// Create an empty ArrayList<Integer> called "hits" and add each "hit" to hits
	static int[] searchAll(int arr[], int needle) {
	// Then at the very end of your method, call convertList to convert your "hits" list to a native java array which you can then return
		ArrayList<Integer> hits = new ArrayList<>();		
		return convertList(hits);
	}
	
	// the list MUST BE SORTED ALREADY for this to work
	static ArrayList<Integer> searchSorted(ArrayList<Integer> sortedlist, int needle) {
		ArrayList<Integer> hits = new ArrayList<>();
		for (int i=hits.indexOf(needle); i!=-1 && sortedlist.get(i)==needle; i++) {
			hits.add(i);
		}		
		return hits;
	}	
	
}
