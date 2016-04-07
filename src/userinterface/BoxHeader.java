package userinterface;

import org.controlsfx.tools.Borders;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class BoxHeader extends BorderPane {

	private Node helpNode;
	private Node calNode;
	private Node minNode;
	private Node escNode;
	private Node summaryNode;
	
	private Label taskLbl = new Label("     Tasks     ");
	private Label helpLbl = new Label("F1");
	private Label calLbl = new Label("F2");
	private Label summaryLbl = new Label("F3");
	private Label minLbl = new Label("Del");
	private Label escLbl = new Label("Esc");
	private Label dummyLbl = new Label(" ");
	
	public BoxHeader() {
		implementHeaderNodes();
		this.setLeft(createTaskBox());
		this.setRight(createShortcutBox());
		this.setStyle("-fx-background-color: #272b39;");
	}

	private void implementHeaderNodes() {
		helpNode = createHelpNode();
		calNode = createCalNode();
		minNode = createMinNode();
		escNode = createEscNode();
		summaryNode = createSummaryNode();
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
		Node wrappedTaskLabel = Borders.wrap(taskLbl).lineBorder().color(Color.WHITE).build().build();
		taskBox.setLeft(wrappedTaskLabel);
		
		return taskBox;
	}
	
	private Node createHelpNode() {
		ComponentContentBoxHeader helpShortcutBox = new ComponentContentBoxHeader();

		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setCenter(dummyLbl);
		helpShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.QUESTION));

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalNode() {
		ComponentContentBoxHeader calShortcutBox = new ComponentContentBoxHeader();

		calShortcutBox.setTop(calLbl);
		calShortcutBox.setCenter(dummyLbl);
		calShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR));
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
	
	private Node createMinNode() {
		ComponentContentBoxHeader minShortcutBox = new ComponentContentBoxHeader();

		Label dummyLbl = new Label(" ");
		
		minShortcutBox.setTop(minLbl);
		minShortcutBox.setCenter(dummyLbl);
		minShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));
		
		Node wrappedMinLabel = Borders.wrap(minShortcutBox).lineBorder()
								.color(Color.WHITE).build().build();

		return wrappedMinLabel;
	}
	
	private Node createEscNode(){
		ComponentContentBoxHeader escShortcutBox = new ComponentContentBoxHeader();
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setCenter(dummyLbl);
		escShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPLY));
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
	
	private Node createSummaryNode(){
		ComponentContentBoxHeader summaryShortcutBox = new ComponentContentBoxHeader();
		
		summaryShortcutBox.setTop(summaryLbl);
		summaryShortcutBox.setCenter(dummyLbl);
		summaryShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.COLUMNS));
		Node wrappedSummaryLabel = Borders.wrap(summaryShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedSummaryLabel;
	}
	
	public void removeEscNode(){
		escNode = null;
		this.setLeft(createTaskBox());
		this.setRight(createShortcutBox());
		this.setStyle("-fx-background-color: #272b39;");
	}
}
