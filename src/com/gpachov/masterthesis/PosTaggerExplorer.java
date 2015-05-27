package com.gpachov.masterthesis;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.linguistics.sentencemodel.ExtractionEngine;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosTokenizer;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTaggerExplorer {
    public static void main(String[] args) {
	String a = "not worth it terrible! the room was dirty. there were dust bunnies the size of cats under the beds, especially frustrating if you have allergies, or drop something. we had to ask for a tv and a blow dryer. they were given with an attitude. not the most customer service oriented place. yea, it was cheap, but now i see why. use discretion! this should be your last and only choice!";
	MaxentTagger tagger =  new MaxentTagger("/home/georgi/EEworkspace/Diplomna/src/resources/models/english-left3words-distsim.tagger");
	String tagged = tagger.tagString(a);
	System.out.println(tagged);
	Collection<SentenceModel> b = new ExtractionEngine(Arrays.asList("nva")).extractSimplifiedSentences(a);
	System.out.println(b);
    }
}
