package logic;

import java.util.ArrayList;

public class SignalHandler {

    private static final String PREFIX = "ClockWork : ";
    private static  ArrayList<String> ArrListForGUI = new ArrayList<String> ();
    
    public static ArrayList<String> getArrListForGUI(){
    	return ArrListForGUI;
    }
    public static void clearArrListForGUI(){
    	ArrListForGUI.clear();
    }
    public static void printSignal(Signal signal) {
        assert (signal != null);
        String message = signal.toString();
        if (message.equals(Signal.EXIT_SUCCESS)) {
        	ArrListForGUI.add(PREFIX + message);
            System.exit(0);
        }
        if (!message.isEmpty()) {
        	ArrListForGUI.add(PREFIX + message);
        }
	}
}
