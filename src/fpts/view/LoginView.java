package fpts.view;

import fpts.controller.Context;
import fpts.controller.GuiController;
import fpts.controller.ResourceLoader;
import fpts.controller.UserController;
import fpts.toolkit.GuiComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Created by Ryan on 2/28/2016.
 * <p>
 * The login view for the program.  Shows input fields to enter username and password.
 */
public class LoginView extends View {

    private JLabel titleLbl;
    private JLabel usernameLbl;
    private JTextField usernameField;
    private JLabel passwordLbl;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JLabel messageLbl;
    private JButton registerBtn;

    @Override
    public void initialize(Context context) {
        super.initialize(context);

        this.setLayout(null);
        GuiController.getInstance().hideToolbar();
    }

    @Override
    public void addGuiComponents() {
        titleLbl = new JLabel();
        titleLbl.setFont(ResourceLoader.robotoFont.deriveFont(32.0f));
        titleLbl.setText("");
        titleLbl.setForeground(new Color(41, 41, 41));
        titleLbl.setSize(300, 150);
        Image logo = new ImageIcon("src/lib/res/logo.png").getImage().getScaledInstance(300, 110, Image.SCALE_SMOOTH);
        titleLbl.setIcon(new ImageIcon(logo));
        titleLbl.setLocation((GuiController.windowWidth / 2) - 150, (GuiController.windowHeight / 2) - 250);
        this.add(titleLbl);

        usernameLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        usernameLbl.setText("username");
        usernameLbl.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) - 90);
        this.add(usernameLbl);

        usernameField = GuiComponentFactory.getInstance().createBasicTextField();
        usernameField.addActionListener(this);
        usernameField.setSize(350, 50);
        usernameField.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) - 50);
        this.add(usernameField);

        passwordLbl = GuiComponentFactory.getInstance().createBasicFieldLabel();
        passwordLbl.setText("password");
        passwordLbl.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) + 10);
        this.add(passwordLbl);

        passwordField = GuiComponentFactory.getInstance().createBasicPasswordField();
        passwordField.setSize(350, 50);
        passwordField.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) + 50);
        this.add(passwordField);

        registerBtn = GuiComponentFactory.getInstance().createBasicButton();
        registerBtn.setText("Register");
        registerBtn.addActionListener(this);
        registerBtn.setSize(150, 35);
        registerBtn.setFont(ResourceLoader.robotoFont.deriveFont(22.0f));
        registerBtn.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) + 125);
        this.add(registerBtn);

        loginBtn = GuiComponentFactory.getInstance().createBasicButton();
        loginBtn.setText("Login");
        loginBtn.addActionListener(this);
        loginBtn.setSize(100, 35);
        loginBtn.setFont(ResourceLoader.robotoFont.deriveFont(22.0f));
        loginBtn.setLocation((GuiController.windowWidth / 2) + 74, (GuiController.windowHeight / 2) + 125);
        this.add(loginBtn);

        GuiController.getInstance().getWindow().getRootPane().setDefaultButton(loginBtn);

        messageLbl = new JLabel();
        messageLbl.setFont(ResourceLoader.robotoFont.deriveFont(24.0f));
        messageLbl.setText("");
        messageLbl.setForeground(new Color(69, 69, 69));
        messageLbl.setSize(300, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - 150, (GuiController.windowHeight / 2) + 200);
        this.add(messageLbl);
    }

    @Override
    public void removeGuiComponents() {

    }

    @Override
    public void resize() {
        titleLbl.setLocation((GuiController.windowWidth / 2) - 150, (GuiController.windowHeight / 2) - 250);
        usernameLbl.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) - 90);
        usernameField.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) - 50);
        passwordLbl.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) + 10);
        passwordField.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) + 50);
        loginBtn.setLocation((GuiController.windowWidth / 2) + 74, (GuiController.windowHeight / 2) + 125);
        registerBtn.setLocation((GuiController.windowWidth / 2) - 175, (GuiController.windowHeight / 2) + 125);

        int width = messageLbl.getFontMetrics(ResourceLoader.robotoFont.deriveFont(24.0f)).stringWidth(messageLbl.getText());
        messageLbl.setSize(width, 40);
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), (GuiController.windowHeight / 2) + 200);
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
        messageLbl.setLocation((GuiController.windowWidth / 2) - (width / 2), (GuiController.windowHeight / 2) + 200);
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == registerBtn) {

            // Ensure a username was entered
            if (usernameField.getText().equals("")) {
                showMessage("Please enter a username", View.ERROR_MESSAGE);
            }
            // Ensure a password was entered
            else if (passwordField.getPassword().length == 0) {
                showMessage("Please enter a password", View.ERROR_MESSAGE);
            }
            // If both username and password were entered
            else {
                char[] pwd = passwordField.getPassword();

                UserController.getInstance().registerUser(new String[]{usernameField.getText()}, pwd);

                Arrays.fill(pwd, '0');
            }
        }

        // If the loginBtn was clicked, send the form info to the login view
        if (e.getSource() == loginBtn) {

            // Ensure a username was entered
            if (usernameField.getText().equals("")) {
                showMessage("Please enter a username", View.ERROR_MESSAGE);
            }
            // Ensure a password was entered
            else if (passwordField.getPassword().length == 0) {
                showMessage("Please enter a password", View.ERROR_MESSAGE);
            }
            // If both username and password were entered
            else {
                char[] pwd = passwordField.getPassword();

                UserController.getInstance().login(usernameField.getText(), pwd);

                Arrays.fill(pwd, '0');
            }
        }
    }
}