package userinterface.view;

import java.util.ArrayList;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import userinterface.model.InputBox;
import userinterface.model.TaskBox;
import userinterface.controller.UIController;
import userinterface.model.FeedbackBox;
import userinterface.model.HeaderBox;

public class BorderPaneLayout extends BorderPane {
	
	private ArrayList<String> _taskList;
	private String _feedback;
	
	public BorderPaneLayout(){
		this.setDisplayRegions();
	};
	public BorderPaneLayout(ArrayList<String> taskList, String feedback) {
		_taskList = taskList;
		_feedback = feedback;
		this.setDisplayRegions();
	}

	/*
	 * ===========================================
	 * Populating Layout
	 * ===========================================
	 */

	/** Set top, left, center, right and bottom region to display information */
	public void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}

	/** Set top region to display welcome text */
	private void setTopRegion() {
		HeaderBox headerBox = new HeaderBox();
		Label taskLbl = new Label("     Tasks     ");
		Node wrappedTaskLabel = Borders.wrap(taskLbl)
			     			.lineBorder().color(Color.BLUE).build()
			     			.build();
		HeaderBox smallHeaderBox = new HeaderBox();
		HeaderBox anotherHeaderBox  = new HeaderBox();
		HeaderBox oneMoreHeaderBox = new HeaderBox();
		Label helpLbl = new Label("  F1  ");
		Label helpTag = new Label("Help");
		anotherHeaderBox.setTop(helpLbl);
		anotherHeaderBox.setBottom(helpTag);
		Node wrappedHelpLabel = Borders.wrap(anotherHeaderBox)
			     			.lineBorder().color(Color.RED).build()
			     			.build();
		Label calLbl = new Label("    F2    ");
		Label calTag = new Label ("Calendar");
		oneMoreHeaderBox.setTop(calLbl);
		oneMoreHeaderBox.setBottom(calTag);
		Node wrappedCalLabel = Borders.wrap(oneMoreHeaderBox)
			     			.lineBorder().color(Color.RED).build()
			     			.build();
		smallHeaderBox.setLeft(wrappedHelpLabel);
		smallHeaderBox.setRight(wrappedCalLabel);
	    headerBox.setLeft(wrappedTaskLabel);
	    headerBox.setRight(smallHeaderBox);
		this.setTop(headerBox);
	}

	/** Set center region to display task list */
	private void setCenterRegion() {
		TaskBox taskBox = new TaskBox();
		ListView<String> taskListView = new ListView<String>();
		taskListView = UIController.formatArrayList(_taskList);
		taskBox.getChildren().add(taskListView);
		this.setCenter(taskBox);
	}

	/** Set bottom region to display input section */
	private void setBottomRegion() {
		BorderPane borderPane = new BorderPane();
		FeedbackBox feedbackBox = new FeedbackBox();
		Label lbl = new Label(_feedback);
		feedbackBox.getChildren().add(lbl);
		InputBox inputBox = new InputBox();
		TextField textField = new TextField();
		UIController.implementKeystrokeEvents(textField);
		TextFields.bindAutoCompletion(
	            textField,
	            "add", "delete", "undo", "search", "display", "mark", "edit");
	    inputBox.getChildren().add(textField);
	    borderPane.setTop(feedbackBox);
	    borderPane.setBottom(inputBox);
		this.setBottom(borderPane);
	}
}
