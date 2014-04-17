package run_for_your_life.screens;

import android.app.Activity;
import android.os.Bundle;

public class FriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}	
}
