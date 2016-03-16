package userinterface;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClockworkGUI extends Application {
	
	/** Application window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 900;
	private static final int DEFAULT_WINDOW_HEIGHT = 600;

	/** Static variables */
	private static ArrayList<String> consoleList = new ArrayList<String>();
//	private static ArrayList<String> helpList = new ArrayList<String>();
//	private static ArrayList<String> taskList = new ArrayList<String>();
	private static BorderPane defaultLayout = new BorderPane();
	
	private static Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	
//	private String rawInputString = new String();
	
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
	private static void setDisplayRegions(BorderPane defaultLayout) {
		setBottomRegion(defaultLayout);
		setTopRegion(defaultLayout);
		setLeftRegion(defaultLayout);
		setCenterRegion(defaultLayout);
		setRightRegion(defaultLayout);	
	}
	
	/** Set top region to display welcome text*/
	private static void setTopRegion(BorderPane defaultLayout) {
		TopDisplay topSection = new TopDisplay();
		defaultLayout.setTop(topSection);
//		Available Methods:
//		topSection.changeDisplayText("IF YOU WANNA CHANGE ME");
	}
	
	/** Set left region to display help list */
	private static void setLeftRegion(BorderPane defaultLayout) {
		LeftDisplay leftSection = new LeftDisplay(helpListTest);
		defaultLayout.setLeft(leftSection);
//		Available Methods:
//		ArrayList<String> testArray = new ArrayList(Arrays.asList("HAHAHAHA", "IT WORKS"));
//		leftSection.changeDisplayList(testArray);
	}
	
	/** Set center region to display task list */
	private static void setCenterRegion(BorderPane defaultLayout) {
		CenterDisplay centerSection = new CenterDisplay(taskListTest);
		defaultLayout.setCenter(centerSection);
//		Available Methods:
//		ArrayList<String> testArray = new ArrayList(Arrays.asList("HAHAHAHA", "IT WORKS"));
//		centerSection.changeDisplayList(testArray);
	}
	
	/** Set right region to display calendar widget [INCOMPLETE]*/
	private static void setRightRegion(BorderPane defaultLayout) {
		RightDisplay rightSection = new RightDisplay();
		defaultLayout.setRight(rightSection);
	}
	
	/** Set bottom region to display user command and input section*/
	private static void setBottomRegion(BorderPane defaultLayout) {
		BottomDisplay bottomSection = new BottomDisplay(consoleList);
		defaultLayout.setBottom(bottomSection);
	}
	
	/*
	* ===========================================
	* Protected Methods
	* ===========================================
	*/
	
	/** 
	 * Handle event after enter is pressed
	 * 
	 * @param textField				User input to be handled
	 */
	protected static void handleUserInput(TextField textField) {
		textField.setOnKeyPressed(new EventHandler<KeyEvent>()
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
	            	if ((textField.getText() != null && !textField.getText().isEmpty())) {
	            		handleUserEnter(textField.getText());
		            	textField.clear();
	            	}
	            } 
	        }
	    });
	}
	
	//*********** REGINE WORK ON THIS PART! ************************
	protected static void handleUserEnter(String userInput){
//		rawInputString = userInput;
		
		consoleList.add(userInput);
		
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
	protected static void refresh(){
		setDisplayRegions(defaultLayout);
	}

	/** 
	 * Set list format from ArrayList to ListView so that list can be seen on GUI
	 * 
	 * @param arrayList				List of type ArrayList String 
	 * @return listView				List of type ListView String 
	 */
	protected static ListView<String> formatArrayList(ArrayList<String> arrayList) {
		ObservableList<String> obsList = FXCollections.observableList(arrayList);
		ListView<String> listView = new ListView<String>(obsList);
		listView.setItems(obsList);
		return listView;
	}
}