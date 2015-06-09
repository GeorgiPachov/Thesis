package com.gpachov.masterthesis.reddit;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gpachov.masterthesis.extract.SentenceExtractor;
import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.extract.RelevantSentenceExtractor;

public class RedditClientImpl implements RedditClient {

    private static final String REDIT_SEARCH_API_MF = "http://www.reddit.com/search.json?q={0}&sort=new&limit=100";

    @Override
    public List<String> getSearchResults(String query) {
	List<String> results = new ArrayList<String>();

	try {
	    String encodedQuery = new URLCodec("UTF-8").encode(query);
	    String url = MessageFormat.format(REDIT_SEARCH_API_MF, encodedQuery);
	    String jsonResponse = Request.Get(url).userAgent("opinion mining bot 1.0 by /u/gpachov").execute().returnContent().asString();
	    JSONObject data = new JSONObject(jsonResponse).getJSONObject("data");
	    JSONArray children = data.getJSONArray("children");
	    for (int i = 0; i < children.length(); i++) {
		JSONObject childData = children.getJSONObject(i).getJSONObject("data");
		String text = childData.getString("selftext");
		results.add(text);
	    }
	} catch (IOException | EncoderException e) {
	    e.printStackTrace();
	}
	return results;
    }

    private void acquireToken() {
	final NameValuePair grantType = new BasicNameValuePair("grant_type", "client_credentials");

	String credentialsString = Base64.getEncoder().encodeToString("ue5CatWtOAtxtw:RHtiA85owI5YUX7-2A1xJA79p3M".getBytes());
	try {
	    String jsonResponse = Request.Post("https://www.reddit.com/api/v1/access_token").bodyForm(grantType).userAgent("bot 1.0 by /u/gpachov")
		    .addHeader("Authorization", "Basic " + credentialsString).execute().returnContent().asString();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	final String userInput = "Samsung";
	RedditClient redditClient = new RedditClientImpl();
	SentenceExtractor extractor = new LinguisticSentenceExtractor();
	redditClient.getSearchResults(userInput);
//	redditClient.getSearchResults(userInput).stream().map(o -> extractor.extractRelevant(o, userInput)).forEach(s -> {
//	    s.stream().forEach(se -> {
//		System.out.println(se);
//		System.out.println("-----");
//	    });
//	});
//	;
    }
}
