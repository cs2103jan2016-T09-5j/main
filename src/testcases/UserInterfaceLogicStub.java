package testcases;

import java.util.ArrayList;
import java.util.Arrays;

import userinterface.Main;

public class UserInterfaceLogicStub {
	/** Test arrays from Logic API to display on GUI */
	private static final ArrayList<String> _helpListTest = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Search", "Display", "Mark", "Edit"));
	private static final ArrayList<String> _taskListTest = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));
	private static final ArrayList<String> _taskListTestInitial = new ArrayList<String>(
			Arrays.asList("RAHRAH", "OLALA"));
	
	/** 
	 * Uncomment in ClockworkGUIController.java to simulate logic after pressing enter 
	 * to display test ArrayLists on GUI.
	 * 
	 */
	public static void simulateLogic(){
		Main.setTaskList(_taskListTest);
		Main.displayDefaultScene();
	}
	
	/** Set all ArrayLists to test ArrayLists */
	public static void displayRandomList(){
		Main.setTaskList(_taskListTestInitial);
		
	}
	
	public static ArrayList<String> getHelpList(){
		return _helpListTest;
	}
}
