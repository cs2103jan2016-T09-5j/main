package main;
/**
 * Restores the last stored state in memory.
 */
public class UndoCommand extends Command{
	
	public UndoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
	public Signal execute() {
		//check if the number of parameters is correct
		int numberOfKeywords = keyParamPairList.size();
		if(numberOfKeywords != 1){
            return new Signal(Signal.UNDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.restoreHistoryState();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.UNDO_SUCCESS, true);
	}
}
