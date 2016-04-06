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
	
	private static int _numToday;
	private static int _numTomorrow;
	private static int _numUpcoming;
	private static int _numSomeday;
	

	private static LayoutSummary summaryLayout;
	private static LayoutHelp helpLayout;
	private static LayoutCalendar calendarLayout;
	private static LayoutTemplate todayLayout;
	private static LayoutTemplate tomorrowLayout;
	private static LayoutTemplate upcomingLayout;
	private static LayoutTemplate somedayLayout;
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
		displaySummaryScene();
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
		summaryLayout = new LayoutSummary(_numToday, _numTomorrow, _numUpcoming, _numSomeday);
		scene = new Scene(summaryLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
	}
	
	/** Initialise Stage for GUI */
	private static void setStage() {
		stage.setTitle("Clockwork");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
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
	
	public static void setNumToday(int numToday){
		_numToday = numToday;
	}
	
	public static void setNumTomorrow(int numTomorrow){
		_numTomorrow = numTomorrow;
	}
	
	public static void setNumUpcoming(int numUpcoming){
		_numUpcoming = numUpcoming;
	}
	
	public static void setNumSomeday(int numSomeday){
		_numSomeday = numSomeday;
	}
	
	public static void minimise(){
		stage.setIconified(true);
	}
	
	public static void displayHelpScene(){
		helpLayout = new LayoutHelp();
		scene = new Scene(helpLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayCalendarScene(){
		calendarLayout = new LayoutCalendar();
		scene = new Scene(calendarLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySummaryScene(){
		summaryLayout = new LayoutSummary(_numToday, _numTomorrow, _numUpcoming, _numSomeday);
		scene = new Scene(summaryLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayTodayScene(){
		todayLayout = new LayoutTemplate("Today", _todayList);
		scene = new Scene(todayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayTomorrowScene(){
		tomorrowLayout = new LayoutTemplate("Tomorrow", _tomorrowList);
		scene = new Scene(tomorrowLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayUpcomingScene(){
		upcomingLayout = new LayoutTemplate("Upcoming", _upcomingList);
		scene = new Scene(upcomingLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySomedayScene(){
		somedayLayout = new LayoutTemplate("Someday", _somedayList);
		scene = new Scene(somedayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
}