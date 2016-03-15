package graphicUserInterface;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class BottomDisplay extends GridPane{
	protected Text _textConsole = new Text("Console: ");
	protected Text _textInput = new Text("Command: ");
	
	public  BottomDisplay(ArrayList<String> consoleList){
		styleGrid();
		
		BottomTopDisplay botTop = new BottomTopDisplay(consoleList);
		BottomBotDisplay botBot = new BottomBotDisplay();
		botBot.handleUserInput(botBot.getTextField());
		
		styleNodes(botTop, botBot);
		
		this.getChildren().addAll(_textConsole, botBot, _textInput, botTop);
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
	
	private void styleNodes(BottomTopDisplay botTop, BottomBotDisplay botBot) {
		this.setConstraints(_textConsole, 0, 0); // column=0 row=0
		this.setConstraints(botTop, 0, 1); // column=0 row=1
		this.setConstraints(_textInput, 0, 2); // column=0 row=2
		this.setConstraints(botBot, 0, 3); // column=0 row=3
	}
}
