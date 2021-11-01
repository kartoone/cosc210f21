package data;

import java.util.Scanner;

public class WebBrowserStack {

	public static void main(String[] args) {
		Stack<String> backButtonStack = new ArrayStack<>();
		Scanner in = new Scanner(System.in);
		int choice;
		do {
			System.out.println("MENU");
			System.out.println(" 1. Goto URL");
			System.out.println(" 2. Press Back Button");
			System.out.println(" 3. Exit");
			System.out.print("Choice? ");
			choice = Integer.parseInt(in.nextLine());
			switch (choice) {
			case 1:
				System.out.print("URL? ");
				backButtonStack.push(in.nextLine());
				break;
			case 2:
				if (!backButtonStack.isEmpty()) {
					System.out.println("Clicked back");
					backButtonStack.pop();
				}
				break;
			}
			if (!backButtonStack.isEmpty()) {
				System.out.println("Your current url: " + backButtonStack.top());
			} else {
				System.out.println("You are currently on the blank start page. You cannot go back any farther");
			}
		} while (choice != 3);
		System.out.println("Good-bye");
	}

}
