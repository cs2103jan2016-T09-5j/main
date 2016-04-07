package logic;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DisplayCategory {
    
      //For GUI display
    private static  ArrayList<String[]> todayArrListForGUI = new ArrayList<String[]> ();
    private static  ArrayList<String[]> tmrArrListForGUI = new ArrayList<String[]> ();
    private static  ArrayList<String[]> somedaysArrListForGUI = new ArrayList<String[]> ();
    private static  ArrayList<String[]> upcommingArrListForGUI = new ArrayList<String[]> ();
    private static  ArrayList<String[]> commandArrListForGUI = new ArrayList<String[]> ();
	private static Calendar calendar = Calendar.getInstance();
	private static DateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
	private static Date today; 
	
    /**
	 * ArrayList<String[]> sorted in to 5 categories:
	 * Today, Tomorrow, Somedays, Upcoming, Command
	 * 
	 */ 
	public static ArrayList<String[]> getTodayArrListForGUI(){
    	sortTodayList();
		return todayArrListForGUI;
    }
	public static ArrayList<String[]> geTmrArrListForGUI(){
		sortTmrList();
    	return tmrArrListForGUI;
    }
	public static ArrayList<String[]> getSomedaysArrListForGUI(){
		sortSomedayList();
    	return somedaysArrListForGUI;
    }
	public static ArrayList<String[]> getUpcommingArrListForGUI(){
		sortUpcommingList();
    	return upcommingArrListForGUI;
    }
	public static ArrayList<String[]> getCommandArrListForGUI(){
		sortCommandList();
    	return commandArrListForGUI;
    }
    public static void clearArrListForGUI(){
    	todayArrListForGUI.clear();
    	tmrArrListForGUI.clear();
    	somedaysArrListForGUI.clear();
    	upcommingArrListForGUI.clear();
    	commandArrListForGUI.clear();
    }
	private static boolean sortTodayList(){
		today = calendar.getTime();
    	String todayAsString = dateFormat.format(today);
    	ArrayList<ArrayList<String[]>> display = taskListProcessor();
    	
    	for(int i=0; i<display.size(); i++){
    		if(display.get(i).get(0)[0].equals(todayAsString)){
    			todayArrListForGUI = display.get(i);
    			return true;
    		}
    	}
    	return false;
    }
    private static boolean sortTmrList(){
    	today = calendar.getTime();
    	calendar.add(Calendar.DAY_OF_YEAR,1);
    	Date tmr = calendar.getTime();
    	String tmrAsString = dateFormat.format(tmr);
    	ArrayList<ArrayList<String[]>> display = taskListProcessor();
    	for(int i=0; i<display.size(); i++){
    		if(display.get(i).get(0)[0].equals(tmrAsString)){
    			tmrArrListForGUI = display.get(i);
    			return true;
    		}
    	}
    	return false;
    }
    private static boolean sortSomedayList(){
    	ArrayList<ArrayList<String[]>> display = taskListProcessor();
    	for(int i=0; i<display.size(); i++){
    		if(display.get(i).get(0)[0].equals("Anytime.")){
    			somedaysArrListForGUI = display.get(i);
    			return true;
    		}
    	}
    	return false;
    }
    private static boolean sortUpcommingList(){
    	today = calendar.getTime();
    	ArrayList<ArrayList<String[]>> display = taskListProcessor();
    	calendar.add(Calendar.DAY_OF_YEAR,1);
    	Date tmr = calendar.getTime();
    	upcommingArrListForGUI.clear();
    	for(int i=0; i<display.size(); i++){
    		String str = display.get(i).get(1)[3];
    		if(!str.equals("Anytime.") && !str.equals(dateFormat.format(tmr))
    				&& !str.equals(dateFormat.format(today)) && !str.equals("Thu 07 Apr 2016")){
    			combineArrList(display.get(i),upcommingArrListForGUI);
    		}
    	}
    	return true;
    }
    private static boolean sortCommandList(){
    	ArrayList<ArrayList<String[]>> display = taskListProcessor();
    	for(int i=0; i<display.size(); i++){
    		combineArrList(display.get(i),commandArrListForGUI);
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
    private static ArrayList<ArrayList<String[]>> taskListProcessor(){
        	ArrayList<ArrayList<String[]>> list = new ArrayList<ArrayList<String[]>>();
        	try{
        		String[] splitString = DisplayCommand.getArrListForGUI().get(0).split("\\r?\\n");
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