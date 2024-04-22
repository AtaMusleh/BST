
public class martyrAVLNode {
	private Martyr martyr;
	private martyrAVLNode left;
	private martyrAVLNode right;
	private int height;

	public martyrAVLNode(Martyr martyr) {
		this(martyr, null, null);
	}

	public martyrAVLNode(Martyr martyr, martyrAVLNode left, martyrAVLNode right) {
		this.martyr = martyr;
		this.left = left;
		this.right = right;
		this.height = 0;
	}

	public Martyr getMartyr() {
		return martyr;
	}

	public void setMartyr(Martyr martyr) {
		this.martyr = martyr;
	}

	public martyrAVLNode getLeft() {
		return left;
	}

	public void setLeft(martyrAVLNode left) {
		this.left = left;
	}

	public martyrAVLNode getRight() {
		return right;
	}

	public void setRight(martyrAVLNode right) {
		this.right = right;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "martyrAVLNode [martyr=" + martyr + "]";
	}

}
