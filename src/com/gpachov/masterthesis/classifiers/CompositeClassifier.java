package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.SampleData;

public class CompositeClassifier extends Classifier{

	private List<Classifier> classifiers;

	public CompositeClassifier(SampleData sampleData, Classifier... classifiers) {
		super(sampleData);
		this.classifiers = Arrays.asList(classifiers);
	}

	@Override
	public ClassifierResult classify(String text) {
		List<ClassifierResult> results = classifiers.stream().map(c -> c.classify(text)).collect(Collectors.toList());
		long goodCount = results.stream().filter( c -> c == ClassifierResult.GOOD).count();
		long badCount = results.stream().filter( c -> c == ClassifierResult.BAD).count(); 
		return goodCount > badCount ? ClassifierResult.GOOD : ClassifierResult.BAD;
	}

}
