package com.gpachov.masterthesis.classifiers;

import java.util.StringTokenizer;
import java.util.function.Function;

import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.dictionaries.SentimentDictionary;

public class WordDictClassifier extends Classifier{

	private final SentimentDictionary wordDictionary;

	public WordDictClassifier(SampleData sampleData) {
		super(sampleData);
		this.wordDictionary = new SentimentDictionary();
	}

	@Override
	public ClassifierResult classify(String text) {
		Function<String, Integer> mapper = s -> {
			StringTokenizer tokenizer = new StringTokenizer(s);

			int score = 0;
			while (tokenizer.hasMoreElements()) {
				String word = tokenizer.nextToken();
				score += wordDictionary.getSentiment(word);
			}

			return score;
		};
		int result = mapper.apply(text);
		return result > 0 ? ClassifierResult.GOOD : ClassifierResult.BAD;
	}
}
