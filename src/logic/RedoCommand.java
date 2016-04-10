package logic;

import exceptions.StateUndefinedException;
import parser.ParsedInput;
import storage.Memory;

//@@author Prem
/**
 * Houses a method which processes the redo request from the user. 
 *
 */

public class RedoCommand extends Command {	
	
	/**
	 * Creates a RedoCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public RedoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Reverses the last undo operation.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {	
		if(!(input.containsOnlyCommand() && input.containsEmptyParams())){
            return new Signal(Signal.REDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.redo();
			memory.saveToFile();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.REDO_SUCCESS, true);
	}
}
