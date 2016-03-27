package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class HelpLayout extends BorderPane {
	
	private Label dummyLbl = new Label(" ");

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
		HeaderBox headerBox = new HeaderBox();
		this.setTop(headerBox);
	}

	private void setCenterRegion() {
		wrappedAdd = createAddNode();
		wrappedDelete = createDeleteNode();
		wrappedEdit = createEditNode();
		wrappedUndo = createUndoNode();
		wrappedRedo = createRedoNode();
		wrappedSearch = createSearchNode();
		wrappedDisplay = createDisplayNode();
		wrappedMark = createMarkNode();
		wrappedEmail = createEmailNode();

		implementHelpContentBar();
		implementHelpContentBox();

		this.setCenter(helpContentBox);
	}

	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	/** IMPLEMENTING REGION OBJECTS */

	private void implementHelpContentBox() {	
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.CENTER);
		helpContentBox.setStyle("-fx-background-color: #182733");
		
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		helpContentBox.getColumnConstraints().add(columnConstraints);
	}

	private void implementHelpContentBar() {
		helpContentBar.getChildren().addAll(wrappedAdd, wrappedDelete, wrappedEdit, wrappedUndo, wrappedRedo,
				wrappedSearch, wrappedDisplay, wrappedMark, wrappedEmail);
		helpContentBar.setAlignment(Pos.CENTER);
		helpContentBar.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
	}

	private TextField implementTextField() {
		InputBox textField = new InputBox();
		textField.setEditable(false);

		return textField;
	}

	/** CREATING LAYOUT OBJECTS */

	private Node createEmailNode() {
		BorderPane emailBox = new BorderPane();

		Label emailLbl = new Label("Email");

		emailBox.setTop(emailLbl);
		emailBox.setCenter(dummyLbl);
		emailBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.ENVELOPE));

		Node wrappedEmail = Borders.wrap(emailBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEmail;
	}

	private Node createMarkNode() {
		BorderPane markBox = new BorderPane();

		Label markLbl = new Label("Mark");

		markBox.setTop(markLbl);
		markBox.setCenter(dummyLbl);
		markBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CHECK));

		Node wrappedMark = Borders.wrap(markBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedMark;
	}

	private Node createDisplayNode() {
		BorderPane displayBox = new BorderPane();

		Label displayLbl = new Label("Display");

		displayBox.setTop(displayLbl);
		displayBox.setCenter(dummyLbl);
		displayBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.TABLET));

		Node wrappedDisplay = Borders.wrap(displayBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedDisplay;
	}

	private Node createSearchNode() {
		BorderPane searchBox = new BorderPane();

		Label searchLbl = new Label("Search");

		searchBox.setTop(searchLbl);
		searchBox.setCenter(dummyLbl);
		searchBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.SEARCH));

		Node wrappedSearch = Borders.wrap(searchBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedSearch;
	}

	private Node createRedoNode() {
		BorderPane redoBox = new BorderPane();

		Label redoLbl = new Label("Redo");

		redoBox.setTop(redoLbl);
		redoBox.setCenter(dummyLbl);
		redoBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPEAT));

		Node wrappedRedo = Borders.wrap(redoBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedRedo;
	}

	private Node createUndoNode() {
		BorderPane undoBox = new BorderPane();

		Label undoLbl = new Label("Undo");

		undoBox.setTop(undoLbl);
		undoBox.setCenter(dummyLbl);
		undoBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.UNDO));

		Node wrappedUndo = Borders.wrap(undoBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedUndo;
	}

	private Node createEditNode() {
		BorderPane editBox = new BorderPane();

		Label editLbl = new Label("Edit");

		editBox.setTop(editLbl);
		editBox.setCenter(dummyLbl);
		editBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.PENCIL));

		Node wrappedEdit = Borders.wrap(editBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEdit;
	}

	private Node createAddNode() {
		BorderPane addBox = new BorderPane();

		Label addLbl = new Label("Add");

		addBox.setTop(addLbl);
		addBox.setCenter(dummyLbl);
		addBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.PLUS));

		Node wrappedAdd = Borders.wrap(addBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedAdd;
	}

	private Node createDeleteNode() {
		BorderPane deleteBox = new BorderPane();

		Label deleteLbl = new Label("Delete");

		deleteBox.setTop(deleteLbl);
		deleteBox.setCenter(dummyLbl);
		deleteBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));

		Node wrappedDelete = Borders.wrap(deleteBox).lineBorder().color(Color.WHITE).build().build();
		return wrappedDelete;
	}
}
