package com.gpachov.masterthesis.dbbuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.data.Entry;
import com.gpachov.masterthesis.db.DBConstants;
import com.gpachov.masterthesis.db.DbConnectionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class AmazonReviewsDBBuilder implements DBBuilder {

	@Override
	public String getCollectionName() {
		return DBConstants.AMAZON_COLLECTION;
	}

	public static void main(String[] args) {
		DBBuilder dbBuilder = new AmazonReviewsDBBuilder();
		MongoClient client = new DbConnectionFactory().newConnection();
		DB db = client.getDB(DBConstants.DB_NAME);
		DBCollection collection = db.getCollection(dbBuilder
				.getCollectionName());
		collection.drop();
		final Path amazonDataHomePath = Paths
				.get("/home/georgi/Dev/AmazonReviewsData");
		try {
			Files.list(amazonDataHomePath).forEach(path -> {
				try {
					Files.list(path).map(p -> parseOpinions(p)).forEach(m -> {

						List<Entry> entries = new ArrayList<>();
						m.keySet().forEach(k -> {
							entries.add(new Entry(k, m.get(k)));
						});

						entries.forEach(e -> {
							if (collection.count(e) == 0) {
								if (e.getEvaluation() != 0 /*
															 * &&
															 * e.getEvaluation()
															 * < 0.6f
															 */) {
									System.out.println("Saving " + e);
									collection.save(e);
								}
							}
						});

					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Map<String, Float> parseOpinions(Path jsonFile) {
		final Map<String, Float> results = new HashMap<String, Float>();
		try {
			String json = new String(Files.readAllBytes(jsonFile));
			JSONObject jsonObject = new JSONObject(json);
			JSONArray reviews = jsonObject.getJSONArray("Reviews");
			for (int i = 0; i < reviews.length(); i++) {
				JSONObject review = reviews.getJSONObject(i);
				try {
					double overall = review.getDouble("Overall");
					String text = review.getString("Content");
					results.put(text, (float) overall);
				} catch (JSONException e) {
					continue;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

}
