package acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdventDay18 {

	static class Snailnum {
		protected Snailnum parent; 
		protected Snailnum left; // if left is null, then regleft won't be and vice versa ... also same for right
		protected Snailnum right;
		protected Integer regleft;
		protected Integer regright;
		public Snailnum(Snailnum left, Snailnum right, Integer regleft, Integer regright) {
			this.left = left;
			this.right = right;			
			this.regleft = regleft;
			this.regright = regright;
			if (this.left!=null)
				this.left.parent = this;
			if (this.right!=null)
				this.right.parent = this;
		}
		public Snailnum(Snailnum left, Snailnum right, Integer regleft, Integer regright, Snailnum parent) {
			this(left, right, regleft, regright);
			this.left.parent = parent;
			this.right.parent = parent;
		}
		@Override
		public String toString() {
			String ret = "[";
			if (this.left != null) {
				ret += this.left.toString();
			} else {
				ret += this.regleft.toString();
			}
			ret += ",";
			if (this.right != null) {
				ret += this.right.toString();
			} else {
				ret += this.regright.toString();
			}
			ret += "]";
			return ret;
		}
		public Snailnum add(Snailnum other) {
			Snailnum result = new Snailnum(this, other, null, null, null);
			return result;
		}
		public int magnitude() {
			return 0;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		puzzle1and2();
	}

	protected static ArrayList<String> extractLines() throws FileNotFoundException {
		Scanner filein = new Scanner(new File("day18.txt"));
		ArrayList<String> lines = new ArrayList<>();
		while (filein.hasNextLine()) {
			lines.add(filein.nextLine());
		}
		filein.close();
		return lines;
	}

	protected static Snailnum pair(String str) {
		if (str.charAt(ci)!='[') {
			// parsing error
		} else {
			// one of two possibilities now ... either we have a nested pair or a regular number
			Snailnum left=null;
			Snailnum right=null;
			Integer regleft=null;
			Integer regright=null;
			ci++;
			if (str.charAt(ci)=='[') {
				left = pair(str);
			} else {
				regleft = str.charAt(ci)-48; // convert to number (all numbers are only single digit)
			}
			ci++;
			if (str.charAt(ci)!=',') {
				// parsing error
			} else {
				ci++;
				if (str.charAt(ci)=='[') {
					right=pair(str);
				} else {
					regright = str.charAt(ci)-48; // convert to number (all numbers are only single digit)
				}
			}
			ci++;
			if (str.charAt(ci)!=']') {
				// parsing error
			}
			return new Snailnum(left, right, regleft, regright);
		}
		return null;
	}
	
	protected static int ci = 0; // used during parsing
	protected static Snailnum parse(String str) {
		ci = 0; // reset ci to 0 for the next string we are parsing
		return pair(str);
	}
	
	private static void puzzle1and2() throws FileNotFoundException {
		ArrayList<String> lines = extractLines();
		ArrayList<Snailnum> nums = new ArrayList<>();
		for (String line: lines)
			nums.add(parse(line));
		Snailnum result = nums.get(0);
//		reduce(result);
		for (int i=1; i<nums.size(); i++) {
			result = result.add(nums.get(i));
			reduce(result);
			System.out.println(result);
		}
		System.out.println(result.magnitude());
	}
	
	public static void reduce(Snailnum num) {
		System.out.println(num);
		// first always try to go down the left side
		if (explode(num.left, 1))
			reduce(num); // recursively call reduce if our helper did something
		else if (explode(num.right, 1))
			reduce(num); // recursively call reduce if our helper did something
		else if (split(num.left))
			reduce(num);
		else if (split(num.right))
			reduce(num);			
	}	

	// finds the most immediate regular number to the left of this snailnum
	public static Snailnum findleft(Snailnum num) {
		if (num.parent==null) // root
			return null;
		else if (num.parent.right==num) {
			// this was the right child ... there MUST be a regular number somewhere in the left child
			// find the rightmost regular number in the left child of the parent
			if (num.parent.left==null)
				return num.parent;
			else
				return findrightmostleaf(num.parent.left);
		} else if (num.parent.left==num) {
			// this is trickier ... we need to keep going up until a parent is a right child
			Snailnum p = num.parent;
			while (p !=null && p.parent!=null && p.parent.right!=p) {
				p = p.parent;
			}
			if (p != null && p.parent != null) {
				if (p.parent.left == null)
					return p.parent;
				return findrightmostleaf(p.parent.left);
			}
		}
		return null;
	}

	public static Snailnum findright(Snailnum num) {
		if (num.parent==null) // root
			return null;
		else if (num.parent.left==num) {
			// this was the left child ... there MUST be a regular number somewhere in the right child (i.e., my sibling)
			// find the leftmost regular number in the right child of the parent
			if (num.parent.right==null)
				return num.parent;
			else 
				return findleftmostleaf(num.parent.right);
		} else if (num.parent.right==num) {
			// this is trickier ... we need to keep going up until a parent is a left child
			Snailnum p = num.parent;
			while (p !=null && p.parent!=null && p.parent.left!=p) {
				p = p.parent;
			}
			if (p != null && p.parent!=null) {
				if (p.parent.right == null)
					return p.parent;
				return findleftmostleaf(p.parent.right);
			}
		}
		return null;
	}
	
	public static Snailnum findleftmostleaf(Snailnum num) {
		if (num==null)
			return null;
		else if (num.regleft!=null)
			return num;
		else
			return num.left!=null?findleftmostleaf(num.left):findleftmostleaf(num.right);
	}

	public static Snailnum findrightmostleaf(Snailnum num) {
		if (num==null)
			return null;
		else if (num.regright!=null)
			return num;
		else
			return num.right!=null?findrightmostleaf(num.right):findrightmostleaf(num.left);
	}

	public static boolean explode(Snailnum num, int depth) {
		if (num==null)
			return false;
		
		// base case, we made it to regular numbers for both left and right
		if (num.left==null && num.right==null)
			return false;
		else if (depth==3) {
			// uh-oh we need to explode if either left or right is not null
			// handle them separately b/c otherwise it is confusing as hell
			if (num.left!=null) {
				// we are exploding num.left
				Snailnum leftreg = findleft(num.left);
				if (leftreg!=null && leftreg.regright != null)
					leftreg.regright = leftreg.regright + num.left.regleft;
				else if (leftreg!=null && leftreg.regleft != null)
					leftreg.regleft = leftreg.regleft + num.left.regleft;
				Snailnum rightreg = findright(num.left);
				if (rightreg!=null && rightreg.regleft != null)
					rightreg.regleft = rightreg.regleft + num.left.regright;					
				else if (rightreg!=null && rightreg.regright != null)
					rightreg.regright = rightreg.regright + num.left.regright;					
				// replace the old pair that we just obliterated with a regular number 0
				num.left=null;
				num.regleft = 0;
			} else {
				// we are exploding num.right
				Snailnum leftreg = findleft(num.right);
				if (leftreg!=null && leftreg.regright != null)
					leftreg.regright = leftreg.regright + num.right.regleft;
				else if (leftreg!=null && leftreg.regleft != null)
					leftreg.regleft = leftreg.regleft + num.right.regleft;
				Snailnum rightreg = findright(num.right);
				if (rightreg!=null && rightreg.regleft != null)
					rightreg.regleft = rightreg.regleft + num.right.regright;					
				else if (rightreg!=null && rightreg.regright != null)
					rightreg.regright = rightreg.regright + num.right.regright;					
				// replace the old pair that we just obliterated with a regular number 0
				num.right=null;
				num.regright = 0;
			}
			return true; // we did something!
		} else {
			if (num.left!=null) {
				if (explode(num.left, depth+1))
					return true; // we did something
			}
			if (num.right!=null) {
				if (explode(num.right, depth+1))
					return true; // we did something
			}
		}
		return false; // we didn't end up exploding anything ... so this help reduce try the next action			
	}
	public static boolean split(Snailnum num) {
		if (num==null)
			return false;
		if (num.left!=null)
			if (split(num.left))
				return true; 
		if (num.regleft!=null)
			if (num.regleft>=10) {
				num.left = new Snailnum(null, null, num.regleft/2, (int)Math.ceil(num.regleft/2.0));
				num.left.parent = num;
				num.regleft = null;
				return true; // we did something
			}
		if (num.right!=null)
			if (split(num.right))
				return true; 
		if (num.regright!=null)
			if (num.regright>=10) {
				num.right = new Snailnum(null, null, num.regright/2, (int)Math.ceil(num.regright/2.0));
				num.right.parent = num;
				num.regright = null;
				return true; // we did something
			}
		return false; // we didn't do anything
	}		

}
