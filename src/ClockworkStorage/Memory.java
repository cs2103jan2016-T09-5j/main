

package ClockworkStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import ClockworkExceptions.ExceptionMessages;
import ClockworkExceptions.InvalidParamException;
import ClockworkExceptions.NullRuleException;
import ClockworkExceptions.NullTodoException;
import ClockworkExceptions.StateUndefinedException;
import ClockworkLogic.Keywords;
import ClockworkLogic.RecurringTodoRule;
import ClockworkLogic.Todo;
import ClockworkParser.IDBuffer;
import ClockworkParser.ParsedInput;
import ClockworkStorage.VolatileMemory;
/**
 * Stores all Todos and keeps state information allowing Undo and Redo
 * operations. Maximum number of states that can be stored by Memory is
 * {@value #STATE_STACK_MAX_SIZE}.
 */
public class Memory {
	// Constants
	private static final String REGEX_SPACE = "\\s";

	// Primary memory
	private HashMap<Integer, Todo> allTodos;
	private HashMap<Integer, RecurringTodoRule> recurringRules;

	// Auxiliary memory for ID maintenance
	private final IDBuffer<Todo> idBuffer;
	private final IDBuffer<RecurringTodoRule> recurringIdBuffer;

	// Volatile memory for undo/redo
	private VolatileMemory vMem;
	
	// Indexes for search
	private SearchMap searchMap;

	// Handler for writing to file
	private StorageHandler storage;

	/**
	 * Constructs an empty Memory object.
	 */
	public Memory() {
		this.allTodos = new HashMap<Integer, Todo>();
		this.recurringRules = new HashMap<Integer, RecurringTodoRule>();
		this.idBuffer = new IDBuffer<Todo>(allTodos);
		this.recurringIdBuffer = new IDBuffer<RecurringTodoRule>(recurringRules);
		this.searchMap = new SearchMap();
		this.vMem = new VolatileMemory(allTodos, idBuffer, recurringRules, recurringIdBuffer);
	}
	
	public void onCreate() {
		vMem = new VolatileMemory(allTodos, idBuffer, recurringRules, recurringIdBuffer);
		idBuffer.setMemory(allTodos);
		recurringIdBuffer.setMemory(recurringRules);
	}
	
	public void onDestroy() {
		vMem.flushStacks(); //Recycles all IDs
		vMem = null;
	}

	/**
	 * Adds the specified Todo to memory. The current state is saved prior to
	 * any operation. Since add is a memory modifying command, the redoStack is
	 * flushed.
	 * <p>
	 * This operation also adds all parameters of the Todo specified into the
	 * SearchMap for indexing.
	 * 
	 * @param todo the Todo to be added.
	 */
	public void userAdd(Todo todo) {
		// Save to stacks
		vMem.save(todo.getPlaceholder());
		// Save to memory
		allTodos.put(todo.getId(), todo);
		// Save to indexes
		searchMap.add(todo);
	}
	
	public void systemAdd(Todo todo) {
		// Does not save to stacks
		// Save to memory
		allTodos.put(todo.getId(), todo);
		// Save to indexes
		searchMap.add(todo);
	}

	/**
	 * Handle adding of recurring tasks as rules
	 * 
	 * @param rule
	 */
	public void add(RecurringTodoRule rule) {
		// Save to stacks
		vMem.save(rule.getPlaceholder());
		// Save to memory
		recurringRules.put(rule.getId(), rule);
		updateRecurringRules();
		// TODO Add to searchmap
	}

	private void updateRecurringRules() {
		Collection<RecurringTodoRule> rules = recurringRules.values();
		for (Iterator<RecurringTodoRule> iterator = rules.iterator(); iterator
				.hasNext();) {
			RecurringTodoRule rule = (RecurringTodoRule) iterator.next();
			rule.updateTodoList(this);
		}
	}

	/**
	 * Retrieves the Todo identified by the specified ID from the memory.
	 * 
	 * @param id the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException if the Todo identified by the specified ID does
	 *             not exist.
	 */
	public Todo getTodo(int id) throws NullTodoException {
		Todo returnTodo = allTodos.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		return returnTodo;
	}

	/**
	 * Retrieves the Todo identified by the specified ID from the memory for
	 * editing. The current state is saved prior to any operation.
	 * 
	 * @param id the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException if the Todo identified by the specified ID does
	 *             not exist.
	 */
	public Todo getToModifyTodo(int id) throws NullTodoException {
		Todo returnTodo = allTodos.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		// Save to stacks
		vMem.save(returnTodo);
		return returnTodo;
	}
	
	public RecurringTodoRule getRule(int recurringId) throws NullRuleException {
		RecurringTodoRule returnRule = recurringRules.get(recurringId);
		if (returnRule == null) {
			throw new NullRuleException(ExceptionMessages.NULL_RULE_EXCEPTION);
		}
		return returnRule;
	}

    public Collection<RecurringTodoRule> getAllRules() {
        return recurringRules.values();
    }

	/**
	 * Retrieves the RecurringTodoRule identified by the specified ID from the memory for
	 * editing. The current state is saved prior to any operation.
	 * 
	 * @param recurringId the ID of the RecurringTodoRule to be retrieved.
	 * @return the RecurringTodoRule object identified by the specified ID.
	 * @throws NullRuleException if the RecurringTodoRule identified by the specified ID does
	 *             not exist.
	 */
	public RecurringTodoRule getToModifyRule(int recurringId) throws NullRuleException {
		RecurringTodoRule returnRule = recurringRules.get(recurringId);
		if (returnRule == null) {
			throw new NullRuleException(ExceptionMessages.NULL_RULE_EXCEPTION);
		}
		// Save to stacks
		vMem.save(returnRule);
		return returnRule;
	}

	/**
	 * Removes the Todo identified by the specified id from the memory. The
	 * current state is saved prior to any operation.
	 * 
	 * @param id the ID of the Todo to be removed.
	 * @throws NullTodoException if the Todo identified by the specified ID does
	 *             not exist.
	 */
	public Todo removeTodo(int id) throws NullTodoException {
		Todo returnTodo = allTodos.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		// Save to stacks
		vMem.save(returnTodo);
		allTodos.remove(id);
		searchMap.remove(returnTodo);
		return returnTodo;
	}

	
	public RecurringTodoRule removeRule(int recurringId) throws NullRuleException {
		RecurringTodoRule returnRule = recurringRules.get(recurringId);
		if (returnRule == null) {
			throw new NullRuleException(ExceptionMessages.NULL_RULE_EXCEPTION);
		}
		// Save to stacks
		vMem.save(returnRule);
		recurringRules.remove(recurringId);
		// TODO Remove from search map
		return returnRule;
	}
	
	public void undo() throws StateUndefinedException {
		vMem.undo();
	}
	
	public void redo() throws StateUndefinedException {
		vMem.redo();
	}

	/**
	 * Method to get all the Todos for displaying purposes.
	 * 
	 * @return all Todos as Collection
	 */
	public Collection<Todo> getAllTodos() {
		updateRecurringRules();
		return allTodos.values();
	}

	/**
	 * Obtains an ID number from the pool of available ID numbers.
	 * 
	 * @return the ID obtained.
	 */
	public int obtainFreshId() {
		return idBuffer.get();
	}

	public int obtainFreshRecurringId() {
		return recurringIdBuffer.get();
	}

	/**
	 * Releases the specified ID number to the pool of available ID numbers for
	 * future use by new Todos.
	 * 
	 * @param id the ID to be released.
	 */
	public void releaseId(int id) {
		idBuffer.put(id);
	}

	public void releaseRecurringId(int recurringId) {
		recurringIdBuffer.put(recurringId);
	}	


	

	/**
	 * This class stores the mapping of various types of index to a list of Todo
	 * ids for the purpose of the search command
	 */
	private class SearchMap {
		private HashMap<String, ArrayList<Integer>> nameMap;
		private HashMap<LocalDate, ArrayList<Integer>> dateMap;
		private HashMap<LocalTime, ArrayList<Integer>> timeMap;
		private HashMap<Integer, ArrayList<Integer>> monthMap;
		private HashMap<Integer, ArrayList<Integer>> dayMap;
		private HashMap<Integer, ArrayList<Integer>> yearMap;

		SearchMap() {
			this.nameMap = new HashMap<String, ArrayList<Integer>>();
			this.dateMap = new HashMap<LocalDate, ArrayList<Integer>>();
			this.timeMap = new HashMap<LocalTime, ArrayList<Integer>>();
			this.monthMap = new HashMap<Integer, ArrayList<Integer>>();
			this.dayMap = new HashMap<Integer, ArrayList<Integer>>();
			this.yearMap = new HashMap<Integer, ArrayList<Integer>>();
		}

		/**
		 * This operation adds the properties of todo into the different maps.
		 * 
		 * @param todo
		 */
		public void add(Todo todo) {
			int id = todo.getId();

			addToNameMap(todo.getName(), id);
			Todo.TYPE type = todo.getType();
			DateTime startDateTime = todo.getStartTime();
			DateTime endDateTime = todo.getEndTime();

			switch (type) {
				case DEADLINE:
					assert (endDateTime != null);
					assert (startDateTime == null);
					addToAllDateMaps(endDateTime, id);
					break;
				case EVENT:
					assert (endDateTime != null);
					assert (startDateTime != null);
					if (!todo.isSameDayEvent()) {
						ArrayList<Todo> shortTodos = todo.breakIntoShortEvents();
						for (Todo shortTodo : shortTodos) {
							addToAllDateMaps(shortTodo.getStartTime(), id);
							addToAllDateMaps(shortTodo.getEndTime(), id);
						}
					} else {
						addToAllDateMaps(startDateTime, id);
						addToAllDateMaps(endDateTime, id);
					}
					break;
				default:
					assert (startDateTime == null);
					assert (endDateTime == null);
			}

		}

		
		

		/**
		 * This operation adds the dateTime property of todo with the given id
		 * into the various date related maps
		 * 
		 * @param dateTime
		 * @param id
		 */
		private void addToAllDateMaps(DateTime dateTime, int id) {
			// add id to dateMap
			LocalDate date = dateTime.toLocalDate();
			if (dateMap.containsKey(date)) {
				if (!dateMap.get(date).contains(id)) {
					dateMap.get(date).add(id);
				}
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				dateMap.put(date, newIdList);
			}

			// add id to timeMap
			LocalTime time = dateTime.toLocalTime();
			if (timeMap.containsKey(time)) {
				if (!timeMap.get(time).contains(id)) {
					timeMap.get(time).add(id);
				}
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				timeMap.put(time, newIdList);
			}

			// add id to dayMap
			int day = dateTime.getDayOfWeek();
			if (dayMap.containsKey(day)) {
				if (!dayMap.get(day).contains(id)) {
					dayMap.get(day).add(id);
				}
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				dayMap.put(day, newIdList);
			}

			// add id to monthMap
			int month = dateTime.getMonthOfYear();
			if (monthMap.containsKey(month)) {
				if (!monthMap.get(month).contains(id)) {
					monthMap.get(month).add(id);
				}
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				monthMap.put(month, newIdList);
			}

			// add id to yearMap
			int year = dateTime.getYear();
			if (yearMap.containsKey(year)) {
				if (!yearMap.get(year).contains(id)) {
					yearMap.get(year).add(id);
				}
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				yearMap.put(year, newIdList);
			}
		}

		/**
		 * This operation removes the different properties of given todo from
		 * the different maps.
		 * 
		 * @param todo
		 */
		public void remove(Todo todo) {
			int id = todo.getId();
			String name = todo.getName();
			removeIdFromNames(name, id);

			Todo.TYPE type = todo.getType();
			DateTime startDateTime = todo.getStartTime();
			DateTime endDateTime = todo.getEndTime();

			switch (type) {
				case DEADLINE:
					assert (endDateTime != null);
					assert (startDateTime == null);
					removeIdFromAllDateMaps(endDateTime, id);
					break;
				case EVENT:
					assert (endDateTime != null);
					assert (startDateTime != null);
					if (!todo.isSameDayEvent()) {
						ArrayList<Todo> shortTodos = todo.breakIntoShortEvents();
						for (Todo shortTodo: shortTodos) {
							removeIdFromAllDateMaps(shortTodo.getStartTime(), id);
							removeIdFromAllDateMaps(shortTodo.getEndTime(), id);
						}
					} else {
						removeIdFromAllDateMaps(startDateTime, id);
						removeIdFromAllDateMaps(endDateTime, id);
					}
					break;
				default:
					assert (startDateTime == null);
					assert (endDateTime == null);
			}

		}

		/**
		 * This operation removes the DateTime property of todo with the given
		 * id from the various date related maps
		 * 
		 * @param dateTime
		 * @param id
		 */
		private void removeIdFromAllDateMaps(DateTime dateTime, int id) {
			// remove id from dateMap
			LocalDate date = dateTime.toLocalDate();
			if(dateMap.containsKey(date) && dateMap.get(date).contains(id)) {
				int todoIdDateIndex = dateMap.get(date).indexOf(id);
				dateMap.get(date).remove(todoIdDateIndex);
				if (dateMap.get(date).isEmpty()) {
					dateMap.remove(date);
				}
			}
			
			// remove id from timeMap
			LocalTime time = dateTime.toLocalTime();
			if(timeMap.containsKey(time) && timeMap.get(time).contains(id)) {
				int todoIdTimeIndex = timeMap.get(time).indexOf(id);
				timeMap.get(time).remove(todoIdTimeIndex);
				if (timeMap.get(time).isEmpty()) {
					timeMap.remove(time);
				}
			}
			
			// remove id from dayMap
			int day = dateTime.getDayOfWeek();
			if(dayMap.containsKey(day) && dayMap.get(day).contains(id)) {
				int todoIdDayIndex = dayMap.get(day).indexOf(id);
				dayMap.get(day).remove(todoIdDayIndex);
				if (dayMap.get(day).isEmpty()) {
					dayMap.remove(day);
				}
			}
			
			// remove id from monthMap
			int month = dateTime.getMonthOfYear();
			if(monthMap.containsKey(month) && monthMap.get(month).contains(id)) {
				int todoIdMonthIndex = monthMap.get(month).indexOf(id);
				monthMap.get(month).remove(todoIdMonthIndex);
				if (monthMap.get(month).isEmpty()) {
					monthMap.remove(month);
				}
			}
			
			// remove id from yearMap
			int year = dateTime.getYear();
			if(yearMap.containsKey(year) && yearMap.get(year).contains(id)) {
				int todoIdYearIndex = yearMap.get(year).indexOf(id);
				yearMap.get(year).remove(todoIdYearIndex);
				if (yearMap.get(year).isEmpty()) {
					yearMap.remove(year);
				}
			}
			
		}

		/**
		 * This operation removes the name of the todo with the given id from
		 * name map.
		 * 
		 * @param name
		 * @param id
		 */
		private void removeIdFromNames(String name, int id) {
			String[] nameArray = name.split(REGEX_SPACE);
			for (String x : nameArray) {
				if(nameMap.containsKey(x) && nameMap.get(x).contains(id)) {
					int todoIdIndex = nameMap.get(x).indexOf(id);
					nameMap.get(x).remove(todoIdIndex);
					if (nameMap.get(x).isEmpty()) {
						nameMap.remove(x);
					}
				}
			}
		}

		/**
		 * This operation adds the name property of the todo with the given id
		 * into the name map.
		 * 
		 * @param name
		 * @param id
		 */
		private void addToNameMap(String name, int id) {
			String[] nameArray = name.split(REGEX_SPACE);
			for (String x : nameArray) {
				if (nameMap.containsKey(x)) {
					nameMap.get(x).add(id);
				} else {
					ArrayList<Integer> newIdList = new ArrayList<Integer>();
					newIdList.add(id);
					nameMap.put(x, newIdList);
				}
			}
		}

		/**
		 * This operation retrieves a result of todo ids when searching the
		 * given searchKey within the given typeKey.
		 * 
		 * @param typeKey
		 * @param searchKey
		 * @return todoIds
		 */
		public ArrayList<Integer> getResult(Keywords typeKey, String searchKey) {
			// searchKey can only be String if searchType is name
			assert (typeKey == Keywords.NAME);
			ArrayList<Integer> toDoIds = new ArrayList<Integer>();

			if (nameMap.containsKey(searchKey)) {
				toDoIds = nameMap.get(searchKey);
			}
			return toDoIds;
		}

		/**
		 * This operation retrieves a result of todo ids when searching the
		 * given datetime within the given typeKey.
		 * 
		 * @param typeKey
		 * @param dateTime
		 * @return todoIds
		 */
		public ArrayList<Integer> getResult(Keywords typeKey, DateTime dateTime)
				throws InvalidParamException {
			ArrayList<Integer> toDoIds = new ArrayList<Integer>();
			switch (typeKey) {
				case DATE:
					LocalDate searchDate = dateTime.toLocalDate();
					if (dateMap.containsKey(searchDate)) {
						toDoIds = dateMap.get(searchDate);
					} // else searchDate is not in dateMap, toDoIds is empty
						// List
					break;
				case DAY:
					int searchDay = dateTime.getDayOfWeek();
					if (dayMap.containsKey(searchDay)) {
						toDoIds = dayMap.get(searchDay);
					}// else searchDay is not in dayMap, toDoIds is empty List
					break;
				case MONTH:
					int searchMonth = dateTime.getMonthOfYear();
					if (monthMap.containsKey(searchMonth)) {
						toDoIds = monthMap.get(searchMonth);
					}// else searchMonth is not in monthMap, toDoIds is empty
						// List
					break;
				case TIME:
					LocalTime searchTime = dateTime.toLocalTime();
					if (timeMap.containsKey(searchTime)) {
						toDoIds = timeMap.get(searchTime);
					}// else searchTime is not in timeMap, toDoIds is empty List
					break;
				case YEAR:
					int searchYear = dateTime.getYear();
					if (yearMap.containsKey(searchYear)) {
						toDoIds = yearMap.get(searchYear);
					} // else searchYear is not in yearMap, todoIds is empty
						// List
					break;
				default:
					throw new InvalidParamException(
							ExceptionMessages.INVALID_SEARCH_TYPE);
			}

			return toDoIds;
		}

		

		public void update(int userIndex, String param, String originalParam) {
			if(originalParam != null) {
				removeIdFromNames(originalParam, userIndex);
			}
			if(param != null) {
				addToNameMap(param, userIndex);
			}	
		}

		public void update(int userIndex, DateTime param, DateTime originalParam) {
			if (originalParam != null) {
				removeIdFromAllDateMaps(originalParam, userIndex);
			}
			if (param != null) {
				addToAllDateMaps(param, userIndex);	
			}
		}
	}

	/**
	 * This operation retrieves a list of ids of todos that has the given
	 * searchString in its property of given typeKey
	 * 
	 * @param typeKey
	 * @param searchString
	 * @return todoIds
	 * @throws InvalidParamException
	 */
	public ArrayList<Integer> search(Keywords typeKey, String searchString)
			throws InvalidParamException {
		// search method with String type search key is only for search in Todo
		// names
		assert (typeKey == Keywords.NAME);

		ArrayList<Integer> tempTodoIds;
		ArrayList<Integer> todoIds = new ArrayList<Integer>();
		String[] paramArray = searchString.split(REGEX_SPACE);
		for (String searchKey : paramArray) {
			tempTodoIds = searchMap.getResult(typeKey, searchKey);
			todoIds.addAll(tempTodoIds);
		}
		return todoIds;
	}

	/**
	 * This operation retrieves a list of ids of todos that has the given
	 * dateTime in its property of given typeKey
	 * 
	 * @param typeKey
	 * @param dateTime
	 * @return todoIds
	 */
	public ArrayList<Integer> search(Keywords typeKey, DateTime dateTime)
			throws InvalidParamException {
		return searchMap.getResult(typeKey, dateTime);
	}

	

	/**
	 * Saves this instance of memory to file by calling the storeMemoryToFile
	 * method in the StorageHandler object.
	 */
	public void saveToFile() {
		storage.storeMemoryToFile(this);
	}

	public void setStorageHandler(StorageHandler storage) {
		this.storage = storage;
	}

	
	public void updateMaps(int userIndex, String param, String originalParam) {
		searchMap.update(userIndex, param, originalParam);

	}

	public void updateMaps(int userIndex, DateTime date, DateTime originalDate) {
		searchMap.update(userIndex, date, originalDate);
	}
}
