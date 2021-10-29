package data;

import java.util.EmptyStackException;

// Interfaces define the methods a class MUST implement

// Stack restricts access to data in LIFO order
// LIFO - last in / first out
public interface Stack<T> {

	/**
	 * Adds data to the top of the stack.
	 * @param data
	 */
	public void push(T data);
	
	/**
	 * Returns and removes the top data item (the last item that was added). Cannot be called on empty stack.
	 * NEVER CALL POP ON AN EMPTY STACK
	 * @return the data that is on top of the stack.
	 * @throws EmptyStackException if you pop an empty stack
	 */
	public T pop() throws EmptyStackException;
	
	/**
	 * Returns but doesn't remove the top data item (the last item that was added). Cannot be called on empty stack.
	 * NEVER CALL TOP ON AN EMPTY STACK
	 * @return the data that is on top of the stack.
	 * @throws EmptyStackException if you pop an empty stack
	 */
	public T top();

	/**
	 * Returns the number of items in the stack.
	 * @return the number of items in the stack.
	 */
	public int size();
	
	/**
	 * Returns true if the stack is empty.
	 * @return true if the stack is empty.
	 */
	public boolean isEmpty();
	
}
