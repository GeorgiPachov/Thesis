package com.gpachov.masterthesis;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTaggerExplorer {
    public static void main(String[] args) {
	String a = "I like watching movies";
	MaxentTagger tagger =  new MaxentTagger("/home/georgi/EEworkspace/Diplomna/src/resources/models/english-left3words-distsim.tagger");
	String tagged = tagger.tagString(a);
	System.out.println(tagged);
    }
}
