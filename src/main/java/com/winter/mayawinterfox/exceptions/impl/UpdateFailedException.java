package com.winter.mayawinterfox.exceptions.impl;

public class UpdateFailedException extends RuntimeException {

	public UpdateFailedException() {
		super();
	}

	public UpdateFailedException(String m) {
		super(m);
	}

	public UpdateFailedException(String m, Throwable c) {
		super(m, c);
	}
}