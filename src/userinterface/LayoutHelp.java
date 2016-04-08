package userinterface;

import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.InfoOverlay;
import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LayoutHelp extends BorderPane {
	
	private Label dummyLbl = new Label(" ");

	private GridPane helpContentBox = new GridPane();
	private HBox helpContentBar = new HBox();

	private Node wrappedAdd;
	private Node wrappedDelete;
	private Node wrappedEdit;
	private Node wrappedSearch;
	private Node wrappedMark;
	private Node wrappedUndo;
	private Node wrappedRedo;
	private Node wrappedFormat;
	
	private String formatString = new String("add <task> [from <date> to <date>] [by <date>] [priority high | medium | low] \n"
			+ "delete <taskID> [<taskID> <taskID> ...] \n"
			+ "edit <taskID> [<new_task_description>] [from <date> to <date> | by <date> | date none] [priority high | medium | low | none] \n"
			+ "search <keyword> [between <date> and <date> | after <date> | before <date> | date none] [priority high | medium | low | none] [type comp | pend] \n"
			+ "mark <taskID> [<taskID> <taskID> ...] | all \n");

	private Text formatText = new Text(formatString);
	

	//@@author Rebekah
	public LayoutHelp() {
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
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}

	private void setCenterRegion() {
		wrappedAdd = createAddNode();
		wrappedDelete = createDeleteNode();
		wrappedEdit = createEditNode();
		wrappedSearch = createSearchNode();
		wrappedMark = createMarkNode();
		wrappedUndo = createUndoNode();
		wrappedRedo = createRedoNode();
		wrappedFormat = createFormatNode();
		
		implementHelpContentBar();
		implementHelpContentBox();
		
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color: #182733");
		
		formatText.setFill(Color.TRANSPARENT);
		
		StackPane sp = new StackPane();
		sp.setStyle("-fx-background-color: #182733");
		sp.setPrefSize(900, 80);
		sp.getChildren().add(formatText);
				
		borderPane.setTop(helpContentBox);
		borderPane.setBottom(sp);
		
		this.setCenter(borderPane);
	}

	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	/** IMPLEMENTING REGION OBJECTS */

	private void implementHelpContentBox() {	
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.BOTTOM_CENTER);
		helpContentBox.setStyle("-fx-background-color: #182733");
		helpContentBox.setPrefHeight(300);
		
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		helpContentBox.getColumnConstraints().add(columnConstraints);
	}

	private void implementHelpContentBar() {
		helpContentBar.getChildren().addAll(wrappedAdd, wrappedDelete, wrappedEdit,
				wrappedSearch, wrappedMark, wrappedUndo, wrappedRedo, wrappedFormat);
		helpContentBar.setAlignment(Pos.CENTER);
		helpContentBar.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					Controller.processEnter("DISPLAY");
				} else if (ke.getCode().equals(KeyCode.TAB)) {
					if (formatText.getFill().equals(Color.WHITE)){
						formatText.setFill(Color.TRANSPARENT);
					} else {
						formatText.setFill(Color.WHITE);
					}
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		return textField;
	}

	/** CREATING LAYOUT OBJECTS */
	
	private Node createFormatNode() {
		BorderPane formatBox = new BorderPane();

		Label formatLbl = new Label("<Tab>");

		formatBox.setTop(formatLbl);
		formatBox.setCenter(dummyLbl);
		formatBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.INFO));

		Node wrappedFormat = Borders.wrap(formatBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedFormat;
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
