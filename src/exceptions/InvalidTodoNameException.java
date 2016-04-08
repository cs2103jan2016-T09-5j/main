package exceptions;

public class InvalidTodoNameException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public InvalidTodoNameException() {

	}

	public InvalidTodoNameException(String message) {
		super(message);
	}
}