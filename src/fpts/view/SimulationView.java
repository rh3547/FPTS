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
import java.text.DecimalFormat;

/**
 * Created by Ryan on 3/11/2016.
 *
 * The view in which a simulation is actually "ran".  Shows the results of simulation.
 */
public class SimulationView extends View {

    // Top panel stuff
    private int topPanelWidth = 0;
    private int topPanelHeight = 0;
    private int mainPanelWidth = 0;
    private int mainPanelHeight = 0;

    private JPanel topPanel;
    private JPanel mainPanel;
    private JLabel titleLbl;
    private JButton endBtn;
    // End top panel stuff

    private JLabel oldValueDollarLbl;
    private JLabel oldValueLbl;
    private JLabel newValueDollarLbl;
    private JLabel newValueLbl;
    private JLabel oldLbl;
    private JLabel newLbl;

    private JPanel formPanel;
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
        titleLbl.setText("Simulation");
        titleLbl.setFont(ResourceLoader.robotoFont.deriveFont(40.0f));
        titleLbl.setSize(400, 50);
        titleLbl.setLocation(35, 40);
        topPanel.add(titleLbl);

        endBtn = GuiComponentFactory.getInstance().createBasicButton();
        endBtn.setText("End Simulation");
        endBtn.addActionListener(this);
        endBtn.setSize(150, 30);
        endBtn.setLocation(topPanelWidth - 150 - 35, 52);
        topPanel.add(endBtn);
        // End top panel stuff

        oldValueDollarLbl = GuiComponentFactory.getInstance().createBasicLabel();
        oldValueDollarLbl.setText("$");
        oldValueDollarLbl.setFont(ResourceLoader.robotoFont.deriveFont(35.0f));
        oldValueDollarLbl.setForeground(new Color(76, 175, 80));
        int oldDollarWidth = oldValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(35.0f))
                .stringWidth(oldValueDollarLbl.getText());
        oldValueDollarLbl.setSize(oldDollarWidth, 40);
        oldValueDollarLbl.setLocation(35, 50);
        mainPanel.add(oldValueDollarLbl);

        oldValueLbl = GuiComponentFactory.getInstance().createBasicLabel();
        float oldValue = (Float)context.getData("currentValue");
        oldValueLbl.setText(df.format(oldValue));
        oldValueLbl.setFont(ResourceLoader.robotoFont.deriveFont(50.0f));
        int totalValueWidth = oldValueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                .stringWidth(oldValueLbl.getText());
        oldValueLbl.setSize(totalValueWidth, 50);
        oldValueLbl.setLocation(35 + oldDollarWidth + 10, 40);
        mainPanel.add(oldValueLbl);

        newValueLbl = GuiComponentFactory.getInstance().createBasicLabel();
        float newValue = (Float)context.getData("newValue");
        newValueLbl.setText(df.format(newValue));
        newValueLbl.setFont(ResourceLoader.robotoFont.deriveFont(50.0f));
        int newValueWidth = newValueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(50.0f))
                .stringWidth(newValueLbl.getText());
        newValueLbl.setSize(newValueWidth, 50);
        newValueLbl.setLocation(topPanelWidth - 35 - (newValueWidth), 40);
        mainPanel.add(newValueLbl);

        newValueDollarLbl = GuiComponentFactory.getInstance().createBasicLabel();
        newValueDollarLbl.setText("$");
        newValueDollarLbl.setFont(ResourceLoader.robotoFont.deriveFont(35.0f));
        newValueDollarLbl.setForeground(new Color(76, 175, 80));
        int newDollarWidth = newValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(35.0f))
                .stringWidth(newValueDollarLbl.getText());
        newValueDollarLbl.setSize(newDollarWidth, 40);
        newValueDollarLbl.setLocation(topPanelWidth - 35 - (newValueWidth) - 10 - newDollarWidth, 50);
        mainPanel.add(newValueDollarLbl);

        oldLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        oldLbl.setText("current value");
        oldLbl.setSize(200, 40);
        oldLbl.setLocation(35, 10);
        mainPanel.add(oldLbl);

        newLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        newLbl.setText("projected value");
        newLbl.setSize(200, 40);
        newLbl.setLocation(mainPanelWidth - 35 - 165, 10);
        mainPanel.add(newLbl);

        int formPanelHeight = 275;
        formPanel = new JPanel();
        formPanel.setSize(mainPanelWidth, formPanelHeight);
        formPanel.setLocation(0, GuiController.windowHeight - formPanelHeight);
        formPanel.setBackground(new Color(219, 219, 219));
        formPanel.setLayout(null);
        mainPanel.add(formPanel);

        // Form fields
        simTypeLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        simTypeLbl.setText("simulation type");
        simTypeLbl.setSize(200, 40);
        simTypeLbl.setLocation(35, 10);
        formPanel.add(simTypeLbl);

        simTypeCB = new JComboBox(simTypeList);
        simTypeCB.setSelectedIndex(0);
        simTypeCB.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        simTypeCB.setBackground(new Color(221, 221, 221));
        simTypeCB.setUI(new BasicComboBoxUI());
        simTypeCB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        simTypeCB.setSize(260, 50);
        simTypeCB.setBackground(new Color(238, 238, 238));
        simTypeCB.setLocation(35, 55);
        formPanel.add(simTypeCB);

        percentLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        percentLbl.setText("annual percent");
        percentLbl.setSize(200, 40);
        percentLbl.setLocation(340, 10);
        formPanel.add(percentLbl);

        percentField = GuiComponentFactory.getInstance().createBasicTextField();
        percentField.setSize(200, 50);
        percentField.setLocation(340, 55);
        percentField.setBackground(new Color(238, 238, 238));
        formPanel.add(percentField);

        durationLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        durationLbl.setText("duration");
        durationLbl.setSize(200, 40);
        durationLbl.setLocation(580, 10);
        formPanel.add(durationLbl);

        durationField = GuiComponentFactory.getInstance().createBasicTextField();
        durationField.setSize(200, 50);
        durationField.setLocation(580, 55);
        durationField.setBackground(new Color(238, 238, 238));
        formPanel.add(durationField);

        durationUnitCB = new JComboBox(durationList);
        durationUnitCB.setSelectedIndex(0);
        durationUnitCB.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        durationField.setBackground(new Color(238, 238, 238));
        durationUnitCB.setUI(new BasicComboBoxUI());
        durationUnitCB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        durationUnitCB.setSize(200, 50);
        durationUnitCB.setLocation(820, 55);
        formPanel.add(durationUnitCB);

        startBtn = GuiComponentFactory.getInstance().createBasicButton();
        startBtn.setText("Simulate");
        startBtn.addActionListener(this);
        startBtn.setSize(180, 50);
        startBtn.setLocation(1060, 55);
        formPanel.add(startBtn);

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
        endBtn.setLocation(topPanelWidth - 150 - 35, 52);
        // End top panel stuff

        float totalValueFontSize = determineValueSize(newValueLbl.getText().length());

        newValueLbl.setFont(ResourceLoader.robotoFont.deriveFont(totalValueFontSize));
        int newValueWidth = newValueLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(totalValueFontSize))
                .stringWidth(newValueLbl.getText());
        newValueLbl.setSize(newValueWidth, 50);
        newValueLbl.setLocation(topPanelWidth - 35 - (newValueWidth), 40);

        newValueDollarLbl.setFont(ResourceLoader.robotoFont.deriveFont(totalValueFontSize - 15));
        int newDollarWidth = newValueDollarLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(totalValueFontSize - 15))
                .stringWidth(newValueDollarLbl.getText());
        newValueDollarLbl.setSize(newDollarWidth, 40);
        newValueDollarLbl.setLocation(topPanelWidth - 35 - (newValueWidth) - 10 - newDollarWidth, 50);

        float oldValueFontSize = determineValueSize(oldValueLbl.getText().length());

        oldValueLbl.setFont(ResourceLoader.robotoFont.deriveFont(oldValueFontSize));

        int formPanelHeight = 275;
        formPanel.setLocation(0, GuiController.windowHeight - formPanelHeight);

        int width = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(24.0f)).stringWidth(messageLbl.getText());
        messageLbl.setSize(width, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), GuiController.windowHeight - 275 - 40);

        formPanel.setSize(mainPanelWidth, formPanelHeight);
        formPanel.setLocation(0, GuiController.windowHeight - formPanelHeight);
        newLbl.setLocation(mainPanelWidth - 35 - 165, 10);

        this.repaint();
    }

    /**
     * Determine the font size that the total value label should be based on the screen size.
     * @return float
     */
    private float determineValueSize(int length) {
        float totalValueFontSize = 50.0f;
        if (length == 9) {
            if (topPanelWidth > 980)
                totalValueFontSize = 50.0f;
            else
                totalValueFontSize = 45.0f;
        }
        else if (length == 10) {
            if (topPanelWidth > 1000)
                totalValueFontSize = 50.0f;
            else
                totalValueFontSize = 40.0f;
        }
        else if (length == 12) {
            if (topPanelWidth > 1010)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 970)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 35.0f;
        }
        else if (length == 13) {
            if (topPanelWidth > 1050)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 990)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 32.0f;
        }
        else if (length == 14) {
            if (topPanelWidth > 1100)
                totalValueFontSize = 50.0f;
            else if (topPanelWidth > 1010)
                totalValueFontSize = 40.0f;
            else
                totalValueFontSize = 30.0f;
        }
        else if (length >= 16) {
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
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), GuiController.windowHeight - 275 - 40);
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
        if (e.getSource() == endBtn) {
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

            SimulationController.getInstance().addSim(percent, type, durationType, duration);
            Float newValue = SimulationController.getInstance()
                    .nextSimulation(Float.parseFloat(newValueLbl.getText().replaceAll(",", "")));

            DecimalFormat df = new DecimalFormat("###,###,###,###.##");

            newValueLbl.setText(df.format(newValue));
            GuiController.getInstance().refreshViewWithToolbar();
        }
    }
}
