package com.gpachov.masterthesis.lexicon;

import java.util.Collection;
import java.util.HashSet;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordNetLexiconDecorator implements SentimentLexicon {

    private HashSet<String> allPositiveWords;
    private HashSet<String> allNegativeWords;
    private SentimentLexicon lexicon;
    private WordNetDatabase wordNetDatabase;

    public WordNetLexiconDecorator(SentimentLexicon lexicon) {
	this.lexicon = lexicon;

	wordNetDatabase = WordNetDatabase.getFileInstance();
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
	this.allPositiveWords = new HashSet<String>(lexicon.getAllPositive());
	this.allNegativeWords = new HashSet<String>(lexicon.getAllNegative());
    }

    @Override
    public Collection<String> getAllNeutral() {
	return this.lexicon.getAllNeutral();
    }

    @Override
    public Collection<String> getAllNegative() {
	return this.lexicon.getAllNegative();
    }

    @Override
    public Collection<String> getAllPositive() {
	return this.lexicon.getAllPositive();
    }
    
    @Override
    public float getScore(String word) {
	float defaultScore = lexicon.getScore(word);
	if (defaultScore!= 0.0f){
	    
	    return defaultScore;
	}
	
	if (defaultScore == 0.0f){
	    Synset[] synsets = wordNetDatabase.getSynsets(word, SynsetType.ADJECTIVE);
	    for (Synset synset : synsets){
		for (String wordForm : synset.getWordForms()){
		    System.out.println(word + " => " + wordForm );
		    float score = lexicon.getScore(wordForm);
		    if (score!=0.0f){
			return score;
		    }
		}
	    }
	}
	return 0.0f;
    }

}
