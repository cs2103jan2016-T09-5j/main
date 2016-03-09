package main;

/**
 * Houses a method which processes the delete request from the user.
 */
public class DeleteCommand extends Command {

	public DeleteCommand(ParsedInput input, Memory memory) {
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
		if(input.containsEmptyParams()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}
		
		//Check for valid number of keywords
    	int numberOfKeywords = keyParamPairList.size();
    	if(numberOfKeywords > 1){
            return new Signal(Signal.DELETE_INVALID_PARAMS, false);
    	}
    	
    	int deleteIndex;
    	Todo deleted;
		try {
			deleteIndex = Integer.parseInt(keyParamPairList.get(0).getParam());
			deleted = memory.remove(deleteIndex);
		} catch (NumberFormatException e) {
            return new Signal(Signal.DELETE_INVALID_PARAMS, false);
		} catch (NullTodoException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(String.format(Signal.DELETE_SUCCESS_FORMAT, deleted),
                true);
	}

}
