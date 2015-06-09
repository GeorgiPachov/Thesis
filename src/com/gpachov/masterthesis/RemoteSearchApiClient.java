package com.gpachov.masterthesis;

import java.util.List;

public interface RemoteSearchApiClient {
    public List<String> getSearchResults(String query);
}
