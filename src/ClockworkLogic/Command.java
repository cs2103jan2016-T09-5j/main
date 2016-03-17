package ClockworkLogic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import ClockworkParser.KeyParamPair;
import ClockworkParser.ParsedInput;
import ClockworkStorage.Memory;

public abstract class Command {
	
	ParsedInput input;
	Memory memory;
	ArrayList<KeyParamPair> keyParamPairs;
	List<DateTime> dateTimes;
	
	/**
	 * Constructs a new Command object with the given parameters and reference
	 * to the memory that stores the Todos.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public Command(ParsedInput input, Memory memory) {
		this.keyParamPairs = input.getParamPairs();
		this.dateTimes = input.getDateTimes();
		this.input = input;
		this.memory = memory;
	}

	public abstract Signal execute();

}
