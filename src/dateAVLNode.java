
public class dateAVLNode {
	private DateStack dateStack;
	private dateAVLNode left;
	private dateAVLNode right;
	private int height;

	public dateAVLNode(DateStack dateStack) {
		this(dateStack, null, null);
	}

	public dateAVLNode(DateStack dataStack, dateAVLNode left, dateAVLNode right) {
		this.dateStack = dataStack;
		this.left = left;
		this.right = right;
		this.height = 0;
	}

	public DateStack getDateStack() {
		return dateStack;
	}

	public void setDateStack(DateStack dateStack) {
		this.dateStack = dateStack;
	}

	public dateAVLNode getLeft() {
		return left;
	}

	public void setLeft(dateAVLNode left) {
		this.left = left;
	}

	public dateAVLNode getRight() {
		return right;
	}

	public void setRight(dateAVLNode right) {
		this.right = right;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public String toString() {
		return dateStack.toString();
	}
}
