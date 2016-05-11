package com.gpachov.masterthesis.data.impl;

import java.util.Date;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.data.SourceDetails;

public class RawDataEntryImpl implements RawDataEntry {

	private Date date;
	private String text;
	private SourceDetails sourceDetails;

	public RawDataEntryImpl(String text){
		this(new Date(), text, null);
	}
	
	public RawDataEntryImpl(Date d, String text, SourceDetails sourceDetails) {
		this.date = d;
		this.text = text;
		this.sourceDetails = sourceDetails;
	}
	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public SourceDetails getSourceDetails() {
		return sourceDetails;
	}
	@Override
	public String toString() {
		return "RawDataEntryImpl [date=" + date + ", text=" + text
				+ ", sourceDetails=" + sourceDetails + "]";
	}

}
