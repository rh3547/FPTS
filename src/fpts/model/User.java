package fpts.model;

import fpts.model.Watchlist.Watchlist;

/**
 *
 * Created by George Herde on 3/2/16.
 *
 * Holds a unique users ID, password and portfolio that contains holdings
 */
public class User {
    private String userID;
    private char[] password;
    private Portfolio portfolio;
    private Watchlist watchlist;

    public User(String userID, char[] password) {
        this.userID = userID;
        this.password = password;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public String getUserID() {
        return userID;
    }

    public char[] getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\"", this.getUserID(), this.getPortfolio());
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }
}
