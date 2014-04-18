package run_for_your_life.remote;

public static interface OpponentSearch{

    public boolean isOnline(String name);

    public User[] getAvailableOpponents();
}
