package userinterface.controller;

import java.io.InvalidClassException;
import java.util.ArrayList;

import java.util.logging.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import userinterface.view.ClockworkGUI;

public class ClockworkGUIController {
	
	private static Logger Logger = java.util.logging.Logger.getLogger("ClockworkGUIController");
	
	/**
	 * Handle event after key is pressed
	 * 
	 * @param textField		User input to be handled
	 */
	
	public static void implementKeystrokeEvents(TextField textField) {
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
	 * @param arrayList		List of type ArrayList String
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
	 * @param textField		User input to be handled
	 * @param ke			Key that is pressed
	 */
	public static void executeKeyPress(TextField textField, KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			if ((textField.getText() != null && !textField.getText().isEmpty())) {
				processUserEnter(textField.getText());
				textField.clear();
			}
		} else if (ke.getCode().equals(KeyCode.ESCAPE)) {
			// DO SOMETHING [INCOMPLETE]
		}
	}

	public static void processUserEnter(String userInput) {
		Logger.log(Level.INFO, "Going to process enter keypress.");
		try {
			callControllerToAddCommand(userInput);
		} catch (Exception ex){
			System.err.println("Enter keypress detected, but failed to process.");
			Logger.log(Level.WARNING, "processing error");
		}
		Logger.log(Level.INFO, "End of processing enter keypress.");
	}

	// *********** LOGIC INTEGRATION HERE ************************
	public static void callControllerToAddCommand(String userInput) {
		// Call Logic API Here
		
//		ClockworkGUI.consoleList.add(userInput);
//		ClockworkGUI.taskListTest.add("NEW TASK!");
//		ClockworkGUI.refresh();
//		System.out.println("This is what you typed: " + userInput);
		
//		if (!ClockworkGUI.consoleList.isEmpty()){
//			//update taskList to show the prompt
//			//feed(userInput)			
//		}
	}
}