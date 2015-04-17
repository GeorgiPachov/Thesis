package com.gpachov.masterthesis.datafetching.fetchers;

import java.util.ArrayList;
import java.util.List;

import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.data.sources.GoogleSearchSource;
import com.gpachov.masterthesis.datafetching.DataFetcher;
import com.gpachov.masterthesis.datafetching.Query;

@FetcherFor(GoogleSearchSource.class)
public class GoogleSearchDataFetcher implements DataFetcher {

	@Override
	public List<RawDataEntry> fetchData(Query query) {
		return new ArrayList<RawDataEntry>();
	}

}
