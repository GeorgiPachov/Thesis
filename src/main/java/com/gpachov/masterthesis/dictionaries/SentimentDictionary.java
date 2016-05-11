package com.gpachov.masterthesis.dictionaries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class SentimentDictionary implements Dictionary {
	private static final Path NEGATIVE_FILE = Paths
			.get("resources/negative-words.txt");
	private static final Path POSITIVE_FILE = Paths
			.get("resources/positive-words.txt");
	private static final int VALUE_POSITIVE = 1;
	private static final Integer VALUE_NEGATIVE = -1;
	private static final int VALUE_NEUTRAL = 0;
	private HashMap<String, Integer> wordPositivity = new HashMap<>();

	public SentimentDictionary() {
		try {
			Files.readAllLines(POSITIVE_FILE).stream()
					.forEach(s -> wordPositivity.put(s, VALUE_POSITIVE));
			Files.readAllLines(NEGATIVE_FILE).stream()
					.forEach(s -> wordPositivity.put(s, VALUE_NEGATIVE));
		} catch (IOException e) {
			throw new RuntimeException("Error building word dictionary!", e);
		}
	}

	public int getSentiment(String word) {
		if (wordPositivity.containsKey(word)) {
			return wordPositivity.get(word);
		}
		return VALUE_NEUTRAL;
	}

	@Override
	public Set<String> getAllWords() {
		return wordPositivity.keySet().stream().collect(Collectors.toSet());
	}
}
