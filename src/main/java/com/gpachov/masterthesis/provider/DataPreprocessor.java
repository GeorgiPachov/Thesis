package com.gpachov.masterthesis.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.PreprocessingInfo;
import com.gpachov.masterthesis.filter.SkipEmptyOpinions;
import com.gpachov.masterthesis.progress.ProgressReporting;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.preprocessors.Preprocessor;

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
				.map(this::applyPreprocessing).filter(new SkipEmptyOpinions()).collect(Collectors.toList());

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
