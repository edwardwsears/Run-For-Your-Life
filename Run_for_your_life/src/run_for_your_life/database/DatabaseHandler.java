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
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " 
                + TABLE_USERINFO + " ("
                + KEY_USERNAME + " TEXT NOT NULL PRIMARY KEY," 
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_HEALTH + " INTEGER," 
                + KEY_AMMO + " INTEGER," 
                + KEY_LEVEL + " INTEGER," 
                + KEY_FRIENDS + " TEXT NOT NULL );";
        db.execSQL(CREATE_USER_TABLE);
        System.out.println("Created SQLiteDatabase");
        System.out.println(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);

        // Create tables again
        onCreate(db);
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getName());
        values.put(KEY_PASSWORD, user.getPw());
        values.put(KEY_HEALTH , user.getHealth());
        values.put(KEY_AMMO , user.getAmmo());
        values.put(KEY_LEVEL, user.getLevel());
        values.put(KEY_FRIENDS , user.getFriends());

        System.out.println("values");
        System.out.println(values.toString());
        
        db.insert(TABLE_USERINFO, null, values);
        db.close(); 
    }
    public User getUser (int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERINFO, new String[] {KEY_USERNAME}, KEY_USERNAME + "=?", 
                new String[] {""+id}, null, null, null, null);
        System.out.println(cursor.toString());
        System.out.println(cursor.getString(0));
        String username = cursor.getString(0);
        String password = cursor.getString(1);
        int health = Integer.parseInt(cursor.getString(2));
        int ammo = Integer.parseInt(cursor.getString(3));
        int level = Integer.parseInt(cursor.getString(4));
        String friends = cursor.getString(5);
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
                String friends = cursor.getString(5);
                User user = new User(username, password, health, ammo, level, friends);
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
 
        // return user list
        return userList;
    }

    public int updateUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getName());
        values.put(KEY_PASSWORD, user.getPw());
        values.put(KEY_HEALTH , user.getHealth());
        values.put(KEY_AMMO , user.getAmmo());
        values.put(KEY_LEVEL, user.getLevel());
        values.put(KEY_FRIENDS , user.getFriends());
        
        // updating row
        return db.update(TABLE_USERINFO, values, KEY_USERNAME + " = ?", new String[] { String.valueOf(user.getName()) });
    }
    
    public void deleteUser (String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERINFO, KEY_USERNAME + " = ?", new String[] { user });
        db.close();
    }
}
