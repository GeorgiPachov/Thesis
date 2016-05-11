package com.gpachov.masterthesis.analyzer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.gpachov.masterthesis.Constants;
import com.gpachov.masterthesis.utils.Pair;
import com.gpachov.masterthesis.utils.Utils;
import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.DataClass;

public class BaseSentimentAnalyzer implements SentimentAnalyzer {

    private float matchRate;

    @Override
    public Map<String, Pair<DataClass, ClassificationResult>> analyze(Classifier classifier, Map<DataClass, List<String>> testData) {
	Map<String, Pair<DataClass, ClassificationResult>> map = new LinkedHashMap<String, Pair<DataClass, ClassificationResult>>();
	final AtomicInteger matchCount = new AtomicInteger(0);

	testData.entrySet().stream().forEach(e -> {
	    DataClass dataClass = e.getKey();
	    List<String> sentences = e.getValue();
	    sentences.stream().forEach(s -> {
		ClassificationResult classificationResult = classifier.classify(s);
		map.put(s, new Pair<DataClass, ClassificationResult>(dataClass, classificationResult));
		if (Utils.mapDataClassToClassifierResult(dataClass) == classificationResult) {
		    matchCount.incrementAndGet();
		} else {
		    if (Constants.DEBUG_ANALYZER) {
			System.out.println("Mistake: " + s + "=>" + classificationResult);
		    }
		}
	    });
	});
	return map;
    }

    @Override
    public float getMatchRate() {
	if (matchRate == 0.0f) {
	    throw new RuntimeException("YOu must call this after you call analyze!");
	}
	return matchRate;
    }
}
