package com.gpachov.masterthesis.extract;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RelevantSentenceExtractor implements Extractor {

	@Override
	public List<String> extractRelevant(String input, String query) {
		String sentenceEndings = "!|\\.\\.\\.|\\.|\\?";
		String[] sentences = input.split(sentenceEndings);
		return Arrays.stream(sentences)
				.filter(s -> s.toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
	}
}
