package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.utils.ConcatenatingSlidingWindowSpliterator;
import com.gpachov.masterthesis.utils.LayeredBayesCache;

public class LayeredBayesClassifier extends Classifier {

	private static final int MAX_NGRAM_SIZE = 3; // words.length
	private final LayeredBayesCache positiveOccurenceCache = new LayeredBayesCache();
	private final LayeredBayesCache negativeOccurenceCache = new LayeredBayesCache();
	private final Map<Integer, Integer> positiveNgramLayerWordCount = new HashMap<Integer, Integer>();
	private final Map<Integer, Integer> negativeNgramLayerWordCount = new HashMap<Integer, Integer>();

	public LayeredBayesClassifier(SampleData sampleData) {
		super(sampleData);
		sampleData.getPositive().forEach(
				sentence -> {
					List<String> words = Arrays.asList(sentence.split("\\s+"));
					for (int ngramSize = MAX_NGRAM_SIZE; ngramSize > 0; ngramSize--) {
						Stream<String> ngramsStream = StreamSupport.stream(new ConcatenatingSlidingWindowSpliterator(
								words, ngramSize), false);

						int ngramClone = ngramSize;
						ngramsStream.forEach(ngram -> {
							Integer occurences = positiveOccurenceCache.get(ngramClone, ngram);
							if (occurences == null) {
								positiveOccurenceCache.put(ngramClone, ngram, 1);
							} else {
								positiveOccurenceCache.put(ngramClone, ngram, occurences + 1);
							}
						});
					}
				});

		sampleData.getNegative().forEach(
				sentence -> {
					List<String> words = Arrays.asList(sentence.split("\\s+"));
					for (int ngramSize = MAX_NGRAM_SIZE; ngramSize > 0; ngramSize--) {
						Stream<String> ngramsStream = StreamSupport.stream(new ConcatenatingSlidingWindowSpliterator(
								words, ngramSize), false);

						int ngramClone = ngramSize;
						ngramsStream.forEach(ngram -> {
							Integer occurences = negativeOccurenceCache.get(ngramClone, ngram);
							if (occurences == null) {
								negativeOccurenceCache.put(ngramClone, ngram, 1);
							} else {
								negativeOccurenceCache.put(ngramClone, ngram, occurences + 1);
							}
						});
					}
				});

		for (int ngramSize = MAX_NGRAM_SIZE; ngramSize > 0; ngramSize--) {
			int negativeNgrams = negativeOccurenceCache.getRow(ngramSize).entrySet().stream().map(e -> e.getValue()).reduce(Integer::sum).get();
			negativeNgramLayerWordCount.put(ngramSize, negativeNgrams);
			int positiveNgrams = positiveOccurenceCache.getRow(ngramSize).entrySet().stream().map(e->e.getValue()).reduce(Integer::sum).get();
			positiveNgramLayerWordCount.put(ngramSize, positiveNgrams);
		}
		
	}

	@Override
	public ClassifierResult classify(String text) {
		List<String> words = Arrays.asList(text.split("\\s+"));
		int ngramSize = MAX_NGRAM_SIZE;
		final double[][] probabilities = new double[ngramSize + 1][2];
		for (; ngramSize > 0; ngramSize--) {
			Stream<String> ngramsStream = StreamSupport.stream(new ConcatenatingSlidingWindowSpliterator(words,
					ngramSize), false);

			int ngramClone = ngramSize;
			double[] probability = new double[] {1, 1};
			ngramsStream.forEach(ngram -> {
				probability[0] *= getPositiveProbability(ngram, ngramClone);
				probability[1] *= getNegativeProbability(ngram, ngramClone);
			});

			probabilities[ngramSize] = probability;
		}
		probabilities[0] = new double[] { 1, 1 };
		return classify(probabilities);
	}

	private ClassifierResult classify(double[][] probabilities) {
		final double[] finalProbabilites = Arrays.stream(probabilities).reduce(new double[] { 1, 1 },
				(a, b) -> new double[] { a[0] * b[0], a[1] * b[1] });
		double finalPositive = finalProbabilites[0];
		double finalNegative = finalProbabilites[1];
		return finalPositive > finalNegative ? ClassifierResult.GOOD : ClassifierResult.BAD;
	}

	private double getNegativeProbability(String ngram, int ngramSize) {
		Integer occurences = negativeOccurenceCache.get(ngramSize, ngram);

		return ((double) (occurences == null ? 1 : occurences))
				/ (negativeOccurenceCache.getCacheSize(ngramSize) + (positiveNgramLayerWordCount.get(ngramSize) + negativeNgramLayerWordCount
						.get(ngramSize)));
	}

	private double getPositiveProbability(String ngram, int ngramSize) {
		Integer occurences = positiveOccurenceCache.get(ngramSize, ngram);
		return ((double) (occurences == null ? 1 : occurences))
				/ (positiveOccurenceCache.getCacheSize(ngramSize) + (positiveNgramLayerWordCount.get(ngramSize) + negativeNgramLayerWordCount
						.get(ngramSize)));
	}

}
