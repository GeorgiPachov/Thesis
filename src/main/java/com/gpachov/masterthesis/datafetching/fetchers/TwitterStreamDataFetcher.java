package com.gpachov.masterthesis.datafetching.fetchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.datafetching.DataFetcher;
import com.gpachov.masterthesis.datafetching.Query;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.gpachov.masterthesis.data.impl.RawDataEntryImpl;
import com.gpachov.masterthesis.exceptions.DataFetchingException;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

//@FetcherFor(TwitterSource.class)
public class TwitterStreamDataFetcher implements DataFetcher {
	private Client client;
	private BlockingQueue<Event> eventQueue;
	private BlockingQueue<String> messageQueue;

	@Override
	public List<RawDataEntry> fetchData(Query query) {
		ArrayList<RawDataEntry> results = new ArrayList<RawDataEntry>();
		initTwitterConnection(query.getText());
		long projectedTimeout = System.currentTimeMillis()
				+ query.getTimeLimit();
		try {
			while (!client.isDone()
					&& System.currentTimeMillis() < projectedTimeout) {
				String msg = messageQueue.poll(query.getTimeLimit(),
						TimeUnit.MILLISECONDS);
				if (msg != null) {
					JSONObject jsonObject = new JSONObject(msg);
					String tweetText = jsonObject.get("text").toString();

					// FIXME source details not null
					results.add(new RawDataEntryImpl(new Date(), tweetText,
							null));
				}
			}
		} catch (InterruptedException e) {
			throw new DataFetchingException(
					"Failed to fetch data from Twitter", e);
		}
		// on a different thread, or multiple different threads....
		return results;
	}

	private void initTwitterConnection(String term) {
		TwitterStreamDataFetcher.this.messageQueue = new LinkedBlockingQueue<String>(
				10000);
		TwitterStreamDataFetcher.this.eventQueue = new LinkedBlockingQueue<Event>(
				1000);

		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		hosebirdEndpoint.trackTerms(Lists.newArrayList(term));

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1("H6MCrojShw7DEIbyNhfKIBRTu",
				"e2yuPnKGlMiesTzdaUCG4NPckyWX6MkH93LMcjlaosjvelOON1",
				"231007029-4Ki2HCVPWYXOfmF53nrC5lHCL8UIQp3MyhPTIbk9",
				"coU3OkkAkq0nsDmZP6yZyhcCHm9yB6AcjaIUIxBNT0pwR");

		ClientBuilder builder = new ClientBuilder()
				.name("Hosebird-Client-01")
				// optional: mainly for the logs
				.hosts(hosebirdHosts).authentication(hosebirdAuth)
				.endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(messageQueue))
				.eventMessageQueue(eventQueue); // optional: use this if
												// you
												// want to process
												// client events

		TwitterStreamDataFetcher.this.client = builder.build();
		// Attempts to establish a connection.
		TwitterStreamDataFetcher.this.client.connect();
	}
}
