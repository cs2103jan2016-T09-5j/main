package userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
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

public class Controller {

	private static Logger _logger = java.util.logging.Logger.getLogger("ClockworkGUIController");
	private static String _currentUserInput;
	private static ArrayList<String> _feedback = new ArrayList<String>(Arrays.asList(" ", " "));
	private static ClockWork _logic;
	private static ArrayList<String[]> todayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _tomorrowList = new ArrayList<String[]>();
	private static ArrayList<String[]> _upcomingList = new ArrayList<String[]>();
	private static ArrayList<String[]> _somedayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _searchList = new ArrayList<String[]>();
	private static ArrayList<String[]> _powerList = new ArrayList<String[]>();
	private static TableView currentTableView;
	
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
		} else if (ke.getCode().equals(KeyCode.ESCAPE)) {
			// DISPLAY SUMMARY SCENE
			Main.setNumToday(getNumTodayItems());
			Main.setNumTomorrow(getNumTomorrowItems());
			Main.setNumUpcoming(getNumUpcomingItems());
			Main.setNumSomeday(getNumSomedayItems());
			Main.displaySummaryScene();
		} else if (ke.getCode().equals(KeyCode.F1)){
			// HELP SCENE
			Main.displayHelpScene();
		} else if (ke.getCode().equals(KeyCode.F2)) {
			// DISPLAY CALENDAR
			Main.displayCalendarScene();
		} else if (ke.getCode().equals(KeyCode.F3)) {
			// MINIMISE
			Main.minimise();
		} 
	}

	public static void processEnter(String userInput) {
		_logger.log(Level.INFO, "Calling logic to process keypress.");
		try {
			
			String[] keyword = userInput.split(" ");
			if (keyword[0].equalsIgnoreCase("search")){
				resetLists();
				ClockWork.ClockworkLogicMain(userInput, _logic);
					_feedback = SignalHandler.getArrListForGUI();
					_searchList = SearchDisplay.getSearchArrListForGUI();
					System.out.println(_searchList.get(0)[3]);
					Main.setSearchList(_searchList);
					Main.displaySearchScene();
			} else {
				resetLists();
				ClockWork.ClockworkLogicMain(userInput, _logic);
				
				_powerList = DisplayCategory.getCommandArrListForGUI();
				Main.setPowerList(_powerList);
				Main.displayAllScene();
//				// FEEDBACK STRING
//				if (!SignalHandler.getArrListForGUI().isEmpty()){
//					System.out.println("SIGNAL HANDLER: " + SignalHandler.getArrListForGUI().get(0));
//				}
//				//CLASH DETECTOR STRING
//				if (!ClashDetector.getArrListForGUI().isEmpty()){
//					System.out.println("CLASH DETECTOR: " + ClashDetector.getArrListForGUI().get(0));
//				}
//				
//				//SHOWS SEARCH STRINGS
//				if (!SearchCommand.getArrListForGUI().isEmpty()){
//					System.out.println("SEARCH COMMAND: " + SearchCommand.getArrListForGUI().get(0));
//				}
//				
//				if (!DisplayCommand.getArrListForGUI().isEmpty()){			
//					Main.setNumToday(getNumTodayItems());
//					Main.setNumTomorrow(getNumTomorrowItems());
//					Main.setNumUpcoming(getNumUpcomingItems());
//					Main.setNumSomeday(getNumSomedayItems());
//				}
				
				Main.setNumToday(getNumTodayItems());
				Main.setNumTomorrow(getNumTomorrowItems());
				Main.setNumUpcoming(getNumUpcomingItems());
				Main.setNumSomeday(getNumSomedayItems());
			
				Main.displayAllScene();
			}
		
		} catch (Exception ex) {
			_logger.log(Level.WARNING, "Keypress detected, but failed to process.", ex);
		}
		_logger.log(Level.INFO, "Sucessfully called logic to process keypress.");
	}
	
	//Get Logic to give you the feedback list here
	public static ArrayList<String> getFeedback() {
		_feedback = UserInterfaceStub.populateFeedbackList();
		if (!SignalHandler.getArrListForGUI().isEmpty()){
			_feedback = SignalHandler.getArrListForGUI();
		}
		return _feedback;
	}
	
	public static void redirectScene(Button button, String sceneName) {
		button.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					ClockWork.ClockworkLogicMain("display", _logic);
//					ArrayList<String[]> a = DisplayCategory.getTodayArrListForGUI();
//					System.out.println(a.get(1)[0]);
					if (sceneName.equals("Today")){
						todayList = DisplayCategory.getTodayArrListForGUI();
						//UserInterfaceStub.populateList(_todayList);
						Main.setTodayList(todayList);
						currentTableView = Main.displayTodayScene();
						Main.displayTodayScene();
					} else if (sceneName.equals("Tomorrow")){				
						_tomorrowList = DisplayCategory.geTmrArrListForGUI();
						Main.setTomorrowList(_tomorrowList);
						Main.displayTomorrowScene();
					} else if (sceneName.equals("Upcoming")){		
						_upcomingList = DisplayCategory.getUpcommingArrListForGUI();
						Main.setUpcomingList(_upcomingList);
						Main.displayUpcomingScene();
					} else if (sceneName.equals("Someday")){
						_somedayList = DisplayCategory.getSomedaysArrListForGUI();
						Main.setSomedayList(_somedayList);
						Main.displaySomedayScene();
					} 
				} 	else if (ke.getCode().equals(KeyCode.F1)){
					// HELP SCENE
					Main.displayHelpScene();
				} else if (ke.getCode().equals(KeyCode.F2)) {
					// DISPLAY CALENDAR
					Main.displayCalendarScene();
				} else if (ke.getCode().equals(KeyCode.F3)) {
					// MINIMISE
					Main.minimise();
				} else if (ke.getCode().equals(KeyCode.UP)){
					
				}
			}
		});
	}
	
	public static void resetLists(){
		SignalHandler.clearArrListForGUI();
		DisplayCommand.clearArrListForGUI();
		SearchCommand.clearArrListForGUI();
		ClashDetector.clearArrListForGUI();
	    DisplayCategory.clearArrListForGUI();
	    SearchDisplay.clearSearchArrListForGUI();
	}
			
	public static int getNumTodayItems(){
		if (todayList.isEmpty()){
			return 0;
		} else {
			return todayList.size()-1;
		}
	}
	
	public static int getNumTomorrowItems(){
		if (_tomorrowList.isEmpty()){
			return 0;
		} else {
			return _tomorrowList.size()-1;
		}
	}
	
	public static int getNumUpcomingItems(){
		if (_upcomingList.isEmpty()){
			return 0;
		} else {
			return _upcomingList.size();
		}
	}
	
	public static int getNumSomedayItems(){
		if (_somedayList.isEmpty()){
			return 0;
		} else {
			return _somedayList.size()-1;
		}
	}
    
	public static String getCurrentUserInput(){
		return _currentUserInput;
	}
	
	public static void setLogic(ClockWork l){
		_logic = l;
	}
	
}
