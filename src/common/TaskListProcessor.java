package common;

import java.util.ArrayList;

public class TaskListProcessor {
	private static ArrayList<ArrayList<String[]>> getDisplayList(String bigString) {
    	ArrayList<ArrayList<String[]>> list = new ArrayList<ArrayList<String[]>>();
    	
    	String[] splitString = bigString.split("\\r?\\n");

    	ArrayList<String[]> currList = null;
    	for (int i = 0; i < splitString.length; i++){
    		if (splitString[i].length() == 0 || splitString[i].equals(System.getProperty("line.separator"))){ 
    			continue;
    		}
    		if (splitString[i].contains("..")){
    			if (currList != null && !currList.isEmpty()) list.add(currList);
    			currList = new ArrayList<String[]>();
    			String dateString = splitString[i];
    			String[] inputDate = {dateString};
    			currList.add(inputDate);
    		} else {
    			splitString[i].trim();
    			System.out.println("Splitting [" + splitString[i] + "]");
    			String[] splitTask = splitString[i].split("~");
    			for (int j = 0; j < splitTask.length; j++){
    				splitTask[j] = splitTask[j].trim();
    			}
    			currList.add(splitTask);
    		}
    	}
    	list.add(currList);
  
    	return list;
    }
	
	public static ArrayList<UserInterfaceObject> getUIObjects (String bigString){
    	ArrayList<ArrayList<String[]>> list = getDisplayList(bigString);

    	ArrayList<UserInterfaceObject> uiObjects = new ArrayList<UserInterfaceObject> ();
    	for (int i = 0; i < list.size(); i++){
    		System.out.println("Date: " + list.get(i).get(0)[0]);
    		String date = list.get(i).get(0)[0];
    		for (int j = 1; j < list.get(i).size(); j++){
    				String[] task = list.get(i).get(j);
    				UserInterfaceObject uiObj = new UserInterfaceObject(
    						task[0], task[1], task[2], "PLACEHOLDER", date);
    				uiObjects.add(uiObj);
    		}
    	}
    	
    	return uiObjects;
	}
}
