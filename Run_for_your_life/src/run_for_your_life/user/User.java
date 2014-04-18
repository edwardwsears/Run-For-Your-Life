package run_for_your_live.user;

public class User{

    private String username;
    private String password;
    private int health;
    private int ammo;
    private int level;
    private String friends;

    public User(){
    }

    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    public User(String username,String password,int health, int ammo, int level, String friends){
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
    public Int getHealth(){
        return health;
    }
    public Int getAmmo(){
        return ammo;
    }
    public Int getLevel(){
        return level;
    }
    public String getFriends(){
        return friends;
    }

    //set methods
    public setUsername(String name){
        username = name;
    }
    public setPassword(String pw){
        password = pw;
    }
    public setHealth(int h){
        health = h;
    }
    public setAmmo(int a){
        ammo = a;
    }
    public setLevel(int l){
        level = l;
    }
    public setFriends(String f){
        friends = f;
    }
}
