package userinterface;

import java.util.ArrayList;

import org.controlsfx.tools.Borders;

import common.UserInterfaceObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LayoutTemplate extends BorderPane {

	private ArrayList<UserInterfaceObject> _guiObjectList = new ArrayList<UserInterfaceObject>();
	private Label _titleLabel;
	private String _titleString;
	private Node _titleNode;
	private ListView<UserInterfaceObject> _indexListView;
	private ListView<UserInterfaceObject> _nameListView;
	private ListView<UserInterfaceObject> _timeListView;

	public LayoutTemplate(String title, ArrayList<UserInterfaceObject> guiObjectList) {
		_titleString = title;
		_guiObjectList = guiObjectList;
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
		implementTitle();
		implementListViews();
		GridPane innerGrid = implementInnerGrid(_indexListView, _nameListView, _timeListView);
		BoxTask outerBox = implementOuterBox(_titleNode, innerGrid);
		HBox mainHBox = implementMainContainer(outerBox);
		this.setCenter(mainHBox);
	}
	
	private HBox implementMainContainer(BoxTask outerBox){
		HBox mainHBox = new HBox();
		mainHBox.setStyle("-fx-background-color: #182733");
		mainHBox.getChildren().add(outerBox);
		mainHBox.setAlignment(Pos.CENTER);
		return mainHBox;
	}

	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	private void implementListViews() {
		createIndexListView();
		createNameListView();
		createTimeListView();
	}

	private BoxTask implementOuterBox(Node title, GridPane grid) {
		BoxTask boxTask = new BoxTask();
		boxTask.setTop(title);
		boxTask.setCenter(grid);
		return boxTask;
	}

	private GridPane implementInnerGrid(ListView<UserInterfaceObject> indexListView,
			ListView<UserInterfaceObject> nameListView, ListView<UserInterfaceObject> timeListView) {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		
		grid.getColumnConstraints().add(new ColumnConstraints(40));
	    grid.getColumnConstraints().add(new ColumnConstraints(500));
	    grid.getColumnConstraints().add(new ColumnConstraints(150));

		grid.add(indexListView, 0, 0);
		grid.add(nameListView, 1, 0);
		grid.add(timeListView, 2, 0);
		return grid;
	}

	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		return textField;
	}

	private void createTimeListView() {
		ObservableList<UserInterfaceObject> timeObsList = FXCollections.observableList(_guiObjectList);
		_timeListView = new ListView<UserInterfaceObject>(timeObsList);
		_timeListView.setItems(timeObsList);
		_timeListView.setCellFactory((ListView<UserInterfaceObject> l) -> new TimeCell());
	}

	private void createNameListView() {
		ObservableList<UserInterfaceObject> nameObsList = FXCollections.observableList(_guiObjectList);
		_nameListView = new ListView<UserInterfaceObject>(nameObsList);
		_nameListView.setItems(nameObsList);
		_nameListView.setCellFactory((ListView<UserInterfaceObject> l) -> new NameCell());

	}

	private void createIndexListView() {
		ObservableList<UserInterfaceObject> indexObsList = FXCollections.observableList(_guiObjectList);
		_indexListView = new ListView<UserInterfaceObject>(indexObsList);
		_indexListView.setItems(indexObsList);
		_indexListView.setCellFactory((ListView<UserInterfaceObject> l) -> new IndexCell());
	}

	static class IndexCell extends ListCell<UserInterfaceObject> {
		@Override
		public void updateItem(UserInterfaceObject userInterfaceObject, boolean empty) {
			super.updateItem(userInterfaceObject, empty);
			Text textIndex = new Text();
			if (userInterfaceObject != null) {
				textIndex.setText(userInterfaceObject.getIndex());
				textIndex.setFill(Color.WHITE);
				setGraphic(textIndex);
			}
		}
	}

	static class NameCell extends ListCell<UserInterfaceObject> {
		@Override
		public void updateItem(UserInterfaceObject userInterfaceObject, boolean empty) {
			super.updateItem(userInterfaceObject, empty);
			Text textName = new Text();
			textName.setWrappingWidth(450);
			if (userInterfaceObject != null) {
				textName.setText(userInterfaceObject.getName());
				textName.setFill(Color.WHITE);
				textName.setFont(Font.font("Calibri", 12));
				if ((userInterfaceObject.getType()).equals("ADD")) {
					textName.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
				} else if ((userInterfaceObject.getType()).equals("EDIT")) {
					textName.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
				} else if ((userInterfaceObject.getType()).equals("MARK")) {
					textName.setStrikethrough(true);
					textName.setFill(Color.GREY);
				} else if ((userInterfaceObject.getType()).equals("REDO")) {
					textName.setUnderline(true);
				} else if ((userInterfaceObject.getType()).equals("CLASH")) {
					textName.setText(userInterfaceObject.getName());
					textName.setFill(Color.CRIMSON);
				}
				setGraphic(textName);
			}
		}
	}

	static class TimeCell extends ListCell<UserInterfaceObject> {
		@Override
		public void updateItem(UserInterfaceObject userInterfaceObject, boolean empty) {
			super.updateItem(userInterfaceObject, empty);
			Text textTime = new Text();
			if (userInterfaceObject != null) {
				textTime.setText(userInterfaceObject.getTime());
				textTime.setFill(Color.WHITE);
				setGraphic(textTime);
			}
		}
	}
}
