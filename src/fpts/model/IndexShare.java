package fpts.model;

import fpts.controller.MarketController;
import fpts.toolkit.GuiComponentFactory;
import fpts.toolkit.SaveNotifier;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

/**
 * A share of a index or sector in the market that holds many equities
 */
public class IndexShare extends Holding {
    private float shares;
    private List<List<String>> sectorInformation;

    /**
     * @param name   name of the index share
     * @param value  total value of the index share
     * @param date   last update of the index share
     * @param shares number of shares of the index
     */
    public IndexShare(String name, float value, LocalDate date, float shares) {
        super(name, value, date);
        this.shares = shares;
        Market m = MarketController.getInstance().m;

        sectorInformation = m.getSector(name);
        int count = sectorInformation.size();
        for (List<String> marketInformation : sectorInformation) {
            String equityName = null;
            float equityPrice = 0;
            String equitySector = null;
            String equityIndex = null;

            for (int i = 0; i < marketInformation.size(); i++) {
                String info = marketInformation.get(i);
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        equityName = marketInformation.get(i);
                        break;
                    case 2:
                        equityPrice = Float.parseFloat(marketInformation.get(i));
                        break;
                    case 3:
                        if (info.equals("NASDAQ100") || info.equals("DOW")) {
                            if (equityIndex == null) {
                                equityIndex = marketInformation.get(i);
                            } else {
                                equitySector = marketInformation.get(i);
                            }
                        } else {
                            equitySector = marketInformation.get(i);
                        }
                        break;
                    case 4:
                        if (info.equals("NASDAQ100") || info.equals("DOW")) {
                            if (equityIndex == null) {
                                equityIndex = marketInformation.get(i);
                            } else {
                                equitySector = marketInformation.get(i);
                            }
                        }
                        break;
                }
                Equity e = new Equity(equityName, equityPrice / count, LocalDate.now(),
                        marketInformation.get(0), (shares / count), equityIndex, equitySector);
                this.addChild(e);
            }
        }
    }


    /**
     * Updates the number of shares, value and purchased date
     *
     * @param shares The new number of shares
     * @param value  The new total value
     * @param date   the new purchased date
     */
    public void updateIndexShare(float shares, float value, LocalDate date) {
        this.setValue(this.getValue() + value);
        this.shares += shares;
        this.setDate(date);

        for (Holding holding : this.getChildren()) {
            if (holding instanceof Equity) {
                float tempValue = holding.getValue();
                float tempShares = ((Equity) holding).getShares();
                float price = tempValue / tempShares;

                float newValue = price * shares / sectorInformation.size() + tempValue;
                float newShares = tempShares + shares / sectorInformation.size();

                ((Equity) holding).updateEquity(newShares, price, newValue, date);
            }
        }
    }

    /**
     * @return number of shares
     */
    public float getShares() {
        return shares;
    }

    /**
     * @return string formatted for proper printing
     */
    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\"",
                this.getName(), this.getShares(), this.getValue(), this.getDate());
    }

    /**
     * @return value of the holding instance
     */
    public float getValue() {
        return MarketController.getInstance().m.getSectorPrice(getName()) * this.shares;
    }

    /**
     * Creates a csv line that holds a index share
     *
     * @return String representing a csv line
     */
    public String exportString() {
        return String.format("\"Index Share\",\"%s\",\"%s\",\"%s\",\"%s\"",
                this.getName(), this.getShares(), this.getValue(), this.getDate());
    }

    /**
     * Make a JPanel that's structured to represent this IndexShare.
     *
     * @return GUI object ready to be displayed
     */
    @Override
    public JPanel makeListObject() {
        DecimalFormat df = new DecimalFormat("###,###,###,###.##");
        String str1 = this.getName();
        String str2 = Float.toString(this.getShares());
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
     * @param saveNotifier Add an saveNotifier to the object to be notified
     */
    @Override
    public void registerWatcher(SaveNotifier saveNotifier) {
        super.registerWatcher(saveNotifier);
    }

    /**
     * @param saveNotifier Remove an saveNotifier so that it is no longer notified
     */
    @Override
    public void removeWatcher(SaveNotifier saveNotifier) {
        super.removeWatcher(saveNotifier);
    }

    /**
     * Notify the observers that something has been changed
     */
    @Override
    public void notifyWatchers() {
        super.notifyWatchers();
    }
}
