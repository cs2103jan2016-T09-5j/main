package common;


/** 
 * This type of object is used for GUI to display Strings from other classes
 * 
 * */
public class UserInterfaceObject {
	
	private String _index;
	private String _name;
	private String _time;
	private String _type;
	
	/**
	 * NOTE: 
	 * index contains the index of the task
	 * name contains the name of the task
	 * time contains the duration of the task
	 * type contains either "DELETE", "MARK", "ADD", "EDIT", 
	 * 						"UNDO", "REDO", "SEARCH", "CLASH", or "NONE"
	 */
	
	public UserInterfaceObject(String index, String name, String time, String type){
		_index = index;
		_name = name;
		_time = time;
		_type = type;
	}

	public void setIndex(String index){
		_index = index;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public void setTime(String time){
		_time = time;
	}
	
	public void setType(String type){
		_type = type;
	}
	
	public String getIndex(){
		return _index;
	}
	
	public String getName(){
		return _name;
	}
	
	public String getTime(){
		return _time;
	}
	
	public String getType(){
		return _type;
	}
}
