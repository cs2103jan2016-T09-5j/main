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
	 * Set list format from ArrayList to ListView so that list can be seen on
	 * GUI
	 * 
	 * @param arrayList
	 *            List of type ArrayList String
	 * @return listView List of type ListView String
	 */
	public static ListView<String> formatArrayList(ArrayList<String> arrayList) {
		ObservableList<String> obsList = FXCollections.observableList(arrayList);
		ListView<String> listView = new ListView<String>(obsList);
		listView.setItems(obsList);
		return listView;
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
				callLogicController(_currentUserInput);				
				textField.clear();
			}
		} else if (ke.getCode().equals(KeyCode.ESCAPE)) {
			// DEFAULT SCENE
			Main.displayDefaultScene();
		} else if (ke.getCode().equals(KeyCode.F1)){
			// HELP SCENE
			Main.displayHelpScene();
		} else if (ke.getCode().equals(KeyCode.F2)) {
			// DISPLAY CALENDAR
			Main.displayCalendarScene();
		} else if (ke.getCode().equals(KeyCode.F3)) {
			// MINIMISE
			Main.minimise();
		} else if (ke.getCode().equals(KeyCode.DELETE)) {
			// DISPLAY SUMMARY SCENE
			Main.displaySummaryScene();
		} else if (ke.getCode().equals(KeyCode.UP)){
			Main.scrollListener("UP");
		} else if (ke.getCode().equals(KeyCode.DOWN)){
			Main.scrollListener("DOWN");
		}
	}
	
	public static void redirectAppropriateScene(Button button, String sceneName) {
		button.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					if (sceneName.equals("Today")){
						Main.displayTodayScene();
					} else if (sceneName.equals("Tomorrow")){
						System.out.println("Tomorrow Layout");
					} else if (sceneName.equals("Upcoming")){
						System.out.println("Upcoming Layout");
					} else {
						System.out.println("Someday Layout");
					}
				} else if (ke.getCode().equals(KeyCode.ESCAPE)) {
					// DEFAULT SCENE
					Main.displayDefaultScene();
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
		});
	}

	// *********** LOGIC INTEGRATION HERE ************************
	public static void callLogicController(String userInput) {
		_logger.log(Level.INFO, "Calling logic to process keypress.");
		try {
			SignalHandler.clearArrListForGUI();
			DisplayCommand.clearArrListForGUI();
			SearchCommand.clearArrListForGUI();
			ClashDetector.clearArrListForGUI();
			ClockWork.ClockworkLogicMain(userInput, _logic);
			
			if (!SignalHandler.getArrListForGUI().isEmpty() & !ClashDetector.getArrListForGUI().isEmpty()) {
				String clashStr = getStringFromArrayList(ClashDetector.getArrListForGUI());
				Main.setFeedback(getStringFromArrayList(SignalHandler.getArrListForGUI())+clashStr);
				/********Create a label for clashDetector************************/
				System.out.println(clashStr);
				Main.setTaskList(DisplayCommand.getArrListForGUI());
				Main.displayDefaultScene();	
				
			} else if (!SignalHandler.getArrListForGUI().isEmpty() && SearchCommand.getArrListForGUI().isEmpty()) {
				Main.setFeedback(getStringFromArrayList(SignalHandler.getArrListForGUI()));
				Main.setTaskList(DisplayCommand.getArrListForGUI());
				Main.displayDefaultScene();	
				
			} else if (!SearchCommand.getArrListForGUI().isEmpty()) {
				Main.setTaskList(SearchCommand.getArrListForGUI());
				Main.displayDefaultScene();	
				
			} else {
				Main.setFeedback(" ");
				Main.setTaskList(DisplayCommand.getArrListForGUI());
				Main.displayDefaultScene();
			}

		} catch (Exception ex) {
			_logger.log(Level.WARNING, "Keypress detected, but failed to process.", ex);
		}
		_logger.log(Level.INFO, "Sucessfully called logic to process keypress.");
	}
	
	public static void unwrapObjectArrayList(ArrayList<UserInterfaceObject> guiObjectList, ArrayList<String> indexList, ArrayList<String> nameList, ArrayList<String> timeList, ArrayList<String> typeList){
		indexList.clear();
		nameList.clear();
		timeList.clear();
		typeList.clear();
		if (!guiObjectList.isEmpty()){
			for (int i = 0; i < guiObjectList.size(); i++){
				indexList.add(guiObjectList.get(i).getIndex());
				nameList.add(guiObjectList.get(i).getName());
				timeList.add(guiObjectList.get(i).getTime());
				typeList.add(guiObjectList.get(i).getType());
			}
		}
	}
		
	/**
	 * Set list format from ArrayList to ListView so that list can be seen on
	 * GUI
	 * 
	 * @param arrayList
	 *            List of type ArrayList String
	 * @return listView List of type ListView String
	 */
	public static ListView<String> setListViewFromArrayList(ArrayList<String> arrayList) {
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
    
	public static String getCurrentUserInput(){
		return _currentUserInput;
	}
	
	public static void setLogic(ClockWork l){
		_logic = l;
	}
}
