package exceptions;

//@@author Prem
public final class ExceptionMessages {
	/**
	 * Exception messages. Used to construct new exceptions.
	 */
	public static final String NULL_TODO_EXCEPTION = "Specified Todo does not exist.";
	public static final String NULL_RULE_EXCEPTION = "Specified Recurring Todo rule does not exist.";
	public static final String NOT_RECURRING_EXCEPTION = "Todo specified is not a recurring Todo.";
	public static final String NO_HISTORY_STATES = "No undoable states exist";
	public static final String NO_FUTURE_STATES = "No redoable states exist";
	public static final String DATE_UNDEFINED_EXCEPTION = "Date String is empty or does not contain dates.";
	public static final String INVALID_SEARCH_TYPE = "Specified search type does not exist.";
}