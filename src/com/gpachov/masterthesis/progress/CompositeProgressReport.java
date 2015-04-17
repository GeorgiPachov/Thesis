package com.gpachov.masterthesis.progress;

import java.util.ArrayList;
import java.util.List;

import com.gpachov.masterthesis.IDataPreprocessor;
import com.gpachov.masterthesis.PreprocessingInfo;
import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.classifiers.ClassifierResult;

public class CompositeProgressReport implements ProgressReport {

	private List<ProgressReport> subscribers = new ArrayList<ProgressReport>();
	public void addSubscriber(ProgressReport subscriber){
		this.subscribers.add(subscriber);
	}

	@Override
	public void onPreprocessingStarted(PreprocessingInfo preprocessingInfo) {
		subscribers.forEach(s -> s.onPreprocessingStarted(preprocessingInfo));
	}

	@Override
	public void onPreprocessingFinished() {
		subscribers.forEach(s -> s.onPreprocessingFinished());
	}

	@Override
	public void onWordPreprocessed(String word) {
		subscribers.forEach(s -> s.onWordPreprocessed(word));
	}

	@Override
	public void onOpinionPreprocessed(String opinion, String newOpinion) {
		subscribers.forEach(s -> s.onOpinionPreprocessed(opinion, newOpinion));
	}

	@Override
	public void onOpinionClassified(String opinion, ClassifierResult result) {
		subscribers.forEach(s -> s.onOpinionClassified(opinion, result));
	}

	@Override
	public void onDatasetAnalyzeFinished() {
		subscribers.forEach(s -> s.onDatasetAnalyzeFinished());
	}

	@Override
	public void onClassificationStarted(SampleData sampleData) {
		subscribers.forEach( s -> s.onClassificationStarted(sampleData));
	}

	@Override
	public void onClassificationFinished() {
		subscribers.forEach( s -> s.onClassificationFinished());
	}

}
