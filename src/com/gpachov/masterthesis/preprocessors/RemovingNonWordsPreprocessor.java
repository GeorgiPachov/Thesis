package com.gpachov.masterthesis.preprocessors;


public class RemovingNonWordsPreprocessor implements Preprocessor {

	@Override
	public String applyPreprocessing(String opinion) {
		return opinion.replaceAll("\\W", " ");
	}

}
