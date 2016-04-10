package userinterface;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//@@author Rebekah
/**
* The LayoutSummaryRectangle class creates the rectangles used in the four boxes of
* the LayoutSummary class.
*/
public class LayoutCategoryRectangle extends Rectangle {
	/** Constructor for LayoutSummaryRectangle object*/
	public LayoutCategoryRectangle(){
		this.setWidth(150);
		this.setHeight(150);
		this.setFill(Color.TRANSPARENT);
	}
}
