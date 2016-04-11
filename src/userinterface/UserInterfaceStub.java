package userinterface;

import java.util.ArrayList;

//@@author A0129833Y
/**
* The UserInterfaceStub class is used in place of the Logic and Storage component and is mostly 
* used while testing the GUI to check if the correct information is displayed on GUI.
*/
public class UserInterfaceStub {
	/** Populates the task with placeholder task information */
	public static ArrayList<String[]> populateList(ArrayList<String[]> taskList){
		taskList.clear();
		
		String[] indivTask = new String[4];
		
		indivTask[0] = "1.";
		indivTask[1] = "Running with Felicia";
		indivTask[2] = "17:00 - 19:00";
		indivTask[3] = "7 Apr 2016";
		
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		taskList.add(indivTask);
		
		return taskList;
	}
	
	/** Populates the feedback list with placeholder text and type */
	public static ArrayList<String> populateFeedbackList(){
		ArrayList<String> feedbackList = new ArrayList<String>();
		feedbackList.add("Added");
		feedbackList.add("Added Run with Felicia");
		return feedbackList;
	}
}
