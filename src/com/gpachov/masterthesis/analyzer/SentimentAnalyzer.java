package com.gpachov.masterthesis.analyzer;

import java.util.Map;

import com.gpachov.masterthesis.classifiers.ClassifierResult;
import com.gpachov.masterthesis.progress.ProgressReporting;

public interface SentimentAnalyzer extends ProgressReporting{

	/**
	 * @return between 0 and 1
	 */
	public abstract Map<String, ClassifierResult> analyze();

	public abstract float getMatchRate();

}