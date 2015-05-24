package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SentenceModel implements Iterable<PosToken>{
    private String raw;
    private List<PosToken> tokens = new ArrayList<PosToken>();
    
    public SentenceModel(String raw){
	this.raw = raw;
	this.tokens = PosTokenizer.tokenize(raw);
    }
    
    public String getRawSentence(){
	return raw;
    }

    @Override
    public Iterator<PosToken> iterator() {
	return tokens.iterator();
    }
    
    public List<PosToken> getTokenOrderedList(){
	return tokens;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((raw == null) ? 0 : raw.hashCode());
	result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
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
	SentenceModel other = (SentenceModel) obj;
	if (raw == null) {
	    if (other.raw != null)
		return false;
	} else if (!raw.equals(other.raw))
	    return false;
	if (tokens == null) {
	    if (other.tokens != null)
		return false;
	} else if (!tokens.equals(other.tokens))
	    return false;
	return true;
    }
}
