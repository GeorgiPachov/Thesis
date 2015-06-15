package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.Collection;

public interface ExtractionEngine {
    public abstract Collection<SentenceModel> extractSimplifiedSentences(String sentence);
}
