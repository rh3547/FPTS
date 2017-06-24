package fpts.model;

import fpts.toolkit.GuiComponentFactory;
import fpts.toolkit.SaveNotifier;
import fpts.toolkit.SaveSubject;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George Herde on 3/2/16.
 * <p>
 * Abstract class representing a holding a user can have that has a monetary value
 */
public abstract class Holding implements SaveSubject {
    private List<Holding> children = new ArrayList<>();
    private float value;
    private String name;
    private LocalDate date;
    private ArrayList<SaveNotifier> saveNotifiers = new ArrayList<>();

    /**
     * Provides functionality to iterate through the portfolio tree
     */
    public class PortfolioIterator {
        private Holding portfolio;
        private Holding currChild;
        private boolean isDone = false;

        public PortfolioIterator(Holding portfolio) {
            this.portfolio = portfolio;
        }

        /**
         * Gets the first item in a users portfolio and sets it to the current child
         */
        public void first() {
            if (portfolio.getChildren().size() > 0)
                currChild = portfolio.getChildren().get(0);
            if (portfolio.getChildren().size() <= 1)
                isDone = true;
        }

        /**
         * Gets the next item in a users portfolio and sets it the current child
         */
        public void next() {
            int index = portfolio.getChildren().indexOf(currChild);

            if (portfolio.getChildren().size() > index + 1)
                currChild = portfolio.getChild(index + 1);
            if (portfolio.getChildren().size() >= (index + 2))
                isDone = true;
        }

        /**
         * Tells if the currChild is that last in the portfolio
         * @return
         */
        public boolean isDone() {
            return isDone;
        }

        public Holding getCurrChild() {
            return currChild;
        }
    }

    /**
     * Creates a new iterator
     * @return iterator that interates through this portfolio's children
     */
    public PortfolioIterator createIterator() {
        return new PortfolioIterator(this);
    }

    /**
     * @param name  name of the holding
     * @param value value of the holding (total value)
     * @param date  date of last modification of holding (normally now())
     */
    public Holding(String name, float value, LocalDate date) {
        this.name = name;
        this.value = value;
        this.date = date;
    }

    /**
     * @return value of the holding instance
     */
    public float getValue() {
        return value;
    }

    /**
     * @return date of the holding instance
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return name of holding instance
     */
    public String getName() {
        return name;
    }

    /**
     * @return all children of holding instance (can be null)
     */
    public List<Holding> getChildren() {
        return children;
    }

    /**
     * @param num index number to access a single child
     * @return a holding that is a child of the instance
     */
    public Holding getChild(int num) {
        return children.get(num);
    }

    /**
     * @param value value to set on the holding
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * @param date date to set on the holding
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Adds a new child to this node
     *
     * @param holding - child to be added
     */
    public void addChild(Holding holding) {
        this.children.add(holding);
        this.updateValue(holding.value);
        notifyWatchers();
    }

    /**
     * Removes a child from this nodes children and any of its children
     *
     * @param holding - child to be removed
     */
    public void removeChild(Holding holding) {
        // remove holding and update value
        updateValue(-(holding.value));
        children.remove(holding);
        notifyWatchers();
    }


    /**
     * Updates the value of a holding
     *
     * @param value - value to be added
     */
    public void updateValue(float value) {
        float oldValue = getValue();
        float newValue = oldValue + value;
        setValue(newValue);
        notifyWatchers();
    }

    /**
     * Gets the number of children of this node
     *
     * @return - integer of number of children
     */
    public int getNumChildren() {
        return children.size();
    }

    /**
     * Creates a csv line that holds a index share
     *
     * @return - string representing a csv line
     */
    public abstract String exportString();

    /**
     * Creates a JPanel object that represents a holding.
     *
     * @return - JPanel object
     */
    public JPanel makeListObject() {
        DecimalFormat df = new DecimalFormat("###,###,###,###.##");

        String str1 = this.getName();
        String str4 = df.format(this.getValue());

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
        numSharesLbl.setText("-");
        numSharesLbl.setForeground(new Color(142, 141, 141));
        numSharesLbl.setSize(150, 30);
        numSharesLbl.setLocation(205, 5);
        panel.add(numSharesLbl);

        JLabel pricePerLbl;
        pricePerLbl = GuiComponentFactory.getInstance().createBasicLabel();
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
     * Gets a cash account object given a name
     *
     * @param name - name of cash account
     * @return - Holding representing a cash account or null
     */
    public Holding getCashAccount(String name) {
        for (Holding h : getChildren()) {
            if (h.getName().equals(name)) {
                return h;
            }
        }
        return null;
    }

    /**
     * Gets an equity given a ticker symbol
     *
     * @param tickerSymbol - ticker symbol of the equity
     * @return - Equity object or null
     */
    public Equity getEquity(String tickerSymbol) {
        for (Holding h : getChildren()) {
            if (h instanceof Equity) {
                Equity e = (Equity) h;
                if (e.getTickerSymbol().equals(tickerSymbol)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Gets an index share given an index name
     *
     * @param name - name of the index
     * @return - IndexShare object or null
     */
    public IndexShare getIndexShare(String name) {
        for (Holding h : getChildren()) {
            if (h instanceof IndexShare) {
                IndexShare i = (IndexShare) h;
                if (i.getName().equals(name)) {
                    return i;
                }
            }
        }
        return null;
    }

    /**
     * @param saveNotifier add an saveNotifier to the object to be notified
     */
    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        saveNotifiers.add(saveNotifier);
    }

    /**
     * @param saveNotifier remove an saveNotifier so that it is no longer notified
     */
    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        saveNotifiers.remove(saveNotifier);
    }

    /**
     * notify the saveNotifiers of any changes made
     */
    @Override
    public void notifyWatchers() {
        saveNotifiers.forEach(SaveNotifier::update);
    }
}
