package logic;

import java.util.ArrayList;

public class SearchDisplay{
	private static  ArrayList<String[]> searchArrListForGUI = new ArrayList<String[]> ();
	
	//@@author Regine
	private static boolean sortSearchList(){
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		for(int i=0; i<display.size(); i++){
			combineArrList(display.get(i), searchArrListForGUI);
    	}
		 return true;
	}
	private static void combineArrList(ArrayList<String[]> displayAL, ArrayList<String[]> upComingAL){
	    	for(int i=1 ; i < displayAL.size(); i++){
	    			upComingAL.add(displayAL.get(i));
	    			for(int j=0; j < displayAL.get(i).length;j++){
	    		//	System.out.println(displayAL.get(i)[j]);
	    			}
	    }
	}
	public static ArrayList<String[]> getSearchArrListForGUI(){
	//	System.out.println(SearchCommand.getArrListForGUI().get(0));
		sortSearchList();
		return searchArrListForGUI;
	}
	public static void clearSearchArrListForGUI(){
		searchArrListForGUI.clear();
	}
	private static ArrayList<ArrayList<String[]>> taskListProcessor(){
     	ArrayList<ArrayList<String[]>> list = new ArrayList<ArrayList<String[]>>();
     	clearSearchArrListForGUI();
     	try{
     		String[] splitString = SearchCommand.getArrListForGUI().get(0).split("\\r?\\n");	
    		ArrayList<String[]> currList = null;
    		String currDate = "";
    		for (int i = 0; i < splitString.length; i++){
    			if (splitString[i].length() == 0 || splitString[i].equals(System.getProperty("line.separator"))){ 
    				continue;
    		    }
    			if (splitString[i].contains("..")){
    				if (currList != null && !currList.isEmpty()) list.add(currList);
    				currList = new ArrayList<String[]>();
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