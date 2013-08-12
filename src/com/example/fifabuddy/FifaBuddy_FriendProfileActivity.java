package com.example.fifabuddy;

import java.util.concurrent.ExecutionException;

import Data.User;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import databaseTasks.GetUserInfoTask;

/**
 * The activity is responsible to display the screen for when the user visits
 * the profile of one of his friends.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_FriendProfileActivity extends Activity {

	private String username;
	private String friendname;
	private Bitmap pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendprofile);
		Bundle extras = getIntent().getExtras();
		username = extras.getString("userName");
		friendname = extras.getString("friendName");
		pic = (Bitmap) extras.get("picture");
		TextView title = (TextView) findViewById(R.id.friendsText);
		try {
			User friend = new GetUserInfoTask().execute(friendname).get();
			title.setText("Welcome to " + friend.getName() + "'s Profile");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		ImageView image = (ImageView) findViewById(R.id.friendProfilePic);
		if (pic != null) {
			image.setImageBitmap(pic);
		}
		Button addResult = (Button) findViewById(R.id.NewResultWithFriendButton);
		addResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newResult();
			}
		});
		initializeStatistics();
	}

	private void initializeStatistics() {
		Button pendingResults = (Button) findViewById(R.id.friendStatisticsButton);
		pendingResults.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				seeStatistics();
			}
		});
	}

	protected void seeStatistics() {
		Intent i = new Intent(this, FifaBuddy_StatisticsActivity.class);
		i.putExtra("userOrFriend", "friend");
		i.putExtra("friendName", friendname);
		startActivity(i);
	}

	protected void newResult() {
		Intent i = new Intent(this, FifaBuddy_ResultActivity.class);
		i.putExtra("userName", username);
		i.putExtra("friendName", friendname);
		i.putExtra("anyFriend", "no");
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__friend_profile, menu);
		return true;
	}

}
