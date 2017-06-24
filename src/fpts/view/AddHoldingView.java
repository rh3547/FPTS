package fpts.view;

import fpts.controller.*;
import fpts.model.Market;
import fpts.toolkit.CustomScrollBar;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Ryan on 3/11/2016.
 *
 * View used to add new holdings to the portfolio.  Contains a form to input the required values for a new holding.
 */
public class AddHoldingView extends View {

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

    private JLabel holdingTypeLbl;
    private String[] holdingTypes = {"Cash Account", "Equity", "Index Share"};
    private JComboBox holdingTypeCB;
    private JSeparator topSep;

    private JPanel cashFormPanel;
    private JLabel cashNameLbl;
    private JTextField cashNameField;
    private JLabel cashAmountLbl;
    private JTextField cashAmountField;
    private JButton cashAddBtn;
    private JSeparator vertSep;

    private JPanel equityFormPanel;
    private JPanel equityRightPanel;
    private JPanel equityLeftPanel;
    private JLabel equityAmountLbl;
    private JTextField equityAmountField;
    private JButton equityAddBtn;
    private JTextField searchField;
    private JScrollPane scrollPane;
    private JPanel scrollList;
    private ArrayList<JPanel> marketItems = null;
    private String[] cashAccountList;
    private JLabel cashAccountLbl;
    private JComboBox cashAccountCB;
    private JLabel chosenStockLbl;
    private JLabel equityPriceLbl;
    private JLabel equityTotalCostLbl;

    private JPanel indexFormPanel;
    private JLabel indexNameLbl;
    private String[] indexNameList;
    private JComboBox indexNameCB;
    private JLabel indexAmountLbl;
    private JTextField indexAmountField;
    private JLabel indexCashAccountLbl;
    private JComboBox indexCashAccountCB;
    private JButton indexAddBtn;
    private JLabel indexPriceLbl;
    private JLabel indexTotalCostLbl;

    private int formShown = 0;

    private JLabel messageLbl;

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
        titleLbl.setText("Add New Holding");
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

        messageLbl = new JLabel();
        messageLbl.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        messageLbl.setText("");
        messageLbl.setForeground(new Color(69, 69, 69));
        messageLbl.setSize(300, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - 150, (GuiController.windowHeight / 2) + 200);
        mainPanel.add(messageLbl);

        holdingTypeLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        holdingTypeLbl.setText("holding type");
        holdingTypeLbl.setSize(150, 30);
        holdingTypeLbl.setLocation((mainPanelWidth / 2) - 150, 20);
        mainPanel.add(holdingTypeLbl);

        holdingTypeCB = new JComboBox(holdingTypes);
        holdingTypeCB.setSelectedIndex(0);
        holdingTypeCB.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        holdingTypeCB.setBackground(new Color(221, 221, 221));
        holdingTypeCB.setUI(new BasicComboBoxUI());
        holdingTypeCB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        holdingTypeCB.addActionListener(this);
        holdingTypeCB.setSize(300, 50);
        holdingTypeCB.setLocation((mainPanelWidth / 2) - 150, 55);
        mainPanel.add(holdingTypeCB);

        topSep = GuiComponentFactory.getInstance().createSeparator();
        topSep.setSize((mainPanelWidth - 100), 30);
        topSep.setLocation((mainPanelWidth / 2) - ((mainPanelWidth - 100) / 2), 130);
        mainPanel.add(topSep);

        setupCashAccountForm();
        setupEquityForm();
        setupIndexForm();
        showCashForm();
    }

    /**
     * Setup the form for adding a cash account.
     */
    private void setupCashAccountForm() {
        cashFormPanel = new JPanel();
        cashFormPanel.setLayout(null);
        cashFormPanel.setSize(mainPanelWidth, mainPanelHeight - 150);
        cashFormPanel.setLocation(0, 200);

        cashNameLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        cashNameLbl.setText("account name");
        cashNameLbl.setSize(200, 30);
        cashNameLbl.setLocation((mainPanelWidth / 2) - 200, 0);
        cashFormPanel.add(cashNameLbl);

        cashNameField = GuiComponentFactory.getInstance().createBasicTextField();
        cashNameField.setSize(400, 50);
        cashNameField.setLocation((mainPanelWidth / 2) - 200, 35);
        cashFormPanel.add(cashNameField);

        cashAmountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        cashAmountLbl.setText("initial amount");
        cashAmountLbl.setSize(200, 30);
        cashAmountLbl.setLocation((mainPanelWidth / 2) - 200, 95);
        cashFormPanel.add(cashAmountLbl);

        cashAmountField = GuiComponentFactory.getInstance().createBasicTextField();
        cashAmountField.setSize(400, 50);
        cashAmountField.setLocation((mainPanelWidth / 2) - 200, 130);
        cashAmountField.setText("0");
        cashFormPanel.add(cashAmountField);

        cashAddBtn = GuiComponentFactory.getInstance().createBasicButton();
        cashAddBtn.setText("Add Cash Account");
        cashAddBtn.setSize(175, 40);
        cashAddBtn.setLocation((mainPanelWidth / 2) - 87, 200);
        cashAddBtn.addActionListener(this);
        cashFormPanel.add(cashAddBtn);
    }

    /**
     * Setup the form for adding an equity.
     */
    private void setupEquityForm() {
        equityFormPanel = new JPanel();
        equityFormPanel.setLayout(null);
        equityFormPanel.setSize(mainPanelWidth, mainPanelHeight - 150);
        equityFormPanel.setLocation(0, 200);

        int rightPanelWidth = 300;

        equityRightPanel = new JPanel();
        equityRightPanel.setLayout(null);
        equityRightPanel.setSize(rightPanelWidth, mainPanelHeight);
        equityRightPanel.setLocation((mainPanelWidth / 2) + rightPanelWidth, 0);
        equityFormPanel.add(equityRightPanel);

        equityAmountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        equityAmountLbl.setText("# of shares");
        equityAmountLbl.setSize(125, 30);
        equityAmountLbl.setLocation((rightPanelWidth / 2) - 62, 0);
        equityRightPanel.add(equityAmountLbl);

        equityAmountField = GuiComponentFactory.getInstance().createBasicTextField();
        equityAmountField.setSize(125, 50);
        equityAmountField.setLocation((rightPanelWidth / 2) - 62, 35);
        equityAmountField.setText("0");
        equityAmountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (equityPriceLbl != null && !equityPriceLbl.getText().equals("Price: -") &&
                        isNumeric(equityAmountField.getText()) && Integer.parseInt(equityAmountField.getText()) >= 0) {

                    String priceStr = equityPriceLbl.getText().substring(9).replace(",", "");
                    float price = Float.parseFloat(priceStr);
                    int quantity = Integer.parseInt(equityAmountField.getText());
                    float total = price * quantity;

                    if (quantity == 0)
                        equityTotalCostLbl.setText("Total: -");
                    else
                        equityTotalCostLbl.setText("Total: $ " + df.format(total));

                    int totalWidth = equityTotalCostLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(20.0f))
                            .stringWidth(equityTotalCostLbl.getText());
                    equityTotalCostLbl.setSize(totalWidth, 30);
                    equityTotalCostLbl.setLocation((rightPanelWidth / 2) - (totalWidth / 2), 105);

                    GuiController.getInstance().refreshViewWithToolbar();
                }
            }
        });
        equityRightPanel.add(equityAmountField);

        equityTotalCostLbl = GuiComponentFactory.getInstance().createBasicLabel();
        equityTotalCostLbl.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        equityTotalCostLbl.setText("Total: -");
        int totalWidth = equityTotalCostLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(20.0f))
                .stringWidth(equityTotalCostLbl.getText());
        equityTotalCostLbl.setSize(totalWidth, 30);
        equityTotalCostLbl.setLocation((rightPanelWidth / 2) - (totalWidth / 2), 105);
        equityRightPanel.add(equityTotalCostLbl);

        cashAccountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        cashAccountLbl.setText("purchased from");
        cashAccountLbl.setSize(175, 30);
        cashAccountLbl.setLocation((rightPanelWidth / 2) - 87, 100);
        equityRightPanel.add(cashAccountLbl);

        cashAccountList = PortfolioController.getInstance().getCashAccountsList();

        cashAccountCB = GuiComponentFactory.getInstance().createComboBox(cashAccountList);
        cashAccountCB.addActionListener(this);
        cashAccountCB.setSize(250, 50);
        cashAccountCB.setLocation((rightPanelWidth / 2) - 125, 135);
        equityRightPanel.add(cashAccountCB);

        equityAddBtn = GuiComponentFactory.getInstance().createBasicButton();
        equityAddBtn.setText("Add Equity");
        equityAddBtn.setSize(125, 40);
        equityAddBtn.setLocation((rightPanelWidth / 2) - 25, 200);
        equityAddBtn.addActionListener(this);
        equityRightPanel.add(equityAddBtn);

        vertSep = GuiComponentFactory.getInstance().createSeparator();
        vertSep.setOrientation(JSeparator.VERTICAL);
        vertSep.setLocation(0, 10);
        vertSep.setSize(10, mainPanelHeight - 225);
        equityRightPanel.add(vertSep);

        int leftPanelWidth = 300;

        equityLeftPanel = new JPanel();
        equityLeftPanel.setLayout(null);
        equityLeftPanel.setSize(leftPanelWidth, mainPanelHeight);
        equityLeftPanel.setLocation((mainPanelWidth / 2) - 300, 0);
        equityFormPanel.add(equityLeftPanel);

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
        equityLeftPanel.add(searchField);

        chosenStockLbl = GuiComponentFactory.getInstance().createBasicLabel();
        chosenStockLbl.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        chosenStockLbl.setText("Stock: None");
        chosenStockLbl.setSize(250, 30);
        chosenStockLbl.setLocation(25, 50);
        equityLeftPanel.add(chosenStockLbl);

        equityPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        equityPriceLbl.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        equityPriceLbl.setText("Price: -");
        equityPriceLbl.setSize(250, 30);
        equityPriceLbl.setLocation(25, 80);
        equityLeftPanel.add(equityPriceLbl);

        marketItems = new ArrayList<>();

        setupScrollPane();
    }

    /**
     * Setup the form used for adding index shares.
     */
    private void setupIndexForm() {
        indexFormPanel = new JPanel();
        indexFormPanel.setLayout(null);
        indexFormPanel.setSize(mainPanelWidth, mainPanelHeight - 150);
        indexFormPanel.setLocation(0, 200);

        indexNameLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        indexNameLbl.setText("index name");
        indexNameLbl.setSize(200, 40);
        indexNameLbl.setLocation((mainPanelWidth / 2) - 250, 0);
        indexFormPanel.add(indexNameLbl);

        Set<String> list = new Market().getSectors().keySet();
        indexNameList = new String[list.size()];
        int index = 0;
        for (String s : list) {
            indexNameList[index] = s;
            index++;
        }

        indexNameCB = new JComboBox(indexNameList);
        indexNameCB.setSelectedIndex(0);
        indexNameCB.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        indexNameCB.setBackground(new Color(221, 221, 221));
        indexNameCB.setUI(new BasicComboBoxUI());
        indexNameCB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        indexNameCB.setSize(400, 50);
        indexNameCB.setLocation((mainPanelWidth / 2) - 250, 40);
        indexNameCB.addActionListener(this);
        indexFormPanel.add(indexNameCB);

        indexAmountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        indexAmountLbl.setText("# of shares");
        indexAmountLbl.setSize(150, 30);
        indexAmountLbl.setLocation((mainPanelWidth / 2) - 250 + 410, 0);
        indexFormPanel.add(indexAmountLbl);

        indexAmountField = GuiComponentFactory.getInstance().createBasicTextField();
        indexAmountField.setSize(90, 50);
        indexAmountField.setLocation((mainPanelWidth / 2) - 250 + 410, 40);
        indexAmountField.setText("0");
        indexAmountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (indexPriceLbl != null && !indexPriceLbl.getText().equals("Price: -") &&
                        isNumeric(indexAmountField.getText()) && Integer.parseInt(indexAmountField.getText()) >= 0) {

                    String priceStr = indexPriceLbl.getText().substring(9).replace(",", "");
                    float price = Float.parseFloat(priceStr);
                    int quantity = Integer.parseInt(indexAmountField.getText());
                    float total = price * quantity;

                    if (quantity == 0)
                        indexTotalCostLbl.setText("Total: -");
                    else
                        indexTotalCostLbl.setText("Total: $ " + df.format(total));

                    GuiController.getInstance().refreshViewWithToolbar();
                }
            }
        });
        indexFormPanel.add(indexAmountField);

        indexPriceLbl = GuiComponentFactory.getInstance().createBasicLabel();
        indexPriceLbl.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        float price = MarketController.getInstance().getMarket().getSectorPrice((String)indexNameCB.getSelectedItem());
        indexPriceLbl.setText("Price: $ " + df.format(price));
        indexPriceLbl.setSize(250, 30);
        indexPriceLbl.setLocation((mainPanelWidth / 2) - 250, 100);
        indexFormPanel.add(indexPriceLbl);

        indexTotalCostLbl = GuiComponentFactory.getInstance().createBasicLabel();
        indexTotalCostLbl.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        indexTotalCostLbl.setText("Total: -");
        indexTotalCostLbl.setSize(250, 30);
        indexTotalCostLbl.setLocation((mainPanelWidth / 2) - 250, 130);
        indexFormPanel.add(indexTotalCostLbl);

        indexCashAccountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        indexCashAccountLbl.setText("purchased from");
        indexCashAccountLbl.setSize(175, 30);
        indexCashAccountLbl.setLocation((mainPanelWidth / 2) - 250, 110);
        indexFormPanel.add(indexCashAccountLbl);

        cashAccountList = PortfolioController.getInstance().getCashAccountsList();

        indexCashAccountCB = GuiComponentFactory.getInstance().createComboBox(cashAccountList);
        indexCashAccountCB.addActionListener(this);
        indexCashAccountCB.setSize(500, 50);
        indexCashAccountCB.setLocation((mainPanelWidth / 2) - 250, 145);
        indexFormPanel.add(indexCashAccountCB);

        indexAddBtn = GuiComponentFactory.getInstance().createBasicButton();
        indexAddBtn.setText("Add Index Share");
        indexAddBtn.setSize(150, 40);
        indexAddBtn.setLocation((mainPanelWidth / 2) - 75, 250);
        indexAddBtn.addActionListener(this);
        indexFormPanel.add(indexAddBtn);
    }

    /**
     * Setup the scroll pane containing the list of market shares.
     */
    private void setupScrollPane() {

        if (scrollPane != null)
            equityLeftPanel.remove(scrollPane);

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
        scrollPane.setLocation((300 / 2) - 125, 65);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        equityLeftPanel.add(scrollPane);

        if (marketItems != null) {

            for (int x = 0; x < marketItems.size(); x++) {
                JPanel panel = marketItems.get(x);

                panel.setLocation(0, 20 + (48 * x));
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseClicked(e);

                        String stock = ((JLabel) panel.getComponent(0)).getText();
                        String price = df.format(MarketController.getInstance().getMarket().getStockPrice(stock));

                        chosenStockLbl.setText("Stock: " + stock);
                        equityPriceLbl.setText("Price: $ " + price);
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
        equityLeftPanel.repaint();
    }

    /**
     * Show the cash account form in the view.
     */
    private void showCashForm() {
        mainPanel.remove(equityFormPanel);
        mainPanel.remove(indexFormPanel);
        mainPanel.add(cashFormPanel);
        mainPanel.repaint();
        this.resize();

        GuiController.getInstance().getWindow().getRootPane().setDefaultButton(cashAddBtn);

        formShown = 0;
    }

    /**
     * Show the equity form in the view.
     */
    private void showEquityForm() {
        mainPanel.remove(cashFormPanel);
        mainPanel.remove(indexFormPanel);
        mainPanel.add(equityFormPanel);
        mainPanel.repaint();
        this.resize();

        GuiController.getInstance().getWindow().getRootPane().setDefaultButton(equityAddBtn);

        formShown = 1;
    }

    /**
     * Show the index share form in the view.
     */
    private void showIndexForm() {
        mainPanel.remove(cashFormPanel);
        mainPanel.remove(equityFormPanel);
        mainPanel.add(indexFormPanel);
        mainPanel.repaint();
        this.resize();

        GuiController.getInstance().getWindow().getRootPane().setDefaultButton(indexAddBtn);

        formShown = 2;
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

        holdingTypeLbl.setLocation((mainPanelWidth / 2) - 150, 20);
        holdingTypeCB.setLocation((mainPanelWidth / 2) - 150, 55);
        topSep.setSize((mainPanelWidth - 100), 30);
        topSep.setLocation((mainPanelWidth / 2) - ((mainPanelWidth - 100) / 2), 130);

        cashFormPanel.setSize(mainPanelWidth, mainPanelHeight - 150);
        cashNameLbl.setLocation((mainPanelWidth / 2) - 200, 0);
        cashNameField.setLocation((mainPanelWidth / 2) - 200, 35);
        cashAmountLbl.setLocation((mainPanelWidth / 2) - 200, 95);
        cashAmountField.setLocation((mainPanelWidth / 2) - 200, 130);
        cashAddBtn.setLocation((mainPanelWidth / 2) - 87, 210);

        equityFormPanel.setSize(mainPanelWidth, mainPanelHeight - 150);
        int rightPanelWidth = 300;
        equityRightPanel.setLocation((mainPanelWidth / 2), 0);
        equityAmountLbl.setLocation((rightPanelWidth / 2) - 62, 0);
        equityAmountField.setLocation((rightPanelWidth / 2) - 62, 35);
        vertSep.setSize(10, mainPanelHeight - 300);
        cashAccountLbl.setLocation((rightPanelWidth / 2) - 87, 150);
        cashAccountCB.setLocation((rightPanelWidth / 2) - 125, 185);
        equityAddBtn.setLocation((rightPanelWidth / 2) - 62, 260);

        int leftPanelWidth = 300;
        equityLeftPanel.setLocation((mainPanelWidth / 2) - 300, 0);
        searchField.setLocation((leftPanelWidth / 2) - 125, 0);
        scrollPane.setSize(270, mainPanelHeight - 400);
        scrollPane.setLocation((leftPanelWidth / 2) - 125, 110);
        scrollList.setSize(250, (48 * marketItems.size()));

        int width = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(24.0f)).stringWidth(messageLbl.getText());
        messageLbl.setSize(width, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), 130);

        indexFormPanel.setLocation(0, 165);
        indexNameLbl.setLocation((mainPanelWidth / 2) - 250, 0);
        indexNameCB.setLocation((mainPanelWidth / 2) - 250, 40);
        indexAmountLbl.setLocation((mainPanelWidth / 2) - 250 + 410, 0);
        indexAmountField.setLocation((mainPanelWidth / 2) - 250 + 410, 40);
        indexCashAccountLbl.setLocation((mainPanelWidth / 2) - 250, 170);
        indexCashAccountCB.setLocation((mainPanelWidth / 2) - 250, 205);
        indexAddBtn.setLocation((mainPanelWidth / 2) - 75, 280);

        this.repaint();
    }

    @Override
    public void showMessage(String message, int type) {
        switch (type) {
            case View.INFO_MESSAGE:
                messageLbl.setForeground(new Color(34, 34, 34));
                break;

            case View.ERROR_MESSAGE:
                messageLbl.setForeground(new Color(155, 0, 5));
                break;

            case View.SUCCESS_MESSAGE:
                messageLbl.setForeground(new Color(0, 116, 13));
                break;
        }

        messageLbl.setText(message);
        int width = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(24.0f)).stringWidth(message);
        messageLbl.setSize(width, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), 150);
        this.repaint();
        GuiController.getInstance().refreshViewWithToolbar();
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

        if (e.getSource() == cashAddBtn) {
            if (cashNameField.getText().equals("")) {
                showMessage("Please enter a unique name for the account", View.ERROR_MESSAGE);
                return;
            }

            if (!isNumeric(cashAmountField.getText())) {
                showMessage("Please enter a number for initial amount", View.ERROR_MESSAGE);
                return;
            }

            UndoRedoController.getInstance().DepositCashAccount(cashNameField.getText(), cashAmountField.getText());
        }

        if (e.getSource() == equityAddBtn) {
            if (chosenStockLbl.getText().equals("Stock: None")) {
                showMessage("Please select a stock", View.ERROR_MESSAGE);
                return;
            }

            if (!isNumeric(equityAmountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (equityAmountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            String str = null;
            if (!((String) cashAccountCB.getSelectedItem()).equals("Outside System"))
                str = (String) cashAccountCB.getSelectedItem();
            UndoRedoController.getInstance().BuyEquity(chosenStockLbl.getText(), equityAmountField.getText(), str);
        }

        if (e.getSource() == holdingTypeCB) {
            JComboBox cb = (JComboBox)e.getSource();
            String choice = (String)cb.getSelectedItem();

            if (choice.equals("Cash Account") && formShown != 0) {
                showCashForm();

                GuiController.getInstance().refreshViewWithToolbar();
            }
            else if (choice.equals("Equity") && formShown != 1) {

                cashAccountList = PortfolioController.getInstance().getCashAccountsList();

                equityRightPanel.remove(cashAccountCB);
                cashAccountCB = GuiComponentFactory.getInstance().createComboBox(cashAccountList);
                cashAccountCB.addActionListener(this);
                cashAccountCB.setSize(250, 50);
                cashAccountCB.setLocation((300 / 2) - 125, 135);
                equityRightPanel.add(cashAccountCB);

                showEquityForm();

                GuiController.getInstance().refreshViewWithToolbar();
            }
            else if (choice.equals("Index Share") && formShown != 2) {

                cashAccountList = PortfolioController.getInstance().getCashAccountsList();

                indexFormPanel.remove(indexCashAccountCB);
                indexCashAccountCB = GuiComponentFactory.getInstance().createComboBox(cashAccountList);
                indexCashAccountCB.addActionListener(this);
                indexCashAccountCB.setSize(500, 50);
                indexCashAccountCB.setLocation((mainPanelWidth / 2) - 250, 145);
                indexFormPanel.add(indexCashAccountCB);

                showIndexForm();

                GuiController.getInstance().refreshViewWithToolbar();
            }
        }

        if (e.getSource() == searchField) {
            System.out.println("Search");
        }

        if (e.getSource() == indexNameCB) {
            JComboBox cb = (JComboBox)e.getSource();
            String choice = (String)cb.getSelectedItem();

            float price = MarketController.getInstance().getMarket().getSectorPrice(choice);
            indexPriceLbl.setText("Price: $ " + df.format(price));
        }

        if (e.getSource() == indexAddBtn) {

            if (!isNumeric(indexAmountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (indexAmountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            String str = null;
            if (!(indexCashAccountCB.getSelectedItem()).equals("Outside System"))
                str = (String) indexCashAccountCB.getSelectedItem();
            UndoRedoController.getInstance().BuyIndexShare((String)indexNameCB.getSelectedItem(), indexAmountField.getText(), str);
        }
    }
}
