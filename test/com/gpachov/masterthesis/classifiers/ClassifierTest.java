package com.gpachov.masterthesis.classifiers;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.TestClass;

import com.gpachov.masterthesis.DataPreprocessor;
import com.gpachov.masterthesis.DataProviderWrapper;
import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.analyzer.CrossvalidatorSentimentAnalyzer;
import com.gpachov.masterthesis.preprocessors.DefaultPreprocessor;
import com.gpachov.masterthesis.preprocessors.WordCompactingPreprocessor;

public abstract class ClassifierTest {

	@Test
	public void testClassifier() throws Exception {
		final DataPreprocessor dataPreprocessor = new DataPreprocessor(DataProviderWrapper.getInstance(),
				new DefaultPreprocessor());
		final ClassifierFactory classifierFactory = s -> instantiateClassifier(s);

		final CrossvalidatorSentimentAnalyzer crossvalidator = new CrossvalidatorSentimentAnalyzer(10,
				classifierFactory, dataPreprocessor);
		crossvalidator.analyze();
		float matchRate = crossvalidator.getMatchRate();
		
		System.out.println(matchRate);
		Assert.assertTrue(matchRate > 0.7);
	}

	protected abstract Classifier instantiateClassifier(SampleData s);
}
