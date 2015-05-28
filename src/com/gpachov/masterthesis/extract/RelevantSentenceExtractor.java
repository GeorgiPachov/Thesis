package com.gpachov.masterthesis.extract;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RelevantSentenceExtractor implements SentenceExtractor {

	@Override
	public List<String> extractRelevant(String input, String query) {
		String sentenceEndings = "!|\\.\\.\\.|\\.|\\?";
		String[] sentences = input.split(sentenceEndings);
		List<String> opinions = Arrays.stream(sentences)
				.map(sentence -> sentence.split("\n{2,}"))
				.flatMap(lines -> Arrays.stream(lines))
				.collect(Collectors.toList());
		return opinions.stream()
				.filter(s -> query!=null ? s.toLowerCase().contains(query.toLowerCase()) : true)
				.collect(Collectors.toList());
	}
}
