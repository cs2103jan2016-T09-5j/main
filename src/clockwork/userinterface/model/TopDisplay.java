package clockwork.userinterface.model;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Display welcome Text for top region of main GUI class
 * 
 * @author Rebekah
 */

public class TopDisplay extends HBox {
	private static Text _textDisplay = new Text("Welcome to Clockwork (:");

	public TopDisplay() {
		styleHBox();
		styleText();
		this.getChildren().add(_textDisplay);
	}

	public static void changeDisplayText(String text) {
		_textDisplay.setText(text);
	}

	public static Text getDisplayText() {
		return _textDisplay;
	}

	private void styleHBox() {
		this.setPadding(new Insets(10, 12, 10, 12));
		this.setSpacing(5);
		this.setStyle("-fx-background-color: #336699;");
	}

	private void styleText() {
		_textDisplay.setFill(Color.WHITE);
	}
}
