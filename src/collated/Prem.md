# Prem
###### \exceptions\InvalidDateException.java
``` java
	public InvalidDateException() {

	}

	public InvalidDateException(String message) {
		super(message);
	}
}
```
###### \exceptions\InvalidParamException.java
``` java
	public InvalidParamException() {
	}
	
	public InvalidParamException(String message) {
		super(message);
	}
}
```
###### \exceptions\InvalidPeriodException.java
``` java
	public InvalidPeriodException() {
	}

	public InvalidPeriodException(String message) {
		super(message);
	}
}
```
###### \exceptions\InvalidRecurringException.java
``` java
	public InvalidRecurringException() {

	}

	public InvalidRecurringException(String message) {
		super(message);
	}
}
```
###### \exceptions\InvalidTodoNameException.java
``` java
	public InvalidTodoNameException() {

	}

	public InvalidTodoNameException(String message) {
		super(message);
	}
}
```
###### \exceptions\NotRecurringException.java
``` java
	public NotRecurringException() {
		
	}
	
	public NotRecurringException(String message) {
		super(message);
	}
}
```
###### \exceptions\NullRuleException.java
``` java
	public NullRuleException(String message) {
		super(message);
	}

}
```
###### \exceptions\NullTodoException.java
``` java
	public NullTodoException () {
		super();
	}
	
	public NullTodoException (String message) {
		super(message);
	}

}
```
###### \exceptions\ParsingFailureException.java
``` java
	public ParsingFailureException() {
	}

	public ParsingFailureException(String message) {
		super(message);
	}

}
```
###### \exceptions\StateUndefinedException.java
``` java
	public StateUndefinedException() {
	}

	public StateUndefinedException(String message) {
		super(message);
	}
}
```
###### \logic\ClockWork.java
``` java
	public ClockWork() {
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		memory.onCreate();
		Parser.initialize();
	}
	
	public ClockWork(String fileDir) {
		fileDirectory = fileDir;
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		Parser.initialize();
	}
	
	public static ClockWork getInstance() {
		if (logic == null) {
			logic = new ClockWork();
		}
		return logic;
	}
	
	public static void setFileDirectory(String storageFile){
		fileDirectory = storageFile;
	}
   
    /**
     * Clears the user's console, OS-dependent implementation
     */
	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				for (int i = 0; i < 105; i++) {
					System.out.println();
				}
			} else {
				final String ANSI_CLS = "\u001b[2J";
				final String ANSI_HOME = "\u001b[H";
				System.out.print(ANSI_CLS + ANSI_HOME);
				System.out.flush();
			}
		} catch (final Exception e) {
			System.out.println("error in clearing");
		}
	}
 
	/**
	 * The main logic unit of Clockwork. Reads the input from Clockwork and
	 * passes it to the Parser, the first element in the flow of component calls
	 * present in all operations.
	 * 
	 * @param args
	 *            contains arguments from the command line at launch. (Not used)
	 */
	public static void ClockworkLogicMain(String input, ClockWork logic) {

			clearConsole();
			Signal signal;
			try {
				signal = logic.handleInput(input);
			} catch (InvalidRecurringException e) {
				signal = new Signal(Signal.ADD_INVALID_RECURRING_ERROR, false);
			} catch(InvalidTodoNameException e) {
				signal = new Signal(Signal.ADD_INVALID_TODO_NAME_ERROR, false);
			} catch (ParsingFailureException e) {
				signal = new Signal(Signal.DATE_PARSING_ERROR, false);
			}
			SignalHandler.printSignal(signal);
	}

	public Signal handleInput(String input) throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		ParsedInput c = Parser.parseInput(input);
		return execute(c);
	}
	
	/**
	 * Creates a Command object with the given ParsedInput and executes it.
	 * 
	 * @param userInput
	 *            input from user, parsed by the Parser.
	 * @return a Signal containing a message to be printed, denoting success or
	 *         failure of the execution.
	 */
	private Signal execute(ParsedInput userInput) {
		Signal processSignal;
		Command c;

		Keywords commandType = userInput.getType();
		if (commandType == Keywords.ERROR) {
			return new Signal(String.format(
					Signal.GENERIC_INVALID_COMMAND_FORMAT, ""), false);
		} else {

			switch (commandType) {
                case ADD :
                    c = new AddCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case DELETE :
                    c = new DeleteCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case MARK :
                    c = new MarkCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case REDO :
                    c = new RedoCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case UNDO :
                    c = new UndoCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case EDIT :
                    c = new EditCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case DISPLAY :
                    c = new DisplayCommand(userInput, memory);
                    processSignal = c.execute();
                    break;

                case SEARCH :
                    c = new SearchCommand(userInput, memory);
                    processSignal = c.execute();
                    break;

                case EXIT :
                    c = new ExitCommand(userInput, memory);
                    processSignal = c.execute();
                    break;

                default :
                    // NOTE: This case should never happen
                    processSignal = new Signal(Signal.GENERIC_FATAL_ERROR,
                            false);
                    System.exit(-1);
                    break;
			}

			return processSignal;
		}
	}
	
	public void deleteStorageFile() {
		StorageHandler.deleteStorageFileIfExists(fileDirectory);
	}
	
	public void reloadMemory() {
		memory = new Memory();
		memory.setStorageHandler(storage);
	}
	
	public static String getStorageFileDirFromSettings(){
		return StorageUtils.readSettingsFile();
	}
}
```
###### \logic\Command.java
``` java
	/**
	 * Constructs a new Command object with the given parameters and reference
	 * to the memory that stores the Todos.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public Command(ParsedInput input, Memory memory) {
		this.keyParamPairs = input.getParamPairs();
		this.dateTimes = input.getDateTimes();
		this.input = input;
		this.memory = memory;
	}

	public abstract Signal execute();

}
```
###### \logic\DeleteCommand.java
``` java
	/**
	 * Creates a DeleteCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public DeleteCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Processes a ParsedInput object containing the delete command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		boolean isRecurringRule = false;
		
		// Check 2nd Keyword for -r flag
		if (keyParamPairs.size() == 2) {
			if (keyParamPairs.get(1).getKeyword() == Keywords.RULE) {
				keyParamPairs.remove(1);
				isRecurringRule = true;
			}
		}
		
		// Ensure that there is only one KeyParamPair in inputList
		if (!input.containsOnlyCommand()) {
			return new Signal(Signal.DELETE_INVALID_PARAMS, false);
		}
		
		if(input.containsEmptyParams()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}
    	
    	int deleteIndex;
    	Signal returnSignal;
    	Todo deleted;
    	RecurringTodoRule deletedRule;
		try {
			deleteIndex = Integer.parseInt(keyParamPairs.get(0).getParam());
			if(isRecurringRule) {
				int recurringId = memory.getTodo(deleteIndex).getRecurringId();
				deletedRule = memory.removeRule(recurringId);
				returnSignal = new Signal(String.format(Signal.DELETE_SUCCESS_FORMAT, deletedRule), true);
			} else {
				deleted = memory.removeTodo(deleteIndex);
				returnSignal = new Signal(String.format(Signal.DELETE_SUCCESS_FORMAT, deleted),
		                true);
			}
			memory.saveToFile();
		} catch (NumberFormatException e) {
            return new Signal(Signal.DELETE_INVALID_PARAMS, false);
		} catch (NullTodoException e) {
            return new Signal(e.getMessage(), false);
		} catch (NullRuleException e) {
			return new Signal(e.getMessage(), false);
		} catch (NotRecurringException e) {
			return new Signal(e.getMessage(), false);
		}
		
        return returnSignal;
	}

}
```
###### \logic\RedoCommand.java
``` java
	/**
	 * Creates a RedoCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public RedoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Reverses the last undo operation.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {	
		if(!(input.containsOnlyCommand() && input.containsEmptyParams())){
            return new Signal(Signal.REDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.redo();
			memory.saveToFile();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.REDO_SUCCESS, true);
	}
}
```
###### \logic\Signal.java
``` java
	/**
	 * Constructor for Signal
	 * 
	 * @param signal
	 */
	public Signal(String signal, boolean isSuccessful) {
		this.message = signal;
		if (!isSuccessful) {
			this.message = ERROR_PREFIX.concat(this.message);
		}
	}

	@Override
	public String toString() {
		return message;
	}

	public static boolean areParamsEqual(String[] params1, String[] params2) {
		// check if params1 and params2 are null
		if (params1 == null && params2 == null) {
			return true;
		} else if (params1 == null && params2 != null) {
			return false;
		} else if (params1 != null && params2 == null) {
			return false;
		}

		// Neither params1 nor params2 are null.
		// Check for equal length
		if (params1.length != params2.length) {
			return false;
		}
		// Every string in params1 is equal to every corresponding string in
		// params2
		int index = 0;
		for (String str : params1) {
			if (!str.equals(params2[index])) {
				return false;
			}
			index++;
		}
		return true;
	}

	@Override
	// for unit testing purposes
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Signal other = (Signal) obj;

		if (!(this.message.equals(other.message))) {
			return false;
		}
		return true;
	}
}
```
###### \logic\Todo.java
``` java
	/**
	 * Constructs a Todo of type: TASK.
	 * 
	 * @param id
	 *            the ID of the Todo.
	 * @param name
	 *            name of the task.
	 */
	public Todo(int id, String name) {
		this.id = id;
		this.name = name;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
		this.type = TYPE.TASK;
		this.recurringId = null;
	}

	/**
	 * Constructs a Todo of type: DEADLINE or EVENT.
	 * 
	 * ASSUMPTION: dateTimeList is not empty or null and has only either 1 or 2
	 * dates.
	 * 
	 * @param id
	 *            the ID of the Todo.
	 * @param name
	 *            name of the task.
	 * @param dateTimes
	 *            a List of DateTimes specifying the end and/or start times.
	 */
	public Todo(int id, String name, List<DateTime> dateTimes) {
		this.id = id;
		this.name = name;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.isDone = false;
		if (dateTimes.size() == 1) {
			this.startTime = null;
			this.endTime = dateTimes.get(0);
			this.type = TYPE.DEADLINE;
		} else if (dateTimes.size() == 2) {
			this.startTime = dateTimes.get(0);
			this.endTime = dateTimes.get(1);
			this.type = TYPE.EVENT;
		}
		this.recurringId = null;
	}

	/**
	 * Constructs a Recurring Todo of type: DEADLINE or EVENT.
	 * 
	 * ASSUMPTION: dateTimeList is not empty or null and has only either 1 or 2
	 * dates.
	 * 
	 * @param id
	 *            the ID of the Todo.
	 * @param name
	 *            name of the task.
	 * @param dateTimes
	 *            a List of DateTimes specifying the end and/or start times.
	 * @param recurringId
	 *            the ID for the recurring rule
	 */
	public Todo(int id, String name, List<DateTime> dateTimes, int recurringId) {
		this.id = id;
		this.name = name;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.isDone = false;
		if (dateTimes.size() == 1) {
			this.startTime = null;
			this.endTime = dateTimes.get(0);
			this.type = TYPE.DEADLINE;
		} else if (dateTimes.size() == 2) {
			this.startTime = dateTimes.get(0);
			this.endTime = dateTimes.get(1);
			this.type = TYPE.EVENT;
		}
		this.recurringId = recurringId;

	}

	/**
	 * Makes an exact copy of another Todo.
	 * 
	 * @param todo
	 *            the Todo to be copied.
	 */
	private Todo(Todo todo) {
		this.id = todo.id;
		this.name = todo.name;
		this.createdOn = todo.createdOn;
		this.modifiedOn = todo.modifiedOn;
		this.startTime = todo.startTime;
		this.endTime = todo.endTime;
		this.isDone = todo.isDone;
		this.type = todo.type;
		this.recurringId = todo.recurringId;
	}

	/**
	 * Constructs a placeholder Todo with null fields except the ID. To be used
	 * by Memory class in its stacks for undo/redo operations.
	 * 
	 * @param id
	 *            the ID of the Todo that was removed from Memory.
	 */
	private Todo(int id, Integer recurringId) {
		this.id = id;
		this.recurringId = recurringId;
	}
	
	public Todo copy() {
		return new Todo(this);
	}
	
	/**
	 * Returns the placeholder Todo constructed from the ID of this Todo. For
	 * use in Undo and Redo stacks in Memory.
	 */
	public Todo getPlaceholder() {
		return new Todo(id, recurringId);
	}
	
	public boolean isPlaceholder() {
		return createdOn == null;
	}

	/**
	 * Returns the ID of the Todo.
	 * 
	 * @return the ID of the Todo.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the Todo.
	 * 
	 * @return the name of the Todo.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Replaces the title with the specified String and updates the last
	 * modified time.
	 * 
	 * @param title
	 *            the new title of the Todo.
	 */
	public void setName(String title) {
		this.name = title;
		modifiedOn = new DateTime();
	}

	/**
	 * Returns the start time of the Todo.
	 * 
	 * @return the start time of the Todo.
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * Replaces the start time with the date encoded in the specified
	 * startTimeString and updates the last modified time.
	 * 
	 * @param startTime
	 *            DateTime of the new startTime
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
		modifiedOn = new DateTime();
	}
	
	/**
	 * Returns the end time of the Todo.
	 * 
	 * @return the end time of the Todo.
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * Replaces the end time with the specified DateTime and updates the last
	 * modified field of the Todo.
	 * 
	 * @param endTime
	 *            String containing the new end time of the Todo.
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
		modifiedOn = new DateTime();
	}

	/**
	 * Checks if the Todo is marked as done.
	 * 
	 * @return true if the Todo has been marked as done, false otherwise.
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * Marks the Todo as done or undone and updates the last modified time.
	 * 
	 * @param isDone
	 *            the new status of the Todo.
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
		modifiedOn = new DateTime();
	}

	/**
	 * Returns the time at which the Todo was created.
	 * 
	 * @return the time at which the Todo was created.
	 */
	public DateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * Returns the time at which the Todo was last modified.
	 * 
	 * @return the time at which the Todo was last modified.
	 */
	public DateTime getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * Returns the type of the Todo as specified by Todo.TYPE.
	 * 
	 * @return returns the type of the Todo.
	 */
	public TYPE getType() {
		return type;
	}
	
```
###### \logic\UndoCommand.java
``` java
	/**
	 * Creates an UndoCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */	
	public UndoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Reverses the last modifying operation.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		//check if the number of parameters is correct
		if(!(input.containsOnlyCommand() && input.containsEmptyParams())){
            return new Signal(Signal.UNDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.undo();
			memory.saveToFile();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.UNDO_SUCCESS, true);
	}
}
```
###### \logic\UndoRedoStack.java
``` java
	public UndoRedoStack(HashMap<Integer, T> memory, IDBuffer<T> idBuffer, int maxStates) {
		this.undoStack = new LinkedList<T>();
		this.redoStack = new LinkedList<T>();
		this.memory = memory;
		this.idBuffer = idBuffer;
		this.maxStates = maxStates;
	}
		
	/**
	 * Saves the a copy of the state of an object into the undo stack. If the object
	 * specified is null, a placeholder is used instead.
	 * <p>
	 * The stack never contains null values. <br>
	 * If the maximum stack size is reached, the earliest state is discarded. <br>
	 * If the stack and memory no longer contains a particular object, its ID is
	 * returned to the pool of available IDs.
	 * 
	 * @param toBeSaved the object to be saved.
	 */
	public void save(T toBeSaved) {
		T toBeSavedCopy = toBeSaved.copy();
		undoStack.add(toBeSavedCopy);
		
		// If undo stack has exceeded max size, discard earliest state.
		if (undoStack.size() > maxStates) {
			int id = undoStack.removeFirst().getId();
			if (!memory.containsKey(id)) {
				idBuffer.put(id);
			}
		}
	}
	
	/**
	 * Restores the latest future state of the object.
	 * 
	 * @throws StateUndefinedException if there are no future states to restore
	 *             to.
	 */
	public T restoreFutureState() throws StateUndefinedException {
		T fromStack;
		try {
			fromStack = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(
					ExceptionMessages.NO_FUTURE_STATES);
		}

		int id = fromStack.getId();
		T inMemory = memory.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		save(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.isPlaceholder()) {
			memory.remove(id);
		} else {
			memory.put(id, fromStack);
		}
		return fromStack;
	}
	
	/**
	 * Restores the latest history state of the object.
	 * 
	 * @throws StateUndefinedException if there are no history states to restore
	 *             to.
	 */
	public T restoreHistoryState() throws StateUndefinedException {
		T fromStack;
		try {
			fromStack = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(
					ExceptionMessages.NO_HISTORY_STATES);
		}

		int id = fromStack.getId();
		T inMemory = memory.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		// Redo stack will not exceed maximum size.
		redoStack.add(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.isPlaceholder()) {
			memory.remove(id);
		} else {
			memory.put(id, fromStack);
		}
		return fromStack;
	}
	
	
	/**
	 * Returns the first object from the stack of history state.
	 * 
	 * @return the first object from the stack of history state.
	 */
	public T peekHistoryState() {
		return undoStack.peekLast();
	}
	
	/**
	 * Returns the first object from the stack of future states.
	 * 
	 * @return the first object from the stack of future states.
	 */
	public T peekFutureState() {
		return redoStack.peekLast();
	}
	

	/**
	 * Flushes both undo and redo stacks to release all reserved IDs back to the
	 * pool of available IDs.
	 */
	public void flushStacks() {
		flushRedoStack();
		flushUndoStack();
	}

	/**
	 * Flushes the undoStack of all states of objects.
	 */
	private void flushUndoStack() {
		while (!undoStack.isEmpty()) {
			int id = undoStack.pollLast().getId();
			if (!memory.containsKey(id)) {
				idBuffer.put(id);
			}
		}
	}

	/**
	 * Flushes the redoStack of all states of objects.
	 */
	public void flushRedoStack() {
		while (!redoStack.isEmpty()) {
			int id = redoStack.pollLast().getId();
			if (!memory.containsKey(id)) {
				idBuffer.put(id);
			}
		}
	}
}
```
###### \parser\IDBuffer.java
``` java
	public IDBuffer(HashMap<Integer, E> memory) {
		this.buffer = new TreeSet<Integer>();
		this.minFreeId = ID_INITIAL;
		for (int i = ID_INITIAL; i < ID_INITIAL + ID_BUFFER_INITIAL_SIZE; i++) {
			buffer.add(i);
		}
		this.memory = memory;
	}
	
	public void setMemory(HashMap<Integer, E> memory) {
		this.memory = memory; 
	}

	public int get() {
		if (buffer.size() == 1) {
			loadToSize();
		}
		int returnId = buffer.pollFirst();
		minFreeId = buffer.first();
		return returnId;
	}

	public void put(int id) {
		if (id < minFreeId) {
			minFreeId = id;
		}
		buffer.add(id);
		if (buffer.size() > ID_BUFFER_MAX_SIZE) {
			unloadToSize();
		}
	}

	private void loadToSize() {
		int minUnloadedId = minFreeId + 1;
		int i = minUnloadedId;

		while (i < minUnloadedId + ID_BUFFER_INITIAL_SIZE) {
			if (memory.containsKey(i)) { // TODO: DEPENDENCY
				minUnloadedId++;
			} else {
				buffer.add(i);
				i++;
			}
		}
	}

	private void unloadToSize() {
		for (int i = 0; i < ID_BUFFER_INITIAL_SIZE; i++) {
			buffer.pollLast();
		}
	}
}
```
###### \parser\KeyParamPair.java
``` java
	/**
	 * Constructs a KeyParamPair object with fields equal to the respective parameters.
	 * @param inputKeyword
	 * @param inputKeyString
	 * @param inputParam
	 */
	public KeyParamPair(Keywords inputKeyword, String inputKeyString, String inputParam) {
		keyword = inputKeyword;
		keyString = inputKeyString;
		param = inputParam;
	}

	/**
	 * Returns the keyword as a Keywords enum type
	 * @return keyword of the pair
	 */
	public Keywords getKeyword() {
		return keyword;
	}

	/**
	 * Returns the parameters of the pair
	 * @return parameters
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Returns the keyword as its original String
	 * @return keyword of the pair as String
	 */
	public String getKeyString() {
		return keyString;
	}

	/**
	 * Sets the parameters of the pair as param
	 * @param param
	 */
	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public boolean equals(Object other) {
		if (other.getClass() == this.getClass()) {
			Keywords xKeyword = this.getKeyword();
			Keywords yKeyword = ((KeyParamPair) other).getKeyword();
			String xKeyString = this.getKeyString();
			String yKeyString = ((KeyParamPair) other).getKeyString();
			String xParam = this.getParam();
			String yParam = ((KeyParamPair) other).getParam();
			return xKeyword.equals(yKeyword) && xParam.equals(yParam)
					&& xKeyString.equals(yKeyString);
		} else {
			return false;
		}
	}
}
```
###### \parser\Parser.java
``` java
	/**
	 * Tries to parse the specified String into ParsedInput object for various
	 * commands to work on. If the recurring todo which user is trying to add
	 * does not include any time frame or deadline, throws an
	 * InvalidRecurringException If the todo which user is trying to add has
	 * flag keywords as parameters, throws an InvalidTodoNameException.
	 * 
	 * @param input
	 *            the String read from the user.
	 * @return a ParsedInput object containing the command type,
	 *         keyword-parameter pairs and dates identified.
	 * @throws InvalidRecurringException
	 * @throws InvalidTodoNameException
	 * @throws ParsingFailureException
	 */
	public static ParsedInput parseInput(String input)
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		boolean hasLimit = false;
		boolean isRecurring = false;
		Period period = new Period();
		ArrayList<String> words = tokenize(input);
		Keywords cType = getCommandType(words);

		// if command type is error
		if (cType == Keywords.ERROR) {
			return ParsedInput.getPlaceholder();
		}

		ArrayList<Integer> dateIndexes = new ArrayList<Integer>();
		DateTime limit = new DateTime(0);
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		ParsedInput returnInput;
		ArrayList<KeyParamPair> keyParamPairs = extractParam(words);

		if (cType == Keywords.ADD) {

			// ignores thefirst pair as it is assumed to be the name of the todo
			for (int i = 1; i < keyParamPairs.size(); i++) {
				KeyParamPair currentPair = keyParamPairs.get(i);
				Keywords key = currentPair.getKeyword();

				if (InputStringKeyword.isFlag(currentPair.getKeyString())) {
					throw new InvalidTodoNameException();
				}
				// assumes that 'every _ until _' is at the end of user input
				if (key == Keywords.UNTIL) { // check if there is a recurring
												// limit parsed in
					if (isRecurring) {
						List<DateTime> parsedLimits = interpretAsDate(
								keyParamPairs, currentPair, true);

						if (!parsedLimits.isEmpty()) { // if parsing is
														// successful
							limit = parsedLimits.get(0);
							hasLimit = true;
						}
					} else {
						interpretAsName(keyParamPairs, currentPair);
					}
				} else if (key == Keywords.EVERY) {
					// tries to detect if there is a period in user input

					// tries to parse param as period
					period = interpretAsPeriod(period, keyParamPairs,
							currentPair);
					if (!period.equals(new Period())) { // if param is valid
														// period
						isRecurring = true;
						if (period.equals(new Period().withDays(1))
								&& dateTimes.isEmpty()) { // period = every day
							dateTimes
									.add(new DateTime().withTime(23, 59, 0, 0));
						}
					}
					// tries to parse param as date to extract the date
					// if (dateTimes.isEmpty()) {
					List<DateTime> parsedDate = interpretAsDate(keyParamPairs,
							currentPair, false);
					if (!parsedDate.isEmpty()) {
						if (!isRecurring) {
							period = period.withYears(1);
							isRecurring = true;
						}
						addToDateTimes(parsedDate, dateTimes, keyParamPairs,
								dateIndexes, i);
						// }
					} else if (!isRecurring) {
						interpretAsName(keyParamPairs, currentPair);
					}

				} else {
					// tries to parse param as date
					List<DateTime> parsedDates = interpretAsDate(keyParamPairs,
							currentPair, true);

					if (!parsedDates.isEmpty()) { // if parsing is successful
						addToDateTimes(parsedDates, dateTimes, keyParamPairs,
								dateIndexes, i);
					}

				}
			}
			for (int i = keyParamPairs.size() - 1; i > 0; i--) {
				keyParamPairs.remove(i);
			}

			// check parameters for recurring todos
			if (isRecurring) {
				if (!isValidRecurring(dateTimes)) {
					throw new InvalidRecurringException();
				}
			}

		} else if (cType == Keywords.EDIT) {
			// Post-process EDIT command parameter

			// ignores the first pair as it is assumed to be the name of the
			// todo
			for (int i = 1; i < keyParamPairs.size(); i++) {
				KeyParamPair currentPair = keyParamPairs.get(i);
				Keywords key = currentPair.getKeyword();

				if (InputStringKeyword.isFlag(currentPair.getKeyString())
						&& !InputStringKeyword.isRule(currentPair
								.getKeyString())) {
					throw new InvalidTodoNameException();
				}

				// assumes that 'every _ until _' is at the end of user input
				// check if there is a recurring limit parsed
				// in
				if (key == Keywords.UNTIL) {
					List<DateTime> parsedLimits = interpretAsDate(
							keyParamPairs, currentPair, true);

					if (!parsedLimits.isEmpty()) { // if parsing is
													// successful
						limit = parsedLimits.get(0);
						hasLimit = true;
						isRecurring = true;
					} else {
						interpretAsName(keyParamPairs, currentPair);
					}
				} else if (key == Keywords.EVERY) {
					// tries to detect if there is a period in user input

					// tries to parse param as period
					period = interpretAsPeriod(period, keyParamPairs,
							currentPair);
					if (!period.equals(new Period())) { // if param is valid
														// period
						isRecurring = true;
						if (period.equals(new Period().withDays(1))
								&& dateTimes.isEmpty()) { // period = every day
							dateTimes
									.add(new DateTime().withTime(23, 59, 0, 0));
						}
					}
					// tries to parse param as date to extract the date
					// if (dateTimes.isEmpty()) {
					List<DateTime> parsedDate = interpretAsDate(keyParamPairs,
							currentPair, false);
					if (!parsedDate.isEmpty()) {
						if (!isRecurring) {
							period = period.withYears(1);
							isRecurring = true;
						}
						addToDateTimes(parsedDate, dateTimes, keyParamPairs,
								dateIndexes, i);
					} else if (!isRecurring) {
						interpretAsName(keyParamPairs, currentPair);
					}

				} else if (key == Keywords.RULE) {
					// appends the param for rule as the name
					interpretAsName(keyParamPairs, currentPair);
					keyParamPairs.get(i).setParam(EMPTY_STRING);
				} else {
					// tries to parse param as date
					List<DateTime> parsedDates = interpretAsDate(keyParamPairs,
							currentPair, true);

					if (!parsedDates.isEmpty()) { // if parsing is successful
						addToDateTimes(parsedDates, dateTimes, keyParamPairs,
								dateIndexes, i);
					}

				}
			}
			int toIndex;
			for (int i = 0; i < keyParamPairs.size(); i++) {
				KeyParamPair currentPair = keyParamPairs.get(i);

				String currentParam = currentPair.getParam();
				if ((toIndex = currentParam.indexOf(STRING_TO)) != -1) {
					String toString = currentParam.substring(toIndex,
							currentParam.length());
					if (toString.length() != 2) {
						String afterTo = toString.substring(3,
								toString.length());
						currentPair.setParam(currentParam.substring(0,
								toIndex - 1));
						keyParamPairs.add(new KeyParamPair(Keywords.TO,
								STRING_TO, afterTo));
						if (i == 0) {
							List<DateTime> parsedDates = interpretAsDate(
									keyParamPairs,
									keyParamPairs.get(keyParamPairs.size() - 1),
									true);
							dateTimes.addAll(parsedDates);
						}
					}
				}

			}
		} else if (cType == Keywords.SEARCH) {
			// Post-process SEARCH command parameters
			for (KeyParamPair keyParamPair : keyParamPairs) {
				Keywords key = keyParamPair.getKeyword();
				if (!(key == Keywords.NAME || key == Keywords.SEARCH)) {
					if (key == Keywords.YEAR) {
						keyParamPair.setParam(STRING_MARCH.concat(keyParamPair
								.getParam()));
					}
					List<DateTime> parsedDates = interpretAsDate(keyParamPairs,
							keyParamPair, false);
					if (!parsedDates.isEmpty()) {
						dateTimes.add(parsedDates.get(0));
					}
				}
			}
		}

		returnInput = new ParsedInput(cType, keyParamPairs, dateTimes, period,
				isRecurring, hasLimit, limit);
		return returnInput;
	}

	private static void interpretAsName(ArrayList<KeyParamPair> keyParamPairs,
			KeyParamPair currentPair) {
		String newName;
		int currentIndex = keyParamPairs.indexOf(currentPair);

		newName = appendParameters(keyParamPairs, 0, currentIndex);

		keyParamPairs.get(0).setParam(newName);
	}

	/**
	 * This method tries to parse parameters at given index as a period. If
	 * parsing is unsuccessful, method appends the parameters along with its
	 * keyword at the back of the parameters at the nameIndex
	 * 
	 * @param period
	 * @param keyParamPairs
	 * @param currentPair
	 * @return period stated in parameters if parsing is successful, original
	 *         period in parameters if unsuccessful.
	 * 
	 */
	private static Period interpretAsPeriod(Period period,
			ArrayList<KeyParamPair> keyParamPairs, KeyParamPair currentPair) {
		try {
			// tries to parse as period
			period = retrieveRecurringPeriod(currentPair.getParam()
					.toLowerCase());

		} catch (InvalidPeriodException e) { // no valid period

		}
		return period;
	}
	
```
###### \storage\Memory.java
``` java
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
```
###### \storage\StorageHandler.java
``` java
	/**
	 * Builder inner class for creating instances of StorageHandler with
	 * the option of setting the filePath variable.
	 */
	public static class Builder{
		 String fileDirectory;
		 String filePath;
		
		/**
		 * Set directory path with the given string.
		 * 
		 * @param fileDirectory
		 * @return Builder
		 */
		public Builder setDirectoryPath(String fileDirectory){
			this.fileDirectory = fileDirectory;
			return this;
		}
		
		/**
		 * Set file path with the directory path and file name.
		 * 
		 * @return Builder
		 */
		public Builder setFilePath(){
			this.filePath = fileDirectory + "/" + FILE_NAME;
			return this;
		}
		
		/**
		 * Overloaded method: 
		 * Set file path with the absolute path of the given file.
		 * 
		 * @param File 
		 * @return Builder
		 */
		public Builder setFilePath(File file){
			this.filePath = file.getAbsolutePath();
			return this;
		}
		
		/**
		 * Returns a StorageHandler instance.
		 * 
		 * @return StorageHandler
		 */
		public StorageHandler build(){
			return new StorageHandler(this);
		}
	}
		
	/**
	 * Constructor for StorageHandler, which takes in a Builder object
	 * to initialise variables. 
	 * 
	 * @param builder 
	 */
	private StorageHandler(Builder builder) {
	    filePath = builder.filePath;
		storageFile = new File(filePath);
		createFileIfNonExistent(storageFile);
		checkFileFormat();
	}
	
	/**
	 * Stores the memory object passed as a parameter into a file in JSON formatting
	 * 
	 * @param memoryToStore
	 */
	public void storeMemoryToFile(Memory memoryToStore) {
		initialiseWriter(storageFile);
		String jsonString = exportAsJson(memoryToStore);
		writer.println(jsonString);
		tearDownWriter();
	}
	
	/**
	 * Overloaded method:
	 * Stores the memory object that is passed as a parameter into the file that 
	 * is passed as the other parameter, in JSON formatting
	 * 
	 * @param memoryToStore
	 * @param file
	 */
	public static void storeMemoryToFile(Memory memoryToStore, File file) {
		initialiseWriter(file);
		String jsonString = exportAsJson(memoryToStore);
		writer.println(jsonString);
		tearDownWriter();
	}
	
	/**
	 * Retrieves a Memory object from the JSON file.
	 * 
	 * @return Memory
	 */
	public Memory retrieveMemoryFromFile() throws JsonSyntaxException{
		initialiseReader(storageFile);
		StringBuilder builder = new StringBuilder();

		while (reader.hasNextLine()) {
			builder.append(reader.nextLine() + "\n");
		}
		String jsonString = builder.toString();
		tearDownReader();
		Memory retrievedMemory = null;
		retrievedMemory = importFromJson(jsonString);

		return retrievedMemory;
	}
	
	/**
	 * 
	 * Checks that storageFile.json is readable. If it is not, 
	 * the user will be asked to replace the file with a new blank
	 * file, or to exit the program.
	 * 
	 */
	private void checkFileFormat(){
		if(!StorageUtils.isFileInJsonFormat(storageFile)){
			String command;
			Scanner scn = ClockWork.scn;
			do{
				System.out.println(MESSAGE_CORRUPT_FILE);
				command = scn.nextLine().toUpperCase().trim();
				if(command.equals("R")){
					storageFile.delete();
					createFileIfNonExistent(storageFile);
				}
				else if(command.equals("E")){
					System.exit(0);
				}
				else{
					System.out.println("Incorrect command given.");
				}
			}while(!command.equals("R")&&!command.equals("E"));
		}
	}
	/**
	 * Initialise the file reader.
	 * 
	 */
	private static void initialiseReader(File file) {
		try {
			reader = new Scanner(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialise the file writer.
	 * 
	 */
	private static void initialiseWriter(File file) {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					file, false)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tear down the file reader.
	 * 
	 */
	private static void tearDownReader() {
		reader.close();
	}
	/**
	 * Tear down the file writer.
	 * 
	 */
	private static void tearDownWriter() {
		writer.close();
	}
	
	/**
	 * Create file with the specified file path if it does not exist.
	 * 
	 */
	public static void createFileIfNonExistent(File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
				//write a null Memory in JSON format to file
                storeMemoryToFile(new Memory(), file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes testStorageFile for tearing-down in system tests.
	 * 
	 */
	public static void deleteStorageFileIfExists(String fileDirectory){
		String filePath = fileDirectory + "/" + FILE_NAME;
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}

	/**
	 * Method to export the current memory into a JSON String for external
	 * storage in a file
	 * 
	 * @return String json
	 */
	public static String exportAsJson(Memory mem) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalDate.class,
				new LocalDateTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class,
				new LocalTimeTypeConverter());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String jsonString = gson.toJson(mem);
		return jsonString;
	}

	/**
	 * Method to parse a json String representing an instance of memory into an
	 * instance of Memory class
	 * 
	 * @param jsonString JSON representation of an instance of memory as String
	 * @return an instance of Memory class
	 */
	public static Memory importFromJson(String jsonString) throws JsonSyntaxException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalDate.class,
				new LocalDateTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class,
				new LocalTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(DurationFieldType.class, new DurationFieldTypeDeserialiser());
		Gson gson = gsonBuilder.create();
		
		return gson.fromJson(jsonString, Memory.class);
	}

    
	/**
	 * Converter to serialize and deserialize between org.joda.time.DateTime and
	 * com.google.gson.JsonElement
	 * 
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 * 
	 *
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
	 * Converter to serialize and deserialize between org.joda.time.DateTime and
	 * com.google.gson.JsonElement
	 *
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 */

	private static class LocalDateTypeConverter implements
			JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
		@Override
		public JsonElement serialize(LocalDate src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public LocalDate deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return new LocalDate(json.getAsJsonPrimitive().getAsString());
		}

	}

	/**
	 * Converter class to serialize and deserialize between
	 * org.joda.time.LocalTime and com.google.gson.JsonElement
	 * 
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 *
	 */
	private static class LocalTimeTypeConverter implements
			JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
		@Override
		public JsonElement serialize(LocalTime src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public LocalTime deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return new LocalTime(json.getAsJsonPrimitive().getAsString());
		}

	}
	
	/**
	 * Deserialiser for JodaTime's DurationFieldType for proper 
	 * Json deserialisation
	 */
	private static class DurationFieldTypeDeserialiser implements JsonDeserializer<DurationFieldType> {

		  @Override
		  public DurationFieldType deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
		      throws JsonParseException {
		    final JsonObject jsonObject = json.getAsJsonObject();

		    final String iName = jsonObject.get("iName").getAsString();
		    
		    switch(iName){
		    
		    case "years":
		    return DurationFieldType.years();
		    
		    case "months":
		    return DurationFieldType.months();
		    
		    case "weeks":
		    return DurationFieldType.weeks();
		    
		    case "days":
		    return DurationFieldType.days();
		    
		    case "hours":
		    return DurationFieldType.hours();
		    
		    case "minutes":
		    return DurationFieldType.minutes();
		    
		    case "seconds":
		    return DurationFieldType.seconds();
		    
		    case "millis":
		    return DurationFieldType.millis();
		    
		    // Should not be reached
		    default:
		    throw new JsonParseException("No suitable iName found.");
		    }
		    
		  }
		}

}
```
###### \testcases\AddCommandTest.java
``` java
	public enum TYPE {
		ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;
	}

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
			.forPattern("EEE dd MMM yyyy");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");

	private static String formatTime(DateTime time) {
		String timeString = TimeFormatter.print(time);
		return timeString;
	}

	private static String formatDate(DateTime time) {
		String dateString = DateFormatter.print(time);

		return dateString;
	}

	@Before
	public void setUp() {
		String fileDirectory = ClockWork.getStorageFileDirFromSettings();
		logic = new ClockWork(fileDirectory);
		logic.reloadMemory();

	}

	@After
	public void tearDown() {
		logic.deleteStorageFile();
	}

	@Test
	public void testInsufficientParams() {
		Signal insufficientParamSignal;
		String command;
		// Mock Signal object
		insufficientParamSignal = new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		command = "add";
		try {
			// Test for equivalence in Signal object
			assertEquals(insufficientParamSignal, logic.handleInput(command));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		command = "add by 4pm";
		// Mock Signal object
		insufficientParamSignal = new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		try {
			// Test for equivalence in Signal object
			assertEquals(insufficientParamSignal, logic.handleInput(command));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		command = "add from 1am to 2am";
		// Mock Signal object
		insufficientParamSignal = new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		try {
			// Test for equivalence in Signal object
			assertEquals(insufficientParamSignal, logic.handleInput(command));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
	}

	/*
	 * Tests adding of floating tasks
	 */
	@Test
	public void testFloatingTask() {
		String floatingTaskCommand;
		String floatingTaskString;
		Signal addSuccess;

		/*
		 * Test for a single-worded, lower-case floating task
		 */
		floatingTaskCommand = "add running";
		floatingTaskString = "Floating task \"running\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				floatingTaskString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, mixed-case floating task
		 */
		floatingTaskCommand = "add Running";
		floatingTaskString = "Floating task \"Running\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				floatingTaskString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case floating task
		 */
		floatingTaskCommand = "add Running for StandChart marathon";
		floatingTaskString = "Floating task \"Running for StandChart marathon\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				floatingTaskString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case floating task
		 */
		floatingTaskCommand = "add Running for 42km StandChart marathon";
		floatingTaskString = "Floating task \"Running for 42km StandChart marathon\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				floatingTaskString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

	}

	/*
	 * Test adding of deadlines
	 */
	@Test
	public void testDeadline() {
		String deadlineCommand;
		String deadlineString;
		Signal addSuccess;
		DateTime changedTime;
		String formattedTime;
		String formattedDate;
		final DateTime baseTime = new DateTime();
		/*
		 * Absolute deadlines
		 */

		/*
		 * Test for a single-worded, lower-case title, with absolute date and
		 * time, long-format
		 */
		deadlineCommand = "add interview by 0800 on 13 Apr 2017";
		deadlineString = "Deadline \"interview\" by Thu 13 Apr 2017 at 08:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, absolute date and time,
		 * short-format with upper-case
		 */
		deadlineCommand = "add interview by 8am on 15 Mar 2017";
		deadlineString = "Deadline \"interview\" by Wed 15 Mar 2017 at 08:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (day),
		 * long-format
		 */
		changedTime = baseTime.plusDays(2);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add interview in two days";
		deadlineString = "Deadline \"interview\" by " + formattedDate
				+ " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (day),
		 * short-format
		 */
		deadlineCommand = "add interview in 2 days";
		deadlineString = "Deadline \"interview\" by " + formattedDate
				+ " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		/*
		 * Test for a multiple-worded, mixed-case title, absolute date and time,
		 * long-format
		 */
		deadlineCommand = "add interview with Google by 10am on 17 June 2017";
		deadlineString = "Deadline \"interview with Google\" by Sat 17 Jun 2017 at 10:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case title, absolute date and time,
		 * long-format
		 */
		deadlineCommand = "add hand in CS2103T developers guide by 1 April 2017 at 6pm";
		deadlineString = "Deadline \"hand in CS2103T developers guide\" by Sat 01 Apr 2017 at 18:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Relative deadlines
		 */

		/*
		 * Test for a single-worded, lower-case title, relative time (months),
		 * long-format in lower-case
		 */
		changedTime = baseTime.plusMonths(3);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add NOC in three months";
		deadlineString = "Deadline \"NOC\" by " + formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (months),
		 * short-format in lower-case
		 */
		deadlineCommand = "add NOC in 3 months";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (months),
		 * short-format in lower-case
		 */
		deadlineCommand = "add NOC in 3 month";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (months),
		 * short-format in mixed-case
		 */
		deadlineCommand = "add NOC in 3 Months";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case title, relative time (months),
		 * short-format in mixed-case
		 */
		changedTime = baseTime.plusMonths(3);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add NOC in Silicon Valley in 3 months";
		deadlineString = "Deadline \"NOC in Silicon Valley\" by "
				+ formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (hours),
		 * long-format in lower-case
		 */
		changedTime = baseTime.plusHours(2);
		formattedDate = formatDate(changedTime);
		formattedTime = formatTime(changedTime);
		deadlineCommand = "add exam in two hours";
		deadlineString = "Deadline \"exam\" by " + formattedDate + " at "
				+ formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (hours),
		 * long-format in mixed-case
		 */
		deadlineCommand = "add exam in Two Hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (hours),
		 * short-format in lower-case
		 */
		deadlineCommand = "add exam in 2 hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (hours),
		 * short-format in lower-case
		 */
		deadlineCommand = "add exam in 2 hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (hours),
		 * short-format in mixed-case
		 */
		deadlineCommand = "add exam in 2 Hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (hours),
		 * short-format in mixed-case
		 */
		deadlineCommand = "add exam in 2 Hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case title, relative time (hours),
		 * long-format in lower-case
		 */
		changedTime = baseTime.plusHours(6);
		formattedDate = formatDate(changedTime);
		formattedTime = formatTime(changedTime);
		deadlineCommand = "add CS2103T exam in SR1 in six hours";
		deadlineString = "Deadline \"CS2103T exam in SR1\" by " + formattedDate
				+ " at " + formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case title, relative time (hours),
		 * long-format in mixed-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in Six Hours ";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case title, relative time (hours),
		 * short-format in lower-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, lower-case title, relative time (hours),
		 * short-format in lower-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, lower-case title, relative time (hours),
		 * short-format in mixed-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 Hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, lower-case title, relative time (hours),
		 * short-format in mixed-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 Hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes),
		 * long-format in lower-case
		 */
		changedTime = baseTime.plusMinutes(30);
		formattedDate = formatDate(changedTime);
		formattedTime = formatTime(changedTime);
		deadlineCommand = "add lunch in thirty minutes";
		deadlineString = "Deadline \"lunch\" by " + formattedDate + " at "
				+ formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes),
		 * long-format in mixed-case
		 */
		deadlineCommand = "add lunch in Thirty Minutes";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes),
		 * short-format in lower-case
		 */

		deadlineCommand = "add lunch in 30 min";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes),
		 * short-format in lower-case
		 */

		deadlineCommand = "add lunch in 30 mins";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes),
		 * short-format in mixed-case
		 */

		deadlineCommand = "add lunch in 30 Min";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes),
		 * short-format in mixed-case
		 */

		deadlineCommand = "add lunch in 30 Mins";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case title, relative time
		 * (minutes), long-format in lower-case
		 */
		deadlineCommand = "add lunch at McDonalds in 30 min";
		deadlineString = "Deadline \"lunch at McDonalds\" by " + formattedDate
				+ " at " + formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				deadlineString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

	}
	
```
###### \testcases\InputStringKeywordTest.java
``` java
	@Test
	public void testIsFlag() {
		String s = "-y";
		String s1 = "nonflag";
		assertEquals(true, InputStringKeyword.isFlag(s));
		assertEquals(false, InputStringKeyword.isFlag(s1));
	}

	@Test
	public void testIsCommand() {
		String s = "add";
		String s1 = "noncommand";
		assertEquals(true, InputStringKeyword.isCommand(s));
		assertEquals(false, InputStringKeyword.isCommand(s1));
	}

	@Test
	public void testIsKeyword() {
		String s = "add";
		String s1 = "nonkeyword";
		assertEquals(true, InputStringKeyword.isKeyword(s));
		assertEquals(false, InputStringKeyword.isKeyword(s1));
	}

	@Test
	public void testGetFlag() {
		String s = "-d";
		String s2 = "nonflag";
		assertEquals(Keywords.DAY, InputStringKeyword.getFlag(s));
		assertEquals(Keywords.ERROR, InputStringKeyword.getFlag(s2));
	}

	@Test
	public void testGetCommand() {
		String s = "delete";
		String s1 = "noncommand";
		assertEquals(Keywords.DELETE, InputStringKeyword.getCommand(s));
		assertEquals(Keywords.ERROR, InputStringKeyword.getCommand(s1));
	}

	@Test
	public void testGetKeyword() {
		String s = "on";
		String s1 = "nonkeyword";
		assertEquals(Keywords.ON, InputStringKeyword.getKeyword(s));
		assertEquals(Keywords.ERROR, InputStringKeyword.getKeyword(s1));
	}
}
```
###### \testcases\MarkCommandTest.java
``` java
	@Before
	public void setUp(){
		String fileDirectory = ClockWork.getStorageFileDirFromSettings();
		logic = new ClockWork(fileDirectory);
		logic.reloadMemory();
			try{
				logic.handleInput(COMMAND_1);
				logic.handleInput(COMMAND_2);
				logic.handleInput(COMMAND_3);
			} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
			}
		}
	
	@After
	public void tearDown(){
		logic.deleteStorageFile();
	}
	
	@Test
	public void testMark(){
		String markCommand;
		String markString;
		Signal markSuccess;
		
		/*
		 * Test marking of Todo with index 0
		 */
		markCommand = "mark 0";
		markString = "Floating task \"floatingTask\"";
		markSuccess = new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, markString), true);
		try{
			assertEquals(markSuccess, logic.handleInput(markCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
		}
		
		/*
		 * Test marking of Todo with index 1
		 */
		markCommand = "mark 1";
		markString = "Deadline \"deadline\" by Sun 05 Jun 2016 at 15:00";
		markSuccess = new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, markString), true);
		try{
			assertEquals(markSuccess, logic.handleInput(markCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
		}
		
		/*
		 * Test marking of Todo with index 2
		 */
		markCommand = "mark 2";
		markString = "Event \"event\" from Thu 07 Jul 2016 at 16:00 to Thu 07 Jul 2016 at 17:00";
		markSuccess = new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, markString), true);
		try{
			assertEquals(markSuccess, logic.handleInput(markCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
		}
	}
}
```
###### \testcases\ParserTest.java
``` java
	@Test
	public void testAddFloating() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {

		// floating task without any other keywords
		String add1 = "add test 1";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	public void testAddFloatingWithKeyword() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		// floating task with 'one keyword + non-datetime'
		String add2 = "add study for test on algorithms";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "study for test on algorithms"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));

		assertEquals(parsed2, Parser.parseInput(add2));
	}

	@Test
	public void testAddDeadlineWithDateOnly() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task with 'KEYWORD + <datetime>'
		String add0 = "add test 0 by Friday";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddDeadlineWithTime() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday 3pm
		List<Date> dates0 = parser.parse("Friday 3pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
		}

		// deadline task with 'KEYWORD + <datetime>'
		String add0 = "add test 0 by Friday 3pm";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddDeadlineWithNondateAndDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("at 3pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			// dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}
		// deadline task 'KEYWORD <invalid datetime> + KEYWORD <datetime>'
		String add5 = "add test 5 in sr1 at 3pm";
		ParsedInput parsed5 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 5 in sr1"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed5, Parser.parseInput(add5));
	}

	@Test
	public void testAddDeadlineWithDateAndNondate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + KEYWORD <invalid datetime>'
		String add6 = "add test 6 by Friday at Computing";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 at Computing"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithTimeAndDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 2359 on 15 March
		List<Date> dates0 = parser.parse("2359 on 15 March").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + KEYWORD <invalid datetime>'
		String add6 = "add test 6 by 2359 on 15 March";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithNonPeriod()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + EVERY <invalid period>'
		String add6 = "add test 6 by Friday every body";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 every body"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithLimit() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + UNTIL <datetime>'
		String add6 = "add test 6 by Friday until 1 June";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 until 1 June"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithNonPeriodAndLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + EVERY <invalid period> + UNTIL
		// <datetime>'
		String add6 = "add test 6 by Friday every one until 1 June";
		ParsedInput parsed6 = new ParsedInput(
				Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 every one until 1 June"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	public void testAddDeadlineWithNonPeriodAndNonLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + EVERY <invalid period> + UNTIL
		// <invalid datetime>'
		String add6 = "add test 6 by Friday every one until we die";
		ParsedInput parsed6 = new ParsedInput(
				Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 every one until we die"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddEventWithTwoTimes() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm
		List<Date> dates0 = parser.parse("3pm to 4pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
		}

		// event task default 'one keyword + 2 datetime'
		String add0 = "add test 0 from 3pm to 4pm";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddEventWithDateAndTwoTimes()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates1 = parser.parse("3pm to 4pm on Sunday").get(0)
				.getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
		}
		// event task default 'keyword + day + keyword + 2 datetime'
		String add1 = "add test 1 on Sunday from 3pm to 4pm";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))), dateTimes1,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	@Test
	public void testAddEventWithTwoTimesAndDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates1 = parser.parse("3pm to 4pm on Sunday").get(0)
				.getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
		}
		// event task default '+ keyword + 2 datetime + keyword + day'
		String add1 = "add test 1 from 3pm to 4pm on Sunday";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))), dateTimes1,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	@Test
	public void testAddEventWithTwoDateAndTime()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates2 = parser.parse("3 March at 10am to 3 March at 12pm")
				.get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
		}

		String add2 = "add CIP event from 3 March at 10am to 3 March at 12pm";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "CIP event"))), dateTimes2,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed2, Parser.parseInput(add2));
	}

	@Test
	public void testAddEventWithTwoDates() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {

		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates2 = parser.parse("Sunday to Monday").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withTime(23, 59, 0, 0));
		}

		String add0 = "add test 0 from Sunday to Monday";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes2,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testStringProcessing() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {
		String add0 = "       add";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", ""))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));

		String add1 = "add         ";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", ""))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));

		String add2 = "add         something";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "something"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));

		assertEquals(parsed2, Parser.parseInput(add2));

	}

	@Test
	public void testAddRecurringDeadlineWithDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'KEYWORD <valid date> + EVERY <valid period>
		String add0 = "add test 0 on Friday every week";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));

	}

	@Test
	public void testAddRecurringDeadlineWithDayOfWeek()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'EVERY <valid day of week>'
		String add6 = "add test 6 every Friday";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddRecurringDeadlineWithTimeEveryDayOfWeek()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());
		// date: Friday
		List<Date> dates0 = parser.parse("3pm on Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
		}

		// recurring deadline task 'EVERY <valid day of week>'
		String add6 = "add test 6 at 3pm every Friday";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddRecurringDeadlineEveryDay()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("today").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'EVERY day'
		String add6 = "add test 6 every day";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period().withDays(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}
	
```
###### \testcases\RecurringTodoRuleTest.java
``` java
	@Test
    public void testUpdateTodoList() throws InvalidDateException {
        Period periodWeek = new Period().withWeeks(1);
        Period periodMonth = new Period().withMonths(1);
        List<DateTime> pastArrayList;
        DateTime past;

        // Test the case where the limit is in the past and is before the
        // initial date, the recurrence does not happen, only the initial todo
        // is added
        pastArrayList = Parser.parseDates("4 March 4pm");
        past = pastArrayList.get(0);
        DateTime pastLimitBefore = past.minus(2);
        RecurringTodoRule ruleBefore = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, pastLimitBefore);
        assertEquals(1, ruleBefore.updateTodoList(new Memory()));

        // Test the case where the limit is in the past and is immediately after
        // the initial date, the todo does not recur no new todos should be
        // added except the initial one
        pastArrayList = Parser.parseDates("4 March 4pm");
        past = pastArrayList.get(0);
        DateTime pastLimitImmediate = past.plus(2);
        RecurringTodoRule ruleImmediate = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, pastLimitImmediate);
        assertEquals(1, ruleImmediate.updateTodoList(new Memory()));
        
        // Test the case where the limit is in the past and is after the initial
        // date, the rule recurs once, 2 new todos should be added
        pastArrayList = Parser.parseDates("4 March 4pm");
        past = pastArrayList.get(0);
        DateTime pastLimitOneWeek = past.plus(periodWeek).plus(2);
        RecurringTodoRule ruleOneWeek = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, pastLimitOneWeek);
        assertEquals(2, ruleOneWeek.updateTodoList(new Memory()));

        // Test the case where the limit is in distant future, initial todo is
        // in near past, add 2 new todos until the next occurrence in the future
        past = new DateTime().minus(2);
        pastArrayList = new ArrayList<DateTime>();
        pastArrayList.add(past);
        DateTime futureLimitOneMonth = past.plus(periodMonth).plus(2);
        RecurringTodoRule ruleFuture = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, futureLimitOneMonth);
        assertEquals(2, ruleFuture.updateTodoList(new Memory()));

        // Test the case where the limit is in distant future, initial todo is
        // in distance past, add 4 new todos until the next occurrence in the
        // future
        past = new DateTime().minus(periodWeek).minus(periodWeek).minus(2);
        pastArrayList = new ArrayList<DateTime>();
        pastArrayList.add(past);
        RecurringTodoRule ruleFuture2 = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, futureLimitOneMonth);
        assertEquals(4, ruleFuture2.updateTodoList(new Memory()));

    }

}
```