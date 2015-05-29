package com.gpachov.masterthesis.lexicon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractSentimentLexicon implements SentimentLexicon{

    protected Map<String, Float> lexicon = new HashMap<String, Float>();

    @Override
    public Collection<String> getAllNeutral() {
        return new ArrayList<String>();
    }

    @Override
    public Collection<String> getAllNegative() {
        return lexicon.entrySet().stream().filter(e -> e.getValue() < 0.0f).map(e -> e.getKey()).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAllPositive() {
        return lexicon.entrySet().stream().filter(e -> e.getValue() > 0.0f).map(e -> e.getKey()).collect(Collectors.toList());
    }

    @Override
    public float getScore(String word) {
        return lexicon.getOrDefault(word, 0.0f);
    }

}
