package com.gpachov.masterthesis.analyzer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Holder;

import com.gpachov.masterthesis.TrainingData;
import com.gpachov.masterthesis.TrainingDataBuilder;
import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.ClassifierFactory;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.provider.IDataProvider;
import com.gpachov.masterthesis.utils.Pair;
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
	public Map<String, Pair<DataClass, ClassificationResult>> analyze() {
		final Map<String, Pair<DataClass, ClassificationResult>> result = new HashMap<String, Pair<DataClass, ClassificationResult>>();

		Holder<Integer> matchCount = new Holder<Integer>();
		matchCount.value = 0;
		TrainingDataBuilder builder = new TrainingDataBuilder(dataProviderWrapper, crossValidationPieces);
		for (TrainingData trainingData : builder){
			Classifier classifier = classifierFactory.newInstance(trainingData.getReal());
			Map<String, Pair<DataClass, ClassificationResult>> analyzed = new BaseSentimentAnalyzer(classifier, trainingData.getSamples()).analyze();
			result.putAll(analyzed);
			analyzed.entrySet().stream().forEach(e -> {
				if (Utils.mapDataClassToClassifierResult(e.getValue().getFirst()) == e.getValue().getSecond()){
					matchCount.value++;
				}
			});
		}
		
		this.matchRate = ((float) matchCount.value / result.size());
		return result;
	}

	private DataClass findDataClassOf(TrainingData trainingData, String key) {
		Holder<DataClass> holder = new Holder<DataClass>();
		trainingData.getAll().forEach((k,v) -> {
			if (v.contains(key)){
				holder.value = k;
			}
		});
		return holder.value;
	}

	@Override
	public float getMatchRate() {
		return matchRate;
	}

}
