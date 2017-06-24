package fpts.view;

import fpts.controller.*;
import fpts.model.User;
import fpts.toolkit.Export;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Ryan on 3/8/2016.
 * <p>
 * The "hub" view so to speak.  Everything can be accessed from this view.  It shows the information about a user's
 * portfolio, as well as buttons to navigate to other parts of the application.
 */
public class PortfolioView extends View {

    private int topPanelWidth = 0;
    private int topPanelHeight = 0;
    private int sidePanelWidth = 0;
    private int sidePanelHeight = 0;
    private int mainPanelWidth = 0;
    private int mainPanelHeight = 0;

    // Panels
    private JPanel topPanel;
    private JPanel sidePanel;
    private JPanel mainPanel;

    // Top panel content
    private JLabel totalValueDollarLbl;
    private JLabel totalValueLbl;
    private JTextField searchField;
    private JButton searchBtn;

    // Side panel content
    private JButton addHoldingBtn;
    private JButton watchlistBtn;
    private JButton simulateBtn;
    private JButton transactionsBtn;
    private JButton exportBtn;
    private JButton importBtn;

    // Main panel content
    private JLabel nameLbl;
    private JLabel numSharesLbl;
    private JLabel pricePerLbl;
    private JLabel totalHoldingLbl;
    private JSeparator holdingsSep;

    private JScrollPane scrollPane;
    private JPanel scrollList;
    private ArrayList<JPanel> holdingItems = null;

    private DecimalFormat df = new DecimalFormat("###,###,###,###.##");

    @Override
    public void initialize(Context context) {
        super.initialize(context);

        GuiController.getInstance().showToolbar();

        holdingItems = new ArrayList<>();
    }

    @Override
    public void addGuiComponents() {

        // Setup the panels
        topPanel = new JPanel();
        topPanel.setBackground(new Color(219, 219, 219));
        topPanel.setLayout(null);
        topPanelWidth = GuiController.windowWidth;
        topPanelHeight = 110;
        topPanel.setSize(topPanelWidth, topPanelHeight);
        topPanel.setLocation(0, 0);
        this.add(topPanel);

        sidePanel = new JPanel();
        sidePanel.setBackground(new Color(209, 207, 207));
        sidePanel.setLayout(null);
        sidePanelWidth = 275;
        sidePanelHeight = GuiController.windowHeight - topPanelHeight;
        sidePanel.setSize(sidePanelWidth, sidePanelHeight);
        sidePanel.setLocation(GuiController.windowWidth - sidePanelWidth, topPanelHeight);
        this.add(sidePanel);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(239, 239, 239));
        mainPanel.setLayout(null);
        mainPanelWidth = GuiController.windowWidth - 275;
        mainPanelHeight = GuiController.windowHeight - topPanelHeight;
        mainPanel.setSize(mainPanelWidth, mainPanelHeight);
        mainPanel.setLocation(0, topPanelHeight);
        this.add(mainPanel);

        // Setup the top panel content
        totalValueLbl = GuiComponentFactory.getInstance().createBasicLabel();
        float totalValue = ((User)context.getData("user")).getPortfolio().getValue();
        totalValueLbl.setText(df.format(totalValue));
        totalValueLbl.setFont(ResourceLoader.robotoFont.deriveFont(50.0f));
        int totalValueWidth = totalValueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                .stringWidth(totalValueLbl.getText());
        totalValueLbl.setSize(totalValueWidth, 50);
        totalValueLbl.setLocation(topPanelWidth - 35 - (totalValueWidth), 40);
        topPanel.add(totalValueLbl);

        totalValueDollarLbl = GuiComponentFactory.getInstance().createBasicLabel();
        totalValueDollarLbl.setText("$");
        totalValueDollarLbl.setFont(ResourceLoader.robotoFont.deriveFont(35.0f));
        totalValueDollarLbl.setForeground(new Color(76, 175, 80));
        int dollarWidth = totalValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(35.0f))
                .stringWidth(totalValueDollarLbl.getText());
        totalValueDollarLbl.setSize(dollarWidth, 40);
        totalValueDollarLbl.setLocation(topPanelWidth - 35 - (totalValueWidth) - 10 - dollarWidth, 50);
        topPanel.add(totalValueDollarLbl);

        searchField = GuiComponentFactory.getInstance().createPlaceholderTextField("search holdings");
        searchField.addActionListener(this);
        searchField.setBackground(new Color(239, 239, 239));
        searchField.setSize(550, 50);
        searchField.setLocation(35, 42);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                holdingItems = PortfolioController.getInstance().searchHoldings(searchField.getText());
                setupScrollPane();
            }
        });
        topPanel.add(searchField);

        searchBtn = GuiComponentFactory.getInstance().createBasicButton();
        searchBtn.setText("");
        searchBtn.addActionListener(this);
        searchBtn.setSize(50, 50);
        searchBtn.setLocation(595, 42);
        searchBtn.setIcon(new ImageIcon("src/lib/res/magGlass.png"));
        topPanel.add(searchBtn);

        // Setup the side panel content
        addHoldingBtn = GuiComponentFactory.getInstance().createBasicButton();
        addHoldingBtn.setText("Add Holding");
        addHoldingBtn.addActionListener(this);
        addHoldingBtn.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        addHoldingBtn.setSize(200, 40);
        addHoldingBtn.setLocation((sidePanelWidth / 2) - 100, 20);
        sidePanel.add(addHoldingBtn);

        watchlistBtn = GuiComponentFactory.getInstance().createBasicButton();
        watchlistBtn.setText("Watchlist");
        watchlistBtn.addActionListener(this);
        watchlistBtn.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        watchlistBtn.setSize(200, 40);
        watchlistBtn.setLocation((sidePanelWidth / 2) - 100, 80);
        sidePanel.add(watchlistBtn);

        simulateBtn = GuiComponentFactory.getInstance().createBasicButton();
        simulateBtn.setText("Simulate");
        simulateBtn.addActionListener(this);
        simulateBtn.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        simulateBtn.setSize(200, 40);
        simulateBtn.setLocation((sidePanelWidth / 2) - 100, 140);
        sidePanel.add(simulateBtn);

        transactionsBtn = GuiComponentFactory.getInstance().createBasicButton();
        transactionsBtn.setText("Transaction History");
        transactionsBtn.addActionListener(this);
        transactionsBtn.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        transactionsBtn.setSize(200, 40);
        transactionsBtn.setLocation((sidePanelWidth / 2) - 100, 200);
        sidePanel.add(transactionsBtn);

        exportBtn = GuiComponentFactory.getInstance().createBasicButton();
        exportBtn.setText("Export Portfolio");
        exportBtn.addActionListener(this);
        exportBtn.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        exportBtn.setSize(200, 40);
        exportBtn.setLocation((sidePanelWidth / 2) - 100, 260);
        sidePanel.add(exportBtn);

        importBtn = GuiComponentFactory.getInstance().createBasicButton();
        importBtn.setText("Import Portfolio");
        importBtn.addActionListener(this);
        importBtn.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        importBtn.setSize(200, 40);
        importBtn.setLocation((sidePanelWidth / 2) - 100, 320);
        sidePanel.add(importBtn);

        // Setup the main panel content
        nameLbl = GuiComponentFactory.getInstance().createBasicLabel();
        nameLbl.setText("name");
        nameLbl.setForeground(new Color(49, 49, 49));
        nameLbl.setSize(75, 30);
        nameLbl.setLocation(35, 10);
        mainPanel.add(nameLbl);

        numSharesLbl = GuiComponentFactory.getInstance().createBasicLabel();
        numSharesLbl.setText("# of shares");
        numSharesLbl.setForeground(new Color(49, 49, 49));
        numSharesLbl.setSize(125, 30);
        numSharesLbl.setLocation(225, 10);
        mainPanel.add(numSharesLbl);

        pricePerLbl = GuiComponentFactory.getInstance().createBasicLabel();
        pricePerLbl.setText("price per share");
        pricePerLbl.setForeground(new Color(49, 49, 49));
        pricePerLbl.setSize(175, 30);
        pricePerLbl.setLocation(390, 10);
        mainPanel.add(pricePerLbl);

        totalHoldingLbl = GuiComponentFactory.getInstance().createBasicLabel();
        totalHoldingLbl.setText("value");
        totalHoldingLbl.setForeground(new Color(49, 49, 49));
        totalHoldingLbl.setSize(150, 30);
        totalHoldingLbl.setLocation(595, 10);
        mainPanel.add(totalHoldingLbl);

        holdingsSep = GuiComponentFactory.getInstance().createSeparator();
        holdingsSep.setSize(750, 30);
        holdingsSep.setLocation(25, 50);
        mainPanel.add(holdingsSep);

        holdingItems = (ArrayList<JPanel>)context.getData("holdings");

        setupScrollPane();
    }

    /**
     * Allows the user to scroll through the portfolio items
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

                panel.setLocation(0, 20 + (48 * x));
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseClicked(e);

                        PortfolioController.getInstance().showHoldingDetailView(((JLabel) panel.getComponent(0)).getText());
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
            }
        }

        this.resize();
    }

    @Override
    public void removeGuiComponents() {

    }

    @Override
    public void resize() {
        topPanelWidth = GuiController.windowWidth;
        topPanelHeight = 110;
        sidePanelWidth = 275;
        sidePanelHeight = GuiController.windowHeight - topPanelHeight;
        mainPanelWidth = GuiController.windowWidth - 275;
        mainPanelHeight = GuiController.windowHeight - topPanelHeight;

        topPanel.setSize(topPanelWidth, topPanelHeight);
        sidePanel.setSize(sidePanelWidth, sidePanelHeight);
        sidePanel.setLocation(GuiController.windowWidth - sidePanelWidth, topPanelHeight);
        mainPanel.setSize(mainPanelWidth, mainPanelHeight);
        mainPanel.setLocation(0, topPanelHeight);

        float totalValueFontSize = determineValueSize();

        totalValueLbl.setFont(ResourceLoader.robotoFont.deriveFont(totalValueFontSize));
        int totalValueWidth = totalValueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(totalValueFontSize))
                .stringWidth(totalValueLbl.getText());
        totalValueLbl.setSize(totalValueWidth, 50);
        totalValueLbl.setLocation(topPanelWidth - 35 - (totalValueWidth), 40);

        totalValueDollarLbl.setFont(ResourceLoader.robotoFont.deriveFont(totalValueFontSize - 15));
        int dollarWidth = totalValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(totalValueFontSize - 15))
                .stringWidth(totalValueDollarLbl.getText());
        totalValueDollarLbl.setSize(dollarWidth, 40);
        totalValueDollarLbl.setLocation(topPanelWidth - 35 - (totalValueWidth) - 10 - dollarWidth, 50);

        if (topPanelWidth < (665 + totalValueWidth + 50 + dollarWidth)) {
            searchField.setSize(topPanelWidth - (totalValueWidth + dollarWidth + 110) - 60, 50);
            searchBtn.setLocation((topPanelWidth - (totalValueWidth + dollarWidth + 120)), 42);
        }

        scrollPane.setSize(mainPanelWidth - 50, mainPanelHeight - 200);
        scrollList.setSize(mainPanelWidth - 50, mainPanelHeight - 200);
    }

    /**
     * Determine the font size that the total value label should be based on the screen size.
     *
     * @return float
     */
    private float determineValueSize() {
        float totalValueFontSize = 50.0f;
        if (totalValueLbl.getText().length() == 9) {
            if (topPanelWidth > 980)
                totalValueFontSize = 50.0f;
            else
                totalValueFontSize = 45.0f;
        }
        else if (totalValueLbl.getText().length() == 10) {
            if (topPanelWidth > 1000)
                totalValueFontSize = 50.0f;
            else
                totalValueFontSize = 40.0f;
        }
        else if (totalValueLbl.getText().length() == 12) {
            if (topPanelWidth > 1010)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 970)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 35.0f;
        }
        else if (totalValueLbl.getText().length() == 13) {
            if (topPanelWidth > 1050)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 990)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 32.0f;
        }
        else if (totalValueLbl.getText().length() == 14) {
            if (topPanelWidth > 1100)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 1010)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 30.0f;
        }
        else if (totalValueLbl.getText().length() >= 16) {
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

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // If the addHoldingBtn was clicked, show the AddHoldingView
        if (e.getSource() == addHoldingBtn) {
            PortfolioController.getInstance().showAddHoldingView();
        }

        // If the watchlistBtn was clicked, show the WatchlistView
        if (e.getSource() == watchlistBtn) {
            WatchlistController.getInstance().showWatchlistView();
        }

        // If the simulateBtn was clicked, show the CreateSimulationView
        if (e.getSource() == simulateBtn) {
            SimulationController.getInstance().showCreateSimulationView();
        }

        // If the transactionsBtn was clicked, show the TransactionHistoryView
        if (e.getSource() == transactionsBtn) {
            TransactionController.getInstance().showTransactionHistoryView();
        }

        // If the exportBtn was clicked, export the portfolio to file
        if (e.getSource() == exportBtn) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setDialogTitle("Export");
            fc.setApproveButtonText("Export");

            int returnVal = fc.showSaveDialog(GuiController.getInstance().getWindow());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();

                Export export = new Export();
                export.export(path);
            }
        }

        // If the importBtn was clicked, import a portfolio from file
        if (e.getSource() == importBtn) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setDialogTitle("Import");
            fc.setApproveButtonText("Import");

            int returnVal = fc.showOpenDialog(GuiController.getInstance().getWindow());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();

                Export export = new Export();
                export.importPortfolio(path);
            }
        }
    }
}
