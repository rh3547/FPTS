package fpts.model.UndoRedo;

import fpts.controller.PortfolioController;
import fpts.controller.UserController;

/**
 * Created by alexandergarrity on 3/30/16.
 *
 * The command to interact with cash accounts:
 * Add, remove, deposit, withdraw.
 */
public class UndoRedoCashAccount implements UndoRedo {
    String account;
    String value;
    String action; //options: "deposit" "withdraw" "remove"
    boolean newAccount; //true if new account was made, false if no new account, only deposit

    public UndoRedoCashAccount(String account, String value, String action){
        this.account = account;
        this.value = value;
        this.action = action;
    }

    public void execute() {
        if(action.equals("deposit")){
            this.newAccount = PortfolioController.getInstance().addCashAccount(account,value);
        }
        else if (action.equals("withdraw")){
            PortfolioController.getInstance().withdraw(account,value);
        }
        else if (action.equals("remove")){
            PortfolioController.getInstance().removeCashAccount(account);
        }
    }

    public void deexecuteify() {
        if(action.equals("deposit")){
            if (newAccount) {
                PortfolioController.getInstance().removeCashAccount(account);
            }
            else{
                PortfolioController.getInstance().withdraw(account, value);
            }
        }
        else if (action.equals("withdraw")){
            PortfolioController.getInstance().addCashAccount(account, value);
        }
        else if (action.equals("remove")){
            PortfolioController.getInstance().addCashAccount(account, value);
        }
        UserController.getInstance().getAuthedUser().getPortfolio().removeTransaction();
    }
}
