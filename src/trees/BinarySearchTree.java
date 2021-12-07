package trees;

public class BinarySearchTree<T> {
	
	protected BinaryTree<T> tree;
	protected int size; // different than tree size b/c the tree has fake leaves which get promoted to house data
	
	public BinarySearchTree() {
		tree = new BinaryTree<>();
	}
		
	/** 
	 * Add data to be indexed by our tree. This requires creating a BinaryTreeNode for the data
	 * and adding it at the correct spot in the tree. Plus, we may need to rebalance the tree afterwards.
	 * @return
	 */
	public void addData(T data) {
		// special case when adding the very first piece of data
		if (size==0) {
			tree.setRoot(data);
			promoteNode((BinaryTreeNode<T>)tree.root(), data);
		} else {
			// let's find the "leaf" where this data belongs
			BinaryTreeNode<T> nloc = searchTree(data);
			if (nloc.getElement()!=null) {
				// uh-oh, this data is already in the tree!
				System.err.println("data already in the tree ... ignoring!");
			} else {
				promoteNode(nloc, data);
			}
		}
	}
	
	// promote Node n to a node that "houses" data and has two leaf nodes to hold new data
	// Note that this method is only called once when we are adding data ... so it is a nice centralized place to increment our size
	protected void promoteNode(BinaryTreeNode<T> n, T data) {
		size++;
		n.setElement(data);
		tree.setLeft(n, null);
		tree.setRight(n, null);
	}
	
	protected BinaryTreeNode<T> searchTree(T data) {
		return searchTree((BinaryTreeNode<T>)tree.root(), data);
	}
	
	// recursive algo
	//   base case: made it to a leaf OR found the data already at an internal node... return that leaf or internal node
	//   recursive step: still haven't made it to the leaf
	protected BinaryTreeNode<T> searchTree(BinaryTreeNode<T> n, T data) {
		Comparable<T> c = (Comparable<T>) n.getElement();
		if (c==null || c.compareTo(data)==0) {
			// base case: made it to "fake leaf" or found a hit ... meaning data already in the tree
			return n;
		} else if (c.compareTo(data)>0) {
			return searchTree(n.left, data);
		} else if (c.compareTo(data)<0) {
			return searchTree(n.right, data);
		}
		return null; // should never happen (all possibilities for compareTo are accounted for)
	}
	
	@Override
	public String toString() {
		return tree.toString();
	}
}

