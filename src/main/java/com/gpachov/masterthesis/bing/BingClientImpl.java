package com.gpachov.masterthesis.bing;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;

import org.apache.http.client.fluent.Request;

public class BingClientImpl implements BingClient {
	private static final String BING_SEARCH_MESSAGE_FORMAT = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/Web?Query=%27{0}%27&$top={1}&$format=Json";
	private static final String BING_API_URL = "https://api.datamarket.azure.com/Bing/SearchWeb/";

	@Override
	public List<String> getSearchResults(String query) {
		byte[] authEncBytes = Base64.getEncoder().encode(
				":nQNJINYfOxhioBK3aC5bAv6Vvbvjgo4Gc+OV1jnV7SM".getBytes());
		try {
			String response = Request
					.Get(MessageFormat.format(BING_SEARCH_MESSAGE_FORMAT, query, 100))
					.addHeader("Authorization", "Basic " + new String(authEncBytes))
					.execute().returnContent().asString();
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		new BingClientImpl().getSearchResults("VMware");
	}

}
