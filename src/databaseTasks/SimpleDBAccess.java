package databaseTasks;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

/**
 * This class is used to get an Amazon SimpleDB client in order to start
 * interacting with the databases. This client is used by all of the above
 * tasks.
 * 
 * @author Vishal
 * 
 */
public class SimpleDBAccess {

	private static final String ACCESS_KEY_ID = "AKIAJF3YPCWQHGAT35MA";
	private static final String SECRET_KEY = "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";
	private AmazonSimpleDBClient client;

	public SimpleDBAccess() {
		AWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY_ID,
				SECRET_KEY);
		client = new AmazonSimpleDBClient(creds);
	}

	public AmazonSimpleDBClient getClient() {
		return client;
	}

}
