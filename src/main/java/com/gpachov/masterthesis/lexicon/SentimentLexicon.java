package com.gpachov.masterthesis.lexicon;

import java.util.Collection;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;

public interface SentimentLexicon {

    public abstract Collection<String> getAllNeutral();

    public abstract Collection<String> getAllNegative();

    public abstract Collection<String> getAllPositive();

    public abstract float getScore(String word, PosType posType);
    
}
