package main;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Stores parameters of a Todo using org.joda.time.DateTime objects. A Todo can
 * be subdivided into 3 different subtypes namely Task, Deadline, or Event,
 * which is uniquely determined at construction by the availability of
 * parameters. Todos are uniquely specified identifier known as ID until
 * their deletion, upon which the ID may be recycled.
 */
public class Todo{
	
	public enum TYPE {
		TASK, DEADLINE, EVENT;
	}

	private final int id;
	private String title;
	private final DateTime createdOn;
	private DateTime modifiedOn, startTime, endTime;
	private boolean isDone;
	private TYPE type;

    private static final DateTimeFormatter DateFormatter = DateTimeFormat
            .forPattern("dd MMM");
    private static final DateTimeFormatter TimeFormatter = DateTimeFormat
            .forPattern("HH:mm");
    private static final String DateTimeStringFormat = "%1$s at %2$s";

    private static final String EventStringFormat = "Event \"%1$s\" from %2$s to %3$s";

    private static final String DeadlineStringFormat = "Deadline \"%1$s\" by %2$s";

    private static final String FloatingTaskStringFormat = "Floating task \"%1$s\"";
	
	/**
	 * Constructs a Todo of type: TASK.
	 * 
	 * @param userTitle title of the task.
	 * @throws DateUndefinedException 
	 */
	public Todo(Memory memory, String titleString) throws DateUndefinedException {
		this.id = memory.obtainFreshId();
		this.title = titleString;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
		this.type = TYPE.TASK;
	}
	
	/**
	 * Constructs a Todo of type: DEADLINE or EVENT.
	 * 
	 * @param userTitle title of the task.
	 * @throws DateUndefinedException 
	 */
	public Todo(Memory memory, String titleString, String dateString) throws DateUndefinedException {
		this.id = memory.obtainFreshId();
		this.title = titleString;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.isDone = false;
		List<DateTime> dateList = parseDates(dateString);
		if(dateList.size() == 1) {
			this.startTime = null;
			this.endTime = dateList.get(0);
			this.type = TYPE.DEADLINE;
		} else if(dateList.size() == 2) {
			this.startTime = dateList.get(0);
			this.endTime = dateList.get(1);
			this.type = TYPE.EVENT;
		} // Catch DateList more than 2 DateTimes (optional)
	}

	/**
	 * Makes an exact copy of another Todo.
	 * 
	 * @param todo the Todo to be copied.
	 */
	protected Todo(Todo todo) {
		this.id = todo.id;
		this.title = todo.title;
		this.createdOn = todo.createdOn;
		this.modifiedOn = todo.modifiedOn;
		this.startTime = todo.startTime;
		this.endTime = todo.endTime;
		this.isDone = todo.isDone;
		this.type = todo.type;
	}
	
	/**
	 * Constructs a placeholder Todo with null fields except the ID. To be
	 * used by Memory class in its stacks for undo/redo operations.
	 * 
	 * @param id the ID of the Todo that was removed from Memory.
	 */
	private Todo(int id) {
		this.id = id;
		this.title = null;
		this.createdOn = null;
		this.modifiedOn = null;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
		this.type = null;
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
	 * Returns the title of the Todo.
	 * 
	 * @return the title of the Todo.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Replaces the title with the specified String and updates the last modified field of the Todo.
	 * 
	 * @param title the new title of the Todo.
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * startTimeString and updates the last modified field of the Todo.
	 * 
	 * @param startTime String containing the new start time of the Todo.
	 * @throws DateUndefinedException if startTimeString does not contain a valid date, is empty, or null
	 */
	public void setStartTime(String startTimeString) throws DateUndefinedException {
		List<DateTime> dateList = parseDates(startTimeString); // Catch dateList has more than 1 DateTime
		this.startTime = dateList.get(0);
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
	 * Replaces the end time with the specified DateTime and updates the last modified field of the Todo.
	 * 
	 * @param endTime String containing the new end time of the Todo.
	 * @throws DateUndefinedException if endTimeString does not contain a valid date, is empty, or null
	 */
	public void setEndTime(String endTimeString) throws DateUndefinedException {
		List<DateTime> dateList = parseDates(endTimeString); // Catch dateList has more than 1 DateTime
		this.endTime = dateList.get(0);
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
	 * Marks the Todo as done or undone and updates the last modified field.
	 * 
	 * @param isDone the new status of the Todo.
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
	
	/**
	 * Returns the placeholder Todo constructed from the ID of this Todo.
	 * 
	 */
	protected Todo getPlaceholder() {
		return new Todo(id);
	}
	
	protected boolean isValid() {
		if(startTime != null && endTime != null) {
			type = TYPE.EVENT;
			if(endTime.isBefore(startTime)) {
				return false;
			}
		} else if(startTime == null && endTime != null) {
			type = TYPE.DEADLINE;
		} else if(startTime == null && endTime == null) {
			type = TYPE.TASK;
		}
		return true;
	}
	
	/**
	 * Parses a String with multiple dates provided to the DateParser, and returns a DateTime array.
	 * 
	 * @param dateString String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates processed in the string.
	 * @throws DateUndefinedException if dateString does not contain a valid date, is empty, or null
	 */
	private static List<DateTime> parseDates(String dateString) throws DateUndefinedException {
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			DateGroup parsedDate = parser.parse(dateString).get(0);
			List<Date> dateList = parsedDate.getDates();
			for(Date date : dateList) {
				dateTimeList.add(new DateTime(date));
			}
		} catch (IndexOutOfBoundsException e) {
			throw new DateUndefinedException(ExceptionMessages.UNDEFINED_DATE_STRING_EXCEPTION);
		} catch (NullPointerException e) {
			throw new DateUndefinedException(ExceptionMessages.NULL_DATE_STRING_EXCEPTION);
		}
		return dateTimeList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
        String endDateTimeString = formatDateTime(endTime);
        String startDateTimeString = formatDateTime(startTime);

        switch (type) {
            case TASK :
                return String.format(FloatingTaskStringFormat, title);
            case DEADLINE :
                return String.format(DeadlineStringFormat, title,
                        endDateTimeString);
            case EVENT :
                return String.format(EventStringFormat, title,
                        startDateTimeString, endDateTimeString);
            default :
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
     * Method to return a DateTime of the todo for ordering them chronologically
     * The order of preference: start time > end time > null
     * For events, start time will be returned
     * For deadlines, end time will be returned
     * For floating todos, null will be returned
     * @return DateTime object
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
    
    /**
     * Method to compare two DateTime objects at the minute resolution
     * Returns -1 if the first object is smaller (earlier)
     * Returns 0 if the two objects are equal
     * Returns 1 if the first object is larger(later)
     * @return int
     */
    public int compareDateTime(DateTime first, DateTime second){
    	DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfDay());
    	return comparator.compare(first, second);
    }
	
	/**
	 * Overriding the equals method. 
	 * Compares the title, startTime, endTime and isDone parameters between this Todo object
	 * and the other Todo object being compared to.
	 * 
	 * Returns true if the parameters being compared to are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(this.getClass() != obj.getClass()){
			return false;
		}
		final Todo other = (Todo) obj;
		
		//Construct a DateTimeComparator comparing DateTime objects at the minute resolution.
		//If the argument passed into the compare method is null, it will be treated as DateTime.now
		//Thus null checks must be done beforehand
		DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfDay());
		
		//Comparing startTime. If it is null in both objects, treat as equal.
		if((this.startTime == null) && (other.startTime != null)){
			return false;
		}
		else if((this.startTime != null) && (other.startTime == null)){
			return false;
		}
		else if((this.startTime != null) && (other.startTime != null)){
			if(comparator.compare(this.startTime, other.startTime) != 0){
				return false;
			}
		}
				
		//Comparing endTime. If it is null in both objects, treat as equal.
		if((this.endTime == null) && (other.endTime != null)){
			return false;
		}
		else if((this.endTime != null) && (other.endTime == null)){
			return false;
		}
		else if((this.endTime != null) && (other.endTime != null)){
			if(comparator.compare(this.endTime, other.endTime) != 0){
				return false;
			}
		}
		
		//Comparing title
				if(!this.title.equals(other.title)){
					return false;
				}
				
		//Comparing isDone
				if(this.isDone != other.isDone){
					return false;
				}
				
		return true;
	}
}
