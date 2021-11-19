package trees;

import java.util.ArrayList;

public class GenericTree<T> implements Tree<T> {

	protected Node<T> rootNode;
	protected int size;
	
	// Constructors on Monday ... don't forget to init size correctly
	
	@Override
	public Node<T> root() {
		return rootNode;
	}

	@Override
	public ArrayList<Node<T>> leaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<T> parent(Node<T> n) {
		return ((TreeNode<T>)n).parent;
	}

	@Override
	public ArrayList<Node<T>> children(Node<T> n) {
		return ((TreeNode<T>)n).children;
	}

	@Override
	public ArrayList<Node<T>> siblings(Node<T> n) {
		// TODO Auto-generated method stub
		return null;
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
