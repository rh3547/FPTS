package fpts.toolkit;

import fpts.model.CashAccount;
import fpts.model.Equity;
import fpts.model.IndexShare;

/**
 *  Interface that represents a command that can be done on many
 *  different types of holdings.
 */
public interface PortfolioVisitor {
    void visit(Equity e);
    void visit(IndexShare e);
    void visit(CashAccount e);
}
