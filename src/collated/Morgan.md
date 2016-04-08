# Morgan
###### \logic\ClashDetector.java
``` java
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
		switch(attemptedTodo.type) {
			case DEADLINE:
				todoClashExists = isDeadlineClash();
				if(todoClashExists) {
					ArrListForGUI.add("WARNING : "+String.format(Signal.CLASH_DEADLINE_DOES_EXIST, 
							attemptedTodo.endTime));
				}
				break;
			case EVENT:
				todoClashExists = isEventClash();
				if(todoClashExists) {
					ArrListForGUI.add("WARNING : "+String.format(Signal.CLASH_EVENT_DOES_EXIST,
							 attemptedTodo.endTime));
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
		String userResponse = "yes";
		if(userResponse.equals("yes") || userResponse.equals("y")) {
			ArrListForGUI.add(String.format(Signal.CLASH_USER_OVERRIDE));
			return false;
		}
		else {
			ArrListForGUI.add(String.format(Signal.CLASH_USER_VOID_TASK));
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
```
###### \logic\EditCommand.java
``` java
	/**
	 * Creates an EditCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public EditCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Processes a ParsedInput object containing the edit command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * Reverts the Todo to its original state if the changes are invalid.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		try {
			int id;
			boolean containsNewName = false;
			boolean hasRuleFlag = false;
			String title = new String(); // Stub initialization
			
			// Check if first param has any text appended to it intended as Todo name
			String[] firstKeywordParams = keyParamPairs.get(0).getParam().trim().split("\\s", 2);
			if (firstKeywordParams.length > 1) {
				// Try to parse first sub-param as int. If fail send invalidParams Signal.
				id = Integer.parseInt(firstKeywordParams[0].trim());
				title = firstKeywordParams[1];
				containsNewName = true;
			} else {
				// Check if input contains only 1 keyword (keyParamPairs.size() == 1)
				if (input.containsOnlyCommand()) {
					return new Signal(Signal.EDIT_INVALID_PARAMS, false);
				}
				id = Integer.parseInt(keyParamPairs.get(0).getParam().trim());
			}

			// Check for presence of -r flag
			hasRuleFlag = input.containsFlag(Keywords.RULE);
			
			if (input.isRecurring() || hasRuleFlag) {
				Todo stubTodo = memory.getTodo(id).copy();
				
				// Parameter loading and validation
				DateTime startTime = stubTodo.getStartTime();
				DateTime endTime = stubTodo.getEndTime();
				ArrayList<DateTime> newDateTimes = new ArrayList<DateTime>();
                boolean startTimeEdited = false;
				
				// Date checks
				if(!dateTimes.isEmpty()) { // Keywords will always exist if dateTimes 

                    if (dateTimes.size() == 2) {
                        startTime = dateTimes.remove(0);
                        endTime = dateTimes.remove(0);
                    } else {
                        for (int i = 1; i < keyParamPairs.size(); i++) {
                            Keywords keyword = keyParamPairs.get(i)
                                    .getKeyword();

                            switch (keyword) {
                                case FROM :
                                    startTime = dateTimes.remove(0);
                                    startTimeEdited = true;
                                    break;
                                case BY :
                                case ON :
                                case AT :
                                    if (!dateTimes.isEmpty()) {
                                        // Prevent overwriting of edit by FROM
                                        // keyword
                                        if (!startTimeEdited) {
                                            startTime = null;
                                        }
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                case TO :
                                    if (!dateTimes.isEmpty()) {
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                case RULE :
                                case UNTIL :
                                case EVERY :
                                    // Ignore
                                    break;
                                default :
                                    return new Signal(
                                            Signal.EDIT_INVALID_PARAMS, false);
                            }
                        }
                    }
					
					if(startTime != null && endTime != null) {
						if(startTime.isAfter(endTime)) {
							return new Signal(Signal.EDIT_END_BEFORE_START, false);
						}
						newDateTimes.add(startTime);
						newDateTimes.add(endTime);
					} else if(startTime != null) {
						newDateTimes.add(startTime);
					} else if(endTime != null) {
						newDateTimes.add(endTime);
					}
				}
				
				// Limit checks
				if(input.hasLimit()) {
					if(input.getLimit().isBeforeNow()) {
						return new Signal(Signal.EDIT_LIMIT_BEFORE_NOW, false);
					}
				}
				
				// End parameter loading and validation
				
				// Commit edited fields
				RecurringTodoRule rule = memory.getToModifyRule(memory.getTodo(id).getRecurringId());
				RecurringTodoRule ruleOld = rule.copy();
				
				// If input contains new title
				if(containsNewName) {
					rule.setOriginalName(title);
				}
				
				// If input has a limit
				if (input.hasLimit()) {
					rule.setRecurrenceLimit(input.getLimit());
				}
				
				// If input has a period
				if (input.hasPeriod()) {
					rule.setRecurringInterval(input.getPeriod());
				}
				
				if(!newDateTimes.isEmpty()) {
					rule.setDateTimes(newDateTimes);
				}
				
				// End commit
				
				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, ruleOld, rule), true);	
			} else {
				
				// Parameter loading and validation
				Todo stubTodo = memory.getTodo(id).copy();
				DateTime startTime = stubTodo.getStartTime();
				DateTime endTime = stubTodo.getEndTime();
                boolean startTimeEdited = false;
				
				if(!dateTimes.isEmpty()) {
                    if (dateTimes.size() == 2) {
                        startTime = dateTimes.remove(0);
                        endTime = dateTimes.remove(0);
                    } else {
                        for (int i = 1; i < keyParamPairs.size(); i++) {
                            Keywords keyword = keyParamPairs.get(i)
                                    .getKeyword();

                            switch (keyword) {
                                case FROM :
                                    startTime = dateTimes.remove(0);
                                    startTimeEdited = true;
                                    break;
                                case BY :
                                case ON :
                                case AT :
                                    if (!dateTimes.isEmpty()) {
                                        // Prevent overwriting of edit by FROM
                                        // keyword
                                        if (!startTimeEdited) {
                                            startTime = null;
                                        }
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                case TO :
                                    if (!dateTimes.isEmpty()) {
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                default :
                                    return new Signal(
                                            Signal.EDIT_INVALID_PARAMS, false);
                            }
                        }
                    }

					if(startTime != null && endTime != null) {
						if(startTime.isAfter(endTime)) {
							return new Signal(Signal.EDIT_END_BEFORE_START, false);
						}
					}
				}
				
				// Commit edited fields
				Todo todo = memory.getToModifyTodo(id);
				Todo oldTodo = todo.copy();
				
				// If input contains new title
				if(containsNewName) {
					memory.updateMaps(id, title, todo.getName());
					todo.setName(title);
				}
				
				memory.updateMaps(id, startTime, todo.getStartTime());
				memory.updateMaps(id, endTime, todo.getEndTime());
				todo.setStartTime(startTime);
				todo.setEndTime(endTime);
				todo.updateType();

				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, oldTodo, todo), true);
			}
		} catch (NullTodoException e) {
			return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
			return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		} catch (NullRuleException e) {
			return new Signal(Signal.EDIT_NO_LONGER_RECURS, false);
		} catch (NotRecurringException e) {
			return new Signal(e.getMessage(), false);
        } catch (InvalidParamException e) {
            return new Signal(Signal.EDIT_INVALID_PARAMS, false);
        }
	}

}
```
###### \logic\EmailCommand.java
``` java
	/**
	 * Creates an EmailCommand object.
	 * 
	 * @param input
	 *            the ParsedInput object containing the parameters (saved user email).
	 * @param memory
	 *            the memory containing the Todos to which the changes impending
	 *            notifications can be detected.
	 */
	public EmailCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}
	
	//TODO: ACCEPT COMMANDS FOR EMAIL PREFERENCE AND PARSE TO SETTINGS
	@Override
	public Signal execute() {
		return null;
	}
	
	//TODO: PAYLOAD WILL BE VALIDATED AND SETTINGS WILL BE UPDATED
	public void saveNewEmail(String userEmail, String frequency) {
		
	}
	
	//TODO: A SEPARATE THREAD WILL RUN IN THE BACKGROUND TO ACCEPT
	//EMAIL REQUESTS AND SEND PERIODIC REMINDERS
	public void mailDaemonProgram() {
		
	}

}
```
###### \logic\Todo.java
``` java
	public Integer getRecurringId() throws NotRecurringException {
		if (recurringId == null) {
			throw new NotRecurringException(
					ExceptionMessages.NOT_RECURRING_EXCEPTION);
		}
		return recurringId;
	}

	public boolean isRecurring() {
		if (recurringId == null) {
			return false;
		}
		return true;
	}


	public void updateType() {
		if (startTime != null && endTime != null) {
			type = TYPE.EVENT;
		} else if (startTime != null && endTime == null) {
			endTime = startTime;
			startTime = null;
			type = TYPE.DEADLINE;
		} else if (startTime == null && endTime != null) {
			type = TYPE.DEADLINE;
		} else if (startTime == null && endTime == null) {
			type = TYPE.TASK;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String endDateTimeString = formatDateTime(endTime);
		String startDateTimeString = formatDateTime(startTime);

		switch (type) {
			case TASK:
				return String.format(FloatingTaskStringFormat, name);
			case DEADLINE:
				return String.format(DeadlineStringFormat, name,
						endDateTimeString);
			case EVENT:
				return String.format(EventStringFormat, name,
						startDateTimeString, endDateTimeString);
			default:
				return "";
		}
	}

	private String formatDateTime(DateTime dateTime) {
		if (dateTime == null) {
			return "";
		}
		String dateString = DateFormatter.print(dateTime);
		String timeString = TimeFormatter.print(dateTime);
		return String.format(DateTimeStringFormat, dateString, timeString);
	}

    
	/**
	 * Method to return a DateTime of the Todo for ordering them chronologically
	 * The order of preference: start time > end time > null
	 * 
	 * 
	 * @return start time for events; end time for deadlines; null for tasks.
	 */
	public DateTime getDateTime() {
		if (this.startTime != null) {
			return this.startTime;
		} else if (this.endTime != null) {
			return this.endTime;
		} else {
			return null;
		}
	}

	public boolean isEvent() {
		if (this.startTime != null && this.endTime != null) {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * Method to compare two DateTime objects at the minute resolution
	 * @return -1 if the first object is smaller (earlier), 0 if the two objects
	 *         are equal, 1 if the first object is larger(later).
	 */
	public int compareDateTime(DateTime first, DateTime second) {
		DateTimeComparator comparator = DateTimeComparator
				.getInstance(DateTimeFieldType.minuteOfDay());
		return comparator.compare(first, second);
	}

	/**
	 * Overriding the equals method. Compares the title, startTime, endTime and
	 * isDone parameters between this Todo object and the other Todo object
	 * being compared to.
	 */
	@Override
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
		final Todo other = (Todo) obj;

		// Construct a DateTimeComparator comparing DateTime objects at the
		// minute resolution.
		// If the argument passed into the compare method is null, it will be
		// treated as DateTime.now
		// Thus null checks must be done beforehand
		DateTimeComparator comparator = DateTimeComparator
				.getInstance(DateTimeFieldType.minuteOfDay());

		// Comparing startTime. If it is null in both objects, treat as equal.
		if ((this.startTime == null) && (other.startTime != null)) {
			return false;
		} else if ((this.startTime != null) && (other.startTime == null)) {
			return false;
		} else if ((this.startTime != null) && (other.startTime != null)) {
			if (comparator.compare(this.startTime, other.startTime) != 0) {
				return false;
			}
		}

		// Comparing endTime. If it is null in both objects, treat as equal.
		if ((this.endTime == null) && (other.endTime != null)) {
			return false;
		} else if ((this.endTime != null) && (other.endTime == null)) {
			return false;
		} else if ((this.endTime != null) && (other.endTime != null)) {
			if (comparator.compare(this.endTime, other.endTime) != 0) {
				return false;
			}
		}

		// Comparing title
		if (!this.name.equals(other.name)) {
			return false;
		}

		// Comparing isDone
		if (this.isDone != other.isDone) {
			return false;
		}

		return true;
	}


	/**
	 * Returns if todo is a same day event.
	 * 
	 * @return true if todo is a same day event false otherwise
	 */
	public boolean isSameDayEvent() {

		return isSameDay(getStartTime(), getEndTime());
	}

	
	/**
	 * This method handles the events that spans over a few days and break them
	 * down into smaller todos within one day for display purposes
	 * 
	 * @return todos which are within a single day. If todo is originally not a
	 *         long event, returns an ArrayList with a single Todo
	 */
	public ArrayList<Todo> breakIntoShortEvents() {
		ArrayList<Todo> shortTodos = new ArrayList<Todo>();
		DateTime currentStartTime = getStartTime();
		DateTime endTime = getEndTime();
		Todo shortTodo;
        // Prevent the case where start time is after end time, causing infinite
        // loop
        if (currentStartTime.isAfter(endTime)) {
            shortTodos.add(this);
            return shortTodos;
        }
		while (!isSameDay(currentStartTime, endTime)) {
			shortTodo = new Todo(this);
			shortTodo.setStartTime(currentStartTime);
			// Set the end time of intermediate days to 2359
			shortTodo.setEndTime(currentStartTime.withHourOfDay(23)
					.withMinuteOfHour(59));
			shortTodos.add(shortTodo);
			// Move the start time to beginning of the next day
			currentStartTime = currentStartTime.plusDays(1).withMillisOfDay(0);
		}
		// Add the last day of event
		shortTodo = new Todo(this);
		shortTodo.setStartTime(currentStartTime);
		shortTodos.add(shortTodo);
		return shortTodos;
	}

    /**
     * This method checks if the two DateTime objects are the same day
     * 
     * @param date1
     * @param date2
     * @return result of check in boolean
     */
	private boolean isSameDay(DateTime date1, DateTime date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return (date1.getDayOfYear() == date2.getDayOfYear() && date1.getYear() == date2
				.getYear());
	}
}
```
###### \parser\InputStringKeyword.java
``` java
    /**
     * Checks if the String encodes a keyword
     * 
     * @param s String to be checked
     * @return 
     */
    public static boolean isKeyword(String s) {
        return keywords.containsKey(s.toLowerCase());
    }

    /**
     * Retrieves the enum constant that represents the keyword encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the command.
     * 		enum error if string is not a keyword
     */
    public static Keywords getKeyword(String s) {
    	Keywords keyword = keywords.get(s.toLowerCase());
    	if(keyword == null) {
    		return Keywords.ERROR;
    	}
    	return keyword;
    }
    
    /**
     * Checks if the String encodes a command.
     * 
     * @param s
     * @return
     */
    public static boolean isCommand(String s) {
    	return commands.containsKey(s.toLowerCase());
    }
    
    /**
     * Retrieves the enum constant that represents the command encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the command.
     * 		enum error if string is not a command
     */
    public static Keywords getCommand(String s) {
    	Keywords command = commands.get(s.toLowerCase());
    	if(command == null) {
    		return Keywords.ERROR;
    	}
    	return command;
    }
    
    /**
     * Checks if the String encodes a flag.
     * 
     * @param s
     * @return
     */
    public static boolean isFlag(String s) {
    	return flags.containsKey(s.toLowerCase());
    }
    
    /**
     * Retrieves the enum constant that represents the flag encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the flag.
     * 		enum error if string is not a flag
     */
    public static Keywords getFlag(String s) {
    	Keywords flag = flags.get(s.toLowerCase());
    	if(flag == null) {
    		return Keywords.ERROR;
    	}
    	return flag;
    }

	/**
	 * Checks if the String encodes a rule flag.
	 * @param keyString
	 * @return
	 */
	public static boolean isRule(String keyString) {
		return keyString.toLowerCase().equals(KEY_RULE_ABV);
	}
}
```
###### \parser\ParsedInput.java
``` java
	/**
	 * Creates a ParsedInput object with the type of command, a list of
	 * KeyParamPair objects and a list of DateTime objects derived from the user
	 * input String.
	 * 
	 * @param type
	 *            KEYWORDS specifying the type of the command.
	 * @param keyParamPairs
	 *            the list of KeyParamPair objects parsed from the input based
	 *            on the command type
	 * @param dateTimes
	 *            dateTime objects for todo startDate and endDates
	 * @param period
	 *            the period for the recurring todo
	 * @param isRecurring
	 *            whether the todo is recurring
	 * @param hasLimit
	 *            whether user input has a limit identified for recurring todo
	 * @param limit
	 *            the dateTime object as limit for recurring todo
	 */
	public ParsedInput(Keywords type, ArrayList<KeyParamPair> keyParamPairs,
			List<DateTime> dateTimes, Period period, boolean isRecurring,
			boolean hasLimit, DateTime limit) {
		this.type = type;
		this.keyParamPairs = keyParamPairs;
		this.dateTimes = dateTimes;
		this.period = period;
		this.hasPeriod = (period != null && !period.equals(new Period()));
		this.isRecurring = isRecurring;
		this.limit = limit;
		this.hasLimit = hasLimit;
	}

	/**
	 * Creates a ParsedInput object to represent an invalid command.
	 */
	private ParsedInput() {
		this.type = Keywords.ERROR;
	}

	public static ParsedInput getPlaceholder() {
		return new ParsedInput();
	}

	/**
	 * Retrieves the type of command specified by the input.
	 * 
	 * @return KEYWORDS specifying the type of the command.
	 */
	public Keywords getType() {
		return type;
	}

	/**
	 * Retrieves the list of KeyParamPair objects parsed from the input if any.
	 * 
	 * @return the list of KeyParamPair objects parsed from the input.
	 */
	public ArrayList<KeyParamPair> getParamPairs() {
		return keyParamPairs;
	}

	/**
	 * Retrieves the list of DateTime objects parsed from the input if any.
	 * 
	 * @return the list of DateTime objects parsed from the input.
	 */
	public List<DateTime> getDateTimes() {
		return dateTimes;
	}

	/**
	 * Checks if only the command keyword and its parameter is present.
	 * 
	 * @return true if there only the command keyword and its parameter is
	 *         present.
	 */
	public boolean containsOnlyCommand() {
		if (keyParamPairs.size() == 1 && dateTimes.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Iterates through the keyParamPair ArrayList and checks if any parameter
	 * is an empty string.
	 * 
	 * @return boolean If there is at least one empty string parameter, return
	 *         true. Else, return false.
	 */
	public boolean containsEmptyParams() {
		for (KeyParamPair pair : keyParamPairs) {
			if (pair.getParam().equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Iterates through the keyParamPair ArrayList and checks if input contains
	 * given flagKeyword
	 * 
	 * @param flagKeyword
	 * @return if input contains given flagKeyword
	 */
	public boolean containsFlag(Keywords flagKeyword) {
		for (KeyParamPair pair : keyParamPairs) {
			if (pair.getKeyword() == flagKeyword
					&& InputStringKeyword.isFlag(pair.getKeyString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if any dates are parsed.
	 * 
	 * @return true if at least one date has been parsed.
	 */
	public boolean containDates() {
		if (dateTimes.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if parsedInput is pertaining to recurring tasks.
	 * 
	 * @return true if parsedInput is pertaining to recurring tasks.
	 */
	public boolean isRecurring() {
		return isRecurring;
	}

	/**
	 * Returns the period in object
	 * @return period of the object
	 */
	public Period getPeriod() {
		return period;
	}

	/**
	 * Checks if parsedInput has a period
	 * @return true if parsedInput has a period
	 */
	public boolean hasPeriod() {
		return hasPeriod;
	}

	/**
	 * Checks if parsedInput has a limit
	 * @return true if parsedInput has a limit
	 */
	public boolean hasLimit() {
		return hasLimit;
	}

	/**
	 * Retrieves the limit of the parsedInput object
	 * @return limit as DateTime object
	 */
	public DateTime getLimit() {
		return limit;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass().equals(this.getClass())) {
			ParsedInput other = (ParsedInput) o;

			return this.getType().equals(other.getType())
					&& this.getParamPairs().equals(other.getParamPairs())
					&& this.getDateTimes().equals(other.getDateTimes())
					&& this.getPeriod().equals(other.getPeriod())
					&& this.isRecurring() == other.isRecurring()
					&& this.hasLimit() == other.hasLimit()
					&& this.getLimit().equals(other.getLimit());
		}
		return false;
	}
}
```
###### \parser\Parser.java
``` java
	/**
	 * This method tries to parse the parameters at the given index as a date.
	 * If parsing as date is not successful and addToName is true, method
	 * interprets the parameters as part of the name. When append is false,
	 * method simply ignores the parameters.
	 * 
	 * @param keyParamPairs
	 * @param currentPair
	 * @param addToName
	 * @return list of datetimes parsed. If parsing is unsuccessful, returns
	 *         empty list.
	 */
	private static List<DateTime> interpretAsDate(
			ArrayList<KeyParamPair> keyParamPairs, KeyParamPair currentPair,
			boolean addToName) {
		List<DateTime> parsedDate = new ArrayList<DateTime>();
		try {
			parsedDate = parseDates(combineKeyPair(currentPair.getKeyString(),
					currentPair.getParam()));
		} catch (InvalidDateException e) { // no valid date given
			if (addToName) {
				interpretAsName(keyParamPairs, currentPair);
			}
		}
		return parsedDate;
	}

	private static String combineKeyPair(String keyString, String paramString) {
		StringBuilder sBuilder = new StringBuilder(keyString);
		sBuilder.append(CHAR_SPACE);
		sBuilder.append(paramString);
		return sBuilder.toString();
	}

	/**
	 * Check if given dateTimes has enough elements for todo to be a valid
	 * recurring todo
	 * 
	 * @param dateTimes
	 * @return isValidRecurring
	 */
	private static boolean isValidRecurring(List<DateTime> dateTimes) {
		return dateTimes.size() > 0;
	}

	/**
	 * Adds parsedDate to dateTimes depending on number of elements in
	 * dateTimes. If dateTimes already contains some elements, method tries to
	 * combine the parameters and re-parse them as dates to check if resulting
	 * dateTime is different
	 * 
	 * @param parsedDate
	 * @param dateTimes
	 * @param keyParamPairs
	 * @param dateIndexes
	 * @param currentIndex
	 * @throws ParsingFailureException
	 */
	private static void addToDateTimes(List<DateTime> parsedDate,
			List<DateTime> dateTimes, ArrayList<KeyParamPair> keyParamPairs,
			ArrayList<Integer> dateIndexes, int currentIndex)
			throws ParsingFailureException {

		if (dateTimes.size() > 0) {
			int appendedPairIndex = dateIndexes.get(0);
			List<DateTime> newDateTimes = new ArrayList<DateTime>();

			assert (dateTimes.size() < 3); // There should be at the most 2
											// dates only
			assert (dateIndexes.size() == 1); // There should be at the most 1
												// date param

			String newDateParam = appendParameters(keyParamPairs,
					appendedPairIndex, currentIndex);
			try {
				newDateTimes = parseDates(combineKeyPair(
						keyParamPairs.get(appendedPairIndex).getKeyString(),
						newDateParam));

			} catch (InvalidDateException e) {
				// should never enter this catch block as old parameters
				// were parse-able as dates
			}

			if (!newDateTimes.isEmpty() && newDateTimes.equals(dateTimes)) {

				// natty could not parse in the first order, try appending the
				// other way
				appendedPairIndex = currentIndex;
				currentIndex = dateIndexes.get(0);
				newDateParam = appendParameters(keyParamPairs,
						appendedPairIndex, currentIndex);
				try {
					newDateTimes = parseDates(combineKeyPair(
							keyParamPairs.get(appendedPairIndex).getKeyString(),
							newDateParam));

					// new dateTime parsed from 'param y param x' is same as
					// 'param y'
					if (newDateTimes.get(0).toLocalDate()
							.equals(parsedDate.get(0).toLocalDate())) {
						if (newDateTimes.get(0).getHourOfDay() == parsedDate
								.get(0).getHourOfDay()
								&& newDateTimes.get(0).getMinuteOfHour() == parsedDate
										.get(0).getMinuteOfHour()) {
							if (newDateTimes.size() == 2) {
								if (newDateTimes.get(1).getHourOfDay() == parsedDate
										.get(1).getHourOfDay()
										&& newDateTimes.get(1)
												.getMinuteOfHour() == parsedDate
												.get(1).getMinuteOfHour()) {
									throw new ParsingFailureException();
								}
							} else {
								throw new ParsingFailureException();
							}

						}
					}
				} catch (InvalidDateException e) {
					// should never enter this catch block as old date
					// parameters were parse-able
				}
			}

			assert (!newDateTimes.isEmpty()); // shouldn't be empty because
												// parameters should be
												// parse-able
			dateTimes.clear(); // removes all elements in dateTimes
			dateTimes.addAll(newDateTimes);
			keyParamPairs.get(appendedPairIndex).setParam(newDateParam);
			dateIndexes.remove(0);
			dateIndexes.add(appendedPairIndex);
		} else {
			dateTimes.addAll(parsedDate);
			dateIndexes.add(currentIndex);
		}
	}

	/**
	 * Appends the keyword and parameter of the second keyParamPair to the
	 * parameters of the first keyParamPair.
	 * 
	 * @param keyParamPairs
	 * @param indexOfFirstPair
	 * @param indexOfSecondPair
	 * @return appended string
	 */
	private static String appendParameters(
			ArrayList<KeyParamPair> keyParamPairs, int indexOfFirstPair,
			int indexOfSecondPair) {
		KeyParamPair firstPair = keyParamPairs.get(indexOfFirstPair);
		KeyParamPair secondPair = keyParamPairs.get(indexOfSecondPair);
		String key = secondPair.getKeyString();

		StringBuilder sBuilder = new StringBuilder(firstPair.getParam());
	
		if (sBuilder.length() != 0){
			if (!secondPair.getKeyword().equals(Keywords.RULE)) {
				sBuilder.append(CHAR_SPACE);
				sBuilder.append(key);
			}
		} else {
			sBuilder.append(key);
			
		} 
		if(!secondPair.getParam().isEmpty()) {
			sBuilder.append(CHAR_SPACE);
			sBuilder.append(secondPair.getParam());
		}
		
		return sBuilder.toString();
	}

	/**
	 * Retrieves period given string. Throws an InvalidPeriodException if
	 * parameter given is not a valid period.
	 * 
	 * @param param
	 * @return period for recurrence
	 * @throws InvalidPeriodException
	 */
	private static Period retrieveRecurringPeriod(String param)
			throws InvalidPeriodException {
		param.toLowerCase();
		Period period = new Period();
		switch (param) {
			case STRING_YEAR:
				return period.withYears(1);
			case STRING_MONTH:
				return period.withMonths(1);
			case STRING_WEEK:
				return period.withWeeks(1);
			case STRING_DAY:
				return period.withDays(1);
			case STRING_MON:
				return period.withWeeks(1);
			case STRING_TUE:
				return period.withWeeks(1);
			case STRING_WED:
				return period.withWeeks(1);
			case STRING_THU:
				return period.withWeeks(1);
			case STRING_FRI:
				return period.withWeeks(1);
			case STRING_SAT:
				return period.withWeeks(1);
			case STRING_SUN:
				return period.withWeeks(1);
			case STRING_MONDAY:
				return period.withWeeks(1);
			case STRING_TUESDAY:
				return period.withWeeks(1);
			case STRING_WEDNESDAY:
				return period.withWeeks(1);
			case STRING_THURSDAY:
				return period.withWeeks(1);
			case STRING_FRIDAY:
				return period.withWeeks(1);
			case STRING_SATURDAY:
				return period.withWeeks(1);
			case STRING_SUNDAY:
				return period.withWeeks(1);
			default:
				throw new InvalidPeriodException();
		}
	}

	/**
	 * Takes in a user input string and puts individual words into elements in a
	 * String ArrayList.
	 *
	 * @param input
	 *            the String read from the user.
	 * @return an ArrayList of words from the input String.
	 */
	private static ArrayList<String> tokenize(String input) {
		input = input.trim();
		String[] inputWords = input.split(REGEX_SPACE);
		ArrayList<String> words = new ArrayList<String>();
		for (int i = 0; i < inputWords.length; i++) {
			words.add(inputWords[i]);
		}
		return words;
	}

	/**
	 * Parses the tokenized input, wordList for keywords and their associated
	 * parameters, stores them in KeyParamPair objects and adds all KeyParamPair
	 * objects to an ArrayList which is returned. ASSUMPTION: The first word in
	 * user input is a keyword.
	 * 
	 * @param words
	 *            the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	private static ArrayList<KeyParamPair> extractParam(ArrayList<String> words) {
		String key = words.get(0);
		ArrayList<KeyParamPair> keyParamPairs = new ArrayList<KeyParamPair>();
		Keywords keyword;
		EnumSet<Keywords> keywordOccurrence = EnumSet.noneOf(Keywords.class);
		StringBuilder paramBuilder = new StringBuilder();
		for (int i = 1; i < words.size(); i++) {
			String currentParam = words.get(i);

			// wordList.get(i) is a keyword. Create a KeyParamPair with previous
			// param
			// and paramStringBuilder and add to ArrayList. Update key and
			// paramBuilder.
			// If currentParam is a keyword:
			if (InputStringKeyword.isKeyword(currentParam)) {
				keyword = InputStringKeyword.getKeyword(key);
				// Ignore and append keyword if it has occurred before
				if (!keywordOccurrence.contains(keyword)) {
					keywordOccurrence.add(keyword);
					keyParamPairs.add(new KeyParamPair(keyword, key,
							paramBuilder.toString()));
					key = currentParam;
					paramBuilder = new StringBuilder();
				} else { // wordList.get(i) is a repeated keyword; append to
							// paramString
					buildParam(paramBuilder, currentParam);

				}
			} else { // wordList.get(i) is not a keyword; append to paramString
				buildParam(paramBuilder, currentParam);
			}
		}
		// last KeyParamPair to be added to ArrayList
		keyword = InputStringKeyword.getKeyword(key);
		keyParamPairs.add(new KeyParamPair(keyword, key, paramBuilder
				.toString()));
		return keyParamPairs;
	}

	private static void buildParam(StringBuilder paramBuilder,
			String currentParam) {
		if (paramBuilder.length() != 0) {
			paramBuilder.append(CHAR_SPACE);
		}
		paramBuilder.append(currentParam);
	}

	/**
	 * This operation gets the type of command of the user input.
	 * 
	 * ASSUMPTION: the first word in user input is the command type keyword.
	 * 
	 * @param words
	 *            the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	private static Keywords getCommandType(ArrayList<String> words) {
		String typeString = words.get(0);
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed command
	 * types.
	 * 
	 * @param typeString
	 *            String specifying the type of command.
	 * @return KEYWORDS specifying the type, null if typeString does not contain
	 *         command.
	 */
	private static Keywords determineCommandType(String typeString) {
		Keywords type = Keywords.ERROR;

		if (InputStringKeyword.isCommand(typeString)) {
			type = InputStringKeyword.getCommand(typeString);
		}
		return type;
	}

	/**
	 * Parses a String with multiple dates provided to the DateParser, and
	 * returns a DateTime ArrayList.
	 * 
	 * @param dateString
	 *            String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates
	 *         processed in the string.
	 * @throws InvalidDateException
	 *             if dateString does not contain a valid date, is empty, or
	 *             null
	 * @throws InterruptedException
	 */
	public static List<DateTime> parseDates(String dateString)
			throws InvalidDateException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		DateGroup parsedDate = null;
		DateGroup dateNow = null;
		DateTime currentDateTime = null;
		try {
			parsedDate = parser.parse(dateString).get(0);
			Thread.sleep(1);
			currentDateTime = new DateTime();
			Thread.sleep(1);
			// Parse the date again to detect dateString type
			dateNow = parser.parse(dateString).get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidDateException(
					ExceptionMessages.DATE_UNDEFINED_EXCEPTION);
		} catch (InterruptedException e) {

		}

		List<Date> dates = parsedDate.getDates();
		List<Date> secondDates = dateNow.getDates();
		for (int i = 0; i < dates.size(); i++) {
			Date date = dates.get(i);
			DateTime dateTime = new DateTime(date);
			DateTime secondDateTime = new DateTime(secondDates.get(i));
			// date does not include a time
			if (dateTime.toLocalTime().isBefore(currentDateTime.toLocalTime())
					&& currentDateTime.toLocalTime().isBefore(
							secondDateTime.toLocalTime())) {
				// sets the default time to be 2359h
				dateTime = dateTime.withTime(23, 59, 0, 0);
			}
			dateTimes.add(dateTime);
		}
		return dateTimes;
	}

	/**
	 * Initializes the natty parser by test parsing a string to reduce execution
	 * time for future parsing
	 */
	public static void initialize() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());
		parser.parse(INIT_STRING);
	}
}
```
###### \storage\StorageUtils.java
``` java
		/**
		 * Processes the user-specified path.
		 * 
		 * If the path is valid, and there exists a Storage file (storageFile.json)
		 * at that user-specified path, the file will be checked if it is a valid
		 * JSON file. If it is, it will be used automatically.
		 * 
		 * If the file at the user-specified path is corrupt or invalid, the user
		 * will be asked to choose the following options, which would be executed accordingly.
		 * 1. Copy the storage file from the default location 
		 * 2. Create and use a new storage file at the user-specified path 
		 * 3. Revert back to using the storage file at the default location 
		 * 4. Exit
		 * 
		 * @param String customFileDirPath
		 * @return String Path of storageFile.json after processing
		 */
		public static String processStorageDirectory(String customFileDirPath) {
			Scanner scn = ClockWork.scn;
			// If valid: copy any existing storageFile from its current location to
			// the
			// new user-specified location. Change the settings file.
			if (isValidDirPath(customFileDirPath)) {
				// Check if there is a file at customFileDirPath and select
				// overwrite option.
				String customFilePath = customFileDirPath + "/" + STORAGE_FILE_NAME;
				File newStorageFile = new File(customFilePath);

				if (newStorageFile.exists()) {
					// newStorageFile is not in Json format
					if(!isFileInJsonFormat(newStorageFile)){
						System.out
						.println(MESSAGE_CORRUPT_FILE);
						String command;
						do{
							command = scn.next().toUpperCase().trim();
							switch(command){
							case "C":
								String storageFilePath = fileDirectory + "/"
										+ STORAGE_FILE_NAME;
								File currentStorageFile = new File(storageFilePath);
								
								if (currentStorageFile.exists()) {
									copyStorageFile(storageFilePath, customFileDirPath);
									System.out
											.println("Storage file copied to specified location: "
													+ customFileDirPath);
								}
								// No existing storageFile.json at the default location
								// Create a blank storageFile.json at the user-specified location
								else{
									newStorageFile.delete();
									try{
										newStorageFile.createNewFile();
									} catch(IOException e){
										e.printStackTrace();
									}
									StorageHandler.createFileIfNonExistent(newStorageFile);
									StorageHandler.storeMemoryToFile(new Memory(), newStorageFile);
									
								}
								// Update settings file and file directory
								modifySettingsFile(customFileDirPath);
								fileDirectory = customFileDirPath;
								System.out
										.println(String.format(MESSAGE_DIRECTORY_UPDATED, customFileDirPath));
								break;
							case "O":
								newStorageFile.delete();
								try{
									newStorageFile.createNewFile();
								} catch(IOException e){
									e.printStackTrace();
								}
								StorageHandler.createFileIfNonExistent(newStorageFile);
								StorageHandler.storeMemoryToFile(new Memory(), newStorageFile);
								
								// Update settings file and file directory
								modifySettingsFile(customFileDirPath);
								fileDirectory = customFileDirPath;
								System.out
										.println(String.format(MESSAGE_DIRECTORY_UPDATED, customFileDirPath));
								break;
							case "R":
								// Do nothing; fileDirectory is already default
								System.out
								.println(String.format(MESSAGE_DIRECTORY_DEFAULT, fileDirectory));
								break;
							case "E":
								System.exit(0);
							default:
								System.out.println(MESSAGE_INCORRECT_COMMAND);
							break;
							}
							scn.nextLine();
						}while(!command.equals("C")&&!command.equals("O")&&!command.equals("R")&&!command.equals("E"));
					}
					// newStorageFile is in Json format
					// Use the existing file at user-specified location automatically
					else{
					 modifySettingsFile(customFileDirPath);
					 fileDirectory = customFileDirPath;
					}
				}
				// newStorageFile does not exist.
				else {
					// If there exists a storageFile.json in the current directory
					// as
					// specified in the settings file, copy it to the custom
					// directory
					String storageFilePath = fileDirectory + "/"
							+ STORAGE_FILE_NAME;
					File currentStorageFile = new File(storageFilePath);
					if (currentStorageFile.exists()) {
						copyStorageFile(storageFilePath, customFileDirPath);
						System.out
								.println(String.format(MESSAGE_COPIED_TO_LOCATION, customFileDirPath));
					}
					// Update regardless of existence of storageFile.json
					modifySettingsFile(customFileDirPath);
					fileDirectory = customFileDirPath;
					System.out
							.println(String.format(MESSAGE_DIRECTORY_UPDATED, customFileDirPath));
				}

			}
			// If customFileDirPath is invalid: revert back to default directory
			else {
				System.out
						.println(String.format(MESSAGE_INVALID_DIRECTORY, fileDirectory));
			}
			return fileDirectory;
		}
		
		/**
		 * Checks if the file passed as parameter is in Json format.
		 * 
		 * @param File
		 * @return Boolean
		 */
		static Boolean isFileInJsonFormat(File file){
			try{
				Scanner reader = new Scanner(file);
				StringBuilder builder = new StringBuilder();

				while (reader.hasNextLine()) {
					builder.append(reader.nextLine() + "\n");
				}
				String jsonString = builder.toString();
				StorageHandler.importFromJson(jsonString);
				reader.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch(JsonSyntaxException e){
				return false;
			}
			return true;
		}
		/**
		 * Copies storageFile.json from the storageFilePath to customFileDirPath 
		 * 
		 * @param String storageFilePath
		 * @param String customFileDirPath
		 */
		static void copyStorageFile(String storageFilePath,
				String customFileDirPath) {
			String customFilePath = customFileDirPath + "/" + STORAGE_FILE_NAME;
			try {
				Files.copy(Paths.get(storageFilePath), Paths.get(customFilePath),
						REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Modifies the Settings file (settings.txt) by overriding the file with
		 * the path specified by customFileDirPath.
		 * 
		 * @param String customFileDirPath
		 */
		static void modifySettingsFile(String customFileDirPath) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						settingsFile, false));
				writer.write(customFileDirPath);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	
		/**
		 * Attempt to read settings.txt in the default directory.
		 * 
		 * If settings.txt does not exist, it will be created and the default
		 * directory path string will be written into it.
		 * 
		 * If settings.txt exists, the directory for saving storageFile.json will be
		 * read from the file. If the directory read is invalid, the directory of
		 * storageFile.json will be reverted to the default.
		 * 
		 */
		public static String readSettingsFile() {
			// Set default file directory
			setDefaultFileDirectory();

			// Build settings file path
			settingsFilePath = defaultFileDirectory + "/" + SETTINGS_FILE_NAME;

			// Check if settings file exists
			settingsFile = new File(settingsFilePath);
			BufferedWriter writer;
			try {
				if (!settingsFile.exists()) {
					settingsFile.createNewFile();
					// Write default storage file directory path to settings file
					writer = new BufferedWriter(new FileWriter(settingsFile));
					writer.write(defaultFileDirectory);
					fileDirectory = defaultFileDirectory;
					writer.close();
				}
				// Settings file exists. Read storage file directory path from file.
				else {
					BufferedReader reader = new BufferedReader(new FileReader(
							settingsFile));
					// Read storage directory file path
					String fileDirectoryString = reader.readLine();

					// If storage directory file path is invalid, overwrite settings
					// file
					// with default directory path and set the storage file
					// directory path to default
					if (!isValidDirPath(fileDirectoryString)) {
						writer = new BufferedWriter(new FileWriter(settingsFile,
								false));
						writer.write(defaultFileDirectory);
						writer.close();
						fileDirectory = defaultFileDirectory;
					}
					// If storage file path is valid, set it as file directory
					else {
						fileDirectory = fileDirectoryString;
					}

					reader.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileDirectory;
		}

		/**
		 * Sets the default file directory where storageFile.json is saved as the
		 * same directory the program is being run from.
		 */
		static void setDefaultFileDirectory() {
			defaultFileDirectory = new File("").getAbsolutePath();
		}

		/**
		 * Checks if the string is a valid file directory.
		 * 
		 * @param String fileDirectoryString
		 * @return Boolean if string is a valid directory, false otherwise.
		 */
		static Boolean isValidDirPath(String fileDirectoryString) {
			if (fileDirectoryString == null || fileDirectoryString == "") {
				return false;
			}

			return new File(fileDirectoryString).isDirectory();

		}

}
```
###### \testcases\SystemsTestZ.java
``` java
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
    public void testAll() throws InvalidRecurringException,
            InvalidTodoNameException, ParsingFailureException {

        logic.handleInput("add floating task");

        logic.handleInput("add CS2103 deadline on 9 March 9pm");

        logic.handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

        logic.handleInput("add new year on 1 January every year");

        logic.handleInput("add CS1010 deadline by 3 Feb at 10pm");

        logic.handleInput("add read floating books");

        logic.handleInput("add EE2024 project deadline by 7 March at 9am");

        logic.handleInput("add CG2271 project 2 on 7 Apr 10am");

        logic.handleInput("mark 0");

        logic.handleInput("mark 2");

        logic.handleInput("search read");

        logic.handleInput("add go to NUS hackers on 3 apr every friday until 4 apr");

        logic.handleInput("add learn something every sunday");

        logic.handleInput("search learn");

        logic.handleInput("undo");

        logic.handleInput("undo");

        logic.handleInput("redo");

        logic.handleInput("mark 1");

        logic.handleInput("undo");

        logic.handleInput("mark 2");

        logic.handleInput("search new");

        logic.handleInput("search NUS");

        logic.handleInput("edit 3 last new year");

        logic.handleInput("search new");

        logic.handleInput("delete 6");

        logic.handleInput("display");

        todos = ClockWork.memory.getAllTodos();

        String expected = "Showing pending todos:\n"
                + "ID | Name                           | Time\n\n"
                + "..Fri 01 Jan 2016...\n3  | last new year                  | 23:59\n\n"
                + "..Wed 03 Feb 2016...\n5  | CS1010 deadline                | 22:00\n\n"
                + "..Mon 07 Mar 2016...\n7  | EE2024 project deadline        | 09:00\n\n"
                + "..Wed 09 Mar 2016...\n1  | CS2103 deadline                | 21:00\n\n"
                + "..Thu 03 Apr 2016...\n9  | (Recurring) go to NUS hackers  | 23:59\n\n"
                + "..Fri 07 Apr 2016...\n8  | CG2271 project 2               | 10:00\n\n"
                + "..Sun 01 Jan 2017...\n4  | (Recurring) new year           | 23:59\n";
        assertEquals(expected, DisplayCommand.getDisplayString(todos, 0));

    }

}
```
###### \testcases\TodoTest.java
``` java
	@Before
	public void setup() {
		name = "Todo One";
		deadlineTime = new DateTime(2015, 8, 26, 12, 59, 0);
		startDate = new DateTime(2015, 9, 29, 00, 00, 0);
		endDate = new DateTime(2015, 11, 10, 18, 59, 0);
		eventDateTimes = new ArrayList<DateTime>();
		eventDateTimes.add(startDate);
		eventDateTimes.add(endDate);
		deadlineDateTimes = new ArrayList<DateTime>();
		deadlineDateTimes.add(deadlineTime);
	}
	
	@Test
	public void testTask() {
		Todo task = new Todo(0, name);
		assertEquals("Start time", null, task.getStartTime());
		assertEquals("End time", null, task.getEndTime());
		assertEquals("Type ", Todo.TYPE.TASK, task.getType());
	}
	
	@Test
	public void testDeadline() {
		Todo deadline = new Todo(0, name, deadlineDateTimes);
		assertEquals("Start time", null, deadline.getStartTime());
		assertEquals("End time", deadlineTime, deadline.getEndTime());
		assertEquals("Type ", Todo.TYPE.DEADLINE, deadline.getType());
	}
	
	@Test
	public void testEvent() {
		Todo event = new Todo(0, name, eventDateTimes);
		assertEquals("Start time", startDate, event.getStartTime());
		assertEquals("End time", endDate, event.getEndTime());
		assertEquals("Type ", Todo.TYPE.EVENT, event.getType());
	}
}
```
###### \userinterface\DigitalClock.java
``` java
	public DigitalClock() {
    bindToTime();
  }

  // the digital clock updates once a second.
  private void bindToTime() {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            Calendar time = Calendar.getInstance();
            String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
            String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
            String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
            String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            LocalDate localDate = new LocalDate();
            setText("          " + hourString + ":" + minuteString + ":" + secondString + " " + ampmString + "            " + localDate.toString());
          }
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}

class StringUtilities {
  /**
   * Creates a string left padded to the specified width with the supplied padding character.
   * @param fieldWidth the length of the resultant padded string.
   * @param padChar a character to use for padding the string.
   * @param s the string to be padded.
   * @return the padded string.
   */
  public static String pad(int fieldWidth, char padChar, String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = s.length(); i < fieldWidth; i++) {
      sb.append(padChar);
    }
    sb.append(s);

    return sb.toString();
  }
}
```
###### \userinterface\LayoutCalendar.java
``` java
	public LayoutCalendar() {
		setDisplayRegions();
	}

	/** POPULATING LAYOUT */
	
	private void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}
	
	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}

   /****************CALENDAR IMPLEMENTATION **********/
	private void setCenterRegion() {
		//Creating pane for calendar and current time
		BorderPane centralPane = new BorderPane();
		
		// Create the non-interactive DatePicker object with today in focus.
		DatePicker dp = new DatePicker();
		dp.setValue(LocalDate.now());
		// Add some action (in Java 8 lambda syntax style).
		dp.setOnAction(event -> {
		    LocalDate date = dp.getValue();
		    System.out.println("Selected date: " + date);
		});		
		
		//Isolate internal component, which was previously the component that "popped up"
		DatePickerSkin skin = new DatePickerSkin(new DatePicker());
		Node calendarControl = skin.getPopupContent();
		calendarControl.setId("calendar-control");
		calendarControl.setStyle("-fx-padding: 1;  -fx-background-insets: 0, 100;  "
				+ "-fx-background-radius: 0, 0; -fx-background-color: rgba(0, 100, 100, 0.1);");
		
		//Preparation of current time  
		this.currentTime.setFont(Font.font("Cambria", 50));
		this.currentTime.setTextFill(Color.WHITE);
		this.currentTime.setTextAlignment(TextAlignment.CENTER);
		
		
		//Affix isolated node along with the current time
		centralPane.setCenter(calendarControl);
		centralPane.setBottom(this.currentTime);
		centralPane.setStyle("-fx-background-color: #182733;");
		centralPane.setPadding(new Insets(25));

		//Exploit this component as our stand-alone calendar widget
		this.setCenter(centralPane);
	}
	/************* END IMPLEMENTATION ***************/
	
	private void setBottomRegion() {
		TextField textField = implementTextField();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					Controller.processEnter("DISPLAY");
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		this.setBottom(textField);
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);
		return textField;
	}
}
```
