package com.gpachov.masterthesis;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;

public class WordNetExplorer {
    public static void main(String[] args) {
	WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
	
	String input = "ugly";
	Synset[] synsets = wordNetDatabase.getSynsets(input);
	for (Synset synset : synsets){
	    WordSense[] derivationallyRelatedForms = synset.getDerivationallyRelatedForms(input);
	    System.out.println("---");
	    for (WordSense sense : derivationallyRelatedForms){
		System.out.println(sense.getWordForm());
	    }
	}
    }
}
