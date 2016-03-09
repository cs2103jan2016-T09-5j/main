package com.clockwork;

import java.util.Collection;

/**
 * The AddCommand class handles all user commands with "add" as the first
 * keyword and processes ParsedInput to generate Todo objects and adds them into
 * memory.
 */

public class AddCommand extends Command {

	/**
	 * Creates an AddCommand object.
	 * 
	 * @param input
	 *            the ParsedInput object containing the parameters.
	 * @param memory
	 *            the memory containing the Todos to which the changes should be
	 *            committed.
	 */
	public AddCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * It takes in a ParsedInput object and generates a Todo object with respect
	 * to the ParsedInput object. The Todo object can be a floating task,
	 * deadline or event.
	 * 
	 * It returns a Signal object to indicate success or failure (if exception
	 * is thrown).
	 * 
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute() {
		// Check for empty string params
		if (input.containsEmptyParams()) {
			return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		String todoName = keyParamPairs.get(0).getParam();
		//keyParamPair.size() should be 1, and maximum of dateTimes.size() should be 2
		int numberOfParams = keyParamPairs.size() + dateTimes.size();
		// Check if Todo to be created is a recurring task
		// Recurring Deadline

		if (input.isRecurring()) {
			// Check for valid number of keywords
			if (numberOfParams > 3) {
				return new Signal(Signal.ADD_INVALID_PARAMS, false);
			}

			RecurringTodoRule rule;

			// If recurrence rule has a limit
			if (input.hasLimit()) {
				rule = new RecurringTodoRule(memory.obtainFreshRecurringId(),
						todoName, dateTimes, input.getPeriod(),
						input.getLimit());
				memory.add(rule);
			}
			// If recurrence rule has no limit
			else {
				rule = new RecurringTodoRule(memory.obtainFreshRecurringId(),
						todoName, dateTimes, input.getPeriod());
			}
			memory.add(rule);
			memory.saveToFile();
			return new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
					rule.toString()), true);

		}
		// Not recurring task
		else {
			// Check for valid number of keywords
			
			if (numberOfParams > 3) {
				
				return new Signal(Signal.ADD_INVALID_PARAMS, false);
			}

			int numberOfDates = dateTimes.size();
			Collection<Todo> listOnSameDay;
			ClashDetector eventOverlapDetector;
			
			switch (numberOfDates) {
			// No dates = floating task
			case 0:
				Todo floatingTask = new Todo(memory.obtainFreshId(), todoName);
				memory.userAdd(floatingTask);
				memory.saveToFile();
				return new Signal(String.format(
						Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTask), true);
			// 1 date = deadline
			case 1:
				Todo timedTodo = new Todo(memory.obtainFreshId(), todoName,
						dateTimes);
				
				//ClashDetector object warns user of an impending time overlap
				listOnSameDay = SearchCommand.getTodosOfSameDay(Keywords.DAY, 
						timedTodo.endTime, memory);
				eventOverlapDetector = new ClashDetector(listOnSameDay, timedTodo);
				if(eventOverlapDetector.verifyTodoClash()) {
					return new Signal(Signal.CLASH_USER_VOID_TASK, false);
				}
				
				memory.userAdd(timedTodo);
				memory.saveToFile();
				return new Signal(String.format(
						Signal.ADD_SUCCESS_SIGNAL_FORMAT, timedTodo), true);
			// 2 dates = event
			case 2:
				timedTodo = new Todo(memory.obtainFreshId(), todoName,
						dateTimes);
				
				//ClashDetector object warns user of an impending time overlap
				listOnSameDay = SearchCommand.getTodosOfSameDay(Keywords.DAY, 
						timedTodo.endTime, memory);
				eventOverlapDetector = new ClashDetector(listOnSameDay, timedTodo);
				if(eventOverlapDetector.verifyTodoClash()) {
					return new Signal(Signal.CLASH_USER_VOID_TASK, false);
				}
				
				// Start-time is after end-time
				if (dateTimes.get(0).isAfter(dateTimes.get(1))) {
					memory.saveToFile();
					return new Signal(Signal.ADD_END_BEFORE_START_ERROR, false);
				
				// Valid dates
				} else {
					memory.userAdd(timedTodo);
					memory.saveToFile();
					return new Signal(String.format(
							Signal.ADD_SUCCESS_SIGNAL_FORMAT, timedTodo), true);
				}
			}
			// Should not be reached
			memory.saveToFile();
			return new Signal(Signal.ADD_UNKNOWN_ERROR, false);
		}
	}
}
