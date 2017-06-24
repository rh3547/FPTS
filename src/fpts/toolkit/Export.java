package fpts.toolkit;

import fpts.controller.MarketController;
import fpts.controller.PortfolioController;
import fpts.controller.UserController;
import fpts.model.*;
import fpts.model.Watchlist.Watchlist;
import fpts.model.Watchlist.WatchlistItem;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by George Herde on 3/12/16.
 */
public class Export implements SaveNotifier {
    private HashMap<Character, String> encryption = new HashMap<>();
    private HashMap<Character, String> decryption = new HashMap<>();

    public Export() {
        encryption.put("a".charAt(0), "p");
        encryption.put("b".charAt(0), "a");
        encryption.put("c".charAt(0), "c");
        encryption.put("d".charAt(0), "k");
        encryption.put("e".charAt(0), "m");
        encryption.put("f".charAt(0), "y");
        encryption.put("g".charAt(0), "b");
        encryption.put("h".charAt(0), "o");
        encryption.put("i".charAt(0), "x");
        encryption.put("j".charAt(0), "w");
        encryption.put("k".charAt(0), "i");
        encryption.put("l".charAt(0), "t");
        encryption.put("m".charAt(0), "h");
        encryption.put("n".charAt(0), "f");
        encryption.put("o".charAt(0), "v");
        encryption.put("p".charAt(0), "e");
        encryption.put("q".charAt(0), "d");
        encryption.put("r".charAt(0), "z");
        encryption.put("s".charAt(0), "g");
        encryption.put("t".charAt(0), "j");
        encryption.put("u".charAt(0), "l");
        encryption.put("v".charAt(0), "n");
        encryption.put("w".charAt(0), "q");
        encryption.put("x".charAt(0), "r");
        encryption.put("y".charAt(0), "s");
        encryption.put("z".charAt(0), "u");
        encryption.put("A".charAt(0), "P");
        encryption.put("B".charAt(0), "A");
        encryption.put("C".charAt(0), "C");
        encryption.put("D".charAt(0), "K");
        encryption.put("E".charAt(0), "M");
        encryption.put("F".charAt(0), "Y");
        encryption.put("G".charAt(0), "B");
        encryption.put("H".charAt(0), "O");
        encryption.put("I".charAt(0), "X");
        encryption.put("J".charAt(0), "W");
        encryption.put("K".charAt(0), "I");
        encryption.put("L".charAt(0), "T");
        encryption.put("M".charAt(0), "H");
        encryption.put("N".charAt(0), "F");
        encryption.put("O".charAt(0), "V");
        encryption.put("P".charAt(0), "E");
        encryption.put("Q".charAt(0), "D");
        encryption.put("R".charAt(0), "Z");
        encryption.put("S".charAt(0), "G");
        encryption.put("T".charAt(0), "J");
        encryption.put("U".charAt(0), "L");
        encryption.put("V".charAt(0), "N");
        encryption.put("W".charAt(0), "Q");
        encryption.put("X".charAt(0), "R");
        encryption.put("Y".charAt(0), "S");
        encryption.put("Z".charAt(0), "U");
        encryption.put("1".charAt(0), "!");
        encryption.put("3".charAt(0), "#");
        encryption.put("4".charAt(0), "$");
        encryption.put("5".charAt(0), "%");
        encryption.put("6".charAt(0), "^");
        encryption.put("7".charAt(0), "&");
        encryption.put("8".charAt(0), "*");
        encryption.put("9".charAt(0), "(");
        encryption.put("2".charAt(0), "@");
        encryption.put("0".charAt(0), ")");
        decryption.put("*".charAt(0), "8");
        decryption.put("(".charAt(0), "9");
        decryption.put(")".charAt(0), "0");
        decryption.put("u".charAt(0), "z");
        decryption.put("A".charAt(0), "B");
        decryption.put("p".charAt(0), "a");
        decryption.put("a".charAt(0), "b");
        decryption.put("c".charAt(0), "c");
        decryption.put("k".charAt(0), "d");
        decryption.put("m".charAt(0), "e");
        decryption.put("y".charAt(0), "f");
        decryption.put("b".charAt(0), "g");
        decryption.put("o".charAt(0), "h");
        decryption.put("x".charAt(0), "i");
        decryption.put("w".charAt(0), "j");
        decryption.put("i".charAt(0), "k");
        decryption.put("t".charAt(0), "l");
        decryption.put("h".charAt(0), "m");
        decryption.put("f".charAt(0), "n");
        decryption.put("v".charAt(0), "o");
        decryption.put("e".charAt(0), "p");
        decryption.put("d".charAt(0), "q");
        decryption.put("z".charAt(0), "r");
        decryption.put("g".charAt(0), "s");
        decryption.put("j".charAt(0), "t");
        decryption.put("l".charAt(0), "u");
        decryption.put("n".charAt(0), "v");
        decryption.put("q".charAt(0), "w");
        decryption.put("r".charAt(0), "x");
        decryption.put("s".charAt(0), "y");
        decryption.put("P".charAt(0), "A");
        decryption.put("C".charAt(0), "C");
        decryption.put("K".charAt(0), "D");
        decryption.put("M".charAt(0), "E");
        decryption.put("Y".charAt(0), "F");
        decryption.put("B".charAt(0), "G");
        decryption.put("O".charAt(0), "H");
        decryption.put("X".charAt(0), "I");
        decryption.put("W".charAt(0), "J");
        decryption.put("I".charAt(0), "K");
        decryption.put("T".charAt(0), "L");
        decryption.put("H".charAt(0), "M");
        decryption.put("F".charAt(0), "N");
        decryption.put("V".charAt(0), "O");
        decryption.put("E".charAt(0), "P");
        decryption.put("D".charAt(0), "Q");
        decryption.put("Z".charAt(0), "R");
        decryption.put("G".charAt(0), "S");
        decryption.put("J".charAt(0), "T");
        decryption.put("L".charAt(0), "U");
        decryption.put("N".charAt(0), "V");
        decryption.put("Q".charAt(0), "W");
        decryption.put("R".charAt(0), "X");
        decryption.put("S".charAt(0), "Y");
        decryption.put("U".charAt(0), "Z");
        decryption.put("!".charAt(0), "1");
        decryption.put("@".charAt(0), "2");
        decryption.put("#".charAt(0), "3");
        decryption.put("$".charAt(0), "4");
        decryption.put("%".charAt(0), "5");
        decryption.put("^".charAt(0), "6");
        decryption.put("&".charAt(0), "7");
    }

    private String generateContent() {
        String content = "";
        User user = UserController.getInstance().getAuthedUser();
        Portfolio portfolio = user.getPortfolio();
        Watchlist watchlist = user.getWatchlist();
        if (portfolio != null) {
            for (Holding obj : portfolio.getChildren()) {
                content = String.format("%s%s\n", content, obj.exportString());
            }
            for (Transaction obj : portfolio.getTransactions()) {
                content = String.format("%s%s\n", content, obj.exportString());
            }
            if (watchlist != null) {
                for (WatchlistItem obj : watchlist.getWatchlist()) {
                    content = String.format("%s%s\n", content, obj.exportString());
                }
            }
            content = String.format("%s%s\n", content, MarketController.getInstance().exportString());
        }
        return content;
    }

    /**
     * Exports data in the form of a csv
     *
     * @param path - absolute path to the file to be imported
     */
    public void export(String path) {
        File file = new File(String.format("%s", path));
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    System.out.print("User portfolio file missing, was recreated");
            }
            PrintWriter out = new PrintWriter(file);
            out.append(generateContent());
            out.close();
        } catch (IOException e) {
            System.out.println("Export error");
        }
    }

    /**
     * @param path absolute path to the file to be imported
     */
    public void importPortfolio(String path) {
        User user = UserController.getInstance().getAuthedUser();
        Portfolio portfolio = user.getPortfolio();
        List<List<String>> file = CSVParser.parseFile(String.format("%s", path), false);
        List<Transaction> transactions = new ArrayList<>();
        List<WatchlistItem> watchlist = new ArrayList<>();
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
                    Float totalValue = Float.parseFloat(line.get(2));
                    date = LocalDate.parse(line.get(3));
                    ticker = line.get(4);
                    index = line.get(5);
                    sector = line.get(6);
                    shares = Float.parseFloat(line.get(7));
                    Equity portfolioEquity = portfolio.getEquity(ticker);
                    if (portfolioEquity != null) {
                        portfolioEquity.updateEquity(shares, (totalValue / shares), totalValue, date);
                    } else {
                        portfolio.addChild(new Equity(name, (totalValue / shares), date, ticker, shares, index, sector));
                    }
                    break;

                case "Index Share":
                    name = line.get(1);
                    shares = Float.parseFloat(line.get(2));
                    value = Float.parseFloat(line.get(3));
                    date = LocalDate.parse(line.get(4));
                    IndexShare portfolioIndexShare = portfolio.getIndexShare(name);
                    if (portfolioIndexShare != null) {
                        portfolioIndexShare.updateIndexShare(shares, value, date);
                    } else {
                        portfolio.addChild(new IndexShare(name, value, date, shares));
                    }
                    break;

                case "Cash Account":
                    name = line.get(1);
                    value = Float.parseFloat(line.get(2));
                    date = LocalDate.parse(line.get(3));
                    Holding portfolioCashAccount = portfolio.getCashAccount(name);
                    if (portfolioCashAccount != null) {
                        String[] options = {"Add", "Replace", "Ignore"};
                        String message = "Cash account " + name + " already exists." + "\n" +
                                "How would you like to handle the funds?";
                        int choice = JOptionPane.showOptionDialog(null, message, "Cash Account Conflict", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        switch (choice) {
                            case 0:
                                portfolioCashAccount.updateValue(value);
                                break;

                            case 1:
                                portfolioCashAccount.setValue(value);
                                break;
                        }
                    } else {
                        portfolio.addChild(new CashAccount(name, value, date));
                    }
                    break;

                case "Transaction":
                    type = line.get(2);
                    String orig = line.get(3);
                    String dest = line.get(4);
                    value = Float.parseFloat(line.get(5));
                    LocalDateTime transactionDate = LocalDateTime.parse(line.get(1));
                    transactions.add(new Transaction(type, orig, dest, value, transactionDate));
                    break;

                case "Watch List Item":
                    ticker = line.get(1);
                    float highTrigger = Float.parseFloat(line.get(2));
                    boolean highTriggerActive = Boolean.parseBoolean(line.get(3));
                    boolean highTriggerActiveHistory = Boolean.parseBoolean(line.get(4));
                    float lowTrigger = Float.parseFloat(line.get(5));
                    boolean lowTriggerActive = Boolean.parseBoolean(line.get(5));
                    boolean lowTriggerActiveHistory = Boolean.parseBoolean(line.get(6));
                    watchlist.add(new WatchlistItem(ticker,
                            highTrigger, lowTrigger, lowTriggerActive, lowTriggerActiveHistory,
                            highTriggerActive, highTriggerActiveHistory));
                    break;

                case "Market Refresh Timer":
                    MarketController.getInstance().setDelay(Long.parseLong(line.get(1)));
                    break;

            }
        }
        Watchlist watch = new Watchlist();
        watch.setWatchlist(watchlist);
        user.setWatchlist(watch);
        transactions.forEach(portfolio::addTransaction);
        PortfolioController.getInstance().showPortfolioView();
    }


    /**
     * Encrypts a string
     *
     * @param string - The string to be encrypted
     * @return - an encrypted string
     */
    public static String encryptString(String string) {
        Export e = new Export();
        String encrypt = "";
        for (int i = 0; i < string.length(); i++) {
            if (e.encryption.containsKey(string.charAt(i))) {
                encrypt = encrypt + e.encryption.get(string.charAt(i));
            } else {
                encrypt = encrypt + string.charAt(i);
            }
        }
        return encrypt;
//        return string;
    }

    /**
     * decrypts a string
     *
     * @param string - The string to be decrypted
     * @return - an decrypted string
     */
    public static String decryptString(String string) {
        Export e = new Export();
        String decrypt = "";
        for (int i = 0; i < string.length(); i++) {
            if (e.decryption.containsKey(string.charAt(i))) {
                decrypt = decrypt + e.decryption.get(string.charAt(i));
            } else {
                decrypt = decrypt + string.charAt(i);
            }

        }
        return decrypt;
//        return string;
    }

    @Override
    public void update() {
        User user = UserController.getInstance().getAuthedUser();
        Portfolio portfolio = user.getPortfolio();
        if (portfolio != null) {
            String name = user.getUserID();
            File file = new File(String.format("src/lib/portfolios/%s.csv", name));
            try {
                if (!file.exists()) {
                    if (file.createNewFile())
                        System.out.print("User portfolio file missing, was recreated");
                }
                PrintWriter out = new PrintWriter(file);
                out.append(encryptString(generateContent()));
                out.close();
            } catch (IOException e) {
                System.out.println("Save error");
            }
        }
    }
}
