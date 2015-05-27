package com.gpachov.masterthesis;

import java.util.List;

public interface RemoteApiClient {
    public List<String> getSearchResults(String query);
}
