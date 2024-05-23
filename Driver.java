import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {
	private BST district;
	private BorderPane pane;
	private File file;
	private ComboBox<District> cbDistrict;
	private ComboBox<Location> cbLocation;
	private ComboBox<Martyr> cbMartyr;
	private ObservableList<District> distList;
	private ObservableList<Location> locList;
	private TableView<Location> tLocation;
	private TableView<MartyrsDates> tDate;
	private TableView<Martyr> tMart;
	private Stack stack;
	private Stack prev;
	private BNode curr;
	private Location currLocation;
	private BNode currDate;
	private LocalDate now;
	private Stack stackLocation;
	private Stack stackMartyr;

	@Override
	public void start(Stage stage) {
		district = new BST();
		distList = FXCollections.observableArrayList();
		locList = FXCollections.observableArrayList();
		cbDistrict = new ComboBox<District>(distList);
		cbLocation = new ComboBox<Location>(locList);
		pane = new BorderPane();
		Label welcome = new Label("Martyrs of Palestine");
		welcome.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-style: italic");

		/* Creating the Main menus for the operations */
		MenuBar bar = new MenuBar();
		Menu open = new Menu("file");
		Menu districtMenu = new Menu("District");
		Menu locationMenu = new Menu("Location");
		Menu martyrMenu = new Menu("Martyrs");
		bar.getMenus().addAll(open, districtMenu, locationMenu, martyrMenu);

		/* To open a file to read from it */
		MenuItem openFile = new MenuItem("open file");
		MenuItem saveFile = new MenuItem("save");
		MenuItem saveAsFile = new MenuItem("save as");
		open.getItems().addAll(openFile, saveFile, saveAsFile);

		/* The operations for the district screen */
		MenuItem addDistrict = new MenuItem("add district");
		MenuItem deleteDistrict = new MenuItem("remove district");
		MenuItem updateDistrict = new MenuItem("update district");
		MenuItem navigateDistrict = new MenuItem("navigate districts");
		MenuItem printOnFile = new MenuItem("print district on file");

		districtMenu.getItems().addAll(addDistrict, deleteDistrict, updateDistrict, navigateDistrict, printOnFile);

		/* To add menu for adding deleting and updating the locations */
		MenuItem addLocation = new MenuItem("add location");
		MenuItem deleteLocation = new MenuItem("delete location");
		MenuItem updateLocation = new MenuItem("update location");
		MenuItem navigateLocation = new MenuItem("navigate locations");

		locationMenu.getItems().addAll(addLocation, deleteLocation, updateLocation, navigateLocation);

		/* To add menu for adding deleting and updating the locations */
		MenuItem navigateMartyrs = new MenuItem("navigate dates");
		MenuItem addMartyr = new MenuItem("add martyr");
		MenuItem deleteMartyr = new MenuItem("delete martyr");
		MenuItem updateMartyr = new MenuItem("update martyr");
		MenuItem searchMartyr = new MenuItem("search martyrs");

		martyrMenu.getItems().addAll(addMartyr, deleteMartyr, updateMartyr, navigateMartyrs, searchMartyr);

		/* Open the file */
		openFile.setOnAction(e -> {
			FileChooser choose = new FileChooser();
			file = choose.showOpenDialog(stage);
			if (file != null) {
				read();
				welcome.setText("File read successfully");
				pane.setCenter(welcome);
			}
		});

		/* Save on the same file */
		saveFile.setOnAction(e -> saveFile());

		saveAsFile.setOnAction(e -> saveAsFile());

		/* Calling the buttons actions for the operations for the District screen */
		addDistrict.setOnAction(e -> addDistrict());
		deleteDistrict.setOnAction(e -> deleteDistrict());
		updateDistrict.setOnAction(e -> updateDistrict());
		navigateDistrict.setOnAction(e -> navigateDistrict());
		printOnFile.setOnAction(e -> printFile());

		/* Calling the buttons actions for the operations for the Location screen */
		addLocation.setOnAction(e -> addLocation());
		deleteLocation.setOnAction(e -> deleteLocation());
		updateLocation.setOnAction(e -> updateLocation());
		navigateLocation.setOnAction(e -> navigateLocation());

		/* Calling the buttons actions for the operations for the Martyr screen */
		navigateMartyrs.setOnAction(e -> navigateMartyrs());
		addMartyr.setOnAction(e -> addMartyr());
		deleteMartyr.setOnAction(e -> deleteMartyr());
		updateMartyr.setOnAction(e -> updateMartyr());
		searchMartyr.setOnAction(e -> searchMartyr());

		cbDistrict.setMinWidth(150);
		cbLocation.setMinWidth(150);
		pane.setCenter(welcome);
		pane.setTop(bar);
		String css = getClass().getResource("newStyle.css").toExternalForm();
		pane.getStylesheets().add(css);
		Scene scene = new Scene(pane, 1500, 700);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setTitle("Martyrs of Palestine");
		stage.show();
	}

	/* This method saves the data on a new file with a name from the user */
	public void saveAsFile() {
		pane.setLeft(null);
		Label lblResult = new Label();
		if (district == null || district.getRoot() == null) {
			lblResult.setText("No data");
			pane.setCenter(lblResult);
			BorderPane.setAlignment(lblResult, Pos.CENTER);
			return;
		}
		Label lblFileName = new Label("File name");
		TextField tfFileName = new TextField();
		Button btClear = new Button("clear");
		Button btSave = new Button("save");
		HBox hbButtons = new HBox(10);
		hbButtons.getChildren().addAll(btClear, btSave);
		hbButtons.setAlignment(Pos.CENTER);
		GridPane gPane = new GridPane();
		gPane.add(lblFileName, 0, 0);
		gPane.add(tfFileName, 1, 0);
		gPane.add(hbButtons, 1, 1);
		gPane.add(lblResult, 1, 2);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);
		pane.setCenter(gPane);

		btClear.setOnAction(e -> {
			tfFileName.clear();
			lblResult.setText("");
			return;
		});

		btSave.setOnAction(e -> {
			String fileName = tfFileName.getText();
			if (fileName.isBlank()) {
				lblResult.setText("No name was inserted");
				return;
			}
			if (district == null || district.getRoot() == null)
				return;
			File newFile = new File(fileName + ".csv");
			try (PrintWriter print = new PrintWriter(newFile)) {
				stack = new Stack();
				inOrder(district.getRoot());
				stack = reverse(stack);
				print.println("Name, event, Age, Location, District, Gender");

				while (!stack.isEmpty()) {
					District currDis = (District) ((BNode) stack.pop()).getElement();
					if (currDis.getLocations() == null || currDis.getLocations().getRoot() == null)
						continue;
					stackLocation = new Stack();
					inOrderLocation(currDis.getLocations().getRoot());
					stackLocation = reverse(stackLocation);
					while (!stackLocation.isEmpty()) {
						Location currLoc = (Location) ((BNode) stackLocation.pop()).getElement();
						if (currLoc.getDates() == null || currLoc.getDates().getRoot() == null)
							continue;
						stackMartyr = new Stack();
						inOrderDate(currLoc.getDates().getRoot());
						stackMartyr = reverse(stackMartyr);
						while (!stackMartyr.isEmpty()) {
							MartyrsDates currD = (MartyrsDates) ((BNode) stackMartyr.pop()).getElement();
							SNode node = currD.getMartyrs().getFront();
							while (node != null) {
								Martyr currMart = (Martyr) node.getElement();
								print.println(currMart.getName() + "," + currD.getDate() + "," + currMart.getAge() + ","
										+ currLoc.getName() + "," + currDis.getName() + "," + currMart.getGender());
								node = node.getNext();
							}
						}
					}
				}
				lblResult.setText("File is saved");
				lblResult.setTextFill(Color.WHITE);
				GridPane.setHalignment(lblResult, HPos.CENTER);
				tfFileName.clear();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}

		});
	}

	/* This method saves the data on the same file it read the data from */
	public void saveFile() {
		if (district == null || district.getRoot() == null)
			return;
		try (PrintWriter print = new PrintWriter(file)) {
			stack = new Stack();
			inOrder(district.getRoot());
			stack = reverse(stack);
			print.println("Name, event, Age, Location, District, Gender");

			while (!stack.isEmpty()) {
				District currDis = (District) ((BNode) stack.pop()).getElement();
				if (currDis.getLocations() == null || currDis.getLocations().getRoot() == null)
					continue;
				stackLocation = new Stack();
				inOrderLocation(currDis.getLocations().getRoot());
				stackLocation = reverse(stackLocation);
				while (!stackLocation.isEmpty()) {
					Location currLoc = (Location) ((BNode) stackLocation.pop()).getElement();
					if (currLoc.getDates() == null || currLoc.getDates().getRoot() == null)
						continue;
					stackMartyr = new Stack();
					inOrderDate(currLoc.getDates().getRoot());
					stackMartyr = reverse(stackMartyr);
					while (!stackMartyr.isEmpty()) {
						MartyrsDates currD = (MartyrsDates) ((BNode) stackMartyr.pop()).getElement();
						SNode node = currD.getMartyrs().getFront();
						while (node != null) {
							Martyr currMart = (Martyr) node.getElement();
							print.println(currMart.getName() + "," + currD.getDate() + "," + currMart.getAge() + ","
									+ currLoc.getName() + "," + currDis.getName() + "," + currMart.getGender());
							node = node.getNext();
						}
					}
				}
			}
			Label lblResult = new Label("File is saved");
			pane.setCenter(lblResult);
			lblResult.setTextFill(Color.WHITE);
			BorderPane.setAlignment(lblResult, Pos.CENTER);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/* A method to add the values of the tree to the stack */
	private void inOrderDate(BNode root) {
		if (root != null) {
			inOrderDate(root.getLeft());
			stackMartyr.push(root);
			inOrderDate(root.getRight());
		}
	}

	/*
	 * This method to search all martyrs with a part of their name, by their
	 * districts or their locations
	 */
	public void searchMartyr() {
		pane.setLeft(null);
		tMart = new TableView<Martyr>();
		TableColumn<Martyr, String> tName = new TableColumn<Martyr, String>("name");
		TableColumn<Martyr, Byte> tAge = new TableColumn<Martyr, Byte>("age");
		TableColumn<Martyr, Character> tGender = new TableColumn<Martyr, Character>("gender");
		tName.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
		tAge.setCellValueFactory(new PropertyValueFactory<Martyr, Byte>("age"));
		tGender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
		tMart.getColumns().add(tName);
		tMart.getColumns().add(tAge);
		tMart.getColumns().add(tGender);
		tName.setPrefWidth(250);
		tAge.setPrefWidth(150);
		tGender.setPrefWidth(150);
		tMart.setPrefHeight(200);
		tMart.setPrefWidth(500);
		Label lblResult = new Label();
		Label lblDistrict = new Label("district");
		Label lblLocation = new Label("location");
		Label lblName = new Label("name");
		TextField tfName = new TextField();
		Button btClear = new Button("clear");
		Button btSearch = new Button("search");
		HBox hbButtons = new HBox(10);
		hbButtons.getChildren().addAll(btClear, btSearch);
		hbButtons.setAlignment(Pos.CENTER);
		GridPane gPane = new GridPane();
		gPane.add(lblDistrict, 0, 0);
		gPane.add(cbDistrict, 1, 0);
		gPane.add(lblLocation, 0, 1);
		gPane.add(cbLocation, 1, 1);
		gPane.add(lblName, 0, 2);
		gPane.add(tfName, 1, 2);
		gPane.add(hbButtons, 1, 3);
		gPane.add(lblResult, 1, 4);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);
		pane.setCenter(gPane);
		cbDistrict.setMinWidth(200);
		cbLocation.setMinWidth(200);
		cbDistrict.setOnAction(e -> {
			if (cbDistrict != null) {
				District dist = cbDistrict.getValue();
				locList.clear();
				if (dist == null) {
					return;
				}
				locationsToList(dist.getLocations().getRoot());
			}
		});

		btClear.setOnAction(e -> {
			tfName.clear();
		});

		btSearch.setOnAction(e -> {
			String name = tfName.getText();
			District dist = cbDistrict.getValue();
			if (district == null || district.getRoot() == null)
				return;
			if (name.isBlank()) {
				lblResult.setText("Enter a name");
				lblResult.setTextFill(Color.RED);
				return;
			}
			if (dist == null) {
				searchAllData(name);
				pane.setLeft(tMart);
				return;
			}
			Location loc = cbLocation.getValue();
			if (loc == null) {
				searchInDistrict(dist, name);
				pane.setLeft(tMart);
				return;
			}
			searchInLocation(loc, name);
			pane.setLeft(tMart);
		});
	}

	/*
	 * This method to search martyrs in all dates in a location and display their
	 * data in a table view
	 */
	private void searchInLocation(Location loc, String name) {
		tMart.getItems().clear();
		stackMartyr = new Stack();
		inOrderDate(loc.getDates().getRoot());
		stackMartyr = reverse(stackMartyr);
		while (!stackMartyr.isEmpty()) {
			MartyrsDates currD = (MartyrsDates) ((BNode) stackMartyr.pop()).getElement();
			SNode node = currD.getMartyrs().getFront();
			while (node != null) {
				Martyr currMart = (Martyr) node.getElement();
				if (currMart.getName().contains(name))
					tMart.getItems().add(currMart);
				node = node.getNext();
			}
		}
	}

	/*
	 * This method to search martyrs in all locations in a district and display
	 * their data in a table view
	 */
	private void searchInDistrict(District dist, String name) {
		tMart.getItems().clear();
		stackLocation = new Stack();
		inOrderLocation(dist.getLocations().getRoot());
		while (!stackLocation.isEmpty()) {
			Location currLoc = (Location) ((BNode) stackLocation.pop()).getElement();
			if (currLoc.getDates() == null || currLoc.getDates().getRoot() == null)
				continue;
			stackMartyr = new Stack();
			inOrderDate(currLoc.getDates().getRoot());
			stackMartyr = reverse(stackMartyr);
			while (!stackMartyr.isEmpty()) {
				MartyrsDates currD = (MartyrsDates) ((BNode) stackMartyr.pop()).getElement();
				SNode node = currD.getMartyrs().getFront();
				while (node != null) {
					Martyr currMart = (Martyr) node.getElement();
					if (currMart.getName().contains(name))
						tMart.getItems().add(currMart);
					node = node.getNext();
				}
			}
		}
	}

	/*
	 * This method to search martyrs in all districts and get their data and display
	 * them in a table view
	 */
	private void searchAllData(String name) {
		tMart.getItems().clear();
		stack = new Stack();
		inOrder(district.getRoot());
		while (!stack.isEmpty()) {
			District currDis = (District) ((BNode) stack.pop()).getElement();
			if (currDis.getLocations() == null || currDis.getLocations().getRoot() == null)
				continue;
			stackLocation = new Stack();
			inOrderLocation(currDis.getLocations().getRoot());
			while (!stackLocation.isEmpty()) {
				Location currLoc = (Location) ((BNode) stackLocation.pop()).getElement();
				if (currLoc.getDates() == null || currLoc.getDates().getRoot() == null)
					continue;
				stackMartyr = new Stack();
				inOrderDate(currLoc.getDates().getRoot());
				stackMartyr = reverse(stackMartyr);
				while (!stackMartyr.isEmpty()) {
					MartyrsDates currD = (MartyrsDates) ((BNode) stackMartyr.pop()).getElement();
					SNode node = currD.getMartyrs().getFront();
					while (node != null) {
						Martyr currMart = (Martyr) node.getElement();
						if (currMart.getName().contains(name))
							tMart.getItems().add(currMart);
						node = node.getNext();
					}
				}
			}
		}
	}

	/*
	 * In this method we update a selected martyr from a combo box in a selected
	 * district and location, you can update each one of the data about the martyr
	 * not necessarily to update them all.
	 */
	public void updateMartyr() {
		pane.setLeft(null);
		cbMartyr = new ComboBox<Martyr>();
		now = LocalDate.now();
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		if (dist == null || dist.getLocations().getRoot() == null) {
			Label lblResult = new Label("No District or Location");
			pane.setCenter(lblResult);
			return;
		}
		Location loc;
		if (currLocation == null)
			loc = (Location) dist.getLocations().min();
		else
			loc = currLocation;
		MartyrsDates date;
		if (currDate == null)
			date = (MartyrsDates) loc.getDates().min();
		else
			date = (MartyrsDates) currDate.getElement();

		if (date == null) {
			Label lblResult = new Label("No data");
			pane.setCenter(lblResult);
			return;
		}

		cbMartyr.setItems(date.getList());
		cbMartyr.setMinWidth(200);

		Label lblResult = new Label();
		Button btUpdate = new Button("update");
		Button btClear = new Button("clear");
		HBox hbButtons = new HBox(10);
		hbButtons.getChildren().addAll(btClear, btUpdate);
		hbButtons.setAlignment(Pos.CENTER);

		Label lblTitle = new Label(dist.getName() + ", " + loc.getName());
		Label lblOldName = new Label("name");
		Label lblNewName = new Label("new name");
		TextField tfName = new TextField();
		Label lblNewAge = new Label("new age");
		TextField tfAge = new TextField();
		Label lblGender = new Label("Gender");
		RadioButton rdMale = new RadioButton("Male");
		RadioButton rdFemale = new RadioButton("Female");
		ToggleGroup tgGender = new ToggleGroup();
		rdMale.setToggleGroup(tgGender);
		rdFemale.setToggleGroup(tgGender);
		HBox hbGender = new HBox(10);
		hbGender.getChildren().addAll(rdMale, rdFemale);
		hbGender.setAlignment(Pos.CENTER);
		DatePicker pick = new DatePicker();
		Label lblDate = new Label("date");

		GridPane gPane = new GridPane();
		gPane.add(lblTitle, 1, 0);
		gPane.add(lblOldName, 0, 1);
		gPane.add(cbMartyr, 1, 1);
		gPane.add(lblNewName, 0, 2);
		gPane.add(tfName, 1, 2);
		gPane.add(lblNewAge, 0, 3);
		gPane.add(tfAge, 1, 3);
		gPane.add(lblGender, 0, 4);
		gPane.add(hbGender, 1, 4);
		gPane.add(lblDate, 0, 5);
		gPane.add(pick, 1, 5);
		gPane.add(hbButtons, 1, 6);
		gPane.add(lblResult, 1, 7);

		GridPane.setHalignment(lblTitle, HPos.CENTER);
		GridPane.setHalignment(lblResult, HPos.CENTER);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);
		pane.setCenter(gPane);

		btClear.setOnAction(e -> {
			lblResult.setText("");
			tfName.clear();
			tfAge.clear();
			pick.setValue(null);
			if (rdMale.isSelected() || rdFemale.isSelected())
				tgGender.getSelectedToggle().setSelected(false);
		});

		btUpdate.setOnAction(e -> {
			Martyr mart = cbMartyr.getValue();
			String name = tfName.getText();
			String strAge = tfAge.getText();
			LocalDate martDate = pick.getValue();

			/*
			 * Check if the user entered a new update for the martyr or kept everything
			 * empty
			 */
			if (martDate == null && name.isBlank() && strAge.isBlank()
					&& (!rdMale.isSelected() && !rdFemale.isSelected())) {
				lblResult.setText("All fields are empty");
				lblResult.setTextFill(Color.RED);
				return;
			}

			if (!name.isBlank()) {
				if (!availabelString(name)) {
					lblResult.setText("Invalid name");
					lblResult.setTextFill(Color.RED);
					return;
				}
			} else {
				name = mart.getName();
			}

			char gender = 'M';
			if (rdMale.isSelected())
				gender = 'M';
			else if (rdFemale.isSelected())
				gender = 'F';
			else
				gender = mart.getGender();

			byte age;

			if (strAge.isBlank())
				age = mart.getAge();
			else {

				try {
					age = Byte.parseByte(strAge);
					/* To check if the age is valid or not */
					if (age < 0 || age > 120)
						throw new NumberFormatException();
				} catch (NumberFormatException ex) {
					lblResult.setText("Invalid age");
					lblResult.setTextFill(Color.RED);
					return;
				}
			}

			MartyrsDates mdMart;

			if (martDate == null)
				mdMart = date;
			else {
				String strMartDate = reformatDate(martDate);
				String strNow = reformatDate(now);

				mdMart = new MartyrsDates(strMartDate);
				MartyrsDates mdNow = new MartyrsDates(strNow);

				if (mdMart.compareTo(mdNow) > 0) {
					lblResult.setText("Invalid date");
					lblResult.setTextFill(Color.RED);
					return;
				}
			}

			/*
			 * A warning for the user before updating the martyr
			 */
			Label lblSure = new Label("Are you sure you want to update?");
			Button btNo = new Button("No");
			Button btYes = new Button("Yes");
			HBox hbNodes = new HBox(10);
			hbNodes.getChildren().addAll(btNo, btYes);
			hbNodes.setAlignment(Pos.CENTER);

			Stage stage = new Stage();
			GridPane gPane1 = new GridPane();
			gPane1.add(lblSure, 0, 0);
			gPane1.add(hbNodes, 0, 1);
			gPane1.setHgap(10);
			gPane1.setVgap(10);
			gPane1.setAlignment(Pos.CENTER);
			Scene scene = new Scene(gPane1, 300, 200);
			stage.setScene(scene);
			stage.setTitle("Warning");
			stage.show();

			btNo.setOnAction(e1 -> {
				lblResult.setText("");
				tfName.clear();
				tfAge.clear();
				pick.setValue(null);
				if (rdMale.isSelected() || rdFemale.isSelected())
					tgGender.getSelectedToggle().setSelected(false);
				stage.close();
			});
			Martyr newMart = new Martyr(name, age, gender);
			btYes.setOnAction(e1 -> {
				if (loc.getDates().contains(mdMart)) {
					mdMart.getMartyrs().remove(mart);
					cbMartyr.getItems().remove(mart);
					cbMartyr.setItems(date.getList());
					cbMartyr.getItems().add(newMart);
					mdMart.addMartyr(newMart);
				} else {
					mdMart.addMartyr(newMart);
					loc.getDates().insert(mdMart);
				}
				lblResult.setText("Martyr updated successfully");
				lblResult.setTextFill(Color.WHITE);
				stage.close();
				lblResult.setText("");
				tfName.clear();
				tfAge.clear();
				pick.setValue(null);
				if (rdMale.isSelected() || rdFemale.isSelected())
					tgGender.getSelectedToggle().setSelected(false);
				stage.close();
			});
		});
	}

	/* To delete a selected martyr from a combo box */
	public void deleteMartyr() {
		pane.setLeft(null);
		cbMartyr = new ComboBox<Martyr>();
		cbMartyr.setMinWidth(200);
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		if (dist == null || dist.getLocations().getRoot() == null) {
			Label lblResult = new Label("No District or Location");
			pane.setCenter(lblResult);
			return;
		}
		Location loc;
		if (currLocation == null)
			loc = (Location) dist.getLocations().min();
		else
			loc = currLocation;
		MartyrsDates date;
		if (currDate == null)
			date = (MartyrsDates) loc.getDates().min();
		else
			date = (MartyrsDates) currDate.getElement();

		if (date == null) {
			Label lblResult = new Label("No data");
			pane.setCenter(lblResult);
			return;
		}

		cbMartyr.setItems(date.getList());

		Label lblResult = new Label();
		Button btDelete = new Button("delete");
		HBox hbDelete = new HBox(10);
		hbDelete.getChildren().addAll(cbMartyr, btDelete);
		hbDelete.setAlignment(Pos.CENTER);
		Label lblTitle = new Label(dist.getName() + ", " + loc.getName());
		VBox vbNodes = new VBox(10);
		vbNodes.getChildren().addAll(lblTitle, hbDelete, lblResult);
		vbNodes.setAlignment(Pos.CENTER);
		pane.setCenter(vbNodes);

		btDelete.setOnAction(e -> {
			/*
			 * Check if there is a chosen martyr from the combo box, thus it will be deleted
			 * from the linked list
			 */
			Martyr mart = cbMartyr.getValue();
			if (mart == null) {
				lblResult.setText("Select a Martyr");
				lblResult.setTextFill(Color.RED);
				return;
			}

			Label lblWarning = new Label("Are you sure you want to delete this martyr?");
			Button btYes = new Button("Yes");
			Button btNo = new Button("No");
			HBox hbButtons = new HBox(10);

			hbButtons.getChildren().addAll(btNo, btYes);
			hbButtons.setAlignment(Pos.CENTER);

			GridPane gPane = new GridPane();
			gPane.add(lblWarning, 0, 0);
			gPane.add(hbButtons, 0, 1);

			gPane.setHgap(10);
			gPane.setVgap(10);
			gPane.setAlignment(Pos.CENTER);

			Scene scene = new Scene(gPane, 300, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Warning");
			stage.show();

			/* A warning scene to see if the user is sure to delete the district */
			btNo.setOnAction(e1 -> {
				lblResult.setText("");
				stage.close();
			});

			btYes.setOnAction(e1 -> {
				date.getMartyrs().remove(mart);
				cbMartyr.getItems().remove(mart);
				lblResult.setText("Martyr is Deleted");
				lblResult.setTextFill(Color.WHITE);
				stage.close();
			});

		});
	}

	/*
	 * This method to add a martyr in a selected district and a selected location
	 * and it checks if the martyr already exists
	 */
	public void addMartyr() {
		pane.setLeft(null);
		now = LocalDate.now();
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		if (dist == null || dist.getLocations().getRoot() == null) {
			Label lblResult = new Label("No District or Location");
			pane.setCenter(lblResult);
			return;
		}
		Location loc;
		if (currLocation == null)
			loc = (Location) dist.getLocations().min();
		else
			loc = currLocation;

		Label lblResult = new Label();
		Label lblName = new Label("Name");
		TextField tfName = new TextField();
		Label lblAge = new Label("Age");
		TextField tfAge = new TextField();
		Label lblGender = new Label("Gender");
		RadioButton rdMale = new RadioButton("Male");
		RadioButton rdFemale = new RadioButton("Female");
		ToggleGroup tgGender = new ToggleGroup();
		rdMale.setToggleGroup(tgGender);
		rdFemale.setToggleGroup(tgGender);
		HBox hbGender = new HBox(10);
		hbGender.getChildren().addAll(rdMale, rdFemale);
		hbGender.setAlignment(Pos.CENTER);
		Label lblDate = new Label("Martyr date");
		DatePicker pick = new DatePicker();
		Button btAdd = new Button("add");
		Button btClear = new Button("clear");
		HBox hbButtons = new HBox(10);
		hbButtons.getChildren().addAll(btClear, btAdd);
		hbButtons.setAlignment(Pos.CENTER);
		Label lblTitle = new Label(dist.getName() + ", " + loc.getName());

		GridPane gPane = new GridPane();

		gPane.add(lblTitle, 1, 0);
		gPane.add(lblName, 0, 1);
		gPane.add(tfName, 1, 1);
		gPane.add(lblAge, 0, 2);
		gPane.add(tfAge, 1, 2);
		gPane.add(lblGender, 0, 3);
		gPane.add(hbGender, 1, 3);
		gPane.add(lblDate, 0, 4);
		gPane.add(pick, 1, 4);
		gPane.add(hbButtons, 1, 5);
		gPane.add(lblResult, 1, 6);
		pane.setCenter(gPane);

		gPane.setAlignment(Pos.CENTER);
		gPane.setHgap(10);
		gPane.setVgap(10);

		GridPane.setHalignment(lblTitle, HPos.CENTER);
		GridPane.setHalignment(lblResult, HPos.CENTER);

		btClear.setOnAction(e -> {

			lblResult.setText("");
			tfName.clear();
			tfAge.clear();
			pick.setValue(null);
			if (rdMale.isSelected() || rdFemale.isSelected())
				tgGender.getSelectedToggle().setSelected(false);

		});

		btAdd.setOnAction(e -> {
			String name = tfName.getText();
			String strAge = tfAge.getText();
			LocalDate martDate = pick.getValue();

			if (martDate == null || name.isBlank() || strAge.isBlank()
					|| (!rdMale.isSelected() && !rdFemale.isSelected())) {
				lblResult.setText("Fill All fields");
				lblResult.setTextFill(Color.RED);
				return;
			}

			if (!availabelString(name)) {
				lblResult.setText("Invalid name");
				lblResult.setTextFill(Color.RED);
				return;
			}

			char gender = 'M';
			if (rdMale.isSelected())
				gender = 'M';
			else if (rdFemale.isSelected())
				gender = 'F';

			byte age;
			try {
				age = Byte.parseByte(strAge);
				/* To check if the age is valid or not */
				if (age < 0 || age > 120)
					throw new NumberFormatException();
			} catch (NumberFormatException ex) {
				lblResult.setText("Invalid age");
				lblResult.setTextFill(Color.RED);
				return;
			}

			String strMartDate = reformatDate(martDate);
			String strNow = reformatDate(now);

			MartyrsDates mdMart = new MartyrsDates(strMartDate);
			MartyrsDates mdNow = new MartyrsDates(strNow);

			if (mdMart.compareTo(mdNow) > 0) {
				lblResult.setText("Invalid date");
				lblResult.setTextFill(Color.RED);
				return;
			}

			Martyr mart = new Martyr(name, age, gender);
			BNode node = loc.checkDates(mdMart);
			if (node == null) {
				mdMart.addMartyr(mart);
				loc.getDates().insert(mdMart);
				lblResult.setText("Martyr added successfully");
				lblResult.setTextFill(Color.WHITE);
				return;
			}

			MartyrsDates exists = ((MartyrsDates) node.getElement());
			if (exists.checkMartyr(mart) == null) {
				exists.addMartyr(mart);
				lblResult.setText("Martyr added successfully");
				lblResult.setTextFill(Color.WHITE);
				return;
			} else {
				lblResult.setText("Martyr already exists");
				lblResult.setTextFill(Color.RED);
			}
		});

	}

	public String reformatDate(LocalDate date) {
		String str = String.valueOf(date);
		String[] split = str.split("-");
		str = split[1] + "/" + split[2] + "/" + split[0];
		return str;
	}

	/*
	 * This method to go through the MartyrsDates tree in order traversal by pushing
	 * the elements of the tree to a stack until we reach the minimum value and show
	 * it on the screen, if we press next we go to the next element on the stack if
	 * we press previous we pop element from the prev stack
	 */
	public void navigateMartyrs() {
		stack = new Stack();
		prev = new Stack();
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		if (dist == null || dist.getLocations().getRoot() == null) {
			Label lblResult = new Label("No content");
			pane.setCenter(lblResult);
			return;
		}
		Location loc;
		if (currLocation == null)
			loc = (Location) dist.getLocations().min();
		else
			loc = currLocation;

		inOrder(loc.getDates().getRoot());
		stack = reverse(stack);

		currDate = (BNode) stack.pop();

		if (currDate == null) {
			Label lblResult = new Label("No content");
			pane.setCenter(lblResult);
			return;
		}

		/* Table View to present the data */
		TableView<MartyrsDates> table = new TableView<MartyrsDates>();
		TableColumn<MartyrsDates, String> tDate = new TableColumn<MartyrsDates, String>("date");
		TableColumn<MartyrsDates, Integer> tAvg = new TableColumn<MartyrsDates, Integer>("average age");
		TableColumn<MartyrsDates, Martyr> tYoung = new TableColumn<MartyrsDates, Martyr>("youngest martyr");
		TableColumn<MartyrsDates, Martyr> tOld = new TableColumn<MartyrsDates, Martyr>("oldest martyr");
		tDate.setCellValueFactory(new PropertyValueFactory<MartyrsDates, String>("date"));
		tAvg.setCellValueFactory(new PropertyValueFactory<MartyrsDates, Integer>("average"));
		tYoung.setCellValueFactory(new PropertyValueFactory<MartyrsDates, Martyr>("young"));
		tOld.setCellValueFactory(new PropertyValueFactory<MartyrsDates, Martyr>("old"));
		table.getColumns().add(tDate);
		table.getColumns().add(tAvg);
		table.getColumns().add(tYoung);
		table.getColumns().add(tOld);
		table.getItems().add((MartyrsDates) currDate.getElement());
		table.setMaxHeight(80);
		table.setMaxWidth(700);
		tDate.setPrefWidth(150);
		tAvg.setPrefWidth(150);
		tYoung.setPrefWidth(200);
		tOld.setPrefWidth(200);

		Button btNext = new Button("next");
		Button btPrev = new Button("prev");
		HBox hbButtons = new HBox(10);
		VBox vbNodes = new VBox(10);
		Label lblTitle = new Label(dist.getName() + ", " + loc.getName());
		hbButtons.getChildren().addAll(btPrev, btNext);
		hbButtons.setAlignment(Pos.CENTER);
		tMart = ((MartyrsDates) currDate.getElement()).getMartyrsList();

		vbNodes.getChildren().addAll(lblTitle, table, hbButtons);
		vbNodes.setAlignment(Pos.CENTER);
		pane.setCenter(vbNodes);

		pane.setLeft(tMart);
		tMart.setPrefHeight(100);
		tMart.setMaxWidth(550);

		BorderPane.setAlignment(tMart, Pos.CENTER);

		/* Button next to go to the next value in the queue */
		btNext.setOnAction(e -> {
			if (stack.peek() != null) {
				prev.push(currDate);
				currDate = (BNode) (stack.pop());
				table.getItems().clear();
				table.getItems().add((MartyrsDates) currDate.getElement());
				tMart = ((MartyrsDates) currDate.getElement()).getMartyrsList();
				pane.setLeft(tMart);
				tMart.setPrefHeight(100);
				tMart.setMaxWidth(550);
				BorderPane.setAlignment(tMart, Pos.CENTER);
				return;
			}
		});

		/* Button prev to go back to the next element in the prev stack */
		btPrev.setOnAction(e -> {
			if (prev.peek() != null) {
				stack.push(currDate);
				currDate = (BNode) prev.pop();
				table.getItems().clear();
				table.getItems().add((MartyrsDates) currDate.getElement());
				tMart = ((MartyrsDates) currDate.getElement()).getMartyrsList();
				pane.setLeft(tMart);
				tMart.setPrefHeight(100);
				tMart.setMaxWidth(550);
				BorderPane.setAlignment(tMart, Pos.CENTER);
				return;
			}
		});

	}

	/*
	 * This method to go through the location tree level by level traversal by
	 * pushing the elements of the tree to a stack until we reach the minimum value
	 * and show it on the screen, if we press next we go to the next element on the
	 * stack if we press previous we pop element from the prev stack
	 */
	public void navigateLocation() {
		pane.setLeft(null);
		stack = new Stack();
		prev = new Stack();
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		if (dist == null || dist.getLocations().getRoot() == null) {
			Label lblResult = new Label("No content");
			pane.setCenter(lblResult);
			return;
		}

		levelByLevel(dist.getLocations().getRoot());
		stack = reverse(stack);
		currLocation = (Location) stack.pop();
		// currLocation.findMax(currLocation.getDates().getRoot());
		/* Table View to present the data */
		TableView<Location> table = new TableView<Location>();
		TableColumn<Location, String> tName = new TableColumn<Location, String>("name");
		TableColumn<Location, MartyrsDates> tEarly = new TableColumn<Location, MartyrsDates>("latest");
		TableColumn<Location, MartyrsDates> tLate = new TableColumn<Location, MartyrsDates>("earliest");
		TableColumn<Location, MartyrsDates> tMax = new TableColumn<Location, MartyrsDates>("max date with martyrs");
		tName.setCellValueFactory(new PropertyValueFactory<Location, String>("name"));
		tEarly.setCellValueFactory(new PropertyValueFactory<Location, MartyrsDates>("earliest"));
		tLate.setCellValueFactory(new PropertyValueFactory<Location, MartyrsDates>("latest"));
		tMax.setCellValueFactory(new PropertyValueFactory<Location, MartyrsDates>("max"));
		table.getColumns().add(tName);
		table.getColumns().add(tEarly);
		table.getColumns().add(tLate);
		table.getColumns().add(tMax);
		table.getItems().add(currLocation);
		table.setMaxHeight(80);
		table.setMaxWidth(600);
		tName.setPrefWidth(150);
		tEarly.setPrefWidth(150);
		tLate.setPrefWidth(150);
		tMax.setPrefWidth(150);

		Button btNext = new Button("next");
		Button btPrev = new Button("prev");
		Button btLoad = new Button("load");
		HBox hbButtons = new HBox(10);
		VBox vbNodes = new VBox(10);
		Label lblDist = new Label(dist.getName());
		hbButtons.getChildren().addAll(btPrev, btNext);
		hbButtons.setAlignment(Pos.CENTER);
		vbNodes.getChildren().addAll(lblDist, table, hbButtons, btLoad);
		vbNodes.setAlignment(Pos.CENTER);
		pane.setCenter(vbNodes);

		GridPane.setHalignment(lblDist, HPos.CENTER);

		tDate = new TableView<MartyrsDates>();
		TableColumn<MartyrsDates, String> date = new TableColumn<MartyrsDates, String>("date");
		TableColumn<MartyrsDates, Integer> total = new TableColumn<MartyrsDates, Integer>("total");
		date.setCellValueFactory(new PropertyValueFactory<MartyrsDates, String>("date"));
		total.setCellValueFactory(new PropertyValueFactory<MartyrsDates, Integer>("total"));
		tDate.getColumns().add(date);
		tDate.getColumns().add(total);
		tDate.setMaxHeight(500);
		tDate.setMaxWidth(300);
		date.setPrefWidth(150);
		total.setPrefWidth(150);

		/* Button next to go to the next value in the queue */
		btNext.setOnAction(e -> {
			if (stack.peek() != null) {
				prev.push(currLocation);
				currLocation = (Location) stack.pop();
				table.getItems().clear();
				table.getItems().add(currLocation);
				return;
			}
		});

		/* Button prev to go back to the next element in the prev stack */
		btPrev.setOnAction(e -> {
			if (prev.peek() != null) {
				stack.push(currLocation);
				currLocation = (Location) prev.pop();
				table.getItems().clear();
				table.getItems().add(currLocation);
				return;
			}
		});

		/* To load a new stage with the dates and the number of martyrs */
		btLoad.setOnAction(e -> {
			tDate.getItems().clear();
			BorderPane bpane = new BorderPane();
			BNode temp = currLocation.getDates().getRoot();
			tableValuesMartyrsDates(temp);
			bpane.setCenter(tDate);
			String css = getClass().getResource("newStyle.css").toExternalForm();
			bpane.getStylesheets().add(css);
			Stage stage = new Stage();
			Scene scene = new Scene(bpane, 600, 600);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle(currLocation.getName());
			stage.show();
		});
	}

	/* To add the values to the table view */
	private void tableValuesMartyrsDates(BNode root) {
		if (root != null) {
			tableValuesMartyrsDates(root.getLeft());
			MartyrsDates temp = (MartyrsDates) root.getElement();
			tDate.getItems().add(temp);
			tableValuesMartyrsDates(root.getRight());
		}
	}

	/* This method to go through the tree level by level */
	private void levelByLevel(BNode root) {
		if (root == null)
			return;
		Queue temp = new Queue();
		temp.enQueue(root);
		while (!temp.isEmpty()) {
			BNode curr = (BNode) temp.front();
			stack.push(curr.getElement());
			if (curr.getLeft() != null)
				temp.enQueue(curr.getLeft());
			if (curr.getRight() != null)
				temp.enQueue(curr.getRight());
			temp.deQueue();
		}
	}

	/*
	 * This method to update a selected location from the combo box and change it on
	 * the tree, it checks if the current dist is chosen if not it use the first one
	 */
	public void updateLocation() {
		pane.setLeft(null);
		Label lblUpdate = new Label("Select the location to update");
		Label lblResult = new Label();
		Label lblLocation = new Label("New Location");
		Label lblDistrict = new Label();
		TextField tfLocation = new TextField();
		Button btUpdate = new Button("update");
		Button btClear = new Button("clear");
		HBox hbButtons = new HBox(10);
		GridPane gPane = new GridPane();
		FXCollections.sort(locList);

		hbButtons.getChildren().addAll(btClear, btUpdate);
		hbButtons.setAlignment(Pos.CENTER);

		gPane.add(lblDistrict, 1, 0);
		gPane.add(lblUpdate, 1, 1);
		gPane.add(cbLocation, 1, 2);
		gPane.add(lblLocation, 0, 3);
		gPane.add(tfLocation, 1, 3);
		gPane.add(hbButtons, 1, 4);
		gPane.add(lblResult, 1, 5);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);

		GridPane.setHalignment(cbLocation, HPos.CENTER);
		GridPane.setHalignment(hbButtons, HPos.CENTER);
		GridPane.setHalignment(lblResult, HPos.CENTER);
		GridPane.setHalignment(lblUpdate, HPos.CENTER);
		GridPane.setHalignment(lblDistrict, HPos.CENTER);

		pane.setCenter(gPane);

		/* Check if there is districts in the tree */
		if (district.getRoot() == null) {
			lblResult.setText("No Districts");
			lblResult.setTextFill(Color.RED);
			btUpdate.setDisable(true);
			return;
		}

		btUpdate.setDisable(false);

		/*
		 * To get the current district if there is nothing selected it will set it as
		 * the first one in the tree
		 */
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		locList.clear();
		locationsToList(dist.getLocations().getRoot());
		FXCollections.sort(locList);
		lblDistrict.setText("District: " + dist.getName());

		btClear.setOnAction(e -> {
			tfLocation.clear();
			lblResult.setText("");
			return;
		});

		btUpdate.setOnAction(e -> {
			Location loc = cbLocation.getValue();
			String locName = tfLocation.getText();
			tfLocation.clear();

			/* To check if the user selected any district */
			if (loc == null) {
				lblResult.setText("Select a Location");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* To check if the user entered an empty field */
			if (locName.isBlank()) {
				lblResult.setText("Please Fill the Field");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* To check if the user entered unavailable string */
			if (!availabelString(locName)) {
				lblResult.setText("Invalid Input");
				lblResult.setTextFill(Color.RED);
				return;
			}

			if (dist.getLocations().contains(new Location(locName))) {
				lblResult.setText("Location already exists");
				lblResult.setTextFill(Color.RED);
				return;
			}

			Label lblWarning = new Label("Are you sure you want to update this district?");
			Button btYes = new Button("Yes");
			Button btNo = new Button("No");
			HBox hbNodes = new HBox(10);

			hbNodes.getChildren().addAll(btNo, btYes);
			hbNodes.setAlignment(Pos.CENTER);

			GridPane gPane2 = new GridPane();
			gPane2.add(lblWarning, 0, 0);
			gPane2.add(hbNodes, 0, 1);

			gPane2.setHgap(10);
			gPane2.setVgap(10);
			gPane2.setAlignment(Pos.CENTER);

			Scene scene = new Scene(gPane2, 300, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Warning");
			stage.show();

			/* A warning scene to see if the user is sure to update the district */
			btNo.setOnAction(e1 -> {
				lblResult.setText("");
				stage.show();
			});

			btYes.setOnAction(e1 -> {
				dist.getLocations().remove(loc);
				locList.remove(loc);
				loc.setName(locName);
				dist.getLocations().insert(loc);
				locList.add(loc);
				FXCollections.sort(locList);
				lblResult.setText("Location updated successfully");
				lblResult.setTextFill(Color.WHITE);
				stage.close();
			});
		});

	}

	/*
	 * This method to delete a location from the current district, if there is no
	 * district selected it will get the first one in the tree, and shows the
	 * locations of this district in a combo box so the user choose what to delete.
	 */
	public void deleteLocation() {
		pane.setLeft(null);
		Label lblDelete = new Label("Select the location to delete");
		Label lblResult = new Label();
		Label lblDistrict = new Label();
		Button btDelete = new Button("delete");
		HBox hbNodes = new HBox(10);
		VBox vb = new VBox(10);

		hbNodes.getChildren().addAll(cbLocation, btDelete);
		vb.getChildren().addAll(lblDistrict, lblDelete, hbNodes, lblResult);
		vb.setAlignment(Pos.CENTER);
		hbNodes.setAlignment(Pos.CENTER);

		GridPane.setHalignment(lblDelete, HPos.CENTER);
		GridPane.setHalignment(lblDistrict, HPos.CENTER);

		pane.setCenter(vb);

		/* Check if there is districts in the tree */
		if (district.getRoot() == null) {
			lblResult.setText("No Districts");
			lblResult.setTextFill(Color.RED);
			btDelete.setDisable(true);
			return;
		}

		btDelete.setDisable(false);

		/*
		 * To get the current district if there is nothing selected it will set it as
		 * the first one in the tree
		 */
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();
		locList.clear();
		locationsToList(dist.getLocations().getRoot());
		FXCollections.sort(locList);
		lblDistrict.setText("District: " + dist.getName());

		btDelete.setOnAction(e -> {
			/* To see if the user selected a value */
			Location loc = cbLocation.getValue();
			if (loc == null) {
				lblResult.setText("Select a Location");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* Warning stage to be sure if the user want to delete the location or not */
			Label lblWarning = new Label("Are you sure you want to delete this location?");
			Button btYes = new Button("Yes");
			Button btNo = new Button("No");
			HBox hbButtons = new HBox(10);

			hbButtons.getChildren().addAll(btNo, btYes);
			hbButtons.setAlignment(Pos.CENTER);

			GridPane gPane = new GridPane();
			gPane.add(lblWarning, 0, 0);
			gPane.add(hbButtons, 0, 1);

			gPane.setHgap(10);
			gPane.setVgap(10);
			gPane.setAlignment(Pos.CENTER);

			Scene scene = new Scene(gPane, 300, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Warning");
			stage.show();

			/* A warning scene to see if the user is sure to delete the location */
			btNo.setOnAction(e1 -> {
				lblResult.setText("");
				stage.close();
			});

			btYes.setOnAction(e1 -> {
				dist.getLocations().remove(loc);
				locList.remove(loc);
				stage.close();
				lblResult.setText("Location deleted successfully");
				lblResult.setTextFill(Color.WHITE);
			});
		});
	}

	/*
	 * This method to add a new Location to the current district, it checks if there
	 * is district chosen if it is it add to it else it add to the first district in
	 * the tree
	 */
	public void addLocation() {
		pane.setLeft(null);
		GridPane gPane = new GridPane();
		Label lblResult = new Label();
		Label lblName = new Label("Location Name");
		TextField tfName = new TextField();
		Label lblDistrict = new Label();
		Button btAdd = new Button("add");
		Button btClear = new Button("clear");
		HBox hbButtons = new HBox(10);
		hbButtons.getChildren().addAll(btClear, btAdd);
		hbButtons.setAlignment(Pos.CENTER);
		gPane.add(lblDistrict, 1, 0);
		gPane.add(lblName, 0, 1);
		gPane.add(tfName, 1, 1);
		gPane.add(hbButtons, 1, 2);
		gPane.add(lblResult, 1, 3);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);
		pane.setCenter(gPane);

		GridPane.setHalignment(hbButtons, HPos.CENTER);
		GridPane.setHalignment(lblResult, HPos.CENTER);
		GridPane.setHalignment(lblDistrict, HPos.CENTER);

		/* Check if there is districts */
		if (district.getRoot() == null) {
			lblResult.setText("No Districts");
			lblResult.setTextFill(Color.RED);
			btAdd.setDisable(true);
			return;
		}

		btAdd.setDisable(false);
		/* Check if there is a selected district */
		District dist;
		if (curr == null)
			dist = (District) district.min();
		else
			dist = (District) curr.getElement();

		lblDistrict.setText("District: " + dist.getName());

		btClear.setOnAction(e -> {
			tfName.clear();
			lblResult.setText("");
			return;
		});

		btAdd.setOnAction(e -> {
			String locName = tfName.getText();
			/* Check if the entered name is empty or not */
			if (locName.isBlank()) {
				lblResult.setText("Please Fill the Field");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* Check if the String is available */
			if (!availabelString(locName)) {
				lblResult.setText("Invalid Input");
				lblResult.setTextFill(Color.RED);
				return;
			}

			Location loc = new Location(locName);

			/* Check if the location already exsists */
			if (dist.getLocations().contains(loc)) {
				lblResult.setText("Location already exists");
				lblResult.setTextFill(Color.RED);
				return;
			}

			dist.getLocations().insert(loc);
			lblResult.setText("Location added successfully");
			lblResult.setTextFill(Color.WHITE);
		});
	}

	/*
	 * This method to add the locations to the observable list to show it in the
	 * combo box
	 */
	private void locationsToList(BNode root) {
		if (root != null) {
			locationsToList(root.getLeft());
			locList.add((Location) root.getElement());
			locationsToList(root.getRight());
		}
	}

	/*
	 * This method let the user choose a district and print all the locations and
	 * the number of martyrs in it
	 */
	public void printFile() {
		pane.setLeft(null);
		Label lblResult = new Label();
		Button btPrint = new Button("print");
		HBox hbNodes = new HBox(10);
		VBox vbPane = new VBox(10);
		hbNodes.getChildren().addAll(cbDistrict, btPrint);
		hbNodes.setAlignment(Pos.CENTER);
		vbPane.getChildren().addAll(hbNodes, lblResult);
		vbPane.setAlignment(Pos.CENTER);
		pane.setCenter(vbPane);
		FXCollections.sort(distList);

		btPrint.setOnAction(e -> {
			District dist = cbDistrict.getValue();
			if (dist != null) {
				stack = new Stack();
				inOrder(dist.getLocations().getRoot());
				stack = reverse(stack);

				try (PrintWriter print = new PrintWriter(dist.getName() + ".txt")) {
					while (stack.peek() != null) {
						Location loc = (Location) ((BNode) stack.pop()).getElement();
						print.print(loc.getName() + " ");
						print.println(loc.getTotal());
					}
					lblResult.setText("District printed on file");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/*
	 * This method to go through the district tree in order traversal by pushing the
	 * elements of the tree to a stack until we reach the minimum value and show it
	 * on the screen, if we press next we go to the next element on the stack if we
	 * press previous we pop element from the prev stack
	 */
	public void navigateDistrict() {
		pane.setLeft(null);
		if (district.getRoot() == null) {
			Label lblResult = new Label("No Districts");
			pane.setCenter(lblResult);
			return;
		}
		stack = new Stack();
		prev = new Stack();
		curr = district.getRoot();
		inOrder(curr);
		stack = reverse(stack);
		curr = (BNode) stack.pop();

		/* Table View to present the data */
		TableView<District> table = new TableView<District>();
		TableColumn<District, String> tName = new TableColumn<District, String>("name");
		TableColumn<District, Integer> tTotal = new TableColumn<District, Integer>("total martyrs");
		tName.setCellValueFactory(new PropertyValueFactory<District, String>("name"));
		tTotal.setCellValueFactory(new PropertyValueFactory<District, Integer>("total"));
		table.getColumns().add(tName);
		table.getColumns().add(tTotal);
		table.getItems().add((District) curr.getElement());
		table.setMaxHeight(80);
		table.setMaxWidth(300);
		tName.setPrefWidth(150);
		tTotal.setPrefWidth(150);

		Button btNext = new Button("next");
		Button btPrev = new Button("prev");
		Button btLoad = new Button("load");
		HBox hbButtons = new HBox(10);
		VBox vbNodes = new VBox(10);
		hbButtons.getChildren().addAll(btPrev, btNext);
		hbButtons.setAlignment(Pos.CENTER);
		vbNodes.getChildren().addAll(table, hbButtons, btLoad);
		vbNodes.setAlignment(Pos.CENTER);
		pane.setCenter(vbNodes);

		tLocation = new TableView<Location>();
		TableColumn<Location, String> tLocationName = new TableColumn<Location, String>("name");
		TableColumn<Location, Integer> tLocationTotal = new TableColumn<Location, Integer>("total martyrs");
		tLocationName.setCellValueFactory(new PropertyValueFactory<Location, String>("name"));
		tLocationTotal.setCellValueFactory(new PropertyValueFactory<Location, Integer>("total"));
		tLocation.getColumns().add(tLocationName);
		tLocation.getColumns().add(tLocationTotal);
		tLocation.setMaxHeight(500);
		tLocation.setMaxWidth(300);
		tLocationName.setPrefWidth(150);
		tLocationTotal.setPrefWidth(150);

		/* Button next to go to the next value in the stack */
		btNext.setOnAction(e -> {
			if (stack.peek() != null) {
				prev.push(curr);
				curr = (BNode) stack.pop();
				table.getItems().clear();
				table.getItems().add((District) curr.getElement());
				return;
			}
		});

		/* Button prev to go back to the next element in the prev stack */
		btPrev.setOnAction(e -> {
			if (prev.peek() != null) {
				stack.push(curr);
				curr = (BNode) prev.pop();
				table.getItems().clear();
				table.getItems().add((District) curr.getElement());
				return;
			}
		});

		/*
		 * This button will open a new Stage that contains a table view that has the
		 * locations of the current district
		 */
		btLoad.setOnAction(e -> {
			tLocation.getItems().clear();
			BorderPane bpane = new BorderPane();
			BNode temp = ((District) curr.getElement()).getLocations().getRoot();
			tableValues(temp);
			bpane.setCenter(tLocation);
			String css = getClass().getResource("newStyle.css").toExternalForm();
			bpane.getStylesheets().add(css);
			Stage stage = new Stage();
			Scene scene = new Scene(bpane, 600, 600);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle(((District) curr.getElement()).getName());
			stage.show();
		});
	}

	/* To add the values to the table view */
	private void tableValues(BNode root) {
		if (root != null) {
			tableValues(root.getLeft());
			Location loc = (Location) root.getElement();
			tLocation.getItems().add(loc);
			tableValues(root.getRight());
		}
	}

	/* A method to reverse the stack */
	private Stack reverse(Stack stack) {
		Stack res = new Stack();
		while (stack.peek() != null)
			res.push(stack.pop());
		return res;
	}

	/* A method to add the values of the tree to the stack */
	private void inOrder(BNode root) {
		if (root != null) {
			inOrder(root.getLeft());
			stack.push(root);
			inOrder(root.getRight());
		}
	}

	/* A method to add the values of the tree to the stack */
	private void inOrderLocation(BNode root) {
		if (root != null) {
			inOrderLocation(root.getLeft());
			stackLocation.push(root);
			inOrderLocation(root.getRight());
		}
	}

	/*
	 * This method to update a selected district from the combo box and change it on
	 * the tree
	 */
	public void updateDistrict() {
		pane.setLeft(null);
		Label lblUpdate = new Label("Select the district to update");
		Label lblResult = new Label();
		Label lblDistrict = new Label("New District");
		TextField tfDistrict = new TextField();
		Button btUpdate = new Button("update");
		Button btClear = new Button("clear");
		GridPane gPane = new GridPane();
		HBox hbNodes = new HBox(10);
		FXCollections.sort(distList);

		hbNodes.getChildren().addAll(btClear, btUpdate);
		hbNodes.setAlignment(Pos.CENTER);

		gPane.add(lblUpdate, 1, 0);
		gPane.add(cbDistrict, 1, 1);
		gPane.add(lblDistrict, 0, 2);
		gPane.add(tfDistrict, 1, 2);
		gPane.add(hbNodes, 1, 3);
		gPane.add(lblResult, 1, 4);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);

		GridPane.setHalignment(cbDistrict, HPos.CENTER);
		GridPane.setHalignment(hbNodes, HPos.CENTER);
		GridPane.setHalignment(lblResult, HPos.CENTER);
		GridPane.setHalignment(lblUpdate, HPos.CENTER);

		pane.setCenter(gPane);

		btClear.setOnAction(e -> {
			tfDistrict.clear();
			lblResult.setText("");
			return;
		});

		btUpdate.setOnAction(e -> {
			District dist = cbDistrict.getValue();
			String distName = tfDistrict.getText();
			tfDistrict.clear();

			/* To check if the user selected any district */
			if (dist == null) {
				lblResult.setText("Select a District");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* To check if the user entered an empty field */
			if (distName.isBlank()) {
				lblResult.setText("Please Fill the Field");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* To check if the user entered unavailable string */
			if (!availabelString(distName)) {
				lblResult.setText("Invalid Input");
				lblResult.setTextFill(Color.RED);
				return;
			}

			District temp = new District(distName);
			if (district.contains(temp)) {
				lblResult.setText("District already exists");
				lblResult.setTextFill(Color.RED);
				return;
			}

			Label lblWarning = new Label("Are you sure you want to update this district?");
			Button btYes = new Button("Yes");
			Button btNo = new Button("No");
			HBox hbButtons = new HBox(10);

			hbButtons.getChildren().addAll(btNo, btYes);
			hbButtons.setAlignment(Pos.CENTER);

			GridPane gPane2 = new GridPane();
			gPane2.add(lblWarning, 0, 0);
			gPane2.add(hbButtons, 0, 1);

			gPane2.setHgap(10);
			gPane2.setVgap(10);
			gPane2.setAlignment(Pos.CENTER);

			Scene scene = new Scene(gPane2, 300, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Warning");
			stage.show();

			/* A warning scene to see if the user is sure to update the district */
			btNo.setOnAction(e1 -> {
				lblResult.setText("");
				stage.show();
			});

			btYes.setOnAction(e1 -> {
				district.remove(dist);
				distList.remove(dist);
				dist.setName(distName);
				district.insert(dist);
				distList.add(dist);
				FXCollections.sort(distList);
				lblResult.setText("District updated successfully");
				lblResult.setTextFill(Color.WHITE);
				stage.close();
			});
		});

	}

	/* To delete a selected district from a combo box */
	public void deleteDistrict() {
		pane.setLeft(null);
		Label lblDelete = new Label("Select the district to delete");
		Label lblResult = new Label();
		Button btDelete = new Button("delete");
		HBox hbNodes = new HBox(10);
		VBox vb = new VBox(10);

		hbNodes.getChildren().addAll(cbDistrict, btDelete);
		vb.getChildren().addAll(lblDelete, hbNodes, lblResult);
		vb.setAlignment(Pos.CENTER);
		hbNodes.setAlignment(Pos.CENTER);

		GridPane.setHalignment(lblDelete, HPos.CENTER);

		pane.setCenter(vb);

		FXCollections.sort(distList);

		btDelete.setOnAction(e -> {
			District dist = cbDistrict.getValue();

			/* To check if the user selected any district */
			if (dist == null) {
				lblResult.setText("Select a District");
				lblResult.setTextFill(Color.RED);
				return;
			}

			Label lblWarning = new Label("Are you sure you want to delete this district?");
			Button btYes = new Button("Yes");
			Button btNo = new Button("No");
			HBox hbButtons = new HBox(10);

			hbButtons.getChildren().addAll(btNo, btYes);
			hbButtons.setAlignment(Pos.CENTER);

			GridPane gPane = new GridPane();
			gPane.add(lblWarning, 0, 0);
			gPane.add(hbButtons, 0, 1);

			gPane.setHgap(10);
			gPane.setVgap(10);
			gPane.setAlignment(Pos.CENTER);

			Scene scene = new Scene(gPane, 300, 200);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Warning");
			stage.show();

			/* A warning scene to see if the user is sure to delete the district */
			btNo.setOnAction(e1 -> {
				lblResult.setText("");
				stage.close();
			});

			btYes.setOnAction(e1 -> {
				district.remove(dist);
				distList.remove(dist);
				stage.close();
				lblResult.setText("District deleted successfully");
				lblResult.setTextFill(Color.WHITE);
			});
		});
	}

	/*
	 * This method to add district to the Binary search tree If the input from user
	 * is available it checks if the district exists or not, if does exists it give
	 * a warning else it add it to the tree.
	 */
	public void addDistrict() {
		pane.setLeft(null);
		Label lblDistrict = new Label("District Name:");
		Label lblResult = new Label();
		TextField tfDistrict = new TextField();
		Button btAdd = new Button("add");
		Button btClear = new Button("clear");
		HBox hbButtons = new HBox(10);
		GridPane gPane = new GridPane();

		hbButtons.getChildren().addAll(btClear, btAdd);
		hbButtons.setAlignment(Pos.CENTER);

		gPane.add(lblDistrict, 0, 0);
		gPane.add(tfDistrict, 1, 0);
		gPane.add(hbButtons, 1, 1);
		gPane.add(lblResult, 1, 2);
		gPane.setHgap(10);
		gPane.setVgap(10);
		gPane.setAlignment(Pos.CENTER);

		GridPane.setHalignment(lblResult, HPos.CENTER);

		pane.setCenter(gPane);

		btClear.setOnAction(e -> {
			tfDistrict.clear();
			lblResult.setText("");
		});

		btAdd.setOnAction(e -> {
			String distName = tfDistrict.getText();
			tfDistrict.clear();
			/* To check if the input from the user is not empty */
			if (distName.isBlank()) {
				lblResult.setText("Please Fill the Field");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* To check if the input from the user is available */
			if (!availabelString(distName)) {
				lblResult.setText("Invalid Input");
				lblResult.setTextFill(Color.RED);
				return;
			}

			/* To check if the district exists or not */
			District dist = new District(distName);
			boolean exists = district.contains(dist);
			if (exists) {
				lblResult.setText("District already Exists");
				lblResult.setTextFill(Color.RED);
				return;
			}

			district.insert(dist);
			distList.add(dist);
			lblResult.setText("District added successfully");
			lblResult.setTextFill(Color.WHITE);
		});
	}

	/*
	 * In this method I used Scanner to read data from the chosen file and stored
	 * the values in variables and made an object of Martyr to add it to the list.
	 */
	public void read() {
		try (Scanner in = new Scanner(file)) {
			in.nextLine();
			while (in.hasNext()) {
				String line = in.nextLine();
				if (line.isBlank())
					continue;
				String[] splitLine = line.split(",");
				String name = splitLine[0];
				String date = splitLine[1];
				byte age = 0;
				try {
					age = Byte.parseByte(splitLine[2]);
				} catch (NumberFormatException e) {
					age = 0;
				}
				String loc = splitLine[3];
				String dist = splitLine[4];
				char gender = splitLine[5].charAt(0);

				Martyr mart = new Martyr(name, age, gender);

				add(dist, loc, date, mart);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void add(String distName, String locName, String date, Martyr mart) {
		District tempDist = new District(distName);
		Location tempLoc = new Location(locName);
		MartyrsDates tempDate = new MartyrsDates(date);

		/*
		 * To check if the district exists or not, if its found it returns the node else
		 * it returns null.
		 */
		BNode distNode = checkDistrict(tempDist);
		if (distNode == null) {
			tempDist.incrementTotal();
			tempDate.addMartyr(mart);
			tempLoc.getDates().insert(tempDate);
			tempDist.getLocations().insert(tempLoc);
			district.insert(tempDist);
			distList.add(tempDist);
		} else {
			/*
			 * To check if the location exists or not, if its found it returns the node else
			 * it returns null.
			 */
			District dist = (District) distNode.getElement();
			dist.incrementTotal();
			BNode locNode = dist.checkLocation(tempLoc);
			if (locNode == null) {
				tempDate.addMartyr(mart);
				tempLoc.getDates().insert(tempDate);
				dist.getLocations().insert(tempLoc);
			} else {
				/*
				 * To check if the Date exists or not, if its found it returns the node else it
				 * returns null.
				 */
				Location loc = (Location) locNode.getElement();
				BNode dateNode = loc.checkDates(tempDate);
				if (dateNode == null) {
					tempDate.addMartyr(mart);
					loc.getDates().insert(tempDate);
				} else {
					/*
					 * To check if the martyr exists or not, if it does it won't add it else it will
					 * add it to the martyr Linked List.
					 */
					MartyrsDates existsDate = (MartyrsDates) dateNode.getElement();
					if (existsDate.checkMartyr(mart) != null)
						return;
					existsDate.addMartyr(mart);
				}
			}
		}
	}

	/* A method to check if the district already exists or not */
	private BNode checkDistrict(District dist) {
		return district.find(dist);
	}

	/*
	 * This method checks if the String is available by check each character if it's
	 * not a letter
	 */
	private boolean availabelString(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isAlphabetic(s.charAt(i)) && s.charAt(i) != '-')
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
