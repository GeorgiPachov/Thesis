package com.gpachov.mastertheysis.exceptions;

public class InitializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InitializationException(Throwable cause){
		super(cause);
	}
}
