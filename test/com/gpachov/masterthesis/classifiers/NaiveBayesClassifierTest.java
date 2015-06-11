package com.gpachov.masterthesis.classifiers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gpachov.masterthesis.TrainingData;
import com.gpachov.masterthesis.TrainingDataBuilder;

public class NaiveBayesClassifierTest extends ClassifierTest {
	private NaiveBayesClassifier classifier;

	@Override
	protected NaiveBayesClassifier instantiateClassifier(Map<DataClass, List<String>> s) {
		return new NaiveBayesClassifier(s);
	}
	
	@Override
	public void setup() {
	    super.setup();

	    TrainingDataBuilder dataBuilder = new TrainingDataBuilder(super.dataPreprocessor, 1);
	    for (TrainingData tData : dataBuilder){
		this.classifier = instantiateClassifier(tData.getAll());
	    }
	}

	public void testPositiveWords() throws Exception {
	    assertFalse(classifier.classify("good").equals(ClassificationResult.NEGATIVE));
	    assertTrue(classifier.classify("great").equals(ClassificationResult.POSITIVE));
	    assertTrue(classifier.classify("fantastic").equals(ClassificationResult.POSITIVE));
	}
}
