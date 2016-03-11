package main;

public class SignalHandler {

    private static final String PREFIX = "BlockWork: ";

    public static void printSignal(Signal signal) {
        String message = signal.toString();
        if (message.equals(Signal.EXIT_SUCCESS)) {
            System.out.println(PREFIX + message);
            System.exit(0);
        }
        if (message != null && !message.isEmpty()) {
            System.out.println(PREFIX + message);
        }
	}
}
