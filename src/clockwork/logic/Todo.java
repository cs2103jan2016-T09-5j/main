package clockwork.logic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import clockwork.exceptions.ExceptionMessages;
import clockwork.exceptions.NotRecurringException;

/**
 * Stores parameters of a Todo using org.joda.time.DateTime objects. A Todo can
 * be subdivided into 3 different subtypes namely Task, Deadline, or Event,
 * which is uniquely determined at construction by the availability of
 * parameters. Todos are uniquely specified identifier known as ID until their
 * deletion, upon which the ID may be recycled.
 *
 */

public class Todo implements UndoableRedoable<Todo> {

	public enum TYPE {
		TASK, DEADLINE, EVENT;
	}

	protected int id;
	protected String name;
	protected DateTime createdOn;
	protected DateTime modifiedOn, startTime, endTime;
	protected boolean isDone;
	protected TYPE type;
	protected Integer recurringId;

	protected static final DateTimeFormatter DateFormatter = DateTimeFormat
			.forPattern("EEE dd MMM yyyy");
	protected static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");
	protected static final String DateTimeStringFormat = "%1$s at %2$s";

	protected static final String EventStringFormat = "Event \"%1$s\" from %2$s to %3$s";

	protected static final String DeadlineStringFormat = "Deadline \"%1$s\" by %2$s";

	protected static final String FloatingTaskStringFormat = "Floating task \"%1$s\"";

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
