package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ExtractionEngine {
    private static BiMap<Character, PosType> binding = HashBiMap.create();
    static {
	binding.put('a', PosType.ADJECTIVE);
	binding.put('n', PosType.NOUN);
	binding.put('v', PosType.VERB);
	binding.put('d', PosType.ADVERB);
	binding.put('p', PosType.PRONOUN);
	binding.put('f', PosType.FOREIGN_WORD);
	binding.put('i', PosType.PREPOSITION);
	binding.put('t', PosType.DETERMINER);
	binding.put('m', PosType.MODAL);
	binding.put('c', PosType.COORDINATING_CONJUNCTION);
	binding.put('e', PosType.POSSESSIVE_ENDING);
	binding.put('b', PosType.INTERJECTION);
	binding.put('o', PosType.TO);
	binding.put('s', PosType.SYMBOL);
	binding.put('u', PosType.CARDINAL_NUMBER);
	binding.put('x', PosType.EXISTENTIAL_WORD);
	binding.put('r', PosType.PREDETERMINER);
	binding.put('l', PosType.PARTICLE);
	binding.put('h', PosType.WH_DETERMINER);
	binding.put('z', PosType.WH_PRONOUN);
	binding.put('_', PosType.OTHER);

    }
    private List<String> formulas;

    public ExtractionEngine(List<String> formulas) {
	this.formulas = formulas;
    }

    public Collection<SentenceModel> extractSimplifiedSentences(String sentence) {
	SentenceModel model = new SentenceModel(sentence);
	String sentenceModelAsRegex = createRegexModel(model);

	Set<SentenceModel> results = new LinkedHashSet<SentenceModel>();
	boolean[] tokenUsed = new boolean[4096];
	for (String formulaRegexModel : formulas) {
	    Pattern formulaPattern = Pattern.compile(formulaRegexModel);
	    Matcher matcher = formulaPattern.matcher(sentenceModelAsRegex);
	    while (matcher.find()) {
		int groupBeginIndex = matcher.start();
		int groupEndIndex = matcher.end();

		boolean phraseAlreadyMarked = false;
		for (int i = groupBeginIndex; i <= groupEndIndex; i++) {
		    if (tokenUsed[i]) {
			phraseAlreadyMarked = true;
		    }
		}

		if (!phraseAlreadyMarked) {
		    for (int i = groupBeginIndex; i < groupEndIndex; i++) {
			tokenUsed[i] = true;
		    }
		    List<PosToken> tokens = model.getTokenOrderedList().subList(groupBeginIndex, groupEndIndex); // is
														 // exclusive
		    String simplifiedSentence = tokens.stream().map(PosToken::getRawWord).collect(Collectors.joining(" "));
		    results.add(new SentenceModel(simplifiedSentence));
		}
		// List<PosToken> tokens =
		// model.getTokenOrderedList().subList(groupBeginIndex,
		// groupEndIndex); // is
		// // exclusive
		// String simplifiedSentence =
		// tokens.stream().map(PosToken::getRawWord).collect(Collectors.joining(" "));
		// results.add(new SentenceModel(simplifiedSentence));
	    }
	}
	return results;
    }

    //DEBUG
    public static String createRegexModel(SentenceModel model) {
	StringBuilder builder = new StringBuilder();
	for (PosToken token : model) {
	    char c = binding.inverse().get(token.getPosType());
	    builder.append(c);
	}
	return builder.toString();
    }
}
