package run_for_your_life.remote;

public static interface FriendConnect{

    public String[] searchFriends(String name);

    public User getFriendInfo(String username);
}
