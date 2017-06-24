package fpts.view;

import fpts.controller.Context;
import fpts.controller.GuiController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Ryan on 2/28/2016.
 *
 * A "template" for View objects.  Is a JPanel and contains method signatures for sub-classes to use.
 * Each View should contain different Swing GUI components (JButton, JLabel, etc.) and be implemented in
 * its own way.
 */
public abstract class View extends JPanel implements ActionListener {

    public static final int INFO_MESSAGE = 0;           // A general message
    public static final int ERROR_MESSAGE = 1;          // A message indicating an error occurred
    public static final int SUCCESS_MESSAGE = 2;        // A message indicating successful

    protected RenderingHints renderingHints;
    protected Context context = null;

    /**
     * Initialize the basics of the view (regarding the JPanel color, size, etc.).
     */
    public void initialize() {
        this.renderingHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        this.setBackground(new Color(239, 239, 239));
        this.setBounds(0, 0, GuiController.windowWidth, GuiController.windowHeight);
        this.setLayout(null);
    }

    /**
     * Initialize the basics of the view (regarding the JPanel color, size, etc.), with context.
     */
    public void initialize(Context context) {
        this.context = context;

        this.renderingHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        this.setBackground(new Color(239, 239, 239));
        this.setBounds(0, 0, GuiController.windowWidth, GuiController.windowHeight);
        this.setLayout(null);
    }

    /**
     * Initialize and add the corresponding GUI components to the View.
     */
    public abstract void addGuiComponents();

    /**
     * Remove all GUI components from the View and re-initialize them to null.
     */
    public abstract void removeGuiComponents();

    /**
     * Called when the window re-sizes.  Implemented in each instance because certain View objects may want to
     * re-size differently.
     */
    public abstract void resize();

    /**
     * Show a message in the view.  Used for error, confirmation, etc. messages.
     * @param message
     */
    public abstract void showMessage(String message, int type);
}