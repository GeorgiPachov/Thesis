package com.gpachov.masterthesis.linguistics;

import java.util.ArrayList;
import java.util.List;

import com.gpachov.masterthesis.linguistics.formula.PhraseFormulaFactory;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;
import com.gpachov.masterthesis.linguistics.formula.PhraseFormula;

public class PhraseExtractor {
    public static List<Phrase> extractPhrases(String input) {
	List<Phrase> phrases = new ArrayList<Phrase>();
	SentenceModel tokenizedSentence = new SentenceModel(input);
	for (PhraseFormula phraseFormula : PhraseFormulaFactory.getFormulas()) {
	    phrases.addAll(phraseFormula.extract(tokenizedSentence));
	}
	return phrases;
    }
}
