package main;

/**
 * This class handles all user input with "mark" as the first keyword with the format of mark <index>.
 * It retrieves a Todo object from memory at the given index, marks the Todo as done and replaces the 
 * existing copy in memory.
 */

public class MarkCommand extends Command {
	
	public MarkCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Retrieves the Todo object specified by index in ParsedInput from Memory
	 * and marks it as done.
	 * 
	 * @param input
	 *            A ParsedInput object that contains a KEYWORDS type and an
	 *            ArrayList<KeyParamPair>
	 * @param memory
	 *            A memory object that stores Todo objects
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute() {
		if(input.containsEmptyParams()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		// Ensure that there is only one KeyParamPair in inputList
        if (keyParamPairList.size() > 1) {
            return new Signal(Signal.MARK_INVALID_PARAMS, false);
		}
		
        if (keyParamPairList.get(0).getParam().isEmpty()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		try {
			int index = Integer.parseInt(keyParamPairList.get(0).getParam());
			Todo todoToMark = memory.setterGet(index);
			todoToMark.setDone(true);
            return new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT,
                    todoToMark), true);
		} catch (NullTodoException e) {
            return new Signal(String.format(Signal.GENERIC_EXCEPTIONS_FORMAT,
                    e.getMessage()), false);
		} catch (NumberFormatException e) {
            return new Signal(
String.format(Signal.MARK_INVALID_PARAMS),
                    false);
		}
	}
}
