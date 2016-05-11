package com.gpachov.masterthesis.preprocessors;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.dictionaries.SkipWordsDictionary;

public class SkipWordsPreprocessor implements Preprocessor {

	private SkipWordsDictionary skipWordsDictionary = new SkipWordsDictionary();

	@Override
	public String applyPreprocessing(String opinion) {
		return Arrays.stream(opinion.split("\\s+")).filter(word -> !skipWordsDictionary.getAllWords().contains(word))
				.filter(word -> word.length() >= 3 && !word.equals("no")).collect(Collectors.joining(" "));
	}
}
