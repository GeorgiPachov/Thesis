package com.gpachov.masterthesis.linguistics.sentencemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ExtractionEngine {
    private static BiMap<Character, PosType> binding = HashBiMap.create();
    static {
	binding.put('a', PosType.ADJECTIVE);
	binding.put('n',PosType.NOUN);
	binding.put('v', PosType.VERB);
	binding.put('d', PosType.ADVERB);
	binding.put('p', PosType.PRONOUN);
	binding.put('t', PosType.DETERMINER);
	
    }
    private List<String> formulas;

    public ExtractionEngine(List<String> formulas) {
	this.formulas = formulas;
    }
    
    public List<SentenceModel> extractSimplifiedSentences(String sentence){
	SentenceModel model = new SentenceModel(sentence);
	String sentenceModelAsRegex = createRegexModel(model);
	
	List<SentenceModel> results = new ArrayList<SentenceModel>();
	for (String formulaRegexModel : formulas){
	    Pattern formulaPattern = Pattern.compile(formulaRegexModel);
	    Matcher matcher = formulaPattern.matcher(sentenceModelAsRegex);
	    
	    int[] intervalIndex = new int[2048];
	    while (matcher.find()){
		int groupBeginIndex = matcher.start();
		int groupEndIndex = matcher.end();
		
		boolean anotherPhraseCollision = false;
		for (int i = groupBeginIndex; i >= 0; i--){
		    if (intervalIndex[i] >= groupBeginIndex){
			anotherPhraseCollision = true;
		    }
		}
		
		for (int i = groupBeginIndex ; i < groupEndIndex; i++){
		    if (intervalIndex[i] != 0){
			anotherPhraseCollision = true;
		    }
		}
		if (anotherPhraseCollision){
		    continue;
		}
		//update indexes
		intervalIndex[groupBeginIndex] = groupEndIndex;
		
		List<PosToken> tokens = model.getTokenOrderedList().subList(groupBeginIndex, groupEndIndex); //is exclusive
		String simplifiedSentence = tokens.stream().map(PosToken::getRawWord).collect(Collectors.joining(" "));
		results.add(new SentenceModel(simplifiedSentence));
	    }
	}
	return results;
    }

    private String createRegexModel(SentenceModel model) {
	StringBuilder builder = new StringBuilder();
	for (PosToken token : model){
	    char c = binding.inverse().get(token.getPosType());
	    builder.append(c);
	}
	return builder.toString();
    }
}
