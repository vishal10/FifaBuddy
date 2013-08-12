package com.example.fifabuddy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Data.Result;
import Data.TeamInfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import databaseTasks.FriendsTask;
import databaseTasks.PendingResultsTask;

/**
 * The activity is responsible to display the screen for when the user wants to
 * add a new result using the application.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_ResultActivity extends Activity {

	private String username;
	private String friendname;
	private Spinner friendList;
	private Spinner userLeagueList;
	private Spinner userTeamList;
	private Spinner friendLeagueList;
	private Spinner friendTeamList;
	private EditText userScore;
	private EditText friendScore;
	private TeamInfo teamInfo;
	private Button addResult;
	private Boolean anyFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Bundle extras = getIntent().getExtras();
		teamInfo = new TeamInfo();
		username = extras.getString("userName");
		friendname = extras.getString("friendName");
		anyFriend = extras.getString("anyFriend").equals("yes");

		friendList = (Spinner) findViewById(R.id.friendsList);
		userLeagueList = (Spinner) findViewById(R.id.userTeamLeagueList);
		userTeamList = (Spinner) findViewById(R.id.userTeamTeamList);
		friendLeagueList = (Spinner) findViewById(R.id.friendTeamLeagueList);
		friendTeamList = (Spinner) findViewById(R.id.friendTeamTeamList);

		userScore = (EditText) findViewById(R.id.userScore);
		friendScore = (EditText) findViewById(R.id.friendScore);

		updateLists();

		addResult = (Button) findViewById(R.id.ResultButton);
		addResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (anyFriend) {
					friendname = (String) friendList.getSelectedItem();
					if (friendname == null) {
						showToast("You have no friends to add a result with!");
					}
				}
				String uS = userScore.getText().toString();
				String fS = friendScore.getText().toString();
				if (uS == null || uS.equals("") || fS == null || fS.equals("")) {
					showToast("Please enter the scores.");
					return;
				}
				int uScore = 0;
				int fScore = 0;
				try {
					uScore = Integer.parseInt(uS);
					fScore = Integer.parseInt(fS);
				} catch (NumberFormatException e) {
					showToast("Please enter valid numbers.");
					return;
				}
				if (uScore < 0) {
					showToast("Users score needs to be valid.");
					return;
				}
				if (fScore < 0) {
					showToast("Friends score needs to be valid.");
					return;
				}
				String uTeam = (String) userTeamList.getSelectedItem();
				String fTeam = (String) friendTeamList.getSelectedItem();
				if (uTeam == null || uTeam.equals("Select a league first.")) {
					showToast("Users team needs to be valid.");
					return;
				}
				if (fTeam == null || fTeam.equals("Select a league first.")) {
					showToast("Friends team needs to be valid.");
					return;
				}
				Result res = new Result(username, friendname, uScore, fScore,
						uTeam, fTeam);
				Result[] params = { res };
				try {
					if (new PendingResultsTask().execute(params).get()) {
						showToast("Result Successfully Added");
						finish();
					} else {
						showToast("Result not added");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void updateLists() {
		try {
			ArrayList<String> friends = new FriendsTask()
					.execute("2", username).get();
			ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					friends);
			int pos = getPosition(friends, friendname);
			friendList.setAdapter(friendsAdapter);
			if (!anyFriend) {
				friendList.setSelection(pos);
				friendList.setEnabled(false);
			}

			String[] leagues = teamInfo.getLeagues();
			ArrayAdapter<String> leaguesAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					leagues);
			userLeagueList.setAdapter(leaguesAdapter);
			friendLeagueList.setAdapter(leaguesAdapter);

			userLeagueList
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							String league = (String) userLeagueList
									.getItemAtPosition(arg2);
							String[] teams = teamInfo.getTeams(league);
							setTeams(userTeamList, teams);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							String[] teams = { "Select a league first" };
							setTeams(userTeamList, teams);
						}
					});

			friendLeagueList
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							String league = (String) friendLeagueList
									.getItemAtPosition(arg2);
							String[] teams = teamInfo.getTeams(league);
							setTeams(friendTeamList, teams);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							String[] teams = { "Select a league first" };
							setTeams(friendTeamList, teams);
						}
					});

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	protected void setTeams(Spinner userTeamList2, String[] teams) {
		ArrayAdapter<String> teamsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, teams);
		userTeamList2.setAdapter(teamsAdapter);
	}

	private int getPosition(ArrayList<String> friends, String friendname2) {
		int pos = 0;
		for (String s : friends) {
			if (s.equals(friendname2)) {
				return pos;
			}
			pos++;
		}
		return -1;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__result, menu);
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
