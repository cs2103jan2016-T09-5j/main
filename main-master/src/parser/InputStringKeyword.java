package parser;
import java.util.HashMap;
import java.util.Map;

public class InputStringKeyword {
	
	private static final String KEY_ADD = "add";
	private static final String KEY_MARK = "mark";
	private static final String KEY_DELETE = "delete";
	private static final String KEY_SEARCH = "search";
	private static final String KEY_EDIT = "edit";
	private static final String KEY_DISPLAY = "display";
	private static final String KEY_UNDO = "undo";
	private static final String KEY_REDO = "redo";
	private static final String KEY_BY = "by";
	private static final String KEY_FROM = "from";
	private static final String KEY_ON = "on";
	private static final String KEY_AT = "at";
	private static final String KEY_IN = "in";
	private static final String KEY_EVERY = "every";
	private static final String KEY_UNTIL = "until";
	private static final String KEY_EXIT = "exit";
	private static final String KEY_RULE_ABV = "-r";
	private static final String KEY_NAME_ABV = "-n";
	private static final String KEY_DATE_ABV = "-dt";
	private static final String KEY_TIME_ABV = "-t";
	private static final String KEY_DAY_ABV = "-d";
	private static final String KEY_MONTH_ABV = "-m";
	private static final String KEY_YEAR_ABV = "-y";

	
    private static Map<String, Keywords> keywords;
    private static Map<String, Keywords> commands;
    private static Map<String, Keywords> flags;

    static {
    	commands = new HashMap<String, Keywords>();
    	commands.put(KEY_ADD, Keywords.ADD);
        commands.put(KEY_MARK, Keywords.MARK);
        commands.put(KEY_DELETE, Keywords.DELETE);
        commands.put(KEY_SEARCH, Keywords.SEARCH);
        commands.put(KEY_EDIT, Keywords.EDIT);
        commands.put(KEY_DISPLAY, Keywords.DISPLAY);
        commands.put(KEY_UNDO, Keywords.UNDO);
        commands.put(KEY_REDO, Keywords.REDO);
        commands.put(KEY_EXIT, Keywords.EXIT);
        
        flags = new HashMap<String, Keywords>();
        flags.put(KEY_NAME_ABV, Keywords.NAME);
        flags.put(KEY_DATE_ABV, Keywords.DATE);
        flags.put(KEY_TIME_ABV, Keywords.TIME);
        flags.put(KEY_DAY_ABV, Keywords.DAY);
        flags.put(KEY_MONTH_ABV, Keywords.MONTH);
        flags.put(KEY_YEAR_ABV,  Keywords.YEAR);
        flags.put(KEY_RULE_ABV,  Keywords.RULE);
        
        keywords = new HashMap<String, Keywords>(commands);
        keywords.putAll(flags);
        keywords.put(KEY_BY, Keywords.BY);
        keywords.put(KEY_FROM, Keywords.FROM);
        keywords.put(KEY_ON, Keywords.ON);
        keywords.put(KEY_AT, Keywords.AT);
        keywords.put(KEY_IN, Keywords.IN);
        keywords.put(KEY_EVERY, Keywords.EVERY);
        keywords.put(KEY_UNTIL, Keywords.UNTIL);
        
    }

    /**
     * Checks if the String encodes a keyword
     * 
     * @param s String to be checked
     * @return 
     */
    public static boolean isKeyword(String s) {
        return keywords.containsKey(s.toLowerCase());
    }

    /**
     * Retrieves the enum constant that represents the keyword encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the command.
     * 		enum error if string is not a keyword
     */
    public static Keywords getKeyword(String s) {
    	Keywords keyword = keywords.get(s.toLowerCase());
    	if(keyword == null) {
    		return Keywords.ERROR;
    	}
    	return keyword;
    }
    
    /**
     * Checks if the String encodes a command.
     * 
     * @param s
     * @return
     */
    public static boolean isCommand(String s) {
    	return commands.containsKey(s.toLowerCase());
    }
    
    /**
     * Retrieves the enum constant that represents the command encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the command.
     * 		enum error if string is not a command
     */
    public static Keywords getCommand(String s) {
    	Keywords command = commands.get(s.toLowerCase());
    	if(command == null) {
    		return Keywords.ERROR;
    	}
    	return command;
    }
    
    /**
     * Checks if the String encodes a flag.
     * 
     * @param s
     * @return
     */
    public static boolean isFlag(String s) {
    	return flags.containsKey(s.toLowerCase());
    }
    
    /**
     * Retrieves the enum constant that represents the flag encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the flag.
     * 		enum error if string is not a flag
     */
    public static Keywords getFlag(String s) {
    	Keywords flag = flags.get(s.toLowerCase());
    	if(flag == null) {
    		return Keywords.ERROR;
    	}
    	return flag;
    }

	/**
	 * Checks if the String encodes a rule flag.
	 * @param keyString
	 * @return
	 */
	public static boolean isRule(String keyString) {
		return keyString.toLowerCase().equals(KEY_RULE_ABV);
	}
}
