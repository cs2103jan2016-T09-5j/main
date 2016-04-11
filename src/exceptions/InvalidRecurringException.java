package exceptions;

//@@author A0126219J
public class InvalidRecurringException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidRecurringException() {

	}

	public InvalidRecurringException(String message) {
		super(message);
	}
}