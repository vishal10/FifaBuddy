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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import databaseTasks.FriendsTask;
import databaseTasks.GetResultsTask;

/**
 * The activity is responsible to display the screen for when the user would
 * like to see the table of his results data with his friends.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_TableActivity extends Activity {

	private TableLayout resultsTable;
	private TableLayout resultsTableFrozen;
	private String username;
	private ArrayList<String> listFriends;
	private ArrayList<TableRow> rows;
	private ArrayList<TableRow> rowsFrozen;
	private ArrayList<ConsolidatedResult> results;
	private int reorderCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		resultsTable = (TableLayout) findViewById(R.id.ResultsTableLayout);
		resultsTableFrozen = (TableLayout) findViewById(R.id.ResultsTableLayoutFrozen);
		results = new ArrayList<ConsolidatedResult>();
		rows = new ArrayList<TableRow>();
		rowsFrozen = new ArrayList<TableRow>();
		Bundle extras = getIntent().getExtras();
		username = extras.getString("userName");
		reorderCount = 0;
		updateFriends();
		createTable();
		if (rows.size() > 1) {
			getFriendResults();
		}
		getUserResults();
		displayResults();
	}

	private void displayResults() {
		for (int i = 1; i <= results.size(); i++) {
			TableRow row = rows.get(i);
			TableRow rowF = rowsFrozen.get(i);
			final ConsolidatedResult cr = results.get(i - 1);
			TextView name = new TextView(this);
			editTextView(name, "" + cr.getName(), R.drawable.cell_shape);
			name.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					goToProfile("" + cr.getName());
				}
			});
			TextView wins = new TextView(this);
			editTextView(wins, "" + cr.getWins(), R.drawable.intcell_shape);
			TextView games = new TextView(this);
			editTextView(games, "" + cr.getGames(), R.drawable.intcell_shape);
			TextView losses = new TextView(this);
			editTextView(losses, "" + cr.getLosses(), R.drawable.intcell_shape);
			TextView draws = new TextView(this);
			editTextView(draws, "" + cr.getDraws(), R.drawable.intcell_shape);
			TextView points = new TextView(this);
			editTextView(points, "" + cr.getPoints(), R.drawable.intcell_shape);
			TextView goalsFor = new TextView(this);
			editTextView(goalsFor, "" + cr.getGoalsFor(),
					R.drawable.intcell_shape);
			TextView goalsAgainst = new TextView(this);
			editTextView(goalsAgainst, "" + cr.getGoalsAgainst(),
					R.drawable.intcell_shape);
			rowF.addView(name, 0);
			row.addView(games, 0);
			row.addView(wins, 1);
			row.addView(draws, 2);
			row.addView(losses, 3);
			row.addView(points, 4);
			row.addView(goalsFor, 5);
			row.addView(goalsAgainst, 6);
		}
	}

	protected void goToProfile(String string) {
		if (string.equals(username)) {
			finish();
			return;
		}
		Intent i = new Intent(this, FifaBuddy_FriendProfileActivity.class);
		i.putExtra("userName", username);
		i.putExtra("friendName", string);
		startActivity(i);
	}

	private void editTextView(TextView t, String string, int headcellShape) {
		t.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		t.setText(string);
		t.setBackgroundResource(headcellShape);
		t.setGravity(android.view.Gravity.CENTER);
		if (headcellShape == R.drawable.headcell_shape
				|| headcellShape == R.drawable.intheadcell_shape) {
			final String s = string;
			t.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					reorderCount++;
					reOrderData(s);
				}
			});
		}
	}

	protected void reOrderData(String s) {
		ArrayList<ConsolidatedResult> orderedResults = new ArrayList<ConsolidatedResult>(
				results.size());
		int count = 0;
		while (!results.isEmpty()) {
			int maxI, maxIndex;
			String maxS;
			if (reorderCount % 2 == 1) {
				maxI = Integer.MIN_VALUE;
			} else {
				maxI = Integer.MAX_VALUE;
			}
			maxS = results.get(0).getName();
			maxIndex = 0;
			for (int i = 0; i < results.size(); i++) {
				ConsolidatedResult cr = results.get(i);
				if (s.equals("Username")) {
					String name = cr.getName();
					if (reorderCount % 2 == 1) {
						if (name.compareTo(maxS) < 0) {
							maxS = name;
							maxIndex = i;
						}
					} else {
						if (name.compareTo(maxS) > 0) {
							maxS = name;
							maxIndex = i;
						}
					}
				} else {
					int val = maxI;
					if (s.equals("Wins")) {
						val = cr.getWins();
					} else if (s.equals("Games")) {
						val = cr.getGames();
					} else if (s.equals("Losses")) {
						val = cr.getLosses();
					} else if (s.equals("Draws")) {
						val = cr.getDraws();
					} else if (s.equals("Points")) {
						val = cr.getPoints();
					} else if (s.equals("Goals Scored")) {
						val = cr.getGoalsFor();
					} else if (s.equals("Goals Conceded")) {
						val = cr.getGoalsAgainst();
					}
					if (reorderCount % 2 == 1) {
						if (val > maxI) {
							maxI = val;
							maxIndex = i;
						}
					} else {
						if (val < maxI) {
							maxI = val;
							maxIndex = i;
						}
					}
				}
			}
			orderedResults.add(count++, results.remove(maxIndex));
		}
		results = orderedResults;
		clearTable();
		displayResults();
	}

	private void clearTable() {
		int count = 0;
		for (TableRow row : rows) {
			if (count > 0) {
				row.removeAllViews();
			}
			count++;
		}
		count = 0;
		for (TableRow row : rowsFrozen) {
			if (count > 0) {
				row.removeAllViews();
			}
			count++;
		}
	}

	private void getUserResults() {
		ConsolidatedResult cr = new ConsolidatedResult(username);
		ArrayList<Result> res;
		try {
			res = new GetResultsTask().execute(username, "ApprovedResults")
					.get();
			for (Result r : res) {
				cr.addResult(r);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		results.add(cr);
	}

	private void getFriendResults() {
		for (String s : listFriends) {
			ConsolidatedResult cr = new ConsolidatedResult(s);
			ArrayList<Result> res;
			try {
				res = new GetResultsTask().execute(s, "ApprovedResults").get();
				for (Result r : res) {
					cr.addResult(r);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			results.add(cr);
		}
	}

	private void createTable() {
		int numRows = 0;
		if (listFriends.size() == 1) {
			if (listFriends.get(0).equals("No Friends Yet")) {
				numRows = 0;
			} else {
				numRows = 1;
			}
		} else {
			numRows = listFriends.size();
		}
		numRows += 2;
		for (int i = 0; i < numRows; i++) {
			TableRow row = new TableRow(this);
			TableRow rowF = new TableRow(this);
			rowsFrozen.add(rowF);
			resultsTableFrozen.addView(rowF);
			rows.add(row);
			resultsTable.addView(row);
		}
		initTable();
	}

	private void initTable() {
		TableRow row = rows.get(0);
		TableRow rowF = rowsFrozen.get(0);
		TextView name = new TextView(this);
		editTextView(name, "Username", R.drawable.headcell_shape);
		TextView wins = new TextView(this);
		editTextView(wins, "Wins", R.drawable.intheadcell_shape);
		TextView games = new TextView(this);
		editTextView(games, "Games", R.drawable.intheadcell_shape);
		TextView losses = new TextView(this);
		editTextView(losses, "Losses", R.drawable.intheadcell_shape);
		TextView draws = new TextView(this);
		editTextView(draws, "Draws", R.drawable.intheadcell_shape);
		TextView points = new TextView(this);
		editTextView(points, "Points", R.drawable.intheadcell_shape);
		TextView goalsFor = new TextView(this);
		editTextView(goalsFor, "Goals For", R.drawable.intheadcell_shape);
		TextView goalsAgainst = new TextView(this);
		editTextView(goalsAgainst, "Goals Against",
				R.drawable.intheadcell_shape);
		row.addView(games, 0);
		row.addView(wins, 1);
		row.addView(draws, 2);
		row.addView(losses, 3);
		row.addView(points, 4);
		row.addView(goalsFor, 5);
		row.addView(goalsAgainst, 6);
		rowF.addView(name, 0);
	}

	private void updateFriends() {
		try {
			ArrayList<String> friends = new FriendsTask()
					.execute("2", username).get();
			if (friends == null) {
				listFriends = new ArrayList<String>();
				listFriends.add("No Friends Yet");
			} else {
				listFriends = friends;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__table, menu);
		return true;
	}

}
