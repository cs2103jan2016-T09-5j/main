
package com.clockwork.exceptions;

public class NotRecurringException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotRecurringException() {
		
	}
	
	public NotRecurringException(String message) {
		super(message);
	}
}