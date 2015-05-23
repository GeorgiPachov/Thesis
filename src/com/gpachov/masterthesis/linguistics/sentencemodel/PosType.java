package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.Arrays;
import java.util.List;

public enum PosType {
    ADJECTIVE {
	@Override
	public List<String> representation() {
	    return Arrays.asList("JJ");
	}
    },
    NOUN {
	@Override
	public List<String> representation() {
	    return Arrays.asList("NN", "NNS");
	}
    },
    VERB {
	@Override
	public List<String> representation() {
	    return Arrays.asList("VB","VBD","VBN","VBG");
	}
    },
    ADVERB {
	@Override
	public List<String> representation() {
	    return Arrays.asList("RB", "RBS", "RBR");
	}
    },
    OTHER{
	public List<String> representation(){
	    return Arrays.asList();
	}
    }, PRONOUN {
	@Override
	public List<String> representation() {
	    return Arrays.asList("PRP");
	}
    };
    public abstract List<String> representation();
}
