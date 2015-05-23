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
		    switch (type) {
		    case ADJECTIVE:
			result.add(new Adjective(realWord));
			break;
		    case VERB:
			result.add(new Verb(realWord));
			break;
		    case NOUN:
			result.add(new Noun(realWord));
		    case ADVERB:
			result.add(new Adverb(realWord));
			break;
		    case PRONOUN:
			result.add(new Pronoun(realWord));
			break;
//		    case DETERMINER:
//			result.add(new Determiner(realWord));
//			break;
//		    default:
//			result.add(new PosToken(realWord, PosType.OTHER));
//			break;
		    }
		}
	    }
	});
	return result;
    }
}
