package exceptions;

public class InvalidPeriodException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public InvalidPeriodException() {
	}

	public InvalidPeriodException(String message) {
		super(message);
	}
}
