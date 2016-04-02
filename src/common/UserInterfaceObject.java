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
	private String _date;
	
	/**
	 * NOTE: 
	 * index contains the index of the task
	 * name contains the name of the task
	 * time contains the duration of the task
	 * type contains either "MARK", "ADD", "EDIT", 
	 * 						"REDO","CLASH" or any other String.
	 * date contains date of the task, and is only used in times when date is defined
	 * but date is not today or tomorrow, eg. 6 December 2016.
	 */
	
	public UserInterfaceObject(String index, String name, String time, String type, String date){
		_index = index;
		_name = name;
		_time = time;
		_type = type;
		_date = date;
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
	
	public void setDate(String date){
		_date = date;
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
	
	public String getDate(){
		return _date;
	}
}
