package userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.*;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logic.ClashDetector;
import logic.ClockWork;
import logic.DisplayCategory;
import logic.DisplayCommand;
import logic.SearchCommand;
import logic.SearchDisplay;
import logic.SignalHandler;

//@@author A0133247L
/**
 * The Controller's class is a link between userinterface and logic package
 * The class would get the userinput from userinterface and pass it on to the logic package.
 * Next, the logic will pass back ArrayList for display. 
 * This controller class is also a link to the AgendaHelper by fecthing the details 
 * from memory package.       
 */

public class Controller {

	private static Logger _logger = java.util.logging.Logger.getLogger("ClockworkGUIController");
	private static String _currentUserInput;
	private static ClockWork _logic;
	
	private static ArrayList<String> _feedback = new ArrayList<String>(Arrays.asList(" ", " "));
	private static ArrayList<String[]> todayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _tomorrowList = new ArrayList<String[]>();
	private static ArrayList<String[]> _upcomingList = new ArrayList<String[]>();
	private static ArrayList<String[]> _somedayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _searchList = new ArrayList<String[]>();
	private static ArrayList<String[]> _powerList = new ArrayList<String[]>();
	
	/**
	 * Handle event after key is pressed
	 * 
	 * @param textField
	 *        User input to be handled
	 */
	public static void implementKeystrokeEvents(final TextField textField) {
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				executeKeyPress(textField, ke);
			}
		});
	}
	
	/**
	 * Function handles all keyboard presses accordingly
	 * 
	 * @param textField
	 *            User input to be handled
	 * @param ke
	 *            Key that is pressed
	 */
	public static void executeKeyPress(TextField textField, KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			_currentUserInput = textField.getText();
			if ((_currentUserInput != null && !_currentUserInput.isEmpty())) {
				processEnter(_currentUserInput);
				textField.clear();
			}
		} else if (ke.getCode().equals(KeyCode.F1)) {
			// HELP SCENE
			Main.displayHelpScene();
		} else if (ke.getCode().equals(KeyCode.F2)) {
			// DISPLAY CALENDAR
			Main.displayCalendarScene();
		} else if (ke.getCode().equals(KeyCode.F3)) {
			// DISPLAY SUMMARY SCENE
			Main.setNumToday(getNumTodayItems());
			Main.setNumTomorrow(getNumTomorrowItems());
			Main.setNumUpcoming(getNumUpcomingItems());
			Main.setNumSomeday(getNumSomedayItems());
			Main.displayCategoryScene();
		} else if (ke.getCode().equals(KeyCode.DELETE)) {
			// MINIMISE
			Main.minimise();
		}
	}
	
	/**
	 * This method is used to handle the linkage between logic and GUI.
	 * userInput is collected from the TextField and passed in on to logic.
	 * 
	 * Logic would return the information generated for display: 
	 * 1. number of items in each category, 2. tasks from each category 
	 * 3. Signal feedback from the logic (error/successful)  
	 */
	public static void processEnter(String userInput) {
		_logger.log(Level.INFO, "Calling logic to process keypress.");
		
		try {	
			String[] keyword = userInput.split(" ");
			
			if (keyword[0].equalsIgnoreCase("search")) {
				resetSearchLists();
				ClockWork.ClockworkLogicMain(userInput, _logic);
				_feedback = SignalHandler.getArrListForGUI();
				_searchList = SearchDisplay.getSearchArrListForGUI();
				//DISPLAY SEARCH LIST
				Main.setSearchList(_searchList);
				Main.displaySearchScene();
		
			} else if (keyword[0].equalsIgnoreCase("add") || keyword[0].equalsIgnoreCase("delete")
					|| keyword[0].equalsIgnoreCase("mark") || keyword[0].equalsIgnoreCase("exit")
					|| keyword[0].equalsIgnoreCase("redo") || keyword[0].equalsIgnoreCase("undo")
					|| keyword[0].equalsIgnoreCase("delete") || keyword[0].equalsIgnoreCase("edit")) {
				resetAllLists();
				ClockWork.ClockworkLogicMain(userInput, _logic);
				getFiveMainLists();
				//DISPLAY ALL TASKES WITH CHANGES
				processDefaultLogicToDisplay();
			
			} else if (keyword[0].equalsIgnoreCase("display")) {
				//DISPLAY ALL TASKES
				if ( keyword.length == 1 ) {
					resetDisplayLists();
					ClockWork.ClockworkLogicMain(userInput, _logic);
					getFiveMainLists();
					processDefaultLogicToDisplay();
				} else {
					prcoessErrorLogicToDisplay(userInput);
				}
			
			} else {
				prcoessErrorLogicToDisplay(userInput);
			}		
		
		} catch (Exception ex) {
			_logger.log(Level.WARNING, "Keypress detected, but failed to process.", ex);
		}
		_logger.log(Level.INFO, "Sucessfully called logic to process keypress.");
	}
	
	/**
	 * This is a method to get the Feedback from logic package 
	 */
	public static ArrayList<String> getFeedback() {
		resetFeedbackList();
		
		if (!SignalHandler.getArrListForGUI().isEmpty() 
				&& ClashDetector.getArrListForGUI().isEmpty()) {
			_feedback = SignalHandler.getArrListForGUI();
		} else if (!ClashDetector.getArrListForGUI().isEmpty()) {
			_feedback = ClashDetector.getArrListForGUI();
		}
		return _feedback;
	}
	
	/**
	 * Function handles all keyboard presses accordingly
	 * and change to the specific scene that is pressed
	 * 
	 * @param buttom
	 *            Buttons of the 4 categories: 
	 *            1. Today
	 *            2. Tomorrow
	 *            3. Upcoming
	 *            4. Somedays
	 * @param ke
	 *            Key that is pressed
	 */
	public static void redirectScene(Button button, String sceneName) {
		button.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				
				if (ke.getCode().equals(KeyCode.ENTER)) {
					ClockWork.ClockworkLogicMain("display", _logic);
					
					if (sceneName.equals("Today")) {
						todayList = DisplayCategory.getTodayArrListForGUI();
						Main.setTodayList(todayList);
						Main.displayTodayScene();
						Main.displayTodayScene();
					} else if (sceneName.equals("Tomorrow")) {				
						_tomorrowList = DisplayCategory.geTmrArrListForGUI();
						Main.setTomorrowList(_tomorrowList);
						Main.displayTomorrowScene();
					} else if (sceneName.equals("Upcoming")) {		
						_upcomingList = DisplayCategory.getUpcommingArrListForGUI();
						Main.setUpcomingList(_upcomingList);
						Main.displayUpcomingScene();
					} else if (sceneName.equals("Someday")) {
						_somedayList = DisplayCategory.getSomedaysArrListForGUI();
						Main.setSomedayList(_somedayList);
						Main.displaySomedayScene();
					} 
				} else if (ke.getCode().equals(KeyCode.F1)) {
					// HELP SCENE
					Main.displayHelpScene();
				} else if (ke.getCode().equals(KeyCode.F2)) {
					// DISPLAY CALENDAR
					Main.displayCalendarScene();
				} else if (ke.getCode().equals(KeyCode.DELETE)) {
					// MINIMISE
					Main.minimise();
				}
			}
		});
	}
	
	/**
	 * This method is to allow user to reset the feedback list 
	 * at initial launch, Main class
	 */
	protected static void resetFeedbackList() {
		_feedback = new ArrayList<String>();
		_feedback.add("");
		_feedback.add("");
	}
	
	/**
	 * To get the five main ArrayLists from logic package:
	 *            1. Today
	 *            2. Tomorrow
	 *            3. Upcoming
	 *            4. Somedays
	 *            5. AllTasks
	 */
	private static void getFiveMainLists() {
		todayList = DisplayCategory.getTodayArrListForGUI();
		_tomorrowList = DisplayCategory.geTmrArrListForGUI();
		_upcomingList = DisplayCategory.getUpcommingArrListForGUI();
		_somedayList = DisplayCategory.getSomedaysArrListForGUI();
		_powerList = DisplayCategory.getAllTasksArrListForGUI();
	}
	
	/**
	 * This method is a break down to process connection between userinput 
	 * and logic after key press in processEnter() method.
	 */
	private static void prcoessErrorLogicToDisplay(String userInput) {
		SignalHandler.clearArrListForGUI();
		ClashDetector.clearArrListForGUI();
		ClockWork.ClockworkLogicMain(userInput, _logic);
		processDefaultLogicToDisplay();
	}
	
	/**
	 * This method is a break down to process connection between userinput 
	 * and logic after key press in processEnter() method.
	 */
	private static void processDefaultLogicToDisplay() {
		Main.setPowerList(_powerList);
		Main.displayAllScene();
		
		Main.setNumToday(getNumTodayItems());
		Main.setNumTomorrow(getNumTomorrowItems());
		Main.setNumUpcoming(getNumUpcomingItems());
		Main.setNumSomeday(getNumSomedayItems());
	}
	
	/**
	 * Reset all the ArrayLists collected from logic output
	 */
	protected static void resetAllLists() {
		SignalHandler.clearArrListForGUI();
		DisplayCommand.clearArrListForGUI();
		SearchCommand.clearArrListForGUI();
		ClashDetector.clearArrListForGUI();
	    DisplayCategory.clearArrListForGUI();
	    SearchDisplay.clearSearchArrListForGUI();
	}
	
	/**
	 * Reset only those ArrayLists related to search classes 
	 * collected from logic output
	 */
	private static void resetSearchLists() {
		ClashDetector.clearArrListForGUI();
		SignalHandler.clearArrListForGUI();
		SearchCommand.clearArrListForGUI();
		SearchDisplay.clearSearchArrListForGUI();
	}
	
	/**
	 * Reset only those ArrayLists related to display classes 
	 * collected from logic output
	 */
	private static void resetDisplayLists() {
		ClashDetector.clearArrListForGUI();
		SignalHandler.clearArrListForGUI();
		DisplayCommand.clearArrListForGUI();
		DisplayCategory.clearArrListForGUI();
	}
	
	/**
	 * To get the number of items for today's task list
	 */
	public static int getNumTodayItems() {
		if (todayList.isEmpty()) {
			return 0;
		} else {
			return todayList.size()-1;
		}
	}
	
	/**
	 * To get the number of items for tomorrow's task list
	 */
	public static int getNumTomorrowItems() {
		if (_tomorrowList.isEmpty()) {
			return 0;
		} else {
			return _tomorrowList.size()-1;
		}
	}
	
	/**
	 * To get the number of items for upcoming's task list
	 */
	public static int getNumUpcomingItems() {
		if (_upcomingList.isEmpty()) {
			return 0;
		} else {
			return _upcomingList.size();
		}
	}
	
	/**
	 * To get the number of items for Someday's task list
	 */
	public static int getNumSomedayItems() {
		if (_somedayList.isEmpty()) {
			return 0;
		} else {
			return _somedayList.size()-1;
		}
	}
    
	/**
	 * To get UserInput as a string type
	 */
	public static String getCurrentUserInput() {
		return _currentUserInput;
	}
	
	public static void setLogic(ClockWork l) {
		_logic = l;
	}
	
	public static ClockWork getLogic() {
		return _logic;
	}
	
}
