package run_for_your_life.user;

import java.util.ArrayList;

public class User{

    private String username;
    private String password;
    private int health;
    private int ammo;
    private int level;
    private ArrayList<String> friends;

    public User(){
    }

    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, int health, int ammo, int level, ArrayList<String> friends){
        this.username = username;
        this.password = password;
        this.health = health;
        this.ammo = ammo;
        this.level = level;
        this.friends = friends;
    }

    //get methods
    public String getName(){
        return username;
    }
    public String getPw(){
        return password;
    }
    public int getHealth(){
        return health;
    }
    public int getAmmo(){
        return ammo;
    }
    public int getLevel(){
        return level;
    }
    public ArrayList<String> getFriends(){
        return friends;
    }

    //set methods
    public void setUsername(String name){
        username = name;
    }
    public void setPassword(String pw){
        password = pw;
    }
    public void setHealth(int h){
        health = h;
    }
    public void setAmmo(int a){
        ammo = a;
    }
    public void setLevel(int l){
        level = l;
    }
    public void setFriends(ArrayList<String> f){
        friends = f;
    }
}
