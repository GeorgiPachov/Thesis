package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTokenizer {
    private static final MaxentTagger tagger;
	static {
		try {
			URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource("/models/english-left3words-distsim.tagger");
			tagger = new MaxentTagger(resourceUrl.getFile());
		} catch (Exception e){
			throw new RuntimeException("Could not initialize maximum entropy tagger!", e);
		}
	}
    public static List<PosToken> tokenize(String raw) {
	raw = tagger.tagString(raw);//not very raw
	List<PosToken> result = new ArrayList<PosToken>();
	Arrays.stream(raw.split("\\s+")).forEach(taggedWord -> {
	    String[] tokens = taggedWord.split("_");
	    String realWord = tokens[0];
	    String tag;
	    if (tokens.length < 2){
		System.out.println("warning: coult not tokenize " + taggedWord);
		tag = "unknown";
	    } else {
		tag = tokens[1];
	    }

	    for (PosType type : PosType.values()) {
		if (type.representation().contains(tag)) {
		    result.add(new PosToken(realWord, type));
		}
	    }
	});
	return result;
    }
}
