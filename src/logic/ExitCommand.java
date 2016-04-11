package logic;

import parser.ParsedInput;
import storage.Memory;

//@@author A0133247L
public class ExitCommand extends Command {
	
	/**
	 * Creates an ExitCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public ExitCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
    public Signal execute() {
        String param = keyParamPairs.get(0).getParam();
        if (!param.isEmpty()) {
            return new Signal(Signal.EXIT_INVALLID_PARAMS, false);
        }
        
        memory.onDestroy();
        memory.saveToFile();
        return new Signal(Signal.EXIT_SUCCESS, true);
    }
}
