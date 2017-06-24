package fpts.toolkit;

import fpts.controller.GuiController;
import fpts.controller.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Ryan on 4/3/2016.
 *
 * A notification that will "pop up" in the window to inform the user that
 * something has happened, is happening, or will happen.
 */
public class Notification {

    private JPanel frame;                   // The "parent" component of the notification
    private JPanel mainPanel;               // The main panel containing the notification message
    private JPanel sidePanel;               // The side panel containing the dismiss button
    private CustomButton exitBtn;           // The button to dismiss the notification
    private Color mainColor =               // The optional color for the main notification area
            new Color(93, 175, 97);
    private Color secondColor =             // The optional color for the right notification area
            new Color(57, 135, 59);
    private JLabel messageLbl;              // The label showing the notification message

    private String message = "";            // The notification message
    private int messageWidth;               // The width of the notification message used to size the components
    private int time = 4;                   // The time (in seconds) to wait before dismissing the notification
    private Thread timeoutThread = null;    // The thread in which a NotificationTimeout will run

    /**
     * Constructor used to create a new notification.
     * @param message - The message to show in the notification
     */
    public Notification(String message) {
        this.message = message;

        setupNotification();
    }

    /**
     * Constructor used to create a new notification with custom colors.
     * @param message - The message to show in the notification
     * @param mainColor - The optional color for the main notification area
     * @param secondColor - The optional color for the right notification area
     */
    public Notification(String message, Color mainColor, Color secondColor) {
        this.message = message;
        this.mainColor = mainColor;
        this.secondColor = secondColor;

        setupNotification();
    }

    /**
     * Constructor used to create a new notification with overridden timeout.
     * @param message - The message to show in the notification
     * @param timeout - The time (in seconds) to wait before dismissing the notification
     */
    public Notification(String message, int timeout) {
        this.message = message;
        this.time = timeout;

        setupNotification();
    }

    /**
     * Constructor used to create a new notification with overridden timeout
     * and custom colors.
     * @param message - The message to show in the notification
     * @param timeout - The time (in seconds) to wait before dismissing the notification
     * @param mainColor - The optional color for the main notification area
     * @param secondColor - The optional color for the right notification area
     */
    public Notification(String message, int timeout, Color mainColor, Color secondColor) {
        this.message = message;
        this.time = timeout;
        this.mainColor = mainColor;
        this.secondColor = secondColor;

        setupNotification();
    }

    /**
     * Build the components of the notification.
     */
    private void setupNotification() {
        messageLbl = new JLabel();
        messageLbl.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        messageLbl.setText(message);
        messageLbl.setForeground(new Color(244, 244, 244));

        messageWidth = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(20.0f))
                .stringWidth(message);

        messageLbl.setSize(messageWidth, 40);

        setupFrame();
        setupMainPanel();
        setupSidePanel();
    }

    /**
     * Build the parent frame of the notification that houses the other components.
     */
    private void setupFrame() {
        frame = new JPanel();
        frame.setLayout(null);
        frame.setSize(messageWidth + 60, 40);
    }

    /**
     * Build the main panel of the notification that shows the message.
     */
    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(mainColor);
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, messageWidth + 20, 40);

        messageLbl.setBounds(10, 0, messageWidth, 40);
        mainPanel.add(messageLbl);

        frame.add(mainPanel);
    }

    /**
     * The side panel of the notification that shows the dismiss button.
     */
    private void setupSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setBackground(secondColor);
        sidePanel.setLayout(null);
        sidePanel.setBounds(messageWidth + 20, 0, 40, 40);

        exitBtn = new CustomButton();
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setBorder(null);
        exitBtn.setFont(ResourceLoader.robotoFont.deriveFont(20.0f));
        exitBtn.setText("X");
        exitBtn.setForeground(new Color(244, 244, 244));
        exitBtn.setBackground(secondColor);
        exitBtn.setHoverBackgroundColor(secondColor);
        exitBtn.setPressedBackgroundColor(mainColor);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.setBounds(0, 0, 40, 40);
        exitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                frame.setVisible(false);
            }
        });
        sidePanel.add(exitBtn);

        frame.add(sidePanel);
    }

    /**
     * Set the position of this notification within the application's main panel.
     * @param x - the x position
     * @param y - the y position
     */
    public void setPosition(int x, int y) {
        frame.setLocation(x, y);
    }

    /**
     * Set the position of this notification at the bottom of the window, centered horizontally.
     */
    public void setPositionBottomCenter() {
        int x = (GuiController.getInstance().getMainPanel().getWidth() / 2) - ((messageWidth + 60) / 2);
        int y = (GuiController.getInstance().getWindow().getHeight() - 65);
        frame.setLocation(x, y);
    }

    /**
     * Set the vertical position of this notification.
     * @param pos
     */
    public void setVerticalPosition(int pos) {
        frame.setLocation(frame.getX(), pos);
    }

    /**
     * Show this notification on the screen.
     */
    public void showNotification() {
        GuiController.getInstance().getMainPanel().add(frame);
        GuiController.getInstance().getMainPanel().setComponentZOrder(frame, 0);

        timeoutThread = new Thread(new NotificationTimeout(this, time));
        timeoutThread.start();

        frame.setVisible(true);
        GuiController.getInstance().getWindow().repaint();
    }

    /**
     * Remove this notification from the screen.
     */
    public void removeNotification() {
        frame.setVisible(false);
        GuiController.getInstance().getMainPanel().remove(frame);
        GuiController.getInstance().getWindow().repaint();
        GuiController.getInstance().decrementNumNotifictions();

        try {
            timeoutThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * NotificationTimeout is a runnable that will run in a thread to automatically close the notification after a certain amount of time.
 */
class NotificationTimeout implements Runnable {

    private boolean running = true;     // True if this timeout is still running
    private int time = 4;               // The time to wait before dismissing the notification
    private Notification notification;  // The notification to close after the timeout

    /**
     * Create a new NotificationTimeout.
     * @param notification - the notification to close after the timeout
     * @param time - the time to wait before dismissing the notification
     */
    public NotificationTimeout(Notification notification, int time) {
        this.notification = notification;
        this.time = time;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);
                time--;

                if (time == 0) {
                    running = false;
                    notification.removeNotification();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
