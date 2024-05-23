
public class Stack {
	private SQNode front;
	private int size;

	public Stack() {
		front = null;
		size = 0;
	}

	public void push(Object element) {
		if (size == 0)
			front = new SQNode(element);
		else {
			SQNode node = new SQNode(element);
			node.setNext(front);
			front = node;
		}
		size++;
	}

	public Object pop() {
		if (isEmpty())
			return null;
		Object temp = front.getElement();
		front = front.getNext();
		size--;
		return temp;
	}

	public Object peek() {
		if (isEmpty())
			return null;
		return front.getElement();
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
}
