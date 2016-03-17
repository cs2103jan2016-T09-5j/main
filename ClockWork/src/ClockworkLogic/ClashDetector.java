package ClockworkLogic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import org.joda.time.DateTime;

public class ClashDetector {
	private Collection<Todo> onDateTodos;
	private Todo attemptedTodo;
	public static Scanner scn = new Scanner(System.in);
	private static  ArrayList<String> ArrListForGUI = new ArrayList<String> ();
	
	public static ArrayList<String> getArrListForGUI(){
	    	return ArrListForGUI;
	}
	public static void clearArrListForGUI(){
		ArrListForGUI.clear();
	}
	ClashDetector(Collection<Todo> onDateTodos, Todo attemptedTodo){
		this.onDateTodos = onDateTodos;
		this.attemptedTodo = attemptedTodo;
	}
	
	public boolean verifyTodoClash() {
		boolean todoClashExists = false;
		ArrListForGUI.clear();
		switch(attemptedTodo.type) {
			case DEADLINE:
				todoClashExists = isDeadlineClash();
				if(todoClashExists) {
					ArrListForGUI.add(String.format(Signal.CLASH_DEADLINE_DOES_EXIST,
							attemptedTodo.name, attemptedTodo.endTime));
					System.out.println(String.format(Signal.CLASH_DEADLINE_DOES_EXIST,
							attemptedTodo.name, attemptedTodo.endTime));
				}
				break;
			case EVENT:
				todoClashExists = isEventClash();
				if(todoClashExists) {
					ArrListForGUI.add(String.format(Signal.CLASH_EVENT_DOES_EXIST,
							attemptedTodo.name, attemptedTodo.endTime));
					System.out.println(String.format(Signal.CLASH_EVENT_DOES_EXIST,
							attemptedTodo.name, attemptedTodo.endTime));
				}
				break;
			default:
				break;
		}
		
		//A user may choose to void a time clash and force the system to add the overlapping times
		if(todoClashExists) {
			todoClashExists = isUserVoidingTodo();
		}
		
		return todoClashExists;
	}
	
	private boolean isUserVoidingTodo() {
		ArrListForGUI.add(String.format(Signal.CLASH_CONTINUE_PROPOSITION));
		System.out.println(String.format(Signal.CLASH_CONTINUE_PROPOSITION));
		String userResponse = scn.nextLine().trim().toLowerCase();
		if(userResponse.equals("yes") || userResponse.equals("y")) {
			ArrListForGUI.add(String.format(Signal.CLASH_USER_OVERRIDE));
			System.out.println(String.format(Signal.CLASH_USER_OVERRIDE));
			return false;
		}
		else {
			ArrListForGUI.add(String.format(Signal.CLASH_USER_VOID_TASK));
			System.out.println(String.format(Signal.CLASH_USER_VOID_TASK));	
			return true;
		}
	}
	
	private boolean isDeadlineClash() {
		DateTime deadlineTime = attemptedTodo.endTime;
		for (Todo item : onDateTodos) {
			
			if(item.type == Todo.TYPE.DEADLINE) {
				if(deadlineTime.isEqual(item.endTime)) {
					return true;
				}	
			}
			
			if(item.type == Todo.TYPE.EVENT) {
				if(deadlineTime.isBefore(item.endTime) && deadlineTime.isAfter(item.startTime)) {
					return true;
				}
				
				if(deadlineTime.isEqual(item.endTime) || deadlineTime.isEqual(item.startTime)) {
					return true;
				}	
			}
			
		}
		return false;
	}
	
	private boolean isEventClash() {
		DateTime startTime = attemptedTodo.startTime;
		DateTime endTime = attemptedTodo.endTime;
		for (Todo item : onDateTodos) {
			
			if(item.type == Todo.TYPE.EVENT) {
				if(startTime.isBefore(item.startTime) && endTime.isAfter(item.startTime)) {
					return true;
				}
				
				if(endTime.isAfter(item.endTime) && startTime.isBefore(item.endTime)) {
					return true;
				}
				
				if(startTime.isEqual(item.startTime) || endTime.isEqual(item.endTime)) {
					return true;
				}
			}
			
			if(item.type == Todo.TYPE.DEADLINE) {
				if(item.endTime.isAfter(startTime) && item.endTime.isBefore(endTime)) {
					return true;
				}
				
				if(startTime.isEqual(item.endTime) || endTime.isEqual(item.endTime)) {
					return true;
				}	
			}
			
		}
		return false;
	}
}