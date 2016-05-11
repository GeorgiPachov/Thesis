package com.gpachov.masterthesis.datafetching;

public class QueryImpl implements Query {
	private static final long DEFAULT_TIME_LIMIT = 5000; //2 seconds
	private String text;
	private long timeLimit;

	public QueryImpl(String text){
		this(text, DEFAULT_TIME_LIMIT);
	}
	
	public QueryImpl(String text, long timeLimit){
		this.text = text;
		this.timeLimit = timeLimit;
	}
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public long getTimeLimit() {
		return timeLimit;
	}

}
