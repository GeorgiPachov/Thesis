package com.gpachov.masterthesis.exceptions;

public class DataFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DataFetchingException(String message, Throwable cause){
		super(message, cause);
	}
}
