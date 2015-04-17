package com.gpachov.masterthesis.extract;

import java.util.List;

@FunctionalInterface
public interface Extractor {
	List<String> extractRelevant(String input, String relevant);
}
