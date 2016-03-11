package com.clockwork;

import java.util.Scanner;

import com.clockwork.exceptions.InvalidRecurringException;
import com.clockwork.exceptions.InvalidTodoNameException;
import com.clockwork.exceptions.ParsingFailureException;

public class ClockWork {
	private static ClockWork logic;
	private static String fileDirectory;
	
	public static Scanner scn = new Scanner(System.in);
	public static StorageHandler storage;
	public static Memory memory;
	
	public ClockWork() {
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		memory.onCreate();
		Parser.initialize();
	}
	
	public ClockWork(String fileDir) {
		fileDirectory = fileDir;
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		Parser.initialize();
	}
	
	public static ClockWork getInstance() {
		if (logic == null) {
			logic = new ClockWork();
		}
		return logic;
	}
	
   
    /**
     * Clears the user's console, OS-dependent implementation
     */
	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				for (int i = 0; i < 105; i++) {
					System.out.println();
				}
			} else {
				final String ANSI_CLS = "\u001b[2J";
				final String ANSI_HOME = "\u001b[H";
				System.out.print(ANSI_CLS + ANSI_HOME);
				System.out.flush();
			}
		} catch (final Exception e) {
			System.out.println("error in clearing");
		}
	}

    
	/**
	 * The main logic unit of ClockWork. Reads the input from ClockWork and
	 * passes it to the Parser, the first element in the flow of component calls
	 * present in all operations.
	 * 
	 * @param args
	 *            contains arguments from the command line at launch. (Not used)
	 */
	public static void main(String[] args) {

		fileDirectory = getStorageFileDirFromSettings();
		// Check if a file directory path is passed in through argument
		if (args.length == 1) {
			// Check if file directory path is valid
			String customFileDirPath = args[0];
			fileDirectory = StorageUtils.processStorageDirectory(customFileDirPath);
		}

		String input;
		ClockWork logic = getInstance();
		SignalHandler.printSignal(new Signal(Signal.WELCOME_SIGNAL, true));
		while (true) {
			SignalHandler.printCommandPrefix();
			input = scn.nextLine();
			clearConsole();
			Signal signal;
			try {
				signal = logic.handleInput(input);
			} catch (InvalidRecurringException e) {
				signal = new Signal(Signal.ADD_INVALID_RECURRING_ERROR, false);
			} catch(InvalidTodoNameException e) {
				signal = new Signal(Signal.ADD_INVALID_TODO_NAME_ERROR, false);
			} catch (ParsingFailureException e) {
				signal = new Signal(Signal.DATE_PARSING_ERROR, false);
			}
			SignalHandler.printSignal(signal);
		}
	}

	public Signal handleInput(String input) throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		ParsedInput c = Parser.parseInput(input);
		return execute(c);
	}

	
	
	/**
	 * Creates a Command object with the given ParsedInput and executes it.
	 * 
	 * @param userInput
	 *            input from user, parsed by the Parser.
	 * @return a Signal containing a message to be printed, denoting success or
	 *         failure of the execution.
	 */
	private Signal execute(ParsedInput userInput) {
		Signal processSignal;
		Command c;

		Keywords commandType = userInput.getType();
		if (commandType == Keywords.ERROR) {
			return new Signal(String.format(
					Signal.GENERIC_INVALID_COMMAND_FORMAT, ""), false);
		} else {

			switch (commandType) {
                case ADD :
                    c = new AddCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case DELETE :
                    c = new DeleteCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case MARK :
                    c = new MarkCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case REDO :
                    c = new RedoCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case UNDO :
                    c = new UndoCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
                    break;

                case EDIT :
                    c = new EditCommand(userInput, memory);
                    processSignal = c.execute();
                    DisplayCommand.displayDefault(memory);
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

			return processSignal;
		}
	}
	
	public void deleteStorageFile() {
		StorageHandler.deleteStorageFileIfExists(fileDirectory);
	}
	
	public void reloadMemory() {
		memory = new Memory();
		memory.setStorageHandler(storage);
	}
	
	public static String getStorageFileDirFromSettings(){
		return StorageUtils.readSettingsFile();
	}
}
