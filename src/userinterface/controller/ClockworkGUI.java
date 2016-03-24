package userinterface.controller;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.ClockWork;
import storage.StorageUtils;
import testcases.UserInterfaceLogicStub;
import userinterface.view.ClockworkGUILayout;

public class ClockworkGUI extends Application {
	
	/** Application window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 900;
	private static final int DEFAULT_WINDOW_HEIGHT = 600;

	/** Static variables */
	private static ArrayList<String> _consoleList = new ArrayList<String>();
	private static ArrayList<String> _taskList = new ArrayList<String>();
	private static ArrayList<String> _helpList = UserInterfaceLogicStub.getHelpList();

	private static ClockworkGUILayout defaultLayout; 
	private static Scene defaultScene;
	private static boolean _minimiseFlag = false; 
	
	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		initialiseGUIPackage();
		initialiseLogicPackage(args);
		Application.launch(args);
	}
	
	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		window.setScene(defaultScene);
		/** START CHANGE */
		window.setResizable(false);
		/** END CHANGE */
		window.show();
	}
	
	/*
	* ===========================================
	* Initialise Packages
	* ===========================================
	*/
	
	/** Initialise GUI layout and scene in GUI package */
	public static void initialiseGUIPackage(){
		defaultLayout = new ClockworkGUILayout(_consoleList, _taskList, _helpList);
		defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	}
	
	/** 
	 * Initialise logic package for GUI. Comment out in main(String[] args)
	 * and uncomment UserInterfaceLogicStub method in ClockworkGUIController to test 
	 * GUI with UserInterfaceLogicStub.
	 */
	private static void initialiseLogicPackage(String[] args) {
		initialiseStorage(args);
		ClockworkGUIController.setLogic(ClockWork.getInstance());
	}
	
	/** Initialise storage paths in logic package*/
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
	* Public Methods
	* ===========================================
	*/
	
	/** START CHANGE */
	public static void minimise(Stage window){
	}
	
	public static void expand(){
	}
	
	public static void setMinimiseFlag(boolean flag){
		_minimiseFlag = flag;
	}
	
	public static void setConsoleTaskList(ArrayList<String> newConsoleList, ArrayList<String> newTaskList){
		_consoleList = newConsoleList;
		_taskList = newTaskList;
	}
	/** END CHANGE */
	
	public static void setConsoleList(ArrayList<String> newConsoleList){
		_consoleList = newConsoleList;
	}
	
	public static void setHelpList(ArrayList<String> newHelpList){
		_helpList = newHelpList;
	}
	
	public static void setTaskList(ArrayList<String> newTaskList){
		_taskList = newTaskList;
	}
	
	public static void updateDisplay(){
		defaultLayout.refresh(_consoleList, _taskList, _helpList);
	}
	
	public static void addToConsoleList(String consoleString){
		_consoleList.add(consoleString);
	}
	
	public static ArrayList<String> getConsoleList(){
		return _consoleList;
	}
	
	public static ArrayList<String> getHelpList(){
		return _helpList;
	}
	
	public static ArrayList<String> getTaskList(){
		return _taskList;
	}
}