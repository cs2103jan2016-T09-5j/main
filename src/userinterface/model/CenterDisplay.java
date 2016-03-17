package userinterface.model;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import userinterface.controller.*;

/**
 * Display ArrayList containing console strings for center region of main GUI
 * class
 * 
 * @author Rebekah
 */

public class CenterDisplay extends VBox {
	private Text _textConsole = new Text("Console: ");
	private static ArrayList<String> _consoleList;
	private static ListView<String> _consoleListView;

	public CenterDisplay(ArrayList<String> consoleList) {
		_consoleList = consoleList;
		_consoleListView = ClockworkGUIController.formatArrayList(_consoleList);
		styleVBox();
		styleListView(_consoleListView);
		this.getChildren().addAll(_textConsole, _consoleListView);
	}

	private void styleVBox() {
		this.setPadding(new Insets(5, 12, 15, 12));
		this.setSpacing(10);
		this.setStyle("-fx-background-color: #FFFFFF;");
	}

	private void styleListView(ListView<String> consoleListView) {
		consoleListView.setPrefSize(100, 200);
	}
}
