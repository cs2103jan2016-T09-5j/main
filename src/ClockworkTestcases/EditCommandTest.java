package ClockworkTestcases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ClockworkExceptions.InvalidRecurringException;
import ClockworkExceptions.InvalidTodoNameException;
import ClockworkExceptions.NullTodoException;
import ClockworkExceptions.ParsingFailureException;
import ClockworkLogic.ClockWork;
import ClockworkLogic.RecurringTodoRule;
import ClockworkLogic.Signal;
import ClockworkLogic.Todo;

public class EditCommandTest {

	ClockWork logic;
	String nameFloating1 = " Arrange meetup";
	String nameFloating2 = " Meetup with friends";
	String nameDeadline1 = " Report submission";
	String nameDeadline2 = " Reflection submission";
	String nameEvent1 = " Dinner with Family";
	String nameEvent2 = " Dinner with extended family";
	String nameRecurring1 = " Read news";
	String nameRecurring2 = " Go to church";
	
	String date1 = " 18 May";
	int date1Day = 18;
	int date1Month = 5;
	
	String date2 = " 20 August";
	int date2Day = 20;
	int date2Month = 8;
	
	String time1 = " 2.30pm";
	int time1Hour = 14;
	int time1Min = 30;
	
	String time2 = " 4pm";
	int time2Hour = 16;
	int time2Min = 0;
	
	int year = new DateTime().getYear();
	
	String period1 = " day";
	Period period1P = new Period().withDays(1);
	String period2 = " week";
	Period period2P = new Period().withWeeks(1);
	
	String limit = " 30 December";
	int limitDay = 30;
	int limitMonth = 12;
	
	String add = "add";
	String edit = "edit";
	
	String by = " by";
	String from = " from";
	String to = " to";
	String on = " on";
	String every = " every";
	String until = " until";
	String rFlag = " -r";
	
	String idStringFloat = " 0";
	int idFloat = 0;
	String idStringDeadline = " 1";
	int idDeadline = 1;
	String idStringEvent = " 2";
	int idEvent = 2;
	String idStringRecurringTodo = " 3";
	int idRecurringTodo = 3;
	
	int idRecurring = 0;
	
	Todo floating;
	Todo deadline;
	Todo event;
	RecurringTodoRule rule;
	
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
