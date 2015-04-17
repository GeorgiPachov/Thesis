package com.gpachov.masterthesis.datafetching.fetchers;

import java.util.ArrayList;
import java.util.List;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.data.sources.FacebookSource;
import com.gpachov.masterthesis.datafetching.DataFetcher;
import com.gpachov.masterthesis.datafetching.Query;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.types.Post;

@FetcherFor(FacebookSource.class)
public class FacebookDataFetcher implements DataFetcher {
	@SuppressWarnings("unused")
	private static final String APP_ID = "945377378813235";
	@SuppressWarnings("unused")
	private static final String APP_SECRET = "fd07f344b94f772a2371573bc157f078";
	@SuppressWarnings("unused")
	private static final String CLIENT_TONE = "6cfba55152404f53e485b43ec50e337b";

	@Override
	public List<RawDataEntry> fetchData(Query query) {
		return new ArrayList<RawDataEntry>();
	}

}
