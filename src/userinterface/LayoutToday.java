package userinterface;

import java.util.ArrayList;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.tools.Borders;

import common.UserInterfaceObjectListTest;
import common.UserInterfaceObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

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

		ListView<String> indexListView = Controller.setListViewFromArrayList(indexList);
		ListView<String> nameListView = Controller.setListViewFromArrayList(nameList);
		ListView<String> timeListView = Controller.setListViewFromArrayList(timeList);
		ListView<String> typeListView = Controller.setListViewFromArrayList(typeList);
		
		typeListView.setCellFactory((ListView<String> l) -> new FeedbackCell());
		
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

	static class FeedbackCell extends ListCell<String> {
		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			Text myText = new Text(item);
			if (item != null) {
				if (item.equals("ADD")) {
					myText.setText(item);
					myText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
					myText.setFill(Color.WHITE);
					setGraphic(myText);
				} else if (item.equals("EDIT")) {
					myText.setText(item);
					myText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
					myText.setFill(Color.WHITE);
					setGraphic(myText);
				} else if (item.equals("MARK")) {
					myText.setText(item);
					myText.setFont(Font.font("Calibri", 12));
					myText.setStrikethrough(true);
					myText.setFill(Color.GREY);
					setGraphic(myText);
				} else if (item.equals("REDO")) {
					myText.setText(item);
					myText.setFont(Font.font("Calibri", 12));
					myText.setUnderline(true);
					myText.setFill(Color.WHITE);
					setGraphic(myText);
				} else if (item.equals("CLASH")) {
					myText.setText(item);
					myText.setFont(Font.font("Calibri", 12));
					myText.setFill(Color.CRIMSON);
					setGraphic(myText);
				} else {
					myText.setText(item);
					myText.setFont(Font.font("Calibri", 12));
					myText.setFill(Color.WHITE);
					setGraphic(myText);
				}
			}
		}
	}
	
	static class UserInterfaceCell extends ListCell<UserInterfaceObject> {
		@Override
		public void updateItem(UserInterfaceObject item, boolean empty) {
			super.updateItem(item, empty);
			Text textName = new Text();
			String type = item.getType();
			if (item != null) {
				textName.setText(item.getName());
				if (type.equals("ADD")) {
					textName.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
					textName.setFill(Color.WHITE);
					setGraphic(textName);
				} else if (item.equals("EDIT")) {
					textName.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
					textName.setFill(Color.WHITE);
					setGraphic(textName);
				} else if (item.equals("MARK")) {
					textName.setFont(Font.font("Calibri", 12));
					textName.setStrikethrough(true);
					textName.setFill(Color.GREY);
					setGraphic(textName);
				} else if (item.equals("REDO")) {
					textName.setFont(Font.font("Calibri", 12));
					textName.setUnderline(true);
					textName.setFill(Color.WHITE);
					setGraphic(textName);
				} else if (item.equals("CLASH")) {
					textName.setFont(Font.font("Calibri", 12));
					textName.setFill(Color.CRIMSON);
					setGraphic(textName);
				} else {
					textName.setFont(Font.font("Calibri", 12));
					textName.setFill(Color.WHITE);
					setGraphic(textName);
				}
			}
		}
	}
}
