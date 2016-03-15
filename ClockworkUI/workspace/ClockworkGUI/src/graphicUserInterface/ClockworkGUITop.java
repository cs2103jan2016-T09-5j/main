package graphicUserInterface;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/** 
 * Displays Text in HBox 
 * To be used for top region of main GUI class
 */

public class ClockworkGUITop extends HBox {
	private Text _displayText;
	
	public ClockworkGUITop(Text displayText){
		_displayText = displayText;
		styleHBox();
		styleText();
		this.getChildren().add(_displayText);
	}

	public void changeDisplayText(String text){
		_displayText.setText(text);
	}
	
	public Text getDisplayText(){
		return _displayText;
	}

	private void styleHBox() {
		this.setPadding(new Insets(10, 12, 10, 12));
		this.setSpacing(5);
		this.setStyle("-fx-background-color: #336699;");
	}
	private void styleText() {
		_displayText.setFill(Color.WHITE);
	}
}
