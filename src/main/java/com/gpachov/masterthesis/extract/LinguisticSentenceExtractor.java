package com.gpachov.masterthesis.extract;

import java.util.ArrayList;
import java.util.List;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosTokenizer;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;

public class LinguisticSentenceExtractor implements SentenceExtractor {
    private static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
    private static final SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();
    private static final long MAX_ADVERB_COUNT = 20;

    @Override
    public List<String> extractRelevant(String input, String relevant) {
	List<String> result = new ArrayList<String>();

	Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(input.toCharArray(), 0, input.length());
	final ArrayList<String> tokens = new ArrayList<String>();
	final ArrayList<String> whiteList = new ArrayList<String>();
	tokenizer.tokenize(tokens, whiteList);
	String[] tokenArray = new String[tokens.size()];
        String[] whites = new String[whiteList.size()];
        tokens.toArray(tokenArray);
        whiteList.toArray(whites);
	int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokenArray, whites);
	
	int sentStartTok = 0;
	int sentEndTok = 0;
	for (int i = 0; i < sentenceBoundaries.length; ++i) {
	    sentEndTok = sentenceBoundaries[i];
	    StringBuilder sentence = new StringBuilder();
	    for (int j=sentStartTok; j <= sentEndTok; j++) {
		sentence.append(tokenArray[j] + whites[j+1]);
	    }
	    if (relevant!= null && sentence.toString().toLowerCase().contains(relevant.toLowerCase())) {
		long adverbCount = PosTokenizer.tokenize(sentence.toString()).stream().map(p -> p.getPosType()).filter(p -> p.equals(PosType.ADVERB)).count();
		if (adverbCount <= MAX_ADVERB_COUNT){
		    result.add(sentence.toString());
		}
	    } else if (relevant == null){
		result.add(sentence.toString());
	    }
	    sentStartTok = sentEndTok+1;
	}
	return result;
    }

}
