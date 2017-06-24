package fpts.model.UndoRedo;

import fpts.controller.PortfolioController;
import fpts.controller.UserController;

/**
 * Created by alexandergarrity on 4/6/16.
 *
 * The command to perform actions related to index shares like adding and removing.
 */
public class UndoRedoIndexShare implements UndoRedo{
    String index;
    String shares;
    String cashAccount;
    boolean buy; //if true buy more shares if false sell shares


    public UndoRedoIndexShare(String index, String shares, String cashAccount, boolean buy){
        this.index = index;
        this.shares = shares;
        this.cashAccount = cashAccount;
        this.buy = buy;
    }

    public void execute(){
        if(buy){
            PortfolioController.getInstance().addIndexShare(index,shares,cashAccount);
        }
        else{
            PortfolioController.getInstance().removeIndexShare(index, shares, cashAccount);
        }

    }

    public void  deexecuteify(){
        if(buy){
            PortfolioController.getInstance().removeIndexShare(index, shares, cashAccount);
        }
        else{
            PortfolioController.getInstance().addIndexShare(index, shares, cashAccount);
        }
        UserController.getInstance().getAuthedUser().getPortfolio().removeTransaction();
    }
}
