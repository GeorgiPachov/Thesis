package com.gpachov.masterthesis.dbbuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicHeader;

import com.gpachov.masterthesis.data.Entry;
import com.gpachov.masterthesis.db.DBConstants;
import com.gpachov.masterthesis.db.DbConnectionFactory;
import com.gpachov.masterthesis.utils.Utils;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class GoodreadsDBBuilder implements DBBuilder {
	public static void main(String[] args) {
		DBBuilder dbBuilder = new GoodreadsDBBuilder();
		
		while (true) {
			Map<String, Integer> opinions;
			try {
				opinions = getOpinions();
				List<Entry> entries = new ArrayList<>();
				opinions.keySet().forEach(k -> {
					float v = Utils.normalize(0, 5, opinions.get(k));
					entries.add(new Entry(k, v));
				});

				MongoClient client = new DbConnectionFactory().newConnection();
				DB db = client.getDB(DBConstants.DB_NAME);
				DBCollection collection = db
						.getCollection(dbBuilder.getCollectionName());

				entries.forEach(e -> {
					if (collection.count(e) == 0) {
						if (e.getEvaluation() != 0 && e.getEvaluation() < 0.6f) {
							System.out.println("Saving " + e);
							collection.save(e);
						}
					}
				});
				client.close();
				Thread.sleep(10000L);
			} catch (IOException | FactoryConfigurationError
					| XMLStreamException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public String getCollectionName() {
		return DBConstants.GOODREADS_COLLECTION_NAME;
	}

	public static Map<String, Integer> getOpinions()
			throws ClientProtocolException, IOException,
			FactoryConfigurationError, XMLStreamException {
		Header key = new BasicHeader("key", "m7wZMKKb5rKohXJFzsULQg");
		Header secret = new BasicHeader("secret",
				"MwAYZjBzbCd4zMnhhu6j647Bkq9MsAvJrdcj0RjOUY");
		String output = Request
				.Get("https://www.goodreads.com/review/recent_reviews.xml?key=m7wZMKKb5rKohXJFzsULQg")
				.addHeader(key).addHeader(secret).execute().returnContent()
				.asString();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader eventReader = factory
				.createXMLStreamReader(new StringReader(output));

		final Map<String, Integer> opinions = new HashMap<String, Integer>();
		int rating = -2;
		String body = "";
		while (eventReader.hasNext()) {
			int event = eventReader.next();
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				if ("rating".equals(eventReader.getLocalName())) {
					rating = Integer.parseInt(eventReader.getElementText());
				}
				if ("body".equals(eventReader.getLocalName())) {
					body = (eventReader.getElementText());
					// body = preProcess(body);
					opinions.put(body, rating);
				}

				break;
			}

		}
		return opinions;
	}
}
