
package com.clockwork.exceptions;

public class InvalidPeriodException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidPeriodException() {
	}

	public InvalidPeriodException(String message) {
		super(message);
	}
}
