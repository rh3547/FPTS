package fpts.controller;

import fpts.model.*;
import fpts.toolkit.CSVParser;
import fpts.view.AddHoldingView;
import fpts.view.HoldingDetailView;
import fpts.view.PortfolioView;
import fpts.view.View;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Ryan on 3/8/2016.
 * <p>
 * Controls the holding models and provides information for the related views
 */
public class PortfolioController {

    private static PortfolioController instance = new PortfolioController();

    private Search search = new Search();
    private DecimalFormat df = new DecimalFormat("###,###,###,###.##");

    /**
     * Private constructor prevents instance from being created.
     */
    private PortfolioController() {
    }

    /**
     * Get the only instance of PortfolioController.
     *
     * @return -  PortfolioController
     */
    public static PortfolioController getInstance() {
        return instance;
    }

    /**
     * Gets the portfolio of a given user
     *
     * @param user
     */
    public void loadPortfolio(User user) {
        if (user.getPortfolio() == null) {
            File f = new File(String.format("src/lib/portfolios/%s.csv", user.getUserID()));
            try {
                if (!f.exists() && !f.isDirectory()) {
                    f.createNewFile();
                }
                List<List<String>> file = CSVParser.parseFile(String.format("src/lib/portfolios/%s.csv", user.getUserID()), true);
                user.setPortfolio(generatePortfolio(file, user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            user.setPortfolio(user.getPortfolio());
        }
    }

    /**
     * Loads the portfolio out of a csv and connects it to a user
     *
     * @param file - where the portfolio csv is located
     * @param user
     * @return
     */
    private Portfolio generatePortfolio(List<List<String>> file, User user) {
        Portfolio portfolio = new Portfolio(String.format("%s", user.getUserID()), LocalDate.now());
        Stack<Transaction> transactions = new Stack<>();
        for (List<String> line : file) {
            String type = line.get(0);
            String name;
            float value;
            LocalDate date;
            String ticker;
            float shares;
            String index;
            String sector;

            switch (type) {
                case "Equity":
                    name = line.get(1);
                    value = Float.parseFloat(line.get(2));
                    date = LocalDate.parse(line.get(3));
                    ticker = line.get(4);
                    index = line.get(5);
                    sector = line.get(6);
                    shares = Float.parseFloat(line.get(7));
                    portfolio.addChild(new Equity(name, value / shares, date, ticker, shares, index, sector));
                    break;
                case "Index Share":
                    name = line.get(1);
                    value = Float.parseFloat(line.get(3));
                    date = LocalDate.parse(line.get(4));
                    shares = Float.parseFloat(line.get(2));
                    portfolio.addChild(new IndexShare(name, value, date, shares));
                    break;
                case "Cash Account":
                    name = line.get(1);
                    value = Float.parseFloat(line.get(2));
                    date = LocalDate.parse(line.get(3));
                    portfolio.addChild(new CashAccount(name, value, date));
                    break;
                case "Transaction":
                    type = line.get(2);
                    String orig = line.get(3);
                    String dest = line.get(4);
                    value = Float.parseFloat(line.get(5));
                    LocalDateTime transactionDate = LocalDateTime.parse(line.get(1));
                    transactions.add(new Transaction(type, orig, dest, value, transactionDate));
                    break;
            }
        }
        portfolio.setTransactions(transactions);
        return portfolio;
    }

    /**
     * Show the user's portfolio view.
     */
    public void showPortfolioView() {

        User user = UserController.getInstance().getAuthedUser();

        if (user == null) {
            GuiController.getInstance().getCurrentView().showMessage("You're not logged in", View.ERROR_MESSAGE);
            return;
        }

        loadPortfolio(user);
        WatchlistController.getInstance().loadWatchlist(user);

        View view = new PortfolioView();

        Context context = new Context();
        context.addData("user", user);
        context.addData("holdings", getHoldingsList());

        view.initialize(context);
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Show the detail view for a specific Holding.
     *
     * @param holdingName - name of the holding
     */
    public void showHoldingDetailView(String holdingName) {
        View view = new HoldingDetailView();

        Context context = new Context();
        Holding holding = UserController.getInstance().getAuthedUser().getPortfolio().getEquity(holdingName);

        if (holding == null) {
            holding = UserController.getInstance().getAuthedUser().getPortfolio().getCashAccount(holdingName);
        }

        context.addData("holding", holding);

        if (holding instanceof CashAccount)
            context.addData("type", "CashAccount");
        else if (holding instanceof Equity)
            context.addData("type", "Equity");
        else if (holding instanceof IndexShare)
            context.addData("type", "IndexShare");

        view.initialize(context);
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Show the view used for adding new holdings to a portfolio.
     */
    public void showAddHoldingView() {
        View view = new AddHoldingView();

        view.initialize();
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Creates JPanel objects that represent holdings from a portfolio
     *
     * @return holdingsList - an list of JPanel objects representing holdings
     */
    private ArrayList<JPanel> getHoldingsList() {
        // get all holdings in portfolio
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        ArrayList<JPanel> holdingsList = new ArrayList<>();
        for (Holding h : portfolio.getChildren()) {
            holdingsList.add(h.makeListObject());
        }
        return holdingsList;
    }

    /**
     * Creates JPanel objects that represent holdings from a search result
     *
     * @return holdingsList - an list of JPanel objects representing holdings
     */
    public ArrayList<JPanel> searchHoldings(String search) {
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        ArrayList<JPanel> holdingsList = new ArrayList<>();
        List<Holding> list = this.search.portfolioSearch(search, portfolio);
        for (Holding h : list) {
            holdingsList.add(h.makeListObject());
        }
        return holdingsList;
    }

    /**
     * Gets a list of search results for a term
     *
     * @param search - the search term
     * @return - a list of strings
     */
    public LinkedList<String> searchMarket(String search) {
        LinkedList<String> list = this.search.marketSearch(search);
        return list;
    }

    /**
     * Gets a list of all cash accounts that the view can use and returns their name
     *
     * @return - an array of cash account names
     */
    public String[] getCashAccountsList() {
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        ArrayList<CashAccount> cashAccountsList = new ArrayList<>();

        for (Holding h : portfolio.getChildren()) {
            if (h instanceof CashAccount) {
                cashAccountsList.add((CashAccount) h);
            }
        }

        String[] cashAccounts = new String[cashAccountsList.size() + 1];
        cashAccounts[0] = "Outside System";
        for (int x = 0; x < cashAccountsList.size(); x++) {
            cashAccounts[x + 1] = cashAccountsList.get(x).getName();
        }

        return cashAccounts;
    }

     /**
     * Adds money to a cash account, creates a new one if the name doesn't exist
     *
     * @param name   - name of the cash account to be created or deposited into
     * @param amount - cash amount being added
     * @return  - true if new account was made, false if no new account, only deposit
     */
    public boolean addCashAccount(String name, String amount) {
        for (Holding h : UserController.getInstance().getAuthedUser().getPortfolio().getChildren()) {
            if (h.getName().equals(name)) {
                UserController.getInstance().getAuthedUser().getPortfolio().getCashAccount(name).updateValue(Float.valueOf(amount));
                UserController.getInstance().getAuthedUser().getPortfolio().updateValue(Float.valueOf(amount));

                GuiController.getInstance().getCurrentView().showMessage("Cash deposited into " + name + " successfully", View.SUCCESS_MESSAGE);
                // creates a transaction object
                Transaction newTransaction = new Transaction("Deposit Funds", name, name,
                        Float.parseFloat(amount), LocalDateTime.now());
                UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);
                return false;
            }
        }
        UserController.getInstance().getAuthedUser().getPortfolio().addChild(new CashAccount(name, Float.parseFloat(amount), LocalDate.now()));

        // creates a transaction object
        Transaction newTransaction = new Transaction("Cash Account Created", name, name,
                Float.parseFloat(amount), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);

        GuiController.getInstance().getCurrentView().showMessage("Cash account added successfully", View.SUCCESS_MESSAGE);
        return true;
    }

    /**
     * Adds a new equity to the portfolio
     *
     * @param equityName      - the tickerSymbol of an equity
     * @param numShares       - number of shares to be bought
     * @param cashAccountName - optional name of cash used to buy the equity
     */
    public void addEquity(String equityName, String numShares, String cashAccountName) {
        equityName = equityName.replace("Stock: ", "");
        // Get equity information
        List<String> marketInformation = MarketController.getInstance().m.getMarket().get(equityName);
        String name = null;
        float price = 0;
        String sector = null;
        String index = null;

        for (int i = 0; i < marketInformation.size(); i++) {
            String info = marketInformation.get(i);
            switch (i) {
                case 0:
                    break;
                case 1:
                    name = marketInformation.get(i);
                    break;
                case 2:
                    price = Float.parseFloat(marketInformation.get(i));
                    break;
                case 3:
                    if (info.equals("NASDAQ100") || info.equals("DOW")) {
                        if (index == null) {
                            index = marketInformation.get(i);
                        } else {
                            sector = marketInformation.get(i);
                        }
                    } else {
                        sector = marketInformation.get(i);
                    }
                    break;
                case 4:
                    if (info.equals("NASDAQ100") || info.equals("DOW")) {
                        if (index == null) {
                            index = marketInformation.get(i);
                        } else {
                            sector = marketInformation.get(i);
                        }
                    }
                    break;
            }
        }

        // if using a cash account to pay
        if (cashAccountName != null) {
            for (String c : getCashAccountsList()) {
                if (c.equals(cashAccountName)) {
                    // if not enough funds then display error message and quit, if enough funds update values
                    if (UserController.getInstance().getAuthedUser().getPortfolio().getCashAccount(c).getValue() < price * Float.parseFloat(numShares)) {
                        GuiController.getInstance().getCurrentView().showMessage("Insufficient Funds", View.ERROR_MESSAGE);
                        return;
                    } else {
                        UserController.getInstance().getAuthedUser().getPortfolio().updateValue(-(price * Float.parseFloat(numShares)));
                        UserController.getInstance().getAuthedUser().getPortfolio().getCashAccount(c).updateValue(-(price * Float.parseFloat(numShares)));
                    }
                }
            }
        }

        // creates new equity or adds more shares
        Equity e = UserController.getInstance().getAuthedUser().getPortfolio().getEquity(equityName);
        if (e == null) {
            e = new Equity(name,price, LocalDate.now(), equityName, Float.parseFloat(numShares), index, sector);
            UserController.getInstance().getAuthedUser().getPortfolio().addChild(e);
        } else {
            float shares = Float.parseFloat(numShares);
            e.updateEquity(shares, price, price * shares, LocalDate.now());
            UserController.getInstance().getAuthedUser().getPortfolio().updateValue(price * shares);
        }

        // Creates a transaction object
        Transaction newTransaction = new Transaction("Purchased Equities", equityName, UserController.getInstance().getAuthedUser().getPortfolio().getName(),
                price * Float.parseFloat(numShares), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);

        GuiController.getInstance().reloadView();

        GuiController.getInstance().getCurrentView().showMessage("Equities added successfully", View.SUCCESS_MESSAGE);
    }

    /**
     * Remove a set number of shares from the portfolio
     *
     * @param name        - ticker symbol of the stock being removed
     * @param amount      - number of shares being removed
     * @param cashAccount - optional cash account where proceeds will go
     */
    public void removeHolding(String name, String amount, String cashAccount) {
        name = name.replace("Stock: ", "");
        float shares = Float.parseFloat(amount);
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        Equity e = portfolio.getEquity(name);


        float stockPrice = e.getValue();

        if (shares >= e.getShares()) {
            if (cashAccount != null) { // if transfering profits to a cash account
                portfolio.getCashAccount(cashAccount).updateValue(e.getTotalValue());
                portfolio.updateValue(e.getTotalValue());
            }
            // if removing all shares delete the equity object and redirect to portfolio
            portfolio.removeChild(e);
            showPortfolioView();
        } else {
            e.updateEquity(-(shares),stockPrice, -(shares * stockPrice), LocalDate.now());
            if (cashAccount != null) {  // if transfering profits to a cash account
                portfolio.getCashAccount(cashAccount).updateValue(shares * stockPrice);
            } else {
                portfolio.updateValue(-(shares * stockPrice));
            }
        }

        // creates a transaction object
        String cashName = cashAccount;
        if (cashName == null)
            cashName = "Outside System";
        Transaction newTransaction = new Transaction("Sold Equities", name, cashName,
                Float.parseFloat(amount), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);

        GuiController.getInstance().reloadView();
    }

    /**
     * Withdraw money from a cash account out of the system
     *
     * @param name   - name of cash account
     * @param amount - amount withdrawn
     */
    public void withdraw(String name, String amount) {
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        Holding account = portfolio.getCashAccount(name);

        // update the value by subtracting the withdrawl amount
        account.updateValue(-(Float.parseFloat(amount)));
        portfolio.updateValue(-(Float.parseFloat(amount)));

        // Create a transaction object
        Transaction newTransaction = new Transaction("Withdraw funds", name, "Outside System",
                Float.parseFloat(amount), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);
    }

    /**
     * Deletes a existing cash account
     *
     * @param removeName - name of the cash account to be removed
     */
    public void removeCashAccount(String removeName) {
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        Holding account = portfolio.getCashAccount(removeName);

        // remove the cash account
        portfolio.removeChild(account);

        // create a transaction object
        Transaction newTransaction = new Transaction("Delete Account", removeName, "Outside System",
                account.getValue(), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);

        // redirect to portfolio
        showPortfolioView();
    }

    /**
     * transfers money from one cash account to another
     *
     * @param from   - name of cash account money is coming from
     * @param to     - name of cash account money goes to
     * @param amount - money being transferd
     */
    public void transfer(String from, String to, String amount) {
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        float transferAmount = Float.parseFloat(amount);
        Holding toAccount = portfolio.getCashAccount(to);
        Holding fromAccount = portfolio.getCashAccount(from);

        // update the account values
        toAccount.updateValue(transferAmount);
        fromAccount.updateValue(-transferAmount);

        // Create a transaction object
        Transaction newTransaction = new Transaction("Transfer", from, to,
                transferAmount, LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);
    }


    /**
     * Adds a new index share to the portfolio
     *
     * @param indexName       - name of the index or sector
     * @param numShares       - number of shares
     * @param cashAccountName - name of cash account supplying the purchase funds
     */
    public void addIndexShare(String indexName, String numShares, String cashAccountName) {
        indexName = indexName.replace("Chosen: ", "");

        float price = MarketController.getInstance().m.getSectorPrice(indexName);

        // if using a cash account to pay
        if (cashAccountName != null) {
            for (String c : getCashAccountsList()) {
                if (c.equals(cashAccountName)) {
                    // if not enough funds then display error message and quit, if enough funds update values
                    if (UserController.getInstance().getAuthedUser().getPortfolio().getCashAccount(c).getValue() < price * Integer.parseInt(numShares)) {
                        GuiController.getInstance().getCurrentView().showMessage("Insufficient Funds", View.ERROR_MESSAGE);
                        return;
                    } else {
                        UserController.getInstance().getAuthedUser().getPortfolio().updateValue(-(price * Integer.parseInt(numShares)));
                        UserController.getInstance().getAuthedUser().getPortfolio().getCashAccount(c).updateValue(-(price * Integer.parseInt(numShares)));
                    }
                }
            }
        }

        // creates new equity or adds more shares
        IndexShare i = UserController.getInstance().getAuthedUser().getPortfolio().getIndexShare(indexName);
        if (i == null) {
            IndexShare indexShare = new IndexShare(indexName, price * Float.parseFloat(numShares), LocalDate.now(), Float.parseFloat(numShares));
            UserController.getInstance().getAuthedUser().getPortfolio().addChild(indexShare);
        } else {
            float shares = Float.parseFloat(numShares);
            i.updateIndexShare(shares, price * shares, LocalDate.now());
            UserController.getInstance().getAuthedUser().getPortfolio().updateValue(price * shares);
        }

        // Creates a transaction object
        Transaction newTransaction = new Transaction("Purchased Index Share", indexName, UserController.getInstance().getAuthedUser().getPortfolio().getName(),
                price * Integer.parseInt(numShares), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);

        GuiController.getInstance().getCurrentView().showMessage("Index shares added successfully", View.SUCCESS_MESSAGE);
    }

    /**
     * Remove a set number of shares from the portfolio
     *
     * @param name        - ticker symbol of the stock being removed
     * @param amount      - number of shares being removed
     * @param cashAccount - optional cash account where proceeds will go
     */
    public void removeIndexShare(String name, String amount, String cashAccount) {
        float shares = Float.parseFloat(amount);
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        IndexShare indexShare = portfolio.getIndexShare(name);

        // calculate the price of 1 stock
        float stockPrice = indexShare.getValue() / indexShare.getShares();

        if (shares >= indexShare.getShares()) {
            portfolio.removeChild(indexShare);
            showPortfolioView();
        } else {
            indexShare.updateIndexShare(-(shares), -(shares * stockPrice), LocalDate.now());
        }

        if (cashAccount != null) {  // if transferring profits to a cash account
            portfolio.getCashAccount(cashAccount).updateValue(shares * stockPrice);
        } else {
            portfolio.updateValue(-(shares * stockPrice));
        }

        // creates a transaction object
        String cashName = cashAccount;
        if (cashName == null)
            cashName = "Outside System";
        Transaction newTransaction = new Transaction("Sold Index Share", name, cashName,
                Float.parseFloat(amount), LocalDateTime.now());
        UserController.getInstance().getAuthedUser().getPortfolio().addTransaction(newTransaction);
    }
}

