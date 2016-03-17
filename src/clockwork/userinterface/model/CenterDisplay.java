package clockwork.userinterface.model;

import java.util.ArrayList;

import clockwork.userinterface.controller.*;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Display ArrayList containing console strings for center region of main GUI
 * class
 * 
 * @author Rebekah
 */

public class CenterDisplay extends VBox {
	private Text _textConsole = new Text("Console: ");
	private static ArrayList _consoleList;
	private static ListView<String> _consoleListView;

	public CenterDisplay(ArrayList consoleList) {
		_consoleList = consoleList;
		_consoleListView = ClockworkGUIController.formatArrayList(_consoleList);
		styleVBox();
		styleListView(_consoleListView);
		this.getChildren().addAll(_textConsole, _consoleListView);
	}

	public static void changeTaskList(ArrayList newList) {
		_consoleList.clear();
		_consoleList.addAll(newList);
		_consoleListView = ClockworkGUIController.formatArrayList(_consoleList);
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
