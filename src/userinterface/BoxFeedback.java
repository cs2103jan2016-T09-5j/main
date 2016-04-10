package userinterface;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

//@@author Rebekah
/**
 * The BoxFeedback class creates the layout to display feedback based
 * on the user input to the user.
 */
public class BoxFeedback extends StackPane {
	/** Constructor for BoxFeedback object */
	public BoxFeedback() {
		this.setPadding(new Insets(10,10,10,10));
		this.setStyle("-fx-background-color: #182733;");
	}
}