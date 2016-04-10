package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.NullTodoException;
import parser.ParsedInput;
import storage.Memory;

//@@author Regine
public class DisplayCommand extends Command {

    private static Logger logger = LoggerFactory
            .getLogger(DisplayCommand.class);

    private static final boolean LOGGING = false;

	// Parameters allowed for display command
    private static final String PARAM_ALL_1 = "all";
    private static final String PARAM_ALL_2 = "a";
    private static final String PARAM_COMPLETE_1 = "completed";
    private static final String PARAM_COMPLETE_2 = "complete";
    private static final String PARAM_COMPLETE_3 = "c";
    private static final String PARAM_RULE = "rule";

	// Signals for whether to display pending or completed todos
	private static final int showPending = 0;
	private static final int showCompleted = 1;
	private static final int showAll = 2;
	
    private static final String MESSAGE_RULES = "";

	// Max length for the title of todo to be displayed
    private static final int MAX_CHAR = 30;
    private static final int TOTAL_LENGTH = 20;

    // Decoration for date section heading
    private static final String DATE_DECO = ".";

    // Empty field text
    private static final String EMPTY_FIELD = "NIL";

    // String formats
    private static final String eventFormat = " %1$s ~ %2$s ~ %3$s - %4$s";
    private static final String deadLineFormat = " %1$s ~ %2$s ~ %3$s";
    private static final String floatingFormat = " %1$s ~ %2$s ~ %3$s";

    private static DateTime inOneDay = new DateTime().plusDays(1);

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
            .forPattern("EEE dd MMM yyyy");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");

    private static final String RELATIVE_PERIOD_PREFIX = " in ";
    private static final String FLOATING_TASK_HEADING = "Anytime";
    
    // Relative timing format
    private static PeriodFormatter formatter = new PeriodFormatterBuilder()
            .appendHours().appendSuffix("h ")
            .printZeroNever().appendMinutes().appendSuffix("min ")
            .printZeroNever().toFormatter();
    //For GUI display
    private static  ArrayList<String> arrListForGUI = new ArrayList<String> ();
    
    /**
     * Creates a DisplayCommand object.
     * 
     * @param input the ParsedInput object containing the parameters.
     * @param memory the memory containing the Todos to which the changes should
     *            be committed.
     */
    public DisplayCommand(ParsedInput input, Memory memory) {
    	super(input, memory);
    }
    protected static ArrayList<String> getArrListForGUI(){
    	return arrListForGUI;
    }
    public static void clearArrListForGUI(){
    	arrListForGUI.clear();
    }

    @Override
	public Signal execute() {
		String displayString;
		Collection<Todo> todos = memory.getAllTodos();
		if (todos.size() == 0) {
            return new Signal(Signal.DISPLAY_EMPTY_TODO_SIGNAL, true);
		}

        // Check that display command only have one key param pair
        if (keyParamPairs.size() > 1) {
            String param = keyParamPairs.get(1).getParam();
            return new Signal(
                    String.format(Signal.DISPLAY_INVALID_PARAM, param), false);
        }

		String param = keyParamPairs.get(0).getParam();
        if (param.isEmpty()) {
            // By default we show pending tasks, in chronological order
            displayDefault();
        } else if (param.equals(PARAM_COMPLETE_1) || param.equals(PARAM_COMPLETE_2)
				|| param.equals(PARAM_COMPLETE_3)) {
            displayString = getDisplayChrono(showCompleted);
            arrListForGUI.add(displayString);
		} else if (param.equals(PARAM_ALL_1) || param.equals(PARAM_ALL_2)) {
            displayString = getDisplayChrono(showAll);
            arrListForGUI.add(displayString);
        } else if (param.equals(PARAM_RULE)) {
            Collection<RecurringTodoRule> rules = memory.getAllRules();
            // Display message if there are no rules
            if (rules.isEmpty()) {
                return new Signal(Signal.DISPLAY_EMPTY_RULE_SIGNAL, true);
            }
            displayString = getDisplayForRules(rules);
        } else {
            // Try to parse the param as the id of a specific todo to show
            // the detail of the todo

            try {
                int id = Integer.parseInt(param);
                Todo todo = memory.getTodo(id);
                displayString = todo.toString();
                arrListForGUI.add(displayString);
                System.out.println(displayString);
            } catch (NullTodoException e) {
                return new Signal(String.format(Signal.DISPLAY_ID_NOT_FOUND,
                        param), false);
            } catch (NumberFormatException e) {
                return new Signal(String.format(Signal.DISPLAY_INVALID_PARAM,
                        param), false);
            }

        }
        return new Signal(Signal.DISPLAY_SUCCESS_SIGNAL, true);
	}

    private String getDisplayForRules(Collection<RecurringTodoRule> rules) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(MESSAGE_RULES + System.lineSeparator());

        Iterator<RecurringTodoRule> iterator = rules.iterator();
        while (iterator.hasNext()) {
            sBuilder.append(System.lineSeparator());
            RecurringTodoRule recurringTodoRule = (RecurringTodoRule) iterator
                    .next();
            sBuilder.append(recurringTodoRule.getDisplayString());

        }
        return sBuilder.toString();
    }

    public void displayDefault() {
        displayDefault(memory);
    }

    public static void displayDefault(Memory memory) {
        String displayString;
        displayString = getDisplayChrono(memory, showPending);
        arrListForGUI.add(displayString);
        //System.out.println(displayString);
    }

    public static String getDisplayChrono(Memory memory, int signal) {
        Collection<Todo> todos = memory.getAllTodos();
        ArrayList<Todo> clonedTodos = cloneTodos(todos);
        // By default, we order the todos in chronological order
        Collections.sort(clonedTodos, new ChronoComparator());
        return getDisplayString(clonedTodos, signal);
    }

    public static String getDisplayChrono(ArrayList<Todo> todos, int signal) {
        ArrayList<Todo> clonedTodos = cloneTodos(todos);
        // By default, we order the todos in chronological order
        Collections.sort(clonedTodos, new ChronoComparator());
        return getDisplayString(clonedTodos, signal);
    }

    public String getDisplayChrono(int signal) {
        Collection<Todo> todos = memory.getAllTodos();
        ArrayList<Todo> clonedTodos = cloneTodos(todos);
        // By default, we order the todos in chronological order
        Collections.sort(clonedTodos, new ChronoComparator());
		return getDisplayString(clonedTodos, signal);
	}

	private static ArrayList<Todo> cloneTodos(Collection<Todo> todos) {
		ArrayList<Todo> clonedTodos = new ArrayList<Todo>(todos.size());
		for (Todo todo : todos) {
			clonedTodos.add(todo.copy());
		}
		return clonedTodos;
	}

    public static String getDisplayString(Collection<Todo> todos, int signal) {
        // Break down the events that spans across several days
        todos = breakDownLongEvents(todos);

		Iterator<Todo> iterator = todos.iterator();
		StringBuilder sBuilder = new StringBuilder();
        DateTime currentDate = new DateTime(0);

		while (iterator.hasNext()) {
			Todo todo = iterator.next();
            if (LOGGING) {
                logger.info("adding todo {} into display", todo.getName());
            }

            // Show pending, skip the completed tasks
            if (signal == showPending && todo.isDone()) {
                continue;
            }
            // Show completed, skip the pending tasks
            if (signal == showCompleted && !todo.isDone()) {
                continue;
            }
            DateTime todoDateTime = todo.getDateTime();
            currentDate = appendDateIfNew(sBuilder, currentDate, todoDateTime);
            appendTodo(sBuilder, todo);
		}
		return sBuilder.toString();
	}

    /**
     * This method handles the events that spans over a few days and break them
     * down into smaller todos within one day for display purposes
     * 
     * @param todos
     *            todos to be broken down
     * @return todos with long events broken down into shorter ones which are
     *         within a single day
     */
    private static Collection<Todo> breakDownLongEvents(Collection<Todo> todos) {
        ArrayList<Todo> shortTodos = new ArrayList<Todo>();
        for (Todo todo : todos) {
            if (todo.isEvent()
                    && !todo.isSameDayEvent()) {
                shortTodos.addAll(todo.breakIntoShortEvents());
            } else {
                shortTodos.add(todo);
            }
        }
        Collections.sort(shortTodos, new ChronoComparator());
        return shortTodos;
    }


    /**
     * Append the date if the date is a new one and has not been displayed yet
     * 
     * @param sBuilder
     * @param currentDate
     * @param todoDateTime
     * @return the updated current date
     */
    private static DateTime appendDateIfNew(StringBuilder sBuilder,
            DateTime currentDate, DateTime todoDateTime) {
        if (!dateAlreadyDisplayed(currentDate, todoDateTime)) {
            // Date not displayed yet, update currentDate and display the
            // date
            currentDate = todoDateTime;
            appendDate(sBuilder, todoDateTime);
        }
        return currentDate;
    }

    /**
     * Check if the date is already displayed
     * 
     * currentDate will be set to null for the first floating task, and a
     * heading for floating task will be displayed.
     * 
     * Subsequent floating tasks will not have the heading displayed when the
     * currentDate is null.
     * 
     * @param currentDate
     * @param dateTime
     * @return	true if date has already been displayed, false otherwise.
     */
    private static boolean dateAlreadyDisplayed(DateTime currentDate,
            DateTime dateTime) {
        if (currentDate == null) {
            // null currentDate indicates that floating task heading has
            // already been displayed, no other headings will be displayed
            return true;
        }
        if (dateTime == null) {
            // null dateTime indicates that this is a floating task
            return false;
        }
        if (isSameDay(currentDate, dateTime)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isSameDay(DateTime date1, DateTime date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return (date1.getDayOfYear() == date2.getDayOfYear() && date1
                .getYear() == date2.getYear());
    }

    /**
     * Append the date before the task details if the date is not already
     * appended.
     * 
     * Each date will be displayed only once for all tasks on that date.
     * 
     * Floating tasks will have a heading in place of the date.
     * 
     * @param sBuilder
     * @param todo
     */
    private static void appendDate(StringBuilder sBuilder, DateTime dateTime) {
        // Add empty spaces in front
        sBuilder.append("");
        if (dateTime == null) {
            // Floating task, add heading
            sBuilder.append(addDecoForDate(FLOATING_TASK_HEADING)
                    + System.lineSeparator());
        } else {
            sBuilder.append(addDecoForDate(formatDateForDisplay(dateTime))
                    + System.lineSeparator());
        }
    }

    private static String addDecoForDate(String s) {
        return StringUtils.center(s, TOTAL_LENGTH, DATE_DECO);
    }
    
    private static void appendTodo(StringBuilder sBuilder, Todo todo) {
		if (todo.getStartTime() != null && todo.getEndTime() != null) {
			sBuilder.append(formatEvent(todo));
		} else if (todo.getEndTime() != null) {
			sBuilder.append(formatDeadline(todo));
		} else if (todo.getStartTime() == null && todo.getEndTime() == null) {
			sBuilder.append(formatFloatingTask(todo));
		}
	}

	private static String formatFloatingTask(Todo todo) {
		String title = todo.getName();
        title = shortenTitle(title);
		String id = String.valueOf(todo.getId());
        return String.format(floatingFormat, id, title, EMPTY_FIELD)
				+ System.lineSeparator();
	}

	private static String formatDeadline(Todo todo) {
		String title = todo.getName();
        title = shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime endTime = todo.getEndTime();
        String endTimeString = formatTimeForDisplay(endTime);
        return String.format(deadLineFormat, id, title,
				endTimeString) + System.lineSeparator();
	}

	private static String formatEvent(Todo todo) {
		String title = todo.getName();
        title = shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime startTime = todo.getStartTime();
		DateTime endTime = todo.getEndTime();
        String startTimeString = formatTimeForDisplay(startTime);
        String endTimeString = formatTimeForDisplay(endTime);
        return String.format(eventFormat, id, title,
				startTimeString, endTimeString) + System.lineSeparator();
	}

	static class ChronoComparator implements Comparator<Todo> {

		@Override
		public int compare(Todo o1, Todo o2) {
			// Floating tasks with no time will be sorted in lexicographical
			// order
			if (o1.getDateTime() == null && o2.getDateTime() == null) {
				return o1.getName().compareTo(o2.getName());
			}
			// If only one todo has time, the other with no time will be sorted
			// to the back
			if (o1.getDateTime() == null) {
				return 1;
			} else if (o2.getDateTime() == null) {
				return -1;
			} else {
				// Both have time, compare directly
				return o1.getDateTime().compareTo(o2.getDateTime());
			}
		}
	}

	private static String shortenTitle(String title) {
		int maxLength = (title.length() < MAX_CHAR) ? title.length() : MAX_CHAR;
		title = title.substring(0, maxLength);
		return title;
	}

    private static String formatTimeForDisplay(DateTime time) {
        DateTime now = new DateTime();
        String periodString = "";
        if (time.isAfter(now) && time.isBefore(inOneDay)) {
            Period period = new Period(now, time);
            periodString = RELATIVE_PERIOD_PREFIX + formatter.print(period);
        }

        String timeString = TimeFormatter.print(time).concat(periodString);
        return timeString;
    }

    private static String formatDateForDisplay(DateTime time) {
        String dateString = DateFormatter.print(time);
        return dateString;
    }

    static class StringUtils {

        public static String center(String s, int size) {
            return center(s, size, " ");
        }

        public static String center(String s, int size, String pad) {
            if (pad == null)
                throw new NullPointerException("pad cannot be null");
            if (pad.length() <= 0)
                throw new IllegalArgumentException("pad cannot be empty");
            if (s == null || size <= s.length())
                return s;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (size - s.length()) / 2; i++) {
                sb.append(pad);
            }
            sb.append(s);
            while (sb.length() < size) {
                sb.append(pad);
            }
            return sb.toString();
        }
    }

}
