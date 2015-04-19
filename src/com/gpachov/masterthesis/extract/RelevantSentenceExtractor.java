package com.gpachov.masterthesis.extract;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RelevantSentenceExtractor implements Extractor {

	@Override
	public List<String> extractRelevant(String input, String query) {
		String sentenceEndings = "!|\\.\\.\\.|\\.|\\?";
		String[] sentences = input.split(sentenceEndings);
		List<String> opinions = Arrays.stream(sentences)
				.map(sentence -> sentence.split("\n{2,}"))
				.flatMap(lines -> Arrays.stream(lines))
				.collect(Collectors.toList());
		return opinions.stream()
				.filter(s -> s.toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
	}
}
