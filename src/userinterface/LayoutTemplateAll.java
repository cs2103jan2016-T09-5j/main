package userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

public class LayoutTemplateAll extends BorderPane {

	private ArrayList<String[]> _list = new ArrayList<String[]>();
	private Label _titleLabel;
	private String _titleString;
	private ArrayList<String> _feedbackList;
	private Node _titleNode;

	public static final String ColumnIndexMapKey = "Index";
	public static final String ColumnNameMapKey = "Name";
	public static final String ColumnTimeMapKey = "Time";
	public static final String ColumnDateMapKey = "Date";
	
	private static int currentScrollIndex = 0;
	private static int scrollDownIndex = 3;
	private static int scrollUpIndex = -3;

	private TableView tableView;
	
	//@@author Rebekah
	public LayoutTemplateAll(String title, ArrayList<String[]> list, ArrayList<String> feedbackList) {
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
		TableColumn<Map, String> fourthDataColumn = new TableColumn<>("Date");

		firstDataColumn.setCellValueFactory(new MapValueFactory(ColumnIndexMapKey));
		firstDataColumn.setMinWidth(20);
		secondDataColumn.setCellValueFactory(new MapValueFactory(ColumnNameMapKey));
		secondDataColumn.setMinWidth(320);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(ColumnTimeMapKey));
		thirdDataColumn.setMinWidth(250);
		fourthDataColumn.setCellValueFactory(new MapValueFactory(ColumnDateMapKey));
		fourthDataColumn.setMinWidth(150);

	    tableView = new TableView<>(populateDataInMap());
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
		//	System.out.println(_list.size());
			
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
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.UP)) {
					if ((currentScrollIndex + scrollUpIndex) >= 0){
						tableView.scrollTo(currentScrollIndex + scrollUpIndex);
						currentScrollIndex = currentScrollIndex + scrollUpIndex;
					}
				} else if (ke.getCode().equals(KeyCode.DOWN)){
					if ((currentScrollIndex + scrollDownIndex) <= tableView.getItems().size()){
						tableView.scrollTo(currentScrollIndex + scrollDownIndex);
						currentScrollIndex = currentScrollIndex + scrollDownIndex;
					}
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "mark", "edit");
		return textField;
	}

	private BoxFeedback implementFeedbackBox(Text feedbackText) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackText);
		return feedbackBox;
	}

	private Text createFeedbackLabel() {
		 String[] result = _feedbackList.get(0).split(" ", 2);
		    String first = result[0];
		   // System.out.println(first);
		Text feedbackText = new Text(_feedbackList.get(0));		
		feedbackText.setText(_feedbackList.get(0));
		feedbackText.setWrappingWidth(500);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font("Calibri", 12));
		if (first.equals("Added")) {
			feedbackText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		} else if (first.equals("Edited")) {
			feedbackText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
		} else if (first.equals("Marked") || first.equals("Deleted")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if (first.equals("Undo")) {
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Clash")) {
			feedbackText.setText(_feedbackList.get(0));
			feedbackText.setFill(Color.CRIMSON);
		}
		return feedbackText;
	}
}
