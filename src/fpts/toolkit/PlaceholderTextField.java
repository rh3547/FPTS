package fpts.toolkit;

import fpts.controller.ResourceLoader;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

/**
 * Created by Ryan on 3/10/2016.
 *
 * A custom JTextField that shows placeholder text in the field when nothing is entered.
 */
public class PlaceholderTextField extends JTextField {

    private String placeholder;     // The placeholder text

    /**
     * Default constructor.
     */
    public PlaceholderTextField() {
    }

    /**
     * Constructor to set the placeholder as a document, or String.  And set the number of columns.
     * @param pDoc
     * @param pText
     * @param pColumns
     */
    public PlaceholderTextField(final Document pDoc, final String pText, final int pColumns)
    {
        super(pDoc, pText, pColumns);

        this.placeholder = pText;
    }

    /**
     * Constructor to just set the number of columns.
     * @param pColumns
     */
    public PlaceholderTextField(final int pColumns) {
        super(pColumns);
    }

    /**
     * Constructor to set the placeholder text.
     * @param pText
     */
    public PlaceholderTextField(final String pText) {
        super(pText);

        setPlaceholder(pText);
        this.setText("");
    }

    /**
     * Constructor to set the placeholder text and number of columns.
     * @param pText
     * @param pColumns
     */
    public PlaceholderTextField(final String pText, final int pColumns) {
        super(pText, pColumns);
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(180, 180, 180));
        g.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        g.drawString(placeholder, getInsets().left + 5, this.getHeight() / 2 + 8);
    }

    /**
     * Get the placeholder text for this text field.
     * @return
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Set the placeholder text for this text field.
     * @param text
     */
    public void setPlaceholder(final String text) {
        placeholder = text;
    }
}