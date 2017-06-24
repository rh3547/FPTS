package fpts.toolkit;

import fpts.controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by Ryan on 3/5/2016.
 *
 * Sets up a JFrame and several JPanels to mimic a window while allowing the JFrame to be undecorated.
 */
public class UndecoratedFrame extends JFrame {

    private int width;                          // The width of the frame
    private int height;                         // The height of the frame
    private int minWidth;                          // The min width of the frame
    private int minHeight;                         // The min height of the frame
    private JFrame frame = new JFrame();        // The main undecorated JFrame
    private JPanel outerPanel;                  // The outer most panel that will contain main panel and border panel
    private JPanel mainPanel;                   // The main panel that contains the actual window content
    private JPanel borderPanel;                 // The panel that rests on top mimicking a typical window bar

    /**
     * Constructor to create the frame.
     * @param width
     * @param height
     */
    public UndecoratedFrame(int width, int height, int minWidth, int minHeight) {
        this.width = width;
        this.height = height;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.createGui();
    }

    /**
     * The main panel that contains the window content (views).
     */
    class MainPanel extends JPanel {

        int width;
        int height;

        public MainPanel(int width, int height) {
            this.width = width;
            this.height = height;
            setBackground(Color.gray);
            setLayout(null);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
    }

    /**
     * The panel that rests at the top of the frame that mimics a typical window bar.
     * Contains the exit button, minimize button.
     */
    class BorderPanel extends JPanel {

        private JLabel exitLbl;
        private JLabel minLbl;
        private JLabel maxLbl;
        private boolean maximized = false;
        private int prevX;
        private int prevY;
        int pX, pY;

        public BorderPanel() {
            exitLbl = new JLabel("XXX") {

                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    g.setColor(new Color(128, 0, 3));
                    g.fillOval(0, 0, 14, 14);
                }
            };
            exitLbl.setOpaque(true);
            exitLbl.setForeground(Color.WHITE);

            setLayout(new FlowLayout(FlowLayout.RIGHT));

            exitLbl.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    UserController.getInstance().logout();
                    System.exit(0);
                }
            });

            minLbl = new JLabel("XXX") {

                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    g.setColor(new Color(203, 198, 0));
                    g.fillOval(0, 0, 14, 14);
                }
            };
            minLbl.setOpaque(true);
            minLbl.setForeground(Color.WHITE);

            minLbl.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    frame.setState(Frame.ICONIFIED);
                }
            });

            maxLbl = new JLabel("XXX") {

                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    g.setColor(new Color(0, 166, 14));
                    g.fillOval(0, 0, 14, 14);
                }
            };
            maxLbl.setOpaque(true);
            maxLbl.setForeground(Color.WHITE);

            maxLbl.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {


                    if (maximized) {
                        frame.setSize(prevX, prevY);
                        frame.setLocationRelativeTo(null);

                        maximized = false;
                    }
                    else {
                        prevX = frame.getWidth();
                        prevY = frame.getHeight();

                        Toolkit tk = Toolkit.getDefaultToolkit();
                        int xSize = ((int) tk.getScreenSize().getWidth());
                        int ySize = ((int) tk.getScreenSize().getHeight());
                        frame.setSize(xSize, ySize);
                        frame.setLocation(0, 0);

                        maximized = true;
                    }
                }
            });

            add(maxLbl);
            add(minLbl);
            add(exitLbl);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    // Get x,y and store them
                    pX = me.getX();
                    pY = me.getY();

                }

                public void mouseDragged(MouseEvent me) {

                    frame.setLocation(frame.getLocation().x + me.getX() - pX,
                            frame.getLocation().y + me.getY() - pY);
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent me) {

                    frame.setLocation(frame.getLocation().x + me.getX() - pX,
                            frame.getLocation().y + me.getY() - pY);
                }
            });
        }
    }

    /**
     * The outside panel that contains the border panel on top and the main panel below.
     */
    class OutsidePanel extends JPanel {

        public OutsidePanel() {
            setLayout(new BorderLayout());
            mainPanel = new MainPanel(width, height);
            borderPanel = new BorderPanel();
            add(mainPanel, BorderLayout.CENTER);
            add(borderPanel, BorderLayout.PAGE_START);
        }
    }

    /**
     * Creates the separate components of the frame and puts them together.
     */
    private void createGui() {
        ComponentResizer cr = new ComponentResizer();
        cr.setMinimumSize(new Dimension(minWidth, minHeight));
        cr.registerComponent(frame);
        cr.setSnapSize(new Dimension(10, 10));
        frame.setUndecorated(true);
        outerPanel = new OutsidePanel();
        frame.add(outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    /**
     * Get the final undecorated JFrame.
     * @return JFrame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Get the outer panel.  (The containing panel)
     * @return JPanel
     */
    public JPanel getOuterPanel() {
        return outerPanel;
    }

    /**
     * Get the main panel.  (The main content panel)
     * @return JPanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Get the border panel.  (The panel on top)
     * @return JPanel
     */
    public JPanel getBorderPanel() {
        return borderPanel;
    }
}