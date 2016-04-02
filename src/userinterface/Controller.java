package userinterface;

import java.util.ArrayList;

import java.util.logging.*;

import common.UserInterfaceObjectListTest;
import common.UserInterfaceObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
	private static ClockWork _logic;
	private static ArrayList<UserInterfaceObject> _todayList = new ArrayList<UserInterfaceObject>();
	private static ArrayList<UserInterfaceObject> _tomorrowList = new ArrayList<UserInterfaceObject>();	
	private static ArrayList<UserInterfaceObject> _upcomingList = new ArrayList<UserInterfaceObject>();	
	private static ArrayList<UserInterfaceObject> _somedayList = new ArrayList<UserInterfaceObject>();

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
	
	public static void redirectFromSummaryScene(Button button, String sceneName) {
		button.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					if (sceneName.equals("Today")){
						//DO SOMETHING TO INTEGRATE WITH LOGIC
						//Eg. _todayList = Logic.getTodayList();
						
						//FOR TESTING PURPOSES, REMOVE ONCE LOGIC INTEGRATION IS DONE
						UserInterfaceObjectListTest.populateTestObjectList(_todayList);
						
						Main.setTodayList(_todayList);
						Main.displayTodayScene();
					
					} else if (sceneName.equals("Tomorrow")){
						//DO SOMETHING TO INTEGRATE WITH LOGIC
						//Eg. _tomorrowList = Logic.getTomorrowList();
						
						//FOR TESTING PURPOSES, REMOVE ONCE LOGIC INTEGRATION IS DONE
						UserInterfaceObjectListTest.populateTestObjectList(_tomorrowList);
						
						Main.setTomorrowList(_tomorrowList);
						Main.displayTomorrowScene();
					} else if (sceneName.equals("Upcoming")){
						//DO SOMETHING TO INTEGRATE WITH LOGIC
						//Eg. _upcomingList = Logic.getUpcomingList();
						
						//FOR TESTING PURPOSES, REMOVE ONCE LOGIC INTEGRATION IS DONE
						UserInterfaceObjectListTest.populateTestObjectList(_upcomingList);
						
						Main.setUpcomingList(_upcomingList);
						Main.displayUpcomingScene();
					} else if (sceneName.equals("Someday")){
						//DO SOMETHING TO INTEGRATE WITH LOGIC
						//Eg. _somedayList = Logic.getSomedayList();
						
						//FOR TESTING PURPOSES, REMOVE ONCE LOGIC INTEGRATION IS DONE
						UserInterfaceObjectListTest.populateTestObjectList(_somedayList);
	
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
				} 
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
//				callLogicController(_currentUserInput);	
				System.out.println("BOO");
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
//		 else if (ke.getCode().equals(KeyCode.DELETE)) {
//			// DEFAULT SCENE
//			Main.displayOldScene();
//		} else if (ke.getCode().equals(KeyCode.UP)){
//			Main.scrollListener("UP");
//		} else if (ke.getCode().equals(KeyCode.DOWN)){
//			Main.scrollListener("DOWN");
//		}
	}

//	// *********** LOGIC INTEGRATION HERE ************************
//	public static void callLogicController(String userInput) {
//		_logger.log(Level.INFO, "Calling logic to process keypress.");
//		try {
//			SignalHandler.clearArrListForGUI();
//			DisplayCommand.clearArrListForGUI();
//			SearchCommand.clearArrListForGUI();
//			ClashDetector.clearArrListForGUI();
//			ClockWork.ClockworkLogicMain(userInput, _logic);
//			
//			if (!SignalHandler.getArrListForGUI().isEmpty() & !ClashDetector.getArrListForGUI().isEmpty()) {
//				String clashStr = getStringFromArrayList(ClashDetector.getArrListForGUI());
//				Main.setFeedback(getStringFromArrayList(SignalHandler.getArrListForGUI())+clashStr);
//				/********Create a label for clashDetector************************/
//				System.out.println(clashStr);
//				Main.setTaskList(DisplayCommand.getArrListForGUI());
//				Main.displayOldScene();	
//				
//			} else if (!SignalHandler.getArrListForGUI().isEmpty() && SearchCommand.getArrListForGUI().isEmpty()) {
//				Main.setFeedback(getStringFromArrayList(SignalHandler.getArrListForGUI()));
//				Main.setTaskList(DisplayCommand.getArrListForGUI());
//				Main.displayOldScene();	
//				
//			} else if (!SearchCommand.getArrListForGUI().isEmpty()) {
//				Main.setTaskList(SearchCommand.getArrListForGUI());
//				Main.displayOldScene();	
//				
//			} else {
//				Main.setFeedback(" ");
//				Main.setTaskList(DisplayCommand.getArrListForGUI());
//				Main.displayOldScene();
//			}
//
//		} catch (Exception ex) {
//			_logger.log(Level.WARNING, "Keypress detected, but failed to process.", ex);
//		}
//		_logger.log(Level.INFO, "Sucessfully called logic to process keypress.");
//	}
			
	/**
	 * Set list format from ArrayList to ListView so that list can be seen on
	 * GUI
	 * 
	 * @param arrayList
	 *            List of type ArrayList String
	 * @return listView List of type ListView String
	 */
	public static ListView<String> getListViewFromArrayList(ArrayList<String> arrayList) {
		ObservableList<String> obsList = FXCollections.observableList(arrayList);
		ListView<String> listView = new ListView<String>(obsList);
		listView.setItems(obsList);
		return listView;
	}
	
	private static String getStringFromArrayList(ArrayList<String> arrList){
		String str="";
		for(int i=0; i<arrList.size();i++){
			str += arrList.get(i)+"\n";
		}
		return str;
	}
	
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
	}
}
