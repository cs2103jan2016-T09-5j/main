package graphicUserInterface;

import java.util.ArrayList;

import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class BottomTopDisplay extends ListView<String> {
	private static ArrayList<String> _consoleList;
	private static ListView<String> _consoleListView;
	
	public BottomTopDisplay(ArrayList<String> consoleList){
		_consoleList = consoleList;
		_consoleListView = ClockworkGUI.formatArrayList(_consoleList);
		styleListView();
	}
	
	public static ArrayList<String> getConsoleList() {
		return _consoleList;
	}
	
	public static void changeConsoleList(ArrayList<String> newList){
		_consoleList.clear();
		_consoleList.addAll(newList);
		_consoleListView = ClockworkGUI.formatArrayList(_consoleList);
	}
	
	private void styleListView(){
		this.setPrefSize(900, 250);
		this.setCellFactory(TextFieldListCell.forListView());
	}
}
