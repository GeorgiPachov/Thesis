package com.gpachov.masterthesis.classifiers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SimpleLexiconClassifierTest extends ClassifierTest{

    @Override
    protected Classifier instantiateClassifier(Map<DataClass, List<String>> s) {
	return new SimpleLexiconClassifier(s);
    }
}