package graphicUserInterface;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class BottomBotDisplay extends TextField {
	
	public BottomBotDisplay(){
	}	
	
	public TextField getTextField() {
		return this;
	}
	/** 
	 * Handle event after enter is pressed
	 * 
	 * @param userInput				User input to be handled
	 */
	public void handleUserInput(TextField userInput) {
		userInput.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	        	//IGNORE FOR NOW - You can try seeing how this part works, not sure if you'll need
	        	//it for the function to see if the user input matches the help list so you can
	        	//change it later
//	        	currentInputList.add(userInput.getText());
//	        	System.out.println(currentInputList);
//	        	System.out.println(userInput.getText());
	        	//End Comment
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	if ((userInput.getText() != null && !userInput.getText().isEmpty())) {
	            		handleUserEnter(userInput.getText());
		            	userInput.clear();
	            	}
	            } 
	        }
	    });
	}
	
	//******** REGINE WORK ON THIS PART COS THIS IS LOGIC (NOT SUPP TO BE HERE)! *********
	private void handleUserEnter(String userInput){
//		rawInputString = userInput;
		
		ArrayList<String> consoleListCopy = BottomTopDisplay.getConsoleList();
		consoleListCopy.add(userInput);
		BottomTopDisplay.changeConsoleList(consoleListCopy);
		
		//Start Comment - Call Logic API here to handle the String user entered.
		//Eg. ('Cos idk the actual API you're gonna call here)
//		clockWork.handleCommand(rawInputString);
		//, where handleCommand is the logic API that processes the string and returns me 
		//the arraylist of tasks I should display on the GUI. That means that
		//the prevCommandsList should not be stored in the GUI, but it should be in logic.
		
		ArrayList<String> taskListCopy = CenterDisplay.getTaskList();
		taskListCopy.add("NEW TASK! :D");
		CenterDisplay.changeTaskList(taskListCopy);
		
		//End Comment
		
		ClockworkGUI.refresh();
		System.out.println("This is what you typed: " + userInput);
	}
}
