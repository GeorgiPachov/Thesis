package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gpachov.masterthesis.ClassifierData;
import com.gpachov.masterthesis.SampleData;

public class NaiveBayesClassifier extends Classifier {
	private Map<String, Integer> positiveWordOccurences = new HashMap<String, Integer>();
	private Map<String, Integer> negativeWordOccurences = new HashMap<String, Integer>();
	private int totalPositiveWords;
	private int totalNegativeWords;

	public NaiveBayesClassifier(ClassifierData sampleData) {
		super(sampleData);
		StringBuilder positive = new StringBuilder();
		StringBuilder negative = new StringBuilder();
		
		sampleData.getPositive().forEach(s -> positive.append(" " + s + " "));
		sampleData.getNegative().forEach(s -> negative.append(" " + s + " "));
		
		Arrays.stream(positive.toString().split("\\s+")).forEach( s -> { 
			Integer old = positiveWordOccurences.get(s);
			if (old == null){
				positiveWordOccurences.put(s, 1);
			} else {
				positiveWordOccurences.put(s, old+1);
			}
		});
		
		Arrays.stream(negative.toString().split("\\s+")).forEach( s -> { 
			Integer old = negativeWordOccurences.get(s);
			if (old == null){
				negativeWordOccurences.put(s, 1);
			} else {
				negativeWordOccurences.put(s, old+1);
			}
		});
		
		this.totalPositiveWords = positiveWordOccurences.entrySet().stream().map(e -> e.getValue()).reduce(Integer::sum).get();
		this.totalNegativeWords = negativeWordOccurences.entrySet().stream().map(e -> e.getValue()).reduce(Integer::sum).get();
		
	}

	@Override
	public ClassifierResult classify(String text) {
		double[] finalResult = new double[2];
		finalResult[0] = 1;
		finalResult[1] = 1;
		Arrays.stream(text.split("\\s+")).forEach(s -> {
			double positiveProbability = getPositiveProbability(s);
			double negativeProbability = getNegativeProbability(s);

			finalResult[0] *= positiveProbability;
			finalResult[1] *= negativeProbability;
		});
		double positiveProbability = finalResult[0];
		double negativeProbability = finalResult[1];
		
		ClassifierResult result = classify(positiveProbability, negativeProbability);
		
		getProgressReport().onOpinionClassified(text, result);
		return result;
	}

	private ClassifierResult classify(double positiveProbability, double negativeProbability) {
		if (positiveProbability > negativeProbability) {
			return ClassifierResult.GOOD;
		} else {
			return ClassifierResult.BAD;
		}
	}

	private double getNegativeProbability(String s) {
		Integer occurences = negativeWordOccurences.get(s);
		return (occurences == null? 1 : occurences) / ((float) negativeWordOccurences.size() + (totalNegativeWords + totalPositiveWords));
	}

	private double getPositiveProbability(String s) {
		Integer occurences = positiveWordOccurences.get(s);
		return (occurences == null? 1 : occurences) / ((float) positiveWordOccurences.size() + (totalPositiveWords + totalNegativeWords));
	}

}
