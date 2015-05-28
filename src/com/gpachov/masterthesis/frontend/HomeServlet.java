package com.gpachov.masterthesis.frontend;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.gpachov.masterthesis.RemoteApiClient;
import com.gpachov.masterthesis.TrainingData;
import com.gpachov.masterthesis.TrainingDataBuilder;
import com.gpachov.masterthesis.analyzer.DirectSentimentAnalyzer;
import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.classifiers.MasterAlgorithmClassifier;
import com.gpachov.masterthesis.extract.SentenceExtractor;
import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.filter.SkipEmptyOpinions;
import com.gpachov.masterthesis.googleplus.GooglePlusClientImpl;
import com.gpachov.masterthesis.preprocessors.DefaultPreprocessor;
import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.preprocessors.RemovingNonWordsPreprocessor;
import com.gpachov.masterthesis.preprocessors.TagStrippingPreprocessor;
import com.gpachov.masterthesis.provider.DataPreprocessor;
import com.gpachov.masterthesis.provider.DatabaseDataProvider;
import com.gpachov.masterthesis.reddit.RedditClient;
import com.gpachov.masterthesis.reddit.RedditClientImpl;
import com.gpachov.masterthesis.utils.Pair;
import com.gpachov.masterthesis.utils.Utils;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/analyze")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Classifier classifier;

    @Override
    public void init() throws ServletException {
	super.init();
	synchronized (HomeServlet.class) {
	    final DataPreprocessor dataPreprocessor = new DataPreprocessor(DatabaseDataProvider.getInstance());
	    // ugly hack, too lazy to to make another class- will reuse the
	    // generic class with instance-1
	    final TrainingDataBuilder trainingDataBuilder = new TrainingDataBuilder(dataPreprocessor, 1);

	    // i was in a hurry
	    TrainingData trainingData = trainingDataBuilder.iterator().next();
	    this.classifier = new MasterAlgorithmClassifier(trainingData.getAll());
	}
    }

    public HomeServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	setEncoding(response);
	final long start = System.currentTimeMillis();
	String command = request.getParameter("command");
	String input = (request.getParameter("input"));
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
	writer.println("Analysis completed in " + (end - start) + " milliseconds");

    }

    private void setEncoding(HttpServletResponse response) {
	response.setContentType("text/html; charset=UTF-8");
	response.setCharacterEncoding("UTF-8");
    }

    private String doAnalyzeBrand(final String input) {
	
	StringBuilder result = new StringBuilder();
	Map<String, ClassificationResult> allAnalyzedSentences = new LinkedHashMap<String, ClassificationResult>();
	
	List<RemoteApiClient> remoteApiClients = new ArrayList<RemoteApiClient>(){{
	    add(new RedditClientImpl());
	    add(new GooglePlusClientImpl());
	}};
	
	List<String> allOpinions = remoteApiClients.stream().map(r -> r.getSearchResults(input)).flatMap(l -> l.stream()).collect(Collectors.toList());
	SentenceExtractor relevanceExtractor = new LinguisticSentenceExtractor();
	float score = 0.0f;
	float maxScore = 0.0f;
	TagStrippingPreprocessor tagStrip = new TagStrippingPreprocessor();
	for (String opinion : allOpinions) {
	    //remove non-words, links, stuff like that
	    opinion = tagStrip.applyPreprocessing(opinion);
	    List<String> relevantSentences = relevanceExtractor.extractRelevant(opinion, input);
	    for (String relevantSentence : relevantSentences) {
		relevantSentence = preprocess(relevantSentence);
		if (new SkipEmptyOpinions().test(relevantSentence)) {
		    final DirectSentimentAnalyzer analyzer = new DirectSentimentAnalyzer(Arrays.asList(relevantSentence), classifier);
		    Entry<String, Pair<DataClass, ClassificationResult>> entry = analyzer.analyze().entrySet().iterator().next();
		    ClassificationResult res = entry.getValue().getSecond();
		    String sentences = entry.getKey();
		    if (!res.equals(ClassificationResult.NEUTRAL)){
			allAnalyzedSentences.put(sentences, res);
			score += Utils.scoreOf(res);
			maxScore += 1;
		    } else if (relevantSentence.contains("noun")){
			allAnalyzedSentences.put(sentences, res);
		    }
		}
	    }
	}
	final float positivismScore = score / maxScore;
	String newLine = "<br/>";
	result.append("Analyzed " + maxScore + " opinions about " + input + newLine);
	result.append("Positive opinions: " + positivismScore * 100 + "%" + newLine);
	result.append("Negative opinions: " + (1 - positivismScore) * 100 + "%" + newLine);

	result.append("Positive opinions about " + input + " " + newLine);
	allAnalyzedSentences.forEach((k, v) -> {
	    result.append(k + " => " + v + "<br/><br/>");
	});
	return result.toString();
    }

    private String preprocess(String relevantSentence) {
	return new DefaultPreprocessor().applyPreprocessing(relevantSentence);
    }

    private String test(String input) throws IOException {
	List<String> presentableResults = analyze(input);
	return presentableResults.stream().collect(Collectors.joining("<br/>"));
    }

    private List<String> analyze(String sentence) {

	final DirectSentimentAnalyzer analyzer = new DirectSentimentAnalyzer(Arrays.asList(sentence), classifier);

	List<String> presentableResults = analyzer.analyze().entrySet().stream().map(e -> sentence + "<br/> Positivity score  => " + e.getValue().getSecond())
		.collect(Collectors.toList());
	return presentableResults;
    }

}
