package trees;

import java.util.ArrayList;

public class TreeNode<T> implements Node<T> {

	protected T data;
	protected Node<T> parent;
	protected ArrayList<Node<T>> children;
	
	// Parent node not being set ... therefore likely the root node
	public TreeNode(T data) {
		this.data = data;
		this.children = new ArrayList<>();
	}
	
	// No children, possibly a leaf node
	public TreeNode(T data, TreeNode<T> parent) {
		this(data);
		parent.children.add(this); // tell the parent that it has a child
		this.parent = parent;      // tell the child who its parent is
	}
	
	// This constructor frequently used when restructuring a tree
	public TreeNode(T data, TreeNode<T> parent, ArrayList<Node<T>> children) {
		this(data, parent);
		this.children = children;
	}
	
	/**
	 * Creates a new node and adds it to the list of children.
	 * @param childdata - data for the child node
	 * @return the newly created node
	 */
	public Node<T> addChild(T childData) {
		Node<T> childNode = new TreeNode<>(childData, this);
		addChild(childNode);
		return childNode;
	}

	/**
	 * Creates a new node and adds it to the list of children.
	 * @param childdata - data for the child node
	 * @return the newly created node
	 */
	public void addChild(Node<T> childNode) {
		((TreeNode<T>) childNode).parent = this;
		children.add(childNode);
	}
	
	@Override
	public T getElement() {		
		return data;
	}

	@Override
	public void setElement(T newdata) {
		data = newdata;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}

}
