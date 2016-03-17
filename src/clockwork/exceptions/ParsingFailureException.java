
package clockwork.exceptions;

public class ParsingFailureException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParsingFailureException() {
	}

	public ParsingFailureException(String message) {
		super(message);
	}

}
