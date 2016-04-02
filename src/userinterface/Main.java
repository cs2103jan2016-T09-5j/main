package userinterface;

import java.util.ArrayList;

import common.UserInterfaceObject;
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
	private static ArrayList<UserInterfaceObject> _todayList = new ArrayList<UserInterfaceObject>();
	private static ArrayList<UserInterfaceObject> _tomorrowList = new ArrayList<UserInterfaceObject>();	
	private static ArrayList<UserInterfaceObject> _upcomingList = new ArrayList<UserInterfaceObject>();	
	private static ArrayList<UserInterfaceObject> _somedayList = new ArrayList<UserInterfaceObject>();	
	
	private static OldLayout mainBorderPane;
	private static LayoutHelp helpBorderPane;
	private static LayoutCalendar calBorderPane;
	private static LayoutTemplate todayBorderPane;
	private static LayoutTemplate tomorrowBorderPane;
	private static LayoutTemplate upcomingBorderPane;
	private static LayoutTemplate somedayBorderPane;
	private static String _feedback;
	private static String _scrollAction = "DO NOTHING";
	private static double _scrollValue = 0;
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
		mainBorderPane = new OldLayout(_taskList, _feedback);
		_scrollValue = mainBorderPane.setScrollPosition(_scrollAction, _scrollValue);
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
		Controller.setLogic(ClockWork.getInstance());
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
	
	public static void setTodayList(ArrayList<UserInterfaceObject> todayList){
		_todayList = todayList;
	}
	
	public static void setTomorrowList(ArrayList<UserInterfaceObject> tomorrowList){
		_tomorrowList = tomorrowList;
	}
	
	public static void setUpcomingList(ArrayList<UserInterfaceObject> upcomingList){
		_upcomingList = upcomingList;
	}
	
	public static void setSomedayList(ArrayList<UserInterfaceObject> somedayList){
		_somedayList = somedayList;
	}
	
	public static void minimise(){
		stage.setIconified(true);
	}
	
	public static void displayDefaultScene(){
		setScene();		
		setStage();
	}
	
	public static void displayHelpScene(){
		helpBorderPane = new LayoutHelp();
		scene = new Scene(helpBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayCalendarScene(){
		calBorderPane = new LayoutCalendar();
		scene = new Scene(calBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySummaryScene(){
		LayoutSummary summaryBorderPane = new LayoutSummary();
		scene = new Scene(summaryBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayTodayScene(){
		LayoutTemplate todayBorderPane = new LayoutTemplate("Today", _todayList);
		scene = new Scene(todayBorderPane, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void scrollListener(String action){
		_scrollAction = action;
		displayDefaultScene();
	}
}