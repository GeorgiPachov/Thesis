package com.gpachov.masterthesis.data;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.mongodb.BasicDBObject;

public class Entry extends BasicDBObject{

	static final String TEXT_COLUMN = "text";
	static final String FLOAT_COLUMN = "evalutation";
	
	private static final long serialVersionUID = 6395474654468782285L;
	public Entry(@Nonnull String text, @Nonnegative float value){
		this.put(TEXT_COLUMN, text);
		this.put(FLOAT_COLUMN, value);
	}
	@Override
	public String toString() {
		return toMap().toString();
	}
	
	public float getEvaluation(){
		return (float) get(FLOAT_COLUMN);
	}
}
