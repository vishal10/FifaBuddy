package com.example.fifabuddy;

import java.io.File;
import java.util.concurrent.ExecutionException;

import Data.BitmapStringConvert;
import Data.User;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import databaseTasks.GetUserInfoTask;
import databaseTasks.PicturesTask;

/**
 * The activity is responsible to display the screen for when the user goes to
 * his profile once he successfully logs in or registers for the application.
 * 
 * @author Vishal
 * 
 */
public class FifaBuddy_UserProfileActivity extends Activity {

	private String username;
	private ImageView profilePic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userprofile);
		getName();
		initializeAddFriendButton();
		initializeFriendsButton();
		initializeAddResultButton();
		initializeTableButton();
		initializeProfilePicture();
		initializeStatistics();
		initializePendingResults();
		initializeLogout();
	}

	private void initializeLogout() {
		Button logout = (Button) findViewById(R.id.Logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				logout();
			}
		});
	}

	protected void logout() {
		Intent i = new Intent(this, FifaBuddy_HomePageActivity.class);
		startActivity(i);
		finish();
	}

	private void initializePendingResults() {
		Button pendingResults = (Button) findViewById(R.id.PendingResultsButton);
		pendingResults.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gotoPending();
			}
		});
	}

	protected void gotoPending() {
		Intent i = new Intent(this, FifaBuddy_PendingResultsActivity.class);
		i.putExtra("userName", username);
		startActivity(i);
	}

	private void initializeStatistics() {
		Button pendingResults = (Button) findViewById(R.id.userStatisticsButton);
		pendingResults.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				seeStatistics();
			}
		});
	}

	protected void seeStatistics() {
		Intent i = new Intent(this, FifaBuddy_StatisticsActivity.class);
		i.putExtra("userOrFriend", "user");
		i.putExtra("userName", username);
		startActivity(i);
	}

	private void initializeProfilePicture() {
		profilePic = (ImageView) findViewById(R.id.userProfilePicture);
		String image = "";
		try {
			image = new PicturesTask().execute("2", username).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		Bitmap pic = null;
		if (image != "") {
			pic = BitmapStringConvert.StringToBitMap(image);
		}
		if (pic != null) {
			profilePic.setImageBitmap(pic);
		}
		profilePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newDialog();
			}
		});
	}

	protected void newDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog);
		Button yes = (Button) dialog.findViewById(R.id.yesButton);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				launchCamera();
				dialog.dismiss();
			}
		});
		Button no = (Button) dialog.findViewById(R.id.noButton);
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	protected void launchCamera() {
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile()));
		startActivityForResult(i, 100);
	}

	private File getFile() {
		File file = new File(Environment.getExternalStorageDirectory(),
				getPackageName());
		if (!file.exists()) {
			file.mkdir();
		}
		File store = new File(file, username + "Pic.tmp");
		return store;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				File file = getFile();
				Bitmap pic = BitmapFactory.decodeFile(file.getAbsolutePath());
				String image = BitmapStringConvert.BitMapToString(pic, this);
				try {
					if ((new PicturesTask().execute("1", username, image).get())
							.equals("saved")) {
						showToast("Image was succesfully saved");
					} else {
						showToast("Image was not saved");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				profilePic.setImageBitmap(pic);
			} else if (resultCode != RESULT_CANCELED) {
				showToast("Image was not saved");
			}
		}

	}

	private void initializeTableButton() {
		Button viewTable = (Button) findViewById(R.id.viewTableButton);
		viewTable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewTable();
			}
		});
	}

	protected void viewTable() {
		Intent i = new Intent(this, FifaBuddy_TableActivity.class);
		i.putExtra("userName", username);
		startActivity(i);
	}

	private void initializeAddResultButton() {
		Button addResult = (Button) findViewById(R.id.homeAddResultButton);
		addResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addResult();
			}
		});
	}

	protected void addResult() {
		Intent i = new Intent(this, FifaBuddy_ResultActivity.class);
		i.putExtra("userName", username);
		i.putExtra("anyFriend", "yes");
		startActivity(i);
	}

	private void initializeFriendsButton() {
		Button friends = (Button) findViewById(R.id.ResultButton);
		friends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFriends();
			}
		});
	}

	protected void viewFriends() {
		Intent i = new Intent(this, FifaBuddy_FriendsActivity.class);
		i.putExtra("userName", username);
		startActivity(i);
	}

	private void initializeAddFriendButton() {
		Button addFriend = (Button) findViewById(R.id.NewResultWithFriendButton);
		addFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFriend();
			}
		});
	}

	protected void addFriend() {
		Intent i = new Intent(this, FifaBuddy_AddFriendActivity.class);
		i.putExtra("userName", username);
		startActivity(i);
	}

	private void getName() {
		Bundle extras = getIntent().getExtras();
		username = extras.getString("userName");
		User user = null;
		try {
			user = new GetUserInfoTask().execute(username).get();
			if (user == null) {
				return;
			}
			String name = user.getName();
			TextView welcome = (TextView) findViewById(R.id.welcomeText);
			welcome.setText(welcome.getText().toString() + name);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifa_buddy__user_profile, menu);
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
