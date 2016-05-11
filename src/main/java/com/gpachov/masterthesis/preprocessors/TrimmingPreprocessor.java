package com.gpachov.masterthesis.preprocessors;

public class TrimmingPreprocessor implements Preprocessor {
	@Override
	public String applyPreprocessing(String opinion) {
		return opinion.trim();
	}
}
