package userinterface;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.tools.Borders;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DefaultLayout extends BorderPane {
	
//	private static final String GLYPH_NAME_HELP = "QUESTION";
//	private static final String GLYPH_NAME_CAL = "CALENDAR";

	private ArrayList<String> _taskList;
	private String _feedback;
	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

	private Node taskLabel;
	private Node helpLabel;
	private Node calLabel;

	private Label taskLbl = new Label("     Tasks     ");
	private Label helpLbl = new Label("  F1");
	private Label calLbl = new Label("  F2 ");

	private Button helpIcon = new Button("", fontAwesome.create("question").color(Color.WHITE));
	private Button calIcon = new Button("", fontAwesome.create("calendar").color(Color.WHITE));

	private Text taskText = new Text();

	private HeaderBox headerBox = new HeaderBox();
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
	// -fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;"

	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		implementHeaderLabels();
		implementHeaderBox();

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
		Label feedbackLbl = implementFeedbackLabel();
		FeedbackBox feedbackBox = implementFeedbackBox(feedbackLbl);
		
		TextField textField = implementTextField();
		InputBox inputBox = implementInputBox(textField);
		
		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}
	
	/** IMPLEMENTING REGION OBJECTS */

	private void implementHeaderBox() {
		headerBox.setLeft(taskLabel);
		headerBox.setRight(createShortcutBox());
	}

	private void implementHeaderLabels() {
		taskLabel = createTaskLabel();
		helpLabel = createHelpLabel();
		calLabel = createCalLabel();
	}	

	private void implementTaskBox() {
		taskBox.getChildren().add(taskText);
	}

	private void implementTaskText() {
		taskText.setText(" ");
		if (!_taskList.isEmpty()) {
			taskText.setText(_taskList.get(0));
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
		return userBox;
	}

	private InputBox implementInputBox(TextField textField) {
		InputBox inputBox = new InputBox();
		inputBox.getChildren().add(textField);
		return inputBox;
	}

	private TextField implementTextField() {
		TextField textField = new TextField();
		UIController.implementKeystrokeEvents(textField);
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "display", "mark", "edit");
		
		textField.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
		
		return textField;
	}

	private FeedbackBox implementFeedbackBox(Label feedbackLbl) {
		FeedbackBox feedbackBox = new FeedbackBox();
		feedbackBox.getChildren().add(feedbackLbl);

		return feedbackBox;
	}

	private Label implementFeedbackLabel() {
		Label feedbackLbl = new Label(_feedback);
		
		feedbackLbl.setStyle("-fx-text-fill: #FFFFFF");

		return feedbackLbl;
	}
	
	/** CREATING LAYOUT OBJECTS */
	
	private Node createShortcutBox() {
		HeaderBox shortcutsBox = new HeaderBox();
		shortcutsBox.setLeft(helpLabel);
		shortcutsBox.setRight(calLabel);

		return shortcutsBox;
	}

	private Node createTaskLabel() {
		Node wrappedTaskLabel = Borders.wrap(taskLbl).lineBorder().color(Color.WHITE).build().build();

		return wrappedTaskLabel;
	}

	private Node createHelpLabel() {
		HeaderBox helpShortcutBox = new HeaderBox();

		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setBottom(helpIcon);

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalLabel() {
		HeaderBox calShortcutBox = new HeaderBox();

		calShortcutBox.setTop(calLbl);
		calShortcutBox.setBottom(calIcon);
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
}
