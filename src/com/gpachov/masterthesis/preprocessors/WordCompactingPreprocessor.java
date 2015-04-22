package com.gpachov.masterthesis.preprocessors;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WordCompactingPreprocessor implements Preprocessor {

	@Override
	public String applyPreprocessing(String opinion) {
		return Arrays.stream(opinion.split("\\s+")).distinct()
				.collect(Collectors.joining(" "));
	}

}
