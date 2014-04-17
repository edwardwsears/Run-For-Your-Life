package run_for_your_life.sessions;

import java.util.HashMap;

import run_for_your_life.screens.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	private SharedPreferences pref;
	private Editor editor;
	private Context _context;

	// Shared preferences mode
	private int PRIVATE_MODE = 0;
	// Shared preferences file name
	private static final String PREF_NAME = "RFYLPref";
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "isLoggedIn";
	public static final String USERNAME = "userName";
	public static final String IS_NOOB = "isNoob";

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 **/
	public void createLoginSession(String userName, String isNoob){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		// Storing name in preferences
		editor.putString(USERNAME, userName);
		// Storing isNoob in preferences
		editor.putString(IS_NOOB, isNoob);

		// commit changes
		editor.commit();
	}   

	/**
	 * Check login status - 
	 * If false it will redirect user to login page
	 * Else won't do anything
	 **/
	public void checkLogin(){
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Starting Login Activity
			_context.startActivity(i);
		}
	}  

	/**
	 * Get stored session data
	 **/
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// userName
		user.put(USERNAME, pref.getString(USERNAME, null));
		// isNoob
		user.put(IS_NOOB, pref.getString(IS_NOOB, null));

		// return user
		return user;
	}

	/**
	 * Clear session details
	 **/
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		//        editor.clear();
		// Just remove the login status and userName, we want to remember whether or not this guy is a noob
		editor.remove(IS_LOGIN);
		editor.remove(USERNAME);
		editor.commit();

		// After logout redirect user to Login Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
