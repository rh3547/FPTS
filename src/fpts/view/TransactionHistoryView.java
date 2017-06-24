package fpts.view;

import fpts.controller.GuiController;
import fpts.controller.PortfolioController;
import fpts.controller.ResourceLoader;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by Ryan on 3/11/2016.
 *
 * A view of the history of transactions within a portfolio.  Shown in a list style.
 */
public class TransactionHistoryView extends View {

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

    private JScrollPane scrollPane;
    private JPanel scrollList;
    private ArrayList<JPanel> transactionItems = null;

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
        titleLbl.setText("Transactions");
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

        transactionItems = (ArrayList<JPanel>)context.getData("transactions");
        setupScrollPane();
    }

    @Override
    public void removeGuiComponents() {

    }

    @Override
    public void resize() {
        // Top panel stuff
        topPanelWidth = GuiController.windowWidth;
        topPanelHeight = 110;
        mainPanelWidth = GuiController.windowWidth;
        mainPanelHeight = GuiController.windowHeight - topPanelHeight;
        topPanel.setSize(topPanelWidth, topPanelHeight);
        mainPanel.setSize(mainPanelWidth, mainPanelHeight);
        mainPanel.setLocation(0, topPanelHeight);
        backBtn.setLocation(topPanelWidth - 100 - 35, 52);
        // End top panel stuff

        scrollPane.setSize(mainPanelWidth - 100, mainPanelHeight - 210);
        scrollList.setSize(mainPanelWidth - 100, mainPanelHeight - 210);
        scrollPane.setLocation((mainPanelWidth / 2) - 450, 40);
    }

    /**
     * Setup the scroll pane that contains a list of transactions.
     */
    private void setupScrollPane() {

        if (scrollPane != null)
            mainPanel.remove(scrollPane);

        scrollPane = null;

        scrollList = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                if (transactionItems != null)
                    return new Dimension(0, 20 + (48 * transactionItems.size()));
                else
                    return new Dimension(0, 0);
            }
        };
        scrollList.setLayout(null);
        scrollList.setSize(mainPanelWidth - 50, mainPanelHeight - 100);

        scrollPane = new JScrollPane(scrollList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setSize(mainPanelWidth - 100, mainPanelHeight - 100);
        scrollPane.setLocation((mainPanelWidth / 2) - ((mainPanelWidth - 100) / 2), topPanelHeight + 40);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        mainPanel.add(scrollPane);

        if (transactionItems != null) {

            for (int x = 0; x < transactionItems.size(); x++) {
                JPanel panel = transactionItems.get(x);

                panel.setLocation(0, 20 + (48 * x));

                scrollList.add(panel);
            }
        }

        this.resize();
    }

    @Override
    public void showMessage(String message, int type) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            PortfolioController.getInstance().showPortfolioView();
        }
    }
}
