package com.gpachov.masterthesis.datafetching;

import java.util.Collection;

import com.gpachov.masterthesis.data.RawDataEntry;

public interface DataFetcher {
	Collection<RawDataEntry> fetchData(Query query);
}
