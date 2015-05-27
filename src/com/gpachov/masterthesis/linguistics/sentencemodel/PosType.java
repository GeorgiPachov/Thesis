package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.Arrays;
import java.util.List;

public enum PosType {
    ADJECTIVE {
	@Override
	public List<String> representation() {
	    return Arrays.asList("JJ","JJR","JJS");
	}
    },
    NOUN {
	@Override
	public List<String> representation() {
	    return Arrays.asList("NN", "NNS", "NNP", "NNPS");
	}
    },
    VERB {
	@Override
	public List<String> representation() {
	    return Arrays.asList("VB","VBD","VBN","VBG", "VBP", "VBZ");
	}
    },
    ADVERB {
	@Override
	public List<String> representation() {
	    return Arrays.asList("RB", "RBS", "RBR");
	}
    }, DETERMINER {
	@Override
	public List<String> representation() {
	    return Arrays.asList("DT");
	}
    }, PREPOSITION {
	@Override
	public List<String> representation() {
	    return Arrays.asList("IN");
	}
    }, MODAL {
	@Override
	public List<String> representation() {
	    return Arrays.asList("MD");
	}
    }, COORDINATING_CONJUNCTION {
	@Override
	public List<String> representation() {
	    return Arrays.asList("CC");
	}
    }, PREDETERMINER {
	@Override
	public List<String> representation() {
	    return Arrays.asList("PDT");
	}
    }, POSSESSIVE_ENDING{

	@Override
	public List<String> representation() {
	    return Arrays.asList("POS");
	}
    }, INTERJECTION {
	@Override
	public List<String> representation() {
	    return Arrays.asList("UH");
	}
    }, TO {
	@Override
	public List<String> representation() {
	    return Arrays.asList("TO");
	}
    }, SYMBOL {
	@Override
	public List<String> representation() {
	    return Arrays.asList("SYM");
	}
    }, PARTICLE {
	@Override
	public List<String> representation() {
	    return Arrays.asList("PR");
	}
    }, FOREIGN_WORD {
	@Override
	public List<String> representation() {
	    return Arrays.asList("FW");
	}
    }, EXISTENTIAL_WORD {

	@Override
	public List<String> representation() {
	    return Arrays.asList("EX");
	}
    }, CARDINAL_NUMBER {
	@Override
	public List<String> representation() {
	    return Arrays.asList("CD");
	}
    },
    OTHER{
	public List<String> representation(){
	    return Arrays.asList("WDT", "WP", "WP$","WRB");
	}
    }, PRONOUN {
	@Override
	public List<String> representation() {
	    return Arrays.asList("PRP");
	}
    };
    public abstract List<String> representation();
}
