package com.gpachov.masterthesis.datafetching.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.gpachov.masterthesis.data.RawDataEntry;
import com.gpachov.masterthesis.data.sources.GooglePlusSource;
import com.gpachov.masterthesis.datafetching.DataFetcher;
import com.gpachov.masterthesis.datafetching.Query;
import com.gpachov.masterthesis.exceptions.DataFetchingException;

@FetcherFor(GooglePlusSource.class)
public class GooglePlusDataFetcher implements DataFetcher {
	private static final String CLIENT_ID = "420817707478-jrhfogmrbk5ojvimlkvsa99va2733te1.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "7WCArFjuactZqADXNK0A1B-G";
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static final String REDIRECT_URI_2 = "http://localhost";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

	@Override
	public List<RawDataEntry> fetchData(Query query) {
		//		GoogleCredential credential = initConnection();

		// Create a new authorized API client.
//		Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
//				.setApplicationName("theiss").build();
//
//		Plus.Activities.Search searchActivities;
//		try {
//			searchActivities = plus.activities().search("awesome");
//			searchActivities.setMaxResults(5L);
//
//			ActivityFeed activityFeed = searchActivities.execute();
//			List<Activity> activities = activityFeed.getItems();
//
//			int pageNumber = 1;
//			while (activities != null && pageNumber <= 2) {
//				pageNumber++;
//
//				for (Activity activity : activities) {
//					System.out.println("ID " + activity.getId() + " Content: "
//							+ activity.getObject().getContent());
//				}
//
//				// We will know we are on the last page when the next page token
//				// is
//				// null.
//				// If this is the case, break.
//				if (activityFeed.getNextPageToken() == null) {
//					break;
//				}
//
//				// Prepare to request the next page of activities
//				searchActivities.setPageToken(activityFeed.getNextPageToken());
//
//				// Execute and process the next page request
//				activityFeed = searchActivities.execute();
//				activities = activityFeed.getItems();
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return new ArrayList<RawDataEntry>();
	}

	private GoogleCredential initConnection() {
		try {
			GoogleAuthorizationCodeRequestUrl authUrl = new GoogleAuthorizationCodeRequestUrl(
					CLIENT_ID, REDIRECT_URI, Arrays.asList(""));
			authUrl.setAccessType("offline");
			authUrl.setApprovalPrompt("force");

			GoogleTokenResponse token = new GoogleAuthorizationCodeTokenRequest(
					TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET,
					authUrl.build(), REDIRECT_URI).execute();
			System.out.println("Valid access token " + token.getAccessToken());
			GoogleCredential credentials = new GoogleCredential()
					.setAccessToken(token.getAccessToken());
			return credentials;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataFetchingException("Cannot fetch from Google Plus", e);
		}
	}
}
