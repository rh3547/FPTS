package fpts.toolkit;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by rhochmuth on 3/13/2016.
 *
 * A custom ScrollBar design.
 */
public class CustomScrollBar extends BasicScrollBarUI {

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(new Color(140, 140, 140));
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }
}