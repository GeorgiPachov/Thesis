package com.gpachov.masterthesis.data.sources;

import java.util.Set;

import org.reflections.Reflections;

import com.gpachov.masterthesis.data.Source;

public class DataSources {
	public Set<Class<? extends Source>> getAllDataSources() {
		Reflections reflections = new Reflections(this.getClass().getPackage().getName());
		Set<Class<? extends Source>> dataSources = reflections.getSubTypesOf(Source.class);
		return dataSources;
	}
}
