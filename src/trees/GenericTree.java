package trees;

import java.util.ArrayList;

public class GenericTree<T> implements Tree<T> {

	protected Node<T> rootNode;
	protected int size;				// keeps track of how many nodes are in the tree ... MUST be updated in many places
	
	// Default constructor for the "mythical" empty tree
	public GenericTree() {
		size=0;
		rootNode=null;
	}
	
	// Typical root data constructor
	public GenericTree(T rootData) {
		size=1;
		rootNode = new TreeNode<>(rootData);
	}
	
	public GenericTree(TreeNode<T> rootNode) {
		size=1+countDescendents(rootNode); // design decision: preserve the children of the root node
		rootNode.parent = null;  // design decision: if the rootNode already had a parent we are eliminating the parent from the tree
		this.rootNode = rootNode;
	}
	
	// Tree manipulation methods
	// DON'T FORGET TO UPDATE SIZE CORRECTLY!!!!
	/**
	 * Create a new node to host childData and set its parent to n
	 * @param n - the parent of the new node
	 * @param childData - the data for the new child node
	 * @return the newly created child node
	 */
	public TreeNode<T> addChild(TreeNode<T> n, T childData) {
		TreeNode<T> childNode = new TreeNode<>(childData, n);
		size++; // only increment size by one b/c we are only creating one new node
		return childNode;
	}
	
	// Recursive algorithm that calculates the number of descendents of node n (not counting n itself)
	// base case: if n has no children, then return 0
	// recursive step: return the number of children plus the recursive call on each child
	protected int countDescendents(Node<T> n) {
		ArrayList<Node<T>> children = children(n);
		if (children.size()==0) {
			// base case: no children!
			System.out.println(n.getElement() + " 0 BASE");
			return 0;
		} else {
			// recursive step: node has children!
			int descendents = children.size();
			System.out.println(n.getElement() + " " + descendents + " RS");
			for (Node<T> child : children) {
				descendents = descendents + countDescendents(child);
			}
			return descendents;
		}
	}

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

	@Override
	public int depth(Node<T> n) {
		if (parent(n)==null) {
			return 0; // base case: made it to the root
		} else {
			return 1+depth(parent(n));
		}
	}

	@Override
	public int height() {
		// TODO Auto-generated method stub
		return 0;
	}

}
