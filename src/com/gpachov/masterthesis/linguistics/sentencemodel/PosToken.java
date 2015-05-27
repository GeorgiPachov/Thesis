package com.gpachov.masterthesis.linguistics.sentencemodel;

public class PosToken {
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((posType == null) ? 0 : posType.hashCode());
	result = prime * result + ((raw == null) ? 0 : raw.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PosToken other = (PosToken) obj;
	if (posType != other.posType)
	    return false;
	if (raw == null) {
	    if (other.raw != null)
		return false;
	} else if (!raw.equals(other.raw))
	    return false;
	return true;
    }

    protected final String raw;
    protected final PosType posType;
    
    @Override
    public String toString() {
        return raw + " => " + posType;
    }
    
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
