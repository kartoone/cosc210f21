package trees;

public class GenericTreeDemo {

	public static void main(String[] args) {
		// Example tree from https://walker.cs.grinnell.edu/courses/207.sp12/readings/reading-intro-trees.shtml
/*
 *                                   a
 *                                /  |  \
 *                               /   |   \
 *                              /    |    \
 *                             /     |     \
 *                            b      c      d
 *                           / \     |    / | \
 *                          e   f    g   h  i  j
 *                             / \  / \     | / \
 *                            k   l m  n    o p q
 * 
 */
		GenericTree<String> tree = new GenericTree<>("a");
		TreeNode<String> bnode = tree.addChild((TreeNode<String>)tree.root(), "b");
		TreeNode<String> cnode = tree.addChild((TreeNode<String>)tree.root(), "c");
		TreeNode<String> dnode = tree.addChild((TreeNode<String>)tree.root(), "d");
		tree.addChild(bnode, "e");
		TreeNode<String> fnode = tree.addChild(bnode, "f");
		tree.addChild(fnode, "k");
		tree.addChild(fnode, "l");
		TreeNode<String> gnode = tree.addChild(cnode, "g");
		tree.addChild(gnode, "m");
		tree.addChild(gnode, "n");
		tree.addChild(dnode, "h");
		TreeNode<String> inode = tree.addChild(dnode, "i");
		tree.addChild(inode, "o");
		TreeNode<String> jnode = tree.addChild(dnode, "j");
		tree.addChild(jnode, "p");
		tree.addChild(jnode, "q");
		System.out.println("tree.size(): " + tree.size());
		System.out.println("tree.countDescendents(bnode): " + tree.countDescendents(bnode));
		System.out.println("tree.depth(tree.root()):" + tree.depth(tree.root()));
		System.out.println("tree.depth(gnode): " + tree.depth(gnode));
		System.out.println("tree.siblings(bnode): " + tree.siblings(bnode));
		System.out.println("tree:");
		System.out.println(tree);
		System.out.println("tree.prettyString():");
		System.out.println(tree.prettyString());
	}

}