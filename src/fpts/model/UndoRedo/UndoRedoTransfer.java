package fpts.model.UndoRedo;

import fpts.controller.PortfolioController;
import fpts.controller.UserController;

/**
 * Created by alexandergarrity on 3/30/16.
 *
 * The command to perform transfer actions like transferring funds between cash accounts.
 */
public class UndoRedoTransfer implements UndoRedo {
    String from;
    String to;
    String amount;

    public UndoRedoTransfer(String from, String to, String amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public void execute() {
        PortfolioController.getInstance().transfer(from,to,amount);
    }


    public void deexecuteify() {
        PortfolioController.getInstance().transfer(to,from,amount);
        UserController.getInstance().getAuthedUser().getPortfolio().removeTransaction();
    }

}
