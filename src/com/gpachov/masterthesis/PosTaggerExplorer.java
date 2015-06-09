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
	String a = "I like volleyball but hate football";
	MaxentTagger tagger =  new MaxentTagger("/home/georgi/EEworkspace/Diplomna/src/resources/models/english-left3words-distsim.tagger");
	String tagged = tagger.tagString(a);
	System.out.println(tagged);
	Collection<SentenceModel> b = new ExtractionEngine(Arrays.asList("pvn")).extractSimplifiedSentences(a);
	System.out.println(b);
    }
}
