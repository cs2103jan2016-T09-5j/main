package userinterface;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.tools.Borders;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LayoutDraftDefault extends BorderPane {
	
	private Text blankText = new Text(" ");
	private Text someText = new Text("TEST TEXT");
	private Text testTextToday;
	private Text testTextTomorrow;
	private Text testTextUpcoming;
	private Text testTextSomeday;
	

	public LayoutDraftDefault() {
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

	/************** EXAMPLE OF POSSIBLE GUI FORMAT (: **********/
	private void setCenterRegion() { 				
		Label todayLbl = new Label("Today");
		Node wrappedTodayLabel = Borders.wrap(todayLbl).lineBorder().color(Color.AQUAMARINE).build().build();
		
		Label tmrLbl = new Label("Tomorrow");
		Node wrappedTmrLabel = Borders.wrap(tmrLbl).lineBorder().color(Color.TEAL).build().build();

		Label upcomingLbl = new Label("Upcoming");
		Node wrappedUpcomingLabel = Borders.wrap(upcomingLbl).lineBorder().color(Color.TEAL).build().build();

		Label somedayLbl = new Label("Someday");
		Node wrappedSomedayLabel = Borders.wrap(somedayLbl).lineBorder().color(Color.TEAL).build().build();

		Text testText = new Text();
		testText.setText("This is a text sample");
		testText.setFont(Font.font ("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 20));
		testText.setStrikethrough(true);
		testText.setUnderline(true);
		testText.setFill(Color.WHITE);
		
		HiddenSidesPane todayPane = new HiddenSidesPane();
		todayPane.setTop(wrappedTodayLabel);
		todayPane.setContent(testText);
		todayPane.setPinnedSide(Side.TOP);
		todayPane.setPrefHeight(160);
				
		HiddenSidesPane tomorrowPane = new HiddenSidesPane();
		tomorrowPane.setTop(wrappedTmrLabel);
		tomorrowPane.setContent(someText);
		tomorrowPane.setPinnedSide(Side.TOP);
		tomorrowPane.setPrefHeight(160);
		
		HiddenSidesPane upcomingPane = new HiddenSidesPane();
		upcomingPane.setTop(wrappedUpcomingLabel);
		upcomingPane.setContent(blankText);
		upcomingPane.setPinnedSide(Side.TOP);
		upcomingPane.setPrefHeight(160);
		
		HiddenSidesPane somedayPane = new HiddenSidesPane();
		somedayPane.setTop(wrappedSomedayLabel);
		somedayPane.setContent(blankText);
		somedayPane.setPinnedSide(Side.TOP);
		somedayPane.setPrefHeight(160);
				
		
		GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: #182733;");
	    gridPane.setPadding(new Insets(5));
	    
	    ColumnConstraints column1 = new ColumnConstraints(222);
	    ColumnConstraints column2 = new ColumnConstraints(222);
	    ColumnConstraints column3 = new ColumnConstraints(222);
	    ColumnConstraints column4 = new ColumnConstraints(222);
	    
	    column1.setHgrow(Priority.ALWAYS);
	    column2.setHgrow(Priority.ALWAYS);
	    column3.setHgrow(Priority.ALWAYS);
	    column4.setHgrow(Priority.ALWAYS);
	    
	    gridPane.getColumnConstraints().addAll(column1, column2, column3, column4);
		gridPane.add(todayPane, 0, 0);
		gridPane.add(tomorrowPane, 1, 0);
		gridPane.add(upcomingPane, 2, 0);
		gridPane.add(somedayPane, 3, 0);
		
		GridPane.setHalignment(todayPane, HPos.CENTER); // To align horizontally in the cell
		GridPane.setValignment(todayPane, VPos.CENTER); // To align vertically in the cell
		GridPane.setHalignment(tomorrowPane, HPos.CENTER); // To align horizontally in the cell
		GridPane.setValignment(tomorrowPane, VPos.CENTER); // To align vertically in the cell
		GridPane.setHalignment(upcomingPane, HPos.CENTER); // To align horizontally in the cell
		GridPane.setValignment(upcomingPane, VPos.CENTER); // To align vertically in the cell
		GridPane.setHalignment(upcomingPane, HPos.CENTER); // To align horizontally in the cell
		GridPane.setValignment(somedayPane, VPos.CENTER); // To align vertically in the cell
		GridPane.setHalignment(somedayPane, HPos.CENTER);
		
		this.setCenter(gridPane);
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
	
	private Text stringToText(String string){
		Text text = new Text(string);
		return text;
	}
}
