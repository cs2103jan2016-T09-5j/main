package userinterface.view;

import java.util.ArrayList;

import javafx.scene.layout.BorderPane;
import userinterface.model.BottomDisplay;
import userinterface.model.CenterDisplay;
import userinterface.model.TopDisplay;

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
		TopDisplay topSection = new TopDisplay();
		this.setTop(topSection);
	}

	/** Set center region to display task list */
	private void setCenterRegion() {
		CenterDisplay centerSection = new CenterDisplay(_taskList);
		this.setCenter(centerSection);
	}

	/** Set bottom region to display input section */
	private void setBottomRegion() {
		BottomDisplay bottomSection = new BottomDisplay(_taskList);
		this.setBottom(bottomSection);
	}
}
