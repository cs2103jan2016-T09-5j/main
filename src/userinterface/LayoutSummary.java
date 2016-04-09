package userinterface;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

//This BorderPane is for the Summary scene where the user can choose from the 4 buttons
public class LayoutSummary extends BorderPane {
	//Buttons in the summary scene
	private Button todayButton = new Button();
	private Button tomorrowButton = new Button();
	private Button upcomingButton = new Button();
	private Button somedayButton = new Button();
	
	//Strings to display in the StackPane
	private final String todayString = "Today";
	private final String tomorrowString = "Tomorrow";
	private final String upcomingString = "Upcoming";
	private final String somedayString = "Someday";
	
	//These integers denote the number to display for each category
	private int _numToday;
	private int _numTomorrow;
	private int _numUpcoming;
	private int _numSomeday;
	
	//These integers denote the size of the buttons
	private final int WIDTH_BUTTON_SIZE = 150;
	private final int HEIGHT_BUTTON_SIZE = 150;
	
	//@@author Rebekah
	//Constructor - Sets the Summary scene with the numbers to display for each category
	public LayoutSummary(int numToday, int numTomorrow, int numUpcoming, int numSomeday) {
		_numToday = numToday;
		_numTomorrow = numTomorrow;
		_numUpcoming = numUpcoming;
		_numSomeday = numSomeday;
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

	private void setCenterRegion() {		
		ComponentRectSummary rectToday = new ComponentRectSummary();
		ComponentRectSummary rectTomorrow = new ComponentRectSummary();
		ComponentRectSummary rectUpcoming = new ComponentRectSummary();
		ComponentRectSummary rectSomeday = new ComponentRectSummary();
		
		StackPane spToday = new StackPane();
		StackPane spTomorrow = new StackPane();
		StackPane spUpcoming = new StackPane();
		StackPane spSomeday = new StackPane();
		
		spToday.getChildren().addAll(rectToday, createSummaryButton(todayButton, todayString, createSummaryButtonString(todayString, _numToday)));
		spTomorrow.getChildren().addAll(rectTomorrow, createSummaryButton(tomorrowButton, tomorrowString, createSummaryButtonString(tomorrowString, _numTomorrow)));
		spUpcoming.getChildren().addAll(rectUpcoming, createSummaryButton(upcomingButton, upcomingString, createSummaryButtonString(upcomingString, _numUpcoming)));
		spSomeday.getChildren().addAll(rectSomeday, createSummaryButton(somedayButton, somedayString, createSummaryButtonString(somedayString, _numSomeday)));

		GridPane gridSummaryButtons = new GridPane();
		
		gridSummaryButtons.add(spToday, 0, 0);
		gridSummaryButtons.add(spTomorrow, 1, 0);
		gridSummaryButtons.add(spUpcoming, 0, 1);
		gridSummaryButtons.add(spSomeday, 1, 1);

		HBox gridSummaryRowWrapper = new HBox();

		gridSummaryRowWrapper.getChildren().add(gridSummaryButtons);
		gridSummaryRowWrapper.setAlignment(Pos.CENTER);

		GridPane centralizedGridSummary = new GridPane();

		centralizedGridSummary.getChildren().add(gridSummaryRowWrapper);
		centralizedGridSummary.setAlignment(Pos.CENTER);
		centralizedGridSummary.setStyle("-fx-background-color: #182733");

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		centralizedGridSummary.getColumnConstraints().add(columnConstraints);

		this.setCenter(centralizedGridSummary);
	}

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
	
	private String createSummaryButtonString(String summaryType, int numItems){
		String summaryString = summaryType + "\n" + Integer.toString(numItems) + " Item(s)";
		return summaryString;
	}
	
	private Node createSummaryButton(Button button, String summaryType, String summaryString){
		button.setText(summaryString);
		button.setWrapText(true);
		button.setPrefSize(WIDTH_BUTTON_SIZE, HEIGHT_BUTTON_SIZE);
		button.setTextFill(Color.WHITE);
		Controller.redirectScene(button, summaryType);
		Node wrappedButton = Borders.wrap(button).lineBorder().color(Color.AQUAMARINE).build().build();
		return wrappedButton;
	}
}
