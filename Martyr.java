
public class Martyr implements Comparable<Martyr> {
	private String name;
	private byte age;
	private char gender;

	public Martyr() {

	}

	public Martyr(String name, byte age, char gender) {
		this.name = name;
		this.age = age;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	/*
	 * Override method to check the equality of two martyrs by their names, ages and
	 * genders
	 */
	@Override
	public boolean equals(Object mart) {
		if (mart instanceof Martyr) {
			if (name == null)
				return false;
			return name.equalsIgnoreCase(((Martyr) mart).getName()) && age == ((Martyr) mart).getAge()
					&& gender == ((Martyr) mart).getGender();
		}
		return false;
	}

	/* Override method to compare two martyrs by their names */
	@Override
	public int compareTo(Martyr mart) {
		return name.compareToIgnoreCase(mart.getName());
	}

	@Override
	public String toString() {
		return name;
	}
}
