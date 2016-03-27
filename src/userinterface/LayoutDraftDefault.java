package userinterface;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.tools.Borders;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class LayoutDraftDefault extends BorderPane {

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
		Node wrappedTodayLabel = Borders.wrap(todayLbl).lineBorder().color(Color.TEAL).build().build();
		
		Label tmrLbl = new Label("Tomorrow");
		Node wrappedTmrLabel = Borders.wrap(tmrLbl).lineBorder().color(Color.TEAL).build().build();

		Label upcomingLbl = new Label("Upcoming");
		Node wrappedUpcomingLabel = Borders.wrap(upcomingLbl).lineBorder().color(Color.TEAL).build().build();

		Label somedayLbl = new Label("Someday");
		Node wrappedSomedayLabel = Borders.wrap(somedayLbl).lineBorder().color(Color.TEAL).build().build();

	
		HiddenSidesPane todayPane = new HiddenSidesPane();
		todayPane.setTop(wrappedTodayLabel);
		todayPane.setContent(new Label(" "));
		todayPane.setPinnedSide(Side.TOP);
		todayPane.setPrefHeight(120);
		
		HiddenSidesPane tomorrowPane = new HiddenSidesPane();
		tomorrowPane.setTop(wrappedTmrLabel);
		tomorrowPane.setContent(new Label(" "));
		tomorrowPane.setPinnedSide(Side.TOP);
		tomorrowPane.setPrefHeight(120);
		
		BorderPane internalTopPane = new BorderPane();
		internalTopPane.setTop(todayPane);
		internalTopPane.setBottom(tomorrowPane);
		
		HiddenSidesPane upcomingPane = new HiddenSidesPane();
		upcomingPane.setTop(wrappedUpcomingLabel);
		upcomingPane.setContent(new Label(" "));
		upcomingPane.setPinnedSide(Side.TOP);
		upcomingPane.setPrefHeight(120);
		
		HiddenSidesPane somedayPane = new HiddenSidesPane();
		somedayPane.setTop(wrappedSomedayLabel);
		somedayPane.setContent(new Label(" "));
		somedayPane.setPinnedSide(Side.TOP);
		somedayPane.setPrefHeight(120);
				
		BorderPane internalBotPane = new BorderPane();
		internalBotPane.setTop(upcomingPane);
		internalBotPane.setBottom(somedayPane);
		
		BorderPane sidePanesBox = new BorderPane();
		sidePanesBox.setStyle("-fx-background-color: #182733;");
		sidePanesBox.setTop(internalTopPane);
		sidePanesBox.setBottom(internalBotPane);
		
		this.setCenter(sidePanesBox);
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
