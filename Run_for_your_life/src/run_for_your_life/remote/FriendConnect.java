package run_for_your_life.remote;

import run_for_your_life.user.User;

public interface FriendConnect{

    public String[] searchFriends(String name);

    public User getFriendInfo(String username);
}
