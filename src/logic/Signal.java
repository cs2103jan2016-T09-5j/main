package logic;

/**
 * The Signal Class is used as a container for the messages displayed to the
 * user at the end of an operation.
 * 
 * It also contains the formats for different signals.
 * 
 *
 */
public class Signal {
	
	/**
	 * Welcome Signal
	 */
	public static final String WELCOME_SIGNAL = "Welcome to ClockWork!";

	/**
	 * Add Command Signals
	 */
	public static final String ADD_SUCCESS_SIGNAL_FORMAT = "Added %1$s";
	public static final String ADD_UNKNOWN_ERROR = "Unknown add error";
	public static final String ADD_END_BEFORE_START_ERROR = "Start and end time error";
	public static final String ADD_INVALID_RECURRING_ERROR = "Recurring todo error";
	public static final String ADD_INVALID_TODO_NAME_ERROR = "todo names error"
			+ System.lineSeparator() + "Flags: -d, -dt, -m, -r, -t, -y";
	public static final String ADD_INVALID_PARAMS = "Invalid parameters."
			+ System.lineSeparator()
			+ "\t Supported formats:"
			+ System.lineSeparator()
			+ "\t Floating tasks: add <name>"
			+ System.lineSeparator()
			+ "\t Deadlines: add <name> by/on/at <date>"
			+ System.lineSeparator()
			+ "\t Events: add <name> from <time> on <date> to <time> on <date>"
			+ System.lineSeparator() + "\t\t add <name> from <date> to <date>";

	/**
	 * Delete Command Signals
	 */
	public static final String DELETE_SUCCESS_FORMAT = "Deleted %1$s";
	public static final String DELETE_INVALID_PARAMS = "Invalid parameters"
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator() + "\t delete <indexNumber>";

	/**
	 * Display Command Signals
	 */
	public static final String DISPLAY_SUCCESS_SIGNAL = "";
    public static final String DISPLAY_EMPTY_TODO_SIGNAL = "Empty list";
    public static final String DISPLAY_EMPTY_RULE_SIGNAL = "Empty recurring";
	public static final String DISPLAY_ID_NOT_FOUND = "ID %1$s does not exist";
	public static final String DISPLAY_INVALID_PARAM = "Parameter %1$s is not recognized."
			+ System.lineSeparator()
            + "The display command only supports only following additional parameters: "
            + "c, completed, a, all, rule, [ID]. "
			+ System.lineSeparator()
			+ "Pending todos will be displayed by default, when no paramters are supplied. ";

	/**
	 * Edit Command Signals
	 */
	public static final String EDIT_SUCCESS_FORMAT = "Edited %1$s to %2$s";
	public static final String EDIT_RULE_SUCCESS_FORMAT = "Edited %1$s to %2$s";
	public static final String EDIT_END_BEFORE_START = "Start and end time error";
	public static final String EDIT_INVALID_DATE = "Date(s) specified is/are invalid";
	public static final String EDIT_NO_LONGER_RECURS = "Recurring no longer exists.";
	public static final String EDIT_LIMIT_BEFORE_NOW = "Limit before current time and date error";
	public static final String EDIT_INVALID_PARAMS = "The number or format of parameters is invalid."
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator()
			+ "\t edit <id> [<newName>] [from <newStartTime>] [to <newEndTime>] [by; on; at <newDeadline>] [every <interval>] [until <limit>]"
			+ System.lineSeparator()
			+ "\t modifications of the recurrence interval requires specification of event period or deadline.";

	/**
	 * Exit Command Signals
	 */
	public static final String EXIT_SUCCESS = "Exit successfully.";
	public static final String EXIT_INVALLID_PARAMS = "Exit command should not be accompanied by additional paramters.";

	/**
	 * Mark Command Signals
	 */
	public static final String MARK_SUCCESS_SIGNAL_FORMAT = "Marked %1$s successfully";
	public static final String MARK_UNKNOWN_ERROR = "Unknown mark error.";
	public static final String MARK_INVALID_PARAMS = "Parameters is invalid."
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator() + "\t mark <indexNumber>";

	/**
	 * Undo Command Signals
	 */
	public static final String UNDO_SUCCESS = "Undo successful.";
	public static final String UNDO_INVALID_PARAMS = "Parameters is invalid."
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator() + "\t undo";

	/**
	 * Redo Command Signals
	 */
	public static final String REDO_SUCCESS = "Redo successful.";
	public static final String REDO_INVALID_PARAMS = "Parameters is invalid"
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator() + "\t redo";

	/**
	 * Search Command Signals
	 */

	public static final String SEARCH_SUCCESS_SIGNAL = "Search found";
	public static final String SEARCH_EMPTY_SIGNAL = "Search No result found.";
	public static final String SEARCH_INVALID_PARAMS = "Parameters is invalid"
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator()
			+ "\t search [<keyword>] [ -n <keyword> ] [ -dt <dateKeyword> ] [ -d <dayKeyword] [-t <timeKeyword>] [-m <monthKeyword>]";
	
	/**
	 * Time Clash Signals
	 */
	public static final String CLASH_DOES_NOT_EXIST = "";
	public static final String CLASH_DEADLINE_DOES_EXIST = "";
	public static final String CLASH_EVENT_DOES_EXIST = "Clash tasks deteched \"%1$s\"";
	public static final String CLASH_CONTINUE_PROPOSITION = "";
	public static final String CLASH_USER_OVERRIDE = "";
	public static final String CLASH_USER_VOID_TASK = "The entry has not been added, remove using 'delete' conflict to continue,";

	/**
	 * Generic Signals
	 */
	public static final String GENERIC_EMPTY_PARAM = "Parameter is unspecified.";
	public static final String GENERIC_INVALID_COMMAND_FORMAT = "Invalid command %1$s"
			+ System.lineSeparator()
			+ "Supported commands: add, mark, delete, edit, undo, etc...";
	public static final String GENERIC_FATAL_ERROR = "Fatal error.";

	public static final String ERROR_PREFIX = "Error: ";

	public static final String DATE_PARSING_ERROR = "Unable to parse dates: Try rephrasing your dates ";

	private String message;

	/**
	 * Constructor for Signal
	 * 
	 * @param signal
	 */
	public Signal(String signal, boolean isSuccessful) {
		this.message = signal;
		if (!isSuccessful) {
			this.message = ERROR_PREFIX.concat(this.message);
		}
	}

	@Override
	public String toString() {
		return message;
	}

	public static boolean areParamsEqual(String[] params1, String[] params2) {
		// check if params1 and params2 are null
		if (params1 == null && params2 == null) {
			return true;
		} else if (params1 == null && params2 != null) {
			return false;
		} else if (params1 != null && params2 == null) {
			return false;
		}

		// Neither params1 nor params2 are null.
		// Check for equal length
		if (params1.length != params2.length) {
			return false;
		}
		// Every string in params1 is equal to every corresponding string in
		// params2
		int index = 0;
		for (String str : params1) {
			if (!str.equals(params2[index])) {
				return false;
			}
			index++;
		}
		return true;
	}

	@Override
	// for unit testing purposes
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
		final Signal other = (Signal) obj;

		if (!(this.message.equals(other.message))) {
			return false;
		}
		return true;
	}
}
