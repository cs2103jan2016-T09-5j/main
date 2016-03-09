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
	/** Application window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 600;
	private static final int DEFAULT_WINDOW_HEIGHT = 400;

	/** Static messages to display */
	private static final Text welcomeText = new Text("Welcome to Clockwork (:");
	private static final Text helpText = new Text("Help: ");
	private static final Text userCommandText = new Text("Command: ");
	private static final Text userInputText = new Text("Type Command: ");
	
	/** Static variables */
	private static final ArrayList<String> prevCommandsList = new ArrayList<String>();
	private static final ArrayList<String> currentInputList = new ArrayList<String>();
	private static final ArrayList<String> helpList = new ArrayList<String>();
	private static final ArrayList<String> taskList = new ArrayList<String>();
	private static BorderPane defaultLayout = new BorderPane();
	private static Scene defaultScene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	private String rawInputString = new String();
	
	/** Test arrays to display */
	private static final ArrayList<String> helpListTest = new ArrayList<String>(
			Arrays.asList("Add", "Delete", "Undo", "Search", "Display", "Mark", "Edit"));
	private static final ArrayList<String> taskListTest = new ArrayList<String>(
			Arrays.asList("Do Meeting Notes", "Have fun with CS2103"));
	
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
	 * The main logic unit of ClockWork. Reads the input from ClockWork and
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
		setDisplayRegions(defaultLayout);
		window.setScene(defaultScene);
		window.show();
	}
	
	/** Set top, left, center, right and bottom region to display information */
	private void setDisplayRegions(BorderPane defaultLayout) {
		setBottomRegion(defaultLayout);
		setTopRegion(defaultLayout);
		setLeftRegion(defaultLayout);
		setCenterRegion(defaultLayout);
		setRightRegion(defaultLayout);	
	}
	
	/** Set top region to display welcome text*/
	private void setTopRegion(BorderPane defaultLayout) {
		HBox topSection = displayWelcomeText(welcomeText);
		defaultLayout.setTop(topSection);
	}
	
	/** Set left region to display help list */
	private void setLeftRegion(BorderPane defaultLayout) {
		VBox leftSection = displayHelpList(helpListTest);
		defaultLayout.setLeft(leftSection);
	}
	
	/** Set center region to display task list */
	private void setCenterRegion(BorderPane defaultLayout) {
		VBox centerSection = displayTaskList(taskListTest);
		defaultLayout.setCenter(centerSection);
	}
	
	/** Set right region to display calendar widget [INCOMPLETE]*/
	private void setRightRegion(BorderPane defaultLayout) {
		Button rightSection = new Button("INSERT CALENDAR WIDGET");
		defaultLayout.setRight(rightSection);
	}
	
	/** Set bottom region to display user command and input section*/
	private void setBottomRegion(BorderPane defaultLayout) {
		GridPane bottomSection = setBottomSection(prevCommandsList);
		defaultLayout.setBottom(bottomSection);
	}
	
	/*
	* ===========================================
	* Private Class Methods
	* ===========================================
	*/
	
	/** 
	 * Display previous commands entered by user in top subsection, and take in input
	 * in bottom subsection in the bottom section
	 * 
	 * @param prevCommandsList		Previous test commands entered to show top sub-section
	 * 								in bottom section
	 * @return bottomSection		Bottom section to display previous commands and 
	 * 								for user to input text
	 */
	private GridPane setBottomSection(ArrayList<String> prevCommandsList) {
		GridPane grid = new GridPane();
		formatBottomSection(grid);
		
		ListView<String> prevCommandsListView = setTopSubsection(prevCommandsList);
		final TextField userInput = setBottomSubsection();
		
		grid.getChildren().add(userInput);
		grid.getChildren().addAll(userCommandText, prevCommandsListView, userInputText);
		
		return grid;
	}
	/** 
	 * Display welcome message in top section [Static message, for now]
	 * 
	 * @param welcomeText			Text to welcome user in top section
	 * @return topSection			Top section to display welcome text
	 */
	private HBox displayWelcomeText(Text welcomeText) {
		HBox topSection = new HBox();
		
		styleTopSection(welcomeText, topSection);
		
		topSection.getChildren().add(welcomeText);
		
		return topSection;
	}
	
	/** 
	 * Display help list in left section
	 * 
	 * @param helpList				Help list of commands to show in left section
	 * @return leftSection			Left section to display help list
	 */
	private VBox displayHelpList(ArrayList<String> helpList) {
		VBox leftSection = new VBox();
		
		ListView<String> helpListView = formatArrayList(helpList);
		
		styleLeftSection(leftSection, helpListView);
		
		leftSection.getChildren().addAll(helpText, helpListView);
		return leftSection;
	}
	
	/** 
	 * Display task list in center section
	 * 
	 * @param taskList				Task list to show in center section
	 * @return centerSection		Left section to display task list
	 */
	private VBox displayTaskList(ArrayList<String> taskList) {
		VBox centerSection = new VBox();
		
		ListView<String> taskListView = formatArrayList(taskList);
		
		styleCenterSection(centerSection, taskListView);
		
		centerSection.getChildren().addAll(taskListView);
		return centerSection;
	}
	
	/** 
	 * Display previous commands entered by user in top subsection in bottom section
	 * 
	 * @param prevCommandsList		Previous user commands for display in ArrayList
	 * @return prevCommandsListView	Previous user commands for display in ListView
	 */
	private ListView<String> setTopSubsection(ArrayList<String> prevCommandsList) {
		ListView<String> prevCommandsListView = formatArrayList(prevCommandsList);
		prevCommandsListView.setPrefSize(600, 250);
		prevCommandsListView.setCellFactory(TextFieldListCell.forListView());
		
		GridPane.setConstraints(userCommandText, 0, 0); // column=0 row=0
		GridPane.setConstraints(prevCommandsListView, 0, 1); // column=0 row=1
		
		return prevCommandsListView;
	}
	/** 
	 * Input user command in bottom subsection in bottom section
	 * 
	 * @return TextField			Text field for user to input commands
	 */
	private TextField setBottomSubsection() {
		final TextField userInput = new TextField();
		userInput.getText();	
		handleUserInput(userInput);
		
		GridPane.setConstraints(userInputText, 0, 2); // column=0 row=2
		GridPane.setConstraints(userInput, 0, 3); // column=0 row=3
		
		return userInput;
	}
	/** 
	 * Handle event after enter is pressed
	 * 
	 * @param userInput				User input to be handled
	 */
	private void handleUserInput(final TextField input) {
		input.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	        	//IGNORE FOR NOW - You can try seeing how this part works, not sure if you'll need
	        	//it for the function to see if the user input matches the help list so you can
	        	//change it later
//	        	currentInputList.add(userInput.getText());
//	        	System.out.println(currentInputList);
//	        	System.out.println(userInput.getText());
	        	//End Comment
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	if ((input.getText() != null && !input.getText().isEmpty())) {
	            		handleUserEnter(input.getText());
		            	input.clear();
	            	}
	            } 
	        }
	    });
	}
	//*********** REGINE WORK ON THIS PART! ************************
		private void handleUserEnter(String userInput){
			rawInputString = userInput;
			prevCommandsList.add(userInput);
			//Start Comment - Call Logic API here to handle the String user entered.
			//Eg. ('Cos idk the actual API you're gonna call here)
//			clockWork.handleCommand(rawInputString);
			//, where handleCommand is the logic API that processes the string and returns me 
			//the arraylist of tasks I should display on the GUI. That means that
			//the prevCommandsList should not be stored in the GUI, but it should be in logic.
			//taskListTest.add("NEW TASK!");
			//End Comment
			refresh();
			//System.out.println("This is what you typed: " + userInput);
			ClockworkGUI logic = getInstance();
			SignalHandler.printCommandPrefix();
    		String input = userInput;
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
		/** Refreshes the scene so that updated lists and variables can be shown */
		private void refresh(){
			setDisplayRegions(defaultLayout);
		}
		
		/** 
		 * Set list format from ArrayList to ListView so that list can be seen on GUI
		 * 
		 * @param arrayList				List of type ArrayList String 
		 * @return listView				List of type ListView String 
		 */
		private ListView<String> formatArrayList(ArrayList<String> arrayList) {
			ObservableList<String> obsList = FXCollections.observableList(arrayList);
			ListView<String> listView = new ListView<String>(obsList);
			listView.setItems(obsList);
			return listView;
		}
		/** 
		 * Format grid section in bottom section for displaying previous user commands 
		 * and user input
		 * 
		 * @param grid					Grid in bottom section to be formatted
		 */
		private void formatBottomSection(GridPane grid) {
			/** to display previous commands */
			grid.setHgap(5);
			grid.setVgap(5);
			grid.setPadding(new Insets(5, 5, 5, 5));
			
			/** for user to key input */
			grid.setPadding(new Insets(10, 10, 10, 10));
			grid.setVgap(5);
			grid.setHgap(5);
			
			grid.getRowConstraints().add(new RowConstraints(5)); // row 0 is 5 high
			grid.getRowConstraints().add(new RowConstraints(100)); // row 0 is 100 high
			grid.getRowConstraints().add(new RowConstraints(10)); // row 1 is 10 high
			grid.getRowConstraints().add(new RowConstraints(30)); // row 1 is 30 high
		}
		
		//STYLING SECTIONS
		
		/** Styling center section containing task list */
		private void styleCenterSection(VBox centerSection, ListView<String> taskListView) {
			centerSection.setPadding(new Insets(10, 12, 15, 12));
			centerSection.setSpacing(10);
			centerSection.setStyle("-fx-background-color: #FFFFFF;");
			taskListView.setPrefSize(70, 150);
		}

		/** Styling left section containing help list */
		private void styleLeftSection(VBox leftSection, ListView<String> commandListView) {
			leftSection.setPadding(new Insets(5, 12, 15, 12));
			leftSection.setSpacing(10);
			leftSection.setStyle("-fx-background-color: #FFFFFF;");
			commandListView.setPrefSize(80, 150);
		}
		
		/** Styling top section containing welcome text */
		private void styleTopSection(Text welcomeText, HBox topSection) {
			topSection.setPadding(new Insets(10, 12, 10, 12));
			topSection.setSpacing(5);
			topSection.setStyle("-fx-background-color: #336699;");
			welcomeText.setFill(Color.WHITE);
		}
}