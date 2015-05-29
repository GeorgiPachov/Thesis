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
import com.gpachov.masterthesis.lexicon.BasicSentimentLexicon;
import com.gpachov.masterthesis.lexicon.ComposingSentimentLexicon;
import com.gpachov.masterthesis.lexicon.InquirerSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.lexicon.WordNetLexiconDecorator;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngine;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;

public class MasterAlgorithmClassifier extends Classifier {
    private static final List<String> formulas = new ArrayList<String>() {
	{
//	    add("[ntp]{1,3}(!=[nva]){0,3}v{1,2}(!=[nva]){0,3}[ad]{0,2}");
//	    add("[ntp]v{1,2}[ad]{1,2}[np]"); // amazingly correct
//	    add("[ntp]v[ad]{1,2}n"); // beds were awlful thing
	    add("[ntp]v[ad]{1,2}"); // beds were bad

	    // it was in the middle
	    // it was a circus
	    add("n{1,2}vdv"); //something did not care
	    add("nia{1,2}n");
	    add("pvn"); // beds were bad
	    add("t?nva"); // beds were bad
	    add("[ad]{1,3}n"); // amazingly correct <!--staff-->
	    add("da?[np]"); // amazingly correct
	    add("va{1,10}n"); // amazingly correct
	    add("d?a{1,10}"); // amazingly correct
	    add("na(?!n)"); // security nonexistent
	    add("dv"); // absolutely sucks
	    
	    //new
	    add("nvdd"); //spirits dropped even further
	    add("[ntp]vv"); //we were misled
	    add("vtn"); //boycott this hotel
	    add("tvn"); //this was goodness
	}
    };
    private ExtractionEngine extractionEngine = new ExtractionEngine(formulas);
    private SentimentLexicon lexicon = new ComposingSentimentLexicon(Arrays.asList(new BasicSentimentLexicon()));
    private SentimentLexicon wordNetLexicon = new WordNetLexiconDecorator(lexicon);
    static {
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
    }
    private WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();

    public MasterAlgorithmClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
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
		// handle negation
		if (posToken.getPosType().equals(PosType.ADJECTIVE) || posToken.getPosType().equals(PosType.NOUN) || posToken.getPosType().equals(PosType.VERB)) {
		    // handle multiples, singles, etc
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

		    // handle derivatives
		    if (Constants.DERIVATIVE_CORRECTION) {
			if (score == 0.0f) {
			    Synset[] synsets = wordNetDatabase.getSynsets(posToken.getRawWord(), mapType(posToken));
			    for (Synset synset : synsets) {
				if (score == 0.0f) {
				    WordSense[] derivationallyRelatedForms = synset.getDerivationallyRelatedForms(posToken.getRawWord());
				    for (WordSense sense : derivationallyRelatedForms) {
					score = lexicon.getScore(sense.getWordForm());
				    }
				}
			    }
			}
		    }
		    // check synonim words
		    if (Constants.SYNONIM_CORRECTION) {
			if (score == 0.0f) {
			    // this is safe only for adjectives
			    if (posToken.getPosType().equals(PosType.ADJECTIVE)) {
				score = wordNetLexicon.getScore(posToken.getRawWord());
			    }
			}
		    }

		    // basic check for negation
		    if (i > 0 && isNegationNaive(tokenOrderedList.get(i - 1))) {
			score *= -1;
		    }
		    
		    if (Constants.MULTIPLY_CORRECTION){
			if (posToken.getPosType().equals(PosType.ADJECTIVE)){
			    //check for multiplication word
			    List<String> multiplicationWords = Arrays.asList("unbelievably","incredibly","extremely","highly", "very", "really", "much", "truly", "real", "genuinely", "a lot", "lots", "rightfully", "sincerely" );
			    if (i > 0 && multiplicationWords.contains(tokenOrderedList.get(i-1).getRawWord())){
				score *=2.0;
			    }
			}
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
	return Arrays.asList("no", "not", "didnt", "didn't", "did nt", "n't").stream().filter(s -> posToken.getRawWord().contains(s)).count() > 0;
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
