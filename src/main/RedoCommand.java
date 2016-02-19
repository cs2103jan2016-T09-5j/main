package main;

/**
 * Houses a method which processes the redo request from the user. 
 */
public class RedoCommand extends Command {

	public RedoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Processes a ParsedInput object containing the delete command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		int numberOfKeywords = keyParamPairList.size();
		if(numberOfKeywords != 1){
            return new Signal(Signal.REDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.restoreFutureState();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.REDO_SUCCESS, true);
	}
}
