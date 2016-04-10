package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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

//@@author Rebekah
/**
* The LayoutHelp class creates the layout to display help to the user. The available command 
* types the user can use, as well as the input format to call the commands are shown in this 
* class to visually aid the user in using Clockwork.
*/
public class LayoutHelp extends BorderPane {
	/** Enumerator for nodes used in BoxHeader */
	private enum NODETYPE { ADD, DELETE, EDIT, SEARCH, MARK, UNDO, REDO, EXIT, FORMAT };
	
	/** Strings to display in the help bar */
	private static final String TEXT_NODE_ADD = "Add";
	private static final String TEXT_NODE_DELETE = "Delete";
	private static final String TEXT_NODE_EDIT = "Edit";
	private static final String TEXT_NODE_SEARCH = "Search";
	private static final String TEXT_NODE_MARK = "Mark";
	private static final String TEXT_NODE_UNDO = "Undo";
	private static final String TEXT_NODE_REDO = "Redo";
	private static final String TEXT_NODE_EXIT = "Exit";
	private static final String TEXT_NODE_FORMAT = "<Tab>";
	
	/** Icons to display in the help bar */
	private final Node ICON_ADD = GlyphsDude.createIcon(FontAwesomeIcon.PLUS);
	private final Node ICON_DELETE = GlyphsDude.createIcon(FontAwesomeIcon.MINUS);
	private final Node ICON_EDIT = GlyphsDude.createIcon(FontAwesomeIcon.PENCIL);
	private final Node ICON_SEARCH = GlyphsDude.createIcon(FontAwesomeIcon.SEARCH);
	private final Node ICON_MARK = GlyphsDude.createIcon(FontAwesomeIcon.CHECK);
	private final Node ICON_UNDO = GlyphsDude.createIcon(FontAwesomeIcon.UNDO);
	private final Node ICON_REDO = GlyphsDude.createIcon(FontAwesomeIcon.REPEAT);
	private final Node ICON_EXIT = GlyphsDude.createIcon(FontAwesomeIcon.CLOSE);
	private final Node ICON_FORMAT = GlyphsDude.createIcon(FontAwesomeIcon.INFO);
	
	/** Background colour for nodes */
	private final String STYLE_MAIN_BORDERPANE = "-fx-background-color: #182733";
	private final String STYLE_MAIN_STACKPANE = "-fx-background-color: #182733";
	private final String STYLE_HELPBOX = "-fx-background-color: #182733";
	private final String STYLE_CONTENT_BAR = "-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;";
	
	/** Dimensions of items used in the class */
	private static final int HEIGHT_CONTENT_BOX = 300;
	private static final int HEIGHT_MAIN_STACKPANE = 150;
	private static final int WIDTH_MAIN_STACKPANE = 900;
	
	/** Dummy label used to set a placeholder for nodes */
	private static final Label LABEL_DUMMY = new Label(" ");
	
	/** Nodes containing both label and icon to display in BoxHeader */
	private Node addNode;
	private Node deleteNode;
	private Node editNode;
	private Node searchNode;
	private Node markNode;
	private Node undoNode;
	private Node redoNode;
	private Node exitNode;
	private Node formatNode;
	
	/** Layouts used to contain the help nodes */
	private StackPane formatBox = new StackPane();
	private GridPane barBox = new GridPane();
	private HBox helpBar = new HBox();
	private BorderPane helpBox = new BorderPane();
	
	/** Text used to show available command formats */
	private Text formatText = new Text();
	
	/** String used to show available command formats */
	private static final String formatString = new String(
			"add [task name] from [starting period] to [ending period]\n"
			+ "delete [task IDs] \n"
			+ "edit <ID> [<newName>] [from <newStartTime>] [ to <newEndTime>] "
			+ "[by;on;at <newDeadline>] [every  <interval> ] [until <limit>] \n"
			+ "search [<taskName>; <date>] \n"
			+ "mark <ID>");

	/** Constructor for LayoutHelp object*/
	public LayoutHelp() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}

	/** Set top region to display available shortcuts */
	private void setTopRegion() {	
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}
	
	/** Set center region to display available help commands and input format */
	private void setCenterRegion() {
		implementHelpNodes();
		implementHelpBar();
		createFormatText();
		this.setCenter(createHelpBox());
	}

	/** Set bottom region for user input */
	private void setBottomRegion() {
		this.setBottom(implementTextField());
	}
	
	/** Implement all nodes to be used for help bar */
	private void implementHelpNodes() {
		addNode = createNode(NODETYPE.ADD);
		deleteNode = createNode(NODETYPE.DELETE);
		editNode = createNode(NODETYPE.EDIT);
		searchNode = createNode(NODETYPE.SEARCH);
		markNode = createNode(NODETYPE.MARK);
		undoNode = createNode(NODETYPE.UNDO);
		redoNode = createNode(NODETYPE.REDO);
		formatNode = createNode(NODETYPE.FORMAT);
		exitNode = createNode(NODETYPE.EXIT);
	}
	
	/** Implement the help bar that holds the help nodes */
	private void implementHelpBar() {
		helpBar.getChildren().addAll(
				addNode, deleteNode, editNode, searchNode, markNode, 
				undoNode, redoNode, exitNode, formatNode);
		helpBar.setAlignment(Pos.CENTER);
		helpBar.setStyle(STYLE_CONTENT_BAR);
	}
	
	/** Implement the textfield for user to enter input */
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
	
	/** Creates the help box containing the help bar and the input format help box */
	private BorderPane createHelpBox(){
		helpBox.setStyle(STYLE_MAIN_BORDERPANE);
		helpBox.setTop(createBarBox());
		helpBox.setBottom(createFormatBox());
		return helpBox;
	}
	
	/** Creates the box that holds the help bar */
	private GridPane createBarBox() {	
		barBox.getChildren().add(helpBar);
		barBox.setAlignment(Pos.BOTTOM_CENTER);
		barBox.setStyle(STYLE_HELPBOX);
		barBox.setPrefHeight(HEIGHT_CONTENT_BOX);
		
		ColumnConstraints barColumnConstraints = new ColumnConstraints();
		barColumnConstraints.setFillWidth(true);
		barColumnConstraints.setHgrow(Priority.ALWAYS);
		barBox.getColumnConstraints().add(barColumnConstraints);
		
		return barBox;
	}
	
	/** Creates the box that holds the input format help */
	private StackPane createFormatBox(){
		formatBox.setStyle(STYLE_MAIN_STACKPANE);
		formatBox.setPrefSize(WIDTH_MAIN_STACKPANE, HEIGHT_MAIN_STACKPANE);
		formatBox.getChildren().add(formatText);
		return formatBox;
	}

	/** Creates a Node of the specified NODETYPE */
	private Node createNode(NODETYPE type){
		BorderPane nodeBox = new BorderPane();
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
		
		nodeBox.setTop(label);
		nodeBox.setCenter(LABEL_DUMMY);
		nodeBox.setBottom(icon);
		Node node = Borders.wrap(nodeBox).lineBorder().color(Color.WHITE).build().build();
		return node;
	}
	
	/** Create input format help to show available command formats */
	private void createFormatText(){
		formatText = new Text(formatString);
		formatText.setFill(Color.WHITE);
	}
}
