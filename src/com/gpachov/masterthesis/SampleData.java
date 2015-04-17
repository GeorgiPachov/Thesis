package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.List;

public class SampleData implements ClassifierData {
	private final List<String> positiveSamples;
	private final List<String> negativeSamples;
	private final List<String> positiveSentences;
	private final List<String> negativeSentences;
	private int sampleSize;

	public SampleData(IDataProvider wrapper) {
		List<String> negativeSentences = wrapper.getNegative();
		List<String> positiveSenteces = wrapper.getPositive().subList(0, negativeSentences.size());

		this.sampleSize = (int) ((float) negativeSentences.size() / 10);

		this.positiveSamples = positiveSenteces.subList(0, sampleSize);
		this.negativeSamples = negativeSentences.subList(0, sampleSize);
		this.positiveSentences = positiveSenteces.subList(sampleSize + 1, positiveSenteces.size() - 1);
		this.negativeSentences = negativeSentences.subList(sampleSize + 1, negativeSentences.size() - 1);
	}

	public SampleData(List<String> positiveSamples, List<String> negativeSamples, List<String> positiveSentences,
			List<String> negativeSentences) {
		this.positiveSamples = positiveSamples;
		this.negativeSamples = negativeSamples;
		this.positiveSentences = positiveSentences;
		this.negativeSentences = negativeSentences;
		
		this.sampleSize = negativeSamples.size();
	}

	public List<String> getPositiveSamples() {
		return new ArrayList<String>(positiveSamples);
	}

	public List<String> getNegativeSamples() {
		return new ArrayList<String>(negativeSamples);
	}

	public List<String> getPositive() {
		return new ArrayList<String>(positiveSentences);
	}

	public List<String> getNegative() {
		return new ArrayList<String>(negativeSentences);
	}

	public float getSampleSize() {
		return sampleSize;
	}
}
