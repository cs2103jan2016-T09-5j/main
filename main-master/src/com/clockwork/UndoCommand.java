package com.clockwork;

import com.clockwork.exceptions.StateUndefinedException;

/**
 * Restores the last stored state in memory.
 */
public class UndoCommand extends Command{
	
	/**
	 * Creates an UndoCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */	
	public UndoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Reverses the last modifying operation.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		//check if the number of parameters is correct
		if(!(input.containsOnlyCommand() && input.containsEmptyParams())){
            return new Signal(Signal.UNDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.undo();
			memory.saveToFile();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.UNDO_SUCCESS, true);
	}
}
