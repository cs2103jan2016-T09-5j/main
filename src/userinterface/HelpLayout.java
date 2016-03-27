package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class HelpLayout extends BorderPane {
	
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
	private GridPane helpContentBox = new GridPane();	
	private HBox helpContentBar = new HBox();
	
	private Node wrappedAdd;
	private Node wrappedDelete;
	private Node wrappedEdit;
	private Node wrappedUndo;
	private Node wrappedRedo;
	private Node wrappedSearch;
	private Node wrappedDisplay;
	private Node wrappedMark;
	private Node wrappedEmail;

	public HelpLayout() {
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

	private void setCenterRegion() {

		wrappedAdd = createAdd();
		wrappedDelete = createDelete();
		wrappedEdit = createEdit();
		wrappedUndo = createUndo();
		wrappedRedo = createRedo();
		wrappedSearch = createSearch();
		wrappedDisplay = createDisplay();
		wrappedMark = createMark();
		wrappedEmail = createEmail();
	
		implementHelpContentBar();
		
		implementHelpContentBox();
		
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
		helpLabel = createHelpLabel();
		calLabel = createCalLabel();
		escLabel = createEscLabel();	
	}
	
	private void implementHelpContentBox() {
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.CENTER);
		helpContentBox.setStyle("-fx-background-color: #182733");
	}

	private void implementHelpContentBar() {
		helpContentBar.getChildren().addAll(wrappedAdd, wrappedDelete, wrappedEdit, 
								wrappedUndo, wrappedRedo, wrappedSearch, 
								wrappedDisplay,	wrappedMark, wrappedEmail);
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

		Label dummyLbl = new Label(" ");
		
		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setCenter(dummyLbl);
		helpShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.QUESTION));

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalLabel() {
		HeaderBox calShortcutBox = new HeaderBox();

		Label dummyLbl = new Label(" ");
		
		calShortcutBox.setTop(calLbl);
		calShortcutBox.setCenter(dummyLbl);
		calShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR));
		
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
	
	private Node createEscLabel(){
		HeaderBox escShortcutBox = new HeaderBox();
		
		Label dummyLbl = new Label(" ");
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setCenter(dummyLbl);
		escShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPLY));
		
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
	
	private Node createEmail() {
		BorderPane emailBox = new BorderPane();
		
		Label emailLbl = new Label("Email");
		
		emailBox.setTop(emailLbl);
		emailBox.setCenter(dummyLbl);
		emailBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.ENVELOPE));
		
		Node wrappedEmail = Borders.wrap(emailBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedEmail;
	}
	
	private Node createMark() {
		BorderPane markBox = new BorderPane();
		
		Label markLbl = new Label("Mark");
		
		markBox.setTop(markLbl);
		markBox.setCenter(dummyLbl);
		markBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CHECK));
		
		Node wrappedMark = Borders.wrap(markBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedMark;
	}
	
	private Node createDisplay() {
		BorderPane displayBox = new BorderPane();
		
		Label displayLbl = new Label("Display");
		
		displayBox.setTop(displayLbl);
		displayBox.setCenter(dummyLbl);
		displayBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.TABLET));
		
		Node wrappedDisplay = Borders.wrap(displayBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedDisplay;
	}
	
	private Node createSearch() {
		BorderPane searchBox = new BorderPane();
		
		Label searchLbl = new Label("Search");
		
		searchBox.setTop(searchLbl);
		searchBox.setCenter(dummyLbl);
		searchBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.SEARCH));
		
		Node wrappedSearch = Borders.wrap(searchBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedSearch;
	}
	
	private Node createRedo() {
		BorderPane redoBox = new BorderPane();
		
		Label redoLbl = new Label("Redo");
		
		redoBox.setTop(redoLbl);
		redoBox.setCenter(dummyLbl);
		redoBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPEAT));
		
		Node wrappedRedo = Borders.wrap(redoBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedRedo;
	}
	
	private Node createUndo() {
		BorderPane undoBox = new BorderPane();
		
		Label undoLbl = new Label("Undo");
		
		undoBox.setTop(undoLbl);
		undoBox.setCenter(dummyLbl);
		undoBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.UNDO));
		
		Node wrappedUndo = Borders.wrap(undoBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedUndo;
	}
	
	private Node createEdit() {
		BorderPane editBox = new BorderPane();
		
		Label editLbl = new Label("Edit");
		
		editBox.setTop(editLbl);
		editBox.setCenter(dummyLbl);
		editBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.PENCIL));
		
		Node wrappedEdit = Borders.wrap(editBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedEdit;
	}

	private Node createAdd() {
		BorderPane addBox = new BorderPane();
		
		Label addLbl = new Label("Add");
		
		addBox.setTop(addLbl);
		addBox.setCenter(dummyLbl);
		addBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.PLUS));
		
		Node wrappedAdd = Borders.wrap(addBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedAdd;
	}
	
	private Node createDelete() {
		BorderPane deleteBox = new BorderPane();
		
		Label deleteLbl = new Label("Delete");
		
		deleteBox.setTop(deleteLbl);
		deleteBox.setCenter(dummyLbl);
		deleteBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));
		
		Node wrappedDelete = Borders.wrap(deleteBox).lineBorder().color(Color.WHITE).build().build();
		return wrappedDelete;
	}
}
