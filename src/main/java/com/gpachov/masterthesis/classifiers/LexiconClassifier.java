package com.gpachov.masterthesis.classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.lexicon.BasicSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngineImpl;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;
import com.gpachov.masterthesis.utils.Utils;

public class LexiconClassifier extends Classifier {

    private static final List<String> formulas = new ArrayList<String>() {
	{
	    // add("[ntp]{1,3}(!=[nva]){0,3}v{1,2}(!=[nva]){0,3}[ad]{0,2}");
	    // add("[ntp]v{1,2}[ad]{1,2}[np]"); // amazingly correct
	    // add("[ntp]v[ad]{1,2}n"); // beds were awlful thing
	    add("[ntp]vdvn"); // beds were bad
	    add("[ntp]vdv[ad]{1,2}"); // i do not feel really good
	    add("[ntp]v[ad]{1,2}"); // beds were bad

	    // it was in the middle
	    // it was a circus
	    add("n{1,2}vdv"); // something did not care
	    add("nia{1,2}n");
	    add("pvd?n"); // beds were bad
	    add("t?nva"); // beds were bad
	    add("[ad]{1,3}n"); // amazingly correct <!--staff-->
	    add("da?[np]"); // amazingly correct
	    add("va{1,10}n"); // amazingly correct
	    add("d?a{1,10}"); // amazingly correct
	    add("na(?!n)"); // security nonexistent
	    add("v?dv"); // absolutely sucks, really do not like

	    // new
	    add("a{1,2}n");
	    add("nvdd"); // spirits dropped even further
	    add("[ntp]vv"); // we were misled
	    add("vtn"); // boycott this hotel
	    add("tvn"); // this was goodness
	    add("tva"); // this was goodness
	    add("a{1,2}");
	    // add("vn"); //hate volleyball
	}
    };

    private static final int MAX_CONTENT_SIZE = 5;

    private static final int MIN_CONTENT_SIZE = 0;
    private SentimentLexicon lexicon = new BasicSentimentLexicon();
    private ExtractionEngineImpl extractionEngine;

    public LexiconClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
	this.extractionEngine = new ExtractionEngineImpl(formulas);
    }

    @Override
    public ClassificationResult classify(String text) {
	String[] sentence = text.split("\\s+");
	float finalScore = 0;
	for (int i = 0; i < sentence.length; i++) {
	    String word = sentence[i];
	    if (lexicon.getScore(word, PosType.OTHER) != 0.0f) {
		contextLoop: for (int contextSize = MAX_CONTENT_SIZE; contextSize >= MIN_CONTENT_SIZE; contextSize--) {
		    StringBuilder context = new StringBuilder();
		    int pointer = i - contextSize;
		    if (pointer < 0)
			pointer = 0;
		    while (pointer < i + contextSize) {
			context.append(sentence[i] + " ");
			pointer++;
		    }

		    float contextScore = 0;
		    List<SentenceModel> simplifiedSentences = extractionEngine.extractSimplifiedSentences(context.toString());
		    for (int phrasePointer = 0; phrasePointer < simplifiedSentences.size(); phrasePointer++) {
			SentenceModel phrase = simplifiedSentences.get(phrasePointer);
			List<PosToken> tokenOrderedList = phrase.getTokenOrderedList();
			for (int k = 0; k < tokenOrderedList.size(); k++) {
			    PosToken phraseWord = tokenOrderedList.get(k);
			    float wordScore = lexicon.getScore(phraseWord.getRawWord(), phraseWord.getPosType());
			    // basic check for negation
			    if (Constants.NEGATION_CORRECTION) {
				int negationRange = contextSize;
				if (k > 0) {
				    int j = k - 1;
				    while (negationRange >= 0 && j >= 0) {
					if (isNegationNaive(phrase.getTokenOrderedList().get(j))) {
					    wordScore *= -1;
					    break;
					}
					j--;
					negationRange--;
				    }
				}
			    }

			    if (Constants.MULTIPLY_CORRECTION) {
				if (phraseWord.getPosType().equals(PosType.ADJECTIVE)) {
				    // check for multiplication word
				    List<String> multiplicationWords = Arrays.asList("unbelievably", "incredibly", "extremely", "highly", "very", "really",
					    "much", "truly", "real", "genuinely", "a lot", "lots", "rightfully", "sincerely");
				    if (k > 0 && multiplicationWords.contains(tokenOrderedList.get(k - 1).getRawWord())) {
					wordScore *= 2.0;
				    }
				}
			    }
			    contextScore += wordScore;
			}
			if (contextScore != 0) {
			    finalScore += contextScore;
			    break contextLoop;
			}
		    }

		}
	    }
	}

	if (finalScore > 0) {
	    return ClassificationResult.POSITIVE;
	} else if (finalScore < 0) {
	    return ClassificationResult.NEGATIVE;
	}
	return ClassificationResult.NEUTRAL;
    }

    private boolean isNegationNaive(PosToken posToken) {
	return Utils.isNegationNaive(posToken);
    }

}
