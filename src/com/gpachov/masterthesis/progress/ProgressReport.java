package com.gpachov.masterthesis.progress;

import com.gpachov.masterthesis.PreprocessingInfo;
import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.classifiers.ClassifierResult;

public interface ProgressReport {
	void onPreprocessingStarted(PreprocessingInfo preprocessingInfo);
	void onPreprocessingFinished();
	void onWordPreprocessed(String word);
	void onOpinionPreprocessed(String opinion, String workingOpinion);
	void onClassificationStarted(SampleData sampleData);
	void onOpinionClassified(String opinion, ClassifierResult result);
	void onClassificationFinished();
	void onDatasetAnalyzeFinished();
}
