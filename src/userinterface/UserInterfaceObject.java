package userinterface;

import logic.Todo;

/** 
 * This type of object is used for GUI to display Strings from other classes
 * 
 * */
public class UserInterfaceObject {
	
	private String _index;
	private String _name;
	private String _time;
	private String _date;
	
	/**
	 * NOTE: 
	 * index contains the index of the task
	 * name contains the name of the task
	 * time contains the duration of the task
	 * date contains date of the task
	 */
	
	public UserInterfaceObject(String index, String name, String time, String date){
		_index = index;
		_name = name;
		_time = time;
		_date = date;
	}

	public UserInterfaceObject (Todo todo){
		_index = "" + todo.getId();
		_name = todo.getName();
		_time = todo.getEndTime().toString();
		_date = todo.getEndTime().toString();
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
	
	public String getDate(){
		return _date;
	}
}
