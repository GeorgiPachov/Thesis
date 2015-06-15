package com.gpachov.masterthesis.lexicon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;

public class ComposingSentimentLexicon implements SentimentLexicon {
    private List<SentimentLexicon> lexicons = new ArrayList<>();

    public ComposingSentimentLexicon(List<SentimentLexicon> lexicons) {
	this.lexicons = lexicons;
    }	
    
    @Override
    public Collection<String> getAllNeutral() {
	return lexicons.stream().map(SentimentLexicon::getAllNeutral).flatMap(l -> l.stream()).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAllNegative() {
	return lexicons.stream().map(SentimentLexicon::getAllNegative).flatMap(l -> l.stream()).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAllPositive() {
	return lexicons.stream().map(SentimentLexicon::getAllPositive).flatMap(l -> l.stream()).collect(Collectors.toList());
    }

    @Override
    public float getScore(String word, PosType posType) {
//	double[] result = new double[1];
	for (SentimentLexicon l : lexicons){
	    if (l.getScore(word, posType)!=0.0f){
		return l.getScore(word, posType);
	    }
	}
//	lexicons.stream().mapToDouble(l -> l.getScore(word)).filter(v -> v!=0.0f).average().ifPresent( d-> {
//	    result[0] = d;  
//	});
	
	return 0.0f;
//	return (float) result[0];
    }
}
