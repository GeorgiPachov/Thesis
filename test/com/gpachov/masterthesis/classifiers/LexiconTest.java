package com.gpachov.masterthesis.classifiers;

import java.util.List;
import java.util.Map;

public class LexiconTest  extends ClassifierTest{

    @Override
    protected Classifier instantiateClassifier(Map<DataClass, List<String>> s) {
	return new LexiconClassifier(s);
    }

}
