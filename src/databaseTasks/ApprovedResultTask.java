package databaseTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Data.Result;
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * When a user approves a result, this task is responsible for moving that
 * result from the PendingResults table to the ApprovedResults table.
 * 
 * @author Vishal
 * 
 */
public class ApprovedResultTask extends AsyncTask<Result, Void, Boolean> {

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
		Boolean b1 = addToApprovedResults(result);
		Boolean b2 = false;
		if (b1) {
			b2 = removeFromPendingResults(result);
		}

		return (b1 && b2);
	}

	private Boolean removeFromPendingResults(Result result) {
		String val = "";
		SelectRequest sr1 = new SelectRequest(
				"select itemName() from `PendingResults` where User1 = '"
						+ result.getUser1() + "' and User2 = '"
						+ result.getUser2() + "' and Team1 = '"
						+ result.getTeam1() + "' and Team2 = '"
						+ result.getTeam2() + "' and Score1 = '"
						+ result.getScore1() + "' and Score2 = '"
						+ result.getScore2() + "'");
		SelectResult res1 = client.select(sr1);
		for (Item i : res1.getItems()) {
			val = i.getName();
			break;
		}
		if (val.equals("")) {
			return false;
		}
		DeleteAttributesRequest dar = new DeleteAttributesRequest(
				"PendingResults", val);
		client.deleteAttributes(dar);
		return true;
	}

	private Boolean addToApprovedResults(Result result) {
		while (true) {
			Random rand = new Random();
			String randKey = "" + rand.nextInt();
			GetAttributesRequest req = new GetAttributesRequest(
					"ApprovedResults", randKey);
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
						"ApprovedResults", randKey, attributes);
				client.putAttributes(par);
				return true;
			}
			return false;
		}
	}

}
