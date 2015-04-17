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
import com.gpachov.masterthesis.utils.Utils;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;


public class TripAdvisorJsonDbBuilder implements DBBuilder{
	private static final Path location = Paths.get("/home/georgi/TripAdvisor/json");
	public static void main(String[] args) throws IOException {
		DBBuilder dbBuilder = new TripAdvisorJsonDbBuilder();
		Map<String, Float> opinions = getOpinions();
		
		List<Entry> entries = new ArrayList<>();
		opinions.keySet().forEach(k -> {
			entries.add(new Entry(k, opinions.get(k)));
		});

		MongoClient client = new DbConnectionFactory().newConnection();
		DB db = client.getDB(DBConstants.DB_NAME);
		DBCollection collection = db.getCollection(dbBuilder.getCollectionName());
		collection.drop();

		entries.forEach(e -> {
			if (collection.count(e) == 0) {
				if (e.getEvaluation() != 0 /* && e.getEvaluation() < 0.6f */) {
					System.out.println("Saving " + e);
					collection.save(e);
				}
			}
		});
		client.close();
	}
	
	public static Map<String, Float> getOpinions() throws IOException{
		Map<String, Float> opinions = new HashMap<String, Float>();
		Files.list(location).forEach( jsonFile -> {
			String fileContent;
			try {
				fileContent = new String(Files.readAllBytes(jsonFile), "UTF-8");
				JsonObject jsonObject = new JsonObject(fileContent);
				JsonArray reviews = jsonObject.getJsonArray("Reviews");
				
				for (int i = 0; i < reviews.length(); i++){
					JsonObject review = reviews.getJsonObject(i);
					String ratingString = review.getJsonObject("Ratings").getString("Overall");
					Float rating = Utils.normalize(0, 5, Float.parseFloat(ratingString));
					String content = review.getString("Content");
					opinions.put(content, rating);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return opinions;
	}
	
	
	@Override
	public String getCollectionName() {
		return DBConstants.TRIP_ADVISOR_COLLECTION_NAME;
	}
	
}
