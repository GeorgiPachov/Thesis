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
}
