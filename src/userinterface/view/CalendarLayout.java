package userinterface.view;

import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.tools.Borders;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import userinterface.controller.Main;
import userinterface.model.HeaderBox;

public class CalendarLayout extends BorderPane {
	
	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
	
	private Node taskLabel;
	private Node helpLabel;
	private Node calLabel;
	
	private Label taskLbl = new Label("     Tasks     ");
	private Label escLbl = new Label("  Esc ");
		
	private Button helpIcon = new Button("", fontAwesome.create("question").color(Color.WHITE));
	private Button escIcon = new Button("", fontAwesome.create("reply").color(Color.WHITE));
	
	private HeaderBox headerBox = new HeaderBox();
	private GridPane helpContentBox = new GridPane();	
	private HBox helpContentBar = new HBox();

	public CalendarLayout() {
		setDisplayRegions();
	}

	/** POPULATING LAYOUT */
	
	private void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
		style();
	}
	
	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		implementHeaderLabels();
		implementHeaderBox();
		
		this.setTop(headerBox);
	}

	private void setCenterRegion() {
		this.setCenter(helpContentBox);
	}
	
	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}
	
	/** IMPLEMENTING REGION OBJECTS */
	
	private void implementHeaderBox(){
		headerBox.setLeft(taskLabel);
		headerBox.setRight(createShortcutBox());
	}
	
	private void implementHeaderLabels(){
		taskLabel = createTaskLabel();
		calLabel = createEscLabel();	
	}

	private TextField implementTextField() {
		TextField textField = new TextField();
		textField.setEditable(false);
		
		textField.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
		
		handleKeyPress(textField);
		
		return textField;
	}

	private void handleKeyPress(TextField textField) {
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					// DEFAULT SCENE
					Main.updateDisplay();
				}
			}
		});
	}
	
	/** CREATING LAYOUT OBJECTS */
	
	private Node createShortcutBox(){
		HeaderBox shortcutsBox = new HeaderBox();
		shortcutsBox.setLeft(helpLabel);
		shortcutsBox.setRight(calLabel);
		
		return shortcutsBox;
	}
	
	private Node createTaskLabel(){
		Node wrappedTaskLabel = Borders.wrap(taskLbl).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedTaskLabel;
	}
	
	private Node createEscLabel(){
		HeaderBox escShortcutBox = new HeaderBox();
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setBottom(escIcon);
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
	
	/** STYLING FIXED OBJECTS */
	
	private void style(){
		taskLbl.setStyle("-fx-text-fill: #FFFFFF");
		helpIcon.setStyle("-fx-background-color: transparent");
		escIcon.setStyle("-fx-background-color: transparent");
		escLbl.setStyle("-fx-text-fill: #FFFFFF");
		
		helpContentBar.setStyle("-fx-background-color: #182733");
	}
}
