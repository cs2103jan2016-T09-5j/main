package userinterface;

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

//@@author Rebekah
/**
* The LayoutCategory class creates the layout to display the category buttons to the user. The 
* category button comprising of Today, Tomorrow, Upcoming and Someday can be clicked by pressing
* Tab+Enter to navigate to the corresponding folders containing tasks of the four types. 
*/
public class LayoutCategory extends BorderPane {
	/** Buttons for navigation for each category */
	private Button todayButton = new Button();
	private Button tomorrowButton = new Button();
	private Button upcomingButton = new Button();
	private Button somedayButton = new Button();
	
	/** Rectangles to display for each category */
	private LayoutCategoryRectangle rectToday = new LayoutCategoryRectangle();
	private LayoutCategoryRectangle rectTomorrow = new LayoutCategoryRectangle();
	private LayoutCategoryRectangle rectUpcoming = new LayoutCategoryRectangle();
	private LayoutCategoryRectangle rectSomeday = new LayoutCategoryRectangle();
	
	/** Stackpanes to contain the rectangles and buttons for each category  */
	private StackPane spToday = new StackPane();
	private StackPane spTomorrow = new StackPane();
	private StackPane spUpcoming = new StackPane();
	private StackPane spSomeday = new StackPane();
	
	/** Strings to display on each category  */
	private static final String todayString = "Today";
	private static final String tomorrowString = "Tomorrow";
	private static final String upcomingString = "Upcoming";
	private static final String somedayString = "Someday";
	
	/** Integers to denote the number to display for each category */
	private int _numToday;
	private int _numTomorrow;
	private int _numUpcoming;
	private int _numSomeday;
	
	/** Integers to denote the size of the buttons */
	private final int WIDTH_BUTTON_SIZE = 150;
	private final int HEIGHT_BUTTON_SIZE = 150;
	
	/** Layouts used to contain the categories */
	private GridPane categoryButtons = new GridPane();
	private HBox categoryRow = new HBox();
	private GridPane categoryGrid = new GridPane();
	
	/** Constructor for LayoutCategory */
	public LayoutCategory(int numToday, int numTomorrow, int numUpcoming, int numSomeday) {
		_numToday = numToday;
		_numTomorrow = numTomorrow;
		_numUpcoming = numUpcoming;
		_numSomeday = numSomeday;
		
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}

	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}
	
	/** Set center region to display the four categories */
	private void setCenterRegion() {		
		implementCategoryBoxes();
		implementCategoryButtons();
		implementCategoryRow();
		this.setCenter(createCategoryGrid());
	}
	
	/** Set bottom region for user input */
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
	
	/** Implement category boxes for each category */
	private void implementCategoryBoxes(){
		spToday.getChildren().addAll(rectToday, 
				createCategoryButton(todayButton, todayString, 
						createCategoryButtonString(todayString, _numToday)));
		spTomorrow.getChildren().addAll(rectTomorrow, 
				createCategoryButton(tomorrowButton, tomorrowString, 
						createCategoryButtonString(tomorrowString, _numTomorrow)));
		spUpcoming.getChildren().addAll(rectUpcoming,
				createCategoryButton(upcomingButton, upcomingString, 
						createCategoryButtonString(upcomingString, _numUpcoming)));
		spSomeday.getChildren().addAll(rectSomeday, 
				createCategoryButton(somedayButton, somedayString, 
						createCategoryButtonString(somedayString, _numSomeday)));
	}
	
	/** Implement category buttons for each category */
	private void implementCategoryButtons(){
		categoryButtons.add(spToday, 0, 0);
		categoryButtons.add(spTomorrow, 1, 0);
		categoryButtons.add(spUpcoming, 0, 1);
		categoryButtons.add(spSomeday, 1, 1);
	}
	
	/** Implement category row to centralize all category buttons */
	private void implementCategoryRow(){
		categoryRow.getChildren().add(categoryButtons);
		categoryRow.setAlignment(Pos.CENTER);
	}
	
	/** Implement the textfield for user to enter input */
	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);

		return textField;
	}
	
	/** Create category grid for all category boxes */
	private GridPane createCategoryGrid(){
		categoryGrid.getChildren().add(categoryRow);
		categoryGrid.setAlignment(Pos.CENTER);
		categoryGrid.setStyle("-fx-background-color: #182733");

		ColumnConstraints categoryColumnConstraints = new ColumnConstraints();
		categoryColumnConstraints.setFillWidth(true);
		categoryColumnConstraints.setHgrow(Priority.ALWAYS);
		categoryGrid.getColumnConstraints().add(categoryColumnConstraints);
		
		return categoryGrid;
	}
	
	/** Create category button string to add on each button */
	private String createCategoryButtonString(String categoryType, int numItems){
		String categoryString = categoryType + "\n" + Integer.toString(numItems) + " Item(s)";
		return categoryString;
	}
	
	/** Create category button for each category */
	private Node createCategoryButton(Button button, String categoryType, String categoryString){
		button.setText(categoryString);
		button.setWrapText(true);
		button.setPrefSize(WIDTH_BUTTON_SIZE, HEIGHT_BUTTON_SIZE);
		button.setTextFill(Color.WHITE);
		Controller.redirectScene(button, categoryType);
		Node buttonNode = Borders.wrap(button).lineBorder().color(Color.AQUAMARINE).build().build();
		return buttonNode;
	}
}
