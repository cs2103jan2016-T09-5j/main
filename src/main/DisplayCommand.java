package main;

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

public class DisplayCommand extends Command {
	
	public DisplayCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	// Signals for whether to display pending or completed todos
	private static final int showPending = 0;
	private static final int showCompleted = 1;
	private static final int showAll = 2;

	// Messages for different categories based on completeness
	private static final String messagePending = "Pending:";
	private static final String messageCompleted = "Completed:";
	private static final String messageAll = "All:";

	// Messages for different types of todos (event, deadline, floating)
	private static final String messageEvent = "E ";
	private static final String messageDeadline = "D ";
	private static final String messageFloating = "F ";

	// Max length for the title of todo to be displayed
	private static final int MAX_CHAR = 15;

	private static final String eventFormat = "%1$-2s %2$-7s %3$-25s %4$s - %5$s";
	private static final String deadLineFormat = "%1$-2s %2$-7s %3$-25s %4$s";
	private static final String floatingFormat = "%1$-2s %2$-7s %3$-25s";

    private static DateTime now = new DateTime();
    private static DateTime inOneDay = new DateTime().plusDays(1);

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
			.forPattern("dd MMM");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");

    private static PeriodFormatter formatter = new PeriodFormatterBuilder()
            .appendPrefix(" in ").appendHours().appendSuffix(" hours ")
            .appendMinutes()
            .appendSuffix(" minutes ")
            .printZeroNever().toFormatter();

	@Override
	public Signal execute() {
		String displayString;
		Collection<Todo> todos = memory.getAllTodos();
		if (todos.size() == 0) {
            return new Signal(Signal.DISPLAY_EMPTY_SIGNAL, true);
		}
		String param = keyParamPairList.get(0).getParam();
		if (param.equals("completed") || param.equals("complete")
				|| param.equals("c")) {
			displayString = getDisplayChrono(todos, showCompleted);
			System.out.println(displayString);
		} else if (param.equals("all") || param.equals("a")) {
			displayString = getDisplayChrono(todos, showAll);
			System.out.println(displayString);
        } else if (param.isEmpty()) {
			// By default we show pending tasks, in chronological order
			displayString = getDisplayChrono(todos, showPending);
			System.out.println(displayString);
        } else {
            return new Signal(
                    String.format(Signal.DISPLAY_INVALID_PARAM, param), false);
        }
        return new Signal(Signal.DISPLAY_SUCCESS_SIGNAL, true);
	}

	public static String getDisplayChrono(Collection<Todo> todos, int signal) {
        ArrayList<Todo> clonedTodos = cloneTodos(todos);
        // By default, we order the todos in chronological order
        Collections.sort(clonedTodos, new ChronoComparator());
		return getDisplayDefault(clonedTodos, signal);
	}

	private static ArrayList<Todo> cloneTodos(Collection<Todo> todos) {
		ArrayList<Todo> clonedTodos = new ArrayList<Todo>(todos.size());
		for (Todo todo : todos) {
			clonedTodos.add(new Todo(todo));
		}
		return clonedTodos;
	}

    public static String getDisplayDefault(Collection<Todo> todos, int signal) {
		Iterator<Todo> iterator = todos.iterator();
		StringBuilder sBuilder = new StringBuilder();

		if (signal == showAll) {
			sBuilder.append(messageAll + System.lineSeparator());
		} else if (signal == showCompleted) {
			sBuilder.append(messageCompleted + System.lineSeparator());
		} else if (signal == showPending) {
			sBuilder.append(messagePending + System.lineSeparator());
		}

		while (iterator.hasNext()) {
			Todo todo = iterator.next();
			appendTodo(signal, sBuilder, todo);
		}
		return sBuilder.toString();
	}

	private static void appendTodo(int signal, StringBuilder sBuilder, Todo todo) {
		// Show pending, skip the completed tasks
		if (signal == showPending && todo.isDone()) {
			return;
		}

		// Show completed, skip the pending tasks
		if (signal == showCompleted && !todo.isDone()) {
			return;
		}

		if (todo.getStartTime() != null && todo.getEndTime() != null) {
			sBuilder.append(formatEvent(todo));
		} else if (todo.getEndTime() != null) {
			sBuilder.append(formatDeadline(todo));
		} else if (todo.getStartTime() == null && todo.getEndTime() == null) {
			sBuilder.append(formatFloatingTask(todo));
		}
	}

	private static String formatFloatingTask(Todo todo) {
		String title = todo.getTitle();
		title = messageFloating + shortenTitle(title);
		String id = String.valueOf(todo.getId());
		return String.format(floatingFormat, id, "", title)
				+ System.lineSeparator();
	}

	private static String formatDeadline(Todo todo) {
		String title = todo.getTitle();
		title = messageDeadline + shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime endTime = todo.getEndTime();
        String endDateString = formatDateForDisplay(endTime);
        String endTimeString = formatTimeForDisplay(endTime);
		return String.format(deadLineFormat, id, endDateString, title,
				endTimeString) + System.lineSeparator();
	}

	private static String formatEvent(Todo todo) {
		String title = todo.getTitle();
		title = messageEvent + shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime startTime = todo.getStartTime();
		DateTime endTime = todo.getEndTime();
        String startDateString = formatDateForDisplay(startTime);
        String startTimeString = formatTimeForDisplay(startTime);
        String endTimeString = formatTimeForDisplay(endTime);
		return String.format(eventFormat, id, startDateString, title,
				startTimeString, endTimeString) + System.lineSeparator();
	}

	static class ChronoComparator implements Comparator<Todo> {

		@Override
		public int compare(Todo o1, Todo o2) {
			// Floating tasks with no time will be sorted in lexicographical
			// order
			if (o1.getDateTime() == null && o2.getDateTime() == null) {
				return o1.getTitle().compareTo(o2.getTitle());
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
        String periodString = "";
        if (time.isAfter(now) && time.isBefore(inOneDay)) {
            Period period = new Period(now, time);
            periodString = formatter.print(period);
        }

        String timeString = TimeFormatter.print(time).concat(periodString);
        return timeString;
    }

    private static String formatDateForDisplay(DateTime time) {
        String dateString = DateFormatter.print(time);
        return dateString;
    }

	public static void main(String[] args) {
        Collection<Todo> todos;

        // try {
        Main.handleInput("add floating task");

        Main.handleInput("add CS3230 deadline on 9 March 9pm");

        Main
                .handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

        Main
                .handleInput("add new year from 1 January at 10am to 1 January at 11am");

        Main.handleInput("add CS1010 deadline by 3 Feb at 10pm");

        Main.handleInput("add read floating books");

        Main.handleInput("add CS3243 project deadline by 7 March at 9am");

        Main.handleInput("add CS3333 project 2 on 7 Apr 10am");

        Main.handleInput("mark 0");

        Main.handleInput("mark 2");

        todos = Main.memory.getAllTodos();
        
        System.out.println(DisplayCommand.getDisplayChrono(todos, showAll));

        System.out.println(DisplayCommand.getDisplayChrono(todos, showPending));

        System.out.println(DisplayCommand
				.getDisplayChrono(todos, showCompleted));

	}
}
