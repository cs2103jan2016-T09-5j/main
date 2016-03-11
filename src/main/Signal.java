package main;

/**
 * The Signal Class is used as a container for the messages displayed to the
 * user at the end of an operation.
 * 
 * It also contains the formats for different signals.
 */ 

public class Signal {
	
    /**
     * Welcome Signal
     */
    public static final String WELCOME_SIGNAL = "Welcome to BlockWork!";

    /**
     * Add Command Signals
     */
    public static final String ADD_SUCCESS_SIGNAL_FORMAT = "%1$s successfully added.";
    public static final String ADD_UNKNOWN_ERROR = "Unknown add error";
    public static final String ADD_END_BEFORE_START_ERROR = "The start time is after the end time.";
    public static final String ADD_INVALID_PARAMS = "The number of parameters is invalid."
    		+ System.lineSeparator()
    		+ "\t Supported formats:"
    		+ System.lineSeparator()
    		+ "\t Floating tasks: add <title>"
    		+ System.lineSeparator()
    		+ "\t Deadlines: add <title> by/on/at <date>"
    		+ System.lineSeparator()
    		+ "\t Events: add <title> from <time> to <time> on <date>"
    		+ System.lineSeparator()
    		+ "\t\t add <title> from <date> to <date>";
    
    
    /**
     * Delete Command Signals
     */
    public static final String DELETE_SUCCESS_FORMAT = "%1$s successfully deleted.";
    public static final String DELETE_INVALID_PARAMS = "Error: The number of parameters is invalid."
			+ System.lineSeparator()
			+ "\t Supported format:"
			+ System.lineSeparator()
			+ "\t delete <indexNumber>";
   
    
    /**
     * Display Command Signals
     */
    public static final String DISPLAY_SUCCESS_SIGNAL = "";
    public static final String DISPLAY_EMPTY_SIGNAL = "The list is empty";
    public static final String DISPLAY_INVALID_PARAM = "The parameter %1$s is not recognized."
            + System.lineSeparator()
            + "The display command only supports only following additional parameters: c, completed, a, all. "
            + System.lineSeparator()
            + "Pending todos will be displayed by default, when no paramters are supplied. ";

    
    /**
     * Edit Command Signals
     */
    public static final String EDIT_SUCCESS_FORMAT = "%1$s successfully modified to %2$s";
    public static final String EDIT_INVALID_TIME = "Error: The start time must be before the end time.";
    public static final String EDIT_INVALID_PARAMS = "Error: The number or format of parameters is invalid."
    		+ System.lineSeparator()
    		+ "\t Supported format:"
    		+ System.lineSeparator()
    		+ "\t edit <indexNumber> [title <newTitle>] [start <newStartTime>] [end <newEndTime>]";

    
    /**
     * Exit Command Signals
     */
    public static final String EXIT_SUCCESS = "Exit successfuly.";
    public static final String EXIT_INVALLID_PARAMS = "Exit command should not be accompanied by additional paramters.";
    
    
    /**
     * Mark Command Signals
     */
    public static final String MARK_SUCCESS_SIGNAL_FORMAT = "%1$s successfully marked as done."; 
    public static final String MARK_UNKNOWN_ERROR = "Error: Unknown mark error.";
    public static final String MARK_INVALID_PARAMS = "Error: The number of parameters is invalid."
    			+ System.lineSeparator()
    			+ "\t Supported format:"
    			+ System.lineSeparator()
    			+ "\t mark <indexNumber>";
    
    
    /**
     * Undo Command Signals
     */
    public static final String UNDO_SUCCESS = "Undo operation successful.";
    public static final String UNDO_INVALID_PARAMS = "Error: The number of parameters is invalid."
    		+ System.lineSeparator()
    		+ "\t Supported format:"
    		+ System.lineSeparator()
    		+ "\t undo <indexNumber>";
    
    
    /**
     * Redo Command Signals
     */
    public static final String REDO_SUCCESS = "Redo operation successful.";
    public static final String REDO_INVALID_PARAMS = "Error: The number of parameters is invalid"
    		+ System.lineSeparator()
    		+ "\t Supported format:"
    		+ System.lineSeparator()
    		+ "\t redo <indexNumber>";
   
    
    /**
     * Search Command Signals
     */
    
    /**
     * Generic Signals
     */
    public static final String GENERIC_EMPTY_PARAM = "Error: At least one parameter is unspecified and empty.";
    public static final String GENERIC_INVALID_COMMAND_FORMAT = "Error: %1$s command is invalid!"
            + System.lineSeparator()
            + "Supported commands: add, mark, delete, edit, undo, etc...";
    public static final String GENERIC_DATE_UNDEFINED_FORMAT = "Error: Date is undefined; %1$s";
    public static final String GENERIC_EXCEPTIONS_FORMAT = "Error: %1$s";
    public static final String GENERIC_FATAL_ERROR = "Fatal error.";
    
    public static final String ERROR_PREFIX = "Error: ";
    
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

    public static boolean areParamsEqual(String[] params1, String[] params2){
    	//check if params1 and params2 are null
    	if(params1 == null && params2 == null){
    		return true;
    	} else if(params1 == null && params2 !=null) {
    		return false;
    	} else if(params1 != null && params2 == null){
    		return false;
    	}
    	
    	//Neither params1 nor params2 are null.
    	//Check for equal length
    	if(params1.length != params2.length){
    		return false;
    	}
    	//Every string in params1 is equal to every corresponding string in params2
    	int index = 0;
    	for(String str : params1){
    		if(!str.equals(params2[index])){
    			return false;
    		}
    		index++;
    	}
    	return true;
    }
    
    @Override
    //for unit testing purposes
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
    	final Signal other = (Signal) obj;
    	
        if (!(this.message == other.message)) {
    		return false;
    	}
    	return true;	
    }
 }
