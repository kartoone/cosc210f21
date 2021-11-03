package data;

/**
 * Interface for a standard queue that implements FIFO access to its data.
 * 
 * @author brtoone
 *
 * @param <T>
 */

public interface Queue<T> {
	
	/**
	 * Add data to the REAR of the queue.
	 * 
	 * @param data
	 */
	public void enqueue(T data);
	
	/**
	 * Remove and return the item at the FRONT of the queue
	 * @return the item at the front
	 * @throws EmptyQueueException 
	 */
	public T dequeue() throws EmptyQueueException;

	/**
	 * Returns the item at the FRONT of the queue without removing it.
	 * @return the item at the front
	 * @throws EmptyQueueException 
	 */
	public T front() throws EmptyQueueException;
	
	/**
	 * 
	 * @return the number of items in the queue
	 */
	public int size();
	
	/**
	 * 
	 * @return true if the queue is empty
	 */
	public boolean isEmpty();
	
}
