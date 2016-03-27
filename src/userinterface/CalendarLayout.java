package userinterface;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CalendarLayout extends BorderPane {
	
	private GridPane calendarBox = new GridPane();

	public CalendarLayout() {
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
		HeaderBox headerBox = new HeaderBox();
		this.setTop(headerBox);
	}

	/************** IMPLEMENT CALENDAR HERE! (: **********/
	private void setCenterRegion() {
		//calendarBox is a dummy variable, you might wanna change it!
		this.setCenter(calendarBox);
	}
	/************* END IMPLEMENTATION ***************/
	
	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	private TextField implementTextField() {
		InputBox textField = new InputBox();
		textField.setEditable(false);

		return textField;
	}
}
