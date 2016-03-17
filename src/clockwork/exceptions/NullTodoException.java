
package clockwork.exceptions;

public class NullTodoException extends Exception {
	private static final long serialVersionUID = 1L;

	public NullTodoException () {
		super();
	}
	
	public NullTodoException (String message) {
		super(message);
	}

}
