package fpts.toolkit;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ryan on 3/10/2016.
 *
 * A custom JButton that allows hover and pressed background colors to be set.
 */
public class CustomButton extends JButton {

    private Color hoverBackgroundColor;         // The color to change to when hovered
    private Color pressedBackgroundColor;       // The color to change to when pressed

    /**
     * Create a new CustomButton with no content.
     */
    public CustomButton() {
        this(null);
    }

    /**
     * Create a new CustomButton with the given text.
     * @param text
     */
    public CustomButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(pressedBackgroundColor);
        } else if (getModel().isRollover()) {
            g.setColor(hoverBackgroundColor);
        } else {
            g.setColor(getBackground());
        }

        if (getBackground() != null)
            g.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }

    /**
     * Get the Color that is shown when the button is hovered.
     * @return Color
     */
    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    /**
     * Set the Color that should be shown when the button is hovered.
     * @param hoverBackgroundColor
     */
    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    /**
     * Get the Color that is shown when the button is pressed.
     * @return Color
     */
    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    /**
     * Set the Color that should be shown when the button is pressed.
     * @param pressedBackgroundColor
     */
    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}
