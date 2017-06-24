package fpts.view;

import fpts.controller.*;
import fpts.model.CashAccount;
import fpts.model.Equity;
import fpts.model.Holding;
import fpts.model.IndexShare;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

/**
 * Created by Ryan on 3/11/2016.
 *
 * A detail view for a holding.  Shows information specific to a single holding.
 */
public class HoldingDetailView extends View {

    // Top panel stuff
    private int topPanelWidth = 0;
    private int topPanelHeight = 0;
    private int mainPanelWidth = 0;
    private int mainPanelHeight = 0;
    private int sidePanelWidth = 0;
    private int sidePanelHeight = 0;

    private JPanel topPanel;
    private JPanel mainPanel;
    private JLabel titleLbl;
    private JButton backBtn;
    // End top panel stuff

    private JLabel valueLblLbl;
    private JLabel valueLbl;
    private JLabel totalValueDollarLbl;
    private JLabel typeLblLbl;
    private JLabel typeLbl;
    private int typeWidth = 0;

    private JPanel sidePanel;
    private JLabel amountLbl;
    private JTextField amountField;
    private JButton depositBtn;
    private JButton withdrawBtn;
    private JButton deleteBtn;
    private JButton addSharesBtn;
    private JButton removeSharesBtn;

    private JLabel pricePerLblLbl;
    private JLabel pricePerLbl;
    private JLabel numSharesLblLbl;
    private JLabel numSharesLbl;
    private JLabel tickerSymbolLblLbl;
    private JLabel tickerSymbolLbl;
    private JLabel indexLblLbl;
    private JLabel indexLbl;
    private JLabel sectorLblLbl;
    private JLabel sectorLbl;
    private JLabel messageLbl;
    private String[] cashAccountList;
    private JLabel cashAccountLbl;
    private JComboBox cashAccountCB;

    private JButton addIndexBtn;
    private JButton removeIndexBtn;

    private DecimalFormat df = new DecimalFormat("###,###,###,###.##");

    private Holding holding;

    @Override
    public void addGuiComponents() {
        holding = (Holding)context.getData("holding");

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
        titleLbl.setText(holding.getName());
        titleLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
        titleLbl.setSize(topPanelWidth - 135, 50);
        titleLbl.setLocation(35, 40);
        topPanel.add(titleLbl);

        backBtn = GuiComponentFactory.getInstance().createBasicButton();
        backBtn.setText("Portfolio");
        backBtn.addActionListener(this);
        backBtn.setSize(100, 30);
        backBtn.setLocation(topPanelWidth - 100 - 35, 52);
        topPanel.add(backBtn);
        // End top panel stuff

        valueLbl = GuiComponentFactory.getInstance().createBasicLabel();
        float totalValue = holding.getValue();
        valueLbl.setText(df.format(totalValue));
        valueLbl.setFont(ResourceLoader.robotoFont.deriveFont(50.0f));
        int totalValueWidth = valueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                .stringWidth(valueLbl.getText());
        valueLbl.setSize(totalValueWidth, 50);
        valueLbl.setLocation(mainPanelWidth - 35 - (totalValueWidth), 160);
        mainPanel.add(valueLbl);

        totalValueDollarLbl = GuiComponentFactory.getInstance().createBasicLabel();
        totalValueDollarLbl.setText("$");
        totalValueDollarLbl.setFont(ResourceLoader.robotoFont.deriveFont(35.0f));
        totalValueDollarLbl.setForeground(new Color(76, 175, 80));
        int dollarWidth = totalValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(35.0f))
                .stringWidth(totalValueDollarLbl.getText());
        totalValueDollarLbl.setSize(dollarWidth, 40);
        totalValueDollarLbl.setLocation(mainPanelWidth - 35 - (totalValueWidth) - 10 - dollarWidth, 170);
        mainPanel.add(totalValueDollarLbl);

        valueLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        valueLblLbl.setText("value");
        valueLblLbl.setLocation(mainPanelWidth - 35 - 57, 130);
        mainPanel.add(valueLblLbl);

        typeLbl = GuiComponentFactory.getInstance().createBasicLabel();
        typeLbl.setFont(ResourceLoader.robotoFont.deriveFont(50.0f));
        typeLbl.setLocation(35, 45);
        mainPanel.add(typeLbl);

        typeLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        typeLblLbl.setText("type");
        typeLblLbl.setLocation(35, 10);
        mainPanel.add(typeLblLbl);

        sidePanel = new JPanel();
        sidePanel.setBackground(new Color(209, 207, 207));
        sidePanel.setLayout(null);
        sidePanelWidth = 275;
        sidePanelHeight = mainPanelHeight;
        sidePanel.setSize(sidePanelWidth, sidePanelHeight);
        sidePanel.setLocation(GuiController.windowWidth - sidePanelWidth, 0);
        mainPanel.add(sidePanel);

        mainPanel.setComponentZOrder(sidePanel, 0);
        mainPanel.setComponentZOrder(valueLbl, 1);

        cashAccountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        cashAccountLbl.setSize(250, 30);
        sidePanel.add(cashAccountLbl);

        cashAccountList = PortfolioController.getInstance().getCashAccountsList();
        cashAccountCB = GuiComponentFactory.getInstance().createComboBox(cashAccountList);
        cashAccountCB.addActionListener(this);
        cashAccountCB.setSize(200, 50);
        sidePanel.add(cashAccountCB);

        if (context.getData("type").equals("CashAccount")) {
            typeLbl.setText("Cash Account");
            typeWidth = typeLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                    .stringWidth(typeLbl.getText());

            amountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            amountLbl.setText("amount");
            amountLbl.setLocation((sidePanelWidth / 2) - 100, 10);
            sidePanel.add(amountLbl);

            amountField = GuiComponentFactory.getInstance().createBasicTextField();
            amountField.setText("0");
            amountField.setSize(200, 40);
            amountField.setLocation((sidePanelWidth / 2) - 100, 45);
            sidePanel.add(amountField);

            cashAccountLbl.setText("transfer to");
            cashAccountLbl.setLocation((sidePanelWidth / 2) - 100, 145);
            cashAccountCB.setLocation((sidePanelWidth / 2) - 100, 185);

            depositBtn = GuiComponentFactory.getInstance().createBasicButton();
            depositBtn.setText("Deposit");
            depositBtn.addActionListener(this);
            depositBtn.setSize(200, 40);
            depositBtn.setLocation((sidePanelWidth / 2) - 100, 95);
            sidePanel.add(depositBtn);

            withdrawBtn = GuiComponentFactory.getInstance().createBasicButton();
            withdrawBtn.setText("Withdraw");
            withdrawBtn.addActionListener(this);
            withdrawBtn.setSize(200, 40);
            withdrawBtn.setLocation((sidePanelWidth / 2) - 100, 145);
            sidePanel.add(withdrawBtn);

            deleteBtn = GuiComponentFactory.getInstance().createBasicButton();
            deleteBtn.setText("Delete");
            deleteBtn.addActionListener(this);
            deleteBtn.setSize(200, 40);
            deleteBtn.setLocation((sidePanelWidth / 2) - 100, sidePanelHeight - 40 - 35);
            sidePanel.add(deleteBtn);
        }
        else if (context.getData("type").equals("Equity")) {
            totalValue = ((Equity)holding).getTotalValue();
            valueLbl.setText(df.format(totalValue));

            typeLbl.setText("Equity");
            typeWidth = typeLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                    .stringWidth(typeLbl.getText());

            amountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            amountLbl.setText("# of shares");
            amountLbl.setLocation((sidePanelWidth / 2) - 100, 10);
            sidePanel.add(amountLbl);

            amountField = GuiComponentFactory.getInstance().createBasicTextField();
            amountField.setText("0");
            amountField.setSize(200, 40);
            amountField.setLocation((sidePanelWidth / 2) - 100, 45);
            sidePanel.add(amountField);

            addSharesBtn = GuiComponentFactory.getInstance().createBasicButton();
            addSharesBtn.setText("Add");
            addSharesBtn.addActionListener(this);
            addSharesBtn.setSize(200, 40);
            addSharesBtn.setLocation((sidePanelWidth / 2) - 100, 95);
            sidePanel.add(addSharesBtn);

            cashAccountLbl.setText("funds to/from");
            cashAccountLbl.setLocation((sidePanelWidth / 2) - 100, 145);
            cashAccountCB.setLocation((sidePanelWidth / 2) - 100, 185);

            removeSharesBtn = GuiComponentFactory.getInstance().createBasicButton();
            removeSharesBtn.setText("Remove");
            removeSharesBtn.addActionListener(this);
            removeSharesBtn.setSize(200, 40);
            removeSharesBtn.setLocation((sidePanelWidth / 2) - 100, 205);
            sidePanel.add(removeSharesBtn);

            pricePerLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            pricePerLblLbl.setText("price per share");
            pricePerLblLbl.setLocation(35, 205);
            mainPanel.add(pricePerLblLbl);

            pricePerLbl = GuiComponentFactory.getInstance().createBasicLabel();
            pricePerLbl.setText("$ " + df.format((((Equity)holding).getValue())));
            pricePerLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
            pricePerLbl.setLocation(35, 245);
            pricePerLbl.setSize(400, 50);
            mainPanel.add(pricePerLbl);

            int pricePerWidth = pricePerLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(40.0f))
                    .stringWidth(pricePerLbl.getText());

            numSharesLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            numSharesLblLbl.setText("# of shares");
            numSharesLblLbl.setLocation(35 + 100 + pricePerWidth + 20, 205);
            mainPanel.add(numSharesLblLbl);

            numSharesLbl = GuiComponentFactory.getInstance().createBasicLabel();
            numSharesLbl.setText(Float.toString(((Equity)holding).getShares()));
            numSharesLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
            numSharesLbl.setLocation(35 + 100 + pricePerWidth + 20, 245);
            numSharesLbl.setSize(400, 50);
            mainPanel.add(numSharesLbl);

            tickerSymbolLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            tickerSymbolLblLbl.setText("ticker symbol");
            tickerSymbolLblLbl.setLocation(35, 295);
            mainPanel.add(tickerSymbolLblLbl);

            tickerSymbolLbl = GuiComponentFactory.getInstance().createBasicLabel();
            tickerSymbolLbl.setText(((Equity)holding).getTickerSymbol());
            tickerSymbolLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
            tickerSymbolLbl.setLocation(35, 330);
            tickerSymbolLbl.setSize(400, 50);
            mainPanel.add(tickerSymbolLbl);

            indexLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            indexLblLbl.setText("index");
            indexLblLbl.setLocation(35, 375);
            mainPanel.add(indexLblLbl);

            indexLbl = GuiComponentFactory.getInstance().createBasicLabel();
            indexLbl.setText(((Equity)holding).getIndex());
            indexLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
            indexLbl.setLocation(35, 410);
            indexLbl.setSize(400, 50);
            mainPanel.add(indexLbl);

            sectorLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            sectorLblLbl.setText("sector");
            sectorLblLbl.setLocation(35, 455);
            mainPanel.add(sectorLblLbl);

            sectorLbl = GuiComponentFactory.getInstance().createBasicLabel();
            sectorLbl.setText(((Equity)holding).getSector());
            sectorLbl.setSize(400, 50);
            sectorLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
            sectorLbl.setLocation(35, 490);
            sectorLbl.setSize(400, 50);
            mainPanel.add(sectorLbl);
        }
        else if (context.getData("type").equals("IndexShare")) {
            typeLbl.setText("Index Share");
            typeWidth = typeLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                    .stringWidth(typeLbl.getText());

            amountLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            amountLbl.setText("# of shares");
            amountLbl.setLocation((sidePanelWidth / 2) - 100, 10);
            sidePanel.add(amountLbl);

            amountField = GuiComponentFactory.getInstance().createBasicTextField();
            amountField.setText("0");
            amountField.setSize(200, 40);
            amountField.setLocation((sidePanelWidth / 2) - 100, 45);
            sidePanel.add(amountField);

            addIndexBtn = GuiComponentFactory.getInstance().createBasicButton();
            addIndexBtn.setText("Add");
            addIndexBtn.addActionListener(this);
            addIndexBtn.setSize(200, 40);
            addIndexBtn.setLocation((sidePanelWidth / 2) - 100, 95);
            sidePanel.add(addIndexBtn);

            cashAccountLbl.setText("funds to/from");
            cashAccountLbl.setLocation((sidePanelWidth / 2) - 100, 145);
            cashAccountCB.setLocation((sidePanelWidth / 2) - 100, 185);

            removeIndexBtn = GuiComponentFactory.getInstance().createBasicButton();
            removeIndexBtn.setText("Remove");
            removeIndexBtn.addActionListener(this);
            removeIndexBtn.setSize(200, 40);
            removeIndexBtn.setLocation((sidePanelWidth / 2) - 100, 205);
            sidePanel.add(removeIndexBtn);

            numSharesLblLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
            numSharesLblLbl.setText("# of shares");
            numSharesLblLbl.setLocation(35, 210);
            mainPanel.add(numSharesLblLbl);

            numSharesLbl = GuiComponentFactory.getInstance().createBasicLabel();
            numSharesLbl.setText(Float.toString(((IndexShare)holding).getShares()));
            numSharesLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
            numSharesLbl.setLocation(35, 245);
            numSharesLbl.setSize(400, 50);
            mainPanel.add(numSharesLbl);
        }

        typeLbl.setSize(typeWidth, 60);
        typeLbl.setLocation(mainPanelWidth - 35 - (typeWidth), 45);

        messageLbl = new JLabel();
        messageLbl.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        messageLbl.setText("");
        messageLbl.setForeground(new Color(69, 69, 69));
        messageLbl.setSize(300, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - 150, 10);
        mainPanel.add(messageLbl);
    }

    @Override
    public void removeGuiComponents() {

    }

    @Override
    public void resize() {
        // Top panel stuff
        sidePanelHeight = mainPanelHeight;
        sidePanelWidth = 275;
        topPanelWidth = GuiController.windowWidth;
        mainPanelWidth = GuiController.windowWidth;
        mainPanelHeight = GuiController.windowHeight - topPanelHeight;
        topPanel.setSize(topPanelWidth, topPanelHeight);
        mainPanel.setSize(mainPanelWidth, mainPanelHeight);
        mainPanel.setLocation(0, topPanelHeight);
        backBtn.setLocation(topPanelWidth - 100 - 35, 52);
        // End top panel stuff

        sidePanel.setSize(sidePanelWidth, sidePanelHeight);
        sidePanel.setLocation(GuiController.windowWidth - sidePanelWidth, 0);

        typeLblLbl.setLocation(35, 10);
        typeLbl.setLocation(35, 45);

        float totalValueFontSize = determineValueSize();
        valueLbl.setFont(ResourceLoader.robotoFont.deriveFont(totalValueFontSize));
        int totalValueWidth = valueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                .stringWidth(valueLbl.getText());
        int dollarWidth = totalValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(35.0f))
                .stringWidth(totalValueDollarLbl.getText());
        valueLbl.setSize(totalValueWidth, 50);

        if (context.getData("type").equals("CashAccount")) {
            valueLbl.setLocation(35 + (dollarWidth) + 10, 145);
            totalValueDollarLbl.setSize(dollarWidth, 40);
            totalValueDollarLbl.setLocation(35, 150);
            valueLblLbl.setLocation(35, 110);

            amountLbl.setLocation((sidePanelWidth / 2) - 100, 10);
            amountField.setLocation((sidePanelWidth / 2) - 100, 45);
            cashAccountLbl.setLocation((sidePanelWidth / 2) - 100, 95);
            cashAccountCB.setLocation((sidePanelWidth / 2) - 100, 125);
            depositBtn.setLocation((sidePanelWidth / 2) - 100, 215);
            withdrawBtn.setLocation((sidePanelWidth / 2) - 100, 270);
            deleteBtn.setLocation((sidePanelWidth / 2) - 100, sidePanelHeight - 40 - 35);
        }
        else if (context.getData("type").equals("Equity")) {
            valueLbl.setLocation(35 + (dollarWidth) + 10, 145);
            totalValueDollarLbl.setSize(dollarWidth, 40);
            totalValueDollarLbl.setLocation(35, 150);
            valueLblLbl.setLocation(35, 110);

            amountLbl.setLocation((sidePanelWidth / 2) - 100, 10);
            amountField.setLocation((sidePanelWidth / 2) - 100, 45);
            cashAccountLbl.setLocation((sidePanelWidth / 2) - 100, 95);
            cashAccountCB.setLocation((sidePanelWidth / 2) - 100, 125);
            addSharesBtn.setLocation((sidePanelWidth / 2) - 100, 215);
            removeSharesBtn.setLocation((sidePanelWidth / 2) - 100, 270);
        }
        else if (context.getData("type").equals("IndexShare")) {
            valueLbl.setLocation(35 + (dollarWidth) + 10, 145);
            totalValueDollarLbl.setSize(dollarWidth, 40);
            totalValueDollarLbl.setLocation(35, 150);
            valueLblLbl.setLocation(35, 110);

            amountLbl.setLocation((sidePanelWidth / 2) - 100, 10);
            amountField.setLocation((sidePanelWidth / 2) - 100, 45);
            cashAccountLbl.setLocation((sidePanelWidth / 2) - 100, 95);
            cashAccountCB.setLocation((sidePanelWidth / 2) - 100, 125);
            addIndexBtn.setLocation((sidePanelWidth / 2) - 100, 215);
            removeIndexBtn.setLocation((sidePanelWidth / 2) - 100, 270);
        }

        int width = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(24.0f)).stringWidth(messageLbl.getText());
        messageLbl.setSize(width, 40);
        messageLbl.setLocation(((mainPanelWidth - 275) / 2) - (width / 2), 10);
    }

    /**
     * Determine the font size that the total value label should be based on the screen size.
     * @return float
     */
    private float determineValueSize() {
        float totalValueFontSize = 50.0f;
        if (valueLbl.getText().length() == 9) {
            if (topPanelWidth > 980)
                totalValueFontSize = 50.0f;
            else
                totalValueFontSize = 45.0f;
        }
        else if (valueLbl.getText().length() == 10) {
            if (topPanelWidth > 1000)
                totalValueFontSize = 50.0f;
            else
                totalValueFontSize = 40.0f;
        }
        else if (valueLbl.getText().length() == 12) {
            if (topPanelWidth > 1010)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 970)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 35.0f;
        }
        else if (valueLbl.getText().length() == 13) {
            if (topPanelWidth > 1050)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 990)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 32.0f;
        }
        else if (valueLbl.getText().length() == 14) {
            if (topPanelWidth > 1100)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 1010)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 30.0f;
        }
        else if (valueLbl.getText().length() >= 16) {
            if (topPanelWidth > 1200)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 1050)
                totalValueFontSize = 40.0f;
            else if (topPanelWidth > 1000)
                totalValueFontSize = 35.0f;
            else if (topPanelWidth > 900)
                totalValueFontSize = 30.0f;
            else
                totalValueFontSize = 25.0f;
        }
        else {
            totalValueFontSize = 50.0f;
        }

        return totalValueFontSize;
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
        messageLbl.setLocation((mainPanelWidth / 2) - (width / 2), 10);
        this.resize();
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

        // Back button
        if (e.getSource() == backBtn) {
            PortfolioController.getInstance().showPortfolioView();
        }

        // Withdraw button
        if (e.getSource() == withdrawBtn) {

            if (!isNumeric(amountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (amountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            if (((String)cashAccountCB.getSelectedItem()).equals("Outside System"))
                UndoRedoController.getInstance().WithdrawCashAccount(holding.getName(), amountField.getText());
            else
                UndoRedoController.getInstance().Transfer(holding.getName(),
                        (String) cashAccountCB.getSelectedItem(), amountField.getText());

            CashAccount ca = (CashAccount) UserController.getInstance().getAuthedUser().getPortfolio()
                    .getCashAccount(holding.getName());
            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            valueLbl.setText(df.format(ca.getValue()));
            valueLbl.repaint();

            showMessage("Update successful", View.SUCCESS_MESSAGE);

            this.resize();
        }

        // Deposit button
        if (e.getSource() == depositBtn) {

            if (!isNumeric(amountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (amountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            UndoRedoController.getInstance().DepositCashAccount(holding.getName(), amountField.getText());
            CashAccount ca = (CashAccount) UserController.getInstance().getAuthedUser().getPortfolio()
                    .getCashAccount(holding.getName());

            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            valueLbl.setText(df.format(ca.getValue()));
            valueLbl.repaint();

            showMessage("Update successful", View.SUCCESS_MESSAGE);

            this.resize();
        }

        // Delete buuton
        if (e.getSource() == deleteBtn) {
            UndoRedoController.getInstance().RemoveCashAccount(holding.getName(), Float.toString(holding.getValue()));
        }

        // Add shares button
        if (e.getSource() == addSharesBtn) {

            if (!isNumeric(amountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (amountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            String cash = null;

            if (!((String)cashAccountCB.getSelectedItem()).equals("Outside System"))
                cash = (String)cashAccountCB.getSelectedItem();

            UndoRedoController.getInstance().BuyEquity(((Equity) holding).getTickerSymbol(), amountField.getText(),
                    cash);
            Equity equity = UserController.getInstance().getAuthedUser().getPortfolio()
                    .getEquity(((Equity) holding).getTickerSymbol());

            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            float value = equity.getTotalValue();
            valueLbl.setText(df.format(value));
            numSharesLbl.setText(Float.toString(equity.getShares()));

            showMessage("Update successful", View.SUCCESS_MESSAGE);

            this.resize();
        }

        // Remove shares button
        if (e.getSource() == removeSharesBtn) {

            if (!isNumeric(amountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (amountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            String cash = null;

            if (!((String)cashAccountCB.getSelectedItem()).equals("Outside System"))
                cash = (String)cashAccountCB.getSelectedItem();

            UndoRedoController.getInstance().SellEquity(((Equity) holding).getTickerSymbol(), amountField.getText(),
                    cash);

            Equity equity = UserController.getInstance().getAuthedUser().getPortfolio()
                    .getEquity(((Equity) holding).getTickerSymbol());

            if (equity == null) {
                showMessage("Update successful", View.SUCCESS_MESSAGE);
                this.resize();
                return;
            }

            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            float value = equity.getTotalValue();
            valueLbl.setText(df.format(value));
            numSharesLbl.setText(Float.toString(equity.getShares()));

            showMessage("Update successful", View.SUCCESS_MESSAGE);

            this.resize();
        }

        // Add index shares button
        if (e.getSource() == addIndexBtn) {

            if (!isNumeric(amountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (amountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            String cash = null;

            if (!((String)cashAccountCB.getSelectedItem()).equals("Outside System"))
                cash = (String)cashAccountCB.getSelectedItem();

            UndoRedoController.getInstance().BuyIndexShare(holding.getName(), amountField.getText(),
                    cash);
            IndexShare index = UserController.getInstance().getAuthedUser().getPortfolio()
                    .getIndexShare(holding.getName());

            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            float value = index.getValue();
            valueLbl.setText(df.format(value));
            numSharesLbl.setText(Float.toString(index.getShares()));

            showMessage("Update successful", View.SUCCESS_MESSAGE);

            this.resize();
        }

        // Remove shares button
        if (e.getSource() == removeIndexBtn) {

            if (!isNumeric(amountField.getText())) {
                showMessage("Please enter a valid number", View.ERROR_MESSAGE);
                return;
            }

            if (amountField.getText().equals("0")) {
                showMessage("Please enter a number greater than 0", View.ERROR_MESSAGE);
                return;
            }

            String cash = null;

            if (!((String)cashAccountCB.getSelectedItem()).equals("Outside System"))
                cash = (String)cashAccountCB.getSelectedItem();


            UndoRedoController.getInstance().SellIndexShare(holding.getName(), amountField.getText(),
                    cash);

            IndexShare index = UserController.getInstance().getAuthedUser().getPortfolio()
                    .getIndexShare(holding.getName());

            if (index == null) {
                showMessage("Update successful", View.SUCCESS_MESSAGE);
                this.resize();
                return;
            }

            DecimalFormat df = new DecimalFormat("###,###,###,###.##");
            float value = index.getValue();
            valueLbl.setText(df.format(value));
            numSharesLbl.setText(Float.toString(index.getShares()));

            showMessage("Update successful", View.SUCCESS_MESSAGE);

            this.resize();
        }
    }
}
