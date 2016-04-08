package exceptions;

public class NotRecurringException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public NotRecurringException() {
		
	}
	
	public NotRecurringException(String message) {
		super(message);
	}
}
