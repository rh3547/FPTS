package fpts.model.Watchlist;

import fpts.controller.UserController;
import fpts.toolkit.Export;
import fpts.toolkit.SaveNotifier;
import fpts.toolkit.SaveSubject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by George Herde on 4/5/16.
 *
 * A single item in a watchlist that represents a certain stock.
 */
public class WatchlistItem implements SaveSubject {
    private String ticker;
    private float highTrigger;
    private float lowTrigger;

    private boolean lowTriggerActive;
    private boolean lowTriggerActiveHistory;
    private boolean highTriggerActive;
    private boolean highTriggerActiveHistory;

    private List<SaveNotifier> saveNotifiers = new LinkedList<>();

    /**
     * Create a new watchlist item.
     * @param lowTrigger - triggers when the stock price is less than or equal to this
     * @param highTrigger - triggers when the stock price is greater than or equal to this
     * @param ticker - the ticker symbol of the equity being monitored
     */
    public WatchlistItem(float lowTrigger, float highTrigger, String ticker) {
        this(ticker, highTrigger, lowTrigger, false, false, false, false);
    }

    /**
     * Create a new watchlist item from CSV.
     * @param lowTrigger - triggers when the stock price is less than or equal to this
     * @param highTrigger - triggers when the stock price is greater than or equal to this
     * @param ticker - the ticker symbol of the equity being monitored
     * @param lowTriggerActive - is low trigger active
     * @param highTriggerActive - is high trigger active
     * @param lowTriggerActiveHistory - has the low trigger been active
     * @param highTriggerActiveHistory - has the low trigger been active
     */
    public WatchlistItem(String ticker, float highTrigger,
                         float lowTrigger, boolean lowTriggerActive,
                         boolean lowTriggerActiveHistory, boolean highTriggerActive,
                         boolean highTriggerActiveHistory) {
        this.ticker = ticker;
        this.highTrigger = highTrigger;
        this.lowTrigger = lowTrigger;
        this.lowTriggerActive = lowTriggerActive;
        this.lowTriggerActiveHistory = lowTriggerActiveHistory;
        this.highTriggerActive = highTriggerActive;
        this.highTriggerActiveHistory = highTriggerActiveHistory;
        this.registerWatcher(new Export());
        if (UserController.getInstance().getAuthedUser().getWatchlist() != null) {
            UserController.getInstance().getAuthedUser().getWatchlist().addWatchItem(this);
            notifyWatchers();
        }
    }

    /**
     * Get the ticker symbol for this item.
     * @return ticker - String
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * Get the high trigger for this item.
     * @return highTrigger - float
     */
    public float getHighTrigger() {
        return highTrigger;
    }

    /**
     * Get the low trigger for this item.
     * @return lowTrigger - float
     */
    public float getLowTrigger() {
        return lowTrigger;
    }

    /**
     * Check if the low trigger is active.
     * @return lowTriggerActive - boolean
     */
    public boolean isLowTriggerActive() {
        return this.lowTrigger != -1 && lowTriggerActive;
    }

    /**
     * Check if the low trigger has been active.
     * @return lowTriggerActiveHistory - boolean
     */
    public boolean isLowTriggerActiveHistory() {
        return this.lowTrigger != -1 && lowTriggerActiveHistory;
    }

    /**
     * Check if the high trigger is active.
     * @return highTriggerActive - boolean
     */
    public boolean isHighTriggerActive() {
        return this.highTrigger != -1 && highTriggerActive;
    }

    /**
     * Check if the high trigger has been active.
     * @return highTriggerActiveHistory - boolean
     */
    public boolean isHighTriggerActiveHistory() {
        return this.lowTrigger != -1 && highTriggerActiveHistory;
    }

    /**
     * Set the ticker symbol of this item.
     * @param ticker - String
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
        notifyWatchers();
    }

    /**
     * Set the high trigger of this item.
     * @param highTrigger - float
     */
    public void setHighTrigger(float highTrigger) {
        this.highTrigger = highTrigger;
        notifyWatchers();
    }

    /**
     * Remove the high trigger of this item.
     */
    public void removeHighTrigger() {
        this.highTrigger = -1;
        notifyWatchers();
    }

    /**
     * Set the low trigger of this item.
     * @param lowTrigger - float
     */
    public void setLowTrigger(float lowTrigger) {
        this.lowTrigger = lowTrigger;
        notifyWatchers();
    }

    /**
     * Remove the low trigger of this item.
     */
    public void removeLowTrigger() {
        this.lowTrigger = -1;
        notifyWatchers();
    }

    /**
     * Toggle the high trigger active flag.
     */
    public void toggleHighTriggerActive() {
        setHighTriggerActive(!this.highTriggerActive);
    }

    /**
     * Toggle the low trigger active flag.
     */
    public void toggleLowTriggerActive() {
        setLowTriggerActive(!this.lowTriggerActive);
    }

    /**
     * Set the low trigger active flag.
     * @param lowTriggerActive - boolean
     */
    public void setLowTriggerActive(boolean lowTriggerActive) {
        this.lowTriggerActive = lowTriggerActive;
        if (lowTriggerActive) setLowTriggerActiveHistory(true);
        notifyWatchers();
    }

    /**
     * Set the low trigger active history flag.
     * @param lowTriggerActiveHistory - boolean
     */
    public void setLowTriggerActiveHistory(boolean lowTriggerActiveHistory) {
        this.lowTriggerActiveHistory = lowTriggerActiveHistory;
        notifyWatchers();
    }

    /**
     * Set the high trigger active flag.
     * @param highTriggerActive - boolean
     */
    public void setHighTriggerActive(boolean highTriggerActive) {
        this.highTriggerActive = highTriggerActive;
        if (highTriggerActive) setHighTriggerActiveHistory(true);
        notifyWatchers();
    }

    /**
     * Set the high trigger active history flag.
     * @param highTriggerActiveHistory - boolean
     */
    public void setHighTriggerActiveHistory(boolean highTriggerActiveHistory) {
        this.highTriggerActiveHistory = highTriggerActiveHistory;
        notifyWatchers();
    }

    @Override
    public String toString() {
        return String.format("Ticker: %s High:%f High-Active:%s High-History:%s Low:%f Low-Active:%s Low-History:%s",
                this.getTicker(),
                this.getHighTrigger(), this.isHighTriggerActive(), this.isHighTriggerActiveHistory(),
                this.getLowTrigger(), this.isLowTriggerActive(), this.isLowTriggerActiveHistory());
    }

    /**
     * Export this watchlist item as a string to CSV.
     * @return String
     */
    public String exportString() {
        return String.format("\"Watch List Item\", \"%s\", \"%f\", \"%s\", \"%s\", \"%f\", \"%s\", \"%s\"",
                this.getTicker().trim(),
                this.getHighTrigger(), this.isHighTriggerActive(), this.isHighTriggerActiveHistory(),
                this.getLowTrigger(), this.isLowTriggerActive(), this.isLowTriggerActiveHistory());

    }

    /**
     * @param saveNotifier add an saveNotifier to the object to be notified
     */
    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        saveNotifiers.add(saveNotifier);
    }

    /**
     * @param saveNotifier remove an saveNotifier so that it is no longer notified
     */
    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        saveNotifiers.remove(saveNotifier);
    }

    /**
     * notify the observes of any changes made
     */
    @Override
    public void notifyWatchers() {
        saveNotifiers.forEach(SaveNotifier::update);
    }
}
