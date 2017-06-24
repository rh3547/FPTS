package fpts.view;

import fpts.controller.GuiController;
import fpts.controller.PortfolioController;
import fpts.controller.ResourceLoader;
import fpts.controller.SimulationController;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Ryan on 3/11/2016.
 *
 * The view where the user initially enters information on what type of simulation to run.
 */
public class CreateSimulationView extends View {

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

    private JLabel simTypeLbl;
    private String[] simTypeList = {"No Growth", "Bear Market", "Bull Market"};
    private JComboBox simTypeCB;
    private JLabel percentLbl;
    private JTextField percentField;
    private JLabel durationLbl;
    private JTextField durationField;
    private String[] durationList = {"Year", "Month", "Day"};
    private JComboBox durationUnitCB;
    private JButton startBtn;

    private JLabel messageLbl;

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
        titleLbl.setText("New Simulation");
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

        simTypeLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        simTypeLbl.setText("simulation type");
        simTypeLbl.setSize(200, 40);
        simTypeLbl.setLocation((mainPanelWidth / 2) - 250, 20);
        mainPanel.add(simTypeLbl);

        simTypeCB = new JComboBox(simTypeList);
        simTypeCB.setSelectedIndex(0);
        simTypeCB.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        simTypeCB.setBackground(new Color(221, 221, 221));
        simTypeCB.setUI(new BasicComboBoxUI());
        simTypeCB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        simTypeCB.setSize(500, 50);
        simTypeCB.setLocation((mainPanelWidth / 2) - 250, 60);
        mainPanel.add(simTypeCB);

        percentLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        percentLbl.setText("annual percent");
        percentLbl.setSize(200, 40);
        percentLbl.setLocation((mainPanelWidth / 2) - 250, 130);
        mainPanel.add(percentLbl);

        percentField = GuiComponentFactory.getInstance().createBasicTextField();
        percentField.setSize(500, 50);
        percentField.setText("0");
        percentField.setLocation((mainPanelWidth / 2) - 250, 170);
        mainPanel.add(percentField);

        durationLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        durationLbl.setText("duration");
        durationLbl.setSize(200, 40);
        durationLbl.setLocation((mainPanelWidth / 2) - 250, 240);
        mainPanel.add(durationLbl);

        durationField = GuiComponentFactory.getInstance().createBasicTextField();
        durationField.setSize(300, 50);
        durationField.setLocation((mainPanelWidth / 2) - 250, 280);
        mainPanel.add(durationField);

        durationUnitCB = new JComboBox(durationList);
        durationUnitCB.setSelectedIndex(0);
        durationUnitCB.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        durationUnitCB.setBackground(new Color(221, 221, 221));
        durationUnitCB.setUI(new BasicComboBoxUI());
        durationUnitCB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        durationUnitCB.setSize(190, 50);
        durationUnitCB.setLocation((mainPanelWidth / 2) - 250 + 310, 280);
        mainPanel.add(durationUnitCB);

        startBtn = GuiComponentFactory.getInstance().createBasicButton();
        startBtn.setText("Start Simulation");
        startBtn.addActionListener(this);
        startBtn.setSize(200, 50);
        startBtn.setLocation((mainPanelWidth / 2) - 100, 370);
        mainPanel.add(startBtn);

        GuiController.getInstance().getWindow().getRootPane().setDefaultButton(startBtn);

        messageLbl = new JLabel();
        messageLbl.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        messageLbl.setText("");
        messageLbl.setForeground(new Color(69, 69, 69));
        messageLbl.setSize(300, 40);
        mainPanel.add(messageLbl);
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

        simTypeCB.setLocation((mainPanelWidth / 2) - 250, 60);
        percentLbl.setLocation((mainPanelWidth / 2) - 250, 130);
        percentField.setLocation((mainPanelWidth / 2) - 250, 170);
        durationLbl.setLocation((mainPanelWidth / 2) - 250, 240);
        durationField.setLocation((mainPanelWidth / 2) - 250, 280);
        durationUnitCB.setLocation((mainPanelWidth / 2) - 250 + 310, 280);
        startBtn.setLocation((mainPanelWidth / 2) - 100, 370);

        int width = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(24.0f)).stringWidth(messageLbl.getText());
        messageLbl.setSize(width, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), (mainPanelHeight - 75));

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
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), (mainPanelHeight - 75));
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

        if (e.getSource() == startBtn) {
            String type = (String)simTypeCB.getSelectedItem();
            String percent = percentField.getText();
            String duration = durationField.getText();
            String durationType = (String)durationUnitCB.getSelectedItem();

            if (!isNumeric(percent)) {
                showMessage("Please enter a valid number for \'annual percent\'", View.ERROR_MESSAGE);
                return;
            }

            if (!type.equals("No Growth") && percent.equals("0")) {
                showMessage("Please enter a number greater than 0 for " + type, View.ERROR_MESSAGE);
                return;
            }

            if (!isNumeric(duration)) {
                showMessage("Please enter a valid number for \'duration\'", View.ERROR_MESSAGE);
                return;
            }

            if (duration.equals("0")) {
                showMessage("Please enter a number greater than 0 for \'duration\'", View.ERROR_MESSAGE);
                return;
            }

            SimulationController.getInstance().showSimulationView(percent, type, durationType, duration);
        }
    }
}
