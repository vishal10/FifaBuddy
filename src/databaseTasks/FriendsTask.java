package databaseTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * This task is responsible for adding friendships to the database and getting
 * the friends for a given user.
 * 
 * @author Vishal
 * 
 */
public class FriendsTask extends AsyncTask<String, Void, ArrayList<String>> {

	private static AmazonSimpleDBClient client;
	int numFriends;

	private boolean addFriendship(String name, String friend) {
		try {
			while (true) {
				Random rand = new Random();
				String randKey = "" + rand.nextInt();
				GetAttributesRequest req = new GetAttributesRequest(
						"PendingResults", randKey);
				GetAttributesResult res = client.getAttributes(req);
				if (res == null || res.getAttributes().size() == 0) {
					ReplaceableAttribute friendName = new ReplaceableAttribute(
							"Friend1", friend, Boolean.TRUE);
					ReplaceableAttribute userName = new ReplaceableAttribute(
							"Friend2", name, Boolean.TRUE);
					List<ReplaceableAttribute> friends = new ArrayList<ReplaceableAttribute>();
					friends.add(userName);
					friends.add(friendName);
					PutAttributesRequest par = new PutAttributesRequest(
							"Friends", randKey, friends);
					client.putAttributes(par);
					return true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private ArrayList<String> getFriends(String username) {
		ArrayList<String> friends = new ArrayList<String>();
		try {
			SelectRequest sr1 = new SelectRequest(
					"select Friend1 from `Friends` where Friend2 = '"
							+ username + "'", true);
			SelectRequest sr2 = new SelectRequest(
					"select Friend2 from `Friends` where Friend1 = '"
							+ username + "'", true);
			SelectResult res1 = client.select(sr1);
			SelectResult res2 = client.select(sr2);
			for (Item i : res1.getItems()) {
				String s = i.getAttributes().get(0).getValue();
				friends.add(s);
			}
			for (Item i : res2.getItems()) {
				String s = i.getAttributes().get(0).getValue();
				friends.add(s);
			}
			return friends;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		client = new SimpleDBAccess().getClient();
		ArrayList<String> friends = null;

		if (params[0].equals("1")) {
			if (addFriendship(params[1], params[2])) {
				friends = new ArrayList<String>();
				friends.add(0, "Success");
			}
		} else if (params[0].equals("2")) {
			friends = getFriends(params[1]);
		}
		return friends;
	}

}
