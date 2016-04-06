package userinterface;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.ClockWork;
import storage.StorageUtils;

public class Main extends Application {
	
	/** Application window dimensions */
	private static int WIDTH_WINDOW_DEFAULT = 900;
	private static int HEIGHT_WINDOW_DEFAULT = 600;

	/** Static variables */
	private static ArrayList<String[]> _todayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _tomorrowList = new ArrayList<String[]>();
	private static ArrayList<String[]> _upcomingList = new ArrayList<String[]>();
	private static ArrayList<String[]> _somedayList = new ArrayList<String[]>();
	private static ArrayList<String[]> _searchList = new ArrayList<String[]>();
	private static ArrayList<String[]> _powerList = new ArrayList<String[]>();
	
	private static int _numToday;
	private static int _numTomorrow;
	private static int _numUpcoming;
	private static int _numSomeday;
	

	private static LayoutSummary summaryLayout;
	private static LayoutHelp helpLayout;
	private static LayoutCalendar calendarLayout;
	private static LayoutTemplate todayLayout;
	private static LayoutTemplate tomorrowLayout;
	private static LayoutUpcoming upcomingLayout;
	private static LayoutTemplate somedayLayout;
	private static LayoutTemplate searchLayout;
	private static LayoutUpcoming allLayout;
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
	
	public static void setFeedback(String feedback){
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
	
	public static TableView displayTodayScene(){
		todayLayout = new LayoutTemplate("Today", _todayList, _feedback);
		scene = new Scene(todayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
		return todayLayout.getTableView();
	}
	
	public static void displayTomorrowScene(){
		tomorrowLayout = new LayoutTemplate("Tomorrow", _tomorrowList, _feedback);
		scene = new Scene(tomorrowLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayUpcomingScene(){
		upcomingLayout = new LayoutUpcoming("Upcoming", _upcomingList,  _feedback);
		scene = new Scene(upcomingLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySomedayScene(){
		somedayLayout = new LayoutTemplate("Someday", _somedayList,  _feedback);
		scene = new Scene(somedayLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displaySearchScene(){
		searchLayout = new LayoutTemplate("Search", _searchList,  _feedback);
		scene = new Scene(searchLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
	
	public static void displayAllScene(){
		allLayout = new LayoutUpcoming("All Tasks", _powerList,  _feedback);
		scene = new Scene(allLayout, WIDTH_WINDOW_DEFAULT, HEIGHT_WINDOW_DEFAULT);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Main.class.getResource("clockwork.css").toExternalForm());
		setStage();
	}
}