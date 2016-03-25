package userinterface.model;

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
		this.setStyle("-fx-background-color: #000000;");
		this.setPadding(new Insets(10,10,10,10));
	}

}
