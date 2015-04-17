package com.gpachov.masterthesis.classifiers;

import com.gpachov.masterthesis.ClassifierData;
import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.progress.ProgressReporting;

public abstract class Classifier implements ProgressReporting {
	protected ClassifierData sampleData;
	public Classifier(ClassifierData sampleData){
		this.sampleData = sampleData;
	}
	public abstract ClassifierResult classify(String text);
}
