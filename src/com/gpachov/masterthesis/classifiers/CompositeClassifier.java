package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.SampleData;

public class CompositeClassifier extends Classifier {

	private List<Classifier> classifiers;

	public CompositeClassifier(SampleData sampleData, Classifier... classifiers) {
		super(sampleData);
		this.classifiers = Arrays.asList(classifiers);
	}

	@Override
	public ClassifierResult classify(String text) {
		List<ClassifierResult> results = classifiers.stream()
				.map(c -> c.classify(text)).collect(Collectors.toList());
		// Map<ClassifierResult, Integer> partitioned =
		// results.stream().collect(Collectors.groupingBy(s -> s,
		// Collectors.summingInt( s-> 1)));
		Map<ClassifierResult, Integer> partitioned = new HashMap<ClassifierResult, Integer>();
		results.stream().forEach(
				cr -> partitioned.compute(cr, (k, v) -> v == null ? 1 : v + 1));
		return partitioned.entrySet().stream()
				.max((e1, e2) -> e1.getValue() - e2.getValue()).get().getKey();
	}

}
