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
import com.gpachov.masterthesis.extract.SentenceExtractor;
import com.gpachov.masterthesis.lexicon.BasicSentimentLexicon;
import com.gpachov.masterthesis.lexicon.ComposingSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;
import com.gpachov.masterthesis.lexicon.WordNetLexiconDecorator;
import com.gpachov.masterthesis.utils.Utils;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngine;
import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngineImpl;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;

public class SemanticClassifier extends Classifier {
    private static final List<String> formulas = new ArrayList<String>() {
	{
//	    add("[ntp]v[ad]{1,2}n"); // beds were awlful thing
//	    add("[ntp].*?v.*?[ad]{1,2}"); // I do not enjoy volleyball
//	    add("[ntp].*?v.*?n"); // I do not enjoy volleyball
	    add("[ntp]vdvn"); // I do not enjoy volleyball
//	    add("[ntp]vdvv"); // I do not enjoy swimming
	    add("[ntp]vdv[ad]{1,2}"); // i do not feel really good
	    add("[ntp]v[ad]{1,2}"); // beds were bad
	    add("[ntp]vov[ntpad]{0,2}");

	    // it was in the middle
	    // it was a circus
	    add("n{1,2}vdv"); //something did not care
	    add("nia{1,2}n");
	    add("pvd?n"); // beds were bad
	    add("t?nva"); // beds were bad
	    add("[ad]{1,3}n"); // amazingly correct <!--staff-->
	    add("da?[np]"); // amazingly correct
	    add("va{1,10}n"); // amazingly correct
	    add("d?a{1,10}"); // amazingly correct
	    add("na(?!n)"); // security nonexistent
	    add("v?dv"); // absolutely sucks, really do not like
	    
	    //new
	    add("a{1,2}n");
	    add("nvdd"); //spirits dropped even further
	    add("[ntp]vv"); //we were misled
	    add("vtn"); //boycott this hotel
	    add("tvn"); //this was goodness
	    add("tva"); //this was goodness
	    add("a{1,2}");
	    add("vov"); //started to die
//	    add("vn"); //hate volleyball
	}
    };
    private ExtractionEngine extractionEngine = new ExtractionEngineImpl(formulas);
    private SentimentLexicon lexicon = new ComposingSentimentLexicon(Arrays.asList(new BasicSentimentLexicon()));
    private SentimentLexicon wordNetLexicon = new WordNetLexiconDecorator(lexicon);
    static {
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
    }
    private WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();

    public SemanticClassifier(Map<DataClass, List<String>> trainingData) {
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
		    float score = lexicon.getScore(posToken.getRawWord(), posToken.getPosType());
		    SynsetType synsetType = mapType(posToken);
		    if (score == 0.0f) {
			boolean hasDerivatives = synsetType != null;
			if (hasDerivatives) {
			    for (String word : wordNetDatabase.getBaseFormCandidates(posToken.getRawWord(), synsetType)) {
				score = lexicon.getScore(word, posToken.getPosType());
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
					score = lexicon.getScore(sense.getWordForm(), posToken.getPosType());
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
				score = wordNetLexicon.getScore(posToken.getRawWord(), posToken.getPosType());
			    }
			}
		    }
		    
		    if (Constants.PLURAL_CORRECTION){
			if (score == 0.0f){
			    if (posToken.getPosType().equals(PosType.NOUN) && posToken.getRawWord().endsWith("s")){
				score = wordNetLexicon.getScore(posToken.getRawWord().substring(0, posToken.getRawWord().length()-1), posToken.getPosType());
			    }
			}
		    }

		    // basic check for negation
		    if (Constants.NEGATION_CORRECTION){
        		    int negationRange = Constants.NEGATION_RANGE;
        		    if (i > 0){
        			int j = i-1; 
        			while (negationRange >= 0 && j >= 0){
        			    if (isNegationNaive(tokenOrderedList.get(j))){
        				score *= -1;
        				break;
        			    }
        			    j--; negationRange--;
        			}
        		    }
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
	return Utils.isNegationNaive(posToken);
    }

    private ClassificationResult classify(int[] sentimentScore) {
	if (sentimentScore[0] > 0)
	    return ClassificationResult.POSITIVE;
	else if (sentimentScore[0] < 0)
	    return ClassificationResult.NEGATIVE;
	return ClassificationResult.NEUTRAL;
    }

    private static List<String> splitSentences(String opinion) {
	SentenceExtractor sentenceExtractor = new RelevantSentenceExtractor();
	List<String> setences = sentenceExtractor.extractRelevant(opinion, null);
	return setences;
    }

}
