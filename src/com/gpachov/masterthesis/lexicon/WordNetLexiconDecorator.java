package com.gpachov.masterthesis.lexicon;

import java.util.Collection;
import java.util.HashSet;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordNetLexiconDecorator implements SentimentLexicon {

    private SentimentLexicon lexicon;
    private WordNetDatabase wordNetDatabase;

    public WordNetLexiconDecorator(SentimentLexicon lexicon) {
	this.lexicon = lexicon;

	wordNetDatabase = WordNetDatabase.getFileInstance();
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
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
    public float getScore(String word, PosType posType) {
	float defaultScore = lexicon.getScore(word, posType);
	if (defaultScore!= 0.0f){
	    
	    return defaultScore;
	}
	
	if (defaultScore == 0.0f){
	    Synset[] synsets = wordNetDatabase.getSynsets(word, SynsetType.ADJECTIVE);
	    for (Synset synset : synsets){
		for (String wordForm : synset.getWordForms()){
		    float score = lexicon.getScore(wordForm, posType);
		    if (score!=0.0f && !wordForm.trim().equalsIgnoreCase(word.trim())){
			System.out.println(word + " => " + wordForm );
			return score;
		    }
		}
	    }
	}
	return 0.0f;
    }

}
