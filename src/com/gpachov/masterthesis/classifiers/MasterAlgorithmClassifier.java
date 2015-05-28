package com.gpachov.masterthesis.classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.extract.RelevantSentenceExtractor;
import com.gpachov.masterthesis.extract.SentenceExtractor;
import com.gpachov.masterthesis.lexicon.AdvancedSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngine;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

public class MasterAlgorithmClassifier extends Classifier {
    private SentimentLexicon lexicon = new AdvancedSentimentLexicon();
    private static final List<String> formulas = new ArrayList<String>() {
	{
	    add("[np]v{1,2}[ad]{1,10}[np]"); // amazingly correct
	    add("[np]v[ad]{1,10}n"); // beds were awlful thing
	    add("[np]va{1,10}n"); // beds were awlful thing
	    add("[np]va"); // beds were bad

	    // it was in the middle
	    // it was a circus
	    add("n{1,2}vdv");
	    add("nia{1,10}n");
	    // cheese with fiery sauce

	    // air conditioner did not work
	    // conditioner did not work
	    add("pvn"); // beds were bad
	    add("t?nva"); // beds were bad
	    add("[ad]{1,10}n"); // amazingly correct <!--staff-->
	    add("da?[np]"); // amazingly correct
	    add("va{1,10}n"); // amazingly correct
	    add("d?a{1,10}"); // amazingly correct
	    add("na(?!n)"); // security nonexistent
	}
    };
    private ExtractionEngine extractionEngine = new ExtractionEngine(formulas);

    public MasterAlgorithmClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
	// Map<String, String> allResusls = new HashMap<String, String>();
	// trainingData.entrySet().stream().map(Entry::getValue).flatMap(l ->
	// l.stream()).forEach(opinion -> {
	// SentenceExtractor sentenceSplitter = new
	// LinguisticSentenceExtractor();
	// List<String> relevantSentences =
	// sentenceSplitter.extractRelevant(opinion, null);
	// for (String relevantSentence : relevantSentences) {
	// Collection<SentenceModel> fromThisSentence = new
	// ExtractionEngine(Arrays.asList("n.{0,3}[ad]{0,3}.{0,3}v{1,3}.{0,3}a{0,3}")).extractSimplifiedSentences(relevantSentence);
	// Map<String, String> collect =
	// fromThisSentence.stream().collect(Collectors.toMap( s ->
	// s.getRawSentence(), s -> ExtractionEngine.createRegexModel(s)));
	// allResusls.putAll(collect);
	// }
	// });
	//
	// allResusls.entrySet().forEach(e -> {
	// // System.out.println(e.getKey() + "==>" + e.getValue());
	// });
    }

    @Override
    public ClassificationResult classify(String text) {
	List<String> sentences = splitSentences(text);
	List<SentenceModel> allSimplifiedSentences = new ArrayList<SentenceModel>();
	for (String sentence : sentences) {
	    Collection<SentenceModel> simplifiedSentence = extractionEngine.extractSimplifiedSentences(sentence);
	    allSimplifiedSentences.addAll(simplifiedSentence);
	}

	int[] sentimentScore = new int[1];
	allSimplifiedSentences.stream().forEach(sm -> {
	    List<PosToken> tokenOrderedList = sm.getTokenOrderedList();
	    for (int i = 0; i < tokenOrderedList.size(); i++) {
		PosToken posToken = tokenOrderedList.get(i);
		float score = lexicon.getScore(posToken.getRawWord());
		if (posToken.getPosType().equals(PosType.ADJECTIVE) || posToken.getPosType().equals(PosType.NOUN)){
		    //basic check for negation
		    if (i > 0 && isNegationNaive(tokenOrderedList.get(i-1))){
			score *=-1;
		    }
		    if (score != 0.0f) {
        		    if (Constants.DEBUG_CLASSIFIER) {
        			// System.out.println(" Assigning " + score + " to " +
        			// posToken.getRawWord());
        		    }
        		}
        		sentimentScore[0] += score;
        	    }
	    }
	});
	ClassificationResult result = classify(sentimentScore);
	if (Constants.DEBUG_CLASSIFIER) {
	    System.out.println();
	    System.out.println(text + "\n=> [ " + allSimplifiedSentences.stream().map(SentenceModel::getRawSentence).collect(Collectors.joining(", ")) + " ]");
	    System.out.println("-------");
	}
	return result;
    }

    private boolean isNegationNaive(final PosToken posToken) {
	return Arrays.asList("no", "not", "didnt", "didn't", "did nt", "'nt").stream().filter(s -> posToken.getRawWord().contains(s)).count() > 0;
    }

    private ClassificationResult classify(int[] sentimentScore) {
	if (sentimentScore[0] > 0)
	    return ClassificationResult.POSITIVE;
	else if (sentimentScore[0] < 0)
	    return ClassificationResult.NEGATIVE;
	return ClassificationResult.NEUTRAL;
    }

    private static List<String> splitSentences(String opinion) {
	RelevantSentenceExtractor sentenceExtractor = new RelevantSentenceExtractor();
	List<String> setences = sentenceExtractor.extractRelevant(opinion, null);
	return setences;
    }

}
