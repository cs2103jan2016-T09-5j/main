

package testcases;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import logic.Todo;

public class TodoTest {
	
	String name;
	DateTime deadlineTime, startDate, endDate;
	ArrayList<DateTime> deadlineDateTimes, eventDateTimes;
	
	//@@author Morgan
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
