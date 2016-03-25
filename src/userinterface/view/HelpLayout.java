package userinterface.view;

import java.util.Random;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ColorGridCell;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import userinterface.controller.Main;
import userinterface.controller.UIController;

public class HelpLayout extends BorderPane {
	public HelpLayout() {
		this.setStyle("-fx-background-color: #272b39;");
		TextField textField = new TextField();
		textField.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					// DEFAULT SCENE
					Main.updateDisplay();
				}
			}
		});
		textField.setEditable(false);
		this.setBottom(textField);
	}
}
