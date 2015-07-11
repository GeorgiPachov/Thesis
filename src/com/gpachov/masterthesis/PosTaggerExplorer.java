package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngineImpl;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosTokenizer;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTaggerExplorer {
    private static final List<String> formulas = new ArrayList<String>() {
	{
//	    add("[ntp]v[ad]{1,2}n"); // beds were awlful thing
//	    add("[ntp].*?v.*?[ad]{1,2}"); // I do not enjoy volleyball
//	    add("[ntp].*?v.*?n"); // I do not enjoy volleyball
	    add("[ntp]vdvn"); // I do not enjoy volleyball
//	    add("[ntp]vdvv"); // I do not enjoy swimming
	    add("[ntp]vdv[ad]{1,2}"); // i do not feel really good
	    add("[ntp]v[ad]{1,2}"); // beds were bad

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
    public static void main(String[] args) {
	String a = "I started to kill myself before I remembered the remote";
	MaxentTagger tagger =  new MaxentTagger("/home/georgi/EEworkspace/Diplomna/src/resources/models/english-left3words-distsim.tagger");
	String tagged = tagger.tagString(a);
	System.out.println(tagged);
	Collection<SentenceModel> b = new ExtractionEngineImpl(formulas).extractSimplifiedSentences(a);
	System.out.println(b);
    }
}
