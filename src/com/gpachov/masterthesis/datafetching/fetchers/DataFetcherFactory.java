package com.gpachov.masterthesis.datafetching.fetchers;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.gpachov.masterthesis.data.Source;
import com.gpachov.masterthesis.data.sources.DataSources;
import com.gpachov.masterthesis.datafetching.DataFetcher;

public class DataFetcherFactory {

	public List<DataFetcher> getAllFetchers() {
		DataSources dataSources = new DataSources();
		Set<Class<? extends Source>> alLSources = dataSources
				.getAllDataSources();

		List<DataFetcher> allFetchers = alLSources.stream()
				.map(c -> getFetcher(c)).collect(Collectors.toList());
		return allFetchers;
	}

	public DataFetcher getFetcher(Class<? extends Source> sourceClazz) {
		Reflections reflections = new Reflections(this.getClass().getPackage()
				.getName());
		Set<Class<?>> allAnnotatedClasses = reflections
				.getTypesAnnotatedWith(FetcherFor.class);

		Predicate<? super Class<?>> annotatedWithClass = c -> c
				.getAnnotation(FetcherFor.class).value().equals(sourceClazz);
		Class<?> fetcher = allAnnotatedClasses.stream()
				.filter(annotatedWithClass).collect(Collectors.toList()).get(0);
		try {
			return (DataFetcher) fetcher.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
