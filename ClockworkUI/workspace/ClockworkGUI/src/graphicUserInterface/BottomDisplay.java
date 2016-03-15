package graphicUserInterface;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BottomDisplay extends GridPane{
	private ArrayList _displayList;
	private ListView<String> _displayListView;
	
	public  BottomDisplay(ArrayList<String> displayList){
		_displayList = displayList;
		_displayListView = ClockworkGUI.formatArrayList(displayList);
		styleGrid();
	}
	
	private void styleGrid() {
		/** to display previous commands */
		this.setHgap(5);
		this.setVgap(5);
		this.setPadding(new Insets(5, 5, 5, 5));
		
		/** for user to key input */
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setVgap(5);
		this.setHgap(5);
		
		this.getRowConstraints().add(new RowConstraints(10)); // row 0 is 10 high
		this.getRowConstraints().add(new RowConstraints(280)); // row 0 is 280 high
		this.getRowConstraints().add(new RowConstraints(10)); // row 1 is 10 high
		this.getRowConstraints().add(new RowConstraints(30)); // row 1 is 30 high
	}
}
