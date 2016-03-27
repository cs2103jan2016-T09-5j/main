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
	
	private Label taskLbl = new Label("     Tasks     ");
	private Label helpLbl = new Label("F1");
	private Label calLbl = new Label("F2");
	private Label minLbl = new Label("F3");
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
	}
	
	private Node createShortcutBox(){
		ContentBoxHeader shortcutsBox = new ContentBoxHeader();
		
		ContentBoxHeader helpCalBox = createLeftShortcutBox();
		ContentBoxHeader minEscBox = createRightShortcutBox();
		
		shortcutsBox.setLeft(helpCalBox);
		shortcutsBox.setRight(minEscBox);
		
		return shortcutsBox;
	}
	
	private ContentBoxHeader createLeftShortcutBox() {
		ContentBoxHeader helpCalBox = new ContentBoxHeader();
		helpCalBox.setLeft(helpNode);
		helpCalBox.setRight(calNode);
		return helpCalBox;
	}

	private ContentBoxHeader createRightShortcutBox() {
		ContentBoxHeader minEscBox = new ContentBoxHeader();
		minEscBox.setLeft(minNode);
		minEscBox.setRight(escNode);
		return minEscBox;
	}

	private ContentBoxHeader createTaskBox(){
		ContentBoxHeader taskBox = new ContentBoxHeader();
		Node wrappedTaskLabel = Borders.wrap(taskLbl).lineBorder().color(Color.WHITE).build().build();
		taskBox.setLeft(wrappedTaskLabel);
		
		return taskBox;
	}
	
	private Node createHelpNode() {
		ContentBoxHeader helpShortcutBox = new ContentBoxHeader();

		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setCenter(dummyLbl);
		helpShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.QUESTION));

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalNode() {
		ContentBoxHeader calShortcutBox = new ContentBoxHeader();

		calShortcutBox.setTop(calLbl);
		calShortcutBox.setCenter(dummyLbl);
		calShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR));
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
	
	private Node createMinNode() {
		ContentBoxHeader minShortcutBox = new ContentBoxHeader();

		Label dummyLbl = new Label(" ");
		
		minShortcutBox.setTop(minLbl);
		minShortcutBox.setCenter(dummyLbl);
		minShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));
		
		Node wrappedMinLabel = Borders.wrap(minShortcutBox).lineBorder()
								.color(Color.WHITE).build().build();

		return wrappedMinLabel;
	}
	
	private Node createEscNode(){
		ContentBoxHeader escShortcutBox = new ContentBoxHeader();
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setCenter(dummyLbl);
		escShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPLY));
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
	
	public void removeEscNode(){
		escNode = null;
		this.setLeft(createTaskBox());
		this.setRight(createShortcutBox());
		this.setStyle("-fx-background-color: #272b39;");
	}
	
	public class ContentBoxHeader extends BorderPane {
		public ContentBoxHeader() {
			this.setStyle("-fx-background-color: #272b39;");
		}
	}

}
