package userinterface.controller;

import java.util.ArrayList;

import java.util.logging.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logic.ClockWork;
import logic.DisplayCommand;
import logic.SignalHandler;
import testcases.UserInterfaceLogicStub;

public class UIController {

	private static Logger _logger = java.util.logging.Logger.getLogger("ClockworkGUIController");
	private static String _currentUserInput;
	private static ClockWork _logic;

	/**
	 * Handle event after key is pressed
	 * 
	 * @param textField
	 *            User input to be handled
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
			// HIDE/UNNHIDE GUI [INCOMPLETE]
			System.out.println("UH TESTING");
		} else if (ke.getCode().equals(KeyCode.T)){
			// EXPAND TASK LIST
			// DO CSS
		}
	}

	// *********** LOGIC INTEGRATION HERE ************************
	public static void callLogicController(String userInput) {
		_logger.log(Level.INFO, "Calling logic to process keypress.");
		try {
			SignalHandler.clearArrListForGUI();
			DisplayCommand.clearArrListForGUI();
			
			ClockWork.ClockworkLogicMain(userInput, _logic);
			
			if (!SignalHandler.getArrListForGUI().isEmpty()){
				Main.setFeedback(SignalHandler.getArrListForGUI().get(0));
				Main.updateDisplay();	
			} else {
				Main.setFeedback(" ");
				Main.updateDisplay();
			}
			
			Main.setTaskList(DisplayCommand.getArrListForGUI());
			Main.updateDisplay();
			
			/** Skeleton for ClashDetector*/
	//		if (!ClashDetector.getArrListForGUI().isEmpty()){
	//			ClashDetector.userResponse = getCurrentUserInput();
	//			//some action
	//			ClockworkGUI.setConsoleList(ClashDetector.getArrListForGUI());
	//		}

		} catch (Exception ex) {
			_logger.log(Level.WARNING, "Keypress detected, but failed to process.", ex);
		}
		_logger.log(Level.INFO, "Sucessfully called logic to process keypress.");
	}
	
	public static String getCurrentUserInput(){
		return _currentUserInput;
	}
	
	public static void setLogic(ClockWork l){
		_logic = l;
	}
}
