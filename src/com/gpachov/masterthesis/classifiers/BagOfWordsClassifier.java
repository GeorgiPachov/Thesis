package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.gpachov.masterthesis.ClassifierData;

public class BagOfWordsClassifier extends Classifier {

	private Map<String, Long> occurencesInPositiveCache = new HashMap<String, Long>();
	private Map<String, Long> occurencesInNegativeCache = new HashMap<String, Long>();
	
	public BagOfWordsClassifier(ClassifierData sampleData) {
		super(sampleData);
		
		sampleData.getPositive().stream().forEach(sentence -> {
			Arrays.stream(sentence.split("\\s+")).forEach(word -> {
				occurencesInPositiveCache.compute(word, (k,v) -> v == null? 1l : v+1);
			});
		});
		
		sampleData.getNegative().stream().forEach(sentence -> {
			Arrays.stream(sentence.split("\\s+")).forEach(word -> {
				occurencesInNegativeCache.compute(word, (k,v) -> v == null? 1l : v+1);
			});
		});
	}

	@Override
	public ClassifierResult classify(String text) {
		final float[] sums = new float[2];
		Arrays.stream(text.split("\\s+")).forEach(word -> {
			long occurencesInPositiveSentences = occurencesInPositiveCache.getOrDefault(word,0l);
			long occurencesInNegativeSentences = occurencesInNegativeCache.getOrDefault(word,0l);
			final long total = occurencesInNegativeSentences + occurencesInPositiveSentences;
			float positive = occurencesInPositiveSentences / ((float) total);
			float negative = 1 - positive;
			sums[0] += positive;
			sums[1] += negative;
		});
		return sums[0]>= sums[1]? ClassifierResult.GOOD : ClassifierResult.BAD;
	}
}
