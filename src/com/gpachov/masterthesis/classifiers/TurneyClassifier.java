package com.gpachov.masterthesis.classifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.linguistics.Phrase;
import com.gpachov.masterthesis.linguistics.PhraseExtractor;
import com.gpachov.masterthesis.linguistics.formula.PhraseFormulaFactory;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

public class TurneyClassifier extends Classifier {

    private List<Phrase> badPhrases = new ArrayList<Phrase>();
    private List<Phrase> goodPhrases = new ArrayList<Phrase>();

    public TurneyClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
	init();
    }

    private void init() {
	// search for phases in the training data

	sampleData.entrySet().stream().parallel().forEach(e -> {
	    DataClass dataClass = e.getKey();
	    List<String> sentences = e.getValue();
	    if (dataClass.mean() > 0.31 && dataClass.mean() < 0.61) {
		return;
	    }
	    List<Phrase> phrases = extractPhrases(sentences);
	    if (dataClass.equals(DataClass.BAD)){
		System.out.println("Adding " + phrases + " to bad ");
		this.badPhrases.addAll(phrases);
	    } else if (dataClass.equals(DataClass.GOOD)) {
		this.goodPhrases.addAll(phrases);
	    }
	});
    }

    private List<Phrase> extractPhrases(List<String> sentences) {
	List<Phrase> phrases = new ArrayList<Phrase>();
	for (String sentence : sentences) {
	    phrases.addAll(PhraseExtractor.extractPhrases(sentence));
	}
	return phrases;
    }

    @Override
    public ClassificationResult classify(String text) {
	SentenceModel tokenizedSentence = new SentenceModel(text);
	List<Phrase> phrases = new ArrayList<Phrase>();
	PhraseFormulaFactory.getFormulas().forEach(f -> {
	    phrases.addAll(f.extract(tokenizedSentence));
	});

	float totalSum = 0;
	float maxSum = 0;
	for (Phrase phrase : phrases) {
	    long occurenceInBad = badPhrases.stream().filter(p -> p.equals(phrase)).count();
	    long occurenceInGood = goodPhrases.stream().filter(p -> p.equals(phrase)).count();
	    if (occurenceInBad > occurenceInGood){
		System.out.println(phrase + " qualified as bad");
	    } else {
	    }
	    float goodRatio = 0.0f;
	    if (occurenceInBad + occurenceInBad == 0){
		goodRatio = 0.5f;
	    } else {
		goodRatio = occurenceInGood / (occurenceInBad + occurenceInGood);
	    }
//	    System.out.println(phrase.toString() + " received " + goodRatio);
	    totalSum += goodRatio;
	    maxSum += 1;
	}
	ClassificationResult result = classify(totalSum, maxSum);
//	System.out.println("Classified " + text + " as " + result +" because " + );
	return result;
    }

    private ClassificationResult classify(float totalSum, float maxSum) {
	float goodnessRatio = (totalSum / maxSum);
	if (goodnessRatio < 0.33) {
	    return ClassificationResult.NEGATIVE;
	} else if (goodnessRatio < 0.66) {
	    return ClassificationResult.NEUTRAL;
	} else
	    return ClassificationResult.POSITIVE;
    }
}
