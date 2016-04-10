package userinterface;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

//@@author Rebekah
/**
 * The BoxTask class creates the layout used to contain the nodes used to display
 * user tasks to the user.
 */
public class BoxTask extends BorderPane{
	/** Constructor for BoxTask object*/
	public BoxTask() {
		this.setStyle("-fx-background-color: #182733;");
		this.setPadding(new Insets(10,10,10,10));
	}

}
