package com.example.fifabuddy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Data.FriendData;
import Data.FriendListBaseAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import databaseTasks.FriendsTask;

/**
 * The activity is responsible to display the screen for when the user wants to
 * view a list of his friends
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_FriendsActivity extends Activity {

	private FriendListBaseAdapter adapter;
	private ArrayList<String> listItems;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		Bundle extras = getIntent().getExtras();
		username = extras.getString("userName");
		updateFriends();
		ListView friendsListView = (ListView) findViewById(R.id.friendsListView);
		adapter = new FriendListBaseAdapter(this, listItems);
		friendsListView.setAdapter(adapter);
		addListeners(friendsListView);
	}

	private void addListeners(final ListView friendsListView) {
		friendsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FriendData fd = (FriendData) friendsListView
						.getItemAtPosition(arg2);
				String friendName = fd.getName();
				Bitmap pic = fd.getImage();
				if (!friendName.equals("No Friends Yet"))
					goToFriendsProfile(friendName, pic);
			}
		});
	}

	protected void goToFriendsProfile(String friendName, Bitmap pic) {
		Intent i = new Intent(this, FifaBuddy_FriendProfileActivity.class);
		i.putExtra("userName", username);
		i.putExtra("friendName", friendName);
		i.putExtra("picture", pic);
		startActivity(i);
	}

	private void updateFriends() {
		try {
			ArrayList<String> friends = new FriendsTask()
					.execute("2", username).get();
			if (friends == null) {
				listItems = new ArrayList<String>();
				listItems.add("No Friends Yet");
			} else {
				listItems = friends;
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
		getMenuInflater().inflate(R.menu.fifa_buddy__friends, menu);
		return true;
	}

}
