package userinterface;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LayoutCalendar extends BorderPane {

	public LayoutCalendar() {
		setDisplayRegions();
	}

	/** POPULATING LAYOUT */
	
	private void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}
	
	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}

	/************** IMPLEMENT CALENDAR HERE! (: **********/
	private void setCenterRegion() {
		//borderPane is a dummy variable, you might wanna change it!
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color: #182733;");
		
		this.setCenter(borderPane);
	}
	/************* END IMPLEMENTATION ***************/
	
	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);

		return textField;
	}
}
