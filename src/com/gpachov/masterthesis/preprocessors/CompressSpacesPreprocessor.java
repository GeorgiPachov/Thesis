package com.gpachov.masterthesis.preprocessors;


public class CompressSpacesPreprocessor implements Preprocessor {

	@Override
	public String applyPreprocessing(String opinion) {
		return opinion.replaceAll("\\s+", " ").toLowerCase();
	}

}
