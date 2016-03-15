package graphicUserInterface;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
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

public class ClockworkGUI extends Application {
	
	/** Application window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 900;
	private static final int DEFAULT_WINDOW_HEIGHT = 600;

	/** Static messages to display */
	private static final Text TEXT_WELCOME = new Text("Welcome to Clockwork (:");
	private static final Text TEXT_HELP = new Text("Help: ");
	private static final Text TEXT_CONSOLE = new Text("Console: ");
	private static final Text TEXT_INPUT = new Text("Command: ");
	
	/** Static variables */
	private static ArrayList<String> prevCommandsList = new ArrayList<String>();
	private static ArrayList<String> currentInputList = new ArrayList<String>();
	private static ArrayList<String> helpList = new ArrayList<String>();
	private static ArrayList<String> taskList = new ArrayList<String>();
	private static BorderPane defaultLayout = new BorderPane();
	private static Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	private String rawInputString = new String();
	
	/** Test arrays to display */
	private static final ArrayList<String> helpListTest = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Search", "Display", "Mark", "Edit"));
	private static final ArrayList<String> taskListTest = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));

	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		//Start Comment - tldr; getCurrentState which is what logic API is supposed to return 
		// to GUI, so I can unpack it to get the arrayLists I need to display.
		//Aka, taskList for now.
		//Cos the GUI is not supposed to have any memory storing 
		//arraylists or whatever cos that belongs to logic. GUI is only supposed to output
		//out whatever the logic API gives.
//		ClockWork clockWork = new ClockWork();
//		taskList = clockWork.getCurrentState();
		//End Comment
		Application.launch(args);
	}
	
	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		setDisplayRegions(defaultLayout);
		window.setScene(defaultScene);
		window.show();
	}
	
	/*
	* ===========================================
	* Creating Basic Layout
	* ===========================================
	*/
	
	/** Set top, left, center, right and bottom region to display information */
	private void setDisplayRegions(BorderPane defaultLayout) {
		setBottomRegion(defaultLayout);
		setTopRegion(defaultLayout);
		setLeftRegion(defaultLayout);
		setCenterRegion(defaultLayout);
		setRightRegion(defaultLayout);	
	}
	
	/** Set top region to display welcome text*/
	private void setTopRegion(BorderPane defaultLayout) {
		TopDisplay topSection = new TopDisplay(TEXT_WELCOME);
		defaultLayout.setTop(topSection);
//		Available Methods:
//		topSection.changeDisplayText("IF YOU WANNA CHANGE ME");
	}
	
	/** Set left region to display help list */
	private void setLeftRegion(BorderPane defaultLayout) {
		LeftDisplay leftSection = new LeftDisplay(TEXT_HELP, helpListTest);
		defaultLayout.setLeft(leftSection);
//		Available Methods:
//		ArrayList<String> testArray = new ArrayList(Arrays.asList("HAHAHAHA", "IT WORKS"));
//		leftSection.changeDisplayList(testArray);
	}
	
	/** Set center region to display task list */
	private void setCenterRegion(BorderPane defaultLayout) {
		VBox centerSection = displayTaskList(taskListTest);
		defaultLayout.setCenter(centerSection);
	}
	
	/** Set right region to display calendar widget [INCOMPLETE]*/
	private void setRightRegion(BorderPane defaultLayout) {
		Button rightSection = new Button("INSERT CALENDAR WIDGET");
		defaultLayout.setRight(rightSection);
	}
	
	/** Set bottom region to display user command and input section*/
	private void setBottomRegion(BorderPane defaultLayout) {
		GridPane bottomSection = setBottomSection(prevCommandsList);
		defaultLayout.setBottom(bottomSection);
	}
	
	/*
	* ===========================================
	* Private Class Methods
	* ===========================================
	*/
	
	/** 
	 * Display previous commands entered by user in top subsection, and take in input
	 * in bottom subsection in the bottom section
	 * 
	 * @param prevCommandsList		Previous test commands entered to show top sub-section
	 * 								in bottom section
	 * @return bottomSection		Bottom section to display previous commands and 
	 * 								for user to input text
	 */
	private GridPane setBottomSection(ArrayList<String> prevCommandsList) {
		GridPane grid = new GridPane();
		formatBottomSection(grid);
		
		ListView<String> prevCommandsListView = setTopSubsection(prevCommandsList);
		final TextField userInput = setBottomSubsection();
		
		grid.getChildren().add(userInput);
		grid.getChildren().addAll(TEXT_CONSOLE, prevCommandsListView, TEXT_INPUT);
		
		return grid;
	}
	
	/** 
	 * Display task list in center section
	 * 
	 * @param taskList				Task list to show in center section
	 * @return centerSection		Left section to display task list
	 */
	private VBox displayTaskList(ArrayList<String> taskList) {
		VBox centerSection = new VBox();
		
		ListView<String> taskListView = formatArrayList(taskList);
		
		styleCenterSection(centerSection, taskListView);
		
		centerSection.getChildren().addAll(taskListView);
		return centerSection;
	}
	
	/** 
	 * Display previous commands entered by user in top subsection in bottom section
	 * 
	 * @param prevCommandsList		Previous user commands for display in ArrayList
	 * @return prevCommandsListView	Previous user commands for display in ListView
	 */
	private ListView<String> setTopSubsection(ArrayList<String> prevCommandsList) {
		ListView<String> prevCommandsListView = formatArrayList(prevCommandsList);
		prevCommandsListView.setPrefSize(900, 250);
		prevCommandsListView.setCellFactory(TextFieldListCell.forListView());
		
		GridPane.setConstraints(TEXT_CONSOLE, 0, 0); // column=0 row=0
		GridPane.setConstraints(prevCommandsListView, 0, 1); // column=0 row=1
		
		return prevCommandsListView;
	}
	
	/** 
	 * Input user command in bottom subsection in bottom section
	 * 
	 * @return TextField			Text field for user to input commands
	 */
	private TextField setBottomSubsection() {
		final TextField userInput = new TextField();
		userInput.getText();	
		handleUserInput(userInput);
		
		GridPane.setConstraints(TEXT_INPUT, 0, 2); // column=0 row=2
		GridPane.setConstraints(userInput, 0, 3); // column=0 row=3
		
		return userInput;
	}
	
	/** 
	 * Handle event after enter is pressed
	 * 
	 * @param userInput				User input to be handled
	 */
	private void handleUserInput(TextField userInput) {
		userInput.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	        	//IGNORE FOR NOW - You can try seeing how this part works, not sure if you'll need
	        	//it for the function to see if the user input matches the help list so you can
	        	//change it later
//	        	currentInputList.add(userInput.getText());
//	        	System.out.println(currentInputList);
//	        	System.out.println(userInput.getText());
	        	//End Comment
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	if ((userInput.getText() != null && !userInput.getText().isEmpty())) {
	            		handleUserEnter(userInput.getText());
		            	userInput.clear();
	            	}
	            } 
	        }
	    });
	}
	
	//*********** REGINE WORK ON THIS PART! ************************
	private void handleUserEnter(String userInput){
		rawInputString = userInput;
		prevCommandsList.add(userInput);
		//Start Comment - Call Logic API here to handle the String user entered.
		//Eg. ('Cos idk the actual API you're gonna call here)
//		clockWork.handleCommand(rawInputString);
		//, where handleCommand is the logic API that processes the string and returns me 
		//the arraylist of tasks I should display on the GUI. That means that
		//the prevCommandsList should not be stored in the GUI, but it should be in logic.
		taskListTest.add("NEW TASK!");
		//End Comment
		refresh();
		System.out.println("This is what you typed: " + userInput);
	}
	/** Refreshes the scene so that updated lists and variables can be shown */
	private void refresh(){
		setDisplayRegions(defaultLayout);
	}

	/** 
	 * Set list format from ArrayList to ListView so that list can be seen on GUI
	 * 
	 * @param arrayList				List of type ArrayList String 
	 * @return listView				List of type ListView String 
	 */
	private ListView<String> formatArrayList(ArrayList<String> arrayList) {
		ObservableList<String> obsList = FXCollections.observableList(arrayList);
		ListView<String> listView = new ListView<String>(obsList);
		listView.setItems(obsList);
		return listView;
	}
	
	/** 
	 * Format grid section in bottom section for displaying previous user commands 
	 * and user input
	 * 
	 * @param grid					Grid in bottom section to be formatted
	 */
	private void formatBottomSection(GridPane grid) {
		/** to display previous commands */
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		/** for user to key input */
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		grid.getRowConstraints().add(new RowConstraints(10)); // row 0 is 10 high
		grid.getRowConstraints().add(new RowConstraints(280)); // row 0 is 280 high
		grid.getRowConstraints().add(new RowConstraints(10)); // row 1 is 10 high
		grid.getRowConstraints().add(new RowConstraints(30)); // row 1 is 30 high
	}
	
	//STYLING SECTIONS
	
	/** Styling center section containing task list */
	private void styleCenterSection(VBox centerSection, ListView<String> taskListView) {
		centerSection.setPadding(new Insets(10, 12, 15, 12));
		centerSection.setSpacing(10);
		centerSection.setStyle("-fx-background-color: #FFFFFF;");
		taskListView.setPrefSize(100, 200);
	}
}