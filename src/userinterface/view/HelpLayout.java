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
import userinterface.controller.UIController;
import userinterface.model.HeaderBox;

public class HelpLayout extends BorderPane {

//	private static final String GLYPH_NAME_ADD = "PLUS";
//	private static final String GLYPH_NAME_DELETE = "MINUS";
//	private static final String GLYPH_NAME_EDIT = "PENCIL";
//	private static final String GLYPH_NAME_UNDO = "UNDO";
//	private static final String GLYPH_NAME_REDO = "REPEAT";
//	private static final String GLYPH_NAME_SEARCH = "SEARCH";
//	private static final String GLYPH_NAME_DISPLAY = "TABLET";
//	private static final String GLYPH_NAME_MARK = "CHECK";
//	private static final String GLYPH_NAME_EMAIL = "ENVELOPE";
	
	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
	
	private Node taskLabel;
	private Node helpLabel;
	private Node calLabel;
	private Node escLabel;
	
	private Label taskLbl = new Label("     Tasks     ");
	private Label helpLbl = new Label("  F1");
	private Label calLbl = new Label("  F2 ");
	private Label escLbl = new Label("  Esc ");
	
	private Button helpIcon = new Button("", fontAwesome.create("question").color(Color.WHITE));
	private Button calIcon = new Button("", fontAwesome.create("calendar").color(Color.WHITE));
	private Button escIcon = new Button("", fontAwesome.create("reply").color(Color.WHITE));	
		
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
		style();
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
		helpContentBox.setStyle("-fx-background-color: #182733");
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.CENTER);
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

		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setBottom(helpIcon);

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalLabel() {
		HeaderBox calShortcutBox = new HeaderBox();

		calShortcutBox.setTop(calLbl);
		calShortcutBox.setBottom(calIcon);
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
	
	private Node createEscLabel(){
		HeaderBox escShortcutBox = new HeaderBox();
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setBottom(escIcon);
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
	
	private Node createEmail() {
		BorderPane emailBox = new BorderPane();
		emailBox.setStyle("-fx-background-color: #182733");
		
		Label emailLbl = new Label("Email");
		Button emailIcon = new Button("", fontAwesome.create("envelope").color(Color.WHITE));
		emailIcon.setStyle("-fx-background-color: transparent");
		emailLbl.setStyle("-fx-text-fill: #FFFFFF");
		emailBox.setTop(emailLbl);
		emailBox.setBottom(emailIcon);
		
		Node wrappedEmail = Borders.wrap(emailBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedEmail;
	}
	
	private Node createMark() {
		BorderPane markBox = new BorderPane();
		markBox.setStyle("-fx-background-color: #182733");
		
		Label markLbl = new Label("Mark");
		Button markIcon = new Button("", fontAwesome.create("check").color(Color.WHITE));
		markIcon.setStyle("-fx-background-color: transparent");
		markLbl.setStyle("-fx-text-fill: #FFFFFF");
		markBox.setTop(markLbl);
		markBox.setBottom(markIcon);
		
		Node wrappedMark = Borders.wrap(markBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedMark;
	}
	
	private Node createDisplay() {
		BorderPane displayBox = new BorderPane();
		displayBox.setStyle("-fx-background-color: #182733");
		
		Label displayLbl = new Label("Display");
		Button displayIcon = new Button("", fontAwesome.create("tablet").color(Color.WHITE));
		displayIcon.setStyle("-fx-background-color: transparent");
		displayLbl.setStyle("-fx-text-fill: #FFFFFF");
		displayBox.setTop(displayLbl);
		displayBox.setBottom(displayIcon);
		
		Node wrappedDisplay = Borders.wrap(displayBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedDisplay;
	}
	
	private Node createSearch() {
		BorderPane searchBox = new BorderPane();
		searchBox.setStyle("-fx-background-color: #182733");
		
		Label searchLbl = new Label("Search");
		Button searchIcon = new Button("", fontAwesome.create("search").color(Color.WHITE));
		searchIcon.setStyle("-fx-background-color: transparent");
		searchLbl.setStyle("-fx-text-fill: #FFFFFF");
		searchBox.setTop(searchLbl);
		searchBox.setBottom(searchIcon);
		
		Node wrappedSearch = Borders.wrap(searchBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedSearch;
	}
	
	private Node createRedo() {
		BorderPane redoBox = new BorderPane();
		redoBox.setStyle("-fx-background-color: #182733");
		
		Label redoLbl = new Label("Redo");
		Button redoIcon = new Button("", fontAwesome.create("repeat").color(Color.WHITE));
		redoIcon.setStyle("-fx-background-color: transparent");
		redoLbl.setStyle("-fx-text-fill: #FFFFFF");
		redoBox.setTop(redoLbl);
		redoBox.setBottom(redoIcon);
		
		Node wrappedRedo = Borders.wrap(redoBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedRedo;
	}
	
	private Node createUndo() {
		BorderPane undoBox = new BorderPane();
		undoBox.setStyle("-fx-background-color: #182733");
		
		Label undoLbl = new Label("Undo");
		Button undoIcon = new Button("", fontAwesome.create("undo").color(Color.WHITE));
		undoIcon.setStyle("-fx-background-color: transparent");
		undoLbl.setStyle("-fx-text-fill: #FFFFFF");
		undoBox.setTop(undoLbl);
		undoBox.setBottom(undoIcon);
		
		Node wrappedUndo = Borders.wrap(undoBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedUndo;
	}
	
	private Node createEdit() {
		BorderPane editBox = new BorderPane();
		editBox.setStyle("-fx-background-color: #182733");
		
		Label editLbl = new Label("Edit");
		Button editIcon = new Button("", fontAwesome.create("pencil").color(Color.WHITE));
		editIcon.setStyle("-fx-background-color: transparent");
		editLbl.setStyle("-fx-text-fill: #FFFFFF");
		editBox.setTop(editLbl);
		editBox.setBottom(editIcon);
		
		Node wrappedEdit = Borders.wrap(editBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedEdit;
	}

	private Node createAdd() {
		BorderPane addBox = new BorderPane();
		addBox.setStyle("-fx-background-color: #182733");
		
		Label addLbl = new Label("Add");
		Button addIcon = new Button("", fontAwesome.create("plus").color(Color.WHITE));
		addIcon.setStyle("-fx-background-color: transparent");
		addLbl.setStyle("-fx-text-fill: #FFFFFF");
		addBox.setTop(addLbl);
		addBox.setBottom(addIcon);
		
		Node wrappedAdd = Borders.wrap(addBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedAdd;
	}
	
	private Node createDelete() {
		BorderPane deleteBox = new BorderPane();
		deleteBox.setStyle("-fx-background-color: #182733");
		
		Label deleteLbl = new Label("Delete");
		Button deleteIcon = new Button("", fontAwesome.create("minus").color(Color.WHITE));
		deleteIcon.setStyle("-fx-background-color: transparent");
		deleteLbl.setStyle("-fx-text-fill: #FFFFFF");
		deleteBox.setTop(deleteLbl);
		deleteBox.setBottom(deleteIcon);
		
		Node wrappedDelete = Borders.wrap(deleteBox).lineBorder().color(Color.WHITE).build().build();
		return wrappedDelete;
	}
	
	/** STYLING FIXED OBJECTS */
	
	private void style(){
		taskLbl.setStyle("-fx-text-fill: #FFFFFF");
		helpIcon.setStyle("-fx-background-color: transparent");
		helpLbl.setStyle("-fx-text-fill: #FFFFFF");
		calIcon.setStyle("-fx-background-color: transparent");
		calLbl.setStyle("-fx-text-fill: #FFFFFF");
		escIcon.setStyle("-fx-background-color: transparent");
		escLbl.setStyle("-fx-text-fill: #FFFFFF");
		
		helpContentBar.setStyle("-fx-background-color: #182733");
	}
}
