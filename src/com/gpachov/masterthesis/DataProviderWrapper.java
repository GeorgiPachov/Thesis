package com.gpachov.masterthesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gpachov.masterthesis.db.DBConstants;
import com.gpachov.masterthesis.db.DbConnectionFactory;
import com.gpachov.masterthesis.utils.Utils;
import com.mongodb.Cursor;
import com.mongodb.MongoClient;

public class DataProviderWrapper implements IDataProvider {
	private static IDataProvider instance;

	private MongoClient mongo;

	private List<String> negativeCache;
	private List<String> positiveCache;

	public static IDataProvider getInstance() {
		if (instance == null) {
			instance = new DataProviderWrapper();
		}
		return instance;
	}

	@Override
	public List<String> getPositive() {
//		return new ArrayList<String>(positiveCache);
		return positiveCache;
	}

	@Override
	public List<String> getNegative() {
//		return new ArrayList<String>(negativeCache);
		return negativeCache;
	}

	public DataProviderWrapper() {
		this.mongo = new DbConnectionFactory().newConnection();
		Map<String, Double> allStrings = new HashMap<>();
		readFromGoodreads(allStrings);
		readFromTripAdvisor(allStrings);	

		negativeCache = allStrings.entrySet().stream().filter(e -> e.getValue() < 0.5)
				.collect(Collectors.toList()).stream().map(e -> e.getKey()).collect(Collectors.toList());
		
		this.positiveCache = allStrings.entrySet().stream().filter(e -> e.getValue() >= 0.5)
				.collect(Collectors.toList()).stream().map(e -> e.getKey()).collect(Collectors.toList());
		this.positiveCache = Utils.subList(getPositive(), 0, negativeCache.size());
		
	}

	private void readFromTripAdvisor(Map<String, Double> allStrings) {
		Cursor c = mongo.getDB(DBConstants.DB_NAME).getCollection(DBConstants.TRIP_ADVISOR_COLLECTION_NAME).find();
		c.forEachRemaining(d -> allStrings.put(d.get("text").toString(),
				Double.valueOf((d.get("evalutation")).toString())));
		c.close();
	}

	private Map<String, Double> readFromGoodreads(Map<String, Double> allStrings) {
		Cursor c = mongo.getDB(DBConstants.DB_NAME).getCollection(DBConstants.GOODREADS_COLLECTION_NAME).find();
		c.forEachRemaining(d -> allStrings.put(d.get("text").toString(),
				Double.valueOf((d.get("evalutation")).toString())));
		c.close();
		return allStrings;
	}
}
