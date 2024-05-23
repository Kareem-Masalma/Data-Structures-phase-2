
public class District implements Comparable<District> {
	private String name;
	private BST locations;
	private int total;

	{
		locations = new BST();
		total = 0;
	}

	public District() {

	}

	public District(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BST getLocations() {
		return locations;
	}

	public void setLocations(BST locations) {
		this.locations = locations;
	}

	public int getTotal() {
		return total;
	}

	public void incrementTotal() {
		this.total++;
	}

	/* Override method to check the equality of two districts by their names */
	@Override
	public boolean equals(Object district) {
		if (district instanceof District) {
			return name.equalsIgnoreCase(((District) district).getName());
		}
		return false;
	}

	/* Override method to compare between two districts' names */
	@Override
	public int compareTo(District district) {
		return name.compareToIgnoreCase(district.getName());
	}

	@Override
	public String toString() {
		return name;
	}

	public BNode checkLocation(Location loc) {
		return locations.find(loc);
	}
}
