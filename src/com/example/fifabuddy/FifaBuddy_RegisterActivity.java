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
 * register for the application.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_RegisterActivity extends Activity {

	private TextView name;
	private TextView email;
	private TextView pass;
	private TextView confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__register);

		Button register = (Button) findViewById(R.id.registerButton2);

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register();
			}
		});
	}

	protected void register() {
		name = (TextView) findViewById(R.id.register_name);
		email = (TextView) findViewById(R.id.register_emailAddress);
		pass = (TextView) findViewById(R.id.register_password);
		confirm = (TextView) findViewById(R.id.register_confirmPassword);

		if (validInput()) {
			addUser();
			Intent i = new Intent(this, FifaBuddy_UserProfileActivity.class);
			i.putExtra("userName", email.getText().toString());
			startActivity(i);
			finish();
		}

	}

	private void addUser() {
		String n = name.getText().toString();
		String e = email.getText().toString();
		String p = pass.getText().toString();
		new UserLoginRegisterTask().execute("2", n, e, p);
	}

	private boolean validInput() {
		if (name == null || name.getText().toString().equals("")) {
			showToast("Invalid Name.");
			return false;
		} else if (email == null || email.getText().toString().equals("")) {
			showToast("Invalid Email.");
			return false;
		} else if (pass == null || pass.getText().toString().equals("")) {
			showToast("Invalid Password.");
			return false;
		} else if (confirm == null || confirm.getText().toString().equals("")) {
			showToast("Invalid Confirmed Password.");
			return false;
		}

		if (emailExists()) {
			showToast("Email address already exists.");
			return false;
		}

		if (!(pass.getText().toString().equals(confirm.getText().toString()))) {
			showToast("Passwords do not match.");
			return false;
		}

		return true;
	}

	private boolean emailExists() {
		try {
			Boolean b = new UserLoginRegisterTask().execute("1",
					email.getText().toString()).get();
			return b;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__register, menu);
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
