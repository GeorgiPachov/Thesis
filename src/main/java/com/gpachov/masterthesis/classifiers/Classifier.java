package com.gpachov.masterthesis.classifiers;

import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.progress.ProgressReporting;

public abstract class Classifier implements ProgressReporting {
	protected Map<DataClass, List<String>> sampleData;
	public Classifier(Map<DataClass, List<String>> trainingData){
		this.sampleData = trainingData;
	}
	public abstract ClassificationResult classify(String text);
}
