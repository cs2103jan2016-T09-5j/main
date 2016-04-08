package exceptions;

public class ParsingFailureException extends Exception {

	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public ParsingFailureException() {
	}

	public ParsingFailureException(String message) {
		super(message);
	}

}
