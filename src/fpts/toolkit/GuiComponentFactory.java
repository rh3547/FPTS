package fpts.toolkit;

import fpts.controller.ResourceLoader;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

/**
 * Created by Ryan on 3/2/2016.
 *
 * Creates various Gui components that are used frequently.
 */
public class GuiComponentFactory {

    public static GuiComponentFactory instance = new GuiComponentFactory();

    /**
     * Private constructor prevents more instances from being created.
     */
    private GuiComponentFactory() {}

    /**
     * Get the only instance of GuiComponentFactory.
     * @return GuiComponentFactory
     */
    public static GuiComponentFactory getInstance() {
        return instance;
    }

    /**
     * Creates a basic button with the FTPS style.
     * @return JButton
     */
    public JButton createBasicButton() {

        CustomButton btn = new CustomButton();
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(ResourceLoader.robotoFont.deriveFont(16.0f));
        btn.setBackground(new Color(76, 175, 80));
        btn.setForeground(new Color(249, 249, 249));
        btn.setHoverBackgroundColor(new Color(64, 150, 68));
        btn.setPressedBackgroundColor(new Color(88, 200, 92));
        btn.setText("Button");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    /**
     * Creates a basic label with the FTPS style.
     * @return JLabel
     */
    public JLabel createBasicLabel() {
        JLabel lbl = new JLabel();
        lbl.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        lbl.setText("label");
        lbl.setForeground(new Color(31, 31, 31));
        lbl.setSize(175, 40);

        return lbl;
    }

    /**
     * Creates a basic file label with the FTPS style.
     * @return JLabel
     */
    public JLabel createBasicFieldLabel() {
        JLabel lbl = new JLabel();
        lbl.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        lbl.setText("label");
        lbl.setForeground(new Color(69, 69, 69));
        lbl.setSize(175, 40);

        return lbl;
    }

    /**
     * Creates a basic text field with the FTPS style.
     * @return JTextField
     */
    public JTextField createBasicTextField() {
        JTextField field = new JTextField();
        field.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setSize(200, 30);
        field.setBackground(new Color(221, 221, 221));

        return field;
    }

    /**
     * Creates a text field containing placeholder text with the FTPS style.
     * @return PlaceholderTextField
     */
    public JTextField createPlaceholderTextField(String text) {
        JTextField field = new PlaceholderTextField(text);
        field.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setSize(200, 30);
        field.setBackground(new Color(221, 221, 221));

        return field;
    }

    /**
     * Creates a basic password field with the FTPS style.
     * @return JPasswordField
     */
    public JPasswordField createBasicPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(ResourceLoader.robotoFont.deriveFont(18.0f));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setSize(200, 30);
        field.setBackground(new Color(221, 221, 221));

        return field;
    }

    /**
     * Creates a form separator.
     * @return JSeparator
     */
    public JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setBackground(new Color(239, 239, 239));
        sep.setForeground(new Color(76, 76, 76));
        sep.setSize(750, 30);

        return sep;
    }

    /**
     * Creates a combo box.
     * @return JSeparator
     */
    public JComboBox createComboBox(String[] list) {
        JComboBox cb = new JComboBox(list);
        cb.setSelectedIndex(0);
        cb.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        cb.setBackground(new Color(221, 221, 221));
        cb.setUI(new BasicComboBoxUI());
        cb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        cb.setSize(300, 50);

        return cb;
    }
}
