package exceptions;

public class InvalidParamException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public InvalidParamException() {
	}
	
	public InvalidParamException(String message) {
		super(message);
	}
}
