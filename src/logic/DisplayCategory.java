package logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// @@author A0133247L
/**
 * The DisplayCategory Class is used to sort out display tasks for GUI 5
 * categories. Today , Tomorrow, Somedays, Upcoming and lastly, All tasks
 * 
 * This class is with DisplayCommand class (logic package) and Controller class
 * (userinterface package)
 */

public class DisplayCategory {

	/**
	 * ArrayList<String[]> sorted in to 5 categories: Today, Tomorrow, Someday,
	 * Upcoming, Command
	 * 
	 */
	private static ArrayList<String[]> todayArrListForGUI = new ArrayList<String[]>();
	private static ArrayList<String[]> tmrArrListForGUI = new ArrayList<String[]>();
	private static ArrayList<String[]> somedaysArrListForGUI = new ArrayList<String[]>();
	private static ArrayList<String[]> upcomingArrListForGUI = new ArrayList<String[]>();
	private static ArrayList<String[]> allTasksArrListForGUI = new ArrayList<String[]>();
	private static DateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");

	/**
	 * For the Today's date display on the top left corner
	 */
	public static String getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		String todayAsString = dateFormat.format(calendar.getTime());
		return todayAsString;
	}

	private static String getTmrDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		String tmrAsString = dateFormat.format(calendar.getTime());
		return tmrAsString;
	}

	/**
	 * For Controller class in userinterface package to get today's ArrayList
	 * for different categories
	 */
	public static ArrayList<String[]> getTodayArrListForGUI() {
		sortTodayList();
		return todayArrListForGUI;
	}
	
	/**
	 * For Controller class in userinterface package to get tomorrow's ArrayList
	 * for different categories
	 */
	public static ArrayList<String[]> geTmrArrListForGUI() {
		sortTmrList();
		return tmrArrListForGUI;
	}

	/**
	 * For Controller class in userinterface package to get Someday's ArrayList
	 * for different categories
	 */
	public static ArrayList<String[]> getSomedaysArrListForGUI() {
		sortSomedayList();
		return somedaysArrListForGUI;
	}

	/**
	 * For Controller class in userinterface package to get upcoming's ArrayList
	 * for different categories
	 */
	public static ArrayList<String[]> getUpcommingArrListForGUI() {
		sortUpcomingList();
		return upcomingArrListForGUI;
	}

	/**
	 * For Controller class in userinterface package to get allTasks' ArrayList
	 * for different categories
	 */
	public static ArrayList<String[]> getAllTasksArrListForGUI() {
		sortAllTasksList();
		return allTasksArrListForGUI;
	}

	/**
	 * For Controller class to refresh/clear the ArrayLists
	 */
	public static void clearArrListForGUI() {
		todayArrListForGUI.clear();
		tmrArrListForGUI.clear();
		somedaysArrListForGUI.clear();
		upcomingArrListForGUI.clear();
		allTasksArrListForGUI.clear();
	}

	/**
	 * Internal logic to sort out today's ArrayList
	 * return true if the sorted list do not have sorting error and is not empty
	 * return false if the sorted list have sorting error or it is empty
	 */
	private static boolean sortTodayList() {
		String todayAsString = getTodayDate();
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		todayArrListForGUI.clear();
		
		try {
			if (!display.isEmpty()) {
				for (int i = 0; i < display.size(); i++) {
					if (!display.get(i).isEmpty()) {
						if (display.get(i).get(0)[0].equals(todayAsString)) {
							todayArrListForGUI = display.get(i);
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Internal logic to sort out tomorrow's ArrayList
	 * return true if the sorted list do not have sorting error and is not empty
	 * return false if the sorted list have sorting error or it is empty
	 */
	private static boolean sortTmrList() {
		String tmrAsString = getTmrDate();
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		tmrArrListForGUI.clear();
		
		try {
			if (!display.isEmpty()) {
				for (int i = 0; i < display.size(); i++) {
					if (!display.get(i).isEmpty()) {
						if (display.get(i).get(0)[0].equals(tmrAsString)) {
							tmrArrListForGUI = display.get(i);
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Internal logic to sort out someday's ArrayList
	 * return true if the sorted list do not have sorting error and is not empty
	 * return false if the sorted list have sorting error or it is empty
	 */
	private static boolean sortSomedayList() {
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		somedaysArrListForGUI.clear();
		
		try {
			if (!display.isEmpty()) {
				for (int i = 0; i < display.size(); i++) {
					if (!display.get(i).isEmpty()) {
						if (display.get(i).get(0)[0].equals("Anytime")) {
							somedaysArrListForGUI = display.get(i);
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Internal logic to sort out upcoming's ArrayList
	 * return true if the sorted list do not have sorting error and is not empty
	 * return false if the sorted list have sorting error or it is empty
	 */
	private static boolean sortUpcomingList() {
		String todayAsString = getTodayDate();
		String tmrAsString = getTmrDate();
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		upcomingArrListForGUI.clear();
		
		try {
			if (!display.isEmpty()) {
				for (int i = 0; i < display.size(); i++) {
					if (!display.get(i).isEmpty()) {
						String str = display.get(i).get(1)[3];
						if (!str.equals("Anytime") && !str.equals(tmrAsString) 
								&& !str.equals(todayAsString)) {
							combineArrList(display.get(i), upcomingArrListForGUI);
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Internal logic to sort out allTasks's ArrayList
	 * return true if the sorted list do not have sorting error and is not empty
	 * return false if the sorted list have sorting error or it is empty
	 */
	private static boolean sortAllTasksList() {
		ArrayList<ArrayList<String[]>> display = taskListProcessor();
		allTasksArrListForGUI.clear();
		
		try {
			if (!display.isEmpty()) {
				for (int i = 0; i < display.size(); i++) {
					if (!display.get(i).isEmpty()) {
						combineArrList(display.get(i), allTasksArrListForGUI);
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * For sortUpcomingList and sortAllTasksList to combine all the sorted
	 * tasks. sortUpcomingList would combine date of tasks without today & tmr &
	 * anytime tasks sortAllTaskList would combine date of tasks with all the 4
	 * categories ( today & tmr & upcoming & anytime )
	 */
	private static void combineArrList(ArrayList<String[]> displayAL, ArrayList<String[]> upComingAL) {
		if (!displayAL.isEmpty()) {
			for (int i = 1; i < displayAL.size(); i++) {
				upComingAL.add(displayAL.get(i));
			}
		}
	}

	/****************PROCESS RAW DATA FROM DISPLAYCOMMAND **********/
	/**
	 * Use to process the raw tasks from the DisplayCommand class into
	 * ArrayList<ArrayList<String[]>> which is (1)ArrayList of different date
	 * and (2)ArrayList of tasks for specific date (3)String[] of index[0] is
	 * the ID of the task, index[1] is the name of the task, index[2] is the
	 * time to time taken for the task, index[3] is the date of the task
	 */
	private static ArrayList<ArrayList<String[]>> taskListProcessor() {
		ArrayList<ArrayList<String[]>> list = new ArrayList<ArrayList<String[]>>();
		
		try {
			String[] splitString = DisplayCommand.getArrListForGUI().get(0).split("\\r?\\n");
			ArrayList<String[]> currList = null;
			String currDate = "";
			
			for (int i = 0; i < splitString.length; i++) {
				
				if (splitString[i].length() == 0 
						|| splitString[i].equals(System.getProperty("line.separator"))) {
					continue;
				}
				
				if (splitString[i].contains("..")) {
					
					if (currList != null && !currList.isEmpty()) {
						list.add(currList);
					}
					currList = new ArrayList<String[]>();
					String dateString = splitString[i];
					dateString = dateString.replace("...", "");
					dateString = dateString.replace("..", "");
					dateString = dateString.replace(".", "");
					currDate = dateString;
					String[] inputDate = { dateString, "", "" };
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
		} catch (Exception e) {
			return list;
		}

	}
}
