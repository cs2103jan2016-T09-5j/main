package logic;
import exceptions.NullTodoException;
import parser.ParsedInput;
import storage.Memory;

//@@author Rebekah
/**
 * This class handles all user input with "mark" as the first keyword with the
 * format of mark <index>. It retrieves a Todo object from memory at the given
 * index, marks the Todo as done and replaces the existing copy in memory.
 */

public class MarkCommand extends Command {
	
	/**
	 * Creates a MarkCommand object.
	 * 
	 * @param input
	 *            the ParsedInput object containing the parameters.
	 * @param memory
	 *            the memory containing the Todos to which the changes should be
	 *            committed.
	 */
	public MarkCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Retrieves the Todo object specified by index in ParsedInput from Memory
	 * and marks it as done.
	 * 
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute() {
		// Ensure that there is only one KeyParamPair in inputList
		if (!input.containsOnlyCommand()) {
			return new Signal(Signal.MARK_INVALID_PARAMS, false);
		}

		if (input.containsEmptyParams()) {
			return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		try {
			int index = Integer.parseInt(keyParamPairs.get(0).getParam());
			Todo todoToMark = memory.getToModifyTodo(index);
			todoToMark.setDone(true);
			memory.saveToFile();
			return new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT,
					todoToMark), true);
		} catch (NullTodoException e) {
			return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
			return new Signal(String.format(Signal.MARK_INVALID_PARAMS), false);
		}
	}
}
