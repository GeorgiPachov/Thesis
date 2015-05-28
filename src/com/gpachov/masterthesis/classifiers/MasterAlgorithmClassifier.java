package com.gpachov.masterthesis.classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.extract.RelevantSentenceExtractor;
import com.gpachov.masterthesis.lexicon.AdvancedSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngine;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class MasterAlgorithmClassifier extends Classifier {
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
	    add("dv"); // absolutely sucks
	}
    };
    private ExtractionEngine extractionEngine = new ExtractionEngine(formulas);
    private SentimentLexicon lexicon = new AdvancedSentimentLexicon();
    static {
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
    }
    private WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();

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
	Map<String, Float> wordEvals = new LinkedHashMap<String, Float>();
	allSimplifiedSentences.stream().forEach(sm -> {
	    List<PosToken> tokenOrderedList = sm.getTokenOrderedList();
	    float weight = 0.0f;
	    for (int i = 0; i < tokenOrderedList.size(); i++) {
		PosToken posToken = tokenOrderedList.get(i);

		// handle derivatives
		float score = lexicon.getScore(posToken.getRawWord());
		SynsetType synsetType = mapType(posToken);
		if (score == 0.0f) {
		    boolean hasDerivatives = synsetType != null;
		    if (hasDerivatives) {
			for (String word : wordNetDatabase.getBaseFormCandidates(posToken.getRawWord(), synsetType)) {
			    score = lexicon.getScore(word);
			    if (score != 0.0f) {
				break;
			    }
			}
		    }
		}

		// handle negation
		if (posToken.getPosType().equals(PosType.ADJECTIVE) || posToken.getPosType().equals(PosType.NOUN)) {
		    // basic check for negation
		    if (i > 0 && isNegationNaive(tokenOrderedList.get(i - 1))) {
			score *= -1;
		    }

		    // write score
		    if (score != 0.0f) {
			if (Constants.DEBUG_CLASSIFIER) {
			    wordEvals.put(posToken.getRawWord(), score);
			    weight += score;
			}
		    }
		    sentimentScore[0] += score;
		}
	    }
	    if (Constants.DEBUG_CLASSIFIER) {
		System.out.println("Assigned " + weight + " to " + sm);
		System.out.println(wordEvals);
		System.out.println("-----------------");
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

    private SynsetType mapType(PosToken posToken) {
	switch (posToken.getPosType()) {
	case ADJECTIVE:
	    return SynsetType.ADJECTIVE;
	case NOUN:
	    return SynsetType.NOUN;
	case VERB:
	    return SynsetType.VERB;
	case ADVERB:
	    return SynsetType.ADVERB;
	}
	return null;
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
