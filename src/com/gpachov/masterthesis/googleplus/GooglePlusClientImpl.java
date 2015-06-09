package com.gpachov.masterthesis.googleplus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.common.io.Files;

public class GooglePlusClientImpl implements GooglePlusClient {

    private static final File FILE = new File("/home/georgi/EEworkspace/Diplomna/src/key.p12");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static NetHttpTransport httpTransport;
    private static final String SERVICE_ACCOUNT_EMAIL = "218210216811-khj96q72httlv5gek11j86ch8haopoat@developer.gserviceaccount.com";
    private static GoogleCredential credential;
    static {
	try {
	    httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    credential= new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(JSON_FACTORY)
	    	.setServiceAccountId(SERVICE_ACCOUNT_EMAIL).setServiceAccountScopes(Collections.singleton(PlusScopes.PLUS_ME))
	    	.setServiceAccountPrivateKeyFromP12File(FILE)
	    	.build();
	} catch (GeneralSecurityException | IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public List<String> getSearchResults(String query) {
	try {
	    validateServiceAccountEmail();
	    validateCertificate();

	    query = new URLCodec("UTF-8").encode(query);
	   

	    // set up global Plus instance
	    Plus plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("post-search").build();

	    Plus.Activities.Search searchActivities = plus.activities().search(query);
	    searchActivities.setMaxResults(20L);

	    ActivityFeed activityFeed = searchActivities.execute();
	    List<Activity> activities = activityFeed.getItems();

	    // Loop through until we arrive at an empty page
	    List<String> results = new ArrayList<String>();
	    int pageNumber = 1;
	    while (activities != null && pageNumber <= 20) {
		pageNumber++;

		for (Activity activity : activities) {
		    results.add(activity.getObject().getContent());
		}

		// We will know we are on the last page when the next page token
		// is
		// null.
		// If this is the case, break.
		if (activityFeed.getNextPageToken() == null) {
		    break;
		}

		// Prepare to request the next page of activities
		searchActivities.setPageToken(activityFeed.getNextPageToken());

		// Execute and process the next page request
		activityFeed = searchActivities.execute();
		activities = activityFeed.getItems();
	    }

	    // success!
	    return results;
	} catch (IOException | EncoderException e) {
	    throw new RuntimeException(e);
	}
    }

    private void validateCertificate() throws IOException {
	String p12Content = Files.readFirstLine(FILE, Charset.defaultCharset());
	if (p12Content.startsWith("Please")) {
	System.err.println(p12Content);
	System.exit(1);
	}
    }

    private void validateServiceAccountEmail() {
	// check for valid setup
	if (SERVICE_ACCOUNT_EMAIL.startsWith("Enter ")) {
	System.err.println(SERVICE_ACCOUNT_EMAIL);
	System.exit(1);
	}
    }
    
    public static void main(String[] args) {
	System.out.println(new GooglePlusClientImpl().getSearchResults("samsung"));
    }

}
