package trees;

import java.util.ArrayList;

public class BinaryTreeNode<T> implements Node<T> {

	protected T data;
	protected BinaryTreeNode<T> parent;
	protected BinaryTreeNode<T> left;
	protected BinaryTreeNode<T> right;
	
	// Parent node not being set ... therefore likely the root node
	public BinaryTreeNode(T data) {
		this.data = data;
	}
	
	/**
	 * Creates a new node and sets it as the left child.
	 * @param childdata - data for the child node
	 * @return the newly created node
	 */
	public Node<T> setLeft(T childData) {
		BinaryTreeNode<T> childNode = new BinaryTreeNode<>(childData);
		this.left = childNode;
		childNode.parent = this;
		return childNode;
	}

	/**
	 * Creates a new node and sets it as the left child.
	 * @param childdata - data for the child node
	 * @return the newly created node
	 */
	public Node<T> setRight(T childData) {
		BinaryTreeNode<T> childNode = new BinaryTreeNode<>(childData);
		this.right = childNode;
		childNode.parent = this;
		return childNode;
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
