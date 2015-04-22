package com.gpachov.masterthesis.classifiers;

import com.gpachov.masterthesis.SampleData;

public class BooleanMultinominativeNaiveBayesClassifierTest extends ClassifierTest {

	@Override
	protected Classifier instantiateClassifier(SampleData s) {
		return new BooleanMultinominalNaiveBayesClassifier(s);
	}
}
