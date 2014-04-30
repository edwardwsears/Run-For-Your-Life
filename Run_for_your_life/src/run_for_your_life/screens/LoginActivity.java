package run_for_your_life.screens;

import java.util.ArrayList;

import run_for_your_life.sessions.SessionManager;
import run_for_your_life.database.*;
import run_for_your_life.user.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText editText_username, editText_password;
	private SessionManager session;
	private DatabaseHandler dbHandle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
		
		// Session Manager
        session = new SessionManager(getApplicationContext());
        dbHandle = new DatabaseHandler(getApplicationContext());
		
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
        	// TODO: Add initial users?
        	if (dbHandle.checkUsernamePassword(userName,password)){
                // Creating user login session with userName = testUser, isNoob=true
                session.createLoginSession("testUser", "true");
                 
                // Staring MainActivity
                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();     
            } else{
                // userName / password doesn't match
            	Toast.makeText(getApplicationContext(), "Login failed... Username/Password is incorrect",
            	Toast.LENGTH_SHORT).show();
            }               
        } else{
            // user didn't enter userName or password
            // Show alert asking him to enter the details
        	Toast.makeText(getApplicationContext(), "Login failed... Please enter username and password",
        	Toast.LENGTH_SHORT).show();
        }
	}
	
	/**
	 * Add new user to the system
	 * @param view
	 */
	public void makeNewUser(View view) {
		// TODO: Do new user stuff (create username/password, check that username is available, create login session)
		// SUCCESS: Launch the Tutorial screens
		// FAILURE: Let them know why and stay put
		String userName = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        if (dbHandle.checkUsernameExists(userName)){
        	//print toast
        	Toast.makeText(getApplicationContext(), "This username has already been taken",Toast.LENGTH_LONG).show();
        }
        else{
        	// Creating user login session with userName = testUser, isNoob=true
            session.createLoginSession(userName, "true");
        	User user = new User(userName,password,100,100,1,new ArrayList<String>());
        	dbHandle.addUser(user);
        	Intent intent = new Intent(this, Tutorial1Activity.class);
			intent.putExtra("isNoob", true);
			intent.putExtra("userName", userName);
			startActivity(intent);
        }
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

}
