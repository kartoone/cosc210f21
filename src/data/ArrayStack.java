package data;

import java.util.EmptyStackException;

public class ArrayStack<T> implements Stack<T> {

	public static final int DEFAULT_MAXSIZE = 1000; // pretty small max, must be a memory constrained system	
	protected int maxsize; // the actual max size
	protected T[] array;   // the storage place for all our data
	protected int top;     // keep track of the index that represents the top of our stack
	
	public ArrayStack() {
		this(DEFAULT_MAXSIZE);
	}
	
	public ArrayStack(int maxsize) {
		this.maxsize = maxsize;
		this.array = (T[])new Object[maxsize];
		this.top = 0;
	}
	
	@Override
	public void push(T data) {
		if (top == maxsize) {
			throw new Error("The stack is FULL!");
		}
		array[top++] = data;
	}

	@Override
	public T pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return array[--top];
	}

	@Override
	public T top() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return array[top-1];
	}

	@Override
	public int size() {
		return top;
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

}
