package trees;

public class GenericTreeDemo {

	public static void main(String[] args) {
		// Example tree from https://walker.cs.grinnell.edu/courses/207.sp12/readings/reading-intro-trees.shtml
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
		System.out.println(tree.size());
		//System.out.println("countDescendents(root)...");
		//System.out.println(tree.countDescendents(tree.rootNode));
		//System.out.println("countDescendents(bnode)");
		//System.out.println(tree.countDescendents(bnode));
//		System.out.println("countDescendents(cnode)");
//		System.out.println(tree.countDescendents(cnode));
		System.out.println("countDescendents(gnode)");
		System.out.println(tree.countDescendents(gnode));
		System.out.println(tree.depth(tree.root()));
	}

}
