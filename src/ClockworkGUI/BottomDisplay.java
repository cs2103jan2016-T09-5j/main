package ClockworkGUI;
import java.util.ArrayList;

import javafx.event.EventHandler;

/**
 * Display ArrayList containing task list and allow user input in bottom section
 * 
 * @author - Rebekah
 */

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class BottomDisplay extends GridPane{
	private Text _textTask = new Text("Tasks: ");
	private Text _textInput = new Text("Command: ");
	private ListView<String> _taskListView;
	private static TextField _consoleInput;
	
	public  BottomDisplay(ArrayList<String> taskList){
		styleGrid();
		
		_taskListView = ClockworkGUI.formatArrayList(taskList);
		styleListView();
		
		_consoleInput = new TextField();
		ClockworkGUI.implementKeystrokeEvents(_consoleInput);
		
		setNodePositions(_taskListView, _consoleInput);
		
		this.getChildren().addAll(_textTask, _consoleInput, _textInput, _taskListView);
	}
	public static String getConsoleInput(){
		_consoleInput.clear();
		_consoleInput.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
			@Override
			 public void handle(KeyEvent ke){System.out.println("*****");
				 while(!ke.getCode().equals(KeyCode.ENTER)){
					 System.out.println("%%%");
					 if ((_consoleInput.getText() != null && !_consoleInput.getText().isEmpty())) {
						 break;
					 }
				 }
		     }
	    });
		return _consoleInput.getText();
	}
	private void styleListView(){
		_taskListView.setPrefSize(900, 200);
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
		this.setConstraints(_textTask, 0, 0); // column=0 row=0
		this.setConstraints(botTop, 0, 1); // column=0 row=1
		this.setConstraints(_textInput, 0, 2); // column=0 row=2
		this.setConstraints(botBot, 0, 3); // column=0 row=3
	}
}