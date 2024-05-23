
public class BNode {
	private Object element;
	private BNode right, left;

	public BNode(Object element) {
		this(element, null, null);
	}

	public BNode(Object element, BNode right, BNode left) {
		this.element = element;
		this.right = right;
		this.left = left;
	}

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public BNode getRight() {
		return right;
	}

	public void setRight(BNode right) {
		this.right = right;
	}

	public BNode getLeft() {
		return left;
	}

	public void setLeft(BNode left) {
		this.left = left;
	}

}
