import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClockworkGUI extends Application {

	public static final int DEFAULT_WINDOW_WIDTH = 600;
	public static final int DEFAULT_WINDOW_HEIGHT = 400;

	public static final Text welcomeText = new Text("Welcome, Username");
	public static final Text userCommandDisplayText = new Text("Command: ");
	public static final Text userCommandInputText = new Text("Type Command: ");
	public static final ArrayList<String> commandList = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Mark", "Delete", "Etc"));
	public static final ArrayList<String> displayList = new ArrayList<String>(Arrays.asList("Add 12th Jan 2016"));
	public static final ArrayList<String> typeList = new ArrayList<String>();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		BorderPane defaultLayout = new BorderPane();
		defaultLayout.setPadding(new Insets(10));

		/** Formatting Top Region */
		HBox welcomeSection = addWelcomeSection(welcomeText);
		defaultLayout.setTop(welcomeSection);

		/** Formatting Left Region */
		VBox commandSection = addCommandSection();
		defaultLayout.setLeft(commandSection);

		/** Formatting Center Region */
		Button btnCenter = new Button("Task List Goes Here~");
		defaultLayout.setCenter(btnCenter);

		/** Formatting Right Region */
		Button btnRight = new Button("Calendar Widget Goes Here~");
		defaultLayout.setRight(btnRight);

		/** Formatting Bottom Region */
		GridPane userDisplayAndInputSection = addDisplayAndInputSection();
		defaultLayout.setBottom(userDisplayAndInputSection);

		/** Setting Default Scene */
		Scene scene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		window.setScene(scene);
		window.show();
	}

	/** Add User Display and Input in Bottom Row */
	private GridPane addDisplayAndInputSection() {
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));

		// display user input
		ObservableList<String> displayObsList = FXCollections.observableList(displayList);
		ListView<String> displayListView = new ListView<String>(displayObsList);
		displayListView.setItems(displayObsList);
		displayListView.setPrefSize(500, 250);
		displayListView.setEditable(false);
		displayListView.setCellFactory(TextFieldListCell.forListView());
		
		// for user to key input
		ObservableList<String> typeObsList = FXCollections.observableList(typeList);
		ListView<String> typeListView = new ListView<String>(typeObsList);
		typeListView.setItems(typeObsList);
		typeListView.setPrefSize(500, 50);
		typeListView.setEditable(true);
		typeListView.setCellFactory(TextFieldListCell.forListView());

		grid.getRowConstraints().add(new RowConstraints(5)); // row 0 is 5 high
		grid.getRowConstraints().add(new RowConstraints(50)); // row 0 is 50 high
		grid.getRowConstraints().add(new RowConstraints(5)); // row 1 is 30 high
		grid.getRowConstraints().add(new RowConstraints(30)); // row 1 is 30 high

		GridPane.setConstraints(userCommandDisplayText, 0, 0); // column=0 row=0
		GridPane.setConstraints(displayListView, 0, 1); // column=0 row=1
		GridPane.setConstraints(userCommandInputText, 0, 2); // column=0 row=2
		GridPane.setConstraints(typeListView, 0, 3); // column=0 row=3

		grid.getChildren().addAll(userCommandDisplayText, displayListView, userCommandInputText, typeListView);

		return grid;
	}

	/** Add Command List in Left Column */
	public VBox addCommandSection() {
		VBox commandBox = new VBox();
		commandBox.setPadding(new Insets(15, 12, 15, 12));
		commandBox.setSpacing(10);
		commandBox.setStyle("-fx-background-color: #FFFFFF;");

		ObservableList<String> commandObsList = FXCollections.observableList(commandList);
		ListView<String> commandListView = new ListView<String>(commandObsList);
		commandListView.setItems(commandObsList);

		commandListView.setPrefSize(200, 250);

		// Methods for Edit (Reference)
		// commandListView.setEditable(true);
		// commandListView.setCellFactory(ComboBoxListCell.forListView(commandObsList));
		// commandListView.setCellFactory(ChoiceBoxListCell.forListView(commandObsList));
		// commandListView.setCellFactory(TextFieldListCell.forListView());

		commandBox.getChildren().add(commandListView);

		return commandBox;
	}

	/** Add Welcome Message in Top Row */
	public HBox addWelcomeSection(Text welcomeText) {
		HBox welcomeBox = new HBox();
		welcomeBox.setPadding(new Insets(15, 12, 15, 12));
		welcomeBox.setSpacing(10);
		welcomeBox.setStyle("-fx-background-color: #336699;");

		welcomeText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		welcomeText.setFill(Color.WHITE);
		welcomeText.setStroke(Color.web("#7080A0"));
		welcomeBox.getChildren().add(welcomeText);

		return welcomeBox;
	}
}