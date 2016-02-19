package main;

/**
 * This is where it all begins.
 */
import java.util.Scanner;

public class Main {

    public static Scanner scn = new Scanner(System.in);;
    public static Memory memory = new Memory();

	public static void main(String[] args) {
        SignalHandler.printSignal(new Signal(Signal.WELCOME_SIGNAL, true));
		String input = scn.nextLine();
		while (true) {
            handleInput(input);
			input = scn.nextLine();
		}
	}

    public static void handleInput(String input) {
        handleInput(memory, input);
    }

    private static void handleInput(Memory memory, String input) {
        ParsedInput c = InputStringParser.parse(input);
        dispatchCommand(c, memory);
    }

	private static void dispatchCommand(ParsedInput userInput, Memory memory) {
		Signal processSignal;
		Command c;

		KEYWORDS commandType = userInput.getType();
		if (commandType == null) {
            SignalHandler.printSignal(new Signal(String
                    .format(Signal.GENERIC_INVALID_COMMAND_FORMAT, ""), false));
		} else {

            switch (commandType) {
                case ADD :
                	c = new AddCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case DELETE :
                	c = new DeleteCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case MARK :
                	c = new MarkCommand(userInput, memory);
                	processSignal = c.execute();
                    break;
				
                case REDO :
                	c = new RedoCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case UNDO :
                	c = new UndoCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case EDIT :
                	c = new EditCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case DISPLAY :
                	c = new DisplayCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case SEARCH :
                	c = new SearchCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case EXIT :
                	c = new ExitCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                default :
                	// NOTE: This case should never happen
                    processSignal = new Signal(Signal.GENERIC_FATAL_ERROR,
                            false);
                    System.exit(-1);
                    break;
			}
            SignalHandler.printSignal(processSignal);
		}
	}
}
