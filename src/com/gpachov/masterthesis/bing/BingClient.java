package com.gpachov.masterthesis.bing;

import java.util.List;

public interface BingClient {
	List<String> getSearchResults(String query);
}
