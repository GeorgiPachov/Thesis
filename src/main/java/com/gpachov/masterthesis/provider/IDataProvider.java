package com.gpachov.masterthesis.provider;

import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.classifiers.DataClass;

public interface IDataProvider {
	public abstract List<String> getUnclassified();
	public Map<DataClass, List<String>> getClassified();

}