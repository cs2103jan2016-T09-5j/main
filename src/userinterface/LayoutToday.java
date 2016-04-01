package userinterface;

import java.util.ArrayList;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.tools.Borders;

import common.UserInterfaceObjectListTest;
import common.UserInterfaceObject;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LayoutToday extends BorderPane {
	
	private ArrayList<String> indexList = new ArrayList<String>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<String> timeList = new ArrayList<String>();
	private ArrayList<String> typeList = new ArrayList<String>();

	public LayoutToday() {
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
		Label todayLbl = new Label("Today");
		Node wrappedTodayLabel = Borders.wrap(todayLbl).lineBorder().color(Color.AQUAMARINE).build().build();

		ArrayList<UserInterfaceObject> guiObjectList = new ArrayList<UserInterfaceObject>();
		UserInterfaceObjectListTest.populateTestUIObjectList(guiObjectList);
		Controller.unwrapGUIObjectArrayList(guiObjectList, indexList, nameList, timeList, typeList);
		
		ListView<String> indexListView = new ListView<String>();
		indexListView = Controller.getListViewFromArrayList(indexList);
		
		ListView<String> nameListView = new ListView<String>();
		nameListView = Controller.getListViewFromArrayList(nameList);
		
		ListView<String> timeListView = new ListView<String>();
		timeListView = Controller.getListViewFromArrayList(timeList);
		
		ListView<String> typeListView = new ListView<String>();
		typeListView = Controller.getListViewFromArrayList(typeList);
		 
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #182733;");
	    grid.setPadding(new Insets(5));
		
	    grid.add(indexListView, 0, 0);
		grid.add(nameListView, 1, 0);
		grid.add(timeListView, 2, 0);
		grid.add(typeListView, 3, 0);
		
		BoxTask boxTask = new BoxTask();
		boxTask.setTop(wrappedTodayLabel);
		boxTask.setCenter(grid);
		
		this.setCenter(boxTask);
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
}

