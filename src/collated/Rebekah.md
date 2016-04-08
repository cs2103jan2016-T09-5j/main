# Rebekah
###### \logic\MarkCommand.java
``` java
	/**
	 * Creates a MarkCommand object.
	 * 
	 * @param input
	 *            the ParsedInput object containing the parameters.
	 * @param memory
	 *            the memory containing the Todos to which the changes should be
	 *            committed.
	 */
	public MarkCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Retrieves the Todo object specified by index in ParsedInput from Memory
	 * and marks it as done.
	 * 
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute() {
		// Ensure that there is only one KeyParamPair in inputList
		if (!input.containsOnlyCommand()) {
			return new Signal(Signal.MARK_INVALID_PARAMS, false);
		}

		if (input.containsEmptyParams()) {
			return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		try {
			int index = Integer.parseInt(keyParamPairs.get(0).getParam());
			Todo todoToMark = memory.getToModifyTodo(index);
			todoToMark.setDone(true);
			memory.saveToFile();
			return new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT,
					todoToMark), true);
		} catch (NullTodoException e) {
			return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
			return new Signal(String.format(Signal.MARK_INVALID_PARAMS), false);
		}
	}
}
```
###### \logic\RecurringTodoRule.java
``` java
    /**
     * Constructor for the RecurringTodoRule without specifying limit
     * 
     * @param id
     * @param recurringId
     * @param name
     * @param dateTimes
     * @param period
     */
    public RecurringTodoRule(int recurringId, String name,
            List<DateTime> dateTimes, Period period) {
        super();
        this.originalName = name;
        this.name = RECURRING_TODO_PREIX + name;
        this.dateTimes = dateTimes;
        this.recurringInterval = period;
        this.recurringId = recurringId;
        this.recurrenceLimit = new DateTime()
                .plus(DEFAULT_RECURRENCE_LIMIT_PERIOD);
    }

    /**
     * Constructor for the RecurringTodoRule with limit
     * 
     * @param id
     * @param recurringId
     * @param name
     * @param dateTimes
     * @param period
     * @param limit
     */
    public RecurringTodoRule(int recurringId, String name,
            List<DateTime> dateTimes, Period period, DateTime limit) {
        super();
        this.originalName = name;
        this.name = RECURRING_TODO_PREIX + name;
        this.dateTimes = dateTimes;
        this.recurringInterval = period;
        this.recurringId = recurringId;
        this.recurrenceLimit = limit;
    }
    
    /**
     * Create a copy of a rule. For use in EditCommand.
     * 
     * @param rule
     */
    private RecurringTodoRule(RecurringTodoRule rule) {
    	this.originalName = rule.originalName;
        this.name = rule.name;
    	this.dateTimes = rule.dateTimes;
    	this.recurringInterval = rule.recurringInterval;
    	this.recurringId = rule.recurringId;
    	this.recurrenceLimit = rule.recurrenceLimit;
    }
    
    private RecurringTodoRule(int recurringId) {
    	this.recurringId = recurringId;
    }
    
    public RecurringTodoRule copy() {
    	return new RecurringTodoRule(this);
    }
    
    public RecurringTodoRule getPlaceholder() {
    	return new RecurringTodoRule(recurringId);
    }
    
    public boolean isPlaceholder() {
    	return recurringInterval == null;
    }

    public String getName() {
        return name;
    }

    public List<DateTime> getDateTimes() {
        return dateTimes;
    }

    public ArrayList<Todo> getRecurringTodos() {
        return recurringTodos;
    }

    public Period getRecurringInterval() {
        return recurringInterval;
    }

    public int getId() {
        return recurringId;
    }

    public DateTime getRecurrenceLimit() {
        return recurrenceLimit;
    }

    /**
     * Update the list of Todos stored in the rule
     * 
     * @return the number of new Todos created due to the update
     */
    public int updateTodoList(Memory memory) {
        int currentID;
        int newTodoCount = 0;
        if (recurringTodos.isEmpty()) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes, recurringId);
            addFirstRecurringTodo(memory, newTodo);
            newTodoCount++;
            updateDateTime();
        }

        DateTime now = new DateTime();
        DateTime nextOccurrence = now.plus(getRecurringInterval());
        // Update until next occurrence or the limit, whichever is earlier
        DateTime updateLimit = nextOccurrence;
        if (nextOccurrence.compareTo(getRecurrenceLimit()) > 0) {
            updateLimit = getRecurrenceLimit();
        }

        while (getDateTime().compareTo(updateLimit) <= 0) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes, recurringId);
            addRecurringTodo(memory, newTodo);
            newTodoCount++;
            updateDateTime();
        }

        return newTodoCount;
    }

    public void setRecurrenceLimit(DateTime recurrenceLimit)
            throws InvalidParamException {
        if (recurrenceLimit == null) {
            throw new InvalidParamException(
                    "Recurring limit of recurring rule cannot be empty");
        } else {
            this.recurrenceLimit = recurrenceLimit;
        }
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
        this.name = RECURRING_TODO_PREIX + originalName;
    }

    public void setRecurringInterval(Period recurringInterval)
            throws InvalidParamException {
        if (recurringInterval == null) {
            throw new InvalidParamException(
                    "Recurring interval cannot be empty");
        } else {
            this.recurringInterval = recurringInterval;
        }
    }

    public void setDateTimes(List<DateTime> dateTimes)
            throws InvalidParamException {
        if (dateTimes == null || dateTimes.size() == 0) {
            throw new InvalidParamException(
                    "DateTime field of recurring rule cannot be empty");
        } else {
            this.dateTimes = dateTimes;
        }
    }

    /**
     * Add the first occurrence of the rule
     * 
     * This is considered to be added together with the rule by the user
     * 
     * @param memory
     * @param newTodo
     */
    private void addFirstRecurringTodo(Memory memory, Todo newTodo) {
        recurringTodos.add(newTodo);
        memory.userAdd(newTodo);
    }

    /**
     * Add the subsequent occurrences of the rule
     * 
     * This is considered to be added automatically by the system, not the user
     * 
     * @param memory
     * @param newTodo
     */
    private void addRecurringTodo(Memory memory, Todo newTodo) {
        recurringTodos.add(newTodo);
        memory.systemAdd(newTodo);
    }

    private void updateDateTime() {
        for (int i = 0; i < dateTimes.size(); i++) {
            if (dateTimes.get(i) != null) {
                dateTimes.set(i, dateTimes.get(i).plus(recurringInterval));
            }
        }
    }

    public String getDisplayString() {
        // Use the todo occurrence's toString result as part of the
        // display string
        Todo todoOccurrnece = new Todo(0, name, dateTimes, recurringId);
        return String.format(recurringDisplayFormat, todoOccurrnece.toString(),
                recurringInterval.toString(PERIOD_FORMATTER),
                recurrenceLimit.toString(DATE_TIME_FORMATTER));

    }

    public String toString() {
        return String.format(recurringFormat, originalName,
                recurringInterval.toString(PERIOD_FORMATTER),
                recurrenceLimit.toString(DATE_TIME_FORMATTER));
    }

    /**
     * Method to return a DateTime of the Recurring rule's last occurrence. The
     * order of preference: start time > end time > null
     * 
     * 
     * @return start time for events; end time for deadlines; null for tasks.
     */
    public DateTime getDateTime() {
        if (dateTimes.get(0) != null) {
            return dateTimes.get(0);
        } else if (dateTimes.get(1) != null) {
            return dateTimes.get(1);
        } else {
            return null;
        }
    }
}
```
###### \testcases\EditCommandTest.java
``` java
	@Before
	public void setUp() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		String fileDirectory = ClockWork.getStorageFileDirFromSettings();
		logic = new ClockWork(fileDirectory);
		logic.reloadMemory();
		
		logic.handleInput(add + nameFloating1);
		logic.handleInput(add + nameDeadline1 + by + date1 + time1);
		logic.handleInput(add + nameEvent1 + from + date1 + time1 + to + date1 + time2);
		logic.handleInput(add + nameRecurring1 + every + period1);
		
		floating = new Todo(idFloat, nameFloating1.trim());
		
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		dateTimes.add(newDate1);
		deadline = new Todo(idDeadline, nameDeadline1.trim(), dateTimes);
		
		DateTime newDate2 = new DateTime(year, date1Month, date1Day, time2Hour, time2Min);
		dateTimes.add(newDate2);
		event = new Todo(idEvent, nameEvent1.trim(), dateTimes);
		
		List<DateTime> dateTimeRecurring = new ArrayList<DateTime>();
		dateTimeRecurring.add(newDate1);
		rule = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimeRecurring, period1P);
	}
	
	@After
	public void tearDown() {
		logic.reloadMemory();
	}

	@Test
	public void testEditFloatingName() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException, NumberFormatException, NullTodoException {
		Todo floatingEdited = new Todo(idFloat, nameFloating2.trim());
		Signal signal = logic.handleInput(edit + idStringFloat + nameFloating2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, floating, floatingEdited), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditFloatingToDeadline() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		dateTimes.add(newDate);
		Todo deadline = new Todo(idFloat, nameFloating1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringFloat + by + date1 + time1);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, floating, deadline), true);
		assertEquals(expected, signal);
	}
	
	@Test 
	public void testEditFloatingToEvent() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date1Month, date1Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		Todo event = new Todo(idFloat, nameFloating1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringFloat + from + date1 + time1 + to + date1 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, floating, event), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditDeadlineName() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		dateTimes.add(newDate1);
		Todo deadline2 = new Todo(idDeadline, nameDeadline2.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringDeadline + nameDeadline2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, deadline, deadline2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditDeadlineTime() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate);
		Todo deadline2 = new Todo(idDeadline, nameDeadline1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringDeadline + by + date2 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, deadline, deadline2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditDeadlineToEvent() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date1Month, date1Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		Todo event = new Todo(idDeadline, nameDeadline1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringDeadline + from + date1 + time1 + to + date1 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, deadline, event), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditEventName() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date1Month, date1Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		Todo event2 = new Todo(idEvent, nameEvent2.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringEvent + nameEvent2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, event, event2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditEventTime() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		Todo event2 = new Todo(idEvent, nameEvent1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringEvent + from + date2 + time1 + to + date2 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, event, event2), true);
		assertEquals(expected, signal);
		
		
	}
	
	@Test
	public void testEditEventOnDateFromTimeToTime() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		Todo event2 = new Todo(idEvent, nameEvent1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringEvent + on + date2 + from + time1 + to + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, event, event2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditEventToDeadline() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		Todo deadline2 = new Todo(idEvent, nameEvent1.trim(), dateTimes);
		Signal signal = logic.handleInput(edit + idStringEvent + by + date2 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, event, deadline2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRuleName() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		dateTimes.add(newDate1);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring2.trim(), dateTimes, period1P);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + nameRecurring2);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRulePeriod() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		dateTimes.add(newDate1);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimes, period2P);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + every + period2);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRuleLimit() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date1Month, date1Day, time1Hour, time1Min);
		dateTimes.add(newDate1);
		DateTime limitDateTime = new DateTime(year, limitMonth, limitDay, 23, 59, 00, 00);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimes, period1P, limitDateTime);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + until + limit);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRuleDeadlineTime() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimes, period1P);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + by + date2 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRuleDeadlineToEvent() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimes, period1P);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + from + date2 + time1 + to + date2 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRuleDeadlineOnFromToEvent() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimes, period1P);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + on + date2 + from + time1 + to + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
	
	@Test
	public void testEditRuleEventToDeadline() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		logic.handleInput(edit + idStringRecurringTodo + rFlag + from + date1 + time1 + to + date1 + time2);
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		DateTime newDate1 = new DateTime(year, date2Month, date2Day, time1Hour, time1Min);
		DateTime newDate2 = new DateTime(year, date2Month, date2Day, time2Hour, time2Min);
		dateTimes.add(newDate1);
		dateTimes.add(newDate2);
		RecurringTodoRule rule2 = new RecurringTodoRule(idRecurring, nameRecurring1.trim(), dateTimes, period1P);
		Signal signal = logic.handleInput(edit + idStringRecurringTodo + rFlag + by + date2 + time2);
		Signal expected = new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, rule, rule2), true);
		assertEquals(expected, signal);
	}
}
```
###### \testcases\ParserTest.java
``` java
	@Test
	public void testAddRecurringEvent() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: Friday to Sunday
		List<Date> dates1 = parser.parse("Friday to Sunday").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}

		// recurring event task
		String add1 = "add test 1 from Friday to Sunday every month";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))), dateTimes1,
				new Period().withMonths(1), true, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	@Test
	public void testAddRecurringDeadlineWithLimit()
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

		List<Date> dates2 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task with limit
		String add3 = "add test 3 on Friday every week until 4 Dec 2015";
		ParsedInput parsed3 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 3"))), dateTimes0,
				new Period().withWeeks(1), true, true, dateTimes2.get(0));
		assertEquals(parsed3, Parser.parseInput(add3));
	}

	@Test
	public void testAddRecurringDeadlineWithNonLimit()
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
		// + UNTIL <invalid limit>'
		String add0 = "add test 0 on Friday every week until forever";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0 until forever"))),
				dateTimes0, new Period().withWeeks(1), true, false,
				new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddRecurringEventWithLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: Friday to Sunday
		List<Date> dates1 = parser.parse("Friday to Sunday").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}

		// dates: limit 4 December 2015
		List<Date> dates2 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withTime(23, 59, 0, 0));
		}

		// recurring event task with limit
		String add4 = "add test 4 from Friday to Sunday every month until 4 Dec 2015";
		ParsedInput parsed4 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 4"))), dateTimes1,
				new Period().withMonths(1), true, true, dateTimes2.get(0));

		assertEquals(parsed4, Parser.parseInput(add4));
	}

	@Test
	public void testAddRecurringDeadlineEveryDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("4 Jan").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'KEYWORD <valid date> + EVERY <valid period>
		String add0 = "add test 0 every 4 Jan";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period().withYears(1), true, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test(expected = InvalidRecurringException.class)
	public void testAddInvalidRecurring() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 4 Dec 2015
		List<Date> dates0 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}
		// invalid recurring command: name + every <valid period> + <valid
		// limit>
		String add0 = "add test 5 every month until 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 5"))),
				new ArrayList<DateTime>(), new Period().withMonths(1), false,
				true, dateTimes0.get(0));
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test(expected = ParsingFailureException.class)
	public void testAddTaskWithMoreThanTwoDateTimes()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		String add0 = "add event from 1 Jan to 2 Jan in 2 days";
		Parser.parseInput(add0);
	}

	@Test
	public void testEditName() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {

		String edit0 = "edit 1 change name";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1 change name"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test(expected = InvalidTodoNameException.class)
	public void testEditInvalidName() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		String edit0 = "edit 1 -n";
		Parser.parseInput(edit0);
	}

	@Test(expected = ParsingFailureException.class)
	public void testEditMoreThanTwoDateTimes()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		String edit0 = "edit 4 from 5 June to 6 June on 8 June";
		Parser.parseInput(edit0);
	}

	@Test
	public void testEditTo() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 4 Dec 2015
		List<Date> dates0 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 to 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.TO, "to", "4 Dec 2015"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditStart() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 4 Dec 2015
		List<Date> dates0 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 from 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.FROM, "from", "4 Dec 2015"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditEnd() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 4 Dec 2015
		List<Date> dates0 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 on 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.ON, "on", "4 Dec 2015"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditStartAndEnd() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 3 Dec 2015 and 4 Dec 2015
		List<Date> dates0 = parser.parse("3 Dec 2015 to 4 Dec 2015").get(0)
				.getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 from 3 Dec 2015 to 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.FROM, "from", "3 Dec 2015"), new KeyParamPair(
						Keywords.TO, "to", "4 Dec 2015"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringName() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		String edit0 = "edit 1 -r change name";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1 change name"),
						new KeyParamPair(Keywords.RULE, "-r", ""))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringStartAndEnd()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 3 Dec 2015 and 4 Dec 2015
		List<Date> dates0 = parser.parse("3 Dec 2015 to 4 Dec 2015").get(0)
				.getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 -r from 3 Dec 2015 to 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.RULE, "-r", ""), new KeyParamPair(
						Keywords.FROM, "from", "3 Dec 2015"), new KeyParamPair(
						Keywords.TO, "to", "4 Dec 2015"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringPeriod() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		String edit0 = "edit 1 -r every week";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.RULE, "-r", ""), new KeyParamPair(
						Keywords.EVERY, "every", "week"))),
				new ArrayList<DateTime>(), new Period().withWeeks(1), true,
				false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringLimit() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 1 jan 2016
		List<Date> dates1 = parser.parse("1 jan 2016").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 -r until 1 jan 2016";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.RULE, "-r", ""), new KeyParamPair(
						Keywords.UNTIL, "until", "1 jan 2016"))),
				new ArrayList<DateTime>(), new Period(), true, true,
				dateTimes1.get(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringStartAndEndWithLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: from 3 Dec 2015 to 4 Dec 2015
		List<Date> dates0 = parser.parse("3 Dec 2015 to 4 Dec 2015").get(0)
				.getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// date: 1 jan 2016
		List<Date> dates1 = parser.parse("1 jan 2016").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 0 -r from 3 Dec 2015 to 4 Dec 2015 until 1 jan 2016";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "0"), new KeyParamPair(
						Keywords.RULE, "-r", ""), new KeyParamPair(
						Keywords.FROM, "from", "3 Dec 2015"), new KeyParamPair(
						Keywords.UNTIL, "until", "1 jan 2016"),
						new KeyParamPair(Keywords.TO, "to", "4 Dec 2015"))),
				dateTimes0, new Period(), true, true, dateTimes1.get(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringNameWithLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {

		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 1 jan 2016
		List<Date> dates1 = parser.parse("1 jan 2016").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}
		String edit0 = "edit 0 -r new name until 1 jan 2016";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "0 new name"), new KeyParamPair(
						Keywords.RULE, "-r", ""), new KeyParamPair(
						Keywords.UNTIL, "until", "1 jan 2016"))),
				new ArrayList<DateTime>(), new Period(), true, true,
				dateTimes1.get(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testEditRecurringWithNonPeriod()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		String edit0 = "edit 1 -r every one";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1 every one"),
						new KeyParamPair(Keywords.RULE, "-r", ""),
						new KeyParamPair(Keywords.EVERY, "every", "one"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testSearchFlagName() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		String search0 = "search -n name";
		ParsedInput parsed0 = new ParsedInput(Keywords.SEARCH,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.SEARCH, "search", ""), new KeyParamPair(
						Keywords.NAME, "-n", "name"))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(search0));
	}
	
	@Test
	public void testSearchName() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		String search0 = "search name";
		ParsedInput parsed0 = new ParsedInput(Keywords.SEARCH,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.SEARCH, "search", "name"))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(search0));
	}
	
	@Test
	public void testSearchYear() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 2016
		List<Date> dates0 = parser.parse("march 2016").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String search0 = "search -y 2016";
		ParsedInput parsed0 = new ParsedInput(Keywords.SEARCH,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.SEARCH, "search", ""), new KeyParamPair(
						Keywords.YEAR, "-y", "march 2016"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(search0));

	}

	@Test(expected = InvalidTodoNameException.class)
	public void testAddFlagName() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {

		String add0 = "add -y";
		Parser.parseInput(add0);
	}
}
```
###### \userinterface\BoxFeedback.java
``` java
	public BoxFeedback() {
		this.setPadding(new Insets(10,10,10,10));
		this.setStyle("-fx-background-color: #182733;");
	}
}
```
###### \userinterface\BoxHeader.java
``` java
	public BoxHeader() {
		implementHeaderNodes();
		this.setLeft(createTaskBox());
		this.setRight(createShortcutBox());
		this.setStyle("-fx-background-color: #272b39;");
	}

	private void implementHeaderNodes() {
		helpNode = createHelpNode();
		calNode = createCalNode();
		minNode = createMinNode();
		escNode = createEscNode();
		summaryNode = createSummaryNode();
	}
	
	private Node createShortcutBox(){
		ComponentContentBoxHeader shortcutsBox = new ComponentContentBoxHeader();
		
		ComponentContentBoxHeader helpCalBox = createLeftShortcutBox();
		ComponentContentBoxHeader minSumBox = createCenterShortcutBox();
		ComponentContentBoxHeader escBox = createRightShortcutBox();
		
		shortcutsBox.setLeft(helpCalBox);
		shortcutsBox.setCenter(minSumBox);
		shortcutsBox.setRight(escBox);
		
		return shortcutsBox;
	}
	
	private ComponentContentBoxHeader createLeftShortcutBox() {
		ComponentContentBoxHeader helpCalBox = new ComponentContentBoxHeader();
		helpCalBox.setLeft(helpNode);
		helpCalBox.setRight(calNode);
		return helpCalBox;
	}

	private ComponentContentBoxHeader createCenterShortcutBox() {
		ComponentContentBoxHeader minSumBox = new ComponentContentBoxHeader();
		minSumBox.setLeft(summaryNode);
		minSumBox.setRight(minNode);
		return minSumBox;
	}
	
	private ComponentContentBoxHeader createRightShortcutBox() {
		ComponentContentBoxHeader escBox = new ComponentContentBoxHeader();
		escBox.setCenter(escNode);
		return escBox;
	}

	private ComponentContentBoxHeader createTaskBox(){
		ComponentContentBoxHeader taskBox = new ComponentContentBoxHeader();
		Node wrappedTaskLabel = Borders.wrap(taskLbl2).lineBorder().color(Color.WHITE).build().build();
		taskBox.setLeft(wrappedTaskLabel);
		
		return taskBox;
	}
	
	private Node createHelpNode() {
		ComponentContentBoxHeader helpShortcutBox = new ComponentContentBoxHeader();

		helpShortcutBox.setTop(helpLbl);
		helpShortcutBox.setCenter(dummyLbl);
		helpShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.QUESTION));

		Node wrappedHelpLabel = Borders.wrap(helpShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedHelpLabel;
	}

	private Node createCalNode() {
		ComponentContentBoxHeader calShortcutBox = new ComponentContentBoxHeader();

		calShortcutBox.setTop(calLbl);
		calShortcutBox.setCenter(dummyLbl);
		calShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CALENDAR));
		Node wrappedCalLabel = Borders.wrap(calShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedCalLabel;
	}
	
	private Node createMinNode() {
		ComponentContentBoxHeader minShortcutBox = new ComponentContentBoxHeader();

		Label dummyLbl = new Label(" ");
		
		minShortcutBox.setTop(minLbl);
		minShortcutBox.setCenter(dummyLbl);
		minShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));
		
		Node wrappedMinLabel = Borders.wrap(minShortcutBox).lineBorder()
								.color(Color.WHITE).build().build();

		return wrappedMinLabel;
	}
	
	private Node createEscNode(){
		ComponentContentBoxHeader escShortcutBox = new ComponentContentBoxHeader();
		
		escShortcutBox.setTop(escLbl);
		escShortcutBox.setCenter(dummyLbl);
		escShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPLY));
		Node wrappedEscLabel = Borders.wrap(escShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEscLabel;
	}
	
	private Node createSummaryNode(){
		ComponentContentBoxHeader summaryShortcutBox = new ComponentContentBoxHeader();
		
		summaryShortcutBox.setTop(summaryLbl);
		summaryShortcutBox.setCenter(dummyLbl);
		summaryShortcutBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.FOLDER));
		Node wrappedSummaryLabel = Borders.wrap(summaryShortcutBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedSummaryLabel;
	}
	
	public void removeEscNode(){
		escNode = null;
		this.setLeft(createTaskBox());
		this.setRight(createShortcutBox());
		this.setStyle("-fx-background-color: #272b39;");
	}
}
```
###### \userinterface\BoxInput.java
``` java
	public BoxInput() {
		Controller.implementKeystrokeEvents(this);
		this.setStyle("-fx-background-color: #272b39; -fx-text-inner-color: white;");
	}
}
```
###### \userinterface\BoxTask.java
``` java
	public BoxTask() {
		this.setStyle("-fx-background-color: #182733;");
		this.setPadding(new Insets(10,10,10,10));
	}

}
```
###### \userinterface\clockwork.css
``` css
.label {
	-fx-text-fill: #FFFFFF;
}

.text {
	-fx-text-fill: #FFFFFF;
}

.button {
	-fx-background-color: transparent;
}
.button:focused {
    -fx-background-color: rgba(0, 100, 100, 0.5);
}

.glyph-icon {
	-fx-fill: #FFFFFF;
}

.hbox {
	-fx-background-color: #182733;
}

.grid {
	-fx-background-color: #182733;
}

.border-pane {
	-fx-background-color: #182733;
}

.scroll-pane {
    -fx-background-color:transparent;
}

.table-view{
   -fx-background-color: #182733;
}

.table-view .column-header-background{
    -fx-background-color: rgba(0, 100, 100, 0.5);
}

.table-view .column-header-background .label{
    -fx-background-color: transparent;
    -fx-text-fill: white;
}

.table-view .column-header {
    -fx-background-color: transparent;
}

.table-cell:filled:selected:focused, .table-cell:filled:selected {
    -fx-background-color: #182733;
    -fx-text-fill: white;
}

.table-cell:odd { 
    -fx-background-color: #182733;
    -fx-text-fill: white;
}

.table-cell:even { 
    -fx-background-color: #182733;
    -fx-text-fill: white;
}

.table-cell:filled:hover {
    -fx-background-color: #182733;
    -fx-text-fill: white;
}

#calendar-control {
	-fx-padding: 1;  
	-fx-background-insets: 0, 100;
	-fx-background-radius: 0, 0; 
	-fx-background-color: rgba(0, 100, 100, 0.1);
}
```
###### \userinterface\ComponentContentBoxHeader.java
``` java
	public ComponentContentBoxHeader() {
		this.setStyle("-fx-background-color: #272b39;");
	}
}
```
###### \userinterface\ComponentRectSummary.java
``` java
	public ComponentRectSummary(){
		this.setWidth(150);
		this.setHeight(150);
		this.setFill(Color.TRANSPARENT);
	}
}
```
###### \userinterface\LayoutHelp.java
``` java
	public LayoutHelp() {
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

	private void setCenterRegion() {
		wrappedAdd = createAddNode();
		wrappedDelete = createDeleteNode();
		wrappedEdit = createEditNode();
		wrappedUndo = createUndoNode();
		wrappedRedo = createRedoNode();
		wrappedSearch = createSearchNode();
		wrappedMark = createMarkNode();

		implementHelpContentBar();
		implementHelpContentBox();

		this.setCenter(helpContentBox);
	}

	private void setBottomRegion() {
		TextField textField = implementTextField();
		this.setBottom(textField);
	}

	/** IMPLEMENTING REGION OBJECTS */

	private void implementHelpContentBox() {	
		helpContentBox.getChildren().add(helpContentBar);
		helpContentBox.setAlignment(Pos.CENTER);
		helpContentBox.setStyle("-fx-background-color: #182733");
		
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		helpContentBox.getColumnConstraints().add(columnConstraints);
	}

	private void implementHelpContentBar() {
		helpContentBar.getChildren().addAll(wrappedAdd, wrappedDelete, wrappedEdit, wrappedUndo, wrappedRedo,
				wrappedSearch, wrappedMark);
		helpContentBar.setAlignment(Pos.CENTER);
		helpContentBar.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ESCAPE)) {
					Controller.processEnter("DISPLAY");
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		return textField;
	}

	/** CREATING LAYOUT OBJECTS */
	
	private Node createMarkNode() {
		BorderPane markBox = new BorderPane();

		Label markLbl = new Label("Mark");

		markBox.setTop(markLbl);
		markBox.setCenter(dummyLbl);
		markBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.CHECK));

		Node wrappedMark = Borders.wrap(markBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedMark;
	}

	private Node createSearchNode() {
		BorderPane searchBox = new BorderPane();

		Label searchLbl = new Label("Search");

		searchBox.setTop(searchLbl);
		searchBox.setCenter(dummyLbl);
		searchBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.SEARCH));

		Node wrappedSearch = Borders.wrap(searchBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedSearch;
	}

	private Node createRedoNode() {
		BorderPane redoBox = new BorderPane();

		Label redoLbl = new Label("Redo");

		redoBox.setTop(redoLbl);
		redoBox.setCenter(dummyLbl);
		redoBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.REPEAT));

		Node wrappedRedo = Borders.wrap(redoBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedRedo;
	}

	private Node createUndoNode() {
		BorderPane undoBox = new BorderPane();

		Label undoLbl = new Label("Undo");

		undoBox.setTop(undoLbl);
		undoBox.setCenter(dummyLbl);
		undoBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.UNDO));

		Node wrappedUndo = Borders.wrap(undoBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedUndo;
	}

	private Node createEditNode() {
		BorderPane editBox = new BorderPane();

		Label editLbl = new Label("Edit");

		editBox.setTop(editLbl);
		editBox.setCenter(dummyLbl);
		editBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.PENCIL));

		Node wrappedEdit = Borders.wrap(editBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedEdit;
	}

	private Node createAddNode() {
		BorderPane addBox = new BorderPane();

		Label addLbl = new Label("Add");

		addBox.setTop(addLbl);
		addBox.setCenter(dummyLbl);
		addBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.PLUS));

		Node wrappedAdd = Borders.wrap(addBox).lineBorder().color(Color.WHITE).build().build();

		return wrappedAdd;
	}

	private Node createDeleteNode() {
		BorderPane deleteBox = new BorderPane();

		Label deleteLbl = new Label("Delete");

		deleteBox.setTop(deleteLbl);
		deleteBox.setCenter(dummyLbl);
		deleteBox.setBottom(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));

		Node wrappedDelete = Borders.wrap(deleteBox).lineBorder().color(Color.WHITE).build().build();
		return wrappedDelete;
	}
}
```
###### \userinterface\LayoutSummary.java
``` java
	public LayoutSummary(int numToday, int numTomorrow, int numUpcoming, int numSomeday) {
		_numToday = numToday;
		_numTomorrow = numTomorrow;
		_numUpcoming = numUpcoming;
		_numSomeday = numSomeday;
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

	private void setCenterRegion() {		
		ComponentRectSummary rectToday = new ComponentRectSummary();
		ComponentRectSummary rectTomorrow = new ComponentRectSummary();
		ComponentRectSummary rectUpcoming = new ComponentRectSummary();
		ComponentRectSummary rectSomeday = new ComponentRectSummary();
		
		StackPane spToday = new StackPane();
		StackPane spTomorrow = new StackPane();
		StackPane spUpcoming = new StackPane();
		StackPane spSomeday = new StackPane();
		
		spToday.getChildren().addAll(rectToday, createSummaryButton(todayButton, todayString, createSummaryButtonString(todayString, _numToday)));
		spTomorrow.getChildren().addAll(rectTomorrow, createSummaryButton(tomorrowButton, tomorrowString, createSummaryButtonString(tomorrowString, _numTomorrow)));
		spUpcoming.getChildren().addAll(rectUpcoming, createSummaryButton(upcomingButton, upcomingString, createSummaryButtonString(upcomingString, _numUpcoming)));
		spSomeday.getChildren().addAll(rectSomeday, createSummaryButton(somedayButton, somedayString, createSummaryButtonString(somedayString, _numSomeday)));

		GridPane gridSummaryButtons = new GridPane();
		
		gridSummaryButtons.add(spToday, 0, 0);
		gridSummaryButtons.add(spTomorrow, 1, 0);
		gridSummaryButtons.add(spUpcoming, 0, 1);
		gridSummaryButtons.add(spSomeday, 1, 1);

		HBox gridSummaryRowWrapper = new HBox();

		gridSummaryRowWrapper.getChildren().add(gridSummaryButtons);
		gridSummaryRowWrapper.setAlignment(Pos.CENTER);

		GridPane centralizedGridSummary = new GridPane();

		centralizedGridSummary.getChildren().add(gridSummaryRowWrapper);
		centralizedGridSummary.setAlignment(Pos.CENTER);
		centralizedGridSummary.setStyle("-fx-background-color: #182733");

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		centralizedGridSummary.getColumnConstraints().add(columnConstraints);

		this.setCenter(centralizedGridSummary);
	}

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
	
	private String createSummaryButtonString(String summaryType, int numItems){
		String summaryString = summaryType + "\n" + Integer.toString(numItems) + " Item(s)";
		return summaryString;
	}
	
	private Node createSummaryButton(Button button, String summaryType, String summaryString){
		button.setText(summaryString);
		button.setWrapText(true);
		button.setPrefSize(150, 150);
		button.setTextFill(Color.WHITE);
		Controller.redirectScene(button, summaryType);
		Node wrappedButton = Borders.wrap(button).lineBorder().color(Color.AQUAMARINE).build().build();
		return wrappedButton;
	}
}
```
###### \userinterface\LayoutTemplate.java
``` java
	public LayoutTemplate(String title, ArrayList<String[]> list, ArrayList<String> feedbackList) {
		if (feedbackList == null) System.out.println("Error: LayoutTemplate null");
		_titleString = title;
		_list = list;
		_listSize = list.size();
		_feedbackList = feedbackList;
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

	private void setCenterRegion() {
		implementTitle();

		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #182733;");

		TableColumn<Map, String> firstDataColumn = new TableColumn<>("No");
		TableColumn<Map, String> secondDataColumn = new TableColumn<>("Name");
		TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Time");

		firstDataColumn.setCellValueFactory(new MapValueFactory(ColumnIndexMapKey));
		firstDataColumn.setMinWidth(10);
		secondDataColumn.setCellValueFactory(new MapValueFactory(ColumnNameMapKey));
		secondDataColumn.setMinWidth(460);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(ColumnTimeMapKey));
		thirdDataColumn.setMinWidth(290);

		tableView = new TableView<>(populateDataInMap());
		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);
		tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn);
		Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = (
				TableColumn<Map, String> p) -> new TextFieldTableCell(new StringConverter() {
					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});

		firstDataColumn.setCellFactory(cellFactoryForMap);
		secondDataColumn.setCellFactory(cellFactoryForMap);
		thirdDataColumn.setCellFactory(cellFactoryForMap);
		
//		System.out.println("Num of Rows: " + tableView.getItems().size());
		
		grid.setHgrow(tableView, Priority.ALWAYS);

		grid.add(_titleNode, 0, 0);
		grid.add(tableView, 0, 1);

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		hbox.setStyle("-fx-background-color: #182733;");

		this.setCenter(hbox);
	}

	private ObservableList<Map> populateDataInMap() {
		ObservableList<Map> allData = FXCollections.observableArrayList();
		for (int i = 1; i < _listSize; i++) {
			Map<String, String> dataRow = new HashMap<>();

			String index = _list.get(i)[0];
			String name = _list.get(i)[1];
			String time = _list.get(i)[2];

			dataRow.put(ColumnIndexMapKey, index);
			dataRow.put(ColumnNameMapKey, name);
			dataRow.put(ColumnTimeMapKey, time);

			allData.add(dataRow);
		}
		return allData;
	}

	private void setBottomRegion() {
		Text feedbackText = createFeedbackLabel();

		BoxFeedback feedbackBox = implementFeedbackBox(feedbackText);
		BoxInput inputBox = implementInputBox();

		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	private BorderPane implementUserBox(BoxFeedback feedbackBox, BoxInput inputBox) {
		BorderPane userBox = new BorderPane();
		userBox.setTop(feedbackBox);
		userBox.setBottom(inputBox);
		userBox.setStyle("-fx-background-color: #182733;");
		return userBox;
	}

	private BoxInput implementInputBox() {
		BoxInput textField = new BoxInput();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.UP)) {
					if ((currentScrollIndex + scrollUpIndex) >= 0){
						tableView.scrollTo(currentScrollIndex + scrollUpIndex);
						currentScrollIndex = currentScrollIndex + scrollUpIndex;
					}
				} else if (ke.getCode().equals(KeyCode.DOWN)){
					if ((currentScrollIndex + scrollDownIndex) <= tableView.getItems().size()){
						tableView.scrollTo(currentScrollIndex + scrollDownIndex);
						currentScrollIndex = currentScrollIndex + scrollDownIndex;
					}
				}  else if (ke.getCode().equals(KeyCode.ESCAPE)){
					// DISPLAY SUMMARY SCENE
					Main.setNumToday(Controller.getNumTodayItems());
					Main.setNumTomorrow(Controller.getNumTomorrowItems());
					Main.setNumUpcoming(Controller.getNumUpcomingItems());
					Main.setNumSomeday(Controller.getNumSomedayItems());
					Main.displaySummaryScene();
				}
				
				Controller.executeKeyPress(textField, ke);
			}
		});
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "mark", "edit");
		return textField;
	}

	private BoxFeedback implementFeedbackBox(Text feedbackText) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackText);
		return feedbackBox;
	}

	private Text createFeedbackLabel() {
		 String[] result = _feedbackList.get(0).split(" ", 2);
		    String first = result[0];
		Text feedbackText = new Text(_feedbackList.get(0));		
		feedbackText.setText(_feedbackList.get(0));
		feedbackText.setWrappingWidth(500);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font("Calibri", 12));
		if (first.equals("Added")) {
			feedbackText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		} else if (first.equals("Edited")) {
			feedbackText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
		} else if (first.equals("Marked") || first.equals("Deleted")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if (first.equals("Undo")) {
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Clash")) {
			feedbackText.setText(_feedbackList.get(0));
			feedbackText.setFill(Color.CRIMSON);
		}
		return feedbackText;
	}
}
```
###### \userinterface\LayoutTemplateAll.java
``` java
	public LayoutTemplateAll(String title, ArrayList<String[]> list, ArrayList<String> feedbackList) {
		_titleString = title;
		_list = list;
		_feedbackList = feedbackList;
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

	private void setCenterRegion() {
		implementTitle();

		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #182733;");

		TableColumn<Map, String> firstDataColumn = new TableColumn<>("No");
		TableColumn<Map, String> secondDataColumn = new TableColumn<>("Name");
		TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Time");
		TableColumn<Map, String> fourthDataColumn = new TableColumn<>("Date");

		firstDataColumn.setCellValueFactory(new MapValueFactory(ColumnIndexMapKey));
		firstDataColumn.setMinWidth(20);
		secondDataColumn.setCellValueFactory(new MapValueFactory(ColumnNameMapKey));
		secondDataColumn.setMinWidth(320);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(ColumnTimeMapKey));
		thirdDataColumn.setMinWidth(250);
		fourthDataColumn.setCellValueFactory(new MapValueFactory(ColumnDateMapKey));
		fourthDataColumn.setMinWidth(150);

	    tableView = new TableView<>(populateDataInMap());
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);
		tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn, fourthDataColumn);
		Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = (
				TableColumn<Map, String> p) -> new TextFieldTableCell(new StringConverter() {
					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});

		firstDataColumn.setCellFactory(cellFactoryForMap);
		secondDataColumn.setCellFactory(cellFactoryForMap);
		thirdDataColumn.setCellFactory(cellFactoryForMap);
		fourthDataColumn.setCellFactory(cellFactoryForMap);
		
		grid.setHgrow(tableView, Priority.ALWAYS);

		grid.add(_titleNode, 0, 0);
		grid.add(tableView, 0, 1);
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		hbox.setStyle("-fx-background-color: #182733;");

		this.setCenter(hbox);
	}

	private ObservableList<Map> populateDataInMap() {
		ObservableList<Map> allData = FXCollections.observableArrayList();
		for (int i = 0; i < _list.size(); i++) {
			Map<String, String> dataRow = new HashMap<>();
			String index = _list.get(i)[0];
			String name = _list.get(i)[1];
			String time = _list.get(i)[2];
			String date = _list.get(i)[3];
		//	System.out.println(_list.size());
			
			dataRow.put(ColumnIndexMapKey, index);
			dataRow.put(ColumnNameMapKey, name);
			dataRow.put(ColumnTimeMapKey, time);
			dataRow.put(ColumnDateMapKey, date);

			allData.add(dataRow);
		}
		return allData;
	}

	private void setBottomRegion() {
		Text feedbackText = createFeedbackLabel();

		BoxFeedback feedbackBox = implementFeedbackBox(feedbackText);
		BoxInput inputBox = implementInputBox();

		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	private BorderPane implementUserBox(BoxFeedback feedbackBox, BoxInput inputBox) {
		BorderPane userBox = new BorderPane();
		userBox.setTop(feedbackBox);
		userBox.setBottom(inputBox);
		userBox.setStyle("-fx-background-color: #182733;");
		return userBox;
	}

	private BoxInput implementInputBox() {
		BoxInput textField = new BoxInput();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.UP)) {
					if ((currentScrollIndex + scrollUpIndex) >= 0){
						tableView.scrollTo(currentScrollIndex + scrollUpIndex);
						currentScrollIndex = currentScrollIndex + scrollUpIndex;
					}
				} else if (ke.getCode().equals(KeyCode.DOWN)){
					if ((currentScrollIndex + scrollDownIndex) <= tableView.getItems().size()){
						tableView.scrollTo(currentScrollIndex + scrollDownIndex);
						currentScrollIndex = currentScrollIndex + scrollDownIndex;
					}
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "mark", "edit");
		return textField;
	}

	private BoxFeedback implementFeedbackBox(Text feedbackText) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackText);
		return feedbackBox;
	}

	private Text createFeedbackLabel() {
		 String[] result = _feedbackList.get(0).split(" ", 2);
		    String first = result[0];
		   // System.out.println(first);
		Text feedbackText = new Text(_feedbackList.get(0));		
		feedbackText.setText(_feedbackList.get(0));
		feedbackText.setWrappingWidth(500);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font("Calibri", 12));
		if (first.equals("Added")) {
			feedbackText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		} else if (first.equals("Edited")) {
			feedbackText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
		} else if (first.equals("Marked") || first.equals("Deleted")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if (first.equals("Undo")) {
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Clash")) {
			feedbackText.setText(_feedbackList.get(0));
			feedbackText.setFill(Color.CRIMSON);
		}
		return feedbackText;
	}
}
```
###### \userinterface\LayoutTemplateDate.java
``` java
	public LayoutTemplateDate(String title, ArrayList<String[]> list, ArrayList<String> feedbackList) {
		_titleString = title;
		_list = list;
		_feedbackList = feedbackList;
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

	private void setCenterRegion() {
		implementTitle();

		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #182733;");

		TableColumn<Map, String> firstDataColumn = new TableColumn<>("No");
		TableColumn<Map, String> secondDataColumn = new TableColumn<>("Name");
		TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Time");
		TableColumn<Map, String> fourthDataColumn = new TableColumn<>("Date");

		firstDataColumn.setCellValueFactory(new MapValueFactory(ColumnIndexMapKey));
		firstDataColumn.setMinWidth(20);
		secondDataColumn.setCellValueFactory(new MapValueFactory(ColumnNameMapKey));
		secondDataColumn.setMinWidth(320);
		thirdDataColumn.setCellValueFactory(new MapValueFactory(ColumnTimeMapKey));
		thirdDataColumn.setMinWidth(250);
		fourthDataColumn.setCellValueFactory(new MapValueFactory(ColumnDateMapKey));
		fourthDataColumn.setMinWidth(150);

	    tableView = new TableView<>(populateDataInMap());
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(false);
		tableView.getSelectionModel().setCellSelectionEnabled(false);
		tableView.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn, fourthDataColumn);
		Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = (
				TableColumn<Map, String> p) -> new TextFieldTableCell(new StringConverter() {
					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});

		firstDataColumn.setCellFactory(cellFactoryForMap);
		secondDataColumn.setCellFactory(cellFactoryForMap);
		thirdDataColumn.setCellFactory(cellFactoryForMap);
		fourthDataColumn.setCellFactory(cellFactoryForMap);
		
		grid.setHgrow(tableView, Priority.ALWAYS);

		grid.add(_titleNode, 0, 0);
		grid.add(tableView, 0, 1);
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(grid);
		hbox.setStyle("-fx-background-color: #182733;");

		this.setCenter(hbox);
	}

	private ObservableList<Map> populateDataInMap() {
		ObservableList<Map> allData = FXCollections.observableArrayList();
		for (int i = 0; i < _list.size(); i++) {
			Map<String, String> dataRow = new HashMap<>();
			String index = _list.get(i)[0];
			String name = _list.get(i)[1];
			String time = _list.get(i)[2];
			String date = _list.get(i)[3];
		//	System.out.println(_list.size());
			
			dataRow.put(ColumnIndexMapKey, index);
			dataRow.put(ColumnNameMapKey, name);
			dataRow.put(ColumnTimeMapKey, time);
			dataRow.put(ColumnDateMapKey, date);

			allData.add(dataRow);
		}
		return allData;
	}

	private void setBottomRegion() {
		Text feedbackText = createFeedbackLabel();

		BoxFeedback feedbackBox = implementFeedbackBox(feedbackText);
		BoxInput inputBox = implementInputBox();

		BorderPane userBox = implementUserBox(feedbackBox, inputBox);

		this.setBottom(userBox);
	}

	private void implementTitle() {
		_titleLabel = new Label(_titleString);
		_titleNode = Borders.wrap(_titleLabel).lineBorder().color(Color.AQUAMARINE).build().build();
	}

	private BorderPane implementUserBox(BoxFeedback feedbackBox, BoxInput inputBox) {
		BorderPane userBox = new BorderPane();
		userBox.setTop(feedbackBox);
		userBox.setBottom(inputBox);
		userBox.setStyle("-fx-background-color: #182733;");
		return userBox;
	}

	private BoxInput implementInputBox() {
		BoxInput textField = new BoxInput();
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.UP)) {
					if ((currentScrollIndex + scrollUpIndex) >= 0){
						tableView.scrollTo(currentScrollIndex + scrollUpIndex);
						currentScrollIndex = currentScrollIndex + scrollUpIndex;
					}
				} else if (ke.getCode().equals(KeyCode.DOWN)){
					if ((currentScrollIndex + scrollDownIndex) <= tableView.getItems().size()){
						tableView.scrollTo(currentScrollIndex + scrollDownIndex);
						currentScrollIndex = currentScrollIndex + scrollDownIndex;
					}
				} else if (ke.getCode().equals(KeyCode.ESCAPE)){
					// DISPLAY SUMMARY SCENE
					Main.setNumToday(Controller.getNumTodayItems());
					Main.setNumTomorrow(Controller.getNumTomorrowItems());
					Main.setNumUpcoming(Controller.getNumUpcomingItems());
					Main.setNumSomeday(Controller.getNumSomedayItems());
					Main.displaySummaryScene();
				}
				Controller.executeKeyPress(textField, ke);
			}
		});
		TextFields.bindAutoCompletion(textField, "add", "delete", "undo", "search", "mark", "edit");
		return textField;
	}

	private BoxFeedback implementFeedbackBox(Text feedbackText) {
		BoxFeedback feedbackBox = new BoxFeedback();
		feedbackBox.getChildren().add(feedbackText);
		return feedbackBox;
	}

	private Text createFeedbackLabel() {
		 String[] result = _feedbackList.get(0).split(" ", 2);
		    String first = result[0];
		   // System.out.println(first);
		Text feedbackText = new Text(_feedbackList.get(0));		
		feedbackText.setText(_feedbackList.get(0));
		feedbackText.setWrappingWidth(500);
		feedbackText.setFill(Color.WHITE);
		feedbackText.setFont(Font.font("Calibri", 12));
		if (first.equals("Added")) {
			feedbackText.setFont(Font.font("Calibri", FontWeight.BOLD, 12));
		} else if (first.equals("Edited")) {
			feedbackText.setFont(Font.font("Calibri", FontPosture.ITALIC, 12));
		} else if (first.equals("Marked") || first.equals("Deleted")) {
			feedbackText.setStrikethrough(true);
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Redo")) {
			feedbackText.setUnderline(true);
		} else if (first.equals("Undo")) {
			feedbackText.setFill(Color.GREY);
		} else if (first.equals("Clash")) {
			feedbackText.setText(_feedbackList.get(0));
			feedbackText.setFill(Color.CRIMSON);
		}
		return feedbackText;
	}
}
```
###### \userinterface\Main.java
``` java
	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		initialiseStorage(args);
		initialiseLogic();
		Application.launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		setScene();
		setStage();
	}
	
	/*
	* ===========================================
	* Initialising Methods
	* ===========================================
	*/
	
	/** Initialise Scene for GUI */
	private static void setScene(){
		Controller.processEnter("DISPLAY");
	}
	
	/** Initialise Stage for GUI */
	private static void setStage() {
		stage.setTitle("Clockwork");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
		stage.getIcons().add(new Image(Main.class
						.getResourceAsStream("icon.png" ))); 
		stage.show();
	}
	
	/** 
	 * Initialise logic package for GUI. Comment out in main(String[] args)
	 * and uncomment UserInterfaceLogicStub method in ClockworkGUIController to test 
	 * GUI with UserInterfaceLogicStub.
	 */
	private static void initialiseLogic() {
		Controller.setLogic(ClockWork.getInstance());
		Controller.resetAllLists();
	}
	
	/** 
	 * Initialise storage paths in logic package.Comment out in main(String[] args) 
	 * and uncomment UserInterfaceLogicStub method in ClockworkGUIController to test 
	 * GUI with UserInterfaceLogicStub.
	 */
	private static void initialiseStorage(String[] args) {
		//Set storage file directory, link with storage file
		ClockWork.setFileDirectory(ClockWork.getStorageFileDirFromSettings());
		// Check if a file directory path is passed in through argument
		if (args.length == 1) {
			// Check if file directory path is valid
			String customFileDirPath = args[0];
			ClockWork.setFileDirectory(StorageUtils.processStorageDirectory(customFileDirPath));
		}
	}
	
	/*
	* ===========================================
	* Public Methods
	* ===========================================
	*/
	
	public static void setFeedback(ArrayList<String> feedback){
		_feedback = feedback;
	}
	
	public static void setTodayList(ArrayList<String[]> todayList){
		_todayList = todayList;
	}
	
	public static void setTomorrowList(ArrayList<String[]> tomorrowList){
		_tomorrowList = tomorrowList;
	}
	
	public static void setUpcomingList(ArrayList<String[]> upcomingList){
		_upcomingList = upcomingList;
	}
	
	public static void setSomedayList(ArrayList<String[]> somedayList){
		_somedayList = somedayList;
	}
	public static void setPowerList(ArrayList<String[]> powerList){
		_powerList = powerList;
	}
	public static void setSearchList(ArrayList<String[]> searchList) {
		_searchList = searchList;
	}
	public static void setNumToday(int numToday){
		_numToday = numToday;
	}
	
	public static void setNumTomorrow(int numTomorrow){
		_numTomorrow = numTomorrow;
	}
	
	public static void setNumUpcoming(int numUpcoming){
		_numUpcoming = numUpcoming;
	}
	
	public static void setNumSomeday(int numSomeday){
		_numSomeday = numSomeday;
	}
	
	public static void minimise(){
		stage.setIconified(true);
	}
	
	public static void displayHelpScene(){
		helpLayout = new LayoutHelp();
		scene = new Scene(helpLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayCalendarScene(){
		calendarLayout = new LayoutCalendar();
		scene = new Scene(calendarLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySummaryScene(){
		summaryLayout = new LayoutSummary(_numToday, _numTomorrow, _numUpcoming, _numSomeday);
		scene = new Scene(summaryLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayTodayScene(){
		_feedback = Controller.getFeedback();
		todayLayout = new LayoutTemplate("Today", _todayList, _feedback);
		scene = new Scene(todayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayTomorrowScene(){
		_feedback = Controller.getFeedback();
		tomorrowLayout = new LayoutTemplate("Tomorrow", _tomorrowList, _feedback);
		scene = new Scene(tomorrowLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayUpcomingScene(){
		_feedback = Controller.getFeedback();
		upcomingLayout = new LayoutTemplateDate("Upcoming", _upcomingList,  _feedback);
		scene = new Scene(upcomingLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySomedayScene(){
		_feedback = Controller.getFeedback();
		somedayLayout = new LayoutTemplate("Someday", _somedayList,  _feedback);
		scene = new Scene(somedayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySearchScene(){
		_feedback = Controller.getFeedback();
		searchLayout = new LayoutTemplateDate("Search", _searchList,  _feedback);
		scene = new Scene(searchLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayAllScene(){
		_feedback = Controller.getFeedback();
		allLayout = new LayoutTemplateAll("All Tasks", _powerList,  _feedback);
		scene = new Scene(allLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}

	
}
```
###### \userinterface\UserInterfaceStub.java
``` java
	public static ArrayList<String[]> populateList(ArrayList<String[]> list){
		
		list.clear();
		
		String[] indivTask = new String[4];
		
		indivTask[0] = "1.";
		indivTask[1] = "Running with Felicia";
		indivTask[2] = "17:00 - 19:00";
		indivTask[3] = "7 Apr 2016";
		
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		list.add(indivTask);
		
		return list;
	}
	
	public static ArrayList<String> populateFeedbackList(){
		ArrayList<String> feedbackList = new ArrayList<String>();
		feedbackList.add("Added");
		feedbackList.add("Added Run with Felicia");
		return feedbackList;
	}
}
```
