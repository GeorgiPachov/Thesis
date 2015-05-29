package com.gpachov.masterthesis.lexicon;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.classifiers.SimpleLexiconClassifier;
import com.gpachov.masterthesis.dictionaries.SkipWordsDictionary;

public class BasicSentimentLexicon extends AbstractSentimentLexicon{
    private static final URL SENTIMENT_LEXICON_FILE = BasicSentimentLexicon.class.getClassLoader().getResource("resources/lexicon.txt");
    private static final String TYPE_KEY = "type";
    private static final String STRONG_SUBJ = "strongsubj";
    private static final String  POLARITY_KEY = "priorpolarity";
    private static final String POSITIVE = "positive";
    private static final String NEGATIVE = "negative";
    private static final String WORD_KEY = "word1";

    public BasicSentimentLexicon(){
	try {
	    Files.readAllLines(Paths.get(SENTIMENT_LEXICON_FILE.toURI())).forEach(l -> {
		float[] multiplier_subj = new float[]{1.0f};
		float[] multiplier_polarity = new float[]{0.0f};
		String[] word = new String[]{""};
		Arrays.stream(l.split("\\s+")).forEach(pair -> {
		    String[] tokens = pair.split("=");
		    String key = tokens[0];
		    String value = tokens[1];
		    if (TYPE_KEY.equals(key)){
			if (STRONG_SUBJ.equals(value)){
			    multiplier_subj[0] = Constants.STRONG_MULTIPLIER;
			}
		    } else if (POLARITY_KEY.equals(key)){
			if (POSITIVE.equals(value)){
			    multiplier_polarity[0] = 1;
			} 
			if (NEGATIVE.equals(value)){
			    multiplier_polarity[0] = -1;
			}
		    } else if (WORD_KEY.equals(key)){
			word[0] = value;
		    }
		});
		super.lexicon.put(word[0], multiplier_subj[0] * multiplier_polarity[0]);
	    });
	    
	} catch (IOException | URISyntaxException e) {
	    throw new RuntimeException(e);
	}
    }
}