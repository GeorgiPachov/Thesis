package com.gpachov.masterthesis.extract;

import java.util.List;

@FunctionalInterface
public interface SentenceExtractor {
	List<String> extractRelevant(String input, String relevant);
}
