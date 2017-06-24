package fpts.toolkit;

import fpts.controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ryan on 3/2/2016.
 */
public class WindowMenuBar implements ActionListener {

    private static WindowMenuBar instance = new WindowMenuBar();

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem portfolioMI;
    private JMenuItem updateMarketMI;
    private JMenuItem marketIntervalMI;
    private JMenuItem logoutMI;
    private JMenuItem exitMI;

    private JMenu editMenu;
    private JMenuItem undoMI;
    private JMenuItem redoMI;

    /**
     * Private constructor prevents instances from being created.
     */
    private WindowMenuBar() {
    }

    /**
     * Get the only instance of WindowMenuBar.
     *
     * @return GuiController
     */
    public static WindowMenuBar getInstance() {
        return instance;
    }

    /**
     * Setup the JMenuBar.
     */
    public void setupMenuBar() {
        this.menuBar = new JMenuBar();
        this.menuBar.setBackground(new Color(187, 187, 187));
        this.menuBar.setBorder(null);

        this.fileMenu = new JMenu();
        this.fileMenu.setText("File");
        this.fileMenu.setBorderPainted(false);
        this.fileMenu.setForeground(new Color(69, 69, 69));
        this.fileMenu.setFont(ResourceLoader.robotoFont);

        this.portfolioMI = new JMenuItem();
        this.portfolioMI.setText("View Portfolio");
        this.portfolioMI.addActionListener(this);
        this.portfolioMI.setFont(ResourceLoader.robotoFont);

        this.updateMarketMI = new JMenuItem();
        this.updateMarketMI.setText("Update Market");
        this.updateMarketMI.addActionListener(this);
        this.updateMarketMI.setFont(ResourceLoader.robotoFont);

        this.marketIntervalMI = new JMenuItem();
        this.marketIntervalMI.setText("Set Market Update Interval");
        this.marketIntervalMI.addActionListener(this);
        this.marketIntervalMI.setFont(ResourceLoader.robotoFont);

        this.logoutMI = new JMenuItem();
        this.logoutMI.setText("Logout");
        this.logoutMI.addActionListener(this);
        this.logoutMI.setFont(ResourceLoader.robotoFont);

        this.exitMI = new JMenuItem();
        this.exitMI.setText("Exit");
        this.exitMI.addActionListener(this);
        this.exitMI.setFont(ResourceLoader.robotoFont);

        this.editMenu = new JMenu();
        this.editMenu.setText("Edit");
        this.editMenu.setBorderPainted(false);
        this.editMenu.setForeground(new Color(69, 69, 69));
        this.editMenu.setFont(ResourceLoader.robotoFont);

        this.undoMI = new JMenuItem();
        this.undoMI.setText("Undo");
        this.undoMI.addActionListener(this);
        this.undoMI.setFont(ResourceLoader.robotoFont);

        this.redoMI = new JMenuItem();
        this.redoMI.setText("Redo");
        this.redoMI.addActionListener(this);
        this.redoMI.setFont(ResourceLoader.robotoFont);

        this.fileMenu.add(portfolioMI);
        this.fileMenu.add(marketIntervalMI);
        this.fileMenu.add(logoutMI);
        this.fileMenu.add(exitMI);
        this.menuBar.add(this.fileMenu);

        this.editMenu.add(undoMI);
        this.editMenu.add(redoMI);
        this.menuBar.add(this.editMenu);
    }

    /**
     * Get the JMenuBar.
     *
     * @return JMenuBar
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /*
            File Menu
         */
        // If the file -> view portfolio button was clicked, show the current user's portfolio
        if (e.getSource() == portfolioMI) {
            PortfolioController.getInstance().showPortfolioView();
        }

        // If the file -> set market update interval button was clicked, set the market update interval
        if (e.getSource() == marketIntervalMI) {
            String input = JOptionPane.showInputDialog(null,
                    "How often would you like FPTS to automatically update the market prices? (In seconds)",
                    MarketController.getInstance().getDelayInSeconds());

            if (input != null)
                MarketController.getInstance().setDelay(Long.parseLong(input));
        }

        // If the file -> logout button was clicked, log the current user out
        if (e.getSource() == logoutMI) {
            UserController.getInstance().logout();
        }

        // If the file -> exit button was clicked, exit the program
        if (e.getSource() == exitMI) {
            System.exit(0);
        }

        /*
            Edit Menu
         */
        // If the edit -> undo button was clicked, undo the last command
        if (e.getSource() == undoMI) {
            UndoRedoController.getInstance().undo();
            GuiController.getInstance().reloadView();
        }

        // If the edit -> redo button was clicked, redo the last undone command
        if (e.getSource() == redoMI) {
            UndoRedoController.getInstance().redo();
            GuiController.getInstance().reloadView();
        }
    }
}