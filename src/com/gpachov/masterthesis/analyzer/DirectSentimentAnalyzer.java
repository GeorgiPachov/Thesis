package com.gpachov.masterthesis.analyzer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.utils.Pair;

public class DirectSentimentAnalyzer implements SentimentAnalyzer{
	private List<String> sentences;

	public DirectSentimentAnalyzer(List<String> sentences) {
		this.sentences = sentences;
	}
	
	@Override
	public Map<String, Pair<DataClass, ClassificationResult>> analyze(Classifier classifier, Map<DataClass, List<String>> testData) {
		return sentences.stream().collect(Collectors.toMap(k->k, v->new Pair<DataClass, ClassificationResult> (null, classifier.classify(v))));
	}

	@Override
	public float getMatchRate() {
		return 0;
	}

}
