package com.gpachov.masterthesis.dictionaries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import com.gpachov.mastertheysis.exceptions.InitializationException;

public class EnglishDictionary implements Dictionary{

	private Set<String> allWords;

	public EnglishDictionary() {
		try {
			//XXX depending on this returning hash set always
			this.allWords = Files.readAllLines(Paths.get("/home/georgi/Dev/workspace/MasterThesis/resources/words.txt")).stream().map( s -> s.toLowerCase().trim()).collect(Collectors.toSet());
		} catch (IOException e) {
			throw new InitializationException(e);
		}
	}
	
	@Override
	public Set<String> getAllWords() {
		return allWords;
	}
	
	public boolean contains(String word){
		return getAllWords().contains(word);
	}
}
