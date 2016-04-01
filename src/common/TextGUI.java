package common;


/** 
 * This type of object is used for GUI to display Strings from other classes
 * 
 * */
public class TextGUI {
	
	private String _textString;
	private String _textType;
	
	/**
	 * NOTE: 
	 * textString contains the String to display on GUI
	 * textType contains either "DELETE", "MARK", "ADD", "EDIT", 
	 * 							"UNDO", "REDO", "SEARCH", "CLASH", or "NONE"
	 */
	
	public TextGUI(String textString, String textType){
		_textString = textString;
		_textType = textType;
	}
	
	public void setTextString(String textString){
		_textString = textString;
	}
	
	public void setTextType(String textType){
		_textType = textType;
	}
	
	public String getTextString(){
		return _textString;
	}
	
	public String getTextType(){
		return _textType;
	}
}
