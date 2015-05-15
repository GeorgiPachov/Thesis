package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.utils.Utils;

public class TrainingDataBuilder implements Iterable<TrainingData> {
    private Map<DataClass, List<String>> classified;
    private int portions;

    public TrainingDataBuilder(IDataProvider provider, int portions) {
	this.portions = portions;

	Map<DataClass, List<String>> classified = provider.getClassified();

//	 equal portions in every data class
	int class1Size = classified.get(DataClass.BAD).size();
	int class3Size = classified.get(DataClass.GOOD).size();
	final int minSentencesPerDataClass = IntStream.of(class1Size, class3Size).min().getAsInt();
	classified.keySet().stream().forEach(key -> {
	    List<String> limited = classified.get(key).stream().limit(minSentencesPerDataClass).collect(Collectors.toList());
	    classified.replace(key, limited);
	});
	this.classified = classified;
    }

    protected TrainingData build(int i) {
	Map<DataClass, List<String>> samples = new LinkedHashMap<DataClass, List<String>>();
	Map<DataClass, List<String>> theRest = new LinkedHashMap<DataClass, List<String>>();
	classified.entrySet().forEach(e -> {
	    int sampleSize = (int) (e.getValue().size() / (float) portions);
	    List<String> sampleSentences = Utils.subList(e.getValue(), i * sampleSize, (i + 1) * sampleSize);

	    List<String> rest = new ArrayList<>(e.getValue());

	    rest.removeAll(sampleSentences);
	    samples.put(e.getKey(), sampleSentences);
	    theRest.put(e.getKey(), rest);
	});

	SampleData result = new SampleData(classified, samples, theRest);
	// System.out.println("Samples: " + samples.values().stream().flatMap(l
	// -> l.stream()).count());
	// System.out.println("Real: " + theRest.values().stream().flatMap(l ->
	// l.stream()).count());
	// System.out.println("All: " + classified.values().stream().flatMap(l
	// -> l.stream()).count());
	return result;
    }

    @Override
    public Iterator<TrainingData> iterator() {
	return new Iterator<TrainingData>() {
	    private int index = 0;

	    @Override
	    public boolean hasNext() {
		return index < portions;
	    }

	    @Override
	    public TrainingData next() {
		return build(index++);
	    }
	};
    }

}
