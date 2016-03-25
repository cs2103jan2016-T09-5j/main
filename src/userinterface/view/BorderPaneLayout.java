package userinterface.view;

import java.util.ArrayList;

import org.controlsfx.tools.Borders;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import userinterface.model.InputBox;
import userinterface.model.TaskBox;
import userinterface.model.HeaderBox;

public class BorderPaneLayout extends BorderPane {
	
	private ArrayList<String> _taskList;

	public BorderPaneLayout(ArrayList<String> taskList) {
		_taskList = taskList;
		this.setDisplayRegions();
	}
	
	public void refresh(ArrayList<String> taskList) {
		_taskList = taskList;
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
		Label taskLbl = new Label("Tasks");
		Node wrappedTaskLabel = Borders.wrap(taskLbl)
			     			.lineBorder().color(Color.BLUE).build()
			     			.build();
		HeaderBox smallHeaderBox = new HeaderBox();
		Label helpLbl = new Label("F1: Help");
		Node wrappedHelpLabel = Borders.wrap(helpLbl)
			     			.lineBorder().color(Color.RED).build()
			     			.build();
		Label calLbl = new Label("F2: Calendar");
		Node wrappedCalLabel = Borders.wrap(calLbl)
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
		Label lbl = new Label("JavaFX 2 StackPane");
	    taskBox.getChildren().add(lbl);
	    Button btn = new Button("Button");
	    taskBox.getChildren().add(btn);
//		this.getChildren().addAll(_textTask, _consoleInput, _textInput, _taskListView);
		this.setCenter(taskBox);
	}

	/** Set bottom region to display input section */
	private void setBottomRegion() {
		InputBox inputBox = new InputBox();
		Label lbl = new Label("JavaFX 2 StackPane");
	    inputBox.getChildren().add(lbl);
	    Button btn = new Button("Button");
	    inputBox.getChildren().add(btn);
//		this.getChildren().addAll(_textTask, _consoleInput, _textInput, _taskListView);
		this.setBottom(inputBox);
	}
}
