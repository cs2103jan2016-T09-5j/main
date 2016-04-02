package userinterface;

import java.util.ArrayList;

import org.controlsfx.tools.Borders;

import common.UserInterfaceObjectListTest;
import common.UserInterfaceObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
		UserInterfaceObjectListTest.populateTestObjectList(guiObjectList);
		Controller.unwrapObjectArrayList(guiObjectList, indexList, nameList, timeList, typeList);

		ObservableList<UserInterfaceObject> nameObsList = FXCollections.observableList(guiObjectList);
		ListView<UserInterfaceObject> nameListView = new ListView<UserInterfaceObject>(nameObsList);
		nameListView.setItems(nameObsList);
		nameListView.setCellFactory((ListView<UserInterfaceObject> l) -> new NameCell());
		
		ListView<String> indexListView = Controller.setListViewFromArrayList(indexList);
		ListView<String> timeListView = Controller.setListViewFromArrayList(timeList);
		
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #182733;");
		grid.setPadding(new Insets(5));
		
		grid.add(indexListView, 0, 0);
		grid.add(nameListView, 1, 0);
		grid.add(timeListView, 2, 0);

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
	
	static class NameCell extends ListCell<UserInterfaceObject> {
		@Override
		public void updateItem(UserInterfaceObject item, boolean empty) {
			super.updateItem(item, empty);
			Text myText = new Text();
			if (item != null) {
				myText.setText(item.getName());
				myText.setFill(Color.WHITE);
				if ((item.getType()).equals("ADD")) {
					myText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
				} else if ((item.getType()).equals("EDIT")) {
					myText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
				} else if ((item.getType()).equals("MARK")) {
					myText.setFont(Font.font("Calibri", 12));
					myText.setStrikethrough(true);
					myText.setFill(Color.GREY);
				} else if ((item.getType()).equals("REDO")) {
					myText.setFont(Font.font("Calibri", 12));
					myText.setUnderline(true);
				} else if ((item.getType()).equals("CLASH")) {
					myText.setText(item.getName());
					myText.setFont(Font.font("Calibri", 12));
					myText.setFill(Color.CRIMSON);
				}
				setGraphic(myText);
			}
		}
	}
}
