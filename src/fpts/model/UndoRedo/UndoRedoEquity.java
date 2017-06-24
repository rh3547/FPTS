package fpts.model.UndoRedo;

import fpts.controller.PortfolioController;
import fpts.controller.UserController;

/**
 * Created by Alexander Garrity on 3/30/16.
 *
 * The command to perform equity actions likeadding or removing shares.
 */
public class UndoRedoEquity implements UndoRedo {
    private String tickerSymbol;
    private String shares;
    private String cashAccount;
    private boolean buy; //if true its a buy, if false its a sell

    public UndoRedoEquity(String tickerSymbol, String shares, String cashAccount, boolean buy){
        this.tickerSymbol = tickerSymbol;
        this.shares = shares;
        this.cashAccount = cashAccount;
        this.buy = buy;
    }

    public void execute() {
        if (buy){
            PortfolioController.getInstance().addEquity(tickerSymbol, shares, cashAccount);
        }
        else{
            PortfolioController.getInstance().removeHolding(tickerSymbol, shares, cashAccount);
        }

    }


    public void deexecuteify() {
        if (buy){
            PortfolioController.getInstance().removeHolding(tickerSymbol, shares, cashAccount);
        }
        else{
            PortfolioController.getInstance().addEquity(tickerSymbol, shares, cashAccount);
        }
        UserController.getInstance().getAuthedUser().getPortfolio().removeTransaction();
    }
}
