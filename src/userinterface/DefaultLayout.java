package userinterface;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DefaultLayout extends BorderPane {

	private ArrayList<String> _taskList;
	private String _feedback;

	private Text taskText = new Text();
	
	private TaskBox taskBox = new TaskBox();

	public DefaultLayout() {
		this.setDisplayRegions();
	};

	public DefaultLayout(ArrayList<String> taskList, String feedback) {
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

	// Translucent Background
	// -fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;

	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		HeaderBox headerBox = new HeaderBox();
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
		
		FeedbackBox feedbackBox = implementFeedbackBox(feedbackLbl);
		InputBox inputBox = implementInputBox();
		
		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	/** IMPLEMENTING REGION OBJECTS */

	private void implementTaskBox() {
		taskBox.getChildren().add(taskText);
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
	}

	private BorderPane implementUserBox(FeedbackBox feedbackBox, InputBox inputBox) {
		BorderPane userBox = new BorderPane();
		userBox.setTop(feedbackBox);
		userBox.setBottom(inputBox);
		userBox.setStyle("-fx-background-color: #182733;");
		return userBox;
	}

	private InputBox implementInputBox() {	
		InputBox textField = new InputBox();
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "display", "mark", "edit");

		return textField;
	}

	private FeedbackBox implementFeedbackBox(Label feedbackLbl) {
		FeedbackBox feedbackBox = new FeedbackBox();
		feedbackBox.getChildren().add(feedbackLbl);

		return feedbackBox;
	}

	private Label createFeedbackLabel() {
		Label feedbackLbl = new Label(_feedback);

		return feedbackLbl;
	}
}
