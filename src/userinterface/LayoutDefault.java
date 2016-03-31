package userinterface;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LayoutDefault extends BorderPane {

	private ArrayList<String> _taskList;
	private String _feedback;

	private Text taskText = new Text();
	
	private BoxTask taskBox = new BoxTask();
	
	private StackPane taskTextBox = new StackPane();
	
	private ScrollPane scrollPane = new ScrollPane();

	public LayoutDefault() {
		this.setDisplayRegions();
	};

	public LayoutDefault(ArrayList<String> taskList, String feedback) {
		_taskList = taskList;
		_feedback = feedback;
		this.setDisplayRegions();
	}

	/** POPULATING LAYOUT */

	/** Set top, center and bottom region to display information */
	public void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}

	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		BoxHeader headerBox = new BoxHeader();
		headerBox.removeEscNode();
		
		this.setTop(headerBox);
	}

	/** Set center region to display task list */
	private void setCenterRegion() {
		implementTaskText();
		implementTaskBox();

		this.setCenter(taskBox);
	}

	/** Set bottom region to display input section */
	private void setBottomRegion() {
		Label feedbackLbl = createFeedbackLabel();
		
		BoxFeedback feedbackBox = implementFeedbackBox(feedbackLbl);
		BoxInput inputBox = implementInputBox();
		
		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	/** IMPLEMENTING REGION OBJECTS */

	private void implementTaskBox() {
createScrollPane();
		
		taskBox.setCenter(taskTextBox);
		taskBox.setCenter(scrollPane);
	}

	private void implementTaskText() {

		try {
			if (!_taskList.isEmpty()) {
				taskText.setText(_taskList.get(0));
			}
		} catch (Exception e) {
			taskText.setText(" ");
			e.printStackTrace();
		}
		
		taskText.setFill(Color.WHITE);
		
		// ListView<String> taskListView = new ListView<String>();
		// taskListView = UIController.formatArrayList(_taskList);
		// taskBox.getChildren().add(taskListView);
		
		taskTextBox.getChildren().add(taskText);
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

	private BoxFeedback implementFeedbackBox(Label feedbackLbl) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackLbl);

		return feedbackBox;
	}

	private Label createFeedbackLabel() {
		Label feedbackLbl = new Label(_feedback);

		return feedbackLbl;
	}
	
	private void createScrollPane() {
		scrollPane.setContent(taskText);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setStyle("-fx-background: #182733;");
	}
}
