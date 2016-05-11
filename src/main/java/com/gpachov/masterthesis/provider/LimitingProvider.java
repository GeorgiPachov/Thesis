package com.gpachov.masterthesis.provider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.classifiers.DataClass;

public class LimitingProvider implements IDataProvider{
    private IDataProvider provider;
    private int limit;

    public LimitingProvider(IDataProvider provider, int limit) {
	this.provider = provider;
	this.limit = limit;
    }
    
    @Override
    public List<String> getUnclassified() {
	return provider.getUnclassified().stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public Map<DataClass, List<String>> getClassified() {
	throw new UnsupportedOperationException();
    }

}
