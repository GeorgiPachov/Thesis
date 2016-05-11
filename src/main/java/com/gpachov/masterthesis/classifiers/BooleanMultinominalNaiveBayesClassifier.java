//package main.java.gpachov.masterthesis.classifiers;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import main.java.gpachov.masterthesis.TrainingData;
//import main.java.gpachov.masterthesis.utils.Utils;
//
//public class BooleanMultinominalNaiveBayesClassifier extends Classifier {
//
//	private Map<String, Integer> positiveWordOccurences = new HashMap<String, Integer>();
//	private Map<String, Integer> negativeWordOccurences = new HashMap<String, Integer>();
//	private int totalWords;
//
//	public BooleanMultinominalNaiveBayesClassifier(TrainingData sampleData) {
//		super(sampleData);
//		init();
//	}
//
//	protected void init() {
//		sampleData.getPositive().stream()
//				.forEach(sentence -> {
//					Arrays.stream(sentence.split("\\s+")).distinct().
//					forEach(s -> positiveWordOccurences.compute(s,(k, v) -> v == null ? 1 : v + 1));
//				});
//				
//	
//		sampleData.getNegative().stream()
//				.forEach(sentence -> {
//					Arrays.stream(sentence.split("\\s+")).distinct().
//					forEach(s -> negativeWordOccurences.compute(s, (k,v) -> v == null? 1 : v + 1));
//				});
//
//		Integer positiveWords = positiveWordOccurences.values().stream()
//				.reduce(0, Integer::sum);
//		Integer negativeWords = negativeWordOccurences.values().stream()
//				.reduce(0, Integer::sum);
//		this.totalWords = positiveWords + negativeWords;
//	}
//
//	@Override
//	public DataClass classify(String text) {
//		double[] finalResult = new double[2];
//		finalResult[0] = 1;
//		finalResult[1] = 1;
//		Arrays.stream(text.split("\\s+")).forEach(s -> {
//			double positiveProbability = getPositiveProbability(s);
//			double negativeProbability = getNegativeProbability(s);
//
//			finalResult[0] *= positiveProbability;
//			finalResult[1] *= negativeProbability;
//		});
//		double positiveProbability = finalResult[0];
//		double negativeProbability = finalResult[1];
//
//		DataClass result = Utils.classify(positiveProbability, negativeProbability);
//
//		getProgressReport().onOpinionClassified(text, result);
//		return result;
//	}
//
//	private double getNegativeProbability(String s) {
//		Integer occurences = negativeWordOccurences.get(s);
//		if (occurences == null) {
//			return (float) 1 / (totalWords);
//		}
//		return occurences
//				/ ((float) negativeWordOccurences.size() + (totalWords));
//	}
//
//	private double getPositiveProbability(String s) {
//		Integer occurences = positiveWordOccurences.get(s);
//		if (occurences == null) {
//			return (float) 1 / (totalWords);
//		}
//		return occurences
//				/ ((float) positiveWordOccurences.size() + (totalWords));
//	}
//}
