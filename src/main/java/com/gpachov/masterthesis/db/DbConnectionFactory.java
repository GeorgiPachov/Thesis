package com.gpachov.masterthesis.db;

import java.net.UnknownHostException;

import com.gpachov.masterthesis.exceptions.InfrastructureException;
import com.mongodb.MongoClient;

public class DbConnectionFactory {
	public MongoClient newConnection() {
		MongoClient client;
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			throw new InfrastructureException("Error connecting DB", e);
		}
		return client;
	}
}
