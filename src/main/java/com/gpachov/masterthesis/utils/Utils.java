package com.gpachov.masterthesis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;

public class Utils {

    public static float normalize(int minInclusive, int maxInclusive, float value) {
	return (float) value / (maxInclusive - minInclusive);
    }

    public static ClassificationResult mapDataClassToClassifierResult(DataClass result) {
	if (result.equals(DataClass.BAD)) {
	    return ClassificationResult.NEGATIVE;
	} 
	return ClassificationResult.POSITIVE;
    }

    public static <T> List<T> subList(List<T> list, int startIndex, int endIdex) {
	ArrayList<T> arrayList = new ArrayList<T>();
	arrayList.addAll(list.subList(startIndex, endIdex));
	return arrayList;
    }

    public static DataClass classify(double positive, double negative) {
	double sum = positive + negative;
	double positivePercentage = positive / sum;
	return classify(positivePercentage);
    }

    public static DataClass classify(double normalizedUserEvaluation) {
	if (normalizedUserEvaluation < DataClass.BAD.upperLimit()) {
	    return DataClass.BAD;
	}
	return DataClass.GOOD;
    }

    public static DataClass classify(float[] probabilities) {
	return classify(probabilities[0], probabilities[1]);
    }

    public static float scoreOf(ClassificationResult res) {
	switch (res) {
	case NEGATIVE:
	    return 0.0f;
	case POSITIVE:
	    return 1.0f;
	}
	return 0;
    }

    public static boolean isNegationNaive(PosToken posToken) {
	return Arrays.asList("no", "not", "didnt", "didn't", "did nt", "n't").stream().filter(s -> posToken.getRawWord().contains(s)).count() > 0;
    }
}
