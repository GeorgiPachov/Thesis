package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

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
}
