package fpts.controller;

import fpts.toolkit.Notification;
import fpts.toolkit.UndecoratedFrame;
import fpts.toolkit.WindowMenuBar;
import fpts.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by Ryan on 2/28/2016.
 *
 * Controls the general GUI.  Creates and shows/hides the JFrame.
 * Sets/removes View objects.
 */
public class GuiController {

    private static GuiController instance = null;

    public static int windowWidth = 0;      // The width of the window
    public static int windowHeight = 0;     // The height of the window

    private UndecoratedFrame uf;            // The undecorated frame constructor
    private JFrame window;                  // The actual JFrame being used
    private boolean toolbarShown = true;    // Is the window toolbar currently being shown

    private View currentView = null;        // The View currently in the JFrame

    private int numNotifications = -1;      // The number of notifications shown

    /**
     * Private constructor prevents instances from being created.
     */
    private GuiController() { }

    /**
     * Instantiate the singleton GuiController if one doesn't exist.
     * @param windowWidth
     * @param windowHeight
     */
    public static void instantiate(int windowWidth, int windowHeight, String title) {
        if (instance == null) {

            // Set anti-aliasing for smoother text rendering
            System.setProperty("awt.useSystemAAFontSettings","on");
            System.setProperty("swing.aatext", "true");

            instance = new GuiController();

            GuiController.windowWidth = windowWidth;
            GuiController.windowHeight = windowHeight;

            // Create the undecorated frame
            instance.uf = new UndecoratedFrame(windowWidth, windowHeight, 600, 600);
            instance.uf.getMainPanel().setBackground(new Color(250, 250, 250));
            instance.uf.getBorderPanel().setBackground(new Color(34, 34, 34));

            // Set the main JFrame properties
            instance.window = instance.uf.getFrame();
            instance.window.setTitle(title);
            instance.window.setSize(windowWidth, windowHeight);
            instance.setWindowIcon();
            instance.setWindowListeners();
            instance.window.setVisible(false);

            // Set up the JMenuBar
            WindowMenuBar.getInstance().setupMenuBar();
            instance.uf.getMainPanel().add(WindowMenuBar.getInstance().getMenuBar());
            WindowMenuBar.getInstance().getMenuBar().setBounds(0, 0, windowWidth, 25);
            instance.uf.getMainPanel().repaint();
        }
    }

    /**
     * Get the only instance of GuiController.
     * @return GuiController
     */
    public static GuiController getInstance() {
        return instance;
    }

    /**
     * Add the proper window listeners to the window.
     * One to detect when the window re-sizes.
     */
    private void setWindowListeners() {
        this.window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                GuiController.windowWidth = e.getComponent().getWidth();
                GuiController.windowHeight = e.getComponent().getHeight();

                if (WindowMenuBar.getInstance().getMenuBar() != null)
                    WindowMenuBar.getInstance().getMenuBar().setBounds(0, 0, windowWidth, 25);

                if (GuiController.getInstance().getCurrentView() != null) {
                    GuiController.getInstance().getCurrentView().setSize(GuiController.windowWidth, GuiController.windowHeight);
                    GuiController.getInstance().getCurrentView().resize();
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    /**
     * Set the icon of the window.
     */
    private void setWindowIcon() {

        this.window.setIconImage(new ImageIcon("src/lib/res/icon32.png").getImage());
    }

    /**
     * Make the window's toolbar visible.
     */
    public void showToolbar() {
        if (!this.toolbarShown) {
            WindowMenuBar.getInstance().getMenuBar().setVisible(true);
            this.toolbarShown = true;
        }
    }

    /**
     * Make the window's toolbar hidden.
     */
    public void hideToolbar() {
        if (this.toolbarShown) {
            WindowMenuBar.getInstance().getMenuBar().setVisible(false);
            this.toolbarShown = false;
        }
    }

    /**
     * Make the main JFrame visible.
     */
    public void showWindow() {
        this.window.setLocationRelativeTo(null);
        this.window.setVisible(true);
    }

    /**
     * Hide the main JFrame.
     */
    public void hideWindow() {
        this.window.setVisible(false);
    }

    /**
     * Change the size of the window.
     * @param windowWidth
     * @param windowHeight
     */
    public void setWindowSize(int windowWidth, int windowHeight) {
        GuiController.windowWidth = windowWidth;
        GuiController.windowHeight = windowHeight;

        this.window.setSize(windowWidth, windowHeight);
        this.window.repaint();
    }

    /**
     * Get the main JPanel that holds the view contents.
     * @return JPanel
     */
    public JPanel getMainPanel() {
        return uf.getMainPanel();
    }

    /**
     * Get the main JFrame.
     * @return JFrame
     */
    public JFrame getWindow() {
        return this.window;
    }

    /**
     * Set the View currently in the JFrame.
     * @param view
     */
    public void setView(View view) {
        if (this.currentView != null) {
            this.currentView.removeGuiComponents();
            this.uf.getMainPanel().remove(this.currentView);
        }
        this.currentView = null;

        this.currentView = view;
        this.uf.getMainPanel().add(this.currentView);
        this.currentView.setVisible(true);

        this.uf.getMainPanel().repaint();
        this.window.repaint();

        JFrame frame = GuiController.getInstance().getWindow();
        frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        this.currentView.resize();
    }

    /**
     * Remove the View currently in the JFrame.
     */
    public void removeView() {
        this.currentView = null;
    }

    /**
     * Get the window's current View.
     * @return View
     */
    public View getCurrentView() {

        return this.currentView;
    }

    /**
     * Refresh the contents of the current view.  Used when GUI components are updating
     * on the view when the view shouldn't be reloaded entirely.
     */
    public void refreshView() {
        window.setBounds(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        window.setVisible(true);
        currentView.resize();
    }

    /**
     * Refresh the contents of the current view.  Used when GUI components are updating
     * on the view when the view shouldn't be reloaded entirely.
     */
    public void refreshViewWithToolbar() {
        window.setBounds(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        hideToolbar();
        showToolbar();
        window.setVisible(true);
        currentView.resize();
    }

    /**
     * Reload the view that is currently shown.
     */
    public void reloadView() {
        if (getCurrentView() instanceof AddHoldingView) {
            PortfolioController.getInstance().showAddHoldingView();
        }
        else if (getCurrentView() instanceof AddWatchlistView) {
            WatchlistController.getInstance().showAddWatchlistView();
        }
        else if (getCurrentView() instanceof CreateSimulationView) {
            SimulationController.getInstance().showCreateSimulationView();
        }
        else if (getCurrentView() instanceof PortfolioView) {
            PortfolioController.getInstance().showPortfolioView();
        }
        else if (getCurrentView() instanceof TransactionHistoryView) {
            TransactionController.getInstance().showTransactionHistoryView();
        }
        else if (getCurrentView() instanceof WatchlistView) {
            WatchlistController.getInstance().showWatchlistView();
        }
    }

    /**
     * Show a new notification on the current view.
     * @param message - The message to show in the notification
     * @param timeout - The time (in seconds) to wait before dismissing the notification
     */
    public void showNotification(String message, int timeout) {
        Notification notification = new Notification(message, timeout);
        incrementNumNotifictions();
        notification.setPositionBottomCenter();
        notification.setVerticalPosition((GuiController.getInstance().getWindow().getHeight() - 65)
                - (numNotifications * 50));
        notification.showNotification();
    }

    /**
     * Show a new notification on the current view that represents an error.
     * @param message - The message to show in the notification
     * @param timeout - The time (in seconds) to wait before dismissing the notification
     */
    public void showErrorNotification(String message, int timeout) {
        Notification notification = new Notification(message, timeout, new Color(136, 60, 70), new Color(116, 35, 47));
        incrementNumNotifictions();
        notification.setPositionBottomCenter();
        notification.setVerticalPosition((GuiController.getInstance().getWindow().getHeight() - 65)
                - (numNotifications * 50));
        notification.showNotification();
    }

    /**
     * Increase the number of notifications by 1.
     */
    public void incrementNumNotifictions() {
        numNotifications++;
    }

    /**
     * Decrease the number of notifications by 1.
     */
    public void decrementNumNotifictions() {
        numNotifications--;
    }
}