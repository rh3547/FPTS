package fpts.controller;

import fpts.model.User;
import fpts.model.Watchlist.Watchlist;
import fpts.model.Watchlist.WatchlistItem;
import fpts.toolkit.CSVParser;
import fpts.view.AddWatchlistView;
import fpts.view.View;
import fpts.view.WatchlistView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by rhochmuth on 4/6/2016.
 * <p>
 * Handles the view and functionality for a user's Watchlist.
 */
public class WatchlistController {

    private static WatchlistController instance = new WatchlistController();

    private Watchlist watchlist = UserController.getInstance().getAuthedUser().getWatchlist();

    /**
     * Private constructor prevents instance from being created.
     */
    private WatchlistController() {
    }

    /**
     * Get the only instance of WatchlistController.
     *
     * @return -  WatchlistController
     */
    public static WatchlistController getInstance() {
        return instance;
    }

    /**
     * Show the Watchlist view that displays the contents of the user's Watchlist.
     */
    public void showWatchlistView() {
        watchlist = UserController.getInstance().getAuthedUser().getWatchlist();

        View view = new WatchlistView();

        Context context = new Context();
        context.addData("Watchlist", watchlist);

        view.initialize(context);
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Show the add Watchlist view that displays a form to add a new item to the user's Watchlist.
     */
    public void showAddWatchlistView() {
        watchlist = UserController.getInstance().getAuthedUser().getWatchlist();

        View view = new AddWatchlistView();

        view.initialize();
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Get the user's Watchlist.
     *
     * @return
     */
    public Watchlist getWatchlist() {
        return watchlist;
    }

    /**
     * Add a new Watchlist item to the user's Watchlist.
     *
     * @param ticker
     * @param lowTrigger
     * @param highTrigger
     */
    public void addWatchlistItem(String ticker, String lowTrigger, String highTrigger) {
        if (watchlist != null) {
            new WatchlistItem(Float.parseFloat(lowTrigger),
                    Float.parseFloat(highTrigger), ticker);
            GuiController.getInstance().showNotification(ticker + " added to Watchlist!", 5);
        }
    }

    /**
     * Remove the given Watchlist item from the user's Watchlist.
     *
     * @param item
     */
    public void removeWatchlistItem(WatchlistItem item) {
        item.setLowTriggerActiveHistory(false);
        watchlist.removeWatchItem(item);
        GuiController.getInstance().reloadView();
    }

    /**
     * Check the user's watchlist against the stock prices to
     * see if anything has triggered.
     */
    public void checkWatchlist() {
        User user = UserController.getInstance().getAuthedUser();
        Watchlist watchlist = user.getWatchlist();
        boolean itemChanged = false;

        for (WatchlistItem item : watchlist.getWatchlist()) {
            float price = MarketController.getInstance().getMarket().getStockPrice(item.getTicker());

            if (price <= item.getLowTrigger()) {
                item.setLowTriggerActive(true);
                itemChanged = true;
            } else {
                item.setLowTriggerActive(false);
                item.setLowTriggerActiveHistory(false);
            }

            if (price >= item.getHighTrigger()) {
                item.setHighTriggerActive(true);
                itemChanged = true;
            } else {
                item.setHighTriggerActive(false);
                item.setHighTriggerActiveHistory(false);
            }
        }

        if (itemChanged) {
            GuiController.getInstance().showNotification("Items in your watchlist have triggered", 7);

            if (GuiController.getInstance().getCurrentView() instanceof WatchlistView)
                showWatchlistView();
        } else
            GuiController.getInstance().showNotification("Market Updated!", 5);
    }

    /**
     * Load the user's watchlist from CSV.
     * @param user
     */
    public void loadWatchlist(User user) {
        if (user.getWatchlist() == null) {
            File f = new File(String.format("src/lib/portfolios/%s.csv", user.getUserID()));
            try {
                if (!f.exists() && !f.isDirectory()) {
                    f.createNewFile();
                }
                List<List<String>> file = CSVParser.parseFile(String.format("src/lib/portfolios/%s.csv", user.getUserID()), true);
                user.setWatchlist(generateWatchlist(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            user.setWatchlist(user.getWatchlist());
        }
    }

    /**
     * Generate the watchlist object structure.
     * @param file
     * @return
     */
    private Watchlist generateWatchlist(List<List<String>> file) {
        Watchlist watchlist = new Watchlist();
        for (List<String> line : file) {
            String type = line.get(0);
            if (type.equals("Watch List Item")) {
                String ticker = line.get(1);
                float highTrig = Float.parseFloat(line.get(2));
                float lowTrig = Float.parseFloat(line.get(5));
                boolean highTrigA = Boolean.parseBoolean(line.get(3));
                boolean highTrigAHist = Boolean.parseBoolean(line.get(4));
                boolean lowTrigA = Boolean.parseBoolean(line.get(6));
                boolean lowTrigAHist = Boolean.parseBoolean(line.get(7));
                watchlist.addWatchItem(
                        new WatchlistItem(ticker, highTrig, lowTrig,
                                lowTrigA, lowTrigAHist, highTrigA,
                                highTrigAHist));
            } else if (type.equals("Market Refresh Timer")) {
                MarketController.getInstance().setDelay(Long.parseLong(line.get(1)));
            }
        }
        return watchlist;
    }
}
