import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//TESTING HERE!

public class ClockworkGUI extends Application {

	public static final int DEFAULT_WINDOW_WIDTH = 600;
	public static final int DEFAULT_WINDOW_HEIGHT = 400;

	public static final Text welcomeText = new Text("Welcome, Username");
	public static final Text userCommandDisplayText = new Text("Command: ");
	public static final Text userCommandInputText = new Text("Type Command: ");
	
	public static final ArrayList<String> commandList = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo, Etc"));
	public static final ArrayList<String> taskList = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));
	public static final ArrayList<String> displayList = new ArrayList<String>(
			Arrays.asList("Add 12th Jan 2016", "Delete 12, Testing", "Mark 3", "Undo"));
	
	//themes the user can call to change
	public static final String customTheme = new String();
	public int lol;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		Scene scene;
		switch (customTheme) {
		  case "DARK":
		        scene = darkTheme();
		        break;
		  default:
		       	scene = defaultTheme();
		        break;
		}
		window.setScene(scene);
		window.show();
	}
	
	public Scene darkTheme() {
		return null;
	}

	public Scene defaultTheme(){
		BorderPane defaultLayout = new BorderPane();
		
		defaultLayout.setPadding(new Insets(10));

		/** Formatting Bottom Region 
		 * Takes in ArrayList<String> displayList to display commands the User has entered
		 * To-be-implemented: Can get Text type of what user keyed into TextBox for 
		 * integration with Logic Component
		 */
		GridPane userDisplayAndInputSection = addDisplayAndInputSection(displayList);
		defaultLayout.setBottom(userDisplayAndInputSection);

		/** Formatting Top Region */
		HBox welcomeSection = addWelcomeSection(welcomeText);
		defaultLayout.setTop(welcomeSection);

		/** Formatting Left Region 
		 * Takes in ArrayList<String> commandList to display commands available
		 */
		VBox commandSection = addCommandSection(commandList);
		defaultLayout.setLeft(commandSection);

		/** Formatting Center Region 
		 * Takes in ArrayList<String> taskList to display User Tasks*/
		VBox taskSection = addTaskSection(taskList);
		defaultLayout.setCenter(taskSection);

		/** Formatting Right Region */
		Button btnRight = new Button("Calendar Widget Goes Here~");
		defaultLayout.setRight(btnRight);

		/** Setting Default Scene */
		Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		return defaultScene;
	}


	/** Add User Display and Input in Bottom Row
	 *
	 */
	private GridPane addDisplayAndInputSection(ArrayList<String> displayList) {
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		/** display user input */
		ObservableList<String> displayObsList = FXCollections.observableList(displayList);
		ListView<String> displayListView = new ListView<String>(displayObsList);
		displayListView.setItems(displayObsList);
		displayListView.setPrefSize(600, 250);
		displayListView.setEditable(false);
		displayListView.setCellFactory(TextFieldListCell.forListView());
		
		/** for user to key input */
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		//defining the user input field
		final TextField userInput = new TextField();
		userInput.setPrefColumnCount(10);
		userInput.getText();
		
		userInput.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	System.out.println("YAY ENTER WORKS!");
	            	if ((userInput.getText() != null && !userInput.getText().isEmpty())) {
	            		System.out.println("This is what you typed: " + userInput.getText());
	            	}
	            	userInput.clear();
	            }
	            
	        }
	    });
		
		grid.getChildren().add(userInput);
		
		grid.getRowConstraints().add(new RowConstraints(5)); // row 0 is 5 high
		grid.getRowConstraints().add(new RowConstraints(100)); // row 0 is 100 high
		grid.getRowConstraints().add(new RowConstraints(10)); // row 1 is 10 high
		grid.getRowConstraints().add(new RowConstraints(30)); // row 1 is 30 high

		GridPane.setConstraints(userCommandDisplayText, 0, 0); // column=0 row=0
		GridPane.setConstraints(displayListView, 0, 1); // column=0 row=1
		GridPane.setConstraints(userCommandInputText, 0, 2); // column=0 row=2
		GridPane.setConstraints(userInput, 0, 3);

		grid.getChildren().addAll(userCommandDisplayText, displayListView, userCommandInputText);

		return grid;
	}
	
	/** Add Task List in the Center */
	private VBox addTaskSection(ArrayList<String> taskList) {
		VBox taskBox = new VBox();
		taskBox.setPadding(new Insets(10, 12, 15, 12));
		taskBox.setSpacing(10);
		taskBox.setStyle("-fx-background-color: #FFFFFF;");

		ObservableList<String> taskObsList = FXCollections.observableList(taskList);
		ListView<String> taskListView = new ListView<String>(taskObsList);
		taskListView.setItems(taskObsList);

		taskListView.setPrefSize(70, 150);
		
		taskBox.getChildren().addAll(taskListView);
		
		return taskBox;
	}

	/** Add Command List in Left Column */
	public VBox addCommandSection(ArrayList<String> commandList) {
		VBox commandBox = new VBox();
		commandBox.setPadding(new Insets(5, 12, 15, 12));
		commandBox.setSpacing(10);
		commandBox.setStyle("-fx-background-color: #FFFFFF;");

		Text helpTextHeader = new Text("Help: ");

		ObservableList<String> commandObsList = FXCollections.observableList(commandList);
		ListView<String> commandListView = new ListView<String>(commandObsList);
		commandListView.setItems(commandObsList);

		commandListView.setPrefSize(70, 150);

		commandBox.getChildren().addAll(helpTextHeader, commandListView);

		return commandBox;
	}

	/** Add Welcome Message in Top Row */
	public HBox addWelcomeSection(Text welcomeText) {
		HBox welcomeBox = new HBox();
		welcomeBox.setPadding(new Insets(10, 12, 10, 12));
		welcomeBox.setSpacing(5);
		welcomeBox.setStyle("-fx-background-color: #336699;");

		// Ignore font style for now
		// welcomeText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		welcomeText.setFill(Color.WHITE);
		// welcomeText.setStroke(Color.web("#7080A0"));
		welcomeBox.getChildren().add(welcomeText);

		return welcomeBox;
	}
}