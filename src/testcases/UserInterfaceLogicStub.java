package testcases;

import java.util.ArrayList;
import java.util.Arrays;

import userinterface.controller.ClockworkGUI;

public class UserInterfaceLogicStub {
	/** Test arrays from Logic API to display on GUI */
	private static final ArrayList<String> _helpListTest = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Search", "Display", "Mark", "Edit"));
	private static final ArrayList<String> _taskListTest = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));
	private static final ArrayList<String> _consoleListTest = new ArrayList<String>(
			Arrays.asList("add 2103", "display", "mark 23 "));
	
	
	/** 
	 * Uncomment in ClockworkGUIController.java to simulate logic after pressing enter 
	 * to display test ArrayLists on GUI.
	 * 
	 */
	public static void simulateLogic(){
		ClockworkGUI.setTaskList(_taskListTest);
		ClockworkGUI.setHelpList(_helpListTest);
		ClockworkGUI.setConsoleList(_consoleListTest);
		ClockworkGUI.updateDisplay();
	}
	
	/** Set all ArrayLists to test ArrayLists */
	public static void displayRandomArrayLists(){
		ClockworkGUI.setTaskList(_taskListTest);
		ClockworkGUI.setHelpList(_helpListTest);
		ClockworkGUI.setConsoleList(_consoleListTest);
	}
	
	// Getter methods to display individual test ArrayLists
	public static ArrayList<String> getHelpList(){
		return _helpListTest;
	}
	
	public static ArrayList<String> getTaskList(){
		return _taskListTest;
	}
	
	public static ArrayList<String> getConsoleList(){
		return _consoleListTest;
	}
}
