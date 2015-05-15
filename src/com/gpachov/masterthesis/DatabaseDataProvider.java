package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.db.DBConstants;
import com.gpachov.masterthesis.db.DbConnectionFactory;
import com.gpachov.masterthesis.utils.Utils;
import com.mongodb.Cursor;
import com.mongodb.Mongo.Holder;
import com.mongodb.MongoClient;

public class DatabaseDataProvider implements IDataProvider {
	private static IDataProvider instance;
	private List<String> unclassified = new ArrayList<String>();
	private Map<DataClass, List<String>> classified = new LinkedHashMap<DataClass, List<String>>();
	
	private MongoClient mongo;

	public static IDataProvider getInstance() {
		if (instance == null) {
			instance = new DatabaseDataProvider();
		}
		return instance;
	}

	public DatabaseDataProvider() {
		this.mongo = new DbConnectionFactory().newConnection();
		Map<String, Double> allStrings = new LinkedHashMap<>();
		readFromGoodreadsCollection(allStrings);
		readFromTripAdvisorCollection(allStrings);
//		readFromAmazon(allStrings);

		// add to unclassified
		allStrings.keySet().stream().forEach(s -> unclassified.add(s));
		
		// classify
		
		allStrings.entrySet().stream().forEach(e -> {
			Double evaluation = e.getValue();
			DataClass dataClass = Utils.classify(evaluation);
			classified.putIfAbsent(dataClass, new ArrayList<String>());
			
			String opinion = e.getKey();
			classified.get(dataClass).add(opinion);
		});
	}

	private void readFromAmazon(Map<String, Double> allStrings) {
		javax.xml.ws.Holder<Integer> holder = new javax.xml.ws.Holder<Integer>();
		holder.value = 100_000;
		Cursor c = mongo.getDB(DBConstants.DB_NAME)
				.getCollection(DBConstants.AMAZON_COLLECTION).find();
		c.forEachRemaining(d -> {
			if (holder.value > 0) {
				allStrings.put(d.get("text").toString(), normalize(Double
						.valueOf(d.get("evalutation").toString())));
				holder.value--;
			}
		});
	}

	private Double normalize(double valueOf) {
		return (valueOf / 5);
	}

	private void readFromTripAdvisorCollection(Map<String, Double> allStrings) {
		Cursor c = mongo.getDB(DBConstants.DB_NAME)
				.getCollection(DBConstants.TRIP_ADVISOR_COLLECTION_NAME).find();
		c.forEachRemaining(d -> allStrings.put(d.get("text").toString(),
				Double.valueOf((d.get("evalutation")).toString())));
		c.close();
	}

	private Map<String, Double> readFromGoodreadsCollection(Map<String, Double> allStrings) {
		Cursor c = mongo.getDB(DBConstants.DB_NAME)
				.getCollection(DBConstants.GOODREADS_COLLECTION_NAME).find();
		c.forEachRemaining(d -> allStrings.put(d.get("text").toString(),
				Double.valueOf((d.get("evalutation")).toString())));
		c.close();
		return allStrings;
	}

	@Override
	public List<String> getUnclassified() {
		return new ArrayList<String>(unclassified);
	}

	@Override
	public Map<DataClass, List<String>> getClassified() {
		return new LinkedHashMap<DataClass, List<String>>(classified);
	}
}
