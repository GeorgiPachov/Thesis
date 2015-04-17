package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gpachov.masterthesis.dictionaries.EnglishDictionary;
import com.gpachov.masterthesis.dictionaries.SkipWordsDictionary;
import com.gpachov.masterthesis.preprocessors.Preprocessor;

public class DataPreprocessor implements IDataProvider, IDataPreprocessor {

	private static final Logger logger = LoggerFactory.getLogger(DataPreprocessor.class);
	private EnglishDictionary dictionary;
	private AtomicLong matches = new AtomicLong(0);
	private AtomicLong total = new AtomicLong(0);
	private List<String> positivePreprocessed = new ArrayList<String>();
	private List<String> negativePreprocessed = new ArrayList<String>();
	private List<Preprocessor> preprocessors;
	public DataPreprocessor(IDataProvider iDataProvider, Preprocessor... preprocessors) {
		this.dictionary = new EnglishDictionary();
		this.preprocessors = Arrays.asList(preprocessors);

		final long allWordsCount = Stream
				.concat(iDataProvider.getPositive().stream(), iDataProvider.getNegative().stream())
				.flatMap(s -> Arrays.stream(s.split("\\s+"))).count();
		
		this.getProgressReport().onPreprocessingStarted(new PreprocessingInfo(allWordsCount));

		this.positivePreprocessed = iDataProvider.getPositive().stream().map(this::applyPreprocessing)
				.collect(Collectors.toList());
		this.negativePreprocessed = iDataProvider.getNegative().stream().map(this::applyPreprocessing)
				.collect(Collectors.toList());

		this.getProgressReport().onPreprocessingFinished();
	}

	@Override
	public List<String> getPositive() {
		return new ArrayList<String>(positivePreprocessed);
	}

	@Override
	public List<String> getNegative() {
		return new ArrayList<String>(negativePreprocessed);
	}

	@Override
	public String applyPreprocessing(final String opinion) {
		String workingOpinion = opinion;
		for (Preprocessor p : preprocessors) {
			workingOpinion = p.applyPreprocessing(workingOpinion);
		}
		
		matches.addAndGet(Arrays.stream(workingOpinion.split("\\s+"))
				.filter(s -> dictionary.contains(s.toLowerCase().trim())).count());
		total.addAndGet(Arrays.stream(workingOpinion.split("\\s+")).count());
		this.getProgressReport().onOpinionPreprocessed(opinion, workingOpinion);

		return workingOpinion;
	}

	@Override
	public float getMatchRate() {
		return (((float) matches.get()) / total.get());
	}
}
