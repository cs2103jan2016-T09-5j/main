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
	private enum NODETYPE { ADD, DELETE, EDIT, SEARCH, MARK, UNDO, REDO, EXIT, FORMAT };
	
	//Strings that inside the displayed boxes
	private final String TEXT_NODE_ADD = "Add";
	private final String TEXT_NODE_DELETE = "Delete";
	private final String TEXT_NODE_EDIT = "Edit";
	private final String TEXT_NODE_SEARCH = "Search";
	private final String TEXT_NODE_MARK = "Mark";
	private final String TEXT_NODE_UNDO = "Undo";
	private final String TEXT_NODE_REDO = "Redo";
	private final String TEXT_NODE_EXIT = "Exit";
	private final String TEXT_NODE_FORMAT = "<Tab>";
	
	//Icons inside the displayed boxes
	private final Node ICON_ADD = GlyphsDude.createIcon(FontAwesomeIcon.PLUS);
	private final Node ICON_DELETE = GlyphsDude.createIcon(FontAwesomeIcon.MINUS);
	private final Node ICON_EDIT = GlyphsDude.createIcon(FontAwesomeIcon.PENCIL);
	private final Node ICON_SEARCH = GlyphsDude.createIcon(FontAwesomeIcon.SEARCH);
	private final Node ICON_MARK = GlyphsDude.createIcon(FontAwesomeIcon.CHECK);
	private final Node ICON_UNDO = GlyphsDude.createIcon(FontAwesomeIcon.UNDO);
	private final Node ICON_REDO = GlyphsDude.createIcon(FontAwesomeIcon.REPEAT);
	private final Node ICON_EXIT = GlyphsDude.createIcon(FontAwesomeIcon.CLOSE);
	private final Node ICON_FORMAT = GlyphsDude.createIcon(FontAwesomeIcon.INFO);
	
	//Styles of the items in the scene
	private final String STYLE_MAIN_BORDERPANE = "-fx-background-color: #182733";
	private final String STYLE_MAIN_STACKPANE = "-fx-background-color: #182733";
	private final String STYLE_HELPBOX = "-fx-background-color: #182733";
	private final String STYLE_CONTENT_BAR = "-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;";
	
	//Dimensions of items in the scene
	private final int HEIGHT_CONTENT_BOX = 300;
	private final int HEIGHT_MAIN_STACKPANE = 150;
	private final int WIDTH_MAIN_STACKPANE = 900;
	
	private Label dummyLbl = new Label(" ");
	private GridPane helpContentBox = new GridPane();
	private HBox helpContentBar = new HBox();

	//Nodes in the scene
	private Node addNode;
	private Node deleteNode;
	private Node editNode;
	private Node searchNode;
	private Node markNode;
	private Node undoNode;
	private Node redoNode;
	private Node exitNode;
	private Node formatNode;
	
	private final String formatString = new String("add [task name] from [starting period] to [ending period]\n"
			+ "delete [task IDs] \n"
			+ "edit <ID> [<newName>] [from <newStartTime>] [ to <newEndTime>] [by;on;at <newDeadline>] [every  <interval> ] [until <limit>] \n"
			+ "search [<taskName>; <date>] \n"
			+ "mark <ID>");

	private final Text formatText = new Text(formatString);
	

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
		addNode = createNode(NODETYPE.ADD);
		deleteNode = createNode(NODETYPE.DELETE);
		editNode = createNode(NODETYPE.EDIT);
		searchNode = createNode(NODETYPE.SEARCH);
		markNode = createNode(NODETYPE.MARK);
		undoNode = createNode(NODETYPE.UNDO);
		redoNode = createNode(NODETYPE.REDO);
		formatNode = createNode(NODETYPE.FORMAT);
		exitNode = createNode(NODETYPE.EXIT);
		
		implementHelpContentBar();
		implementHelpContentBox();
		
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle(STYLE_MAIN_BORDERPANE);
		
		formatText.setFill(Color.WHITE);
		
		StackPane sp = new StackPane();
		sp.setStyle(STYLE_MAIN_STACKPANE);
		sp.setPrefSize(WIDTH_MAIN_STACKPANE, HEIGHT_MAIN_STACKPANE);
		sp.getChildren().add(formatText);
				
		borderPane.setTop(helpContentBox);
		borderPane.setBottom(sp);
		
		this.setCenter(borderPane);
	}

	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	/** CREATES THE BOX THAT HOLDS ALL CONTENT */
	private void implementHelpContentBox() {	
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.BOTTOM_CENTER);
		helpContentBox.setStyle(STYLE_HELPBOX);
		helpContentBox.setPrefHeight(HEIGHT_CONTENT_BOX);
		
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		helpContentBox.getColumnConstraints().add(columnConstraints);
	}

	/** CREATES THE TOP BAR THAT HOLDS THE COMMAND BOXES */
	private void implementHelpContentBar() {
		helpContentBar.getChildren().addAll(
				addNode, deleteNode, editNode, searchNode, markNode, 
				undoNode, redoNode, exitNode, formatNode);
		helpContentBar.setAlignment(Pos.CENTER);
		helpContentBar.setStyle(STYLE_CONTENT_BAR);
	}

	/** CREATES THE TEXT FIELD WHERE THE USER TYPES */
	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					Controller.processEnter("DISPLAY");
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		return textField;
	}

	/** Creates a Node of the specified NODETYPE */
	private Node createNode(NODETYPE type){
		BorderPane borderPane = new BorderPane();
		Label label = null;
		Node icon = null;
		switch (type){
		case ADD:
			label = new Label(TEXT_NODE_ADD);
			icon = ICON_ADD;
			break;
		case DELETE:
			label = new Label(TEXT_NODE_DELETE);
			icon = ICON_DELETE;
			break;
		case EDIT:
			label = new Label(TEXT_NODE_EDIT);
			icon = ICON_EDIT;
			break;
		case SEARCH:
			label = new Label(TEXT_NODE_SEARCH);
			icon = ICON_SEARCH;
			break;
		case MARK:
			label = new Label(TEXT_NODE_MARK);
			icon = ICON_MARK;
			break;
		case UNDO:
			label = new Label(TEXT_NODE_UNDO);
			icon = ICON_UNDO;
			break;
		case REDO:
			label = new Label(TEXT_NODE_REDO);
			icon = ICON_REDO;
			break;
		case EXIT:
			label = new Label(TEXT_NODE_EXIT);
			icon = ICON_EXIT;
			break;
		default:
			label = new Label(TEXT_NODE_FORMAT);
			icon = ICON_FORMAT;
			break;
		}
		
		borderPane.setTop(label);
		borderPane.setCenter(dummyLbl);
		borderPane.setBottom(icon);
		Node node = Borders.wrap(borderPane).lineBorder().color(Color.WHITE).build().build();
		return node;
	}
}
