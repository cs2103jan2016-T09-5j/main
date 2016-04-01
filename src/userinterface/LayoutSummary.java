package userinterface;

import org.controlsfx.tools.Borders;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LayoutSummary extends BorderPane {

	private Button todayButton = new Button();
	private Button tomorrowButton = new Button();
	private Button upcomingButton = new Button();
	private Button somedayButton = new Button();
	
	private String todayString = "Today";
	private String tomorrowString = "Tomorrow";
	private String upcomingString = "Upcoming";
	private String somedayString = "Someday";
	
	
	private int todayNumItemsTest = 1;
	private int tomorrowNumItemsTest = 2;
	private int upcomingNumItemsTest = 3;
	private int somedayNumItemsTest = 4;
	
	public LayoutSummary() {
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
		
		spToday.getChildren().addAll(rectToday, createSummaryButton(todayButton, todayString, createSummaryButtonString(todayString, todayNumItemsTest)));
		spTomorrow.getChildren().addAll(rectTomorrow, createSummaryButton(tomorrowButton, tomorrowString, createSummaryButtonString(tomorrowString, tomorrowNumItemsTest)));
		spUpcoming.getChildren().addAll(rectUpcoming, createSummaryButton(upcomingButton, upcomingString, createSummaryButtonString(upcomingString, upcomingNumItemsTest)));
		spSomeday.getChildren().addAll(rectSomeday, createSummaryButton(somedayButton, somedayString, createSummaryButtonString(somedayString, somedayNumItemsTest)));

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
		button.setPrefSize(150, 150);
		button.setTextFill(Color.WHITE);
		button.setStyle("-fx-background-color: transparent;");
		Controller.redirectAppropriateScene(button, summaryString);
		Node wrappedButton = Borders.wrap(button).lineBorder().color(Color.AQUAMARINE).build().build();
		return wrappedButton;
	}
}
