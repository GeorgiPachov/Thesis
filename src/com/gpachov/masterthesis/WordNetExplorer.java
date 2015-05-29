package com.gpachov.masterthesis;

import java.util.Arrays;
import java.util.List;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;

public class WordNetExplorer {
    public static void main(String[] args) {
	WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
	// very really much big high low
//	List<String> input = Arrays.asList("unbelievably","incredibly","extremely","highly", "very", "really", "much", "truly", "real", "genuinely", "a lot", "lots", "rightfully", "sincerely" );
	List<String> input = Arrays.asList("little","a little","small", "a bit", "minuscle" );
	for (String i : input) {
	    Synset[] synsets = wordNetDatabase.getSynsets(i);
	    for (Synset synset : synsets) {
		for (String wf : synset.getWordForms()) {
		    System.out.println(wf);
		}
	    }
	}
    }
}
