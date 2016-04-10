package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import logic.DisplayCategory;

//@@author Rebekah
/**
 * The BoxHeader class creates the layout to display the date, and available shortcuts
 * the user may use by using FontAwesome icons.
 */
public class BoxHeader extends BorderPane {
	/** Enumerator for nodes used in BoxHeader */
	private enum NODETYPE { 
		HELP, CALENDAR, SUMMARY, MINIMIZE, ESCAPE 
	};
	
	/** Nodes containing both label and icon to display in BoxHeader */
	private Node helpNode;
	private Node calendarNode;
	private Node minimiseNode;
	private Node escapeNode;
	private Node summaryNode;
	
	/** Label for the boxes used to set the nodes */
	private static final Label LABEL_DATE = new Label("     "+ DisplayCategory.getTodayDate()+"     ");
	private static final Label LABEL_HELP = new Label("F1");
	private static final Label LABEL_CALENDAR = new Label("F2");
	private static final Label LABEL_SUMMARY = new Label("F3");
	private static final Label LABEL_MINIMIZE = new Label("Del");
	private static final Label LABEL_ESCAPE = new Label("Esc");
	private static final Label LABEL_DUMMY = new Label(" ");

	/** Icons for the boxes used to set the nodes */
	private final Node ICON_HELP = GlyphsDude.createIcon(FontAwesomeIcon.QUESTION);
	private final Node ICON_CALENDAR = GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR);
	private final Node ICON_SUMMARY = GlyphsDude.createIcon(FontAwesomeIcon.TH_LARGE);
	private final Node ICON_MINIMIZE = GlyphsDude.createIcon(FontAwesomeIcon.MINUS);
	private final Node ICON_ESCAPE = GlyphsDude.createIcon(FontAwesomeIcon.REPLY);
	
	/** Background colour for nodes */
	private static final String STYLE_BOX_HEADER = "-fx-background-color: #272b39;";
	
	/** Constructor for BoxHeader */
	public BoxHeader() {
		implementHeaderNodes();
		this.setLeft(createDateBox());
		this.setRight(createShortcutBox());
		this.setStyle(STYLE_BOX_HEADER);
	}
	
	/** Implement all nodes to be used for shortcut box */
	private void implementHeaderNodes() {
		helpNode = createNode(NODETYPE.HELP);
		calendarNode = createNode(NODETYPE.CALENDAR);
		minimiseNode = createNode(NODETYPE.MINIMIZE);
		escapeNode = createNode(NODETYPE.ESCAPE);
		summaryNode = createNode(NODETYPE.SUMMARY);
	}
	
	/** Create date box to contain date for left side of HeaderBox */
	private BoxHeaderContent createDateBox(){
		BoxHeaderContent dateBox = new BoxHeaderContent();
		Node dateNode = Borders.wrap(LABEL_DATE).lineBorder().color(Color.WHITE).build().build();
		dateBox.setLeft(dateNode);
		
		return dateBox;
	}
	
	/** Create shortcut box to contain nodes for right side of HeaderBox */
	private Node createShortcutBox(){
		BoxHeaderContent shortcutsBox = new BoxHeaderContent();
		
		BoxHeaderContent helpCalBox = createLeftShortcutBox();
		BoxHeaderContent minSumBox = createCenterShortcutBox();
		BoxHeaderContent escBox = createRightShortcutBox();
		
		shortcutsBox.setLeft(helpCalBox);
		shortcutsBox.setCenter(minSumBox);
		shortcutsBox.setRight(escBox);
		
		return shortcutsBox;
	}
	
	/** Create left section of shortcut box */
	private BoxHeaderContent createLeftShortcutBox() {
		BoxHeaderContent helpCalBox = new BoxHeaderContent();
		helpCalBox.setLeft(helpNode);
		helpCalBox.setRight(calendarNode);
		return helpCalBox;
	}
	
	/** Create center section of shortcut box */
	private BoxHeaderContent createCenterShortcutBox() {
		BoxHeaderContent minSumBox = new BoxHeaderContent();
		minSumBox.setLeft(summaryNode);
		minSumBox.setRight(minimiseNode);
		return minSumBox;
	}
	
	/** Create right section of shortcut box */
	private BoxHeaderContent createRightShortcutBox() {
		BoxHeaderContent escBox = new BoxHeaderContent();
		escBox.setCenter(escapeNode);
		return escBox;
	}

	/** Create all nodes to be used for shortcut box */
	private Node createNode(NODETYPE type) {
		BoxHeaderContent nodeBox = new BoxHeaderContent();
		switch (type){
		case HELP:
			nodeBox.setTop(LABEL_HELP);
			nodeBox.setBottom(ICON_HELP);
			break;
		case CALENDAR:
			nodeBox.setTop(LABEL_CALENDAR);
			nodeBox.setBottom(ICON_CALENDAR);
			break;
		case SUMMARY:
			nodeBox.setTop(LABEL_SUMMARY);
			nodeBox.setBottom(ICON_SUMMARY);
			break;
		case MINIMIZE:
			nodeBox.setTop(LABEL_MINIMIZE);
			nodeBox.setBottom(ICON_MINIMIZE);
			break;
		default:
			nodeBox.setTop(LABEL_ESCAPE);
			nodeBox.setBottom(ICON_ESCAPE);
		
		}
		nodeBox.setCenter(LABEL_DUMMY);
		
		Node node = Borders.wrap(nodeBox).lineBorder().color(Color.WHITE).build().build();

		return node;
	}
}
