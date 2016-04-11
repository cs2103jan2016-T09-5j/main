package userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

//@@author A0129833Y
/**
 * The LayoutTemplate class creates the layout to display the table containing
 * all tasks to the user.
 */
public class LayoutTemplate extends BorderPane {

	/** Lists to display in the table and feedback box */
	private ArrayList<String[]> _list = new ArrayList<String[]>();
	private ArrayList<String> _feedbackList;

	/** Boolean to display date column and enable Esc key to change scene */
	private boolean _shouldDisplayDate;
	private boolean _shouldEnableEscape;

	/** Integer to store size of task list */
	private int _listSize;

	/** Nodes to display layout title */
	private Label _titleLabel;
	private String _titleString;
	private Node _titleNode;

	/** Keys for the stored tasks in the HashMap */
	public static final String KEY_COLUMN_INDEX = "Index";
	public static final String KEY_COLUMN_NAME = "Name";
	public static final String KEY_COLUMN_TIME = "Time";
	public static final String KEY_COLUMN_DATE = "Date";

	/** Settings for the scrollpane scrolling speed */
	private static int currentScrollIndex = 0;
	private static final int SPEED_SCROLL_DOWN = 3;
	private static final int SPEED_SCROLL_UP = -3;

	/** Indexes of the task variables in the string array */
	private final int INDEX_TASK_INDEX = 0;
	private final int INDEX_TASK_NAME = 1;
	private final int INDEX_TASK_TIME = 2;
	private final int INDEX_TASK_DATE = 3;

	/** Styling for the objects in the scene */
	public static final String STYLE_CENTRE_REGION = "-fx-background-color: #182733;";
	public static final String STYLE_USER_BOX = "-fx-background-color: #182733;";
	public static final String FONT_FEEDBACK = "Calibri";

	/** Sizes for the elements in the scene */
	private final int FONTSIZE_FEEDBACK = 12;

	/** Display sizes if the scene has 3 columns */
	private final int WIDTH_WRAPPING_FEEDBACK = 500;
	private final int WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_1 = 10;
	private final int WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_2 = 460;
	private final int WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_3 = 290;

	/** Display sizes if the scene has 4 columns */
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_1 = 20;
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_2 = 320;
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_3 = 250;
	private final int WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_4 = 150;

	/** Table that holds all the task information */
	private TableView tableView;

	/** Table columns containing different types of task information */
	private TableColumn<Map, String> indexColumn = new TableColumn<>("Index");
	private TableColumn<Map, String> nameColumn = new TableColumn<>("Name");
	private TableColumn<Map, String> timeColumn = new TableColumn<>("Time");
	private TableColumn<Map, String> dateColumn = new TableColumn<>("Date");

	/** Box that holds the table and title of layout */
	private GridPane innerTableBox = new GridPane();
	private HBox outerTableBox = new HBox();

	/**
	 * Constructor for LayoutTemplate
	 * 
	 * @param title					the title for the template when used in the scene
	 * @param list					the ArrayList list to display tasks
	 * @param feedbackList			the ArrayList feedback list to show feedback to user
	 * @param displayDate			the boolean condition to decide if date column should be shown
	 * @param enableExit			the boolean condition to enable escape to go to the
	 * 								previous page
	 */
	public LayoutTemplate(String title, ArrayList<String[]> list, ArrayList<String> feedbackList, boolean displayDate,
			boolean enableExit) {
		if (feedbackList == null) {
			System.out.println("Error: LayoutTemplate null");
		}
		_titleString = title;
		_list = list;
		_listSize = list.size();
		_feedbackList = feedbackList;
		_shouldDisplayDate = displayDate;
		_shouldEnableEscape = enableExit;

		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}

	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}

	/** Set center region to display tasks in table */
	private void setCenterRegion() {
		implementTitle();
		implementTable();
		configureColumns();
		implementInnerTableBox();
		implementOuterTableBox();
		this.setCenter(outerTableBox);
	}

	/** Set bottom region for user input and keypress */
	private void setBottomRegion() {
		Text feedbackText = createFeedbackLabel();
		BoxFeedback feedbackBox = implementFeedbackBox(feedbackText);
		BoxInput inputBox = implementInputBox();
		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	/** Create outer box to centralize task table */
	private void implementOuterTableBox() {
		outerTableBox.setAlignment(Pos.CENTER);
		outerTableBox.getChildren().add(innerTableBox);
		outerTableBox.setStyle("-fx-background-color: #182733;");
	}

	/** Create inner box to contain the table view and title of layout */
	private void implementInnerTableBox() {
		innerTableBox.setStyle(STYLE_CENTRE_REGION);
		innerTableBox.setHgrow(tableView, Priority.ALWAYS);
		innerTableBox.add(_titleNode, 0, 0);
		innerTableBox.add(tableView, 0, 1);
	}

	/** Create the table containing the task information */
	private void implementTable() {
		tableView = new TableView<>(populateDataInMap());
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);

		if (_shouldDisplayDate) {
			tableView.getColumns().setAll(indexColumn, nameColumn, timeColumn, dateColumn);
		} else {
			tableView.getColumns().setAll(indexColumn, nameColumn, timeColumn);
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

		indexColumn.setCellFactory(cellFactoryForMap);
		nameColumn.setCellFactory(cellFactoryForMap);
		timeColumn.setCellFactory(cellFactoryForMap);
	}

	/** Configure columns in table to reflect corresponding data */
	private void configureColumns() {
		int indexColWidth = _shouldDisplayDate ? WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_1
				: WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_1;
		int nameColWidth = _shouldDisplayDate ? WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_2
				: WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_2;
		int timeColWidth = _shouldDisplayDate ? WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_3
				: WIDTH_DISPLAY3_WRAPPING_DATACOLUMN_3;

		indexColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_INDEX));
		indexColumn.setMinWidth(indexColWidth);
		nameColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_NAME));
		nameColumn.setMinWidth(nameColWidth);
		timeColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_TIME));
		timeColumn.setMinWidth(timeColWidth);

		if (_shouldDisplayDate) {
			dateColumn.setCellValueFactory(new MapValueFactory(KEY_COLUMN_DATE));
			dateColumn.setMinWidth(WIDTH_DISPLAY4_WRAPPING_DATACOLUMN_4);
		}
	}

	/** Fills up the tableView with all the task information */
	private ObservableList<Map> populateDataInMap() {
		ObservableList<Map> allData = FXCollections.observableArrayList();
		if (_shouldDisplayDate) {
			for (int i = 0; i < _listSize; i++) {
				Map<String, String> dataRow = new HashMap<>();

				String index = _list.get(i)[INDEX_TASK_INDEX];
				String name = _list.get(i)[INDEX_TASK_NAME];
				String time = _list.get(i)[INDEX_TASK_TIME];
				String date = _list.get(i)[INDEX_TASK_DATE];

				dataRow.put(KEY_COLUMN_INDEX, index);
				dataRow.put(KEY_COLUMN_NAME, name);
				dataRow.put(KEY_COLUMN_TIME, time);
				dataRow.put(KEY_COLUMN_DATE, date);

				allData.add(dataRow);
			}
		} else {
			for (int i = 1; i < _listSize; i++) {
				Map<String, String> dataRow = new HashMap<>();

				String index = _list.get(i)[INDEX_TASK_INDEX];
				String name = _list.get(i)[INDEX_TASK_NAME];
				String time = _list.get(i)[INDEX_TASK_TIME];

				dataRow.put(KEY_COLUMN_INDEX, index);
				dataRow.put(KEY_COLUMN_NAME, name);
				dataRow.put(KEY_COLUMN_TIME, time);

				allData.add(dataRow);
			}
		}
		return allData;
	}
	
	/** Implements the title of the layout */
	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	/** Implements the user box containing feedback and input field */
	private BorderPane implementUserBox(BoxFeedback feedbackBox, BoxInput inputBox) {
		BorderPane userBox = new BorderPane();
		userBox.setTop(feedbackBox);
		userBox.setBottom(inputBox);
		userBox.setStyle(STYLE_USER_BOX);
		return userBox;
	}

	/** Implements input field for user to key in input and register keypress */
	private BoxInput implementInputBox() {
		BoxInput textField = new BoxInput();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.UP)) {
					if ((currentScrollIndex + SPEED_SCROLL_UP) >= 0) {
						tableView.scrollTo(currentScrollIndex + SPEED_SCROLL_UP);
						currentScrollIndex = currentScrollIndex + SPEED_SCROLL_UP;
					}
				} else if (ke.getCode().equals(KeyCode.DOWN)) {
					if ((currentScrollIndex + SPEED_SCROLL_DOWN) <= tableView.getItems().size()) {
						tableView.scrollTo(currentScrollIndex + SPEED_SCROLL_DOWN);
						currentScrollIndex = currentScrollIndex + SPEED_SCROLL_DOWN;
					}
				} else if (_shouldEnableEscape && ke.getCode().equals(KeyCode.ESCAPE)) {
					// Displays Category Scene
					Main.setNumToday(Controller.getNumTodayItems());
					Main.setNumTomorrow(Controller.getNumTomorrowItems());
					Main.setNumUpcoming(Controller.getNumUpcomingItems());
					Main.setNumSomeday(Controller.getNumSomedayItems());
					Main.displayCategoryScene();
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "mark", "edit", "exit");
		return textField;
	}

	/** Implements feedback box for user to see feedback about their commands */
	private BoxFeedback implementFeedbackBox(Text feedbackText) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackText);
		return feedbackBox;
	}

	/** Creates label for feedback to display to user */
	private Text createFeedbackLabel() {
		String[] result = _feedbackList.get(0).split(" ", 2);

		String feedbackType = result[0];
		String feedbackMessage = _feedbackList.get(0);
		Text feedbackText = new Text(feedbackMessage);

		feedbackText.setText(feedbackMessage);
		feedbackText.setWrappingWidth(WIDTH_WRAPPING_FEEDBACK);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font(FONT_FEEDBACK, FONTSIZE_FEEDBACK));

		if (feedbackType.equals("Added")) {
			feedbackText.setFont(Font.font(FONT_FEEDBACK, FontWeight.BOLD, FONTSIZE_FEEDBACK));
		} else if (feedbackType.equals("Edited")) {
			feedbackText.setFont(Font.font(FONT_FEEDBACK, FontPosture.ITALIC, FONTSIZE_FEEDBACK));
		} else if (feedbackType.equals("Marked") || feedbackType.equals("Deleted")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if (feedbackType.equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if (feedbackType.equals("Undo")) {
			feedbackText.setFill(Color.GREY);
		} else if (feedbackType.equals("Clash")) {
			feedbackText.setText(_feedbackList.get(0));
			feedbackText.setFill(Color.CRIMSON);
		}
		return feedbackText;
	}

}
