package data;

import java.util.ArrayList;

public class ListQueue<T> implements Queue<T> {

	protected ArrayList<T> list = new ArrayList<>();
	
	@Override
	public void enqueue(T data) {
		// add at the "rear" of the queue
		// we are designating the end of the list as the "rear"
		// the ArrayList add() method adds at the end by default
		list.add(data);
	}

	@Override
	public T dequeue() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		// remove from the "front" of the queue which is always the item at position 0
		return list.remove(0);
	}

	@Override
	public T front() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		// return item at the "front" of the queue w/o removing it
		return list.get(0);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

}
