package userinterface;

import javafx.scene.control.TextField;

//This class is for displaying the text box
public class BoxInput extends TextField {
	
	//@@author Rebekah
	public BoxInput() {
		Controller.implementKeystrokeEvents(this);
		this.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
	}
}