package com.gpachov.masterthesis.classifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.lexicon.AdvancedSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.linguistics.Phrase;
import com.gpachov.masterthesis.linguistics.formula.AdjectiveAdjectiveNoun;
import com.gpachov.masterthesis.linguistics.formula.AdjectiveNounAnything;
import com.gpachov.masterthesis.linguistics.formula.NounVerbAdjective;
import com.gpachov.masterthesis.linguistics.formula.PhraseFormula;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.Sentence;

public class MasterAlgorithmClassifier extends Classifier {
    private SentimentLexicon lexicon = new AdvancedSentimentLexicon();
    private List<PhraseFormula> phraseFormula = new ArrayList<PhraseFormula>() {
	{
	    add(new AdjectiveNounAnything());
	    add(new AdjectiveAdjectiveNoun());
	    add(new NounVerbAdjective());
	    
	}
    };

    private SimpleLexiconClassifier classifier;

    public MasterAlgorithmClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
	this.classifier = new SimpleLexiconClassifier(trainingData);
    }

    @Override
    public ClassificationResult classify(String text) {
	List<String> sentences = splitSentences(text);
	List<Phrase> phrases = new ArrayList<>();
	for (String sentence : sentences) {
	    Sentence model = new Sentence(sentence);
	    for (PhraseFormula formula : phraseFormula) {
		phrases.addAll(formula.extract(model));
	    }
	}

	int[] sentimentScore = new int[1];
	for (Phrase phrase : phrases) {
	    for (PosToken posToken : phrase.getPhraseTokens()) {
		float score = lexicon.getScore(posToken.getRawWord());
		if (score != 0.0f) {
		    if (Constants.DEBUG_CLASSIFIER) {
			System.out.println(" Assigning " + score + " to " + posToken.getRawWord());
		    }
		}
		sentimentScore[0] += score;

	    }
	}
	ClassificationResult result = classify(sentimentScore);
	if (Constants.DEBUG_CLASSIFIER) {
	    System.out.println(text + "=>" + phrases);
	    System.out.println("-------");
	}
	return result;
    }

    private ClassificationResult classify(int[] sentimentScore) {
	if (sentimentScore[0] > 0)
	    return ClassificationResult.POSITIVE;
	else if (sentimentScore[0] < 0)
	    return ClassificationResult.NEGATIVE;
	return ClassificationResult.NEUTRAL;
    }

    private static List<String> splitSentences(String opinion) {
	LinguisticSentenceExtractor sentenceExtractor = new LinguisticSentenceExtractor();
	List<String> setences = sentenceExtractor.extractRelevant(opinion, null);
	return setences;
    }

}
