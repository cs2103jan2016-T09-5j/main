package com.clockwork;

public class SignalHandler {

    private static final String PREFIX = "ClockWork: ";
    private static final String COMMAND_PREFIX = "Command: ";

    public static void printSignal(Signal signal) {
        assert (signal != null);
        String message = signal.toString();
        if (message.equals(Signal.EXIT_SUCCESS)) {
            System.out.println(PREFIX + message);
            System.exit(0);
        }
        if (!message.isEmpty()) {
            System.out.println(PREFIX + message);
        }
	}

    public static void printCommandPrefix() {
        System.out.print(COMMAND_PREFIX);
    }
}
