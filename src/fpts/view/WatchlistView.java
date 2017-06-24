package fpts.view;

import fpts.controller.*;
import fpts.model.Watchlist.Watchlist;
import fpts.model.Watchlist.WatchlistItem;
import fpts.toolkit.CustomButton;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by rhochmuth on 4/6/2016.
 * <p>
 * The Watchlist view displays the content of a user's Watchlist
 * as a scrollable list of items.
 */
public class WatchlistView extends View {

    // Top panel stuff
    private int topPanelWidth = 0;
    private int topPanelHeight = 0;
    private int mainPanelWidth = 0;
    private int mainPanelHeight = 0;

    private JPanel topPanel;
    private JPanel mainPanel;
    private JLabel titleLbl;
    private JButton backBtn;
    // End top panel stuff

    private JButton addBtn;

    // List heading
    private JPanel headingPanel;
    private JLabel tickerLbl;
    private JLabel currentPriceLbl;
    private JLabel lowLbl;
    private JLabel highLbl;
    private JSeparator holdingsSep;

    // List
    private JScrollPane scrollPane;
    private JPanel scrollList;
    private ArrayList<JPanel> holdingItems = null;

    private DecimalFormat df = new DecimalFormat("###,###,###,###.##");

    @Override
    public void addGuiComponents() {
        // Top panel stuff
        topPanel = new JPanel();
        topPanel.setBackground(new Color(219, 219, 219));
        topPanel.setLayout(null);
        topPanelWidth = GuiController.windowWidth;
        topPanelHeight = 110;
        topPanel.setSize(topPanelWidth, topPanelHeight);
        topPanel.setLocation(0, 0);
        this.add(topPanel);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(239, 239, 239));
        mainPanel.setLayout(null);
        mainPanelWidth = GuiController.windowWidth;
        mainPanelHeight = GuiController.windowHeight - topPanelHeight;
        mainPanel.setSize(mainPanelWidth, mainPanelHeight);
        mainPanel.setLocation(0, topPanelHeight);
        this.add(mainPanel);

        titleLbl = GuiComponentFactory.getInstance().createBasicLabel();
        titleLbl.setText("Watchlist");
        titleLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
        titleLbl.setSize(400, 50);
        titleLbl.setLocation(35, 40);
        topPanel.add(titleLbl);

        backBtn = GuiComponentFactory.getInstance().createBasicButton();
        backBtn.setText("Portfolio");
        backBtn.addActionListener(this);
        backBtn.setSize(100, 30);
        backBtn.setLocation(topPanelWidth - 100 - 35, 52);
        topPanel.add(backBtn);
        // End top panel stuff

        addBtn = GuiComponentFactory.getInstance().createBasicButton();
        addBtn.setText("Add to Watchlist");
        addBtn.addActionListener(this);
        addBtn.setSize(200, 40);
        addBtn.setLocation((mainPanelWidth - 200 - 35), (mainPanelHeight - 40 - 55));
        mainPanel.add(addBtn);

        /*
            List heading
         */
        headingPanel = new JPanel();
        headingPanel.setLayout(null);
        headingPanel.setSize(900, 60);
        headingPanel.setLocation(0, 0);
        mainPanel.add(headingPanel);

        tickerLbl = GuiComponentFactory.getInstance().createBasicLabel();
        tickerLbl.setText("ticker");
        tickerLbl.setForeground(new Color(49, 49, 49));
        tickerLbl.setSize(75, 30);
        tickerLbl.setLocation(10, 10);
        headingPanel.add(tickerLbl);

        currentPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        currentPriceLbl.setText("current price");
        currentPriceLbl.setForeground(new Color(49, 49, 49));
        currentPriceLbl.setSize(150, 30);
        currentPriceLbl.setLocation(200, 10);
        headingPanel.add(currentPriceLbl);

        lowLbl = GuiComponentFactory.getInstance().createBasicLabel();
        lowLbl.setText("low trigger");
        lowLbl.setForeground(new Color(49, 49, 49));
        lowLbl.setSize(175, 30);
        lowLbl.setLocation(475, 10);
        headingPanel.add(lowLbl);

        highLbl = GuiComponentFactory.getInstance().createBasicLabel();
        highLbl.setText("high trigger");
        highLbl.setForeground(new Color(49, 49, 49));
        highLbl.setSize(150, 30);
        highLbl.setLocation(725, 10);
        headingPanel.add(highLbl);

        holdingsSep = GuiComponentFactory.getInstance().createSeparator();
        holdingsSep.setSize(900, 30);
        holdingsSep.setLocation(0, 50);
        headingPanel.add(holdingsSep);

        /*
            List
         */
        Watchlist watchlist = (Watchlist) context.getData("Watchlist");

        if (watchlist != null) {
            holdingItems = new ArrayList<>();
            for (int x = 0; x < watchlist.getWatchlist().size(); x++) {
                WatchlistItem item = watchlist.getWatchlist().get(x);
                holdingItems.add(makeListObject(item));
            }
        }


        setupScrollPane();
    }

    /**
     * Allows the user to scroll throught the watchlist
     */
    private void setupScrollPane() {

        if (scrollPane != null)
            mainPanel.remove(scrollPane);

        scrollPane = null;

        scrollList = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                if (holdingItems != null)
                    return new Dimension(0, 20 + (48 * holdingItems.size()));
                else
                    return new Dimension(0, 0);
            }
        };
        scrollList.setLayout(null);
        scrollList.setSize(mainPanelWidth - 50, mainPanelHeight - 100);

        scrollPane = new JScrollPane(scrollList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setSize(mainPanelWidth - 50, mainPanelHeight - 100);
        scrollPane.setLocation(25, 60);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        mainPanel.add(scrollPane);

        if (holdingItems != null) {

            for (int x = 0; x < holdingItems.size(); x++) {
                JPanel panel = holdingItems.get(x);

                panel.setLocation(500 - 450, 20 + (48 * x));

                scrollList.add(panel);
            }
        }

        this.resize();
    }

    /**
     * Makes Jpanel objects from watchlist items
     * @param item A watchlist item that is in the users watchlist
     * @return a JPanel object representing the watchlist item
     */
    public JPanel makeListObject(WatchlistItem item) {
        String str1 = item.getTicker();
        String str2 = Float.toString(MarketController.getInstance().getMarket().getStockPrice(item.getTicker()));
        String str3 = Float.toString(item.getLowTrigger());
        String str4 = Float.toString(item.getHighTrigger());
        boolean lowTrigger = item.isLowTriggerActive();
        boolean highTrigger = item.isHighTriggerActive();
        boolean lowTriggerHistory = item.isLowTriggerActiveHistory();
        boolean highTriggerHistory = item.isHighTriggerActiveHistory();

        DecimalFormat df = new DecimalFormat("###,###,###,###.##");

        JPanel panel = new JPanel();
        panel.setSize(900, 40);
        panel.setBackground(new Color(219, 219, 219));
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(190, 190, 190)));

        testNotificationMarks(item, panel);

        JLabel tickerLbl;
        tickerLbl = GuiComponentFactory.getInstance().createBasicLabel();
        tickerLbl.setText(str1);
        tickerLbl.setForeground(new Color(49, 49, 49));
        tickerLbl.setSize(160, 30);
        tickerLbl.setLocation(10, 5);
        panel.add(tickerLbl);

        JLabel currentPriceLbl;
        currentPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str2 != null)
            currentPriceLbl.setText("$ " + str2);
        else
            currentPriceLbl.setText("-");
        currentPriceLbl.setForeground(new Color(142, 141, 141));
        currentPriceLbl.setSize(150, 30);
        currentPriceLbl.setLocation(200, 5);
        panel.add(currentPriceLbl);

        JLabel lowPriceLbl;
        lowPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        if (str3 != null)
            lowPriceLbl.setText("$ " + str3);
        else
            lowPriceLbl.setText("-");
        lowPriceLbl.setForeground(new Color(35, 34, 34));
        lowPriceLbl.setSize(200, 30);
        lowPriceLbl.setLocation(475, 5);
        panel.add(lowPriceLbl);

        if (lowTrigger) {
            lowPriceLbl.setForeground(new Color(76, 175, 80));
        }

        if (lowTriggerHistory) {
            JButton lowPriceBtn;
            lowPriceBtn = GuiComponentFactory.getInstance().createBasicButton();
            lowPriceBtn.setSize(40, 30);
            lowPriceBtn.setFont(ResourceLoader.robotoFont.deriveFont(16.0f));
            lowPriceBtn.setText("!");
            lowPriceBtn.setLocation(475, 5);
            lowPriceBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    item.setLowTriggerActiveHistory(false);
                    testNotificationMarks(item, panel);
                    panel.remove(lowPriceBtn);
                    lowPriceLbl.setLocation(475, 5);
                    panel.repaint();
                }
            });
            panel.add(lowPriceBtn);

            lowPriceLbl.setLocation(475 + 5 + 40, 5);
        }

        JLabel highPriceLbl;
        highPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        highPriceLbl.setText("$ " + str4);
        highPriceLbl.setForeground(new Color(35, 34, 34));
        highPriceLbl.setSize(200, 30);
        highPriceLbl.setLocation(725, 5);
        panel.add(highPriceLbl);

        if (highTrigger) {
            highPriceLbl.setForeground(new Color(76, 175, 80));
        }

        if (highTriggerHistory) {
            JButton highPriceBtn;
            highPriceBtn = GuiComponentFactory.getInstance().createBasicButton();
            highPriceBtn.setSize(40, 30);
            highPriceBtn.setFont(ResourceLoader.robotoFont.deriveFont(16.0f));
            highPriceBtn.setText("!");
            highPriceBtn.setLocation(725, 5);
            highPriceBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    item.setHighTriggerActiveHistory(false);
                    testNotificationMarks(item, panel);
                    panel.remove(highPriceBtn);
                    highPriceLbl.setLocation(725, 5);
                    panel.repaint();
                }
            });
            panel.add(highPriceBtn);

            highPriceLbl.setLocation(725 + 5 + 40 + 5, 5);
        }

        CustomButton removeBtn;
        removeBtn = (CustomButton) GuiComponentFactory.getInstance().createBasicButton();
        removeBtn.setBackground(null);
        removeBtn.setHoverBackgroundColor(new Color(245, 28, 44));
        removeBtn.setPressedBackgroundColor(new Color(245, 28, 44));
        removeBtn.setForeground(new Color(255, 255, 255));
        removeBtn.setSize(45, 30);
        removeBtn.setFont(ResourceLoader.robotoFontBold.deriveFont(16.0f));
        removeBtn.setText("x");
        removeBtn.setLocation(900 - 45 - 5, 5);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WatchlistController.getInstance().removeWatchlistItem(item);
            }
        });
        panel.add(removeBtn);

        return panel;
    }

    /**
     * Checks to see if a trigger notification has been dismissed
     * @param item The watchlist item that the trigger is associated with
     * @param panel The Jpanel the watchlist item is on
     */
    private void testNotificationMarks(WatchlistItem item, JPanel panel) {
        boolean lowTriggerHistory = item.isLowTriggerActiveHistory();
        boolean highTriggerHistory = item.isHighTriggerActiveHistory();

        if (lowTriggerHistory || highTriggerHistory) {
            panel.setBackground(new Color(207, 225, 201));
        } else if (!lowTriggerHistory && !highTriggerHistory) {
            panel.setBackground(new Color(219, 219, 219));
        }
    }

    @Override
    public void removeGuiComponents() {
    }

    @Override
    public void resize() {
        // Top panel stuff
        topPanelWidth = GuiController.windowWidth;
        mainPanelWidth = GuiController.windowWidth;
        mainPanelHeight = GuiController.windowHeight - topPanelHeight;
        topPanel.setSize(topPanelWidth, topPanelHeight);
        mainPanel.setSize(mainPanelWidth, mainPanelHeight);
        mainPanel.setLocation(0, topPanelHeight);
        backBtn.setLocation(topPanelWidth - 100 - 35, 52);
        // End top panel stuff

        addBtn.setLocation((mainPanelWidth - 200 - 35), (mainPanelHeight - 40 - 55));

        // List heading
        headingPanel.setLocation((mainPanelWidth / 2) - 450, 0);

        // List
        scrollPane.setSize(1000, mainPanelHeight - 200);
        scrollPane.setLocation((mainPanelWidth / 2) - 500, 70);
        scrollList.setSize(1000, mainPanelHeight - 200);
    }

    @Override
    public void showMessage(String message, int type) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            PortfolioController.getInstance().showPortfolioView();
        }

        if (e.getSource() == addBtn) {
            WatchlistController.getInstance().showAddWatchlistView();
        }
    }
}
