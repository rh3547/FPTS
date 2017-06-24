package fpts.model;

import fpts.toolkit.GuiComponentFactory;
import fpts.toolkit.SaveNotifier;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by George Herde on 3/2/16.
 * <p>
 * Equity representing a number of shares in a single stock
 */
public class Equity extends Holding {
    private String tickerSymbol;
    private String index;
    private String sector;
    private float shares;
    private float totalValue;

    /**
     * @param name         name of the equity
     * @param value        price of the equity
     * @param date         date of last change for this equity
     * @param tickerSymbol market ticker symbol string
     * @param shares       number of shares of this equity
     * @param index        index this equity belongs to
     * @param sector       sector this equity belongs to
     */
    public Equity(String name, float value, LocalDate date, String tickerSymbol,
                  float shares, String index, String sector) {
        super(name, value, date);
        this.tickerSymbol = tickerSymbol;
        this.shares = shares;
        this.totalValue = shares * value;

        // index is null if given "" or null
        if (Objects.equals(index, "")) {
            this.index = null;
        } else {
            this.index = index;
        }

        // sector is null if given "" or null
        if (Objects.equals(sector, "")) {
            this.sector = null;
        } else {
            this.sector = sector;
        }
    }

    @Override
    public void updateValue(float value) {
        this.totalValue += value;
        notifyWatchers();
    }

    /**
     * Updates the number of shares, value and purchase date of an equity
     *
     * @param shares     - number of shares added
     * @param price      - price of a share
     * @param totalValue - value of the shares being added
     * @param date       - date of latest purchase
     */
    public void updateEquity(float shares, float price, float totalValue, LocalDate date) {
        this.shares += shares;
        this.setValue(price);
        this.totalValue += totalValue;
        this.setDate(date);
        notifyWatchers();
    }

    /**
     * @return number of shares of this equity
     */
    public float getShares() {
        return shares;
    }

    /**
     * @return string of market index (can be null)
     */
    public String getIndex() {
        return index;
    }

    /**
     * @return string of market sector (can be null)
     */
    public String getSector() {
        return sector;
    }

    /**
     * @return string of market ticker symbol
     */
    public String getTickerSymbol() {
        return tickerSymbol;
    }

    /**
     * @return value of stocks
     */
    public float getTotalValue() {
        return totalValue;
    }

    /**
     * Make a JPanel that's structured to represent this Equity.
     *
     * @return GUI object ready to be displayed
     */
    @Override
    public JPanel makeListObject() {
        DecimalFormat df = new DecimalFormat("###,###,###,###.##");

        String str1 = this.tickerSymbol;
        String str2 = Float.toString(this.getShares());
        String str3 = df.format(this.getValue());
        String str4 = df.format(this.getTotalValue());

        JPanel panel = new JPanel();
        panel.setSize(750, 40);
        panel.setBackground(new Color(219, 219, 219));
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(190, 190, 190)));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel nameLbl;
        nameLbl = GuiComponentFactory.getInstance().createBasicLabel();
        nameLbl.setText(str1);
        nameLbl.setForeground(new Color(49, 49, 49));
        nameLbl.setSize(160, 30);
        nameLbl.setLocation(10, 5);
        panel.add(nameLbl);

        JLabel numSharesLbl;
        numSharesLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str2 != null)
            numSharesLbl.setText(str2);
        else
            numSharesLbl.setText("-");
        numSharesLbl.setForeground(new Color(142, 141, 141));
        numSharesLbl.setSize(150, 30);
        numSharesLbl.setLocation(205, 5);
        panel.add(numSharesLbl);

        JLabel pricePerLbl;
        pricePerLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str3 != null)
            pricePerLbl.setText("$ " + str3);
        else
            pricePerLbl.setText("-");
        pricePerLbl.setForeground(new Color(142, 141, 141));
        pricePerLbl.setSize(200, 30);
        pricePerLbl.setLocation(365, 5);
        panel.add(pricePerLbl);

        JLabel totalHoldingLbl;
        totalHoldingLbl = GuiComponentFactory.getInstance().createBasicLabel();
        totalHoldingLbl.setText("$ " + str4);
        totalHoldingLbl.setForeground(new Color(76, 175, 80));
        totalHoldingLbl.setSize(200, 30);
        totalHoldingLbl.setLocation(568, 5);
        panel.add(totalHoldingLbl);

        return panel;
    }


    /**
     * @param saveNotifier add an saveNotifier to the object to be notified
     */
    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        super.registerWatcher(saveNotifier);
    }

    /**
     * @param saveNotifier remove an saveNotifier so that it is no longer notified
     */
    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        super.removeWatcher(saveNotifier);
    }

    /**
     * notify the observers of this instance that a change has been made
     */
    @Override
    public void notifyWatchers() {
//        System.out.println("Notifying save that portfolio has been modified");
        super.notifyWatchers();
    }

    /**
     * @return formatted string for the object
     */
    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                this.getTickerSymbol(), this.getName(), this.getValue(), this.getDate(),
                this.getIndex(), this.getSector(), this.getShares());
    }

    /**
     * Creates a csv line that holds a Cash Account
     *
     * @return - string representing a csv line
     */
    public String exportString() {
        return String.format("\"Equity\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                this.getName(), this.getTotalValue(), this.getDate(), this.getTickerSymbol(),
                this.getIndex(), this.getSector(), this.getShares());
    }
}
