package databaseTasks;

import java.util.List;

import Data.User;
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;

/**
 * This task is responsible for taking in the userid of a user and returning all
 * the other information for that user.
 * 
 * @author Vishal
 * 
 */
public class GetUserInfoTask extends AsyncTask<String, Void, User> {

	private static AmazonSimpleDBClient client;

	@Override
	protected User doInBackground(String... params) {
		client = new SimpleDBAccess().getClient();
		GetAttributesRequest gar = new GetAttributesRequest("Users", params[0]);
		GetAttributesResult result = client.getAttributes(gar);
		if (result == null || result.getAttributes().size() == 0) {
			return null;
		}
		List<Attribute> attrs = result.getAttributes();
		String password = "";
		String name = "";
		for (Attribute a : attrs) {
			if (a.getName().equals("Password")) {
				password = a.getValue();
			}
			if (a.getName().equals("Name")) {
				name = a.getValue();
			}
		}
		User user = new User(name, params[0], password);
		return user;
	}
}
