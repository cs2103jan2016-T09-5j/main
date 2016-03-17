
package clockwork.exceptions;

public class InvalidDateException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidDateException() {

	}

	public InvalidDateException(String message) {
		super(message);
	}
}