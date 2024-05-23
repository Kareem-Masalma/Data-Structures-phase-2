
public class Location implements Comparable<Location> {
	private String name;
	private BST dates;
	private int total;
	private MartyrsDates max;

	{
		dates = new BST();
		total = 0;
		max = new MartyrsDates();
	}

	public Location() {

	}

	public Location(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BST getDates() {
		return dates;
	}

	public void setDates(BST dates) {
		this.dates = dates;
	}

	public int getTotal() {
		total = 0;
		inOrder(dates.getRoot());
		return total;
	}

	/* In order traversal */
	private void inOrder(BNode root) {
		if (root != null) {
			inOrder(root.getLeft());
			total += ((MartyrsDates) root.getElement()).getMartyrs().size();
			inOrder(root.getRight());
		}
	}

	/* Override method to check the equality of two locations by their names */
	@Override
	public boolean equals(Object location) {
		if (location instanceof Location) {
			return name.equalsIgnoreCase(((Location) location).getName());
		}
		return false;
	}

	/* Override method to compare between two locations' names */
	@Override
	public int compareTo(Location location) {
		return name.compareToIgnoreCase(location.getName());
	}

	@Override
	public String toString() {
		return name;
	}

	/* Method to search for a given date */
	public BNode checkDates(MartyrsDates date) {
		return dates.find(date);
	}

	/* Method to get the earliest martyrs date */
	public MartyrsDates getEarliest() {
		return (MartyrsDates) dates.min();
	}

	/* Method to get the latest martyrs date */
	public MartyrsDates getLatest() {
		return (MartyrsDates) dates.max();
	}

	/* Method to find the date with maximum number of martyrs */
	public MartyrsDates getMax() {
		findMax(dates.getRoot());
		return max;
	}

	/* Recursive method to find the date with maximum number of martyrs */
	public void findMax(BNode root) {
		if (root != null) {
			findMax(root.getLeft());
			if (((MartyrsDates) root.getElement()).getTotal() > max.getTotal())
				max = (MartyrsDates) root.getElement();
			findMax(root.getRight());
		}
	}
}
