package userinterface;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class BottomDisplay extends GridPane{
	private Text _textConsole = new Text("Console: ");
	private Text _textInput = new Text("Command: ");
	private ListView<String> _consoleListView;
	private TextField _consoleInput;
	
	public  BottomDisplay(ArrayList<String> consoleList){
		styleGrid();
		
		_consoleListView = ClockworkGUI.formatArrayList(consoleList);
		styleListView();
		
		_consoleInput = new TextField();
		ClockworkGUI.handleUserInput(_consoleInput);
		
		setNodePositions(_consoleListView, _consoleInput);
		
		this.getChildren().addAll(_textConsole, _consoleInput, _textInput, _consoleListView);
	}
	
	private void styleListView(){
		_consoleListView.setPrefSize(900, 200);
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
	
	private void setNodePositions(ListView<String> botTop, TextField botBot) {
		this.setConstraints(_textConsole, 0, 0); // column=0 row=0
		this.setConstraints(botTop, 0, 1); // column=0 row=1
		this.setConstraints(_textInput, 0, 2); // column=0 row=2
		this.setConstraints(botBot, 0, 3); // column=0 row=3
	}
}
