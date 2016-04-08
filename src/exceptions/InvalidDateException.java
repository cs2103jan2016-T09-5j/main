package exceptions;

public class InvalidDateException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public InvalidDateException() {

	}

	public InvalidDateException(String message) {
		super(message);
	}
}