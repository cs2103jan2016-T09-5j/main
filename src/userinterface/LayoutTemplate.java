package userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class LayoutTemplate extends BorderPane {

	private ArrayList<String[]> _list = new ArrayList<String[]>();
	private Label _titleLabel;
	private String _titleString;
	private ArrayList<String> _feedbackList;
	private Node _titleNode;

	public static final String ColumnIndexMapKey = "Index";
	public static final String ColumnNameMapKey = "Name";
	public static final String ColumnTimeMapKey = "Time";
	public static final String ColumnDateMapKey = "Date";
	
	private TableView tableView;
	private double scrollValue = 0.25f;

	public LayoutTemplate(String title, ArrayList<String[]> list, ArrayList<String> feedbackList) {
		if (feedbackList == null) System.out.println("Error: LayoutTemplate null");
		_titleString = title;
		_list = list;
		_feedbackList = feedbackList;
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

		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #182733;");

		TableColumn<Map, String> firstDataColumn = new TableColumn<>("No");
		TableColumn<Map, String> secondDataColumn = new TableColumn<>("Name");
		TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Time");

		firstDataColumn.setCellValueFactory(new MapValueFactory(ColumnIndexMapKey));
		firstDataColumn.setMinWidth(10);
		secondDataColumn.setCellValueFactory(new MapValueFactory(ColumnNameMapKey));
		secondDataColumn.setMinWidth(600);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(ColumnTimeMapKey));
		thirdDataColumn.setMinWidth(90);

		tableView = new TableView<>(populateDataInMap());
		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);
		tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn);
		Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = (
				TableColumn<Map, String> p) -> new TextFieldTableCell(new StringConverter() {
					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});

		firstDataColumn.setCellFactory(cellFactoryForMap);
		secondDataColumn.setCellFactory(cellFactoryForMap);
		thirdDataColumn.setCellFactory(cellFactoryForMap);

		grid.setHgrow(tableView, Priority.ALWAYS);

		grid.add(_titleNode, 0, 0);
		grid.add(tableView, 0, 1);

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		hbox.setStyle("-fx-background-color: #182733;");

		this.setCenter(hbox);
	}

	private ObservableList<Map> populateDataInMap() {
		ObservableList<Map> allData = FXCollections.observableArrayList();
		for (int i = 0; i < _list.size(); i++) {
			Map<String, String> dataRow = new HashMap<>();

			String index = _list.get(0)[0];
			String name = _list.get(0)[1];
			String time = _list.get(0)[2];

			dataRow.put(ColumnIndexMapKey, index);
			dataRow.put(ColumnNameMapKey, name);
			dataRow.put(ColumnTimeMapKey, time);

			allData.add(dataRow);
		}
		return allData;
	}

	private void setBottomRegion() {
		Text feedbackText = createFeedbackLabel();

		BoxFeedback feedbackBox = implementFeedbackBox(feedbackText);
		BoxInput inputBox = implementInputBox();

		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	private BorderPane implementUserBox(BoxFeedback feedbackBox, BoxInput inputBox) {
		BorderPane userBox = new BorderPane();
		userBox.setTop(feedbackBox);
		userBox.setBottom(inputBox);
		userBox.setStyle("-fx-background-color: #182733;");
		return userBox;
	}

	private BoxInput implementInputBox() {
		BoxInput textField = new BoxInput();
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "display", "mark", "edit");
		return textField;
	}

	private BoxFeedback implementFeedbackBox(Text feedbackText) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackText);
		return feedbackBox;
	}

	private Text createFeedbackLabel() {
		Text feedbackText = new Text(_feedbackList.get(1));		
		feedbackText.setText(_feedbackList.get(1));
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font("Calibri", 12));
		if ((_feedbackList.get(0)).equals("Added")) {
			feedbackText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		} else if ((_feedbackList.get(0)).equals("Edited")) {
			feedbackText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
		} else if ((_feedbackList.get(0)).equals("Marked")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if ((_feedbackList.get(0)).equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if ((_feedbackList.get(0)).equals("Clash")) {
			feedbackText.setText(_feedbackList.get(1));
			feedbackText.setFill(Color.CRIMSON);
		}		
		return feedbackText;
	}
	
	public TableView getTableView(){
		return this.tableView;
	}
	
//	public double setScrollPosition(String action, double prevScrollValue){
//		for (Node n: tableView.lookupAll(".scroll-bar")) {
//			  if (n instanceof ScrollBar) {
//			    ScrollBar bar = (ScrollBar) n;
//			    System.out.println(bar.getOrientation() + ": range " + bar.getMin() + " => " + bar.getMax() + ", value " + bar.getValue());
//			  }
//		}
//		if (action == "UP" && ((prevScrollValue - scrollValue) >= 0)){
//			tableView.setVvalue(prevScrollValue - scrollValue);
//		} else if (action == "DOWN" && ((prevScrollValue + scrollValue) <= 1)){
//			tableView.setVvalue(prevScrollValue + scrollValue);
//		} else {
//			tableView.setVvalue(prevScrollValue);
//		}
//		return tableView.getVvalue();
//	}
}
