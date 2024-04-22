import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InterFace extends Application {
	Menu FileMenu;
	Menu martyrMenu;
	Menu locationMenu;
	Menu statMenu;
	MenuItem insertM;
	MenuItem deleteM;
	MenuItem updateM;
	MenuItem searchM;
	MenuItem insertL;
	MenuItem deleteL;
	MenuItem updateL;
	MenuItem searchL;
	MenuItem Cfile;
	MenuItem save;
	MenuItem statM;
	Button fileC;
	MenuBar mb;
	FileChooser fileChooser;
	LinkedList city;
	LocationNode n;

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane bp = new BorderPane();
		FileMenu = new Menu("File");
		martyrMenu = new Menu("Martyrs");
		locationMenu = new Menu("Locations");
		statMenu = new Menu("Stat");
		insertM = new MenuItem("insert");
		updateM = new MenuItem("update");
		deleteM = new MenuItem("delete");
		searchM = new MenuItem("search");
		insertL = new MenuItem("insert");
		updateL = new MenuItem("update");
		deleteL = new MenuItem("delete");
		searchL = new MenuItem("search");
		save = new MenuItem("save Changes");
		Cfile = new MenuItem("Choose another file");
		statM = new MenuItem("Statistics");
		martyrMenu.getItems().addAll(insertM, updateM, deleteM, searchM);
		locationMenu.getItems().addAll(insertL, updateL, deleteL, searchL);
		FileMenu.getItems().addAll(Cfile, save);
		statMenu.getItems().addAll(statM);
		mb = new MenuBar();
		mb.setDisable(true);
		mb.getMenus().addAll(locationMenu, martyrMenu, FileMenu, statMenu);
		fileC = new Button("Choose a file");
		bp.setTop(mb);
		bp.setCenter(fileC);

		/**
		 * 
		 * This event handler is triggered when the "fileC" button is clicked. It allows
		 * the user to select a text file containing martyr information. The file is
		 * read line by line, and each line is split into separate parts. If the line
		 * contains at least 4 parts, a Martyr object is created using the extracted
		 * information (name, age, location, date, gender). The Martyr object is then
		 * inserted into the appropriate data structures (LinkedList, AVL trees). If any
		 * errors occur during the file reading or object creation process, they are
		 * handled gracefully without interrupting the execution. Finally, the file is
		 * closed, and the process is completed. The "Cfile" button is linked to this
		 * event, and it sets the "fileC" pane as the central content of the BorderPane
		 * layout.
		 */
		fileC.setOnAction(e -> {
			city = new LinkedList();
			fileChooser = new FileChooser();
			FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(txtFilter);
			fileChooser.setTitle("Select File");
			File selectedFile = fileChooser.showOpenDialog(stage);
			if (selectedFile == null) {
				mb.setDisable(true);
			} else {
				mb.setDisable(false);

				try {
					BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
					String line;
					while ((line = reader.readLine()) != null) {
						// Skip empty lines
						if (line.trim().isEmpty()) {
							continue;
						}

						String[] parts = line.split(",");
						if (parts.length >= 5) {
							String name = parts[0].trim();
							String ageStr = parts[1].trim();
							String location = parts[2].trim();
							String dateStr = parts[3].trim();
							String gender = parts[4].trim();

							// Skip invalid entries
							if (name.isEmpty() || ageStr.isEmpty() || location.isEmpty() || dateStr.isEmpty()
									|| gender.isEmpty()) {
								continue;
							}

							try {
								int age = Integer.parseInt(ageStr);
								DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
								LocalDate date;
								try {
									date = LocalDate.parse(dateStr, dateFormatter);
								} catch (DateTimeParseException e1) {
									continue; // Skip invalid date format
								}

								// Skip invalid age
								if (age <= 0) {
									continue;
								}

								// Check for duplicates
								Martyr martyr = new Martyr(name, age, location, date, gender.charAt(0));
								LocationNode locationNode = city.search(location);
								if (locationNode == null) {
									locationNode = new LocationNode(new Location(location));
									city.insert(locationNode);
								}

								Location locationObj = locationNode.getLocation();
								if (locationObj != null) {
									DateStack ds = locationObj.getDateAvl().search(date);
									if (ds == null) {
										ds = new DateStack(date);
										ds.getStack().push(martyr);
									} else {
										if (ds.getStack().contains(martyr)) {
											continue;
										}
										ds.getStack().push(martyr);
									}

									locationObj.getDateAvl().insert(ds);
									locationObj.getMartyrAvl().insert(martyr);
								}
							} catch (NumberFormatException e1) {
								continue;
							}
						}
					}
					reader.close();
				} catch (IOException e2) {
					System.out.println("Error reading the file: " + e2.getMessage());
				}
			}
		});

		Cfile.setOnAction(e -> {
			bp.setCenter(fileC);
		});

		/**
		 * 
		 * This event handler is triggered when the "insertM" button is clicked. It
		 * creates a form for entering information about a new martyr. The form includes
		 * fields for name, age, date of death, location, and gender. The user can enter
		 * the required information and click the "insert" button to save the martyr.
		 * Input validation is performed for each field, and error messages are
		 * displayed if necessary. If the input is valid, a Martyr object is created and
		 * inserted into the appropriate data structures. The form fields are cleared,
		 * and a success message is displayed. The "insertM" button is linked to this
		 * event, and it sets the form as the central content of the BorderPane layout.
		 */
		insertM.setOnAction(e -> {
			Label martyrs = new Label("New Martyr");
			martyrs.setFont(new Font(18));
			Label l1 = new Label("Name: ");
			Label l2 = new Label("Age: ");
			Label l3 = new Label("Date of death: ");
			Label l4 = new Label("Location: ");
			Label l5 = new Label("Gender: ");
			Label l6 = new Label();
			TextField t1 = new TextField();
			TextField t2 = new TextField();
			TextField t3 = new TextField();
			TextField t4 = new TextField();
			ComboBox<String> genderBox = new ComboBox<>();
			genderBox.getItems().addAll("M", "F");
			genderBox.setValue("M");

			Button saveB = new Button("insert");
			t3.setPromptText("yyyy-MM-dd");
			HBox h = new HBox(saveB);
			h.setAlignment(Pos.CENTER);
			HBox h2 = new HBox(l6);
			h2.setAlignment(Pos.CENTER);
			GridPane gp = new GridPane();
			gp.add(martyrs, 1, 0);
			gp.add(l1, 0, 1);
			gp.add(l2, 0, 2);
			gp.add(l3, 0, 3);
			gp.add(l4, 0, 4);
			gp.add(l5, 0, 5);
			gp.add(t1, 1, 1);
			gp.add(t2, 1, 2);
			gp.add(t3, 1, 3);
			gp.add(t4, 1, 4);
			gp.add(genderBox, 1, 5);
			gp.add(h, 1, 6);
			gp.add(h2, 1, 7);
			gp.setHgap(10);
			gp.setVgap(5);
			bp.setCenter(gp);
			saveB.setOnAction(e1 -> {
				String name = t1.getText();
				String ageText = t2.getText();
				String date = t3.getText();
				String location = t4.getText();
				String gender = genderBox.getValue();

				if (name.isEmpty()) {
					showAlert("Invalid Name", "Name field is empty.");
					return;
				}

				int age;
				try {
					age = Integer.parseInt(ageText);
					if (age < 0) {
						showAlert("Invalid Age", "Age cannot be negative.");
						return;
					}
				} catch (NumberFormatException e2) {
					showAlert("Invalid Age", "Age must be a valid number.");
					return;
				}
				if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
					showAlert("Invalid Date Format", "Date must be in the format yyyy-MM-dd.");
					return;
				}

				LocalDate localDate;
				try {
					localDate = LocalDate.parse(date);
				} catch (DateTimeParseException e11) {
					showAlert("Invalid Date", "Date must be in the format yyyy-MM-dd.");
					return;
				}

				if (location.isEmpty()) {
					showAlert("Invalid Location", "Location field is empty.");
					return;
				}

				Martyr martyr = new Martyr(name, age, location, localDate, gender.charAt(0));

				n = city.search(location);
				if (n == null) {
					n = new LocationNode(new Location(location));
					city.insert(n);
				}

				n.getLocation().getMartyrAvl().insert(martyr);
				DateStack ds = n.getLocation().getDateAvl().search(localDate);
				if (ds == null) {
					ds = new DateStack(localDate);
					ds.getStack().push(martyr);
					n.getLocation().getDateAvl().insert(ds);
				} else {
					ds.getStack().push(martyr);
				}

				t1.clear();
				t2.clear();
				t3.clear();
				t4.clear();
				genderBox.setValue("M");

				l6.setText("Martyr inserted successfully!");
			});

		});

		/**
		 * 
		 * This event handler is triggered when the "deleteM" button is clicked. It
		 * displays a form for deleting a martyr. The form includes a dropdown menu to
		 * select the city and another dropdown menu to select the martyr. If the city
		 * list is empty, an error message is displayed. When a city is selected, the
		 * corresponding martyrs are populated in the martyr dropdown menu. If a martyr
		 * and a city are selected, a confirmation dialog is shown to confirm the
		 * deletion. If the user confirms the deletion, the martyr is deleted from the
		 * data structures. An information message is displayed to indicate the
		 * successful deletion. The form fields are cleared after the deletion. The
		 * "deleteM" button is linked to this event, and it sets the form as the central
		 * content of the BorderPane layout.
		 */
		deleteM.setOnAction(e -> {
			Label top = new Label("Delete a Martyr");
			top.setFont(new Font(20));
			Label l1 = new Label("Name: ");
			Label l2 = new Label("Location: ");
			Button deleteB = new Button("Delete");
			ComboBox<String> cityComboBox = new ComboBox<>();
			ComboBox<String> martyrBox = new ComboBox<>();
			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}
			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityComboBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);

			cityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
				martyrBox.getItems().clear();
				n = city.search(newvalue);
				if (n != null) {

					n.getLocation().getMartyrAvl().inorderTraversal(n.getLocation().getMartyrAvl().getRoot(),
							martyrBox);
				}
			});
			deleteB.setOnAction(e1 -> {
				if (martyrBox.getValue() != null && cityComboBox.getValue() != null) {
					n = city.search(cityComboBox.getValue());
					martyrAVLNode martyrNode = n.getLocation().getMartyrAvl().searchByName(martyrBox.getValue());
					if (martyrNode != null) {
						Martyr martyr = martyrNode.getMartyr();

						Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
						confirmationAlert.setTitle("Confirmation");
						confirmationAlert.setHeaderText("Confirm Delete");
						confirmationAlert.setContentText("Are you sure you want to delete the martyr?");

						Optional<ButtonType> result = confirmationAlert.showAndWait();

						if (result.isPresent() && result.get() == ButtonType.OK) {
							n.getLocation().getMartyrAvl().delete(martyrBox.getValue().trim());

							DateStack dateStack = n.getLocation().getDateAvl().search(martyr.getDate());
							if (dateStack != null) {
								dateStack.getStack().deleteNode(martyrBox.getValue().trim());
							}

							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							confirmationAlert.setTitle("Deleted");
							confirmationAlert.setHeaderText("");
							confirmationAlert
									.setContentText("Martyr Deleted The martyr has been successfully deleted.");
							martyrBox.getSelectionModel().clearSelection();
							cityComboBox.getSelectionModel().clearSelection();
						}
					}
				}
			});

			HBox h = new HBox(10);
			h.getChildren().addAll(l1, martyrBox);
			h.setAlignment(Pos.CENTER);
			HBox h2 = new HBox(10);
			h2.getChildren().addAll(l2, cityComboBox);
			h2.setAlignment(Pos.CENTER);
			GridPane gp = new GridPane();
			VBox v = new VBox(10);
			v.getChildren().addAll(top, h2, h, deleteB);
			v.setAlignment(Pos.CENTER);
			bp.setCenter(v);
		});

		/**
		 * 
		 * This event handler is triggered when the "updateM" button is clicked. It
		 * displays a form for updating martyr information. The form includes dropdown
		 * menus to select the city and the martyr to be updated. If the city list is
		 * empty, an error message is displayed. When a city is selected, the
		 * corresponding martyrs are populated in the martyr dropdown menu. If a martyr
		 * and a city are selected, the form fields are populated with the martyr's
		 * current information. The user can update the age, date of death, and gender
		 * of the martyr. After updating, the martyr's information is updated in the
		 * data structures. An information message is displayed to indicate the
		 * successful update. The form fields are cleared after the update. The
		 * "updateM" button is linked to this event, and it sets the form as the central
		 * content of the BorderPane layout.
		 */
		updateM.setOnAction(e -> {
			Label top = new Label("Update Martyr info");
			top.setFont(new Font(18));
			Label l1 = new Label("Name: ");
			Label l2 = new Label("Age: ");
			Label l3 = new Label("Date of death: ");
			Label l4 = new Label("Location: ");
			Label l5 = new Label("Gender: ");
			Label l6 = new Label();
			TextField t2 = new TextField();
			TextField t3 = new TextField();
			ComboBox<String> genderBox = new ComboBox<>();
			genderBox.getItems().addAll("M", "F");
			Button updateB = new Button("Update");
			ComboBox<String> cityComboBox = new ComboBox<>();
			ComboBox<String> martyrBox = new ComboBox<>();
			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}
			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityComboBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);
			updateB.setDisable(true);
			cityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) -> {
				martyrBox.getSelectionModel().selectedItemProperty()
						.addListener((observable1, oldValue1, newValue1) -> {
							if (newValue1 != null && cityComboBox.getValue() != null) {
								updateB.setDisable(false);
								n = city.search(cityComboBox.getValue());
								martyrAVLNode martyrNode = n.getLocation().getMartyrAvl().searchByName(newValue1);
								if (martyrNode != null) {
									Martyr martyr = martyrNode.getMartyr();
									t2.setText(String.valueOf(martyr.getAge()));
									t3.setText(martyr.getDate().toString());
									genderBox.setValue(String.valueOf(martyr.getGender()));
								}

							}
						});
				martyrBox.getItems().clear();
				n = city.search(newvalue);
				if (n != null) {
					martyrAVLNode curr = n.getLocation().getMartyrAvl().getRoot();
					n.getLocation().getMartyrAvl().inorderTraversal(n.getLocation().getMartyrAvl().getRoot(),
							martyrBox);
				}
			});
			HBox h = new HBox(updateB);
			updateB.setOnAction(e1 -> {
				if (martyrBox.getValue() != null && cityComboBox.getValue() != null) {
					n = city.search(cityComboBox.getValue());
					martyrAVLNode martyrNode = n.getLocation().getMartyrAvl().searchByName(martyrBox.getValue());
					if (martyrNode != null) {
						Martyr oldMartyr = martyrNode.getMartyr();

						try {
							int age = Integer.parseInt(t2.getText());

							LocalDate date = LocalDate.parse(t3.getText());

							Martyr newMartyr = new Martyr(oldMartyr.getName(), age, l4.getText(), date,
									genderBox.getValue().charAt(0));

							n.getLocation().getMartyrAvl().update(oldMartyr, newMartyr);
							DateStack ds = n.getLocation().getDateAvl().search(oldMartyr.getDate());
							ds.getStack().deleteMartyr(oldMartyr);
							ds = n.getLocation().getDateAvl().search(newMartyr.getDate());
							if (ds == null) {
								ds = new DateStack(date);
								ds.getStack().push(newMartyr);
							} else {
								ds.getStack().push(newMartyr);
							}
							n.getLocation().getDateAvl().insert(ds);
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Updated");
							alert.setHeaderText(null);
							alert.setContentText("Update Successful\", \"Martyr information has been updated.");
							alert.showAndWait();
							martyrBox.getSelectionModel().clearSelection();
							cityComboBox.getSelectionModel().clearSelection();
							t2.clear();
							t3.clear();
							genderBox.getSelectionModel().clearSelection();

						} catch (NumberFormatException e12) {
							showAlert("Invalid Age", "Please enter a valid age as a number.");

						} catch (DateTimeParseException e22) {
							showAlert("Invalid Date", "Please enter a valid date in the format yyyy-MM-dd.");
						}
					}
				}
			});

			;
			h.setAlignment(Pos.CENTER);
			t3.setPromptText("yyyy-MM-dd");
			GridPane gp = new GridPane();
			gp.add(top, 1, 0);
			gp.add(l4, 0, 1);
			gp.add(l1, 0, 2);
			gp.add(l2, 0, 3);
			gp.add(l3, 0, 4);
			gp.add(l5, 0, 5);
			gp.add(cityComboBox, 1, 1);
			gp.add(martyrBox, 1, 2);
			gp.add(t2, 1, 3);
			gp.add(t3, 1, 4);
			gp.add(genderBox, 1, 5);
			gp.add(h, 1, 6);
			gp.add(l6, 1, 7);
			gp.setHgap(10);
			gp.setVgap(5);
			bp.setCenter(gp);
		});
		/**
		 * 
		 * This event handler is triggered when the "insertL" button is clicked. It
		 * displays a form for inserting a new location (city). The form includes a text
		 * field to enter the city name and a button to perform the insertion. After
		 * inserting the city, a message is displayed indicating whether the insertion
		 * was successful or if the city already exists. The "insertL" button is linked
		 * to this event, and it sets the form as the central content of the BorderPane
		 * layout.
		 */
		insertL.setOnAction(e -> {
			Label top = new Label("New Location");
			top.setFont(new Font(20));
			Label l1 = new Label("City name: ");
			TextField t1 = new TextField();
			Label l2 = new Label();
			Button insertB = new Button("Insert");
			HBox h = new HBox(10);
			h.getChildren().addAll(l1, t1);
			h.setAlignment(Pos.CENTER);
			VBox v = new VBox(10);
			v.getChildren().addAll(top, h, insertB, l2);
			v.setAlignment(Pos.CENTER);
			insertB.setOnAction(e1 -> {
				String name = t1.getText();
				if (!name.isEmpty()) {
					LocationNode exist = city.search(name);
					if (exist == null) {
						city.insert(new LocationNode(new Location(name)));
						l2.setText("The city have been inserted succefully");
					} else
						l2.setText("The city already exists");
				}
			});
			bp.setCenter(v);
		});
		/**
		 * 
		 * This event handler is triggered when the "deleteL" button is clicked. It
		 * displays a form for deleting a location (city). The form includes a combo box
		 * to select the city to be deleted and a button to perform the deletion. After
		 * deleting the city, a message is displayed indicating whether the deletion was
		 * successful or if no city was selected. The "deleteL" button is linked to this
		 * event, and it sets the form as the central content of the BorderPane layout.
		 */
		deleteL.setOnAction(e -> {
			Label top = new Label("Delete Location");
			top.setFont(new Font(20));
			Label l1 = new Label("City");
			Button deleteB = new Button("Delete");
			Label l2 = new Label();
			ComboBox<String> cityComboBox = new ComboBox<>();
			HBox h = new HBox(10);
			h.getChildren().addAll(l1, cityComboBox);
			h.setAlignment(Pos.CENTER);
			VBox v = new VBox(10);
			v.getChildren().addAll(top, h, deleteB, l2);
			v.setAlignment(Pos.CENTER);
			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}
			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityComboBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);

			deleteB.setOnAction(e1 -> {
				l2.setText(null);
				if (cityComboBox.getValue() != null) {
					city.delete(cityComboBox.getValue());
					cityComboBox.setValue(null);
					cityComboBox.getItems().clear();

					n = city.getFront();

					LocationNode startNode1 = city.getFront();
					LocationNode currentNode1 = startNode;

					do {
						cityComboBox.getItems().add(currentNode1.getLocation().getLocation());
						currentNode1 = currentNode1.getNext();
					} while (currentNode1 != startNode);
				} else if (cityComboBox.getValue() == null) {

					l2.setText("Choose a city");
				}

			});
			bp.setCenter(v);
		});
		/**
		 * 
		 * This event handler is triggered when the "updateL" button is clicked. It
		 * displays a form for updating a location (city). The form includes a combo box
		 * to select the old city name and a text field to enter the new city name.
		 * There is also a button to perform the update. The event handler first checks
		 * if the city list is empty. If it is, an alert is displayed and the handler
		 * returns. The combo box is populated with the existing city names. When the
		 * update button is clicked, it retrieves the selected old city name and the new
		 * city name entered in the text field. It checks if both values are not null or
		 * empty. Then it searches for the new city name in the city list to ensure it
		 * does not already exist. If the new city name already exists, an error alert
		 * is displayed. If the new city name is unique, it updates the city name in the
		 * city list, updates the items in the combo box, and clears the text field. The
		 * "updateL" button is linked to this event, and it sets the form as the central
		 * content of the BorderPane layout.
		 */
		updateL.setOnAction(e -> {
			Label top = new Label("Update a Location");
			top.setFont(new Font(20));
			Label l1 = new Label("Old City");
			Label l2 = new Label("New City");
			ComboBox<String> cityComboBox = new ComboBox<>();
			TextField t2 = new TextField();
			Button updateB = new Button("Update");
			HBox h1 = new HBox(10);
			h1.getChildren().addAll(l1, cityComboBox);
			h1.setAlignment(Pos.CENTER);
			HBox h2 = new HBox(10);
			h2.getChildren().addAll(l2, t2);
			h2.setAlignment(Pos.CENTER);
			VBox v = new VBox(10);
			v.setAlignment(Pos.CENTER);
			v.getChildren().addAll(top, h1, h2, updateB);
			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}
			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityComboBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);
			t2.setText(null);
			updateB.setOnAction(e1 -> {
				String oldCity = cityComboBox.getValue();
				String newCity = t2.getText();

				if (oldCity != null && !oldCity.isEmpty() && newCity != null && !newCity.isEmpty()) {
					LocationNode currentNode1 = city.getFront();

					boolean cityExists = false;
					do {
						if (city.search(newCity) != null) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("City Update Error");
							alert.setHeaderText("City Already Exists");
							alert.setContentText(
									"The city \"" + newCity + "\" already exists. Please enter a different city name.");
							alert.showAndWait();
							cityExists = true;
							break;
						}

						if (currentNode1.getLocation().getLocation().equals(oldCity)) {
							currentNode1.getLocation().setLocation(newCity);
							break;
						}

						currentNode1 = currentNode1.getNext();
					} while (currentNode1 != city.getFront());

					if (!cityExists) {
						cityComboBox.getItems().remove(oldCity);
						cityComboBox.getItems().add(newCity);
						cityComboBox.setValue(newCity);
						t2.clear();
					}
				}
			});

			bp.setCenter(v);
		});
		/**
		 * 
		 * This event handler is triggered when the "save" button is clicked. It opens a
		 * file chooser dialog to select a file to save the AVL tree data. The selected
		 * file is then used to write the data from the AVL tree to the file. The event
		 * handler first creates a file chooser dialog with a title and a filter for
		 * text files. The user is prompted to select a file to save the AVL tree data.
		 * If a file is selected (i.e., not null), the event handler attempts to write
		 * the data to the file. It iterates through each location in the city list and
		 * calls the "saveToFile" method on the AVL tree of each location. The
		 * "saveToFile" method writes the AVL tree data to the selected file. If any
		 * errors occur during the file writing process, they are printed to the
		 * console. The "save" button is linked to this event, and it triggers the file
		 * selection and saving process.
		 */
		save.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save AVL Tree to File");
			FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(txtFilter);
			File selectedFile = fileChooser.showSaveDialog(stage);

			if (selectedFile != null) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
					LocationNode current = city.getFront();
					do {
						current.getLocation().getMartyrAvl().saveToFile(selectedFile);
						current = current.getNext();
					} while (current != city.getFront());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		/**
		 * 
		 * This event handler is triggered when the "search" button is clicked. It
		 * allows the user to choose a city from a dropdown menu and displays the list
		 * of martyrs from that city in a table. The event handler first creates a
		 * label, a dropdown menu for city selection, and a search button. If the city
		 * list is empty, an alert is shown to prompt the user to add a city first. The
		 * dropdown menu is populated with city names by iterating through the city list
		 * and adding each city name to the menu. A TableView is created with columns
		 * for martyr information (name, age, death date, gender). The table columns are
		 * linked to the corresponding properties of the Martyr class. When a city is
		 * selected from the dropdown menu, the event handler retrieves the selected
		 * city and clears the table. It then searches for the location node of the
		 * selected city. If the location node is found, it retrieves the AVL tree of
		 * martyrs from that location and performs an inorder traversal. During the
		 * traversal, each martyr is added to the table's items list. The table view,
		 * city dropdown menu, and other components are arranged in a VBox and set as
		 * the center content of the border pane. The "search" button is linked to this
		 * event, and it triggers the city selection and martyr listing process.
		 */
		searchL.setOnAction(e -> {
			Label top = new Label("Choose a city");
			ComboBox<String> cityBox = new ComboBox<>();
			Button search = new Button("Search");

			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}

			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);

			TableView<Martyr> tableView = new TableView<>();
			TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
			TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
			TableColumn<Martyr, String> deathColumn = new TableColumn<>("Death");
			TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");

			nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
			deathColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
			genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
			nameColumn.setPrefWidth(250);

			tableView.getColumns().addAll(nameColumn, ageColumn, deathColumn, genderColumn);

			cityBox.setOnAction(e2 -> {
				String selectedCity = cityBox.getValue();
				if (selectedCity != null) {
					tableView.getItems().clear();
					LocationNode locationNode = city.search(selectedCity);
					if (locationNode != null) {
						martyrAVLNode current = locationNode.getLocation().getMartyrAvl().getRoot();
						martyrAVLNode predecessor;

						while (current != null) {
							if (current.getLeft() == null) {
								Martyr martyr = current.getMartyr();
								tableView.getItems().add(martyr);

								current = current.getRight();
							} else {
								predecessor = current.getLeft();
								while (predecessor.getRight() != null && predecessor.getRight() != current) {
									predecessor = predecessor.getRight();
								}

								if (predecessor.getRight() == null) {
									predecessor.setRight(current);
									current = current.getLeft();
								} else {
									predecessor.setRight(null);
									Martyr martyr = current.getMartyr();
									tableView.getItems().add(martyr);

									current = current.getRight();
								}
							}
						}
					}
				}
			});

			VBox v = new VBox(10);
			v.getChildren().addAll(top, cityBox, tableView);
			v.setAlignment(Pos.CENTER);

			bp.setCenter(v);
		});

		/**
		 * 
		 * This event handler is triggered when the "search martyrs" button is clicked.
		 * It allows the user to select a city from a dropdown menu and search for
		 * martyrs by name within that city. The event handler first creates labels, a
		 * dropdown menu for city selection, a search field, a search button, and a
		 * table view to display the search results. If the city list is empty, an alert
		 * is shown to prompt the user to add a city first. The dropdown menu is
		 * populated with city names by iterating through the city list and adding each
		 * city name to the menu. The event handler also creates next and previous
		 * buttons for city navigation. The event handler sets up the actions for the
		 * next and previous buttons to navigate to the next and previous cities,
		 * respectively. When a city is selected from the dropdown menu, the event
		 * handler retrieves the location node of the selected city and displays the
		 * martyrs in a table view. It uses the AVL tree associated with the city to
		 * retrieve the martyrs in sorted order. The event handler also sets up a search
		 * field that allows the user to search for martyrs by name within the selected
		 * city. Whenever the user enters a keyword in the search field, the event
		 * handler filters the martyrs based on the entered keyword and updates the
		 * table view accordingly. The labels, dropdown menu, search field, table view,
		 * and navigation buttons are arranged in a VBox and set as the center content
		 * of the border pane. The "search" button is linked to this event, and it
		 * triggers the city selection and martyr search process.
		 */
		searchM.setOnAction(e -> {
			Label top = new Label("Choose a city");
			ComboBox<String> cityBox = new ComboBox<>();
			Button search = new Button("Search");
			TextField searchField = new TextField();
			searchField.setPromptText("Search for a Martyr By Name");

			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}

			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);

			TableView<Martyr> tableView = new TableView<>();
			TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
			TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
			TableColumn<Martyr, String> deathColumn = new TableColumn<>("Death");
			TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");

			nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
			deathColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
			genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
			nameColumn.setPrefWidth(250);

			tableView.getColumns().addAll(nameColumn, ageColumn, deathColumn, genderColumn);

			Button nextButton = new Button("Next");
			Button previousButton = new Button("Previous");

			nextButton.setOnAction(e1 -> {
				String selectedCity = cityBox.getValue();
				if (selectedCity != null) {
					LocationNode locationNode = city.search(selectedCity);
					if (locationNode != null) {
						LocationNode nextNode = locationNode.getNext();
						if (nextNode != null) {
							cityBox.setValue(nextNode.getLocation().getLocation());
						}
					}
				}
			});

			previousButton.setOnAction(e1 -> {
				String selectedCity = cityBox.getValue();
				if (selectedCity != null) {
					LocationNode locationNode = city.search(selectedCity);
					if (locationNode != null) {
						LocationNode previousNode = locationNode.getPre();
						if (previousNode != null) {
							cityBox.setValue(previousNode.getLocation().getLocation());
						}
					}
				}
			});

			cityBox.setOnAction(e2 -> {
				String selectedCity = cityBox.getValue();
				if (selectedCity != null) {
					tableView.getItems().clear();

					LocationNode locationNode = city.search(selectedCity);
					if (locationNode != null) {
						martyrAVLNode current = locationNode.getLocation().getMartyrAvl().getRoot();
						martyrAVLNode predecessor;

						while (current != null) {
							if (current.getLeft() == null) {
								Martyr martyr = current.getMartyr();
								tableView.getItems().add(martyr);

								current = current.getRight();
							} else {
								predecessor = current.getLeft();
								while (predecessor.getRight() != null && predecessor.getRight() != current) {
									predecessor = predecessor.getRight();
								}

								if (predecessor.getRight() == null) {
									predecessor.setRight(current);
									current = current.getLeft();
								} else {
									predecessor.setRight(null);
									Martyr martyr = current.getMartyr();
									tableView.getItems().add(martyr);

									current = current.getRight();
								}
							}
						}
					}
				}
			});

			searchField.setOnKeyReleased(event -> {
				String searchKeyword = searchField.getText().toLowerCase();
				String selectedCity = cityBox.getValue();

				if (selectedCity != null) {
					LocationNode locationNode = city.search(selectedCity);
					if (locationNode != null) {
						martyrAVLNode current = locationNode.getLocation().getMartyrAvl().getRoot();
						martyrAVLNode predecessor;

						tableView.getItems().clear();

						while (current != null) {
							if (current.getLeft() == null) {
								Martyr martyr = current.getMartyr();
								if (martyr.getName().toLowerCase().contains(searchKeyword)) {
									tableView.getItems().add(martyr);
								}

								current = current.getRight();
							} else {
								predecessor = current.getLeft();
								while (predecessor.getRight() != null && predecessor.getRight() != current) {
									predecessor = predecessor.getRight();
								}

								if (predecessor.getRight() == null) {
									predecessor.setRight(current);
									current = current.getLeft();
								} else {
									predecessor.setRight(null);
									Martyr martyr = current.getMartyr();
									if (martyr.getName().toLowerCase().contains(searchKeyword)) {
										tableView.getItems().add(martyr);
									}

									current = current.getRight();
								}
							}
						}
					}
				}
			});

			HBox h = new HBox(10);
			h.getChildren().addAll(previousButton, nextButton);
			h.setAlignment(Pos.CENTER);
			VBox v = new VBox(10);
			v.getChildren().addAll(top, cityBox, searchField, tableView, h);
			v.setAlignment(Pos.CENTER);

			bp.setCenter(v);

		});
		/**
		 * 
		 * This event handler is triggered when the "statistics" button is clicked. It
		 * allows the user to select a city from a dropdown menu and displays various
		 * statistics about that city. The event handler first creates labels, a
		 * dropdown menu for city selection, and buttons for confirmation and
		 * navigation. If the city list is empty, an alert is shown to prompt the user
		 * to add a city first. The dropdown menu is populated with city names by
		 * iterating through the city list and adding each city name to the menu. The
		 * event handler creates a VBox to hold the top-level components (labels,
		 * dropdown menu, and confirmation button). The VBox is centered in the border
		 * pane. When the confirmation button is clicked, the event handler retrieves
		 * the selected city from the dropdown menu. If a city is selected, it retrieves
		 * the location node of the selected city and displays various statistics about
		 * that city. The statistics include the number of martyrs in the city, the
		 * height of AVL trees associated with the city, and the date with the highest
		 * number of martyrs. Text areas are used to display the AVL tree contents in
		 * level order and the dates in ascending order. The statistics, text areas, and
		 * other components are arranged in a VBox and set as the center content of the
		 * border pane. The user can navigate to the next city or previous city using
		 * the respective buttons. The "confirm" button is linked to this event, and it
		 * triggers the city selection and statistics display process.
		 */
		statM.setOnAction(e -> {
			Label top = new Label("Statistics");
			top.setFont(new Font(20));
			Label l1 = new Label("Select a city to get its stats");
			ComboBox<String> cityBox = new ComboBox<>();
			Button confirmB = new Button("Confirm");
			Button next = new Button("Next city");
			Button pre = new Button("Previous city");
			Label top2 = new Label();
			Label count = new Label();
			Label avl1H = new Label();
			Label avl2H = new Label();
			Label dateD = new Label();
			TextArea ta1 = new TextArea();
			TextArea ta2 = new TextArea();
			ta1.setEditable(false);
			ta2.setEditable(false);
			count.setFont(new Font(14));
			avl1H.setFont(new Font(14));
			avl2H.setFont(new Font(14));
			dateD.setFont(new Font(14));
			top2.setFont(new Font(22));
			if (city.isEmpty()) {
				showAlert("No Cities", "The city list is empty. Please add a city first.");
				return;
			}
			n = city.getFront();

			LocationNode startNode = city.getFront();
			LocationNode currentNode = startNode;

			do {
				cityBox.getItems().add(currentNode.getLocation().getLocation());
				currentNode = currentNode.getNext();
			} while (currentNode != startNode);
			HBox h = new HBox(10);
			h.getChildren().addAll(l1, cityBox);
			h.setAlignment(Pos.CENTER);
			VBox v = new VBox(10);
			v.getChildren().addAll(top, h, confirmB);
			v.setAlignment(Pos.CENTER);
			bp.setCenter(v);
			confirmB.setOnAction(e1 -> {
				String location = cityBox.getValue();
				if (location != null) {
					n = city.search(location);
					top2.setText(n.getLocation().getLocation());
					count.setText(
							"The number of martyrs in this city is: " + n.getLocation().getMartyrAvl().getNodeCount());
					avl1H.setText("AVL 1 height: " + n.getLocation().getMartyrAvl().getHeight());
					avl2H.setText("AVL 2 height: " + n.getLocation().getDateAvl().getHeight());
					dateD.setText(
							"The date with highest martyrs: " + n.getLocation().getDateAvl().getDateWithMaxMartyrs());
					Label ll1 = new Label("AVL 1");
					VBox vh1 = new VBox(10);
					vh1.getChildren().addAll(ll1, ta1);
					vh1.setAlignment(Pos.CENTER);
					Label ll2 = new Label("AVL 2");
					VBox vh2 = new VBox(10);
					vh2.getChildren().addAll(ll2, ta2);
					vh2.setAlignment(Pos.CENTER);
//					n.getLocation().getMartyrAvl().printLevelOrder(ta1);
					ta1.appendText(n.getLocation().getMartyrAvl().printLevelOrder());
					n.getLocation().getDateAvl().printAscendingOrder(ta2);
					VBox v1 = new VBox(10);
					v1.getChildren().add(top2);
					v1.setAlignment(Pos.TOP_CENTER);
					HBox h1 = new HBox(10);
					h1.getChildren().addAll(vh1, vh2);
					HBox h2 = new HBox(10);
					h2.getChildren().addAll(pre, next);
					h2.setAlignment(Pos.CENTER);
					VBox v2 = new VBox(10);
					v2.getChildren().addAll(count, avl1H, avl2H, dateD);
					VBox v3 = new VBox(10);
					v3.getChildren().addAll(v1, v2, h1, h2);
					v3.setAlignment(Pos.CENTER);

					bp.setCenter(v3);
					next.setOnAction(e121 -> {

						n = n.getNext();
						top2.setText(n.getLocation().getLocation());
						count.setText("The number of martyrs in this city is: "
								+ n.getLocation().getMartyrAvl().getNodeCount());
						avl1H.setText("AVL 1 height: " + n.getLocation().getMartyrAvl().getHeight());
						avl2H.setText("AVL 2 height: " + n.getLocation().getDateAvl().getHeight());
						dateD.setText("The date with the highest number of martyrs: "
								+ n.getLocation().getDateAvl().getDateWithMaxMartyrs());
						ta1.clear();
						ta2.clear();
//						n.getLocation().getMartyrAvl().printLevelOrder(ta1);
						ta1.appendText(n.getLocation().getMartyrAvl().printLevelOrder());
						n.getLocation().getDateAvl().printAscendingOrder(ta2);
					});

					pre.setOnAction(e12 -> {
						n = n.getPre();
						top2.setText(n.getLocation().getLocation());
						count.setText("The number of martyrs in this city is: "
								+ n.getLocation().getMartyrAvl().getNodeCount());
						avl1H.setText("AVL 1 height: " + n.getLocation().getMartyrAvl().getHeight());
						avl2H.setText("AVL 2 height: " + n.getLocation().getDateAvl().getHeight());
						dateD.setText("The date with the highest number of martyrs: "
								+ n.getLocation().getDateAvl().getDateWithMaxMartyrs());
						ta1.clear();
						ta2.clear();
						//n.getLocation().getMartyrAvl().printLevelOrder(ta1);
						ta1.appendText(n.getLocation().getMartyrAvl().printLevelOrder());
						n.getLocation().getDateAvl().printAscendingOrder(ta2);
					});
				}

			});
		});
		Scene s = new Scene(bp, 500, 400);
		stage.setScene(s);

		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
