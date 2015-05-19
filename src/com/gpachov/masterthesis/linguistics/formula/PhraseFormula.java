package com.gpachov.masterthesis.linguistics.formula;

import java.util.List;

import com.gpachov.masterthesis.linguistics.Phrase;
import com.gpachov.masterthesis.linguistics.sentencemodel.Sentence;

public interface PhraseFormula {
    List<Phrase> extract(Sentence sentence);
}
