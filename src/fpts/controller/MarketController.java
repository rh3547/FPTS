package fpts.controller;

import fpts.model.Market;
import fpts.model.Portfolio;
import fpts.toolkit.Export;
import fpts.toolkit.SaveNotifier;
import fpts.toolkit.SaveSubject;
import fpts.toolkit.request.UpdateThread;

import java.util.*;

/**
 * Market controller and thread manager
 * Created by George Herde on 3/29/16.
 *
 * The market controller handles all the logic to update the market from
 * yahoo finance in threads.
 */
public class MarketController implements Runnable, SaveSubject {
    private static MarketController instance = new MarketController();
    private List<SaveNotifier> saveNotifiers = new ArrayList<>();

    public Market m;
    private UpdateThread t;
    private long delay = 2 * 60 * 1000;
    private Timer timer = new Timer();

    /**
     * Private constructor to prevent other instances from being created.
     */
    private MarketController() {
        m = new Market();
        ArrayList<String> a = new ArrayList<>();
        a.addAll(m.getMarket().keySet());
        Collections.sort(a, String::compareToIgnoreCase);
        t = new UpdateThread(m, a);
        registerWatcher(new Export());
    }

    /**
     * Get the only instance of MarketController.
     * @return MarketController
     */
    public static MarketController getInstance() {
        return instance;
    }

    /**
     * Start the timer to initiate the threads that update the market every so often.
     */
    public void StartTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                MarketController.getInstance().run();
            }
        }, 0, delay);
    }

    /**
     * Set the delay between market updates.
     * @param seconds - the number of seconds to wait between updates
     */
    public void setDelay(long seconds) {
        delay = seconds * 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                MarketController.getInstance().run();
            }
        }, 0, delay);
        notifyWatchers();
    }

    /**
     * Get the delay between market updates.
     * @return delay
     */
    private long getDelay() {
        return delay;
    }

    /**
     * Get the market update delay in seconds.
     * @return delay / 1000
     */
    public long getDelayInSeconds() {
        return getDelay() / 1000;
    }

    /**
     * Get the market containing all of the stock info.
     * @return m
     */
    public Market getMarket() {
        return m;
    }

    /**
     * Export the market refresh time to CSV to persist it.
     * @return String
     */
    public String exportString() {
        return String.format("\"Market Refresh Timer\",\"%s\"", this.getDelayInSeconds());
    }

    @Override
    public void run() {
        GuiController.getInstance().showNotification("Updating Market...", 5);
        try {
            t.Update();
        } catch (InterruptedException ignored) {
        } finally {
            UserController.getInstance().getAuthedUser().getPortfolio().updatePortfolio();
            WatchlistController.getInstance().checkWatchlist();
        }
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
