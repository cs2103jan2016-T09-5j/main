package userinterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.UserInterfaceObject;

public class TaskListProcessor {
	
	private static ArrayList<UserInterfaceObject> todayObjects = new ArrayList<UserInterfaceObject>();
	private static ArrayList<UserInterfaceObject> tomorrowObjects = new ArrayList<UserInterfaceObject>();
	private static ArrayList<UserInterfaceObject> upcomingObjects = new ArrayList<UserInterfaceObject>();
	private static ArrayList<UserInterfaceObject> somedayObjects = new ArrayList<UserInterfaceObject>();
	
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
//    			System.out.println("Splitting [" + splitString[i] + "]");
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
	
	public static void getUIObjects (String bigString){
    	ArrayList<ArrayList<String[]>> list = getDisplayList(bigString);
    	
    	todayObjects.clear();
    	tomorrowObjects.clear();
    	upcomingObjects.clear();
    	somedayObjects.clear();
    	  	
    	DateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy");
    	Calendar calendar = Calendar.getInstance();
    	Date today = calendar.getTime();
    	calendar.add(Calendar.DAY_OF_YEAR, 1);
    	Date tomorrow = calendar.getTime();
    	String todayAsString = dateFormat.format(today);
    	String tomorrowAsString = dateFormat.format(tomorrow);
    	
//    	System.out.println("TODAY: " + todayAsString);
//    	System.out.println("TOMORROW: " + tomorrowAsString);
    	
    	for (int i = 0; i < list.size(); i++){
    		String taskDate = list.get(i).get(0)[0];
    		String cleanDate = taskDate.replaceAll("[.]","");
//    		System.out.println("Date: " + cleanDate);
    		if (todayAsString.equals(cleanDate)){
    			for (int j = 1; j < list.get(i).size(); j++){
    				String[] task = list.get(i).get(j);
    				UserInterfaceObject uiObj = new UserInterfaceObject(
    						task[0], task[1], task[2], "PLACEHOLDER", taskDate);
    				todayObjects.add(uiObj);
    			}	
    		} else if ((tomorrowAsString.equals(cleanDate))){
    			for (int j = 1; j < list.get(i).size(); j++){
    				String[] task = list.get(i).get(j);
    				UserInterfaceObject uiObj = new UserInterfaceObject(
    						task[0], task[1], task[2], "PLACEHOLDER", taskDate);
    				tomorrowObjects.add(uiObj);
    			}
    		} else if ("Anytime".equals(cleanDate)){
    			for (int j = 1; j < list.get(i).size(); j++){
    				String[] task = list.get(i).get(j);
    				UserInterfaceObject uiObj = new UserInterfaceObject(
    						task[0], task[1], task[2], "PLACEHOLDER", taskDate);
    				somedayObjects.add(uiObj);
    			}
    		} else {
    			for (int j = 1; j < list.get(i).size(); j++){
    				String[] task = list.get(i).get(j);
    				UserInterfaceObject uiObj = new UserInterfaceObject(
    						task[0], task[1], task[2], "PLACEHOLDER", taskDate);
    				upcomingObjects.add(uiObj);
    			}
    		}
    		
    	}
	}
	
	public static ArrayList<UserInterfaceObject> getTodayList(){
		return todayObjects;
	}
	
	public static ArrayList<UserInterfaceObject> getTomorrowList(){
		return tomorrowObjects;
	}
	
	public static ArrayList<UserInterfaceObject> getUpcomingList(){
		return upcomingObjects;
	}
	
	public static ArrayList<UserInterfaceObject> getSomedayList(){
		return somedayObjects;
	}
}
