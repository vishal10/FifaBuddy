package com.example.fifabuddy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Data.ConsolidatedResult;
import Data.Result;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import databaseTasks.GetResultsTask;

/**
 * The activity is responsible to display the screen for when the user wants to
 * view his statistics or those of any of his friends.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_StatisticsActivity extends Activity {

	private String name;
	private Boolean isUser;
	private ArrayList<Result> results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		getName();
		updateData();
		initializeResults();
	}

	private void initializeResults() {
		Button results = (Button) findViewById(R.id.viewResults);
		results.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				seeResults();
			}
		});
	}

	protected void seeResults() {
		Intent i = new Intent(this, FifaBuddy_ViewResultsActivity.class);
		ArrayList<String> stringResults = new ArrayList<String>();
		for (Result r : results) {
			stringResults.add(r.getResult(name));
		}
		if (isUser) {
			i.putExtra("userName", name);
		} else {
			i.putExtra("friendName", name);
		}
		i.putExtra("isUser", isUser);
		i.putExtra("results", stringResults);
		startActivity(i);
	}

	private void updateData() {
		ConsolidatedResult cr = new ConsolidatedResult(name);
		results = new ArrayList<Result>();
		try {
			results = new GetResultsTask().execute(name, "ApprovedResults")
					.get();
			for (Result r : results) {
				cr.addResult(r);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		TextView wins = (TextView) findViewById(R.id.statistics_Wins);
		updateVal(wins, wins.getText().toString(), cr.getWins());
		TextView games = (TextView) findViewById(R.id.statistics_Games);
		updateVal(games, games.getText().toString(), cr.getGames());
		TextView losses = (TextView) findViewById(R.id.statistics_Losses);
		updateVal(losses, losses.getText().toString(), cr.getLosses());
		TextView draws = (TextView) findViewById(R.id.statistics_Draws);
		updateVal(draws, draws.getText().toString(), cr.getDraws());
		TextView goalsfor = (TextView) findViewById(R.id.statistics_GoalsFor);
		updateVal(goalsfor, goalsfor.getText().toString(), cr.getGoalsFor());
		TextView goalsagainst = (TextView) findViewById(R.id.statistics_GoalsAgainst);
		updateVal(goalsagainst, goalsagainst.getText().toString(),
				cr.getGoalsAgainst());
		TextView points = (TextView) findViewById(R.id.statistics_Points);
		updateVal(points, points.getText().toString(), cr.getPoints());
	}

	private void updateVal(TextView t, String s, int count) {
		String app = "";
		app += count;
		t.setText(s + app);
	}

	private void getName() {
		Bundle extras = getIntent().getExtras();
		isUser = extras.getString("userOrFriend").equals("user");
		TextView welcome = (TextView) findViewById(R.id.statistics_welcomeMessage);
		if (isUser) {
			name = extras.getString("userName");
			welcome.setText("YOUR STATISTICS");
		} else {
			name = extras.getString("friendName");
			welcome.setText(name.toUpperCase() + "'S STATISTICS");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__statistics, menu);
		return true;
	}

}
