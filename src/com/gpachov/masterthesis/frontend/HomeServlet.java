package com.gpachov.masterthesis.frontend;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gpachov.masterthesis.DataPreprocessor;
import com.gpachov.masterthesis.DataProviderWrapper;
import com.gpachov.masterthesis.analyzer.DirectSentimentAnalyzer;
import com.gpachov.masterthesis.classifiers.ClassifierResult;
import com.gpachov.masterthesis.classifiers.NaiveBayesClassifier;
import com.gpachov.masterthesis.extract.Extractor;
import com.gpachov.masterthesis.extract.RelevantSentenceExtractor;
import com.gpachov.masterthesis.preprocessors.CompressSpacesPreprocessor;
import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.preprocessors.TagStrippingPreprocessor;
import com.gpachov.masterthesis.reddit.RedditClient;
import com.gpachov.masterthesis.reddit.RedditClientImpl;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/analyze")
public class HomeServlet extends HttpServlet {
	private static final List<Preprocessor> preprocessors = Arrays.asList(
			new CompressSpacesPreprocessor(), new TagStrippingPreprocessor());

	private static final long serialVersionUID = 1L;
	private NaiveBayesClassifier classifier;

	@Override
	public void init() throws ServletException {
		super.init();
		synchronized (HomeServlet.class) {
			final DataPreprocessor dataPreprocessor = new DataPreprocessor(
					DataProviderWrapper.getInstance());
			this.classifier = new NaiveBayesClassifier(dataPreprocessor);
		}
	}

	/**
	 * Default constructor.
	 */
	public HomeServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		List<String> positiveSentences = new ArrayList<String>();
		List<String> negativeSentences = new ArrayList<String>();
		RedditClient redditClient = new RedditClientImpl();
		List<String> redditOpinions = redditClient.getUserOpinions(input);
		Extractor relevanceExtractor = new RelevantSentenceExtractor();
		int positiveCount = 0;
		int negativeCount = 0;
		for (String redditOpinion : redditOpinions) {
			List<String> relevantSentences = relevanceExtractor
					.extractRelevant(redditOpinion, input);
			for (String relevantSentence : relevantSentences) {
				relevantSentence = preprocess(relevantSentence);
				final DirectSentimentAnalyzer analyzer = new DirectSentimentAnalyzer(
						Arrays.asList(relevantSentence), classifier);
				if (analyzer.analyze().entrySet().iterator().next().getValue()
						.equals(ClassifierResult.GOOD)) {
					positiveCount++;
					positiveSentences.add(relevantSentence);
				} else if (analyzer.analyze().entrySet().iterator().next()
						.getValue().equals(ClassifierResult.BAD)) {
					negativeCount++;
					negativeSentences.add(relevantSentence);
				}
			}
		}
		final float positiveRates = ((float) positiveCount / (positiveCount + negativeCount));
		String newLine = "<br/>";
		result.append("Positive opinions: " + positiveRates * 100 + "%"
				+ newLine);
		result.append("Negative opinions: " + (1 - positiveRates) * 100 + "%"
				+ newLine);

		result.append("Positive opinions about " + input + " " + newLine);
		positiveSentences.stream().forEach(
				s -> result.append(s + " => POSITIVE" + " " + newLine));
		result.append("Negative opinions about " + input + " :"  + newLine);
		negativeSentences.stream().forEach(
				s -> result.append(s + " => NEGATIVE" + newLine));
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
		final DirectSentimentAnalyzer analyzer = new DirectSentimentAnalyzer(
				Arrays.asList(sentence), classifier);

		List<String> presentableResults = analyzer.analyze().entrySet()
				.stream().map(e -> e.getKey() + " => " + e.getValue())
				.collect(Collectors.toList());
		return presentableResults;
	}

}
