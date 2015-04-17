package com.gpachov.masterthesis.classifiers;

import com.gpachov.masterthesis.SampleData;

@FunctionalInterface
public interface ClassifierFactory {
	public Classifier newInstance(SampleData sampleData);
}
