//package com.gpachov.masterthesis.analyzer;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import com.gpachov.masterthesis.classifiers.ClassifierResult;
//
//public class CompositeSentimentAnaylzer implements SentimentAnalyzer {
//
//	private List<SentimentAnalyzer> analyzers;
//
//	public CompositeSentimentAnaylzer(List<SentimentAnalyzer> analyzers) {
//		this.analyzers = analyzers;
//	}
//
//	public CompositeSentimentAnaylzer(SentimentAnalyzer... analyzers) {
//		this(Arrays.asList(analyzers));
//	}
//
//	@Override
//	public Map<String, ClassifierResult> analyze() {
//		List<Map<String, ClassifierResult>> results = new ArrayList<>();
//		for (SentimentAnalyzer analyzer : analyzers) {
//			results.add(analyzer.analyze());
//		}
//		return merge(results);
//	}
//
//	private Map<String, ClassifierResult> merge(List<Map<String, ClassifierResult>> results) {
//		final Map<String, Pair<Integer, Integer>> classifierResults = new HashMap<>();
//		for (Map<String, ClassifierResult> map : results) {
//			map.entrySet().stream().forEach(e -> {
//				ClassifierResult result = e.getValue();
//				if (!classifierResults.containsKey(e.getKey())) {
//					classifierResults.put(e.getKey(), new Pair<Integer, Integer>(0, 0));
//				}
//				if (result == ClassifierResult.GOOD) {
//					classifierResults.get(e.getKey()).first++;
//				} else {
//					classifierResults.get(e.getKey()).second++;
//				}
//			});
//		}
//
//		Map<String, ClassifierResult> averagedResult = classifierResults.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), (e -> (e.getValue().first > e.getValue().second) ? ClassifierResult.GOOD : ClassifierResult.BAD)));
//		return averagedResult;
//	}
//
//	@Override
//	public float getMatchRate() {
//		return (float) analyzers.stream().mapToDouble(a -> a.getMatchRate()).average().getAsDouble();
//	}
//
//	public static final class Pair<T, E> {
//		private T first;
//		private E second;
//
//		public Pair(T first, E second) {
//			this.first = first;
//			this.second = second;
//		}
//	}
//
//}
