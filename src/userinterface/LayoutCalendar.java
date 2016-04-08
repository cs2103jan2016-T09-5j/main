package userinterface;

import java.time.LocalDate;
import java.time.LocalTime;
import org.controlsfx.tools.Borders;
import org.joda.time.DateTime;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class LayoutCalendar extends BorderPane {
	
	private Label currentTime = new DigitalClock();
	
	//@@author Morgan
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

   /****************CALENDAR IMPLEMENTATION **********/
	private void setCenterRegion() {
		//Creating pane for calendar and current time
		BorderPane centralPane = new BorderPane();
		
		// Create the non-interactive DatePicker object with today in focus.
		DatePicker dp = new DatePicker();
		dp.setValue(LocalDate.now());
		// Add some action (in Java 8 lambda syntax style).
		dp.setOnAction(event -> {
		    LocalDate date = dp.getValue();
		    System.out.println("Selected date: " + date);
		});		
		
		//Isolate internal component, which was previously the component that "popped up"
		DatePickerSkin skin = new DatePickerSkin(new DatePicker());
		Node calendarControl = skin.getPopupContent();
		calendarControl.setId("calendar-control");
		calendarControl.setStyle("-fx-padding: 1;  -fx-background-insets: 0, 100;  "
				+ "-fx-background-radius: 0, 0; -fx-background-color: rgba(0, 100, 100, 0.1);");
		
		//Preparation of current time  
		this.currentTime.setFont(Font.font("Cambria", 50));
		this.currentTime.setTextFill(Color.WHITE);
		this.currentTime.setTextAlignment(TextAlignment.CENTER);
		
		
		//Affix isolated node along with the current time
		centralPane.setCenter(calendarControl);
		centralPane.setBottom(this.currentTime);
		centralPane.setStyle("-fx-background-color: #182733;");
		centralPane.setPadding(new Insets(25));

		//Exploit this component as our stand-alone calendar widget
		this.setCenter(centralPane);
	}
	/************* END IMPLEMENTATION ***************/
	
	private void setBottomRegion() {
		TextField textField = implementTextField();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					Controller.processEnter("DISPLAY");
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		this.setBottom(textField);
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);
		return textField;
	}
}
