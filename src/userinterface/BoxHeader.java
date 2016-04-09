package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import logic.DisplayCategory;

public class BoxHeader extends BorderPane {
	private enum NODETYPE { HELP, CALENDAR, SUMMARY, MINIMIZE, ESCAPE };
	
	private Node helpNode;
	private Node calNode;
	private Node minNode;
	private Node escNode;
	private Node summaryNode;
	
	//Labels for the boxes on the top
	private final static Label LABEL_DATE = new Label("     "+ DisplayCategory.getTodayDate()+"     ");
	private final static Label LABEL_HELP = new Label("F1");
	private final static Label LABEL_CALENDAR = new Label("F2");
	private final static Label LABEL_SUMMARY = new Label("F3");
	private final static Label LABEL_MINIMIZE = new Label("Del");
	private final static Label LABEL_ESCAPE = new Label("Esc");
	private final static Label LABEL_DUMMY = new Label(" ");

	//The icon for the boxes on the top right
	private final static Node ICON_HELP = GlyphsDude.createIcon(FontAwesomeIcon.QUESTION);
	private final static Node ICON_CALENDAR = GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR);
	private final static Node ICON_SUMMARY = GlyphsDude.createIcon(FontAwesomeIcon.TH_LARGE);
	private final static Node ICON_MINIMIZE = GlyphsDude.createIcon(FontAwesomeIcon.MINUS);
	private final static Node ICON_ESCAPE = GlyphsDude.createIcon(FontAwesomeIcon.REPLY);
	
	private final static String STYLE_BOX_HEADER = "-fx-background-color: #272b39;";
	
	//@@author Rebekah
	public BoxHeader() {
		implementHeaderNodes();
		this.setLeft(createTaskBox());
		this.setRight(createShortcutBox());
		this.setStyle(STYLE_BOX_HEADER);
	}

	private void implementHeaderNodes() {
		helpNode = createNode(NODETYPE.HELP);
		calNode = createNode(NODETYPE.CALENDAR);
		minNode = createNode(NODETYPE.MINIMIZE);
		escNode = createNode(NODETYPE.ESCAPE);
		summaryNode = createNode(NODETYPE.SUMMARY);
	}
	
	private Node createShortcutBox(){
		ComponentContentBoxHeader shortcutsBox = new ComponentContentBoxHeader();
		
		ComponentContentBoxHeader helpCalBox = createLeftShortcutBox();
		ComponentContentBoxHeader minSumBox = createCenterShortcutBox();
		ComponentContentBoxHeader escBox = createRightShortcutBox();
		
		shortcutsBox.setLeft(helpCalBox);
		shortcutsBox.setCenter(minSumBox);
		shortcutsBox.setRight(escBox);
		
		return shortcutsBox;
	}
	
	private ComponentContentBoxHeader createLeftShortcutBox() {
		ComponentContentBoxHeader helpCalBox = new ComponentContentBoxHeader();
		helpCalBox.setLeft(helpNode);
		helpCalBox.setRight(calNode);
		return helpCalBox;
	}

	private ComponentContentBoxHeader createCenterShortcutBox() {
		ComponentContentBoxHeader minSumBox = new ComponentContentBoxHeader();
		minSumBox.setLeft(summaryNode);
		minSumBox.setRight(minNode);
		return minSumBox;
	}
	
	private ComponentContentBoxHeader createRightShortcutBox() {
		ComponentContentBoxHeader escBox = new ComponentContentBoxHeader();
		escBox.setCenter(escNode);
		return escBox;
	}

	private ComponentContentBoxHeader createTaskBox(){
		ComponentContentBoxHeader taskBox = new ComponentContentBoxHeader();
		Node wrappedTaskLabel = Borders.wrap(LABEL_DATE).lineBorder().color(Color.WHITE).build().build();
		taskBox.setLeft(wrappedTaskLabel);
		
		return taskBox;
	}

	private Node createNode(NODETYPE type) {
		ComponentContentBoxHeader componentBox = new ComponentContentBoxHeader();
		switch (type){
		case HELP:
			componentBox.setTop(LABEL_HELP);
			componentBox.setBottom(ICON_HELP);
			break;
		case CALENDAR:
			componentBox.setTop(LABEL_CALENDAR);
			componentBox.setBottom(ICON_CALENDAR);
			break;
		case SUMMARY:
			componentBox.setTop(LABEL_SUMMARY);
			componentBox.setBottom(ICON_SUMMARY);
			break;
		case MINIMIZE:
			componentBox.setTop(LABEL_MINIMIZE);
			componentBox.setBottom(ICON_MINIMIZE);
			break;
		default:
			componentBox.setTop(LABEL_ESCAPE);
			componentBox.setBottom(ICON_ESCAPE);
		
		}
		componentBox.setCenter(LABEL_DUMMY);
		
		Node node = Borders.wrap(componentBox).lineBorder().color(Color.WHITE).build().build();

		return node;
	}
}
