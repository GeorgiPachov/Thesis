package com.gpachov.masterthesis.preprocessors;

@FunctionalInterface
public interface Preprocessor {
	public abstract String applyPreprocessing(String opinion);
}
