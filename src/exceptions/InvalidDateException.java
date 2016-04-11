package exceptions;

//@@author A0126219J
public class InvalidDateException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidDateException() {

	}

	public InvalidDateException(String message) {
		super(message);
	}
}