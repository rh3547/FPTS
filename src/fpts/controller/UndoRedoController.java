package fpts.controller;

import fpts.model.UndoRedo.*;

import java.util.Stack;


/**
 * Created by Ryan on 4/3/2016.
 *
 * Controls the undo and redo functionality by handling events from the front-end.
 */
public class UndoRedoController {
    private static UndoRedoController instance = new UndoRedoController();
    private UndoRedoHolder recentCommands = new UndoRedoHolder();

    /**
     * Private constructor prevents instance from being created.
     */
    private UndoRedoController() {
    }

    /**
     * Get the only instance of UndoRedoController.
     *
     * @return -  UndoRedoController
     */
    public static UndoRedoController getInstance() {
        return instance;
    }

    /**
     * Undo the last command performed.  The undo queue can contain a max
     * of five items, so if no further commands can be undone this function
     * will return false.  Returns true if successful.
     * @return boolean
     */
    public boolean undo() {
        if (recentCommands.undoStack.isEmpty()){
            return false;
        }
        else{
            UndoRedo command = recentCommands.undoStack.pop();
            command.deexecuteify();
            recentCommands.redoStack.add(command);
            return true;
        }

    }

    /**
     * Redo the last command undone.  The redo queue can contain a max
     * of five items, so if no further commands can be redone this function
     * will return false.  Returns true if successful.
     * @return boolean
     */
    public boolean redo() {
        if (recentCommands.redoStack.isEmpty()){
            return false;
        }
        else{
            UndoRedo command = recentCommands.redoStack.pop();
            command.execute();
            recentCommands.undoStack.add(command);
            return true;
        }
    }

    /**
     * For buying more of an equity or creating a new one
     * @param tickerSymbol
     * @param shares
     * @param cashAccount
     */
    public void BuyEquity(String tickerSymbol, String shares, String cashAccount){
        UndoRedoEquity buy = new UndoRedoEquity(tickerSymbol, shares, cashAccount, true);
        buy.execute();
        AddToUndo(buy);

    }

    /**
     * For selling shares of an equity.  Deletes if shares hit 0
     * @param tickerSymbol
     * @param shares
     * @param cashAccount
     */
    public void SellEquity(String tickerSymbol, String shares, String cashAccount){
        UndoRedoEquity sell = new UndoRedoEquity(tickerSymbol, shares, cashAccount, false);
        sell.execute();
        AddToUndo(sell);
    }

    /**
     * Deposit to a CC or Create a new one if the name doesnt exist
     * @param account
     * @param value
     */
    public void DepositCashAccount(String account, String value){
        UndoRedoCashAccount deposit = new UndoRedoCashAccount(account, value, "deposit");
        deposit.execute();
        AddToUndo(deposit);
    }

    /**
     * withdraw from Cash account specified
     * @param account
     * @param value
     */
    public void WithdrawCashAccount(String account, String value){
        UndoRedoCashAccount withdraw = new UndoRedoCashAccount(account, value, "withdraw");
        withdraw.execute();
        AddToUndo(withdraw);
    }

    /**
     * Remove a CC from the system
     * @param account
     * @param value
     */
    public void RemoveCashAccount(String account, String value){
        UndoRedoCashAccount remove = new UndoRedoCashAccount(account, value, "remove");
        remove.execute();
        AddToUndo(remove);
    }

    /**
     * Buy more or create new index share
     * @param index
     * @param shares
     * @param cashAccount
     */
    public void BuyIndexShare(String index, String shares, String cashAccount){
        UndoRedoIndexShare buy = new UndoRedoIndexShare(index, shares, cashAccount, true);
        buy.execute();
        AddToUndo(buy);
    }

    /**
     * Sell shares of or remove and index share
     * @param index
     * @param shares
     * @param cashAccount
     */
    public void SellIndexShare(String index, String shares, String cashAccount){
        UndoRedoIndexShare sell = new UndoRedoIndexShare(index, shares, cashAccount, false);
        sell.execute();
        AddToUndo(sell);
    }

    /**
     * Transfer money from the "from" account to the "to" account
     * @param from
     * @param to
     * @param amount
     */
    public void Transfer(String from, String to, String amount){
        UndoRedoTransfer transfer = new UndoRedoTransfer(from, to, amount);
        transfer.execute();
        AddToUndo(transfer);
    }

    public void AddToUndo(UndoRedo command){
        Stack<UndoRedo> undoStack = recentCommands.undoStack;
        Stack<UndoRedo> redoStack = recentCommands.redoStack;

        if (undoStack.size() < 5){
            undoStack.add(command);
            redoStack.clear();
        }
        else {
            undoStack.removeElementAt(undoStack.size()-1);
            undoStack.add(command);
            redoStack.clear();
        }

    }
}
