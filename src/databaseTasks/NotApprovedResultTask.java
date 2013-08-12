package databaseTasks;

import Data.Result;
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * This task is responsible for removing a result from the PendingResults table
 * if a user does not approve the result.
 * 
 * @author Vishal
 * 
 */
public class NotApprovedResultTask extends AsyncTask<Result, Void, Boolean> {

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
		Boolean b2 = removeFromPendingResults(result);

		return b2;
	}

	private Boolean removeFromPendingResults(Result result) {
		String val = "";
		SelectRequest sr1 = new SelectRequest(
				"Select itemName() from `PendingResults` where User1 = '"
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

}
