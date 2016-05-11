package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompositeClassifier extends Classifier {

	private List<Classifier> classifiers;

	public CompositeClassifier(Map<DataClass, List<String>> sampleData, Classifier... classifiers) {
		super(sampleData);
		this.classifiers = Arrays.asList(classifiers);
	}

	@Override
	public ClassificationResult classify(String text) {
		List<ClassificationResult> results = classifiers.stream()
				.map(c -> c.classify(text)).collect(Collectors.toList());

		Map<ClassificationResult, Integer> partitioned = new HashMap<ClassificationResult, Integer>();
		results.stream().forEach(
				cr -> partitioned.compute(cr, (k, v) -> v == null ? 1 : v + 1));
		return partitioned.entrySet().stream()
				.max((e1, e2) -> e1.getValue() - e2.getValue()).get().getKey();
	}

}
