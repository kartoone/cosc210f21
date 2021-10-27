package data;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class ListStack<T> implements Stack<T> {

	protected ArrayList<T> list = new ArrayList<>(); 
	
	@Override
	public void push(T data) {
		list.add(data);
	}

	@Override
	public T pop() throws EmptyStackException {		
		if (size()==0) {
			throw new EmptyStackException();
		}
		return list.remove(list.size()-1);
	}

	@Override
	public T top() {
		if (size()==0) {
			throw new EmptyStackException();
		}
		return list.get(list.size()-1);
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
