package com.gpachov.masterthesis.classifiers;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gpachov.masterthesis.analyzer.CrossvalidatorSentimentAnalyzer;
import com.gpachov.masterthesis.preprocessors.DefaultPreprocessor;
import com.gpachov.masterthesis.provider.DataPreprocessor;
import com.gpachov.masterthesis.provider.DatabaseDataProvider;

public abstract class ClassifierTest {

    protected DataPreprocessor dataPreprocessor;
    private ClassifierFactory classifierFactory;

    @Before
    public void setup(){
	this.dataPreprocessor = new DataPreprocessor(DatabaseDataProvider.getInstance(), new DefaultPreprocessor());
	this.classifierFactory = s -> instantiateClassifier(s);
    }
    
    @Test
    public void testClassifier() throws Exception {
	final CrossvalidatorSentimentAnalyzer crossvalidator = new CrossvalidatorSentimentAnalyzer(10, classifierFactory, dataPreprocessor);
	crossvalidator.analyze(null, null);
	float matchRate = crossvalidator.getMatchRate();

	System.out.println(matchRate);
	Assert.assertTrue(matchRate > 0.7);
    }

    protected abstract Classifier instantiateClassifier(Map<DataClass, List<String>> s);
}
