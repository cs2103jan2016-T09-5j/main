package logic;

import java.util.ArrayList;

public class SearchDisplay{
	private static  ArrayList<String[]> searchArrListForGUI = new ArrayList<String[]> ();
	private static boolean sortSearchList(){
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		for(int i=0; i<display.size(); i++){
			searchArrListForGUI.addAll(display.get(i));
    	}
		 return true;
	}
	public static ArrayList<String[]> getSearchArrListForGUI(){
		System.out.println(SearchCommand.getArrListForGUI().get(0));
		sortSearchList();
		return searchArrListForGUI;
	}
	public static void clearSearchArrListForGUI(){
		searchArrListForGUI.clear();
	}
	private static ArrayList<ArrayList<String[]>> taskListProcessor(){
     	ArrayList<ArrayList<String[]>> list = new ArrayList<ArrayList<String[]>>();
     	
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
 }
}
