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
//		LogicMain clockWork = new LogicMain();
//		packagedDisplayObj = clockWork.getCurrentState();
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
	* Populating Layout
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
	
	/** Set center region to display console */
	private static void setCenterRegion(BorderPane defaultLayout) {
		CenterDisplay centerSection = new CenterDisplay(consoleList);
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
	
	/** Set bottom region to display task list and input section*/
	private static void setBottomRegion(BorderPane defaultLayout) {
		BottomDisplay bottomSection = new BottomDisplay(taskListTest);
		defaultLayout.setBottom(bottomSection);
	}
	
	/*
	* ===========================================
	* Protected Methods
	* ===========================================
	*/
	
	/** 
	 * Handle event after key is pressed
	 * 
	 * @param textField				User input to be handled
	 */
	protected static void implementKeystrokeEvents(TextField textField) {
		textField.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            executeKeyPress(textField, ke); 
	        }
	    });
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
	
	/*
	* ===========================================
	* Private Methods
	* ===========================================
	*/
	
	/**
	 * Function handles all keyboard presses accordingly
	 * 
	 * @param textField				User input to be handled
	 * @param ke					Key that is pressed
	 */
	private static void executeKeyPress(TextField textField, KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER))
        {
        	if ((textField.getText() != null && !textField.getText().isEmpty())) {
        		processUserEnter(textField.getText());
            	textField.clear();
        	}
        } else if (ke.getCode().equals(KeyCode.ESCAPE)){
        	//DO SOMETHING [INCOMPLETE]
        }
	}
	
	private static void processUserEnter(String userInput){
		callControllerToAddCommand(userInput);
	}

	//*********** LOGIC INTEGRATION HERE ************************
	private static void callControllerToAddCommand(String userInput) {
		//Call Logic API Here
		consoleList.add(userInput);
		taskListTest.add("NEW TASK!");
		refresh();
		System.out.println("This is what you typed: " + userInput);
	}
	
	/** Refreshes the scene so that updated lists and variables can be shown */
	private static void refresh(){
		setDisplayRegions(defaultLayout);
	}

}