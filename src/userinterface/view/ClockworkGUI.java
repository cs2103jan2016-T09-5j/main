package userinterface.view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.ClockWork;
import storage.StorageUtils;
import testcases.UserInterfaceLogicStub;
import userinterface.controller.ClockworkGUIController;
import userinterface.model.*;

public class ClockworkGUI extends Application {
	
	/** Application window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 900;
	private static final int DEFAULT_WINDOW_HEIGHT = 600;

	private static BorderPane defaultLayout = new BorderPane();
	private static Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	
	/** Static variables */
	private static ArrayList<String> _consoleList = new ArrayList<String>();
	private static ArrayList<String> _taskList = new ArrayList<String>();
	private static ArrayList<String> _helpList = UserInterfaceLogicStub.getHelpList();

	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		initialiseLogicPackage(args);
		Application.launch(args);
	}

	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		
//		/** Uncomment to use LogicStub */
//		UserInterfaceLogicStub.displayRandomArrayLists();
		
		setDisplayRegions(defaultLayout);
		window.setScene(defaultScene);
		window.show();
	}
	
	/*
	* ===========================================
	* Initialise External Packages
	* ===========================================
	*/
	
	/** 
	 * Initialise logic package for GUI. Comment out in main and uncomment
	 * LogicStub methods in ClockworkGUI and ClockworkGUIController to separate 
	 * Logic and GUI packages.
	 */
	private static void initialiseLogicPackage(String[] args) {
		initialiseStorage(args);
		ClockworkGUIController.setLogic(ClockWork.getInstance());
	}
	
	/** Initialise storage paths in Logic Package*/
	private static void initialiseStorage(String[] args) {
		//Set storage file directory, link with storage file
		ClockWork.setFileDirectory(ClockWork.getStorageFileDirFromSettings());
		// Check if a file directory path is passed in through argument
		if (args.length == 1) {
			// Check if file directory path is valid
			String customFileDirPath = args[0];
			ClockWork.setFileDirectory(StorageUtils.processStorageDirectory(customFileDirPath));
		}
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
	}
	
	/** Set left region to display help list */
	private static void setLeftRegion(BorderPane defaultLayout) {
		LeftDisplay leftSection = new LeftDisplay(_helpList);
		defaultLayout.setLeft(leftSection);
	}
	
	/** Set center region to display console */
	private static void setCenterRegion(BorderPane defaultLayout) {
		CenterDisplay centerSection = new CenterDisplay(_consoleList);
		defaultLayout.setCenter(centerSection);
	}
	
	/** Set right region to display calendar widget [INCOMPLETE]*/
	private static void setRightRegion(BorderPane defaultLayout) {
		RightDisplay rightSection = new RightDisplay();
		defaultLayout.setRight(rightSection);
	}
	
	/** Set bottom region to display task list and input section*/
	private static void setBottomRegion(BorderPane defaultLayout) {
		BottomDisplay bottomSection = new BottomDisplay(_taskList);
		defaultLayout.setBottom(bottomSection);
	}
	
	/** Refreshes the scene so that updated lists and variables can be shown */
	public static void refresh(){
		setDisplayRegions(defaultLayout);
	}
	
	//Public getter and setter methods
	public static void setConsoleList(ArrayList<String> newConsoleList){
		_consoleList = newConsoleList;
	}
	
	public static void setHelpList(ArrayList<String> newHelpList){
		_helpList = newHelpList;
	}
	
	public static void setTaskList(ArrayList<String> newTaskList){
		_taskList = newTaskList;
	}
	
	public static void addToConsoleList(String consoleString){
		_consoleList.add(consoleString);
	}
	
//	public static ArrayList<String> getConsoleList(){
//		return _consoleList;
//	}
//	
//	public static ArrayList<String> getHelpList(){
//		return _helpList;
//	}
//	
//	public static ArrayList<String> getTaskList(){
//		return _taskList;
//	}
}