package trees;

/**
 * Represents a node in a tree...
 * 
 * @author brtoone
 *
 * @param <T> - the type of data this node is storing
 */
public interface Node<T> {
	
	/**
	 * return the current data stored by this node
	 * 
	 * @return
	 */
	public T getElement();
	
	/**
	 * replace the data stored by this node
	 * @param newdata
	 */
	public void setElement(T newdata);
	
}
