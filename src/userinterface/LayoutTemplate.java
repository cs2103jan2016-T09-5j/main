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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	private int _listSize;
	private boolean _displayDate;
	private boolean _exitEnabled;

	//Keys for the stored tasks in the HashMap
	public static final String KEY_COLUMN_INDEX = "Index";
	public static final String KEY_COLUMN_NAME = "Name";
	public static final String KEY_COLUMN_TIME = "Time";
	public static final String KEY_COLUMN_DATE = "Date";
	
	//Settings for the scrollpane scrolling speed
	private static int currentScrollIndex = 0;
	private static final int SPEED_SCROLL_DOWN = 3;
	private static final int SPEED_SCROLL_UP = -3;

	//Indexes of the task variables in the string array
	private final int INDEX_TASK_INDEX = 0;
	private final int INDEX_TASK_NAME = 1;
	private final int INDEX_TASK_TIME = 2;
	private final int INDEX_TASK_DATE = 3;
	
	//Styling for the objects in the scene
	public static final String STYLE_CENTRE_REGION = "-fx-background-color: #182733;";
	public static final String STYLE_USER_BOX = "-fx-background-color: #182733;";
	public static final String FONT_FEEDBACK = "Calibri";
	
	//Sizes for the elements in the scene
	private final int FONTSIZE_FEEDBACK = 12;
	//Display sizes if the scene has 3 columns
	private final int WIDTH_WRAPPING_FEEDBACK = 500;
	private final int WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_1 = 10;
	private final int WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_2 = 460;
	private final int WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_3 = 290;
	//Display sizes if the scene has 4 columns
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_1 = 20;
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_2 = 320;
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_3 = 250;
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_4 = 150;
	
	//Column that holds all the task information
	private TableView tableView;
	
	//@@author Rebekah
	public LayoutTemplate(String title, ArrayList<String[]> list, 
			ArrayList<String> feedbackList, boolean displayDate, boolean enableExit) {
		if (feedbackList == null) System.out.println("Error: LayoutTemplate null");
		_titleString = title;
		_list = list;
		_listSize = list.size();
		_feedbackList = feedbackList;
		_displayDate = displayDate;
		_exitEnabled = enableExit;
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
		grid.setStyle(STYLE_CENTRE_REGION);

		TableColumn<Map, String> firstDataColumn = new TableColumn<>("Index");
		TableColumn<Map, String> secondDataColumn = new TableColumn<>("Name");
		TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Time");

		int firstColWidth = _displayDate ? WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_1
				: WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_1;
		int secondColWidth = _displayDate ? WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_2
				: WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_2;
		int thirdColWidth = _displayDate ? WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_3
				: WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_3;
		
		firstDataColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_INDEX));
		firstDataColumn.setMinWidth(firstColWidth);
		secondDataColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_NAME));
		secondDataColumn.setMinWidth(secondColWidth);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_TIME));
		thirdDataColumn.setMinWidth(thirdColWidth);
		
		TableColumn<Map, String> fourthDataColumn = new TableColumn<>("Date");
		if (_displayDate) {
			fourthDataColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_DATE));
			fourthDataColumn.setMinWidth(WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_4);
		}

		tableView = new TableView<>(populateDataInMap());
		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);
		
		if (_displayDate){
			tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn, fourthDataColumn);
		} else {
			tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn);
		}
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
		
//		System.out.println("Num of Rows: " + tableView.getItems().size());
		
		grid.setHgrow(tableView, Priority.ALWAYS);

		grid.add(_titleNode, 0, 0);
		grid.add(tableView, 0, 1);

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		hbox.setStyle("-fx-background-color: #182733;");

		this.setCenter(hbox);
	}

	//Fills up the tableView with all the task information
	private ObservableList<Map> populateDataInMap() {
		ObservableList<Map> allData = FXCollections.observableArrayList();
		for (int i = 1; i < _listSize; i++) {
			Map<String, String> dataRow = new HashMap<>();

			String index = _list.get(i)[INDEX_TASK_INDEX];
			String name = _list.get(i)[INDEX_TASK_NAME];
			String time = _list.get(i)[INDEX_TASK_TIME];

			dataRow.put(KEY_COLUMN_INDEX, index);
			dataRow.put(KEY_COLUMN_NAME, name);
			dataRow.put(KEY_COLUMN_TIME, time);
			
			if (_displayDate) {
				String date = _list.get(i)[INDEX_TASK_DATE];
				dataRow.put(KEY_COLUMN_DATE, date);
			}

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
		userBox.setStyle(STYLE_USER_BOX);
		return userBox;
	}

	private BoxInput implementInputBox() {
		BoxInput textField = new BoxInput();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.UP)) {
					if ((currentScrollIndex + SPEED_SCROLL_UP) >= 0){
						tableView.scrollTo(currentScrollIndex + SPEED_SCROLL_UP);
						currentScrollIndex = currentScrollIndex + SPEED_SCROLL_UP;
					}
				} else if (ke.getCode().equals(KeyCode.DOWN)){
					if ((currentScrollIndex + SPEED_SCROLL_DOWN) <= tableView.getItems().size()){
						tableView.scrollTo(currentScrollIndex + SPEED_SCROLL_DOWN);
						currentScrollIndex = currentScrollIndex + SPEED_SCROLL_DOWN;
					}
				}  else if (_exitEnabled && ke.getCode().equals(KeyCode.ESCAPE)){
					// DISPLAY SUMMARY SCENE
					Main.setNumToday(Controller.getNumTodayItems());
					Main.setNumTomorrow(Controller.getNumTomorrowItems());
					Main.setNumUpcoming(Controller.getNumUpcomingItems());
					Main.setNumSomeday(Controller.getNumSomedayItems());
					Main.displaySummaryScene();
				}
				
				Controller.executeKeyPress(textField, ke);
			}
		});
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "mark", "edit", "exit");
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
		Text feedbackText = new Text(_feedbackList.get(0));		
		feedbackText.setText(_feedbackList.get(0));
		feedbackText.setWrappingWidth(WIDTH_WRAPPING_FEEDBACK);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font(FONT_FEEDBACK, FONTSIZE_FEEDBACK));
		if (first.equals("Added")) {
			feedbackText.setFont(Font.font(FONT_FEEDBACK, FontWeight.BOLD, FONTSIZE_FEEDBACK));
		} else if (first.equals("Edited")) {
			feedbackText.setFont(Font.font(FONT_FEEDBACK, FontPosture.ITALIC, FONTSIZE_FEEDBACK));
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
