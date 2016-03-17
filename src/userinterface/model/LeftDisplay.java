package userinterface.model;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import userinterface.controller.*;

/**
 * Display ArrayList containing help strings for left region of main GUI class
 * 
 * @author Rebekah
 */

public class LeftDisplay extends VBox {
	private Text _textHelp = new Text("Help: ");
	private ArrayList<String> _helpList;
	private ListView<String> _helpListView;

	public LeftDisplay(ArrayList<String> helpList) {
		_helpList = helpList;
		_helpListView = ClockworkGUIController.formatArrayList(helpList);
		styleVBox();
		styleListView(_helpListView);
		this.getChildren().addAll(_textHelp, _helpListView);
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
