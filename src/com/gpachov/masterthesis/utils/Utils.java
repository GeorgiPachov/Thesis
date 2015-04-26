package com.gpachov.masterthesis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gpachov.masterthesis.classifiers.ClassifierResult;

public class Utils {
	
	public static float normalize(int minInclusive, int maxInclusive, float value) {
		return (float) value / (maxInclusive - minInclusive );
	}
	
	public static <T> List<T> subList(List<T> list, int startIndex, int endIdex){
		ArrayList<T> arrayList = new ArrayList<T>();
		arrayList.addAll(list.subList(startIndex, endIdex));
		return arrayList;
	}
	
	public static ClassifierResult classify(double positive, double negative){
		double sum = positive + negative;
		double positivePercentage = positive / sum;
		if (positivePercentage < 0.2){
			return ClassifierResult.CLASS_1;
		} else if (positivePercentage < 0.4){
			return ClassifierResult.CLASS_2;
		} else if (positivePercentage < 0.6){
			return ClassifierResult.CLASS_3;
		} else if (positivePercentage < 0.8){
			return ClassifierResult.CLASS_4;
		}  else {
			return ClassifierResult.CLASS_5;
		}
		
	}
	
	public static ClassifierResult classify(float[] probabilities){
		return classify(probabilities[0], probabilities[1]);
	}
}
