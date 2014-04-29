package run_for_your_life.database;

import java.util.ArrayList;
import java.util.List;

import run_for_your_life.user.User;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "localDB";

    // Users table name
    private static final String TABLE_USERINFO = "userInfo";
    private static final String TABLE_FRIENDINFO = "friendInfo";

    // Users Table Columns names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_HEALTH = "health";
    private static final String KEY_AMMO = "ammo";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_FRIENDS = "friends";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void add_to_friends_table(String username,ArrayList<String> friends){
        SQLiteDatabase db = this.getWritableDatabase();
        //add to friends table
        ContentValues fr = new ContentValues();
        for (String friend : friends){
            fr.put(KEY_USERNAME, username);
            fr.put(KEY_FRIEND , friend);
            db.insert(TABLE_FRIENDINFO, null, fr);
        }
    }


    private void delete_from_friends_table(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete * from "+TABLE_FRIENDINFO+" where "+KEY_USERNAME+"="+username;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("**********************************");
        //create user info table
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " 
                + TABLE_USERINFO + " ("
                + KEY_USERNAME + " TEXT NOT NULL PRIMARY KEY," 
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_HEALTH + " INTEGER," 
                + KEY_AMMO + " INTEGER," 
                + KEY_LEVEL + " INTEGER);";
        db.execSQL(CREATE_USER_TABLE);
        //create friend list table
        String CREATE_FRIENDS_TABLE = "CREATE TABLE IF NOT EXISTS " 
                + TABLE_FRIENDS + " ("
                + KEY_USERNAME + " TEXT NOT NULL PRIMARY KEY," 
                + KEY_FRIEND + " TEXT NOT NULL );";
        db.execSQL(CREATE_FRIENDS_TABLE);
        System.out.println("Created SQLiteDatabase");
        System.out.println(CREATE_USER_TABLE);
        System.out.println(CREATE_FRIENDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDINFO);

        // Create tables again
        onCreate(db);
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        username = user.getName();

        //add to user table
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, user.getPw());
        values.put(KEY_HEALTH , user.getHealth());
        values.put(KEY_AMMO , user.getAmmo());
        values.put(KEY_LEVEL, user.getLevel());

        add_to_friends_table(username,user.getFriends());

        System.out.println("values");
        System.out.println(values.toString());
        
        db.insert(TABLE_USERINFO, null, values);
        db.close(); 
    }
    public User getUser (String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select * from "+TABLE_USERINFO+" where "+KEY_USERNAME+"="+username,null);
        c.moveToNext();
        String password = c.getString(c.getColumnIndex(KEY_PASSWORD));
        int health = c.getInt(c.getColumnIndex(KEY_HEALTH));
        int ammo = c.getInt(c.getColumnIndex(KEY_AMMO));
        int level = c.getInt(c.getColumnIndex(KEY_level));
        
        ArrayList<String> friends = new ArrayList<String>();
        Cursor cfr = db.rawQuery("select * from "+TABLE_USERINFO+" where "+KEY_USERNAME+"="+username,null);
        while (cfr.moveToNext()){
            friends.add(cfr.getString(cfr.getColumnIndex(KEY_FRIEND)));
        }

        User user = new User(username, password, health, ammo, level, friends);

        return user;
    }
    
    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERINFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(0);
                String password = cursor.getString(1);
                int health = Integer.parseInt(cursor.getString(2));
                int ammo = Integer.parseInt(cursor.getString(3));
                int level = Integer.parseInt(cursor.getString(4));
                ArrayList<String> friends = new ArrayList<String>();
                //TODO friends not implemented
                User user = new User(username, password, health, ammo, level, friends);
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
 
        // return user list
        return userList;
    }

    public void updateUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        //update user table
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getName());
        values.put(KEY_PASSWORD, user.getPw());
        values.put(KEY_HEALTH , user.getHealth());
        values.put(KEY_AMMO , user.getAmmo());
        values.put(KEY_LEVEL, user.getLevel());

        //update friends table
        delete_from_friends_table(user.getName());
        add_to_friends_table(user.getName(),user.getFriends());
        
        // updating row
        db.update(TABLE_USERINFO, values, KEY_USERNAME + " = ?", new String[] { String.valueOf(user.getName()) });
        db.rawQuery("update "+TABLE_USERINFO+" set dd"where "+KEY_USERNAME+"="+username,null);
    }
    
    public void deleteUser (String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERINFO, KEY_USERNAME + " = ?", new String[] { user });
        delete_from_friends_table(user.getName());

        db.close();
    }
}
