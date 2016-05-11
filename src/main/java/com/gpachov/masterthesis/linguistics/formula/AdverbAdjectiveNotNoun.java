package com.gpachov.masterthesis.linguistics.formula;

import java.util.ArrayList;
import java.util.List;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;
import com.gpachov.masterthesis.linguistics.Phrase;

public class AdverbAdjectiveNotNoun implements PhraseFormula{


    @Override
    public List<Phrase> extract(SentenceModel sentence) {
	List<Phrase> phrases = new ArrayList<Phrase>();
	
	List<PosToken> posTokens = new ArrayList<PosToken>();
	for (PosToken posToken : sentence){
	    posTokens.add(posToken);
	}
	for (int i = 0; i < posTokens.size() -3; i++){
	    PosType current = posTokens.get(i).getPosType();
	    PosType next = posTokens.get(i+1).getPosType();
	    PosType afterNext = posTokens.get(i+2).getPosType();
	    
	    if (current.equals(PosType.ADVERB) && next.equals(PosType.ADJECTIVE) && !afterNext.equals(PosType.NOUN)){
		phrases.add(new Phrase(posTokens.get(i), posTokens.get(i+1)));
	    }
	}
	return phrases;
    }
}
