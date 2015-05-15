package com.gpachov.masterthesis;

import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.classifiers.DataClass;

public interface TrainingData {
	public Map<DataClass, List<String>> getAll();
	public Map<DataClass, List<String>> getSamples();
	public Map<DataClass, List<String>> getReal();
}
