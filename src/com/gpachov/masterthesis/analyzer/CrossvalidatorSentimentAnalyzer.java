package com.gpachov.masterthesis.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gpachov.masterthesis.IDataProvider;
import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.ClassifierFactory;
import com.gpachov.masterthesis.classifiers.ClassifierResult;
import com.gpachov.masterthesis.utils.Utils;

public class CrossvalidatorSentimentAnalyzer implements SentimentAnalyzer {

	private int crossValidationPieces;
	private IDataProvider dataProviderWrapper;
	private ClassifierFactory classifierFactory;
	private float matchRate;

	public CrossvalidatorSentimentAnalyzer(int crossvalidationPieces, ClassifierFactory classifierFactory,
			IDataProvider dataProviderWrapper) {
		this.crossValidationPieces = crossvalidationPieces;
		this.dataProviderWrapper = dataProviderWrapper;
		this.classifierFactory = classifierFactory;
	}

	public CrossvalidatorSentimentAnalyzer(ClassifierFactory classifierFactory, IDataProvider dataProviderWrapper) {
		this(10, classifierFactory, dataProviderWrapper);
	}

	@Override
	public Map<String, ClassifierResult> analyze() {
		final Map<String, ClassifierResult> result = new HashMap<String, ClassifierResult>();

		int matchCount = 0;
		for (int i = 0; i < crossValidationPieces; i++) {
			List<String> negativeSentences = dataProviderWrapper.getNegative();
			List<String> positiveSentences = dataProviderWrapper.getPositive();

			int dataSampleSize = (int) (negativeSentences.size() / (float) crossValidationPieces);

			List<String> positiveSamples = Utils.subList(positiveSentences, i * dataSampleSize, (i + 1)
					* dataSampleSize);
			positiveSentences.removeAll(positiveSamples);

			List<String> negativeSamples = Utils.subList(negativeSentences, i * dataSampleSize, (i + 1)
					* dataSampleSize);
			negativeSentences.removeAll(negativeSamples);

			SampleData sampleData = new SampleData(positiveSamples, negativeSamples, positiveSentences,
					negativeSentences);
			Classifier classifier = classifierFactory.newInstance(sampleData);
			BaseSentimentAnalyzer analyzer = new BaseSentimentAnalyzer(classifier, sampleData);
			Map<String, ClassifierResult> tempResult = analyzer.analyze();
			result.putAll(tempResult);

			for (Entry<String, ClassifierResult> entry : tempResult.entrySet()) {
				if (positiveSamples.contains(entry.getKey())) {
					if (entry.getValue().equals(ClassifierResult.GOOD)) {
						matchCount++;
					}
				} else if (negativeSamples.contains(entry.getKey())) {
					if (entry.getValue().equals(ClassifierResult.BAD)) {
						matchCount++;
					}
				}
			}
		}
		this.matchRate = ((float) matchCount / result.size());
		return result;
	}

	@Override
	public float getMatchRate() {
		return matchRate;
	}

}
