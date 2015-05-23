package com.gpachov.masterthesis.lexicon;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;

public class WordNetLexiconDecorator implements SentimentLexicon {

    private HashSet<String> allPositiveWords;
    private HashSet<String> allNegativeWords;
    private SentimentLexicon lexicon;

    public WordNetLexiconDecorator(SentimentLexicon lexicon) {
	this.lexicon = lexicon;

	WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
	this.allPositiveWords = new HashSet<String>(lexicon.getAllPositive());
	Set<String> newPositiveWords = new HashSet<String>(lexicon.getAllPositive());

	this.allNegativeWords = new HashSet<String>(lexicon.getAllNegative());
	Set<String> newNegativeWords = new HashSet<String>(lexicon.getAllNegative());
	int iterations = 0;
	boolean[] change = new boolean[] { iterations > 0};
	while (change[0]) {
	    Set<String> positivesToAdd = new HashSet<String>();
	    Set<String> negativesToAdd = new HashSet<String>();
	    newPositiveWords.forEach(positiveWord -> {
		Synset[] synset = wordNetDatabase.getSynsets(positiveWord, SynsetType.ADJECTIVE);
		Arrays.stream(synset).forEach(s -> {
		    for (String wordForm : s.getWordForms()) {
			if (!allPositiveWords.contains(wordForm)) {
			    positivesToAdd.add(wordForm);
			}
			for (WordSense antonym : s.getAntonyms(wordForm)) {
			    if (!allNegativeWords.contains(antonym.getWordForm())) {
				negativesToAdd.add(antonym.getWordForm());
			    }
			}
		    }
		});

		newNegativeWords.forEach(negativeWord -> {
		    Synset[] negativeSynsets = wordNetDatabase.getSynsets(negativeWord, SynsetType.ADJECTIVE);
		    Arrays.stream(negativeSynsets).forEach(negativeSynset -> {
			for (String wordForm : negativeSynset.getWordForms()) {
			    if (!allNegativeWords.contains(wordForm)) {
				negativesToAdd.add(wordForm);
			    }
			    for (WordSense antonym : negativeSynset.getAntonyms(wordForm)) {
				if (!allPositiveWords.contains(antonym.getWordForm())) {
				    positivesToAdd.add(antonym.getWordForm());
				}
			    }
			}
		    });
		});
	    });

	    
//	    change[0] = (positivesToAdd.size() + negativesToAdd.size()) > 0;
	    change[0] = iterations-- > 0;
	    
	    allPositiveWords.addAll(positivesToAdd);
	    newPositiveWords.clear();
	    newPositiveWords.addAll(positivesToAdd);
	    positivesToAdd.clear();

	    allNegativeWords.addAll(negativesToAdd);
	    newNegativeWords.clear();
	    newNegativeWords.addAll(negativesToAdd);
	    negativesToAdd.clear();
	}
    }

    @Override
    public Collection<String> getAllNeutral() {
	return this.lexicon.getAllNeutral();
    }

    @Override
    public Collection<String> getAllNegative() {
	return allNegativeWords;
    }

    @Override
    public Collection<String> getAllPositive() {
	return allPositiveWords;
    }

    @Override
    public float getScore(String word) {
	if (allPositiveWords.contains(word))
	    return 1;
	else if (allNegativeWords.contains(word)) {
	    return -1;
	}
	return 0;
    }

}
