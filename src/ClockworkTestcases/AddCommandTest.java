/**
 * Unit test class for AddCommand.
 */

package ClockworkTestcases;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ClockworkExceptions.InvalidRecurringException;
import ClockworkExceptions.InvalidTodoNameException;
import ClockworkExceptions.ParsingFailureException;
import ClockworkParser.Keywords;
import ClockworkParser.ParsedInput;
import ClockworkStorage.Memory;
import ClockworkLogic.ClockWork;
import ClockworkLogic.Signal;


public class AddCommandTest {
	Memory memory;
	ParsedInput input;
	Keywords addCommand = Keywords.ADD;
	ClockWork logic;

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

	/*
	 * Test adding of events
	 */
	@Test
	public void testEvent() {
		final DateTime baseTime = new DateTime();
		DateTime changedTime;
		String formattedDate;
		String eventCommand;
		String eventString;
		Signal addSuccess;

		/*
		 * Absolute time
		 */
		/*
		 * Test for a single-worded, lower-case event, long-format
		 */
		eventCommand = "add canoeing from 3pm to 4pm on six april";
		eventString = "Event \"canoeing\" from Wed 06 Apr 2016 at 15:00 to Wed 06 Apr 2016 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event within the same day,
		 * short format
		 */

		eventCommand = "add climb Mount Everest in Nepal from 3pm to 4pm on 12 Jun";
		eventString = "Event \"climb Mount Everest in Nepal\" from Sun 12 Jun 2016 at 15:00 to Sun 12 Jun 2016 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event straddling different
		 * days, short format
		 */

		eventCommand = "add climb Mount Everest in Nepal from 5 jun 2017 3pm to 20 jun 2017 4pm";
		eventString = "Event \"climb Mount Everest in Nepal\" from Mon 05 Jun 2017 at 15:00 to Tue 20 Jun 2017 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event straddling different
		 * days, short format
		 */

		eventCommand = "add climb Mount Everest in Nepal from 3pm on 5 jun 2017 to 4pm on 20 jun 2017";
		eventString = "Event \"climb Mount Everest in Nepal\" from Mon 05 Jun 2017 at 15:00 to Tue 20 Jun 2017 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Relative time
		 */
		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (year)
		 */
		changedTime = baseTime.plusYears(3);
		formattedDate = formatDate(changedTime);
		eventCommand = "add travel the States from 2pm to 3pm in 3 years";
		eventString = "Event \"travel the States\" from " + formattedDate
				+ " at 14:00 to " + formattedDate + " at 15:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (year)
		 */
		changedTime = baseTime.plusYears(1);
		formattedDate = formatDate(changedTime);
		eventCommand = "add travel the States from 2pm to 3pm next year";
		eventString = "Event \"travel the States\" from " + formattedDate
				+ " at 14:00 to " + formattedDate + " at 15:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (month)
		 */
		changedTime = baseTime.plusMonths(3);
		formattedDate = formatDate(changedTime);
		eventCommand = "add visit the Dentist in 3 months from 2pm to 3pm";
		eventString = "Event \"visit the Dentist\" from " + formattedDate
				+ " at 14:00 to " + formattedDate + " at 15:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (week)
		 */
		changedTime = baseTime.plusMonths(1);
		formattedDate = formatDate(changedTime);
		eventCommand = "add visit the Dentist from 2pm to 3pm next month";
		eventString = "Event \"visit the Dentist\" from " + formattedDate
				+ " at 14:00 to " + formattedDate + " at 15:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (week)
		 */
		changedTime = baseTime.plusWeeks(1);
		formattedDate = formatDate(changedTime);
		eventCommand = "add CS2010 consultation in 1 week from 9am to 12pm";
		eventString = "Event \"CS2010 consultation\" from " + formattedDate
				+ " at 09:00 to " + formattedDate + " at 12:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (week)
		 */
		changedTime = baseTime.plusWeeks(2);
		formattedDate = formatDate(changedTime);
		eventCommand = "add CS2105 consultation in 2 weeks from 10am to 2pm";
		eventString = "Event \"CS2105 consultation\" from " + formattedDate
				+ " at 10:00 to " + formattedDate + " at 14:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (week)
		 */
		changedTime = baseTime.plusWeeks(1);
		formattedDate = formatDate(changedTime);
		eventCommand = "add CS2010 consultation in 1 week from 9am to 12pm";
		eventString = "Event \"CS2010 consultation\" from " + formattedDate
				+ " at 09:00 to " + formattedDate + " at 12:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (day)
		 */
		changedTime = baseTime.plusDays(3);
		formattedDate = formatDate(changedTime);
		eventCommand = "add CS2103T presentation in 3 days from 3pm to 4pm";
		eventString = "Event \"CS2103T presentation\" from " + formattedDate
				+ " at 15:00 to " + formattedDate + " at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event, within a day, relative
		 * (day)
		 */
		changedTime = baseTime.plusDays(1);
		formattedDate = formatDate(changedTime);
		eventCommand = "add cycling at ECP from 6pm to 7pm tomorrow";
		eventString = "Event \"cycling at ECP\" from " + formattedDate
				+ " at 18:00 to " + formattedDate + " at 19:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

		/*
		 * Test for a multiple-worded, mixed-case event within a day, relative
		 * (week)
		 */
		changedTime = baseTime.plusDays(3);
		formattedDate = formatDate(changedTime);
		eventCommand = "add CS2103T presentation in 3 days from 3pm to 4pm";
		eventString = "Event \"CS2103T presentation\" from " + formattedDate
				+ " at 15:00 to " + formattedDate + " at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT,
				eventString), true);
		try {
			assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
	}
	
	@Test
	public void testRecurringTasks(){
		DateTime baseTime = new DateTime();
		DateTime changedTime = baseTime.plusYears(1);
		String formattedDate = formatDate(changedTime);
		Signal addSuccess;
		String recurrenceRule;
		String recurrenceCommand;
		
		/*
		 * Test for a single-worded recurring floating task (daily), no limit
		 */
		recurrenceCommand = "add swim every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a single-worded recurring deadline (daily), no limit
		 */
		recurrenceCommand = "add swim by 3pm every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a single-worded recurring deadline (daily), no limit
		 */
		recurrenceCommand = "add swim from 4pm to 5pm every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a single-worded recurring deadline (daily), no limit
		 */
		recurrenceCommand = "add swim from 4pm to 5pm every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a single-worded recurring floating task (daily), no limit
		 */
		recurrenceCommand = "add swim every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a single-worded recurring deadline (daily), no limit
		 */
		recurrenceCommand = "add swim by 3pm every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring deadline (monthly), no limit
		 */
		recurrenceCommand = "add submit monthly report by 2300pm every month";
		recurrenceRule = "Recurrence Rule: \"submit monthly report\" every 1 month(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring deadline (monthly), with limit
		 */
		recurrenceCommand = "add submit monthly report by 2300pm every month until 8 Dec";
		recurrenceRule = "Recurrence Rule: \"submit monthly report\" every 1 month(s) until Thu 08 Dec 2016";
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring deadline (yearly), no limit
		 */
		recurrenceCommand = "add celebrate New Year on 1 Jan every year";
		recurrenceRule = "Recurrence Rule: \"celebrate New Year\" every 1 year(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a single-worded recurring event (daily), no limit
		 */
		recurrenceCommand = "add swim from 4pm to 5pm every day";
		recurrenceRule = "Recurrence Rule: \"swim\" every 1 day(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring event (daily), with limit
		 */
		recurrenceCommand = "add swim at Jurong Swimming Complex from 4pm to 5pm every day until 5 jun";
		recurrenceRule = "Recurrence Rule: \"swim at Jurong Swimming Complex\" every 1 day(s) until Sun 05 Jun 2016";
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring event(weekly), no limit
		 */
		recurrenceCommand = "add hiking at BTH from 4pm to 5pm every week";
		recurrenceRule = "Recurrence Rule: \"hiking at BTH\" every 1 week(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring event(weekly), with limit
		 */
		recurrenceCommand = "add hiking at BTH from 4pm to 5pm every week until 8 jun";
		recurrenceRule = "Recurrence Rule: \"hiking at BTH\" every 1 week(s) until Wed 08 Jun 2016";
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring event(month), no limit
		 */
		recurrenceCommand = "add hiking at BTH from 4pm to 5pm every month";
		recurrenceRule = "Recurrence Rule: \"hiking at BTH\" every 1 month(s) until " + formattedDate;
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
		/*
		 * Test for a multiple-worded recurring event(month), with limit
		 */
		recurrenceCommand = "add hiking at BTH from 4pm to 5pm every month until 10 dec 2017";
		recurrenceRule = "Recurrence Rule: \"hiking at BTH\" every 1 month(s) until Sun 10 Dec 2017";
		addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, recurrenceRule), true);
		try {
			assertEquals(addSuccess, logic.handleInput(recurrenceCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}
		
	}

}
