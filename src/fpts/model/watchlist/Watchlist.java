package fpts.model.Watchlist;

import fpts.controller.WatchlistController;
import fpts.toolkit.Export;
import fpts.toolkit.SaveNotifier;
import fpts.toolkit.SaveSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Requirement 6C: Watchlist of equities
 * Created by George Herde on 4/4/16.
 *
 * A watchlist is a list of stock items that are monitored so the user can be
 * notified when the price falls within set values.
 */
public class Watchlist implements SaveSubject {
    private List<WatchlistItem> watchlist;
    private ArrayList<SaveNotifier> saveNotifiers = new ArrayList<>();

    /**
     * Create a new watchlist.
     */
    public Watchlist() {
        this.watchlist = new ArrayList<>();
        this.saveNotifiers.add(new Export());
    }

    /**
     * Get the list of watchlist items.
     * @return watchlist
     */
    public List<WatchlistItem> getWatchlist() {
        return watchlist;
    }

    /**
     * Set the list of watchlist items.
     * @param watchlist
     */
    public void setWatchlist(List<WatchlistItem> watchlist) {
        this.watchlist = watchlist;
        notifyWatchers();
    }

    /**
     * Add a new item to the watchlist.
     * @param item - WatchlistItem
     */
    public void addWatchItem(WatchlistItem item) {
        this.watchlist.add(item);
        if (WatchlistController.getInstance().getWatchlist() != null){
            WatchlistController.getInstance().checkWatchlist();
        }
        notifyWatchers();
    }

    /**
     * Remove an item from the watchlist.
     * @param item - WatchlistItem
     */
    public void removeWatchItem(WatchlistItem item) {
        this.watchlist.remove(item);
        notifyWatchers();
    }

    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        saveNotifiers.add(saveNotifier);
    }

    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        saveNotifiers.remove(saveNotifier);
    }

    @Override
    public void notifyWatchers() {
        saveNotifiers.forEach(SaveNotifier::update);

    }
}
