package userinterface.view;

import java.util.Random;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ColorGridCell;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.tools.Borders;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import userinterface.controller.Main;
import userinterface.controller.UIController;
import userinterface.model.HeaderBox;
import userinterface.model.TaskBox;

public class HelpLayout extends BorderPane {

	private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
	
	private Node taskLabel;
	private Node helpLabel;
	private Node calLabel;
	
	Node wrappedAdd;
	Node wrappedDelete;
	Node wrappedEdit;
	Node wrappedUndo;
	Node wrappedRedo;
	Node wrappedSearch;
	Node wrappedDisplay;
	Node wrappedMark;
	Node wrappedEmail;
	
	private Label taskLbl = new Label("     Tasks     ");
	private Label helpLbl = new Label("  F1");
	private Label calLbl = new Label("  F2 ");
	
	private Button helpIcon = new Button("", fontAwesome.create("question").color(Color.WHITE));
	private Button calIcon = new Button("", fontAwesome.create("calendar").color(Color.WHITE));	
		
	private HeaderBox headerBox = new HeaderBox();
	private GridPane helpContentBox = new GridPane();	
	private HBox helpContentBar = new HBox();

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

		Node wrappedAdd = createAdd();
		Node wrappedDelete = createDelete();
		Node wrappedEdit = createEdit();
		Node wrappedUndo = createUndo();
		Node wrappedRedo = createRedo();
		Node wrappedSearch = createSearch();
		Node wrappedDisplay = createDisplay();
		Node wrappedMark = createMark();
		Node wrappedEmail = createEmail();

		helpContentBar.getChildren().addAll(wrappedAdd, wrappedDelete, wrappedEdit, 
								wrappedUndo, wrappedRedo, wrappedSearch, 
								wrappedDisplay,	wrappedMark, wrappedEmail);
		
		helpContentBox.setStyle("-fx-background-color: #182733");
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.CENTER);
		
		this.setCenter(helpContentBox);
	}
	
	private void implementHeaderBox(){
		headerBox.setLeft(taskLabel);
		headerBox.setRight(createShortcutBox());
	}
	
	private void implementHeaderLabels(){
		taskLabel = createTaskLabel();
		helpLabel = createHelpLabel();
		calLabel = createCalLabel();	
	}
	
	private void implementHelpContent(){
		
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
	
	private Node createHelpLabel(){
		HeaderBox helpShortcutBox = new HeaderBox();
		
		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setBottom(helpIcon);
		
		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();
		
		return wrappedHelpLabel;
	}
	
	private Node createCalLabel(){
		HeaderBox calShortcutBox = new HeaderBox();
		
		calShortcutBox.setTop(calLbl);
		calShortcutBox.setBottom(calIcon);
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}

	private void setBottomRegion() {
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
	
	/** STYLING FIXED OBJECTS */
	
	private void style(){
		taskLbl.setStyle("-fx-text-fill: #FFFFFF");
		helpIcon.setStyle("-fx-background-color: transparent");
		helpLbl.setStyle("-fx-text-fill: #FFFFFF");
		calIcon.setStyle("-fx-background-color: transparent");
		calLbl.setStyle("-fx-text-fill: #FFFFFF");
		
		helpContentBar.setStyle("-fx-background-color: #182733");
	}
}
