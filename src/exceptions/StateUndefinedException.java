package exceptions;

//@@author A0126219J
public class StateUndefinedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public StateUndefinedException() {
	}

	public StateUndefinedException(String message) {
		super(message);
	}
}
