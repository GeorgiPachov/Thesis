package com.gpachov.masterthesis.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosTokenizer;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosType;
import com.gpachov.masterthesis.utils.Utils;

public class NaiveBayesClassifier extends Classifier {
    protected static final boolean DEBUG = false;
    protected Map<String, Map<DataClass, Integer>> wordPerDataClassMapping = new ConcurrentHashMap<String, Map<DataClass, Integer>>();
    protected int totalWordCount;
    protected Map<DataClass, Integer> wordCountPerDataClass = new HashMap<DataClass, Integer>();
    private final Object mutex = new Object();

    public NaiveBayesClassifier(Map<DataClass, List<String>> trainingData) {
	super(trainingData);
	init();
    }

    protected void init() {
	super.sampleData.keySet().forEach(key -> {
	    List<String> sentences = sampleData.get(key);
	    sentences.parallelStream().flatMap(s -> Arrays.stream(s.split("\\s+"))).forEach(word -> {
		HashMap<DataClass, Integer> initializedHashTable = new HashMap<DataClass, Integer>();
		Arrays.stream(DataClass.values()).forEach(d -> initializedHashTable.put(d, 1));
		wordPerDataClassMapping.putIfAbsent(word, initializedHashTable);
		wordPerDataClassMapping.get(word).compute(key, (k, v) -> v + 1);
	    });
	});

	wordPerDataClassMapping.entrySet().stream().forEach(e -> {
	    totalWordCount += e.getValue().values().stream().reduce(Integer::sum).get();

	    Arrays.stream(DataClass.values()).forEach(d -> wordCountPerDataClass.putIfAbsent(d, 1));

	    e.getValue().entrySet().stream().forEach(entry -> {
		wordCountPerDataClass.compute(entry.getKey(), (k, v) -> entry.getValue() + v);
	    });
	});
    }

    @Override
    public ClassificationResult classify(String text) {
	double[] finalResult = new double[DataClass.values().length];
	for (int i = 0; i < finalResult.length; i++) {
	    finalResult[i] = 100_000;
	}
	List<PosToken> posTokens = PosTokenizer.tokenize(text);
	posTokens.forEach(posToken -> {
	    if (posToken.getPosType().equals(PosType.ADJECTIVE) || posToken.getPosType().equals(PosType.NOUN) || posToken.getPosType().equals(PosType.ADVERB)) {
		Map<DataClass, Double> probabilities = getScaledLikelyhood(posToken.getRawWord());
		for (int i = 0; i < DataClass.values().length; i++) {
		    DataClass current = DataClass.values()[i];
		    Double probabilityForDataClass = probabilities.get(current);
		    finalResult[i] *= probabilityForDataClass;
		}
	    }
	    if (DEBUG) {
		System.out.println("After " + posToken + " => " + Arrays.toString(finalResult));
	    }
	});

	DataClass result = null;
	double maxProbability = Arrays.stream(finalResult).max().getAsDouble();

	for (int i = 0; i < finalResult.length; i++) {
	    if (finalResult[i] == maxProbability) {
		result = DataClass.values()[i];
	    }
	}

	// edge case override - all results are the same - pick neutral
	if (Arrays.stream(finalResult).distinct().count() == 1) {
	    result = DataClass.NEUTRAL;
	}

	getProgressReport().onOpinionClassified(text, result);
	return mapToClassifierResult(result);
    }

    protected ClassificationResult mapToClassifierResult(DataClass result) {
	return Utils.mapDataClassToClassifierResult(result);
    }

    protected Map<DataClass, Double> getScaledLikelyhood(String s) {
	Map<DataClass, Double> result = new HashMap<DataClass, Double>();
	Map<DataClass, Integer> wordOccurences = wordPerDataClassMapping.get(s);
	if (wordOccurences == null) {
	    Arrays.stream(DataClass.values()).forEach(d -> result.put(d, 1.0));
	    return result;
	}

	wordOccurences.entrySet().forEach(entry -> {
	    int occurences = entry.getValue();
	    int classWordCount = wordCountPerDataClass.get(entry.getKey());
	    double probability = ((double) occurences) / (totalWordCount + classWordCount);
	    result.put(entry.getKey(), probability);
	});

	// 2nd idea -> just generate

	return result;
	// return applyScales(s, result);

    }

    private Map<DataClass, Double> applyScales(String s, Map<DataClass, Double> result) {
	Map<DataClass, Integer> wordOccurencesInClasses = wordPerDataClassMapping.get(s);
	int totalWordCountInClasses;
	if (wordOccurencesInClasses == null) {
	    totalWordCountInClasses = DataClass.values().length;
	    return result;
	} else {
	    totalWordCountInClasses = wordOccurencesInClasses.values().stream().reduce(Integer::sum).get();
	    result.entrySet().stream().forEach(e -> {
		result.compute(e.getKey(), (k, v) -> v / (double) totalWordCountInClasses);
	    });
	}
	return result;
    }
}
