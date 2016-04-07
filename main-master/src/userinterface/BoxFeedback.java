package userinterface;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;


public class BoxFeedback extends StackPane {
	public BoxFeedback() {
		this.setPadding(new Insets(10,10,10,10));
		this.setStyle("-fx-background-color: #182733;");
	}
}