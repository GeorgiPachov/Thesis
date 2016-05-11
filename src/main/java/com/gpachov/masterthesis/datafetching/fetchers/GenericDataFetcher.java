package com.gpachov.masterthesis.datafetching.fetchers;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.data.impl.RawDataEntryImpl;
import com.gpachov.masterthesis.datafetching.DataFetcher;
import com.gpachov.masterthesis.datafetching.Query;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.gpachov.masterthesis.datafetching.QueryImpl;
import com.gpachov.masterthesis.preprocessors.CompressSpacesPreprocessor;
import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.preprocessors.TagStrippingPreprocessor;

public class GenericDataFetcher implements DataFetcher {

	private static final List<Character> sentenceEndings = Arrays.asList('!',
			'.', '?');
	private List<URI> urls;
	private static final List<Preprocessor> preprocessors = Arrays.asList(
			new CompressSpacesPreprocessor(), new TagStrippingPreprocessor());

	public GenericDataFetcher(List<URI> urls) {
		this.urls = urls;
	}

	public static void main(String[] args) {
		final String NVIDIA_GOOGLE = "http://www.gamespot.com/forums/pc-mac-discussion-1000004/why-i-f-hate-nvidia-30881106/";
		List<URI> uris = Arrays.asList(URI.create(NVIDIA_GOOGLE));
		GenericDataFetcher df = new GenericDataFetcher(uris);
		Query q = new QueryImpl("nvidia");
		df.fetchData(q).stream().forEach(r -> System.out.println(r.getText()));
	}

	@Override
	public Collection<RawDataEntry> fetchData(Query query) {
		Map<URI, List<String>> sentences = urls.stream().collect(
				Collectors.toMap(k -> k, v -> fetchSentences(v, query)));

		return sentences.values().stream().flatMap(l -> l.stream()).map(s -> {
			RawDataEntry rde = new RawDataEntryImpl(s);
			return rde;
		}).collect(Collectors.toCollection(HashSet::new));
	}

	private List<String> fetchSentences(URI u, Query q) {
		try {
			final String output = Request.Get(u).execute().returnContent()
					.asString().toLowerCase();
			Document d = Jsoup.parse(output);
			String siteText = extractText(d);
			return doFetchSentences(siteText, q);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	private String extractText(Document d) {
		ArrayList<String> outList = new ArrayList<String>();
		doExtractText(d, outList);
		return outList.stream().collect(Collectors.joining(". "));
	}

	private void doExtractText(Node e, ArrayList<String> arrayList) {
		for (Node n : e.childNodes()) {
			if (n instanceof TextNode) {
				String res = ((TextNode) n).getWholeText();
				arrayList.add(res);
			}
			doExtractText(n, arrayList);
		}
	}

	private List<String> doFetchSentences(String output, Query q) {
		final ArrayList<String> results = new ArrayList<String>();
		String workingCopy = output;
		while (workingCopy.contains(q.getText())) {
			int indexOfQuery = workingCopy.indexOf(q.getText());
			int beginningOfSentenceIndex = findBeginningOfSentence(workingCopy,
					indexOfQuery);
			int endOfSentenceIndex = findEndOfSentence(workingCopy,
					indexOfQuery);
			String sentence = workingCopy.substring(beginningOfSentenceIndex,
					endOfSentenceIndex);
			for (Preprocessor pre : preprocessors) {
				sentence = pre.applyPreprocessing(sentence);
			}
			results.add(sentence);

			workingCopy = workingCopy.substring(endOfSentenceIndex);
		}

		return results;
	}

	private int findEndOfSentence(String workingCopy, int indexOfQuery) {
		int workingIndex = indexOfQuery;
		while (workingIndex < workingCopy.length()) {
			boolean isEndOfSentence = sentenceEndings.contains(workingCopy
					.charAt(workingIndex));
			if (isEndOfSentence) {
				return workingIndex;
			}
			workingIndex++;
		}
		return workingIndex; // end

	}

	private int findBeginningOfSentence(String workingCopy, int indexOfQuery) {
		int workingIndex = indexOfQuery;
		while (workingIndex > 0) {
			boolean isEndOfPreviousSentence = sentenceEndings
					.contains(workingCopy.charAt(workingIndex));
			if (isEndOfPreviousSentence) {
				return workingIndex + 1;
			}
			workingIndex--;
		}
		return workingIndex; // beginning
	}
}
