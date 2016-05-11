package com.gpachov.masterthesis.linguistics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;

public class Phrase {
    private List<PosToken> tokens;

    public Phrase(PosToken... tokens) {
	this.tokens = Arrays.asList(tokens);
    }

    public List<PosToken> getPhraseTokens() {
	return tokens;
    }
    
    @Override
    public String toString(){
	return tokens.stream().map(p->p.getRawWord()).collect(Collectors.joining(" "));
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
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
	Phrase other = (Phrase) obj;
	if (tokens == null) {
	    if (other.tokens != null)
		return false;
	} else if (!tokens.equals(other.tokens))
	    return false;
	return true;
    }
}
