package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.joda.time.DateTime;

public class TodoState {
	private Collection<Todo> todoCollection;
	private ArrayList<Todo> todayTodos = new ArrayList<Todo>();
	private ArrayList<Todo> tmrTodos = new ArrayList<Todo>();
	private ArrayList<Todo> upcomingTodos = new ArrayList<Todo>();
	private ArrayList<Todo> somedayTodos = new ArrayList<Todo>();
	private ArrayList<Todo> completedTodos = new ArrayList<Todo>();
	TodoComparator todoComparator = new TodoComparator();
	
	/* =======================================================================
		Functions for GUI to unpack all the lists of Todos of relevant date
	======================================================================= */
	public ArrayList<Todo> getTodayTodos() {
		return todayTodos;
	}
	public ArrayList<Todo> getTmrTodos() {
		return tmrTodos;
	}
	public ArrayList<Todo> getSomedayTodos() {
		return somedayTodos;
	}
	public ArrayList<Todo> getUpcomingTodos() {
		return upcomingTodos;
	}
	public ArrayList<Todo> getCompletedTodos() {
		return completedTodos;
	}
	
	
	
	/* =======================================================================
		Constructor: Pass in the Collection<Todo> called from memory.getAllTodos
			Will automatically unpack the Todos into their respective ArrayList
	======================================================================= */
	public TodoState(Collection allTodos) {
		this.todoCollection = allTodos;
		unpackTodoCollection(allTodos);
	}
	
	void unpackTodoCollection(Collection<Todo> todos) {
		Iterator<Todo> iterator = todos.iterator();
        DateTime currentDate = new DateTime(0);
        DateTime tmrDate = currentDate.plusDays(1);
		
		while (iterator.hasNext()) {
			Todo todo = iterator.next();
			
			if (todo.isDone()){
				completedTodos.add(todo);
			} else {
				DateTime todoDateTime = todo.getDateTime();
				if (todoDateTime == currentDate) {
					todayTodos.add(todo);
				} else if (todoDateTime == tmrDate) {
					tmrTodos.add(todo);
				} else if (todoDateTime == null) {
					upcomingTodos.add(todo);
				} else {
					somedayTodos.add(todo);
				}
			}
		}
		
		Collections.sort(todayTodos, todoComparator);
		Collections.sort(tmrTodos, todoComparator);
		Collections.sort(upcomingTodos, todoComparator);
		Collections.sort(somedayTodos, todoComparator);
		
	}
	
	static class TodoComparator implements Comparator<Todo> {
		@Override
		public int compare(Todo o1, Todo o2) {
			// Floating tasks with no time will be sorted in lexicographical
			// order
			if (o1.getDateTime() == null && o2.getDateTime() == null) {
				return o1.getName().compareTo(o2.getName());
			}
			// If only one todo has time, the other with no time will be sorted
			// to the back
			if (o1.getDateTime() == null) {
				return 1;
			} else if (o2.getDateTime() == null) {
				return -1;
			} else {
				// Both have time, compare directly
				return o1.getDateTime().compareTo(o2.getDateTime());
			}
		}
	}
}