package com.clockwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.clockwork.exceptions.InvalidRecurringException;
import com.clockwork.exceptions.InvalidTodoNameException;
import com.clockwork.exceptions.ParsingFailureException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClockworkGUI extends Application {

	public static final int DEFAULT_WINDOW_WIDTH = 600;
	public static final int DEFAULT_WINDOW_HEIGHT = 400;

	public static final Text welcomeText = new Text("Welcome, Username");
	public static final Text userCommandDisplayText = new Text("Command: ");
	public static final Text userCommandInputText = new Text("Type Command: ");
	
	public static final ArrayList
	<String> commandList = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo, Etc"));
	public static final ArrayList<String> taskList = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));
	public static ArrayList<String> displayList = new ArrayList<String>(
			Arrays.asList("Add 12th Jan 2016", "Delete 12, Testing", "Mark 3", "Undo"));
	
	//themes the user can call to change
	public static final String customTheme = new String();
	
	//logic part 
	private static ClockworkGUI logic;
	public static Scanner scn = new Scanner(System.in);
	private static String fileDirectory;
	public static StorageHandler storage;
	public static Memory memory;

	public ClockworkGUI() {
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		memory.onCreate();
		Parser.initialize();
	}
	
	public ClockworkGUI(String fileDir) {
		fileDirectory = fileDir;
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		Parser.initialize();
	}
	
	public static ClockworkGUI getInstance() {
		if (logic == null) {
			logic = new ClockworkGUI();
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
	 * The main logic unit of Zeitgeist. Reads the input from Zeitgeist and
	 * passes it to the Parser, the first element in the flow of component calls
	 * present in all operations. And also launch the UI
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

		Application.launch(args);
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

	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		Scene scene;
		switch (customTheme) {
		  case "DARK":
		        scene = darkTheme();
		        break;
		  default:
		       	scene = defaultTheme();
		        break;
		}
		window.setScene(scene);
		window.show();
	}
	
	public Scene darkTheme() {
		return null;
	}

	public Scene defaultTheme(){
		BorderPane defaultLayout = new BorderPane();
		
		defaultLayout.setPadding(new Insets(10));

		/** Formatting Bottom Region 
		 * Takes in ArrayList<String> displayList to display commands the User has entered
		 * To-be-implemented: Can get Text type of what user keyed into TextBox for 
		 * integration with Logic Component
		 */
		GridPane userDisplayAndInputSection = addDisplayAndInputSection(displayList);
		defaultLayout.setBottom(userDisplayAndInputSection);

		/** Formatting Top Region */
		HBox welcomeSection = addWelcomeSection(welcomeText);
		defaultLayout.setTop(welcomeSection);

		/** Formatting Left Region 
		 * Takes in ArrayList<String> commandList to display commands available
		 */
		VBox commandSection = addCommandSection(commandList);
		defaultLayout.setLeft(commandSection);

		/** Formatting Center Region 
		 * Takes in ArrayList<String> taskList to display User Tasks*/
		VBox taskSection = addTaskSection(taskList);
		defaultLayout.setCenter(taskSection);

		/** Formatting Right Region */
		Button btnRight = new Button("Calendar Widget Goes Here~");
		defaultLayout.setRight(btnRight);

		/** Setting Default Scene */
		Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		return defaultScene;
	}


	/** Add User Display and Input in Bottom Row
	 *
	 */
	private GridPane addDisplayAndInputSection(ArrayList<String> list) {
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		/** display user input */
		ObservableList<String> displayObsList = FXCollections.observableList(list);
		ListView<String> displayListView = new ListView<String>(displayObsList);
		displayListView.setItems(displayObsList);
		displayListView.setPrefSize(600, 250);
		displayListView.setEditable(false);
		displayListView.setCellFactory(TextFieldListCell.forListView());
		
		/** for user to key input */
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		//defining the user input field
		final TextField userInput = new TextField();
		userInput.setPrefColumnCount(10);
		userInput.getText();
		
		
		userInput.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	        	ClockworkGUI logic = getInstance();
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	if ((userInput.getText() != null && !userInput.getText().isEmpty())) {
	            		System.out.println("This is what you typed: " + userInput.getText());
	            		displayList.add(userInput.getText());
	            		SignalHandler.printCommandPrefix();
	            		String input = userInput.getText();
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
	            	userInput.clear();
	            }
	            
	        }
	    });
		
		grid.getChildren().add(userInput);
		
		grid.getRowConstraints().add(new RowConstraints(5)); // row 0 is 5 high
		grid.getRowConstraints().add(new RowConstraints(100)); // row 0 is 100 high
		grid.getRowConstraints().add(new RowConstraints(10)); // row 1 is 10 high
		grid.getRowConstraints().add(new RowConstraints(30)); // row 1 is 30 high

		GridPane.setConstraints(userCommandDisplayText, 0, 0); // column=0 row=0
		GridPane.setConstraints(displayListView, 0, 1); // column=0 row=1
		GridPane.setConstraints(userCommandInputText, 0, 2); // column=0 row=2
		GridPane.setConstraints(userInput, 0, 3);

		grid.getChildren().addAll(userCommandDisplayText, displayListView, userCommandInputText);

		return grid;
	}
	
	/** Add Task List in the Center */
	private VBox addTaskSection(ArrayList<String> taskList) {
		VBox taskBox = new VBox();
		taskBox.setPadding(new Insets(10, 12, 15, 12));
		taskBox.setSpacing(10);
		taskBox.setStyle("-fx-background-color: #FFFFFF;");

		ObservableList<String> taskObsList = FXCollections.observableList(taskList);
		ListView<String> taskListView = new ListView<String>(taskObsList);
		taskListView.setItems(taskObsList);

		taskListView.setPrefSize(70, 150);
		
		taskBox.getChildren().addAll(taskListView);
		
		return taskBox;
	}

	/** Add Command List in Left Column */
	public VBox addCommandSection(ArrayList<String> commandList) {
		VBox commandBox = new VBox();
		commandBox.setPadding(new Insets(5, 12, 15, 12));
		commandBox.setSpacing(10);
		commandBox.setStyle("-fx-background-color: #FFFFFF;");

		Text helpTextHeader = new Text("Help: ");

		ObservableList<String> commandObsList = FXCollections.observableList(commandList);
		ListView<String> commandListView = new ListView<String>(commandObsList);
		commandListView.setItems(commandObsList);

		commandListView.setPrefSize(70, 150);

		commandBox.getChildren().addAll(helpTextHeader, commandListView);

		return commandBox;
	}

	/** Add Welcome Message in Top Row */
	public HBox addWelcomeSection(Text welcomeText) {
		HBox welcomeBox = new HBox();
		welcomeBox.setPadding(new Insets(10, 12, 10, 12));
		welcomeBox.setSpacing(5);
		welcomeBox.setStyle("-fx-background-color: #336699;");

		// Ignore font style for now
		// welcomeText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		welcomeText.setFill(Color.WHITE);
		// welcomeText.setStroke(Color.web("#7080A0"));
		welcomeBox.getChildren().add(welcomeText);

		return welcomeBox;
	}
}