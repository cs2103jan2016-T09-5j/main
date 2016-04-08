package exceptions;

public class StateUndefinedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public StateUndefinedException() {
	}

	public StateUndefinedException(String message) {
		super(message);
	}
}
