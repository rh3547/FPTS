package fpts.model;

import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 *
 * Created by George Herde on 3/2/16.
 *
 * Transactions represent the historical movement of money in the system
 */
public class Transaction {
    private LocalDateTime date;
    private String origin;
    private String destination;
    private String type;
    private float value;

    /**
     * Create a new Transaction object.
     * @param type
     * @param origin
     * @param destination
     * @param value
     * @param date
     */
    public Transaction(String type, String origin, String destination,
                       float value, LocalDateTime date ) {
        this.type = type;
        this.origin = origin;
        this.destination = destination;
        this.value = value;
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getType() {
        return type;
    }


    @Override
    public String toString(){
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                this.getDate(), this.getType(), this.getOrigin(), this.getDestination(), this.getValue());
    }

    /**
     * Creates a csv line that holds a transaction
     *
     * @return - string representing a csv line
     */
    public String exportString(){
        return String.format("\"Transaction\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                this.getDate(), this.getType(), this.getOrigin(), this.getDestination(), this.getValue());
    }

    /**
     * Creates a JPanel object that represents a transaction
     *
     * @return - JPanel object
     */
    public JPanel makeListObject() {
        DecimalFormat df = new DecimalFormat("###,###,###,###.##");

        String str1 = this.date.toString();
        String str2 = this.origin;
        String str3 = this.destination;
        String str4 = this.type;
        String str5 = df.format(this.getValue());

        JPanel panel = new JPanel();
        panel.setSize(900, 40);
        panel.setBackground(new Color(219, 219, 219));
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(190, 190, 190)));

        JLabel dateLbl;
        dateLbl = GuiComponentFactory.getInstance().createBasicLabel();
        dateLbl.setText(str1);
        dateLbl.setForeground(new Color(49, 49, 49));
        dateLbl.setSize(160, 30);
        dateLbl.setLocation(10, 5);
        panel.add(dateLbl);

        JLabel originLbl;
        originLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str2 != null)
            originLbl.setText(str2);
        else
            originLbl.setText("-");
        originLbl.setForeground(new Color(142, 141, 141));
        originLbl.setSize(150, 30);
        originLbl.setLocation(205, 5);
        panel.add(originLbl);

        JLabel destinationLbl;
        destinationLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str3 != null)
            destinationLbl.setText(str3);
        else
            destinationLbl.setText("-");
        destinationLbl.setForeground(new Color(142, 141, 141));
        destinationLbl.setSize(200, 30);
        destinationLbl.setLocation(365, 5);
        panel.add(destinationLbl);

        JLabel typeLbl;
        typeLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str3 != null)
            typeLbl.setText(str4);
        else
            typeLbl.setText("-");
        typeLbl.setForeground(new Color(142, 141, 141));
        typeLbl.setSize(200, 30);
        typeLbl.setLocation(568, 5);
        panel.add(typeLbl);

        JLabel valueLbl;
        valueLbl = GuiComponentFactory.getInstance().createBasicLabel();
        valueLbl.setText("$ " + str5);
        valueLbl.setForeground(new Color(76, 175, 80));
        valueLbl.setSize(200, 30);
        valueLbl.setLocation(768, 5);
        panel.add(valueLbl);

        return panel;
    }
}
