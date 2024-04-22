
public class stackNode {
	private Martyr martyr;
	private stackNode next;

	public stackNode(Martyr martyr) {
		this(martyr, null);

	}

	public stackNode(Martyr martyr, stackNode next) {
		this.martyr = martyr;
		this.next = next;
	}

	public Martyr getMartyr() {
		return martyr;
	}

	public void setMartyr(Martyr martyr) {
		this.martyr = martyr;
	}

	public stackNode getNext() {
		return next;
	}

	public void setNext(stackNode next) {
		this.next = next;
	}

}
