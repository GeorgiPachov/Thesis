package com.gpachov.masterthesis.progress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gpachov.masterthesis.PreprocessingInfo;
import com.gpachov.masterthesis.SampleData;
import com.gpachov.masterthesis.classifiers.ClassifierResult;

public class LoggingProgressReport implements ProgressReport{
	private static final Logger logger = LoggerFactory.getLogger(LoggingProgressReport.class);
	
	@Override
	public void onPreprocessingStarted(PreprocessingInfo preprocessor) {
		logger.info("Started preprocessing data with " + preprocessor.toString());
	}

	@Override
	public void onPreprocessingFinished() {
		logger.info("Preprocessing data finished.");
	}

	@Override
	public void onWordPreprocessed(String word) {
		logger.trace("Preprocessing word " + word);
	}

	@Override
	public void onOpinionPreprocessed(String opinion, String newOpinion) {
		logger.trace("Preprocessed opinion" + opinion + " into " + newOpinion);
	}

	@Override
	public void onClassificationStarted(SampleData sampleData) {
		logger.info("Classification started with " + sampleData);
	}

	@Override
	public void onOpinionClassified(String opinion, ClassifierResult result) {
		logger.trace(opinion + " classified as " + result);
	}

	@Override
	public void onClassificationFinished() {
		logger.info("Classification phase finished. ");
	}

	@Override
	public void onDatasetAnalyzeFinished() {
		logger.info("Dataset analysis finished. ");
	}

}
