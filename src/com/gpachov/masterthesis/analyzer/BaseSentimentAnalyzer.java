package com.gpachov.masterthesis.analyzer;

import java.util.HashMap;
import java.util.Map;

import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.ClassifierResult;

public class BaseSentimentAnalyzer implements SentimentAnalyzer {
	private Classifier classifier;
	private SampleData sampleData;
	private float matchRate;

	public BaseSentimentAnalyzer(Classifier classifier, SampleData sampleData){
		this.classifier = classifier;
		this.sampleData = sampleData;
		this.matchRate = 0.0f;
	}
	
	/* (non-Javadoc)
	 * @see com.gpachov.masterthesis.Analyzer#analyze()
	 */
	@Override
	public Map<String, ClassifierResult> analyze(){
		Map<String, ClassifierResult> map = new HashMap<String, ClassifierResult>();
		float matchCount = 0;
		for (String positiveExpectation : sampleData.getPositiveSamples()) {
			ClassifierResult classifierResult = classifier.classify(positiveExpectation);
			map.put(positiveExpectation, classifierResult);

			matchCount += ClassifierResult.Utils.isConsideredPositive(classifierResult) ? 1 : 0;
		}

		for (String negativeExpectation : sampleData.getNegativeSamples()) {
			ClassifierResult classifierResult = classifier.classify(negativeExpectation);
			map.put(negativeExpectation, classifierResult);
			
			matchCount += ClassifierResult.Utils.isConsideredNegative(classifierResult) ? 1 : 0;
		}
		
		this.matchRate = (matchCount / (2*sampleData.getSampleSize()));
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
