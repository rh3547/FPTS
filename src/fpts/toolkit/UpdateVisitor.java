package fpts.toolkit;

import fpts.controller.MarketController;
import fpts.model.CashAccount;
import fpts.model.Equity;
import fpts.model.IndexShare;
import fpts.model.Market;

/**
 * Implementation of PortfolioVisitor that provides functionality for holdings that updates their pricing
 * when the market values change.
 */
public class UpdateVisitor implements PortfolioVisitor {

    /**
     * Updates the value of an equity to reflect the market
     * @param e An equity
     */
    public void visit(Equity e) {
        Market market = MarketController.getInstance().getMarket();
        float price = market.getStockPrice(e.getName());
        e.updateEquity(0, price, (e.getShares() * price) - e.getTotalValue(), e.getDate());
    }

    /**
     * Updates the value of an Indexshare to reflect the market
     * @param e An indexshare
     */
    public void visit(IndexShare e) {
        Market market = MarketController.getInstance().getMarket();
        float sectorPrice = market.getSectorPrice(e.getName());
        float newValue = e.getShares() * sectorPrice;
        e.updateIndexShare(0, (newValue - e.getValue()), e.getDate());
    }

    /**
     * Updates the value of a cash account to reflect the market (does not change)
     * @param e A cash account
     */
    public void visit(CashAccount e) {
        return;
    }
}
