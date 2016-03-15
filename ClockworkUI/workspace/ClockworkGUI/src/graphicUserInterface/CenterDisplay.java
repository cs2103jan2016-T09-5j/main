package graphicUserInterface;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Display ArrayList containing task strings for center region of main GUI class
 * 
 * @author Rebekah
 */

public class CenterDisplay extends VBox {
	private Text _taskText = new Text("Tasks: ");
	private ArrayList _taskList;
	private ListView<String> _taskListView;
	
	public CenterDisplay(ArrayList taskList){
		_taskList = taskList;
		_taskListView = ClockworkGUI.formatArrayList(taskList);
		styleVBox();
		styleListView(_taskListView);
		this.getChildren().addAll(_taskText, _taskListView);
	}
	
	public void changeDisplayList(ArrayList newList){
		_taskList.clear();
		_taskList.addAll(newList);
		_taskListView = ClockworkGUI.formatArrayList(newList);
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


