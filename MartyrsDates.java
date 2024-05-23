import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MartyrsDates implements Comparable<MartyrsDates> {
	private String date;
	private LinkedList martyrs;
	private int totalAge;
	private Martyr maxAge;
	private Martyr minAge;

	{
		martyrs = new LinkedList();
		totalAge = 0;
		maxAge = new Martyr();
		minAge = new Martyr();
	}

	public MartyrsDates() {

	}

	public MartyrsDates(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public LinkedList getMartyrs() {
		return martyrs;
	}

	public void setMartyrs(LinkedList martyrs) {
		this.martyrs = martyrs;
	}

	/* Override method to check the equality of two dates */
	@Override
	public boolean equals(Object date) {
		if (date instanceof MartyrsDates) {
			String str1 = ((MartyrsDates) date).getDate();
			String[] split1 = str1.split("/");
			String[] split2 = this.date.split("/");
			int day1 = Integer.parseInt(split1[1]);
			int month1 = Integer.parseInt(split1[0]);
			int year1 = Integer.parseInt(split1[2]);

			int day2 = Integer.parseInt(split2[1]);
			int month2 = Integer.parseInt(split2[0]);
			int year2 = Integer.parseInt(split2[2]);
			return ((day1 == day2) && (month1 == month2) && (year1 == year2));
		}
		return false;
	}

	/* Override method to compare two dates */
	@Override
	public int compareTo(MartyrsDates date) {
		String str1 = date.getDate();
		String[] split1 = str1.split("/");
		String[] split2 = this.date.split("/");
		int day1 = Integer.parseInt(split1[1]);
		int month1 = Integer.parseInt(split1[0]);
		int year1 = Integer.parseInt(split1[2]);

		int day2 = Integer.parseInt(split2[1]);
		int month2 = Integer.parseInt(split2[0]);
		int year2 = Integer.parseInt(split2[2]);

		if (year1 != year2)
			return year2 - year1;
		else if (month1 != month2)
			return month2 - month1;
		else
			return day2 - day1;
	}

	@Override
	public String toString() {
		return date;
	}

	/* Method to check the existence of a martyr in a given date */
	public SNode checkMartyr(Martyr mart) {
		SNode node = martyrs.getFront();
		if (node == null)
			return null;

		while (node != null) {
			if (((Martyr) node.getElement()).equals(mart)) {
				return node;
			}
			node = node.getNext();
		}
		return null;
	}

	/*
	 * Method to add a martyr to the list sorted by age and gender and add the age
	 * of the martyr to the total
	 */
	public void addMartyr(Martyr mart) {
		SNode node = martyrs.getFront();

		if (martyrs.size() == 0) {
			martyrs.addFirst(new SNode(mart));
			maxAge = minAge = mart;
			totalAge += mart.getAge();
			return;
		}

		if (mart.getAge() > maxAge.getAge())
			maxAge = mart;
		else if (mart.getAge() < minAge.getAge())
			minAge = mart;

		for (int i = 0; i < martyrs.size(); i++) {
			Martyr curr = (Martyr) node.getElement();
			if (curr.getAge() >= mart.getAge() && curr.getGender() >= mart.getGender()) {
				martyrs.add(new SNode(mart), i);
				totalAge += mart.getAge();
				return;
			}
		}
		martyrs.addLast(new SNode(mart));
		totalAge += mart.getAge();
	}

	public int getTotal() {
		return martyrs.size();
	}

	public int getTotalAge() {
		return totalAge;
	}

	public int getAverage() {
		if (totalAge == 0 || getTotal() == 0)
			return 0;
		return totalAge / getTotal();
	}

	public Martyr getOld() {
		return maxAge;
	}

	public void setOld(Martyr maxAge) {
		this.maxAge = maxAge;
	}

	public Martyr getYoung() {
		return minAge;
	}

	public void setYoung(Martyr minAge) {
		this.minAge = minAge;
	}

	/*
	 * This method to add all the martyrs of the list to a table view and returns it
	 */
	public TableView<Martyr> getMartyrsList() {
		ObservableList<Martyr> list = FXCollections.observableArrayList();
		list.clear();
		LinkedList temp = sortMartyrs();
		SNode node = temp.getFront();
		for (int i = 0; i < temp.size(); i++) {
			list.add((Martyr) node.getElement());
			node = node.getNext();
		}

		TableView<Martyr> table = new TableView<Martyr>();
		TableColumn<Martyr, String> tName = new TableColumn<Martyr, String>("name");
		TableColumn<Martyr, Byte> tAge = new TableColumn<Martyr, Byte>("age");
		TableColumn<Martyr, Character> tGender = new TableColumn<Martyr, Character>("gender");
		tName.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
		tAge.setCellValueFactory(new PropertyValueFactory<Martyr, Byte>("age"));
		tGender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
		table.getColumns().add(tName);
		table.getColumns().add(tAge);
		table.getColumns().add(tGender);
		tName.setPrefWidth(250);
		tAge.setPrefWidth(150);
		tGender.setPrefWidth(150);
		table.setPrefHeight(200);
		table.setPrefWidth(500);
		table.setItems(list);
		return table;
	}

	/*
	 * This method to add the martyrs to an observable list to add it to a combo box
	 */
	public ObservableList<Martyr> getList() {
		ObservableList<Martyr> list = FXCollections.observableArrayList();
		list.clear();
		LinkedList temp = sortMartyrs();
		SNode node = temp.getFront();
		for (int i = 0; i < temp.size(); i++) {
			list.add((Martyr) node.getElement());
			node = node.getNext();
		}
		return list;
	}

	/* A method to sort the martyrs by name */
	public LinkedList sortMartyrs() {
		Stack stack = new Stack();
		SNode node = martyrs.getFront();
		while (node != null) {
			stack.push(node);
			node = node.getNext();
		}

		LinkedList temp = new LinkedList();
		boolean flag = false;
		while (!stack.isEmpty()) {
			SNode curr = (SNode) stack.pop();
			if (temp.size() == 0) {
				temp.addFirst(curr);
				continue;
			} else {
				flag = false;
				SNode tempNode = temp.getFront();
				for (int i = 0; i < temp.size(); i++) {
					if (((Martyr) curr.getElement()).compareTo((Martyr) tempNode.getElement()) < 0) {
						temp.add(curr, i);
						flag = true;
						break;
					}
					tempNode = tempNode.getNext();
				}
				if (!flag) {
					temp.addLast(curr);
				}

			}
		}
		return temp;
	}
}
