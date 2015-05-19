package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Sentence implements Iterable<PosToken>{
    private String raw;
    private String tagged;
    private static final MaxentTagger tagger =  new MaxentTagger("/home/georgi/EEworkspace/Diplomna/src/resources/models/english-left3words-distsim.tagger");
    private List<PosToken> tokens = new ArrayList<PosToken>();
    
    public Sentence(String raw){
	this.raw = raw;
	this.tagged = tagger.tagString(raw);
	this.tokens = PosTokenizer.tokenize(tagged);
    }
    
    public String getRawSentence(){
	return raw;
    }

    @Override
    public Iterator<PosToken> iterator() {
	return tokens.iterator();
    }
}
