package userinterface;

import java.util.ArrayList;

public class UserInterfaceStub {
	public static ArrayList<String[]> populateList(ArrayList<String[]> list){
		
		list.clear();
		
		String[] indivTask = new String[4];
		
		indivTask[0] = "1.";
		indivTask[1] = "Running with Felicia";
		indivTask[2] = "17:00 - 19:00";
		indivTask[3] = "7 Apr 2016";
		
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		
		return list;
	}
	
	public static ArrayList<String> populateFeedbackList(){
		ArrayList<String> feedbackList = new ArrayList<String>();
		feedbackList.add(" ");
		feedbackList.add(" ");
		return feedbackList;
	}
}
