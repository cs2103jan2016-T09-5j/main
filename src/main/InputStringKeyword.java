package main;
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
	private static final String KEY_TITLE = "title";
	private static final String KEY_START = "start";
	private static final String KEY_END = "end";
    private static final String KEY_EXIT = "exit";

    private static Map<String, KEYWORDS> keywords;
    private static Map<String, KEYWORDS> commands;

    static {
    	commands = new HashMap<String, KEYWORDS>();
    	commands.put(KEY_ADD, KEYWORDS.ADD);
        commands.put(KEY_MARK, KEYWORDS.MARK);
        commands.put(KEY_DELETE, KEYWORDS.DELETE);
        commands.put(KEY_SEARCH, KEYWORDS.SEARCH);
        commands.put(KEY_EDIT, KEYWORDS.EDIT);
        commands.put(KEY_DISPLAY, KEYWORDS.DISPLAY);
        commands.put(KEY_UNDO, KEYWORDS.UNDO);
        commands.put(KEY_REDO, KEYWORDS.REDO);
        commands.put(KEY_EXIT, KEYWORDS.EXIT);
    	
        keywords = new HashMap<String, KEYWORDS>(commands);
        keywords.put(KEY_BY, KEYWORDS.BY);
        keywords.put(KEY_FROM, KEYWORDS.FROM);
        keywords.put(KEY_ON, KEYWORDS.ON);
        keywords.put(KEY_AT, KEYWORDS.AT);
        keywords.put(KEY_TITLE, KEYWORDS.TITLE);
        keywords.put(KEY_START, KEYWORDS.START);
        keywords.put(KEY_END, KEYWORDS.END);
    }

    public static boolean isKeyword(String s) {
        return keywords.containsKey(s);
    }

    public static KEYWORDS getKeyword(String s) {
        return keywords.get(s);
    }
    
    public static boolean isCommand(String s) {
    	return commands.containsKey(s);
    }
    
    public static KEYWORDS getCommand(String s) {
    	return commands.get(s);
    }
}
