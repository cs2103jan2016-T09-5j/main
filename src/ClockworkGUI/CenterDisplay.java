package ClockworkGUI;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Display ArrayList containing console strings for center region of main GUI class
 * 
 * @author Rebekah
 */

public class CenterDisplay extends VBox {
	private Text _textConsole = new Text("Console: ");
	private static ArrayList<String> _consoleList;
	private static ListView<String> _consoleListView;
	
	public CenterDisplay(ArrayList<String> consoleList){
		_consoleList = consoleList;
		_consoleListView = ClockworkGUI.formatArrayList(_consoleList);
		styleVBox();
		styleListView(_consoleListView);
		this.getChildren().addAll(_textConsole, _consoleListView);
	}
	
	public static void changeTaskList(ArrayList<String> newList){
		_consoleList.clear();
		_consoleList.addAll(newList);
		_consoleListView = ClockworkGUI.formatArrayList(_consoleList);
	}
	
	private void styleVBox() {
		this.setPadding(new Insets(5, 12, 15, 12));
		this.setSpacing(10);
		this.setStyle("-fx-background-color: #FFFFFF;");
	}
	
	private void styleListView(ListView<String> consoleListView) {
		consoleListView.setPrefSize(100, 200);
	}

	public static ArrayList<String> getConsoleList() {
		return _consoleList;
	}
}