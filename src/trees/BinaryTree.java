package trees;

import java.util.ArrayList;

public class BinaryTree<T> implements Tree<T> {

	protected Node<T> rootNode;
	protected int size;				// keeps track of how many nodes are in the tree ... MUST be updated in many places
	
	// Default constructor for the "mythical" empty tree
	public BinaryTree() {
		size=0;
		rootNode=null;
	}
	
	// Typical root data constructor
	public BinaryTree(T rootData) {
		size=1;
		rootNode = new BinaryTreeNode<>(rootData);
	}
	
	public BinaryTree(BinaryTreeNode<T> rootNode) {
		size=1+countDescendents(rootNode); // design decision: preserve the children of the root node
		rootNode.parent = null;  // design decision: if the rootNode already had a parent we are eliminating the parent from the tree
		this.rootNode = rootNode;
	}
	
	public BinaryTreeNode setRoot(T rootData) {
		size = 1;
		BinaryTreeNode<T> rn = new BinaryTreeNode<>(rootData);
		this.rootNode = rn;
		return rn;
	}
	
	// Tree manipulation methods
	// DON'T FORGET TO UPDATE SIZE CORRECTLY!!!!
	/**
	 * Create a new node to host childData and set its parent to n and tell the parent it has a new left child
	 * @param n - the parent of the new node
	 * @param childData - the data for the new child node
	 * @return the newly created child node
	 */
	public BinaryTreeNode<T> setLeft(BinaryTreeNode<T> n, T childData) {
		if (n.left != null) {
			size = size - countDescendents(n.left);
		} else {
			size++; // only increment size by one b/c we are only creating one new node
		}
		BinaryTreeNode<T> childNode = new BinaryTreeNode<>(childData);
		childNode.parent = n;
		n.left = childNode;
		return childNode;
	}
	
	/**
	 * Create a new node to host childData and set its parent to n and tell the parent it has a new left child
	 * @param n - the parent of the new node
	 * @param childData - the data for the new child node
	 * @return the newly created child node
	 */
	public BinaryTreeNode<T> setRight(BinaryTreeNode<T> n, T childData) {
		if (n.right != null) {
			size = size - countDescendents(n.right);
		} else {
			size++; // only increment size by one b/c we are only creating one new node
		}
		BinaryTreeNode<T> childNode = new BinaryTreeNode<>(childData);
		childNode.parent = n;
		n.right = childNode;
		return childNode;
	}
	
	// Recursive algorithm that calculates the number of descendents of node n (not counting n itself)
	// base case: if n has no children, then return 0
	// recursive step: return the number of children plus the recursive call on each child
	protected int countDescendents(Node<T> n) {
		ArrayList<Node<T>> children = children(n);
		if (children.size()==0) {
			// base case: no children!
			return 0;
		} else {
			// recursive step: node has children!
			int descendents = children.size();
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
		ArrayList<Node<T>> leaves = new ArrayList<>();
		leavesHelper(rootNode, leaves);
		return leaves;
	}

	protected void leavesHelper(Node<T> n, ArrayList<Node<T>> leaflist) {
		// base case: n is a leaf
		// recursive step: n is NOT a leaf
		ArrayList<Node<T>> children = children(n);
		if (children.size()==0) {
			// base case: we found a leaf!
			leaflist.add(n);
		} else {
			// recursive step: this node has children, let's recursively call the method again on each child
			for (Node<T> child : children) {
				leavesHelper(child, leaflist);
			}
		}
	}
	
	@Override
	public Node<T> parent(Node<T> n) {
		return ((BinaryTreeNode<T>)n).parent;
	}

	@Override
	public ArrayList<Node<T>> children(Node<T> n) {
		ArrayList<Node<T>> tmpchildrenlist = new ArrayList<>();
		if (hasLeft(n)) {
			tmpchildrenlist.add(left(n));
		}
		if (hasRight(n)) {
			tmpchildrenlist.add(right(n));
		}
		return tmpchildrenlist;
	}

	public Node<T> right(Node<T> n) {
		return ((BinaryTreeNode<T>)n).right;
	}

	public boolean hasRight(Node<T> n) {
		return right(n)!=null;
	}

	public Node<T> left(Node<T> n) {
		return ((BinaryTreeNode<T>)n).left;
	}

	public boolean hasLeft(Node<T> n) {
		return left(n)!=null;
	}

	/**
	 * Return the siblings of node n (but don't include n)
	 */
	@Override
	public ArrayList<Node<T>> siblings(Node<T> n) {
		// Strategy: get the children of the parent of n - and add all the children to a new list except for the node n
		ArrayList<Node<T>> siblings = new ArrayList<>();
		if (n==rootNode) {
			return siblings;
		}
		for (Node<T> child: children(parent(n))) {
			if (child != n) {
				siblings.add(child);
			}
		}
		return siblings;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

	/**
	 * Recursively calculate the depth of node n.
	 *   base case: we made it to the root (i.e., node n has no parent)
	 *   recursive step: n is NOT the root (yet)
	 *  @param n - the node whose depth we are calculating
	 *  @return - the depth of node n
	 */
	@Override
	public int depth(Node<T> n) {
		if (parent(n)==null) { // equivalent to n==rootNode
			return 0; // base case: made it to the root
		} else {
			return 1+depth(parent(n));
		}
	}

	@Override
	public int height() {
		// Algorithm: find the maximum depth of all the leaves, that is the height
		// Step 1: get all the leaves
		// Step 2: iterate through all the leaves and find each leaf's depth
		// Step 3: return the maximum depth found
		ArrayList<Node<T>> leaves = leaves();
		int max = 0;
		for (Node<T> leaf : leaves) {
			int leafdepth = depth(leaf);
			if (leafdepth>max) {
				max = leafdepth;
			}
		}
		return max;
	}
	
	@Override
	public String toString() {
		return toStringHelperSpaces(root(), 0);
	}
	
	protected String toStringHelperSpaces(Node<T> n, int numspaces) {
		String retval = "";
		for (int i=0; i<numspaces; i++) {
			retval += " ";
		}
		retval += n.getElement() + "\n";
		ArrayList<Node<T>> children = children(n);
		for (Node<T> child : children) {
		    retval += toStringHelperSpaces(child, numspaces+2);				
		}
		return retval;
	}

	public String prettyString() {
		ArrayList<Boolean>lastchildhistory = new ArrayList<>();
		return toStringHelperLines(root(), 0, lastchildhistory);
	}

	protected String toStringHelperLines(Node<T> n, int numspaces, ArrayList<Boolean> lastchildhistory) {
		String retval = " ";
		for (int i=0; i<numspaces; i++) {
			if (i%2==0 && i<numspaces-2) {
				retval += lastchildhistory.get(i/2)?" ":"│";
			} else if (i==numspaces-2) {
				retval += lastchildhistory.get(i/2)?"└":"├";
			} else if (i<numspaces-2) {
				retval += " ";
			} else {
				retval += "─";
			}
		}
		retval += n.getElement() + "\n";
		ArrayList<Node<T>> children = children(n);
		for (int i=0; i<children.size(); i++) {
			Node<T> child = children.get(i);
			ArrayList<Boolean> lastchildhistoryR = (ArrayList<Boolean>)lastchildhistory.clone();
			lastchildhistoryR.add(i==children.size()-1);
			retval += toStringHelperLines(child, numspaces+2, lastchildhistoryR);				
		}
		return retval;
	}
}