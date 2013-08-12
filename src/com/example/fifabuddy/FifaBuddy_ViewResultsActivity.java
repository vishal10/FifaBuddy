package com.example.fifabuddy;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The activity is responsible to display the screen for when the user wants to
 * see a history of his past results.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_ViewResultsActivity extends Activity {

	private String name;
	private Boolean isUser;
	private ArrayList<String> stringResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewresults);
		getUser();
		ListView resultsview = (ListView) findViewById(R.id.resultsListView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, stringResults);
		resultsview.setAdapter(adapter);
		resultsview.setClickable(false);
	}

	@SuppressWarnings("unchecked")
	private void getUser() {
		Bundle extras = getIntent().getExtras();
		isUser = extras.getBoolean("isUser");
		stringResults = (ArrayList<String>) extras.get("results");
		TextView welcome = (TextView) findViewById(R.id.resultsdialogText);
		if (isUser) {
			name = extras.getString("userName");
			welcome.setText("YOUR RESULTS");
		} else {
			name = extras.getString("friendName");
			welcome.setText(name.toUpperCase() + "'S STATISTICS");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_results, menu);
		return true;
	}

}
