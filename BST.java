
public class BST {
	private BNode root;

	public BST() {
		root = null;
	}

	public boolean contains(Object element) {
		return contains(root, element);
	}

	private boolean contains(BNode root, Object element) {
		if (element instanceof District) {
			District dist = (District) element;
			if (root == null)
				return false;
			else if (dist.equals((District) root.getElement()))
				return true;
			else if (dist.compareTo((District) root.getElement()) > 0)
				return contains(root.getRight(), element);
			else
				return contains(root.getLeft(), element);
		} else if (element instanceof Location) {
			Location loc = (Location) element;
			if (root == null)
				return false;
			else if (loc.equals((Location) root.getElement()))
				return true;
			else if (loc.compareTo((Location) root.getElement()) > 0)
				return contains(root.getRight(), element);
			else
				return contains(root.getLeft(), element);
		} else if (element instanceof MartyrsDates) {
			MartyrsDates date = (MartyrsDates) element;
			if (root == null)
				return false;
			else if (date.equals((MartyrsDates) root.getElement()))
				return true;
			else if (date.compareTo((MartyrsDates) root.getElement()) > 0)
				return contains(root.getRight(), element);
			else
				return contains(root.getLeft(), element);
		} else
			return true;
	}

	public BNode find(Object element) {
		return find(root, element);
	}

	private BNode find(BNode root, Object element) {
		if (element instanceof District) {
			District dist = (District) element;
			if (root == null)
				return null;
			else if (dist.equals((District) root.getElement()))
				return root;
			else if (dist.compareTo((District) root.getElement()) > 0)
				return find(root.getRight(), element);
			else
				return find(root.getLeft(), element);
		} else if (element instanceof Location) {
			Location loc = (Location) element;
			if (root == null)
				return null;
			else if (loc.equals((Location) root.getElement()))
				return root;
			else if (loc.compareTo((Location) root.getElement()) > 0)
				return find(root.getRight(), element);
			else
				return find(root.getLeft(), element);
		} else if (element instanceof MartyrsDates) {
			MartyrsDates date = (MartyrsDates) element;
			if (root == null)
				return null;
			else if (date.equals((MartyrsDates) root.getElement()))
				return root;
			else if (date.compareTo((MartyrsDates) root.getElement()) > 0)
				return find(root.getRight(), element);
			else
				return find(root.getLeft(), element);
		} else
			return null;
	}

	public Object min() {
		if (root == null) {
			// System.out.println("The tree is Empty");
			return null;
		}
		return min(root).getElement();
	}

	public BNode min(BNode root) {
		if (root == null) {
			// System.out.println("The tree is Empty");
			return null;
		} else if (root.getLeft() == null) {
			return root;
		}
		return min(root.getLeft());
	}

	public Object max() {
		if (root == null) {
			// System.out.println("The tree is Empty");
			return null;
		}
		return max(root).getElement();
	}

	public BNode max(BNode root) {
		if (root == null) {
			// System.out.println("The tree is Empty");
			return null;
		} else if (root.getRight() == null) {
			return root;
		}
		return max(root.getRight());
	}

	public void insert(Object element) {
		root = insert(root, element);
	}

	private BNode insert(BNode root, Object element) {
		if (element instanceof District) {
			District dist = (District) element;
			if (root == null) {
				root = new BNode(element);
				return root;
			} else {
				if (dist.compareTo((District) root.getElement()) > 0)
					root.setRight(insert(root.getRight(), element));
				else if (dist.compareTo((District) root.getElement()) < 0)
					root.setLeft(insert(root.getLeft(), element));

			}
			return root;
		} else if (element instanceof Location) {
			Location loc = (Location) element;
			if (root == null) {
				root = new BNode(element);
				return root;
			} else {
				if (loc.compareTo((Location) root.getElement()) > 0)
					root.setRight(insert(root.getRight(), element));
				else if (loc.compareTo((Location) root.getElement()) < 0)
					root.setLeft(insert(root.getLeft(), element));

			}
			return root;
		} else if (element instanceof MartyrsDates) {
			MartyrsDates date = (MartyrsDates) element;
			if (root == null) {
				root = new BNode(element);
				return root;
			} else {
				if (date.compareTo((MartyrsDates) root.getElement()) > 0)
					root.setRight(insert(root.getRight(), element));
				else if (date.compareTo((MartyrsDates) root.getElement()) < 0)
					root.setLeft(insert(root.getLeft(), element));

			}
			return root;
		} else
			return null;
	}

	public void remove(Object element) {
		root = remove(root, element);
	}

	private BNode remove(BNode root, Object element) {
		if (element instanceof District) {
			District dist = (District) element;
			if (root == null)
				return null;
			else {
				if (dist.compareTo((District) root.getElement()) > 0)
					root.setRight(remove(root.getRight(), element));
				else if (dist.compareTo((District) root.getElement()) < 0)
					root.setLeft(remove(root.getLeft(), element));
				else {
					if (root.getLeft() != null && root.getRight() != null) {
						root.setElement(min(root.getRight()).getElement());
						remove(root.getRight(), element);
					} else {
						root = root.getRight() != null ? root.getRight() : root.getLeft();
					}
				}
			}
			return root;
		} else if (element instanceof Location) {
			Location loc = (Location) element;
			if (root == null)
				return null;
			else {
				if (loc.compareTo((Location) root.getElement()) > 0)
					root.setRight(remove(root.getRight(), element));
				else if (loc.compareTo((Location) root.getElement()) < 0)
					root.setLeft(remove(root.getLeft(), element));
				else {
					if (root.getLeft() != null && root.getRight() != null) {
						root.setElement(min(root.getRight()).getElement());
						remove(root.getRight(), element);
					} else {
						root = root.getRight() != null ? root.getRight() : root.getLeft();
					}
				}
			}
			return root;
		} else if (element instanceof MartyrsDates) {
			MartyrsDates date = (MartyrsDates) element;
			if (root == null)
				return null;
			else {
				if (date.compareTo((MartyrsDates) root.getElement()) > 0)
					root.setRight(remove(root.getRight(), element));
				else if (date.compareTo((MartyrsDates) root.getElement()) < 0)
					root.setLeft(remove(root.getLeft(), element));
				else {
					if (root.getLeft() != null && root.getRight() != null) {
						root.setElement(min(root.getRight()).getElement());
						remove(root.getRight(), element);
					} else {
						root = root.getRight() != null ? root.getRight() : root.getLeft();
					}
				}
			}
			return root;
		} else
			return null;
	}

	public BNode getRoot() {
		return root;
	}

	public void inOrder() {
		if (root == null)
			return;
		inOrder(root);
	}

	private void inOrder(BNode root) {
		if (root != null) {
			inOrder(root.getLeft());
			System.out.println(root.getElement());
			inOrder(root.getRight());
		}
	}

	public void levelByLevel() {
		levelByLevel(root);
	}

	private void levelByLevel(BNode root) {
		if (root == null)
			return;
		Queue queue = new Queue();
		queue.enQueue(root);
		while (!queue.isEmpty()) {
			BNode curr = (BNode) queue.front();
			System.out.println(curr.getElement());
			if (curr.getLeft() != null)
				queue.enQueue(curr.getLeft());
			if (curr.getRight() != null)
				queue.enQueue(curr.getRight());
			queue.deQueue();
		}
	}
}
