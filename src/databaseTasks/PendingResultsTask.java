package databaseTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Data.Result;
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

/**
 * This task is responsible for adding a new result to the PendingResults table.
 * 
 * @author Vishal
 * 
 */
public class PendingResultsTask extends AsyncTask<Result, Void, Boolean> {

	private static AmazonSimpleDBClient client;

	@Override
	protected Boolean doInBackground(Result... arg0) {
		if (arg0 == null) {
			return false;
		}
		client = new SimpleDBAccess().getClient();
		Result result = arg0[0];
		if (result == null) {
			return false;
		}
		while (true) {
			Random rand = new Random();
			String randKey = "" + rand.nextInt();
			GetAttributesRequest req = new GetAttributesRequest(
					"PendingResults", randKey);
			GetAttributesResult res = client.getAttributes(req);
			if (res == null || res.getAttributes().size() == 0) {
				List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
				ReplaceableAttribute u1 = new ReplaceableAttribute("User1",
						result.getUser1(), true);
				ReplaceableAttribute u2 = new ReplaceableAttribute("User2",
						result.getUser2(), true);
				ReplaceableAttribute t1 = new ReplaceableAttribute("Team1",
						result.getTeam1(), true);
				ReplaceableAttribute t2 = new ReplaceableAttribute("Team2",
						result.getTeam2(), true);
				ReplaceableAttribute s1 = new ReplaceableAttribute("Score1", ""
						+ result.getScore1(), true);
				ReplaceableAttribute s2 = new ReplaceableAttribute("Score2", ""
						+ result.getScore2(), true);
				attributes.add(u1);
				attributes.add(u2);
				attributes.add(t1);
				attributes.add(t2);
				attributes.add(s1);
				attributes.add(s2);
				PutAttributesRequest par = new PutAttributesRequest(
						"PendingResults", randKey, attributes);
				client.putAttributes(par);
				return true;
			}
		}
	}

}
