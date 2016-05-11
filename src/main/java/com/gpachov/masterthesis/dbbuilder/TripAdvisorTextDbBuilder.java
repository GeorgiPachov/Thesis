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

public class TripAdvisorTextDbBuilder implements DBBuilder {
	private static final Path location = Paths.get("/home/georgi/TripAdvisor/text");

	public static void main(String[] args) throws Exception {
		DBBuilder dbBuilder = new TripAdvisorTextDbBuilder();

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
		// Thread.sleep(10000L);
	}

	public static Map<String, Float> getOpinions() throws Exception {
		Map<String, Float> opinions = new HashMap<String, Float>();
		Files.list(location).forEach(p -> {

			String[] data = new String[2];
			try {
				Files.readAllLines(p).stream().forEach(line -> {

					if (line.toLowerCase().startsWith("<overall>")) {
						data[1] = line.toLowerCase().replaceAll("<overall>", "");
					}
					if (line.toLowerCase().startsWith("<content>")) {
						data[0] = line.toLowerCase().replaceAll("<content>", "");
					}

				});
				if (data[0] == null || data[1] == null) {
					throw new RuntimeException();
				}
				if (!data[0].contains("showreview(")) {
					opinions.put(data[0], Utils.normalize(0, 5, Float.parseFloat(data[1])));
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
