
public class LinkedList {
	private SNode front, back;
	private int size;

	public LinkedList() {

	}

	public Object getFirst() {
		if (size == 0)
			return null;
		return front.getElement();
	}

	public Object getLast() {
		if (size == 0)
			return null;
		return back.getElement();
	}

	public void addFirst(SNode node) {
		if (size == 0)
			front = back = node;
		else {
			node.setNext(front);
			front = node;
		}
		size++;
	}

	public void addLast(SNode node) {
		add(node);
	}

	public void add(SNode node) {
		if (size == 0)
			front = back = node;
		else {
			back.setNext(node);
			back = node;
		}
		size++;
	}

	public void add(SNode node, int index) {
		if (index == 0)
			addFirst(node);
		else if (index == size)
			add(node);
		else {
			SNode current = front;
			for (int i = 0; i < index - 1; i++)
				current = current.getNext();
			node.setNext(current.getNext());
			current.setNext(node);
			size++;
		}
	}

	public boolean removeFirst() {
		if (size == 0)
			return false;
		else if (size == 1) {
			front = back = null;
			size--;
			return true;
		}
		front = front.getNext();
		size--;
		return true;
	}

	public boolean removeLast() {
		if (size == 0)
			return false;
		else if (size == 1) {
			front = back = null;
			size--;
			return true;
		}

		SNode current = front;
		for (int i = 0; i < size - 2; i++)
			current = current.getNext();
		current.setNext(null);
		back = current;
		size--;
		return true;
	}

	public boolean remove(int index) {
		if (size == 0)
			return false;
		else if (index == 0)
			return removeFirst();
		else if (index == size - 1)
			return removeLast();
		SNode current = front;
		for (int i = 0; i < index - 1; i++)
			current = current.getNext();
		current.setNext(current.getNext().getNext());
		size--;
		return true;
	}

	public boolean remove(Object element) {
		if (size == 0)
			return false;
		else if (element.equals(front.getElement()))
			return removeFirst();
		else if (element.equals(back.getElement()))
			return removeLast();

		SNode current = front.getNext();
		SNode temp = front;
		for (int i = 0; i < size; i++) {
			if (element.equals(current.getElement())) {
				temp.setNext(current.getNext());
				size--;
				return true;
			}
			current = current.getNext();
			temp = temp.getNext();
		}
		return false;
	}

	public Object get(int index) {
		if (size < 0 || index >= size)
			return null;
		else if (index == 0)
			return getFirst();
		else if (index == size - 1)
			return getLast();
		SNode current = front;
		for (int i = 0; i < index; i++)
			current = current.getNext();
		return current.getElement();
	}

	public int size() {
		return size;
	}

	public SNode getFront() {
		return front;
	}

	public void setFront(SNode front) {
		this.front = front;
	}

	public SNode getBack() {
		return back;
	}

	public void setBack(SNode back) {
		this.back = back;
	}

	public void clear() {
		front = null;
	}

}
