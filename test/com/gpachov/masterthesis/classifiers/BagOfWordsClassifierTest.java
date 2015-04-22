package com.gpachov.masterthesis.classifiers;

import com.gpachov.masterthesis.SampleData;

public class BagOfWordsClassifierTest extends ClassifierTest {

	@Override
	protected Classifier instantiateClassifier(SampleData s) {
		return new BagOfWordsClassifier(s);
	}
	
}
