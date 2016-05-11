package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.datafetching.Query;
import com.gpachov.masterthesis.preprocessors.TrimmingPreprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gpachov.masterthesis.analyzer.CrossvalidatorSentimentAnalyzer;
import com.gpachov.masterthesis.classifiers.ClassifierFactory;
import com.gpachov.masterthesis.classifiers.NaiveBayesClassifier;
import com.gpachov.masterthesis.datafetching.QueryImpl;
import com.gpachov.masterthesis.datafetching.fetchers.DataFetcherFactory;
import com.gpachov.masterthesis.preprocessors.CompressSpacesPreprocessor;
import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.preprocessors.RemovingNonWordsPreprocessor;
import com.gpachov.masterthesis.preprocessors.SkipWordsPreprocessor;
import com.gpachov.masterthesis.preprocessors.TagStrippingPreprocessor;
import com.gpachov.masterthesis.preprocessors.WordCompactingPreprocessor;
import com.gpachov.masterthesis.provider.DataPreprocessor;
import com.gpachov.masterthesis.provider.DatabaseDataProvider;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		final long start = System.currentTimeMillis();

		final DataPreprocessor dataPreprocessor = new DataPreprocessor(DatabaseDataProvider.getInstance(),
				getPreprocessors());
		final ClassifierFactory classifierFactory = s -> new NaiveBayesClassifier(s);
//		final ClassifierFactory classifierFactory = s -> new LayeredBayesClassifier(s);

		final CrossvalidatorSentimentAnalyzer crossvalidator = new CrossvalidatorSentimentAnalyzer(10,
				classifierFactory, dataPreprocessor);
		crossvalidator.analyze(null, null);
		float matchRate = crossvalidator.getMatchRate();

		logger.info("It took " + (System.currentTimeMillis() - start) + " milliseconds");
		logger.info("Match rate with crossvalidation " + matchRate);
	}

	private static Preprocessor[] getPreprocessors() {
		final List<Preprocessor> preprocessors = new ArrayList<Preprocessor>();
		preprocessors.add(new TrimmingPreprocessor());
		preprocessors.add(new TagStrippingPreprocessor());
		preprocessors.add(new CompressSpacesPreprocessor());
		preprocessors.add(new RemovingNonWordsPreprocessor());
//		preprocessors.add(new SpellCorrectionPreprocessor());
		preprocessors.add(new SkipWordsPreprocessor());
		preprocessors.add(new WordCompactingPreprocessor());
		return preprocessors.toArray(new Preprocessor[preprocessors.size()]);
	}

	private static Collection<String> fetchAllTweets() {
		final Collection<RawDataEntry> allData = fetchAllData();

		final Stream<String> allTweetsStream = allData.stream().map(r -> r.getText());
		final Collection<String> allTweets = allTweetsStream.collect(Collectors.toList());
		return allTweets;
	}

	private static Collection<RawDataEntry> fetchAllData() {
		final DataFetcherFactory fetcherFactory = new DataFetcherFactory();
		final Query query = new QueryImpl("life", 20000L);
		final Collection<RawDataEntry> allData = fetcherFactory.getAllFetchers().stream().map(f -> f.fetchData(query))
				.reduce(new ArrayList<RawDataEntry>(), (a, b) -> {
					a.addAll(b);
					return a;
				});
		return allData;
	}
}
