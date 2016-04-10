package userinterface;

import javafx.scene.control.TextField;

//@@author Rebekah
/**
* The BoxInput class creates the textfield for user input and links the textfield 
* to the Controller class to handle user input and key events.
*/
public class BoxInput extends TextField {
	/** Constructor for BoxInput object*/
	public BoxInput() {
		Controller.implementKeystrokeEvents(this);
		this.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
	}
}