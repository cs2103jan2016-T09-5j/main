package userinterface;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

/**
 * Display ArrayList containing console strings for center region of main GUI
 * class
 * 
 * @author Rebekah
 */

public class TaskBox extends StackPane {
	public TaskBox() {
		this.setStyle("-fx-background-color: #182733;");
		this.setPadding(new Insets(10,10,10,10));
	}

}
