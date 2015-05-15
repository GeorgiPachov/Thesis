package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.lexicon.AdvancedSentimentLexicon;

public class SentimentLexiconSimpleClassifier extends Classifier {
    private AdvancedSentimentLexicon lexicon = new AdvancedSentimentLexicon();

    public SentimentLexiconSimpleClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
    }

    @Override
    public ClassificationResult classify(String text) {
	int[] sentimentScore = new int[1];
	Arrays.stream(text.split("\\s+")).forEach(word -> {
	    sentimentScore[0] +=lexicon.getScore(word);
	});
	if (sentimentScore[0] > 0)
	    return ClassificationResult.POSITIVE;
	else if (sentimentScore[0] < 0)
	    return ClassificationResult.NEGATIVE;
	return ClassificationResult.NEUTRAL;
    }

}
