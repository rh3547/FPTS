package fpts.model;

import fpts.toolkit.SaveNotifier;

import java.time.LocalDate;

/**
 * Created by Joshua Tobin on 3/2/16.
 * <p>
 * Class representing a Cash Account. Extends holding
 */
public class CashAccount extends Holding{

    /**
     * Constructor for a Cash Account
     * @param name - User specified name for the account
     * @param value - The money in the account
     * @param date - Date the account was created
     */
    public CashAccount(String name, float value, LocalDate date) {
        super(name, value, date);
    }

    /**
     * @return readable string for printing
     */
    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\",\"%s\"",
                this.getName(), this.getValue(), this.getDate());
    }

    /**
     * Creates a csv line that holds a cash account
     *
     * @return - string representing a csv line
     */
    public String exportString(){
        return String.format("\"Cash Account\",\"%s\",\"%s\",\"%s\"",
                this.getName(), this.getValue(), this.getDate());
    }

    /**
     * @param saveNotifier add an saveNotifier to the object to be notified
     */
    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        super.registerWatcher(saveNotifier);
    }

    /**
     * @param saveNotifier remove an saveNotifier so that it is no longer notified
     */
    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        super.removeWatcher(saveNotifier);
    }

    /**
     * notify the observes of any changes made
     */
    @Override
    public void notifyWatchers() {
        super.notifyWatchers();
    }
}
