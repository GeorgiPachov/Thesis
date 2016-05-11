package com.gpachov.masterthesis.linguistics.formula;

import java.util.ArrayList;
import java.util.List;

public class PhraseFormulaFactory {

    public static List<PhraseFormula> getFormulas() {
	return new ArrayList<PhraseFormula>() {
	    {
		add(new AdjectiveAdjectiveNotNoun());
		add(new AdjectiveNounAnything());
		add(new AdverbAdjectiveNotNoun());
		add(new AdverbVerbAnything());
		add(new NounAdjectiveNotNoun());
	    }
	};
    }
}
