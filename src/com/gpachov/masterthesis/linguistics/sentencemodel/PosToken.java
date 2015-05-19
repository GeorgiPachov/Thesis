package com.gpachov.masterthesis.linguistics.sentencemodel;

public class PosToken {
    protected final String raw;
    protected final PosType posType;
    
    public PosToken(String raw, PosType posType){
	this.raw = raw;
	this.posType = posType;
    }
    
    public String getRawWord(){
	return raw;
    }
    
    public PosType getPosType(){
	return posType;
    }
}
