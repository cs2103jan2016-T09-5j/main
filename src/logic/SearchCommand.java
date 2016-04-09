package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import exceptions.ExceptionMessages;
import exceptions.InvalidDateException;
import exceptions.InvalidParamException;
import exceptions.NotRecurringException;
import exceptions.NullRuleException;
import exceptions.NullTodoException;
import parser.KeyParamPair;
import parser.Keywords;
import parser.ParsedInput;
import storage.Memory;

/**
 * The SearchCommand class handles user input with search commands.
 * 
 */
public class SearchCommand extends Command {

	private static  ArrayList<String> ArrListForGUI = new ArrayList<String> ();
	private static final String REGEX_SPACE = "\\s";
	
	//@@author Regine
	/**
	 * Creates a SearchCommand object.
	 * 
	 * @param input
	 *            the ParsedInput object containing the parameters.
	 * @param memory
	 *            the memory containing the Todos to which the changes should be
	 *            committed.
	 */
	public SearchCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}
	protected static ArrayList<String> getArrListForGUI(){
    	return ArrListForGUI;
    }
	public static void clearArrListForGUI(){
    	ArrListForGUI.clear();
    }
	/**
	 * Searches keywords given in parsedInput in the memory. 
	 */
	@Override
	public Signal execute() {
		ArrayList<KeyParamPair> inputList = input.getParamPairs();
		ArrayList<Todo> todos = new ArrayList<Todo>();
		KeyParamPair pair;
		Keywords typeKey;
		String param;
		List<DateTime> dateTimes = input.getDateTimes();

		// Iterates through every KeyParamPair
		for (int i = 0; i < inputList.size(); i++) {
			pair = inputList.get(i);
			typeKey = pair.getKeyword();
			param = pair.getParam();

			// assumes that if no flag input, assume that user is searching in
			// Todo name
			if (i == 0) {
				typeKey = Keywords.NAME;
			}

			// checks if param behind a flag is an empty string
			if (i != 0 && param.isEmpty()) {
				return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
			}

			try {
				searchIndex(todos, typeKey, param, dateTimes);
			} catch (InvalidParamException e) {
				return new Signal(Signal.SEARCH_INVALID_PARAMS, false);
			}

		}

		// checks if resultSet is empty
		if (todos.isEmpty()) {
			return new Signal(Signal.SEARCH_EMPTY_SIGNAL, false);
		}

		// displays the list of todos that were found
		String displayString = DisplayCommand.getDisplayChrono(todos, 2);
		ArrListForGUI.add(displayString);

		return new Signal(Signal.SEARCH_SUCCESS_SIGNAL, true);
	}

	/**
	 * Operation generates Collection of Todo Objects based on their ids and
	 * search date and type
	 * 
	 * @param typeKey
	 * @param searchDate
	 * @param todoIds
	 * @return Collection of Todo
	 * @throws InvalidParamException
	 */
	private Collection<Todo> getTodos(Keywords typeKey, DateTime searchDate,
			ArrayList<Integer> todoIds) throws InvalidParamException {
		if (typeKey != Keywords.NAME && searchDate == null) {
			throw new InvalidParamException(
					ExceptionMessages.INVALID_SEARCH_TYPE);
		}
		ArrayList<Todo> todos = new ArrayList<Todo>();
		Todo current;

		for (int x : todoIds) {
			try {
				current = memory.getTodo(x);
				if (current != null) {
					if (current.isEvent() && !current.isSameDayEvent()) {
						for (Todo shortTodo : current.breakIntoShortEvents()) {
							switch (typeKey) {
								case YEAR:
									if (shortTodo.getEndTime().getYear() == searchDate
											.getYear()) {
										todos.add(shortTodo);
									}
									break;
								case MONTH:
									if (shortTodo.getEndTime().getMonthOfYear() == searchDate
											.getMonthOfYear()) {
										todos.add(shortTodo);
									}
									break;
								case DAY:
									if (shortTodo.getEndTime().getDayOfWeek() == searchDate
											.getDayOfWeek()) {
										todos.add(shortTodo);
									}
									break;
								case TIME:
									if (shortTodo.getEndTime().toLocalTime()
											.equals(searchDate.toLocalTime())
											|| shortTodo
													.getStartTime()
													.toLocalTime()
													.equals(searchDate
															.toLocalTime())) {
										todos.add(shortTodo);
									}
									break;
								case DATE:
									if(shortTodo.getEndTime().toLocalDate().equals(searchDate.toLocalDate())) {
										todos.add(shortTodo);
									}
									break;
								default:
									
							}
						}

					} else {
						todos.add(current);
					}
				}
			} catch (NullTodoException e) {
				
			}
		}
		return todos;
	}

	/**
	 * Search memory for each string in param array based on the typeKey and
	 * updates the resultSet with the ids of the Todos found
	 * 
	 * @param resultSet
	 * @param key
	 * @param paramArray
	 * @throws InvalidDateException
	 * @throws InvalidParamException
	 */
	private void searchIndex(ArrayList<Todo> resultTodos, Keywords typeKey,
			String param, List<DateTime> dateTimes)
			throws InvalidParamException {
		ArrayList<Integer> todoIds;
		DateTime searchDate;
		if (typeKey == Keywords.NAME) {
			String[] paramArray = param.split(REGEX_SPACE);
			for (String searchKey : paramArray) {
				todoIds = memory.search(typeKey, searchKey);
				addUniqueTodos(resultTodos, getTodos(todoIds));
			}
		} else { // assumes if typeKey != NAME, user wants to search for
					// dateTime
			try {
				searchDate = dateTimes.remove(0);
				todoIds = memory.search(typeKey, searchDate);
				addUniqueTodos(resultTodos, getTodos(typeKey, searchDate, todoIds));
			} catch (IndexOutOfBoundsException e) {
				throw new InvalidParamException();
			}

		}

	}

	/**
	 * Adds todos into resultTodos without repetition
	 * @param resultTodos
	 * @param todos
	 */
	private void addUniqueTodos(ArrayList<Todo> resultTodos,
			Collection<Todo> todos) {
		for (Todo todo: todos) {
			if(!resultTodos.contains(todo)) {
				resultTodos.add(todo);
			}
		}
		
	}


	/**
	 * Retrieves a Collection of Todo objects based on their todoIds
	 * 
	 * @param todoIds
	 * @return Collection of Todos
	 */
	private Collection<Todo> getTodos(ArrayList<Integer> todoIds) {
		ArrayList<Todo> todos = new ArrayList<Todo>();
		Todo todo;
		for (int id : todoIds) {
			try {
				todo = memory.getTodo(id);
				todos.add(todo);
			} catch (NullTodoException e) {

			}
		}
		return todos;
	}	
	
	//@@author Morgan
	/**
	 * Operation queries all of memory and returns events that occur on a specific day of the year. Useful for
	 * operations including time collisions and time comparators.
	 * 
	 * @param typeKey (null typeKey signifies AgendaHelper is accessing search for recurrence alignment)
	 * @param searchDate
	 * @param memory
	 * @return Collection of Todo
	 * @throws InvalidParamException
	 */
	public static Collection<Todo> getTodosOfSameDay(Keywords typeKey, 
			DateTime searchDate, Memory memory) {
		
		Collection<Todo> todos = memory.getAllTodos();
		Collection<Todo> queriedTodos = new ArrayList<Todo>();
		
		for(Todo item : todos) {
			//track items that are within the same day queried for
			if(item != null && item.endTime != null && 
					searchDate.getDayOfYear() == item.endTime.getDayOfYear() && !item.isRecurring()) {
				queriedTodos.add(item);
			}
			//track items that recur on same queried day
			else if(item != null && item.isRecurring()) {
				//Grab associated recurrence rule from item set as recurring
				RecurringTodoRule itemRule = null;
				try {
					itemRule = memory.getRule(item.getRecurringId());
				} catch (NullRuleException | NotRecurringException e) {
					continue;
				}
			
				//grab recurrence rule for the item
				Period recurPeriod = itemRule.getRecurringInterval();
			
				//automatically add items recurring daily
				if(recurPeriod.getDays() == 1){
					item = alignRecurringTask(item, searchDate);
					queriedTodos.add(item);
				}
				//ensure weekly recurring items share same day
				else if(recurPeriod.getWeeks() == 1 
						&& item.endTime.dayOfWeek().equals(searchDate.dayOfWeek())) {
					item = alignRecurringTask(item, searchDate);
					queriedTodos.add(item);
				}
				//ensure monthly recurring items share same month
				else if(recurPeriod.getMonths() == 1 &&
						item.endTime.dayOfMonth().equals(searchDate.dayOfMonth())) {
					item = alignRecurringTask(item, searchDate);
					queriedTodos.add(item);
				}
				//ensure yearly recurring items share same date
				else if(recurPeriod.getYears() == 1
						&& item.endTime.dayOfYear().equals(searchDate.dayOfYear())) {
					item = alignRecurringTask(item, searchDate);
					queriedTodos.add(item);
				}
				
			}
		}
		return queriedTodos;	
	}
	
	/**
	 * Aligns recurring tasks that originate in the past to the correct time frame of a query
	 * 
	 * @param item
	 * @param searchDate
	 * @return modified item with same day as query
	 */
	private static Todo alignRecurringTask(Todo item, DateTime searchDate) {
		//change original task to occur on selected date by calculating delta of original and selected
		if (item.getType() == Todo.TYPE.DEADLINE) {
			DateTime originalEnd = item.getEndTime();
			originalEnd = originalEnd.plusDays(searchDate.getDayOfYear() - originalEnd.getDayOfYear());
			item.setEndTime(originalEnd);
		} else if (item.getType() == Todo.TYPE.EVENT) {
			DateTime originalStart = item.getStartTime();
			DateTime originalEnd = item.getEndTime();
			originalStart = originalStart.plusDays(searchDate.getDayOfYear() - originalEnd.getDayOfYear());
			originalEnd = originalEnd.plusDays(searchDate.getDayOfYear() - originalEnd.getDayOfYear());
			item.setStartTime(originalStart);
			item.setEndTime(originalEnd);
		}
		return item;
	}

}
