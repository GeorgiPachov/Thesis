package com.gpachov.masterthesis.classifiers;

import java.util.List;
import java.util.Map;

public class SemanticClassifierTest extends ClassifierTest{

    @Override
    protected Classifier instantiateClassifier(Map<DataClass, List<String>> s) {
	return new SemanticClassifier(s);
    }
}
