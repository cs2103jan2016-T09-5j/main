package userinterface;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

//This class displays the feedback to the user
public class BoxFeedback extends StackPane {
	
	//@@author Rebekah
	public BoxFeedback() {
		this.setPadding(new Insets(10,10,10,10));
		this.setStyle("-fx-background-color: #182733;");
	}
}