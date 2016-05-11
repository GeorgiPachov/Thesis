package com.gpachov.masterthesis.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import com.gpachov.masterthesis.TrainingData;
import com.gpachov.masterthesis.TrainingDataBuilder;
import com.gpachov.masterthesis.provider.IDataProvider;
import com.gpachov.masterthesis.utils.Pair;
import com.gpachov.masterthesis.utils.Utils;
import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.ClassifierFactory;
import com.gpachov.masterthesis.classifiers.DataClass;

public class CrossvalidatorSentimentAnalyzer implements SentimentAnalyzer {

	private int crossValidationPieces;
	private IDataProvider dataProviderWrapper;
	private ClassifierFactory classifierFactory;
	private float matchRate;

	public CrossvalidatorSentimentAnalyzer(ClassifierFactory classifierFactory, IDataProvider dataProviderWrapper) {
	    this(10, classifierFactory, dataProviderWrapper);
	}

    public CrossvalidatorSentimentAnalyzer(int crossvalidationPieces, ClassifierFactory classifierFactory, IDataProvider dataProviderWrapper) {
	this.crossValidationPieces = crossvalidationPieces;
	this.dataProviderWrapper = dataProviderWrapper;
	this.classifierFactory = classifierFactory;
    }

    @Override
    public Map<String, Pair<DataClass, ClassificationResult>> analyze(Classifier ignored, Map<DataClass, List<String>> testData) {
	final Map<String, Pair<DataClass, ClassificationResult>> result = new HashMap<String, Pair<DataClass, ClassificationResult>>();

	Holder<Integer> matchCount = new Holder<Integer>();
	matchCount.value = 0;
	TrainingDataBuilder builder = new TrainingDataBuilder(dataProviderWrapper, crossValidationPieces);
	for (TrainingData trainingData : builder) {
	    Classifier classifier = classifierFactory.newInstance(trainingData.getReal());
	    Map<String, Pair<DataClass, ClassificationResult>> analyzed = new BaseSentimentAnalyzer().analyze(classifier, trainingData.getSamples());
	    result.putAll(analyzed);
	    analyzed.entrySet().stream().forEach(e -> {
		if (Utils.mapDataClassToClassifierResult(e.getValue().getFirst()) == e.getValue().getSecond()) {
		    matchCount.value++;
		}
	    });
	}

	this.matchRate = ((float) matchCount.value / result.size());
	return result;
    }

	@Override
	public float getMatchRate() {
		return matchRate;
	}

}
