package run_for_your_life.screens;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class FriendsActivity extends Activity {
	private BroadcastReceiver br;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
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
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}	
}
