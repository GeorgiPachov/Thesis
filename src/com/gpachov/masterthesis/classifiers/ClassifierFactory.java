package com.gpachov.masterthesis.classifiers;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface ClassifierFactory {
	public Classifier newInstance(Map<DataClass, List<String>> sampleData);
}
