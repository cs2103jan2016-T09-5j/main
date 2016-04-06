package userinterface;

import java.util.ArrayList;

import org.controlsfx.tools.Borders;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class LayoutTemplate extends BorderPane {

	private ArrayList<String[]> _list = new ArrayList<String[]>();
	private Label _titleLabel;
	private String _titleString;
	private Node _titleNode;

	public LayoutTemplate(String title, ArrayList<String[]> list) {
		_titleString = title;
		_list = list;
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
		implementTitle();
		TableView table = new TableView();
		this.setCenter(table);
	}

	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	
	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		return textField;
	}

//	static class NameCell extends ListCell<UserInterfaceObject> {
//		@Override
//		public void updateItem(UserInterfaceObject userInterfaceObject, boolean empty) {
//			super.updateItem(userInterfaceObject, empty);
//			Text textName = new Text();
//			textName.setWrappingWidth(450);
//			if (userInterfaceObject != null) {
//				textName.setText(userInterfaceObject.getName());
//				textName.setFill(Color.WHITE);
//				textName.setFont(Font.font("Calibri", 12));
//				if ((userInterfaceObject.getType()).equals("ADD")) {
//					textName.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
//				} else if ((userInterfaceObject.getType()).equals("EDIT")) {
//					textName.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
//				} else if ((userInterfaceObject.getType()).equals("MARK")) {
//					textName.setStrikethrough(true);
//					textName.setFill(Color.GREY);
//				} else if ((userInterfaceObject.getType()).equals("REDO")) {
//					textName.setUnderline(true);
//				} else if ((userInterfaceObject.getType()).equals("CLASH")) {
//					textName.setText(userInterfaceObject.getName());
//					textName.setFill(Color.CRIMSON);
//				}
//				setGraphic(textName);
//			}
//		}
//	}
}
