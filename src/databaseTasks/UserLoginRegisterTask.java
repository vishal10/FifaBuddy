package databaseTasks;

import java.util.ArrayList;
import java.util.List;

import Data.User;
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

/**
 * This task is responsible for accessing the Users table and registering new
 * users as well as validating returning users.
 * 
 * @author Vishal
 * 
 */
public class UserLoginRegisterTask extends AsyncTask<String, Void, Boolean> {

	private AmazonSimpleDBClient client;

	private Boolean addUser(User user) {
		try {
			ReplaceableAttribute name = new ReplaceableAttribute("Name",
					user.getName(), Boolean.FALSE);
			ReplaceableAttribute pass = new ReplaceableAttribute("Password",
					user.getPassword(), Boolean.FALSE);
			List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(
					2);
			attrs.add(name);
			attrs.add(pass);
			PutAttributesRequest par = new PutAttributesRequest("Users",
					user.getEmail(), attrs);
			client.putAttributes(par);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private Boolean checkIfUserExists(String username) {
		GetAttributesRequest gar = new GetAttributesRequest("Users", username);
		GetAttributesResult result = client.getAttributes(gar);
		if (result == null || result.getAttributes().size() == 0) {
			return false;
		}
		return true;
	}

	private Boolean loginUser(String username, String password) {
		GetAttributesRequest gar = new GetAttributesRequest("Users", username);
		GetAttributesResult result = client.getAttributes(gar);
		List<Attribute> attrs = result.getAttributes();
		for (Attribute a : attrs) {
			if (a.getName().equals("Password")) {
				if (password.equals(a.getValue())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		client = new SimpleDBAccess().getClient();
		Boolean result = false;

		if (params[0].equals("1")) {
			result = checkIfUserExists(params[1]);
		} else if (params[0].equals("2")) {
			User user = new User(params[1], params[2], params[3]);
			result = addUser(user);
		} else if (params[0].equals("3")) {
			result = loginUser(params[1], params[2]);
		}
		return result;
	}
}
