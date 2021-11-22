package trees;

import java.util.ArrayList;

public interface Tree<T> {

	/**
	 * Returns a reference to the root node
	 * @return the root node
	 */
	public Node<T> root();
	
	/**
	 * Returns a list of the leaf nodes
	 * @return the list of leaf nodes
	 */
	public ArrayList<Node<T>> leaves();
	
	/**
	 * Returns the parent of the given node
	 * @param n - the node whose parent want to find
	 * @return - the parent of Node n
	 */
	public Node<T> parent(Node<T> n);
	
	/**
	 * Returns a list of the children of the given node
	 * @param n - the node whose children we want to find
	 * @return - the list of children of Node n
	 */
	public ArrayList<Node<T>> children(Node<T> n);
	
	/**
	 * Returns a list of the siblings of the given node
	 * @param n - the node whose siblings we want to find
	 * @return - the list of the siblings of Node n ... excluding n
	 */
	public ArrayList<Node<T>> siblings(Node<T> n);

	/**
	 * Returns the depth of node n (counting from 0)
	 * @param n - the node whose depth we want
	 * @return the depth of that node
	 */
	public int depth(Node<T> n);
	
	/**
	 * Returns the height of the tree (i.e., the length of the deepest path, counting from 0)
	 */
	public int height();
	
	/**
	 * Returns the number of nodes in the tree.
	 * @return the number of nodes in the tree.
	 */
	public int size();
	
	/**
	 * Returns true if the tree is empty.
	 * @return true if the tree is empty.
	 */
	public boolean isEmpty();
	
}
