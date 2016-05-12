package com.gpachov.masterthesis.preprocessors;


public class TagStrippingPreprocessor implements Preprocessor {
	@Override
	public String applyPreprocessing(String opinion) {
		return opinion.replaceAll("<.*?>", " ");
	}
}