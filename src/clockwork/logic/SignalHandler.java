package clockwork.logic;

import java.util.ArrayList;

public class SignalHandler {

    private static final String PREFIX = "ClockWork: ";
    private static final String COMMAND_PREFIX = "Command: ";
    private static  ArrayList<String> ArrListForGUI = new ArrayList<String> ();
    
    public static ArrayList<String> getArrListForGUI(){
    	return ArrListForGUI;
    }
    public static void clearArrListForGUI(){
    	ArrListForGUI.clear();
    }
    public static void printSignal(Signal signal) {
    	ArrListForGUI.clear();
        assert (signal != null);
        String message = signal.toString();
        if (message.equals(Signal.EXIT_SUCCESS)) {
        	ArrListForGUI.add(PREFIX + message);
           // System.out.println(PREFIX + message);
            System.exit(0);
        }
        if (!message.isEmpty()) {
        	ArrListForGUI.add(PREFIX + message);
           // System.out.println(PREFIX + message);
        }
	}

    public static void printCommandPrefix() {
    	ArrListForGUI.clear();
    	ArrListForGUI.add(COMMAND_PREFIX);
        //System.out.print(COMMAND_PREFIX);
    }
}
