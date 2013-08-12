package databaseTasks;

import java.util.ArrayList;
import java.util.List;

import Data.Result;
import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * This task is responsible for getting all the results involving a particular
 * user, from the chosen table.
 * 
 * @author Vishal
 * 
 */
public class GetResultsTask extends AsyncTask<String, Void, ArrayList<Result>> {

	private static AmazonSimpleDBClient client;
	private ArrayList<Result> results;

	@Override
	protected ArrayList<Result> doInBackground(String... params) {
		if (params == null) {
			return null;
		}
		client = new SimpleDBAccess().getClient();
		String name = params[0];
		if (name == null || name.equals("")) {
			return null;
		}
		String domain = params[1];
		if (domain == null || domain.equals("")) {
			return null;
		}
		results = new ArrayList<Result>();

		if (domain.equals("ApprovedResults")) {
			SelectRequest sr1 = new SelectRequest("Select * from `" + domain
					+ "` where User1 = '" + name + "'");
			SelectResult res1 = client.select(sr1);
			for (Item i : res1.getItems()) {
				addResults(i.getAttributes());
			}
		}

		SelectRequest sr2 = new SelectRequest("Select * from `" + domain
				+ "` where User2 = '" + name + "'");
		SelectResult res2 = client.select(sr2);
		for (Item i : res2.getItems()) {
			addResults(i.getAttributes());
		}
		return results;
	}

	private void addResults(List<Attribute> attrs) {
		String u1 = "";
		String u2 = "";
		String t1 = "";
		String t2 = "";
		int s1 = -1;
		int s2 = -1;
		for (Attribute a : attrs) {
			if (a.getName().equals("User1")) {
				u1 = a.getValue();
			} else if (a.getName().equals("Team1")) {
				t1 = a.getValue();
			} else if (a.getName().equals("Team2")) {
				t2 = a.getValue();
			} else if (a.getName().equals("Score1")) {
				s1 = Integer.parseInt(a.getValue());
			} else if (a.getName().equals("Score2")) {
				s2 = Integer.parseInt(a.getValue());
			} else if (a.getName().equals("User2")) {
				u2 = a.getValue();
			}
		}
		Result temp = new Result(u1, u2, s1, s2, t1, t2);
		results.add(temp);
	}

}
