package userinterface;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.ClockWork;
import storage.StorageUtils;

public class Main extends Application {
	
	/** Application window dimensions */
	private static int WIDTH_WINDOW_DEFAULT = 900;
	private static int HEIGHT_WINDOW_DEFAULT = 600;

	/** Static variables */
	private static ArrayList<String> _taskList = new ArrayList<String>();
	private static DefaultLayout mainBorderPane;
	private static HelpLayout helpBorderPane;
	private static CalendarLayout calBorderPane;
	private static String _feedback;
	private static Scene scene;
	private static Stage stage;
	
	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		initialiseStorage(args);
		initialiseLogic();
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		setScene();
		setStage();
	}
	
	/*
	* ===========================================
	* Initialising Methods
	* ===========================================
	*/
	
	/** Initialise Scene for GUI */
	private static void setScene(){
		mainBorderPane = new DefaultLayout(_taskList, _feedback);
		scene = new Scene(mainBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
	}
	
	/** Initialise Stage for GUI */
	private static void setStage() {
		stage.setTitle("Clockwork");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.getIcons().add(new Image(Main.class
						.getResourceAsStream("icon.png" ))); 
		stage.show();
	}
	
	/** 
	 * Initialise logic package for GUI. Comment out in main(String[] args)
	 * and uncomment UserInterfaceLogicStub method in ClockworkGUIController to test 
	 * GUI with UserInterfaceLogicStub.
	 */
	private static void initialiseLogic() {
		UIController.setLogic(ClockWork.getInstance());
	}
	
	/** 
	 * Initialise storage paths in logic package.Comment out in main(String[] args) 
	 * and uncomment UserInterfaceLogicStub method in ClockworkGUIController to test 
	 * GUI with UserInterfaceLogicStub.
	 */
	private static void initialiseStorage(String[] args) {
		//Set storage file directory, link with storage file
		ClockWork.setFileDirectory(ClockWork.getStorageFileDirFromSettings());
		// Check if a file directory path is passed in through argument
		if (args.length == 1) {
			// Check if file directory path is valid
			String customFileDirPath = args[0];
			ClockWork.setFileDirectory(StorageUtils.processStorageDirectory(customFileDirPath));
		}
	}
	
	/*
	* ===========================================
	* Public Methods
	* ===========================================
	*/
	public static void setTaskList(ArrayList<String> newTaskList){
		_taskList = newTaskList;
	}
	
	public static void setFeedback(String newFeedback){
		_feedback = newFeedback;
	}
	
	public static void minimise(){
		stage.setIconified(true);
	}
	
	public static void displayDefaultScene(){
		setScene();		
		setStage();
	}
	
	public static void displayHelpScene(){
		helpBorderPane = new HelpLayout();
		scene = new Scene(helpBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayCalendarScene(){
		calBorderPane = new CalendarLayout();
		scene = new Scene(calBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
}