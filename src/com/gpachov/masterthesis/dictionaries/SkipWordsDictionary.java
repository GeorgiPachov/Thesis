package com.gpachov.masterthesis.dictionaries;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkipWordsDictionary implements Dictionary {

	private static final URL SKIP_WORDS_FILE = SkipWordsDictionary.class.getClassLoader().getResource("resources/skip-words.txt");
	private Set<String> skipWords;
	public SkipWordsDictionary() {
		try {
			this.skipWords = Files.lines(Paths.get(SKIP_WORDS_FILE.toURI())).filter( l -> {
				l = l.replaceAll("\\s+", "");
				return !l.startsWith("#");	
//				return true;
			}).collect(Collectors.toSet());
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<String> getAllWords() {
		return skipWords;
	}

}
