package com.gpachov.masterthesis.classifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.lexicon.AdvancedSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngine;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

public class MasterAlgorithmClassifier extends Classifier {
    private SentimentLexicon lexicon = new AdvancedSentimentLexicon();
    private static final List<String> formulas = new ArrayList<String>() {
	{
	    add("a{1,10}n");
	    add("pva");
	}
    };
    private ExtractionEngine extractionEngine = new ExtractionEngine(formulas);
    // private List<PhraseFormula> phraseFormula = new
    // ArrayList<PhraseFormula>() {
    // {
    // add(new AdjectiveNounAnything());
    // add(new AdjectiveAdjectiveNoun());
    // add(new NounVerbAdjective());
    //
    // }
    // };

    private SimpleLexiconClassifier classifier;

    public MasterAlgorithmClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
	this.classifier = new SimpleLexiconClassifier(trainingData);
    }

    @Override
    public ClassificationResult classify(String text) {
	List<String> sentences = splitSentences(text);
	// List<Phrase> phrases = new ArrayList<>();
	List<SentenceModel> allSimplifiedSentences = new ArrayList<SentenceModel>();
	for (String sentence : sentences) {
	    List<SentenceModel> simplifiedSentence = extractionEngine.extractSimplifiedSentences(sentence);
	    allSimplifiedSentences.addAll(simplifiedSentence);
	    
	    // SentenceModel model = new SentenceModel(sentence);
	    // for (PhraseFormula formula : phraseFormula) {
	    // phrases.addAll(formula.extract(model));
	    // }
	}

	int[] sentimentScore = new int[1];
	allSimplifiedSentences.stream().forEach(sm -> {
	    for (PosToken posToken : sm.getTokenOrderedList()) {
		float score = lexicon.getScore(posToken.getRawWord());
		if (score != 0.0f) {
		    if (Constants.DEBUG_CLASSIFIER) {
//			System.out.println(" Assigning " + score + " to " + posToken.getRawWord());
		    }
		}
		sentimentScore[0] += score;
	    }
	});
	ClassificationResult result = classify(sentimentScore);
	if (Constants.DEBUG_CLASSIFIER) {
	    System.out.println();
	    System.out.println(/*text + */"=> [ " + allSimplifiedSentences.stream().map(SentenceModel::getRawSentence).collect(Collectors.joining(", ")) + " ]");
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
