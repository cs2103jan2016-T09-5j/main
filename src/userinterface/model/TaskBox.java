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
		this.setStyle("-fx-background-color: #323749;");
		this.setPadding(new Insets(10,10,10,10));
		Rectangle rect = new Rectangle(750,380,750,380);
		Color c = Color.web("#135d62");
		rect.setFill(c);
		this.getChildren().add(rect);
	}

}
