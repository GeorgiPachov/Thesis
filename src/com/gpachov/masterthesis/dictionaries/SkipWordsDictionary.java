package com.gpachov.masterthesis.dictionaries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkipWordsDictionary implements Dictionary {

	private static final Path SKIP_WORDS_FILE = Paths
			.get("resources/skip-words.txt");
	private Set<String> skipWords;
	public SkipWordsDictionary() {
		try {
			this.skipWords = Files.lines(SKIP_WORDS_FILE).filter( l -> {
				l = l.replaceAll("\\s+", "");
				return !l.startsWith("#");	
//				return true;
			}).collect(Collectors.toSet());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<String> getAllWords() {
		return skipWords;
	}

}
