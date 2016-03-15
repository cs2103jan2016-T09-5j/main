package graphicUserInterface;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Display ArrayList containing help strings for left region of main GUI class
 * 
 * @author Rebekah
 */

public class LeftDisplay extends VBox {
	private ArrayList _displayList;
	private ListView<String> _displayListView;
	
	public LeftDisplay(Text header, ArrayList displayList){
		_displayList = displayList;
		_displayListView = ClockworkGUI.formatArrayList(displayList);
		styleVBox();
		styleListView(_displayListView);
		this.getChildren().addAll(header, _displayListView);
	}
	
	public void changeDisplayList(ArrayList newList){
		_displayList.clear();
		_displayList.addAll(newList);
		_displayListView = ClockworkGUI.formatArrayList(newList);
	}
	
	private void styleVBox() {
		this.setPadding(new Insets(5, 12, 15, 12));
		this.setSpacing(10);
		this.setStyle("-fx-background-color: #FFFFFF;");
	}
	
	private void styleListView(ListView<String> commandListView) {
		commandListView.setPrefSize(100, 200);
	}
}
