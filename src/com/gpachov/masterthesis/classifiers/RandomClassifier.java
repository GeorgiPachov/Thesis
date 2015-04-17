package com.gpachov.masterthesis.classifiers;

import com.gpachov.masterthesis.SampleData;


public class RandomClassifier extends Classifier{

	public RandomClassifier(SampleData sampleData) {
		super(sampleData);
	}

	@Override
	public ClassifierResult classify(String text) {
		return Math.random() >= 0.5f? ClassifierResult.GOOD : ClassifierResult.BAD;
	}

}
