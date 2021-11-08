package data;

public class ArrayQueue<T> implements Queue<T> {

	public static final int DEFAULT_MAXSIZE = 5; // pretty small max, must be a memory constrained system	
	protected int size;
	protected T[] array;   // the storage place for all our data
	protected int front;   // keep track of the index that represents the front of our queue
	protected int rear;    // keep track of the index that represents the rear of our queue
	
	public ArrayQueue() {
		this(DEFAULT_MAXSIZE);
	}
	
	public ArrayQueue(int maxsize) {
		this.size = 0;
		this.array = (T[])new Object[maxsize];
		this.front = 0;
		this.rear = 0;
	}
	
	
	@Override
	public void enqueue(T data) {
		if (size==array.length) {
			throw new Error("The queue is full!");
		}
		array[rear] = data;
		rear = (rear + 1) % array.length;
		size++;
	}

	@Override
	public T dequeue() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		size--;
		T dataitem = array[front];
		front = (front+1) % array.length;
		return dataitem;
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
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

}
