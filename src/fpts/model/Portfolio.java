package fpts.model;

import fpts.toolkit.Export;
import fpts.toolkit.SaveNotifier;
import fpts.toolkit.UpdateVisitor;

import java.time.LocalDate;
import java.util.List;
import java.util.Stack;

/**
 * Created by George Herde on 3/2/16.
 * <p>
 * A portfolio that a user owns one of. It has many holdings and a history of transactions
 */
public class Portfolio extends Holding {
    private Stack<Transaction> transactions = new Stack<>();

    public Portfolio(String name, LocalDate date) {
        super(name, 0, date);
        registerWatcher(new Export());
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Get the value of this entire portfolio.
     *
     * @return float
     */
    public float getValue() {
        float total = 0;
        for (Holding h : this.getChildren()) {
            if (h instanceof Equity) {
                Equity e = (Equity) h;
                total += e.getTotalValue();
            } else {
                total += h.getValue();
            }
        }
        return total;
    }

    /**
     * Adds a new transaction to the record of all transactions
     *
     * @param transaction - new transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        notifyWatchers();
    }

    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\",\"%s\"",
                this.getDate(), this.getName(), this.getValue());
    }

    /**
     * Creates a csv line that holds a Portfolio
     *
     * @return - string representing a csv line
     */
    public String exportString() {
        return String.format("\"Portfolio\",\"%s\",\"%s\",\"%s\"",
                this.getDate(), this.getName(), this.getValue());
    }

    /**
     * Populates the portfolio with a history of transactions pulled from csv
     *
     * @param transactions - list of transactions
     */
    public void setTransactions(Stack<Transaction> transactions) {
        this.transactions = transactions;
        notifyWatchers();
    }


    /**
     * Removes the two most recent transactions
     * Used when undoing a transaction
     */
    public void removeTransaction() {
        transactions.pop();
        transactions.pop();
        notifyWatchers();
    }

    /**
     * Uses a portfolio iterator and visitor to update the market prices of all stocks
     * in the portfolio
     */
    public void updatePortfolio() {
        PortfolioIterator iterator = this.createIterator();
        UpdateVisitor visitor = new UpdateVisitor();
        for (iterator.first(); !iterator.isDone(); iterator.next()) {
            Holding h = iterator.getCurrChild();
            if (h instanceof Equity) {
                visitor.visit((Equity)h);
            } else if (h instanceof IndexShare) {
                visitor.visit((IndexShare)h);
            } else if (h instanceof CashAccount) {
                visitor.visit((CashAccount) h);
            }
        }
        notifyWatchers();
    }

    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        super.registerWatcher(saveNotifier);
    }

    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        super.removeWatcher(saveNotifier);
    }

    @Override
    public void notifyWatchers() {
        super.notifyWatchers();
    }
}
