package run_for_your_life.screens;

import run_for_your_life.sessions.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private EditText editText_username, editText_password;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
		
		// Session Manager
        session = new SessionManager(getApplicationContext()); 
		
		editText_username = (EditText) findViewById(R.id.editText_username);
		editText_password = (EditText) findViewById(R.id.editText_password);
	}
	
	/**
	 * Send login credentials to server to check
	 * @param view
	 */
	public void attemptLogin(View view) {
		// Get userName, password from EditText
        String userName = editText_username.getText().toString();
        String password = editText_password.getText().toString();
         
        // Check if userName, password is filled                
        if (userName.trim().length() > 0 && password.trim().length() > 0){
            // For testing purposes - userName, password is checked with sample data
            // userName = test
            // password = test
        	// TODO: Check for userName on server, if exists, get password and check against given password
            if (userName.equals("test") && password.equals("test")){
                // Creating user login session with userName = testUser, isNoob=true
                session.createLoginSession("testUser", "true");
                 
                // Staring MainActivity
                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(i);
                finish();     
            } else{
                // userName / password doesn't match
                System.out.println("Login failed... Username/Password is incorrect");
            }               
        } else{
            // user didn't enter userName or password
            // Show alert asking him to enter the details
            System.out.println("Login failed... Please enter username and password");
        }
	}
	
	/**
	 * Add new user to the system
	 * @param view
	 */
	public void makeNewUser(View view) {
		// TODO: Do new user stuff (create username/password, check that username is available)
		// SUCCESS: Launch the MainMenuActivity as a new user
		// FAILURE: Let them know why and stay put
		String userName = "";
		Intent intent = new Intent(this, MainMenuActivity.class);
		intent.putExtra("isNoob", true);
		intent.putExtra("userName", userName);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

}
