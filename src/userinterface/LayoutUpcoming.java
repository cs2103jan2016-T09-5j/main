package userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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

public class LayoutUpcoming extends BorderPane {

	private ArrayList<String[]> _list = new ArrayList<String[]>();
	private Label _titleLabel;
	private String _titleString;
	private String _feedback;
	private Node _titleNode;

	public static final String ColumnIndexMapKey = "Index";
	public static final String ColumnNameMapKey = "Name";
	public static final String ColumnTimeMapKey = "Time";
	public static final String ColumnDateMapKey = "Date";

	public LayoutUpcoming(String title, ArrayList<String[]> list, String feedbackType) {
		_titleString = title;
		_list = list;
		_feedback = feedbackType;
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
		TableColumn<Map, String> fourthDataColumn = new TableColumn<>("Date");

		firstDataColumn.setCellValueFactory(new MapValueFactory(ColumnIndexMapKey));
		firstDataColumn.setMinWidth(10);
		secondDataColumn.setCellValueFactory(new MapValueFactory(ColumnNameMapKey));
		secondDataColumn.setMinWidth(600);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(ColumnTimeMapKey));
		thirdDataColumn.setMinWidth(90);
		fourthDataColumn.setCellValueFactory(new MapValueFactory(ColumnDateMapKey));
		fourthDataColumn.setMinWidth(100);

		TableView tableView = new TableView<>(populateDataInMap());
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);
		tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn, fourthDataColumn);
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
		fourthDataColumn.setCellFactory(cellFactoryForMap);

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

			String index = _list.get(i)[0];
			String name = _list.get(i)[1];
			String time = _list.get(i)[2];
			String date = _list.get(i)[3];

			dataRow.put(ColumnIndexMapKey, index);
			dataRow.put(ColumnNameMapKey, name);
			dataRow.put(ColumnTimeMapKey, time);
			dataRow.put(ColumnDateMapKey, date);

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
		Text feedbackText = new Text(_feedback);		
		feedbackText.setText(_feedback);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font("Calibri", 12));
		if (_feedback.equals("Added")) {
			feedbackText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		} else if (_feedback.equals("Edited")) {
			feedbackText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
		} else if (_feedback.equals("Marked")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if (_feedback.equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if (_feedback.equals("Clash")) {
			feedbackText.setText(_feedback);
			feedbackText.setFill(Color.CRIMSON);
		}
		return feedbackText;
	}
}
