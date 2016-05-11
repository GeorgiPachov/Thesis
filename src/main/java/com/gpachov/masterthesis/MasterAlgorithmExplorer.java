package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.List;

import com.gpachov.masterthesis.provider.IDataProvider;
import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.linguistics.Phrase;
import com.gpachov.masterthesis.linguistics.formula.AdjectiveNounAnything;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;
import com.gpachov.masterthesis.provider.DatabaseDataProvider;
import com.gpachov.masterthesis.provider.LimitingProvider;

public class MasterAlgorithmExplorer {
    public static void main(String[] args) {
	IDataProvider provider = new LimitingProvider(new DatabaseDataProvider(), 100);
	provider.getUnclassified().forEach(opinion -> {
	    List<String> sentences = splitSentences(opinion);
	    List<Phrase> phrases = new ArrayList<>();
	    for (String sentence : sentences) {
		SentenceModel model = new SentenceModel(sentence);
		AdjectiveNounAnything formula = new AdjectiveNounAnything();
		phrases.addAll(formula.extract(model));
	    }
	    System.out.println(opinion + " => " + phrases.toString());
	    System.out.println("########\n");
	    });
    }

    private static List<String> splitSentences(String opinion) {
	LinguisticSentenceExtractor sentenceExtractor = new LinguisticSentenceExtractor();
	List<String> setences = sentenceExtractor.extractRelevant(opinion, null);
	return setences;
    }
}
