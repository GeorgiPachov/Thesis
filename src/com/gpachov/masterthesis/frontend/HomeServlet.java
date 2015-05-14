package com.gpachov.masterthesis.frontend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gpachov.masterthesis.DataPreprocessor;
import com.gpachov.masterthesis.DatabaseDataProvider;
import com.gpachov.masterthesis.TrainingData;
import com.gpachov.masterthesis.TrainingDataBuilder;
import com.gpachov.masterthesis.analyzer.DirectSentimentAnalyzer;
import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.classifiers.NaiveBayesClassifier;
import com.gpachov.masterthesis.extract.Extractor;
import com.gpachov.masterthesis.extract.RelevantSentenceExtractor;
import com.gpachov.masterthesis.preprocessors.CompressSpacesPreprocessor;
import com.gpachov.masterthesis.preprocessors.DefaultPreprocessor;
import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.preprocessors.TagStrippingPreprocessor;
import com.gpachov.masterthesis.reddit.RedditClient;
import com.gpachov.masterthesis.reddit.RedditClientImpl;
import com.gpachov.masterthesis.utils.Pair;
import com.gpachov.masterthesis.utils.Utils;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/analyze")
public class HomeServlet extends HttpServlet {
	private static final List<Preprocessor> preprocessors = Arrays.asList(
			new CompressSpacesPreprocessor(), new TagStrippingPreprocessor());

	private static final long serialVersionUID = 1L;
	private Classifier classifier;

	@Override
	public void init() throws ServletException {
		super.init();
		synchronized (HomeServlet.class) {
			final DataPreprocessor dataPreprocessor = new DataPreprocessor(
					DatabaseDataProvider.getInstance());
			//ugly hack, too lazy to to make another class-  will reuse the generic class with instance-1
			final TrainingDataBuilder trainingDataBuilder = new TrainingDataBuilder(dataPreprocessor, 1);
			
			//i was in a hurry
			TrainingData trainingData = trainingDataBuilder.iterator().next();
			this.classifier = new NaiveBayesClassifier(trainingData.getAll());
		}
	}

	/**
	 * Default constructor.
	 */
	public HomeServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setEncoding(response);
		final long start = System.currentTimeMillis();
		String command = request.getParameter("command");
		String input = request.getParameter("input");
		PrintWriter writer = null;
		switch (command) {
		case "test":
			String output = test(input);
			writer = response.getWriter();
			writer.println(output);
			break;
		case "analyze":
			String analyzeOutput = doAnalyzeBrand(input);
			writer = response.getWriter();
			writer.println(analyzeOutput);
			break;
		}
		final long end = System.currentTimeMillis();
		writer.println(
				"Analysis completed in " + (end - start) + " milliseconds");

	}

	private void setEncoding(HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
	}

	private String doAnalyzeBrand(String input) {
		StringBuilder result = new StringBuilder();
		Map<String,Float> allAnalyzedSentences = new LinkedHashMap<String, Float>();
		RedditClient redditClient = new RedditClientImpl();
		List<String> redditOpinions = redditClient.getUserOpinions(input);
		Extractor relevanceExtractor = new RelevantSentenceExtractor();
		float score = 0.0f;
		float maxScore = 0.0f;
		for (String redditOpinion : redditOpinions) {
			List<String> relevantSentences = relevanceExtractor
					.extractRelevant(redditOpinion, input);
			for (String relevantSentence : relevantSentences) {
				relevantSentence = preprocess(relevantSentence);
				final DirectSentimentAnalyzer analyzer = new DirectSentimentAnalyzer(
						Arrays.asList(relevantSentence), classifier);
				Entry<String, Pair<DataClass, ClassificationResult>> entry = analyzer.analyze().entrySet().iterator().next();
				ClassificationResult res = entry.getValue().getSecond();
				String sentences = entry.getKey();
				allAnalyzedSentences.put(sentences, Utils.scoreOf(res));
				score+=Utils.scoreOf(res);
				maxScore+=1;
			}
		}
		final float positivismScore = score/maxScore;
		String newLine = "<br/>";
		result.append("Analyzed " + maxScore + " opinions about "+ input + newLine);
		result.append("Positive opinions: " + positivismScore * 100 + "%"
				+ newLine);
		result.append("Negative opinions: " + (1 - positivismScore) * 100 + "%"
				+ newLine);

		result.append("Positive opinions about " + input + " " + newLine);
		allAnalyzedSentences.forEach((k,v) -> {
		    result.append(k + " => " + v + "<br/><br/>") ;
		});
		return result.toString();
	}

	private String preprocess(String relevantSentence) {
		String finalSentence = relevantSentence;
		for (Preprocessor pre : preprocessors) {
			finalSentence = pre.applyPreprocessing(finalSentence);
		}
		return finalSentence;
	}

	private String test(String input) throws IOException {
		List<String> presentableResults = analyze(input);
		return presentableResults.stream().collect(Collectors.joining("<br/>"));
	}

	private List<String> analyze(String sentence) {
		String filteredSentence = new DefaultPreprocessor().applyPreprocessing(sentence);
		
		final DirectSentimentAnalyzer analyzer = new DirectSentimentAnalyzer(
				Arrays.asList(filteredSentence), classifier);

		//XXX relying on analyzer working on 1 sentence
		List<String> presentableResults = analyzer.analyze().entrySet()
				.stream().map(e -> sentence + "<br/> Positivity score  => " + e.getValue().getSecond())
				.collect(Collectors.toList());
		return presentableResults;
	}

}
