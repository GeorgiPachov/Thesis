package com.gpachov.masterthesis.analyzer;

import java.util.Map;

import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.progress.ProgressReporting;
import com.gpachov.masterthesis.utils.Pair;

public interface SentimentAnalyzer extends ProgressReporting{

	public abstract Map<String, Pair<DataClass, ClassificationResult>> analyze();

	public abstract float getMatchRate();

}