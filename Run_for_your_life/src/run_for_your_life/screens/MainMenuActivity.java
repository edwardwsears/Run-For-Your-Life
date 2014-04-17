package run_for_your_life.screens;

import java.util.HashMap;

import run_for_your_life.sessions.SessionManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	private String userName;
	private SessionManager session;
	private BroadcastReceiver br;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		getActionBar().hide();

		// Session Manager
		session = new SessionManager(getApplicationContext());

		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();     

		// Check and redirect to LoginActivity if not logged in
		session.checkLogin();

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// userName
		String userName = user.get(SessionManager.USERNAME);
		// isNoob
		String isNoob = user.get(SessionManager.IS_NOOB);

		TextView userNameDisplay = (TextView)findViewById(R.id.display_userName);
		userNameDisplay.setText(userName);

		if (isNoob.equals("true")) {
			// TODO: Play through tutorial sequence
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("run_for_your_life.ACTION_LOGOUT");
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				System.out.println("onReceive - Logout in progress");
				//At this point you should start the login activity and finish this one
				finish();
			}
		};
		registerReceiver(br, intentFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		unregisterReceiver(br);
	}

	public void logout(View view) {
		// Clear session data and move to login screen
		session.logoutUser();
	}


	public void goToDojo(View view) {
		Intent intent = new Intent(this, DojoActivity.class);
		intent.putExtra("userName", userName);
		startActivity(intent);
	}

	public void goToArena(View view) {
		Intent intent = new Intent(this, ArenaActivity.class);
		intent.putExtra("userName", userName);
		startActivity(intent);
	}

	public void goToFriends(View view) {
		Intent intent = new Intent(this, FriendsActivity.class);
		intent.putExtra("userName", userName);
		startActivity(intent);
	}

	public void goToStats(View view) {
		Intent intent = new Intent(this, StatsActivity.class);
		intent.putExtra("userName", userName);
		startActivity(intent);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
