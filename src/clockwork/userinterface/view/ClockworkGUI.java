package clockwork.userinterface.view;

import java.util.ArrayList;
import java.util.Arrays;

import clockwork.logic.ClockWork;
import clockwork.storage.StorageUtils;
import clockwork.userinterface.controller.ClockworkGUIController;
import clockwork.userinterface.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClockworkGUI extends Application {
	
	/** Application window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 900;
	private static final int DEFAULT_WINDOW_HEIGHT = 600;

	/** Static variables */
	public static ArrayList<String> consoleList = new ArrayList<String>();
//	private static ArrayList<String> helpList = new ArrayList<String>();
//	private static ArrayList<String> taskList = new ArrayList<String>();
	private static BorderPane defaultLayout = new BorderPane();
	
	private static Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	
//	private String rawInputString = new String();
	
	/** Test arrays to display */
	public static final ArrayList<String> helpListTest = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Search", "Display", "Mark", "Edit"));
	public static final ArrayList<String> taskListTest = new ArrayList<String>(
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
		//Set storage file directory, link with storage file
		ClockWork.setFileDirectory(ClockWork.getStorageFileDirFromSettings());
		// Check if a file directory path is passed in through argument
		if (args.length == 1) {
			// Check if file directory path is valid
			String customFileDirPath = args[0];
			ClockWork.setFileDirectory(StorageUtils.processStorageDirectory(customFileDirPath));
		}
		ClockworkGUIController.logic = ClockWork.getInstance();
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
	
	/** Refreshes the scene so that updated lists and variables can be shown */
	public static void refresh(){
		setDisplayRegions(defaultLayout);
	}
}