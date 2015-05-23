package com.gpachov.masterthesis.preprocessors;

import java.util.Arrays;
import java.util.stream.Collectors;


public class CompressSpacesPreprocessor implements Preprocessor {

	@Override
	public String applyPreprocessing(String opinion) {
		String compressedWord = opinion.replaceAll("\\s+", " ").toLowerCase();
		return Arrays.stream(compressedWord.split("\\s+")).filter(s -> !s.trim().equals("")).collect(Collectors.joining(" "));
	}

}
