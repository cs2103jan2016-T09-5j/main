package userinterface;

import java.util.ArrayList;

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
import logic.DisplayCommand;
import logic.SearchCommand;
import logic.SignalHandler;

public class Controller {

	private static Logger _logger = java.util.logging.Logger.getLogger("ClockworkGUIController");
	private static String _currentUserInput;
	private static ArrayList<String> _feedback;
	private static ClockWork _logic;
	private static ArrayList<String[]> _todayList = new ArrayList<String[]>();
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
			if (userInput.equalsIgnoreCase("search")){
				Main.displaySearchScene();
			} else {
				ClockWork.ClockworkLogicMain(userInput, _logic);
//				
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
				
				
				Main.setFeedback("POTATO");
				
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
	
	public static void redirectScene(Button button, String sceneName) {
		button.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					if (sceneName.equals("Today")){
						UserInterfaceStub.populateList(_todayList);
						Main.setTodayList(_todayList);
						currentTableView = Main.displayTodayScene();
					} else if (sceneName.equals("Tomorrow")){				
						Main.setTomorrowList(_tomorrowList);
						Main.displayTomorrowScene();
					} else if (sceneName.equals("Upcoming")){					
						Main.setUpcomingList(_upcomingList);
						Main.displayUpcomingScene();
					} else if (sceneName.equals("Someday")){
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
	
//	public static void resetLists(){
//		SignalHandler.clearArrListForGUI();
//		DisplayCommand.clearArrListForGUI();
//		SearchCommand.clearArrListForGUI();
//		ClashDetector.clearArrListForGUI();
//	}
			
	public static int getNumTodayItems(){
		if (_todayList.isEmpty()){
			return 0;
		} else {
			return _todayList.size();
		}
	}
	
	public static int getNumTomorrowItems(){
		if (_tomorrowList.isEmpty()){
			return 0;
		} else {
			return _tomorrowList.size();
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
			return _somedayList.size();
		}
	}
    
	public static String getCurrentUserInput(){
		return _currentUserInput;
	}
	
	public static void setLogic(ClockWork l){
		_logic = l;
		processEnter("DISPLAY");
	}
	
}
