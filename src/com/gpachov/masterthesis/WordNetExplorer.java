package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gpachov.masterthesis.lexicon.AdvancedSentimentLexicon;
import com.gpachov.masterthesis.lexicon.SentimentLexicon;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

//George A. Miller (1995). WordNet: A Lexical Database for English.
//Communications of the ACM Vol. 38, No. 11: 39-41.
//
//Christiane Fellbaum (1998, ed.) WordNet: An Electronic Lexical Database. Cambridge, MA: MIT Press.
//
//WordNet: An Electronic Lexical Database (citation above) is available from MIT Press.
public class WordNetExplorer {
    public static void main(String[] args) {
	WordNetDatabase wordNetDatabase = WordNetDatabase.getFileInstance();
	System.setProperty("wordnet.database.dir", "/usr/share/wordnet/dict");
	Arrays.stream(wordNetDatabase.getBaseFormCandidates("attractions", SynsetType.NOUN)).forEach(System.out::println);
	// SentimentLexicon lexicon = new AdvancedSentimentLexicon();
	// Set<String> newPositives = new
	// HashSet<String>(lexicon.getAllPositive());
	// Set<String> oldNegative = new
	// HashSet<String>(lexicon.getAllNegative());
	// for (int i = 0; i < 10; i++) {
	// List<String> toAdd = new ArrayList<String>();
	// newPositives.forEach(positiveWord -> {
	// Synset[] synset = wordNetDatabase.getSynsets(positiveWord);
	// Arrays.stream(synset).forEach(s -> {
	// for (String wordForm : s.getWordForms()) {
	// toAdd.add(wordForm);
	// }
	// });
	// });
	// System.out.println(newPositives.size());
	// newPositives.addAll(toAdd);
	// System.out.println(newPositives.size());
	// }
    }
}
