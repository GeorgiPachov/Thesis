package com.gpachov.masterthesis.preprocessors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.dictionaries.EnglishDictionary;
import com.gpachov.masterthesis.utils.Levenstein;

public class SpellCorrectionPreprocessor implements Preprocessor {

	private int processedOpinions = 0;
	private EnglishDictionary dictionary = new EnglishDictionary();
	private Map<String, String> cache = new HashMap<String, String>();

	@Override
	public String applyPreprocessing(String opinion) {
		final String dictionaryCorrected = Arrays
				.stream(opinion.split("\\s+"))
				.map(opinionWord -> {
					// TODO
					// nearest dict word cache
					// local caches per word?
					// indexes, to make it faster with less string objects
					// generated?
					if (!dictionary.contains(opinionWord)) {
						if (cache.containsKey(opinionWord)) {
							return cache.get(opinionWord);
						} else {
							Optional<String> findAny = dictionary
									.getAllWords()
									.parallelStream()
									.filter(dictionaryWord -> Levenstein.getLevensteinDistance(opinionWord,
											dictionaryWord) <= 1).findAny();
							findAny.ifPresent(s -> cache.put(opinion, s));
							return findAny.orElse(opinionWord);
						}
					}
					return opinionWord;
				}).collect(Collectors.joining(" "));
		processedOpinions++;
		// System.out.println(processedOpinions);
		return dictionaryCorrected;
	}

}
