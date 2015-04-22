package com.gpachov.masterthesis.classifiers;

import com.gpachov.masterthesis.SampleData;

public class NaiveBayesClassifierTest extends ClassifierTest {

	@Override
	protected Classifier instantiateClassifier(SampleData s) {
		return new NaiveBayesClassifier(s);
	}

}
