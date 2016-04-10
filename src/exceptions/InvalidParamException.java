package exceptions;

//@@author Prem
public class InvalidParamException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidParamException() {
	}
	
	public InvalidParamException(String message) {
		super(message);
	}
}
