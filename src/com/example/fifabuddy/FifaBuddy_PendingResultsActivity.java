package com.example.fifabuddy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Data.Result;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import databaseTasks.ApprovedResultTask;
import databaseTasks.GetResultsTask;
import databaseTasks.NotApprovedResultTask;

/**
 * The activity is responsible to display the screen for when the user clicks on
 * the “Pending Results” button to view a list of his pending results.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_PendingResultsActivity extends Activity {

	private String username;
	private ArrayList<Result> results;
	private ArrayList<String> stringResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pendingresults);
		getUser();
		getPendingResults();
		final ListView pendingview = (ListView) findViewById(R.id.pendingresultsListView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, stringResults);
		pendingview.setAdapter(adapter);
		pendingview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Result res = results.get(arg2);
				newDialog(res);
			}
		});
	}

	protected void newDialog(Result res) {
		final Dialog dialog = new Dialog(this);
		final Result r = res;
		dialog.setContentView(R.layout.dialog);
		TextView t = (TextView) dialog.findViewById(R.id.dialogText);
		t.setText("Would you like to approve this Result?");
		Button yes = (Button) dialog.findViewById(R.id.yesButton);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				approve(r);
				dialog.dismiss();
			}
		});
		Button no = (Button) dialog.findViewById(R.id.noButton);
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				disapprove(r);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	protected void disapprove(Result r) {
		Boolean app = false;
		try {
			app = new NotApprovedResultTask().execute(r).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (app) {
			showToast("Result Rejected");
			finish();
		} else {
			showToast("Result Not Rejected");
		}
	}

	protected void approve(Result r) {
		Boolean app = false;
		try {
			app = new ApprovedResultTask().execute(r).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (app) {
			showToast("Result Approved");
			finish();
		} else {
			showToast("Result Not Approved");
		}
	}

	private void getPendingResults() {
		try {
			results = new ArrayList<Result>();
			stringResults = new ArrayList<String>();
			results = new GetResultsTask().execute(username, "PendingResults")
					.get();
			for (int i = 0; i < results.size(); i++) {
				stringResults.add("");
			}
			for (int i = 0; i < results.size(); i++) {
				stringResults.set(i, results.get(i).getResult(username));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void getUser() {
		Bundle extras = getIntent().getExtras();
		username = extras.getString("userName");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__pending_results, menu);
		return true;
	}

	public void showToast(String msg) {
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		toast.setView(layout);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);
		toast.show();
	}

}
