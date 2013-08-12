package com.example.fifabuddy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Data.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import databaseTasks.FriendsTask;
import databaseTasks.GetUserInfoTask;

/**
 * The activity is responsible to display the screen for when the user clicks
 * the “Add a Friend” button from his profile.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_AddFriendActivity extends Activity {

	String friendUsername;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriend);

		Button addFriend = (Button) findViewById(R.id.addFriend_submit);
		addFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle extras = getIntent().getExtras();
				username = extras.getString("userName");
				EditText friend = (EditText) findViewById(R.id.addFriend_usernameInput);

				if (friend == null || friend.getText().toString().equals("")) {
					showToast("Enter a valid username.");
					return;
				}
				friendUsername = friend.getText().toString();
				if (friendUsername.equals(username)) {
					showToast("You cannot add yourself as a friend");
					return;
				}
				User user = null;
				try {
					user = new GetUserInfoTask().execute(friendUsername).get();
					if (user == null) {
						showToast("User does not exist.");
						return;
					}
					ArrayList<String> result = new FriendsTask().execute("1",
							username, friendUsername).get();
					if (result.size() == 1 && result.get(0).equals("Success")) {
						showToast("Successfully Added Friend");
						return;
					} else {
						showToast("Unable to add Friend");
						return;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__add_friend, menu);
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
