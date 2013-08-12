package com.example.fifabuddy;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import databaseTasks.UserLoginRegisterTask;

/**
 * The activity is responsible to display the screen for when the user wants to
 * log in to the application or register for the first time.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_HomePageActivity extends Activity {

	private TextView username;
	private TextView pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);

		Button register = (Button) findViewById(R.id.registerButton);
		Button login = (Button) findViewById(R.id.addFriend_submit);

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register();
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				username = (TextView) findViewById(R.id.addFriend_usernameInput);
				pass = (TextView) findViewById(R.id.passwordInput);

				if (validInput()) {
					login();
				}
			}
		});
	}

	private void login() {
		Intent i = new Intent(this, FifaBuddy_UserProfileActivity.class);
		i.putExtra("userName", username.getText().toString());
		startActivity(i);
		finish();
	}

	private boolean validInput() {
		if (username == null || username.getText().toString().equals("")) {
			showToast("Invalid Username.");
			return false;
		} else if (pass == null || pass.getText().toString().equals("")) {
			showToast("Invalid Password.");
			return false;
		}

		try {
			Boolean b = new UserLoginRegisterTask().execute("3",
					username.getText().toString(), pass.getText().toString())
					.get();
			if (!b) {
				showToast("Invalid Password for the given Username.");
				return false;
			}
			return b;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void register() {
		Intent i = new Intent(this, FifaBuddy_RegisterActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__home_page, menu);
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
