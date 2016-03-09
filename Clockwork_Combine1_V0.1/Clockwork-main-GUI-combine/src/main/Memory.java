package main;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Stores all Todos and keeps state information allowing Undo and Redo
 * operations. Maximum number of states that can be stored by Memory is
 */

public class Memory {
	
	private static final int STACK_MAX_SIZE = 5;
	private static final int ID_BUFFER_INITIAL_SIZE = 5;
	private static final int ID_BUFFER_MAX_SIZE = 2 * ID_BUFFER_INITIAL_SIZE;
	private final IDBuffer idBuffer = new IDBuffer();
	private int startingId;
	private HashMap<Integer, Todo> memoryMap;
	private LinkedList<Todo> undoStack;
	private LinkedList<Todo> redoStack;

	/**
	 * Constructs an empty Memory object.
	 */
	public Memory() {
		this.startingId = 0;
		this.memoryMap = new HashMap<Integer, Todo>();
		this.undoStack = new LinkedList<Todo>();
		this.redoStack = new LinkedList<Todo>();
	}

	/**
	 * Adds the specified Todo to memory. The current state is saved prior to
	 * any operation.
	 * 
	 * @param todo the Todo to be added.
	 */
	public void add(Todo todo) {
		save(todo.getPlaceholder());
		flushRedoStack();
		memoryMap.put(todo.getId(), todo);
	}

	/**
	 * Retrieves the Todo identified by the specified ID from the memory.
	 * 
	 * @param id the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException if the Todo identified by the specified
	 *             ID does not exist.
	 */
	public Todo get(int id) throws NullTodoException {
		Todo returnTodo = memoryMap.get(id);
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
	 * @throws NullTodoException if the Todo identified by the specified
	 *             ID does not exist.
	 */
	public Todo setterGet(int id) throws NullTodoException {
		Todo returnTodo = memoryMap.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		save(returnTodo);
		flushRedoStack();
		return returnTodo;
	}

	/**
	 * Removes the Todo identified by the specified id from the memory. The
	 * current state is saved prior to any operation.
	 * 
	 * @param id the ID of the Todo to be removed.
	 * @throws NullTodoException if the Todo identified by the specified
	 *             ID does not exist.
	 */
	public Todo remove(int id) throws NullTodoException {
		Todo returnTodo = memoryMap.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		save(returnTodo);
		flushRedoStack();
		memoryMap.remove(id);
		return returnTodo;
	}

	/**
	 * Saves the a copy of the state of a Todo into the undo stack. If the Todo
	 * specified is null, a placeholder is used instead. The stack never
	 * contains null values. If the maximum stack size is reached, the earliest
	 * state is discarded. If the stack and memory no longer contains a
	 * particular Todo, its ID is returned to the pool of available indices.
	 * 
	 * @param toBeSaved the Todo to be saved.
	 */
	private void save(Todo toBeSaved) {
		// If undo stack has exceeded max size, discard earliest state.
		Todo toBeSavedCopy = new Todo(toBeSaved);
		if (undoStack.size() > STACK_MAX_SIZE) {
			int id = undoStack.removeFirst().getId();
			if(!memoryMap.containsKey(id)) {
				releaseId(id);;
			}
		}
		undoStack.add(toBeSavedCopy);
	}
	
	private void flushRedoStack() {
		while(!redoStack.isEmpty()) {
			int id = redoStack.pollLast().getId();
			if(!memoryMap.containsKey(id)) {
				releaseId(id);;
			}
		}
	}

	/**
	 * Restores the latest history state of the memory. Also known as the undo
	 * operation.
	 * 
	 * @throws StateUndefinedException if there are no history states to restore
	 *             to.
	 */
	public void restoreHistoryState() throws StateUndefinedException {
		Todo fromStack;
		try {
			fromStack = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(ExceptionMessages.NO_HISTORY_STATES_EXCEPTION);
		}

		int id = fromStack.getId();
		Todo inMemory = memoryMap.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		// Redo stack will not exceed maximum size.
		redoStack.add(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.getCreatedOn() == null) {
			memoryMap.remove(id);
		} else {
			memoryMap.put(id, fromStack);
		}
	}

	/**
	 * Restores the latest future state of the memory. Also known as the redo
	 * operation.
	 * 
	 * @throws StateUndefinedException if there are no future states to restore
	 *             to.
	 */
	public void restoreFutureState() throws StateUndefinedException {
		Todo fromStack;
		try {
			fromStack = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(ExceptionMessages.NO_FUTURE_STATES_EXCEPTION);
		}

		int id = fromStack.getId();
		Todo inMemory = memoryMap.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		save(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.getCreatedOn() == null) {
			memoryMap.remove(id);
		} else {
			memoryMap.put(id, fromStack);
		}
	}

	/**
	 * Method to get all the Todos for displaying purposes
	 * 
	 * @return all Todos as Collection
	 */
	public Collection<Todo> getAllTodos() {
		return memoryMap.values();
	}
	
    /**
     * Method to export the current memory into a json String for external
     * storage
     * 
     * @return json String
     */
    public String exportAsJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class,
                new DateTimeTypeConverter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(this);
        return jsonString;

    }

    /**
     * Method to parse a json String representing an instance of memory into an
     * instance of Memory class
     * 
     * @param jsonString
     *            json representation of an instance of memory as String
     * @return an instance of Memory class
     */
    public static Memory importFromJson(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class,
                new DateTimeTypeConverter());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonString, Memory.class);
    }

    /**
     * Converter to serialize and deserialize between org.joda.time.DateTime and
     * com.google.gson.JsonElement
     * 
     * Written with the help of the Gson user guide at
     * 
     * https://sites.google.com/site/gson/gson-user-guide
     */
    private static class DateTimeTypeConverter implements
            JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
        @Override
        public JsonElement serialize(DateTime src, Type srcType,
                JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public DateTime deserialize(JsonElement json, Type type,
                JsonDeserializationContext context) throws JsonParseException {
            try {
                return new DateTime(json.getAsJsonPrimitive().getAsString());
            } catch (IllegalArgumentException e) {
                // Try parsing as java.util.Date instead
                Date date = context.deserialize(json, Date.class);
                return new DateTime(date);
            }
        }

    }

	/**
	 * Obtains an ID number from the pool of available ID numbers.
	 * 
	 * @return the ID obtained.
	 */
	public int obtainFreshId() {
		return idBuffer.get();
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
	
    /**
	 * Serves as a buffer of fixed size for new Todos to draw their ID from.
	 */
	private class IDBuffer {
		private TreeSet<Integer> buffer;
		
		private IDBuffer() {
			buffer = new TreeSet<Integer>();
			for (int i = startingId; i < startingId + ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.add(i);
			}
		}

		protected int get() {
			if (buffer.size() == 1) {
				loadToSize();
			}
			return buffer.pollFirst();
		}
		
		protected void put(int id) {
			buffer.add(id);
			if (buffer.size() > ID_BUFFER_MAX_SIZE) {
				unloadToSize();
			}
		}

		private void loadToSize() { // Bug: Remove 5 elements or so from a memory of size 10. Will load duplicate ID unnecessarily
			int largestId = buffer.last();
			for (int i = largestId; i < largestId + ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.add(i);
			}
		}
		
		private void unloadToSize() {
			for (int i = 0; i < ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.pollLast();
			}
		}
	}
}
