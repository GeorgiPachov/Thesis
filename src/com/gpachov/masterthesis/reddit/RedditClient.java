package com.gpachov.masterthesis.reddit;

import java.util.List;

public interface RedditClient {
	List<String> getUserOpinions(String query);
}
