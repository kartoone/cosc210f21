package trees;

public class BinaryTreeDemo {

	public static void main(String[] args) {
		// Example tree from https://walker.cs.grinnell.edu/courses/207.sp12/readings/reading-intro-trees.shtml
/*
 *                                a
 *                              /   \
 *                             /     \
 *                            b       c
 *                           / \      /
 *                          e   f    g
 *                             / \  / \
 *                            k   l m  n
 * 
 */
		BinaryTree<String> tree = new BinaryTree<>("a");
		BinaryTreeNode<String> bnode = tree.setLeft((BinaryTreeNode<String>)tree.root(), "b");
		BinaryTreeNode<String> cnode = tree.setRight((BinaryTreeNode<String>)tree.root(), "c");
		tree.setLeft(bnode, "e");
		BinaryTreeNode<String> fnode = tree.setRight(bnode, "f");
		tree.setLeft(fnode, "k");
		tree.setRight(fnode, "l");
		BinaryTreeNode<String> gnode = tree.setLeft(cnode, "g");
		tree.setLeft(gnode, "m");
		tree.setRight(gnode, "n");
		System.out.println("tree.size(): " + tree.size());
		System.out.println("tree.countDescendents(bnode): " + tree.countDescendents(bnode));
		System.out.println("tree.depth(tree.root()):" + tree.depth(tree.root()));
		System.out.println("tree.depth(gnode): " + tree.depth(gnode));
		System.out.println("tree.siblings(bnode): " + tree.siblings(bnode));
		System.out.println("tree:");
		System.out.println(tree);
		System.out.println("tree.prettyString():");
		System.out.println(tree.prettyString());
		System.out.println("leaves: " + tree.leaves());
		System.out.println("tree.height(): " + tree.height());
		System.out.println("tree.size(): " + tree.size());
	}

}