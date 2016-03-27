package userinterface;

import javafx.scene.control.TextField;

public class InputBox extends TextField {
	public InputBox() {
		UIController.implementKeystrokeEvents(this);
		this.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
	}
}