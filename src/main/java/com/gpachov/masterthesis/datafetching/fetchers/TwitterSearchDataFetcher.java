package com.gpachov.masterthesis.datafetching.fetchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.data.impl.RawDataEntryImpl;
import com.gpachov.masterthesis.data.sources.TwitterSource;
import com.gpachov.masterthesis.datafetching.DataFetcher;
import com.gpachov.masterthesis.datafetching.Query;
import com.gpachov.masterthesis.exceptions.DataFetchingException;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@FetcherFor(TwitterSource.class)
public class TwitterSearchDataFetcher implements DataFetcher {
	@Override
	public List<RawDataEntry> fetchData(Query userQuery) {
		final long projectedTimeout = userQuery.getTimeLimit()
				+ System.currentTimeMillis();

		List<RawDataEntry> results = new ArrayList<RawDataEntry>();

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("H6MCrojShw7DEIbyNhfKIBRTu")
				.setOAuthConsumerSecret(
						"e2yuPnKGlMiesTzdaUCG4NPckyWX6MkH93LMcjlaosjvelOON1")
				.setOAuthAccessToken(
						"231007029-4Ki2HCVPWYXOfmF53nrC5lHCL8UIQp3MyhPTIbk9")
				.setOAuthAccessTokenSecret(
						"coU3OkkAkq0nsDmZP6yZyhcCHm9yB6AcjaIUIxBNT0pwR");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		twitter4j.Query query = new twitter4j.Query(userQuery.getText());
		try {
			QueryResult result = twitter.search(query);
			for (Status tweet : result.getTweets()) {
				if (System.currentTimeMillis() > projectedTimeout) {
					break;
				}
				RawDataEntry entry = new RawDataEntryImpl(new Date(),
						tweet.getText(), null);
				results.add(entry);
			}
		} catch (TwitterException e) {
			throw new DataFetchingException("Failed to fetch from twitter", e);
		}
		return results;
	}

}
