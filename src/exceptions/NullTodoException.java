package exceptions;

public class NullTodoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//@@author Prem
	public NullTodoException () {
		super();
	}
	
	public NullTodoException (String message) {
		super(message);
	}

}
