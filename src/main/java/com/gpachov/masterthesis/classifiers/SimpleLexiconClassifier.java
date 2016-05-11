//package main.java.gpachov.masterthesis.classifiers;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import main.java.gpachov.masterthesis.lexicon.BasicSentimentLexicon;
//import main.java.gpachov.masterthesis.lexicon.SentimentLexicon;
//import main.java.gpachov.masterthesis.lexicon.WordNetLexiconDecorator;
//
//public class SimpleLexiconClassifier extends Classifier {
//    private SentimentLexicon lexicon = new BasicSentimentLexicon();
//
//    public SimpleLexiconClassifier(Map<DataClass, List<String>> trainingData) {
//	super(trainingData);
//    }
//
//    @Override
//    public ClassificationResult classify(String text) {
//	int[] sentimentScore = new int[1];
//	Arrays.stream(text.split("\\s+")).forEach(word -> {
//	    sentimentScore[0] +=lexicon.getScore(word);
//	});
//	ClassificationResult result = classify(sentimentScore);
////	System.out.println("Classified " + text + " as " + result);
//	return result;
//    }
//
//    private ClassificationResult classify(int[] sentimentScore) {
//	if (sentimentScore[0] > 0)
//	    return ClassificationResult.POSITIVE;
//	else if (sentimentScore[0] < 0)
//	    return ClassificationResult.NEGATIVE;
//	return ClassificationResult.NEUTRAL;
//    }
//
//}
