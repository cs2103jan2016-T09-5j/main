package userinterface.view;

import java.util.ArrayList;

import javafx.scene.layout.BorderPane;
import userinterface.model.BottomDisplay;
import userinterface.model.CenterDisplay;
import userinterface.model.LeftDisplay;
import userinterface.model.RightDisplay;
import userinterface.model.TopDisplay;

public class ClockworkGUILayout extends BorderPane {
	
	private ArrayList<String> _consoleList;
	private ArrayList<String> _taskList;
	private ArrayList<String> _helpList;

	public ClockworkGUILayout(ArrayList<String> consoleList, ArrayList<String> taskList, ArrayList<String> helpList) {
		_consoleList = consoleList;
		_taskList = taskList;
		_helpList = helpList;
		this.setDisplayRegions();
	}
	
	public void refresh(ArrayList<String> consoleList, ArrayList<String> taskList, ArrayList<String> helpList) {
		_consoleList = consoleList;
		_taskList = taskList;
		_helpList = helpList;
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
		setLeftRegion();
		setCenterRegion();
		setRightRegion();
	}

	/** Set top region to display welcome text */
	private void setTopRegion() {
		TopDisplay topSection = new TopDisplay();
		this.setTop(topSection);
	}

	/** Set left region to display help list */
	private void setLeftRegion() {
		LeftDisplay leftSection = new LeftDisplay(_helpList);
		this.setLeft(leftSection);
	}

	/** Set center region to display console */
	private void setCenterRegion() {
		CenterDisplay centerSection = new CenterDisplay(_consoleList);
		this.setCenter(centerSection);
	}

	/** Set right region to display calendar widget [INCOMPLETE] */
	private void setRightRegion() {
		RightDisplay rightSection = new RightDisplay();
		this.setRight(rightSection);
	}

	/** Set bottom region to display task list and input section */
	private void setBottomRegion() {
		BottomDisplay bottomSection = new BottomDisplay(_taskList);
		this.setBottom(bottomSection);
	}
}
