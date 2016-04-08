package exceptions;

public class InvalidRecurringException extends Exception {
	private static final long serialVersionUID = 1L;

	//@@author Prem
	public InvalidRecurringException() {

	}

	public InvalidRecurringException(String message) {
		super(message);
	}
}