package com.gpachov.masterthesis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
	
	public static float normalize(int minInclusive, int maxInclusive, float value) {
		return (float) value / (maxInclusive - minInclusive );
	}
	
	public static <T> List<T> subList(List<T> list, int startIndex, int endIdex){
		ArrayList<T> arrayList = new ArrayList<T>();
		arrayList.addAll(list.subList(startIndex, endIdex));
		return arrayList;
	}
}
