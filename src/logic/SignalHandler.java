package logic;

import java.util.ArrayList;

//@@author Regine
/**
 * The SignalHandle Class is used as a container for the messages displayed 
 * that get from the Signal Class and links to userinterface package
 * 
 * The Controller Class in the userinterface package would access 
 * SignalHandler Class to get feedback as a string
 * which to be display on the userinterface.
 *
 */

public class SignalHandler {

    private static  ArrayList<String> arrListForGUI = new ArrayList<String> ();
        
    /**
	 * For Controller class in userinterface package to get feedback's ArrayList
	 */
    public static ArrayList<String> getArrListForGUI() {
    	return arrListForGUI;
    }
    
    /**
	 * For Controller class in userinterface package to clear feedback's ArrayList
	 */
    public static void clearArrListForGUI(){
    	arrListForGUI.clear();
    }
    
    /**
   	 * To get signal feedback and add in to the ArrayList
   	 */
    public static void printSignal(Signal signal) {
        assert (signal != null);
        String message = signal.toString();
        if (message.equals(Signal.EXIT_SUCCESS)) {
        	arrListForGUI.add( message);
            System.exit(0);
        }
        if (!message.isEmpty()) {
        	arrListForGUI.add(message);
        }
	}
}
