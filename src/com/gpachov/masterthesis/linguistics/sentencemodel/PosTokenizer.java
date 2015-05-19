package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosTokenizer {

    public static List<PosToken> tokenize(String raw) {
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
			break;
		    case ADVERB:
			result.add(new Adverb(realWord));
			break;
		    default:
			result.add(new PosToken(realWord, PosType.OTHER));
			break;
		    }
		}
	    }

	});
	return result;
    }
}
