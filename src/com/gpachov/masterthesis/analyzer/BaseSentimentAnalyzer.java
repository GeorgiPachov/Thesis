package com.gpachov.masterthesis.analyzer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.utils.Pair;
import com.gpachov.masterthesis.utils.Utils;

public class BaseSentimentAnalyzer implements SentimentAnalyzer {
	private Classifier classifier;
	private float matchRate;
	Map<DataClass, List<String>> testData;

	public BaseSentimentAnalyzer(Classifier classifier, Map<DataClass, List<String>> testData){
		this.classifier = classifier;
		this.testData = testData;
		this.matchRate = 0.0f;
	}
	
	/* (non-Javadoc)
	 * @see com.gpachov.masterthesis.Analyzer#analyze()
	 */
	@Override
	public Map<String, Pair<DataClass, ClassificationResult>> analyze(){
	    Map<String, Pair<DataClass, ClassificationResult>> map = new LinkedHashMap<String, Pair<DataClass, ClassificationResult>>();
		final AtomicInteger matchCount = new AtomicInteger(0);

		testData.entrySet().stream().forEach( e-> {
			DataClass dataClass = e.getKey();
			List<String> sentences = e.getValue();
			sentences.stream().forEach(s -> {
				ClassificationResult classificationResult = classifier.classify(s);
				map.put(s,new Pair<DataClass, ClassificationResult>(dataClass, classificationResult));
				if (Utils.mapDataClassToClassifierResult(dataClass )== classificationResult) {
					matchCount.incrementAndGet();
				}
			});
		});
		return map;
	}
	
	/* (non-Javadoc)
	 * @see com.gpachov.masterthesis.Analyzer#getMatchRate()
	 */
	@Override
	public float getMatchRate(){
		if (matchRate == 0.0f){
			throw new RuntimeException("YOu must call this after you call analyze!");
		}
		return matchRate;
	}
}
