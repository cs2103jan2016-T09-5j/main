

package clockwork.parser;

public enum Keywords {
	// Commands
	ADD,
	MARK,
	DELETE,
	SEARCH,
	EDIT,
	DISPLAY, 
	UNDO,
	REDO,
	EXIT,
	
	// Keywords for parameters in ADD command
	BY,
	FROM,
	ON,
	AT,
	EVERY,
	UNTIL,
	IN,
	
	// Keywords for parameters in EDIT command, not in InputStringKeyword maps
	TO,
	
	// Keywords for DELETE command for RecurringTodoRules
	RULE,
	
	//Keywords for parameters in SEARCH command
	NAME,
	DATE,
	TIME,
	DAY,
	MONTH,
	YEAR,
	
	// Keyword for error
	ERROR;
}