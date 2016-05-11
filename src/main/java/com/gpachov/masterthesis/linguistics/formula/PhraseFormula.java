package com.gpachov.masterthesis.linguistics.formula;

import java.util.List;

import com.gpachov.masterthesis.linguistics.Phrase;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

public interface PhraseFormula {
    List<Phrase> extract(SentenceModel sentence);
}
