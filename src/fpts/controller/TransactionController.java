package fpts.controller;

import fpts.model.Portfolio;
import fpts.model.Transaction;
import fpts.view.TransactionHistoryView;
import fpts.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ryan on 3/11/2016.
 *
 *  Controls the transaction models and provides information for the related views
 */
public class TransactionController {

    private static TransactionController instance = new TransactionController();

    /**
     * Private constructor prevents instance from being created.
     */
    private TransactionController() {}

    /**
     * Get the only instance of TransactionController.
     * @return - TransactionController
     */
    public static TransactionController getInstance() {
        return instance;
    }

    /**
     * Show the view of the transaction history for a portfolio.
     */
    public void showTransactionHistoryView() {
        View view = new TransactionHistoryView();

        Context context = new Context();
        context.addData("transactions", getTransactionsList());

        view.initialize(context);
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Gets a list of transactions to display
     *
     * @return - list of JPanel objects representing transactions
     */
    public ArrayList<JPanel> getTransactionsList() {
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        ArrayList<JPanel> transactionsList = new ArrayList<>();
        for(Transaction t : portfolio.getTransactions()) {
            transactionsList.add(t.makeListObject());
        }
        Collections.reverse(transactionsList);
        return transactionsList;
    }
}
