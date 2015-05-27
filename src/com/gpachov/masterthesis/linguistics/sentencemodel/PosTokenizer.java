package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTokenizer {
    private static final MaxentTagger tagger =  new MaxentTagger("/home/georgi/EEworkspace/Diplomna/src/resources/models/english-left3words-distsim.tagger");
    public static List<PosToken> tokenize(String raw) {
	raw = tagger.tagString(raw);//not very raw
	List<PosToken> result = new ArrayList<PosToken>();
	Arrays.stream(raw.split("\\s+")).forEach(taggedWord -> {
	    String[] tokens = taggedWord.split("_");
	    String realWord = tokens[0];
	    String tag = tokens[1];

	    for (PosType type : PosType.values()) {
		if (type.representation().contains(tag)) {
		    result.add(new PosToken(realWord, type));
		}
	    }
	});
	return result;
    }
}
