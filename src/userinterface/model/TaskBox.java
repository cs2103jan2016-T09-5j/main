package userinterface.model;

import java.util.Random;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ColorGridCell;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

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
