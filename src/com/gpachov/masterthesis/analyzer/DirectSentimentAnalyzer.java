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
	private Classifier classifier;

	public DirectSentimentAnalyzer(List<String> sentences, Classifier classifier) {
		this.sentences = sentences;
		this.classifier = classifier;
	}
	
	@Override
	public Map<String, Pair<DataClass, ClassificationResult>> analyze() {
		return sentences.stream().collect(Collectors.toMap(k->k, v->new Pair<DataClass, ClassificationResult> (null, classifier.classify(v))));
	}

	@Override
	public float getMatchRate() {
		return 0;
	}

}
