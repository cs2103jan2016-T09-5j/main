package userinterface.view;

import java.util.ArrayList;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.tools.Borders;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import userinterface.model.InputBox;
import userinterface.model.TaskBox;
import userinterface.controller.UIController;
import userinterface.model.FeedbackBox;
import userinterface.model.HeaderBox;

public class BorderPaneLayout extends BorderPane {
	
	private ArrayList<String> _taskList;
	private String _feedback;
	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
	
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
		HeaderBox smallHeaderBox = new HeaderBox();
		HeaderBox anotherHeaderBox  = new HeaderBox();
		HeaderBox oneMoreHeaderBox = new HeaderBox();
		
		Label taskLbl = new Label("     Tasks     ");
		taskLbl.setStyle("-fx-text-fill: #FFFFFF");
		Node wrappedTaskLabel = Borders.wrap(taskLbl)
			     			.lineBorder().color(Color.WHITE).build()
			     			.build();
		Label helpLbl = new Label("  F1");
		Button helpTag = new Button("", fontAwesome.create("question").color(Color.WHITE));
		//-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;"
		helpTag.setStyle("-fx-background-color: transparent");
		helpLbl.setStyle("-fx-text-fill: #FFFFFF");
		anotherHeaderBox.setTop(helpLbl);
		anotherHeaderBox.setBottom(helpTag);
		Node wrappedHelpLabel = Borders.wrap(anotherHeaderBox)
			     			.lineBorder().color(Color.WHITE).build()
			     			.build();
		
		Label calLbl = new Label("  F2 ");
		Button calTag = new Button("", fontAwesome.create("calendar").color(Color.WHITE));
		calTag.setStyle("-fx-background-color: transparent");
		calLbl.setStyle("-fx-text-fill: #FFFFFF");
		oneMoreHeaderBox.setTop(calLbl);
		oneMoreHeaderBox.setBottom(calTag);
		Node wrappedCalLabel = Borders.wrap(oneMoreHeaderBox)
			     			.lineBorder().color(Color.WHITE).build()
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
		Text t = new Text();
		t.setText(" ");
		if (!_taskList.isEmpty()){
			t.setText(_taskList.get(0));
		}
		taskBox.getChildren().add(t);
		t.setStyle("-fx-text-fill: #FFFFFF");
		t.setFill(Color.WHITE);
//		ListView<String> taskListView = new ListView<String>();
//		taskListView = UIController.formatArrayList(_taskList);		
//		taskBox.getChildren().add(taskListView);
		this.setCenter(taskBox);
	}

	/** Set bottom region to display input section */
	private void setBottomRegion() {
		BorderPane borderPane = new BorderPane();
		FeedbackBox feedbackBox = new FeedbackBox();
		Label lbl = new Label(_feedback);
		lbl.setStyle("-fx-text-fill: #FFFFFF");
		feedbackBox.getChildren().add(lbl);
		InputBox inputBox = new InputBox();
		TextField textField = new TextField();
		textField.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
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
