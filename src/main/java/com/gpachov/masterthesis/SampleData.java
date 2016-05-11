package com.gpachov.masterthesis;

import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.classifiers.DataClass;

public class SampleData implements TrainingData {
	private Map<DataClass, List<String>> classified;
	private Map<DataClass, List<String>> samples;
	private Map<DataClass, List<String>> real;

	public SampleData(Map<DataClass, List<String>> classified, Map<DataClass, List<String>> samples, Map<DataClass, List<String>> real) {
		this.classified = classified;
		this.samples = samples;
		this.real = real;
	}

	@Override
	public Map<DataClass, List<String>> getAll() {
		return classified;
	}

	@Override
	public Map<DataClass, List<String>> getSamples() {
		return samples;
	}

	@Override
	public Map<DataClass, List<String>> getReal() {
		return real;
	}

}
