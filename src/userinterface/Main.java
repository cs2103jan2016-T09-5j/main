package userinterface;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.ClockWork;
import storage.StorageUtils;

//@@author Rebekah
/**
* The Main class contains the main method to run the entire Clockwork program. Clockwork 
* functions as a scheduler for the user to schedule tasks with only typed commands and 
* keypress.
*/
public class Main extends Application {
	
	/** Application window dimensions */
	private static int WIDTH_WINDOW_DEFAULT = 900;
	private static int HEIGHT_WINDOW_DEFAULT = 600;

	/** Lists containing tasks to display in each scene */
	private static ArrayList<String[]> _todayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _tomorrowList = new ArrayList<String[]>();
	private static ArrayList<String[]> _upcomingList = new ArrayList<String[]>();
	private static ArrayList<String[]> _somedayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _searchList = new ArrayList<String[]>();
	private static ArrayList<String[]> _powerList = new ArrayList<String[]>();
	
	/** Integer containing number of tasks for each category */
	private static int _numToday = 0;
	private static int _numTomorrow = 0;
	private static int _numUpcoming = 0;
	private static int _numSomeday = 0;
	
	/** Layouts used to display tasks in different scenes */
	private static LayoutCategory categoryLayout;
	private static LayoutHelp helpLayout;
	private static LayoutCalendar calendarLayout;
	private static LayoutTemplate todayLayout;
	private static LayoutTemplate tomorrowLayout;
	private static LayoutTemplate upcomingLayout;
	private static LayoutTemplate somedayLayout;
	private static LayoutTemplate searchLayout;
	private static LayoutTemplate allLayout;
	
	/** List to display feedback to user */
	private static ArrayList<String> _feedback;
	
	/** Stage and scene to display to user */
	private static Scene scene;
	private static Stage stage;
	
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
	
	/** Initialise Scene for GUI */
	private static void setScene(){
		Controller.processEnter("DISPLAY");
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
		Controller.resetAllLists();
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
	
	/** Minimises the window */
	public static void minimise(){
		stage.setIconified(true);
	}
	
	/** Displays the help scene */
	public static void displayHelpScene(){
		helpLayout = new LayoutHelp();
		scene = new Scene(helpLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the calendar scene */
	public static void displayCalendarScene(){
		calendarLayout = new LayoutCalendar();
		scene = new Scene(calendarLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the overall category scene */
	public static void displayCategoryScene(){
		categoryLayout = new LayoutCategory(_numToday, _numTomorrow, _numUpcoming, _numSomeday);
		scene = new Scene(categoryLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the category scene for today */
	public static void displayTodayScene(){
		_feedback = Controller.getFeedback();
		todayLayout = new LayoutTemplate("Today", _todayList, _feedback, false, true);
		scene = new Scene(todayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the category scene for tomorrow */
	public static void displayTomorrowScene(){
		_feedback = Controller.getFeedback();
		tomorrowLayout = new LayoutTemplate("Tomorrow", _tomorrowList, _feedback, false, true);
		scene = new Scene(tomorrowLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the category scene for upcoming tasks not scheduled today or tomorrow but 
	 * with a specified date
	 */
	public static void displayUpcomingScene(){
		_feedback = Controller.getFeedback();
		upcomingLayout = new LayoutTemplate("Upcoming", _upcomingList,  _feedback, true, true);
		scene = new Scene(upcomingLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the category scene for floating tasks */
	public static void displaySomedayScene(){
		_feedback = Controller.getFeedback();
		somedayLayout = new LayoutTemplate("Someday", _somedayList,  _feedback, false, true);
		scene = new Scene(somedayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the search scene */
	public static void displaySearchScene(){
		_feedback = Controller.getFeedback();
		searchLayout = new LayoutTemplate("Search", _searchList,  _feedback, true, true);
		scene = new Scene(searchLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	/** Displays the all tasks scene */
	public static void displayAllScene(){
		_feedback = Controller.getFeedback();
		allLayout = new LayoutTemplate("All Tasks", _powerList,  _feedback, true, false);
		scene = new Scene(allLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void setFeedbackList(ArrayList<String> feedback){
		_feedback = feedback;
	}
	
	public static void setTodayList(ArrayList<String[]> todayList){
		_todayList = todayList;
	}
	
	public static void setTomorrowList(ArrayList<String[]> tomorrowList){
		_tomorrowList = tomorrowList;
	}
	
	public static void setUpcomingList(ArrayList<String[]> upcomingList){
		_upcomingList = upcomingList;
	}
	
	public static void setSomedayList(ArrayList<String[]> somedayList){
		_somedayList = somedayList;
	}
	public static void setPowerList(ArrayList<String[]> powerList){
		_powerList = powerList;
	}
	public static void setSearchList(ArrayList<String[]> searchList) {
		_searchList = searchList;
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
}