package fpts.view;

import fpts.controller.*;
import fpts.toolkit.CustomScrollBar;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by rhochmuth on 4/6/2016.
 *
 * The add Watchlist view shows a form that allows a user to
 * add a new item to their Watchlist.
 */
public class AddWatchlistView extends View {

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

    private JButton watchlistBtn;

    private JSeparator vertSep;
    private JPanel watchlistFormPanel;
    private JPanel watchlistRightPanel;
    private JPanel watchlistLeftPanel;
    private JLabel chosenStockLbl;
    private JLabel chosenPriceLbl;
    private JLabel lowTriggerLbl;
    private JTextField lowTriggerField;
    private JLabel highTriggerLbl;
    private JTextField highTriggerField;
    private JButton addBtn;
    private JTextField searchField;
    private JScrollPane scrollPane;
    private JPanel scrollList;
    private JLabel noticeLbl;
    private ArrayList<JPanel> marketItems = null;

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
        titleLbl.setText("Add to Watchlist");
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

        watchlistBtn = GuiComponentFactory.getInstance().createBasicButton();
        watchlistBtn.setText("Watchlist");
        watchlistBtn.addActionListener(this);
        watchlistBtn.setSize(125, 30);
        watchlistBtn.setLocation(topPanelWidth - 100 - 35 - 125 - 35, 52);
        topPanel.add(watchlistBtn);

        noticeLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        noticeLbl.setText("Enter -1 into a field if you don't want to set that trigger");
        noticeLbl.setSize(600, 30);
        noticeLbl.setLocation((mainPanelWidth / 2) - 300, 5);
        mainPanel.add(noticeLbl);

        /*
            Watchlist form
         */
        setupWatchlistForm();
        showWatchlistForm();
    }

    /**
     * Sets up a new form to add items to the watchlist
     */
    private void setupWatchlistForm() {
        watchlistFormPanel = new JPanel();
        watchlistFormPanel.setLayout(null);
        watchlistFormPanel.setSize(mainPanelWidth, mainPanelHeight - 150);
        watchlistFormPanel.setLocation(0, 50);

        int rightPanelWidth = 300;

        watchlistRightPanel = new JPanel();
        watchlistRightPanel.setLayout(null);
        watchlistRightPanel.setSize(rightPanelWidth, mainPanelHeight);
        watchlistRightPanel.setLocation((mainPanelWidth / 2) + rightPanelWidth, 0);
        watchlistFormPanel.add(watchlistRightPanel);

        chosenStockLbl = GuiComponentFactory.getInstance().createBasicLabel();
        chosenStockLbl.setText("Stock: None");
        chosenStockLbl.setSize(250, 30);
        chosenStockLbl.setLocation(88, 0);
        watchlistRightPanel.add(chosenStockLbl);

        chosenPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        chosenPriceLbl.setText("Price:  None");
        chosenPriceLbl.setSize(250, 30);
        chosenPriceLbl.setLocation(88, 35);
        watchlistRightPanel.add(chosenPriceLbl);

        lowTriggerLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        lowTriggerLbl.setText("low trigger");
        lowTriggerLbl.setSize(125, 30);
        lowTriggerLbl.setLocation((rightPanelWidth / 2) - 62, 50);
        watchlistRightPanel.add(lowTriggerLbl);

        lowTriggerField = GuiComponentFactory.getInstance().createBasicTextField();
        lowTriggerField.setSize(125, 50);
        lowTriggerField.setLocation((rightPanelWidth / 2) - 62, 55);
        lowTriggerField.setText("0");
        watchlistRightPanel.add(lowTriggerField);

        highTriggerLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        highTriggerLbl.setText("high trigger");
        highTriggerLbl.setSize(125, 30);
        highTriggerLbl.setLocation((rightPanelWidth / 2) - 62, 125);
        watchlistRightPanel.add(highTriggerLbl);

        highTriggerField = GuiComponentFactory.getInstance().createBasicTextField();
        highTriggerField.setSize(125, 50);
        highTriggerField.setLocation((rightPanelWidth / 2) - 62, 130);
        highTriggerField.setText("0");
        watchlistRightPanel.add(highTriggerField);

        addBtn = GuiComponentFactory.getInstance().createBasicButton();
        addBtn.setText("Add");
        addBtn.setSize(125, 40);
        addBtn.setLocation((rightPanelWidth / 2) - 25, 200);
        addBtn.addActionListener(this);
        watchlistRightPanel.add(addBtn);

        vertSep = GuiComponentFactory.getInstance().createSeparator();
        vertSep.setOrientation(JSeparator.VERTICAL);
        vertSep.setLocation(0, 10);
        vertSep.setSize(10, mainPanelHeight - 225);
        watchlistRightPanel.add(vertSep);

        int leftPanelWidth = 300;

        watchlistLeftPanel = new JPanel();
        watchlistLeftPanel.setLayout(null);
        watchlistLeftPanel.setSize(leftPanelWidth, mainPanelHeight);
        watchlistLeftPanel.setLocation((mainPanelWidth / 2) - 300, 0);
        watchlistFormPanel.add(watchlistLeftPanel);

        searchField = GuiComponentFactory.getInstance().createPlaceholderTextField("search market");
        searchField.setBackground(new Color(221, 221, 221));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                LinkedList<String> list = PortfolioController.getInstance().searchMarket(searchField.getText());
                marketItems = new ArrayList<>();

                for (String s : list) {
                    marketItems.add(makeListObject(s));
                }

                setupScrollPane();

                GuiController.getInstance().refreshViewWithToolbar();
            }
        });
        searchField.setSize(250, 50);
        searchField.setLocation((leftPanelWidth / 2) - 125, 0);
        watchlistLeftPanel.add(searchField);

        marketItems = new ArrayList<>();

        setupScrollPane();
    }

    /**
     * Provides the functionality to scroll through watchlist items
     */
    private void setupScrollPane() {

        if (scrollPane != null)
            watchlistLeftPanel.remove(scrollPane);

        scrollPane = null;

        scrollList = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                if (marketItems != null)
                    return new Dimension(0, 20 + (48 * marketItems.size()));
                else
                    return new Dimension(0, 0);
            }
        };
        scrollList.setLayout(null);
        scrollList.setSize(250, (48 * marketItems.size()));

        scrollPane = new JScrollPane(scrollList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(270, mainPanelHeight - 300);
            }
        };
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.setBorder(null);
        scrollPane.setSize(270, mainPanelHeight - 300);
        scrollPane.setLocation((300 / 2) - 125, 60);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        watchlistLeftPanel.add(scrollPane);

        if (marketItems != null) {

            for (int x = 0; x < marketItems.size(); x++) {
                JPanel panel = marketItems.get(x);

                panel.setLocation(0, 20 + (48 * x));
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseClicked(e);

                        chosenStockLbl.setText("Stock: " + ((JLabel) panel.getComponent(0)).getText());
                        float price = MarketController.getInstance().getMarket().getStockPrice(((JLabel) panel.getComponent(0)).getText());
                        chosenPriceLbl.setText("Price:  $ " + Float.toString(price));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        panel.setBackground(new Color(207, 225, 201));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        panel.setBackground(new Color(219, 219, 219));
                    }
                });

                scrollList.add(panel);
                scrollList.repaint();
            }
        }

        scrollList.repaint();
        scrollPane.repaint();
        watchlistLeftPanel.repaint();
    }

    /**
     * Displays the watchlist form
     */
    private void showWatchlistForm() {
        mainPanel.add(watchlistFormPanel);
        mainPanel.repaint();
        this.resize();

        GuiController.getInstance().getWindow().getRootPane().setDefaultButton(addBtn);
    }

    /**
     * Make a JPanel that represents a market share in the list of purchasable market shares.
     * @param str1 - the name of the share
     * @return JPanel
     */
    private JPanel makeListObject(String str1) {

        JPanel panel = new JPanel();
        panel.setSize(200, 40);
        panel.setBackground(new Color(219, 219, 219));
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(190, 190, 190)));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel nameLbl;
        nameLbl = GuiComponentFactory.getInstance().createBasicLabel();
        nameLbl.setText(str1);
        nameLbl.setForeground(new Color(49, 49, 49));
        nameLbl.setSize(200, 30);
        nameLbl.setLocation(10, 5);
        panel.add(nameLbl);

        return panel;
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

        watchlistBtn.setLocation(topPanelWidth - 100 - 35 - 125 - 35, 52);

        watchlistFormPanel.setSize(mainPanelWidth, mainPanelHeight - 200);
        int rightPanelWidth = 300;
        watchlistRightPanel.setLocation((mainPanelWidth / 2), 0);
        lowTriggerLbl.setLocation((rightPanelWidth / 2) - 62, 80);
        lowTriggerField.setLocation((rightPanelWidth / 2) - 62, 115);
        highTriggerLbl.setLocation((rightPanelWidth / 2) - 62, 185);
        highTriggerField.setLocation((rightPanelWidth / 2) - 62, 220);
        vertSep.setSize(10, mainPanelHeight - 200);
        addBtn.setLocation((rightPanelWidth / 2) - 62, 300);

        int leftPanelWidth = 300;
        watchlistLeftPanel.setLocation((mainPanelWidth / 2) - 300, 0);
        searchField.setLocation((leftPanelWidth / 2) - 125, 0);
        scrollPane.setSize(270, mainPanelHeight - 300);
        scrollPane.setLocation((leftPanelWidth / 2) - 125, 100);
        scrollList.setSize(250, (48 * marketItems.size()));
    }

    @Override
    public void showMessage(String message, int type) {

    }

    /**
     * Chaeck if the given string is numeric.
     * @param str - the string to check
     * @return boolean
     */
    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            PortfolioController.getInstance().showPortfolioView();
        }

        if (e.getSource() == watchlistBtn) {
            WatchlistController.getInstance().showWatchlistView();
        }

        if (e.getSource() == addBtn) {
            if (chosenStockLbl.getText().equals("Stock: None")) {
                showMessage("Please select a stock", View.ERROR_MESSAGE);
                GuiController.getInstance().showErrorNotification("Please select a stock", 5);
                return;
            }

            if (!isNumeric(lowTriggerField.getText())) {
                showMessage("Please enter a valid number for low trigger", View.ERROR_MESSAGE);
                GuiController.getInstance().showErrorNotification("Please enter a valid number for low trigger", 5);
                return;
            }

            if (!isNumeric(highTriggerField.getText())) {
                showMessage("Please enter a valid number for high trigger", View.ERROR_MESSAGE);
                GuiController.getInstance().showErrorNotification("Please enter a valid number for high trigger", 5);
                return;
            }

            WatchlistController.getInstance().addWatchlistItem(chosenStockLbl.getText().substring(7),
                    lowTriggerField.getText(),
                    highTriggerField.getText());
        }
    }
}
