package run_for_your_life.remote;

import run_for_your_life.user.User;

public interface OpponentSearch{
    public boolean isOnline(String name);

    public User[] getAvailableOpponents();
}
