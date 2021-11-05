package data;

public class ArrayQueueUnoptimized<T> implements Queue<T> {

	public static final int DEFAULT_MAXSIZE = 1000; // pretty small max, must be a memory constrained system	
	protected int maxsize; // the actual max size
	protected T[] array;   // the storage place for all our data
	protected int front;   // keep track of the index that represents the front of our queue
	protected int rear;    // keep track of the index that represents the rear of our queue
	
	public ArrayQueueUnoptimized() {
		this(DEFAULT_MAXSIZE);
	}
	
	public ArrayQueueUnoptimized(int maxsize) {
		this.maxsize = maxsize;
		this.array = (T[])new Object[maxsize];
		this.front = 0;
		this.rear = 0;
	}
	
	
	@Override
	public void enqueue(T data) {
		if (rear == array.length) {
			throw new Error("The queue is full!");
		}
		array[rear++] = data;		
	}

	@Override
	public T dequeue() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		T frontitem = array[front];
		// unoptimized version: shift all the other items over one spot to the left
		for (int i = 0; i < array.length-1; i++) {
			array[i] = array[i+1];
		}
		rear--;
		return frontitem;
	}

	@Override
	public T front() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		return array[front];
	}

	@Override
	public int size() {
		return rear;
	}

	@Override
	public boolean isEmpty() {
		return front==rear;
	}

}
