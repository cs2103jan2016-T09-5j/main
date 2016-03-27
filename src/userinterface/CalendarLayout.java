package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class CalendarLayout extends BorderPane {
	
	private Node taskLabel;
	private Node helpLabel;
	private Node calLabel;
	private Node escLabel;
	
	private Label taskLbl = new Label("     Tasks     ");
	private Label helpLbl = new Label("F1");
	private Label calLbl = new Label("F2");
	private Label escLbl = new Label("Esc");
	private Label dummyLbl = new Label(" ");
	
	private HeaderBox headerBox = new HeaderBox();
	private GridPane calendarBox = new GridPane();

	public CalendarLayout() {
		setDisplayRegions();
	}

	/** POPULATING LAYOUT */
	
	private void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}
	
	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		implementHeaderLabels();
		implementHeaderBox();
		
		this.setTop(headerBox);
	}

	/************** IMPLEMENT CALENDAR HERE! (: **********/
	private void setCenterRegion() {
		//calendarBox is a dummy variable, you might wanna change it!
		this.setCenter(calendarBox);
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
		helpLabel = createHelpLabel();
		calLabel = createCalLabel();
		escLabel = createEscLabel();	
	}

	private TextField implementTextField() {
		TextField textField = new TextField();
		textField.setEditable(false);
		
		textField.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
		
		handleKeyPress(textField);
		
		return textField;
	}

	private void handleKeyPress(TextField textField) {
		UIController.implementKeystrokeEvents(textField);
	}
	
	/** CREATING LAYOUT OBJECTS */
	
	private Node createShortcutBox(){
		HeaderBox shortcutsBox = new HeaderBox();
		shortcutsBox.setLeft(helpLabel);
		shortcutsBox.setCenter(calLabel);
		shortcutsBox.setRight(escLabel);
		
		return shortcutsBox;
	}
	
	private Node createTaskLabel(){
		Node wrappedTaskLabel = Borders.wrap(taskLbl).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedTaskLabel;
	}
	
	private Node createHelpLabel() {
		HeaderBox helpShortcutBox = new HeaderBox();

		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setCenter(dummyLbl);
		helpShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.QUESTION));

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalLabel() {
		HeaderBox calShortcutBox = new HeaderBox();

		calShortcutBox.setTop(calLbl);
		calShortcutBox.setCenter(dummyLbl);
		calShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR));
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
	
	private Node createEscLabel(){
		HeaderBox escShortcutBox = new HeaderBox();
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setCenter(dummyLbl);
		escShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPLY));
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
}
