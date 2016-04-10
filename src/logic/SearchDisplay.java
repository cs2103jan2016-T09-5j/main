package logic;

import java.util.ArrayList;

//@@author Regine
/**
 * The SearchDisplay class is used to sort out search result 
 * and return the search result into an ArrayList before passing it 
 * to userinterface
 */

public class SearchDisplay{
	private static  ArrayList<String[]> searchArrListForGUI = new ArrayList<String[]> ();
		
	/**
	 * Internal logic to sort out search's ArrayList
	 * return true if the sorted list do not have sorting error and is not empty
	 * return false if the sorted list have sorting error or it is empty
	 */
	private static boolean sortSearchList() {
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		
		for(int i = 0; i < display.size(); i++) {
			combineArrList(display.get(i), searchArrListForGUI);
    	}
		 return true;
	}
	
	/**
	 * To combine all the sorted tasks 
	 */
	private static void combineArrList(ArrayList<String[]> displayAL, ArrayList<String[]> upComingAL) {
		for(int i=1 ; i < displayAL.size(); i++){
			upComingAL.add(displayAL.get(i));
	    }
	}
	
	/**
	 * For Controller class in userinterface package to get search ArrayList
	 */
	public static ArrayList<String[]> getSearchArrListForGUI() {
		sortSearchList();
		return searchArrListForGUI;
	}
	
	/**
	 * For Controller class to refresh/clear the search ArrayList
	 */
	public static void clearSearchArrListForGUI() {
		searchArrListForGUI.clear();
	}
	
	/****************PROCESS RAW DATA FROM SEARCHCOMMAND **********/
	/**
	 * Use to process the raw tasks from the DisplayCommand class into
	 * ArrayList<ArrayList<String[]>> which is (1)ArrayList of different date
	 * and (2)ArrayList of tasks for specific date (3)String[] of index[0] is
	 * the ID of the task, index[1] is the name of the task, index[2] is the
	 * time to time taken for the task, index[3] is the date of the task
	 */
	private static ArrayList<ArrayList<String[]>> taskListProcessor() {
     	ArrayList<ArrayList<String[]>> list = new ArrayList<ArrayList<String[]>> ();
     	clearSearchArrListForGUI();
     	
     	try {
     		String[] splitString = SearchCommand.getArrListForGUI().get(0).split("\\r?\\n");	
    		ArrayList<String[]> currList = null;
    		String currDate = "";
    		
    		for (int i = 0; i < splitString.length; i++) {
    			
    			if (splitString[i].length() == 0 || splitString[i].equals(System.getProperty("line.separator"))) { 
    				continue;
    		    }
    			
    			if (splitString[i].contains("..")) {
    				
    				if (currList != null && !currList.isEmpty()){
    					list.add(currList);
    				}
    				currList = new ArrayList<String[]> ();
    				String dateString = splitString[i];
    				dateString = dateString.replace("...", "");
    				dateString = dateString.replace("..", "");
    				currDate = dateString;
    				String[] inputDate = {dateString, "", ""};
    				currList.add(inputDate);
    				} else {
    					splitString[i].trim();
    					String[] split = splitString[i].split("~");
    					String[] splitTask = new String[4];
    					for (int j = 0; j < 3; j++) {
    						splitTask[j] = split[j];
    						splitTask[j] = splitTask[j].trim();
    					}
    					splitTask[3] = currDate;
    					currList.add(splitTask);
    				}
    		}
    		list.add(currList);
    		return list;
    	
     	}catch(Exception e){
    		return list;
    	}
	}
}