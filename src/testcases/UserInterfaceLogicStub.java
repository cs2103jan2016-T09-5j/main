package testcases;

import java.util.ArrayList;
import java.util.Arrays;

import userinterface.view.ClockworkGUI;

public class UserInterfaceLogicStub {
	/** Test arrays to directly display on GUI before key press */
	private static final ArrayList<String> _helpListTestBefore = new ArrayList<String>(
			Arrays.asList("Help?", "Nah you don't", "need me.", "yet :D"));
	private static final ArrayList<String> _taskListTestBefore = new ArrayList<String>(
			Arrays.asList("CAN YOU", "SEE ME?"));
	private static final ArrayList<String> _consoleListTestBefore = new ArrayList<String>(
			Arrays.asList("Now", "Press", "Enter!"));
	
	/** Test arrays from Logic API to display on GUI after typing something and
	 * pressing enter */
	private static final ArrayList<String> _helpListTestAfter = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Search", "Display", "Mark", "Edit"));
	private static final ArrayList<String> _taskListTestAfter = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));
	private static final ArrayList<String> _consoleListTestAfter = new ArrayList<String>(
			Arrays.asList("add 2103", "display", "mark 23 "));
	
	
	/** Uncomment in ClockworkGUI.java to directly show all test ArrayLists on GUI */
	public static void displayRandomArrayLists(){
		ClockworkGUI.setTaskList(_taskListTestBefore);
		ClockworkGUI.setHelpList(_helpListTestBefore);
		ClockworkGUI.setConsoleList(_consoleListTestBefore);
	}
	
	/** 
	 * Uncomment in ClockworkGUIController.java to simulate logic after pressing enter 
	 * to display test ArrayLists on GUI.
	 * 
	 */
	public static void simulateLogic(){
		ClockworkGUI.setTaskList(_taskListTestAfter);
		ClockworkGUI.setHelpList(_helpListTestAfter);
		ClockworkGUI.setConsoleList(_consoleListTestAfter);
		ClockworkGUI.refresh();
	}
	
	// Getter methods to display individual test ArrayLists
	public static ArrayList<String> getHelpList(){
		return _helpListTestAfter;
	}
	
	public static ArrayList<String> getTaskList(){
		return _taskListTestAfter;
	}
	
	public static ArrayList<String> getConsoleList(){
		return _consoleListTestAfter;
	}
}
