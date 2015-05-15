package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.dictionaries.EnglishDictionary;
import com.gpachov.masterthesis.dictionaries.SkipWordsDictionary;
import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.progress.ProgressReport;
import com.gpachov.masterthesis.progress.ProgressReporting;

public class DataPreprocessor implements IDataProvider, ProgressReporting,
		Preprocessor {
	private List<String> allPreprocessed = new ArrayList<String>();
	private List<Preprocessor> preprocessors;
	private Map<DataClass, List<String>> allClassifiedPreprocessed = new LinkedHashMap<DataClass, List<String>>();

	public DataPreprocessor(IDataProvider iDataProvider,
			Preprocessor... preprocessors) {
		this.preprocessors = Arrays.asList(preprocessors);

		final long allWordsCount = iDataProvider.getUnclassified().stream()
				.flatMap(s -> Arrays.stream(s.split("\\s+"))).count();

		this.getProgressReport().onPreprocessingStarted(
				new PreprocessingInfo(allWordsCount));

		this.allPreprocessed = iDataProvider.getUnclassified().stream()
				.map(this::applyPreprocessing).collect(Collectors.toList());

		iDataProvider.getClassified().keySet().stream().forEach(k -> {
			List<String> preprocessed = iDataProvider.getClassified().get(k).
					stream().map(this::applyPreprocessing).filter(s-> !s.equals("")).collect(Collectors.toList());
			allClassifiedPreprocessed.put(k, preprocessed);
		});

		this.getProgressReport().onPreprocessingFinished();
	}

	@Override
	public String applyPreprocessing(final String opinion) {
		String workingOpinion = opinion;
		for (Preprocessor p : preprocessors) {
			workingOpinion = p.applyPreprocessing(workingOpinion);
		}

		this.getProgressReport().onOpinionPreprocessed(opinion, workingOpinion);
		return workingOpinion;
	}

	@Override
	public List<String> getUnclassified() {
		return allPreprocessed;
	}

	@Override
	public Map<DataClass, List<String>> getClassified() {
		return allClassifiedPreprocessed;
	}
}
