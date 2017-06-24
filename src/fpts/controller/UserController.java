package fpts.controller;

import fpts.model.User;
import fpts.view.LoginView;
import fpts.view.View;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static fpts.toolkit.Export.decryptString;
import static fpts.toolkit.Export.encryptString;

/**
 * Created by Ryan on 3/5/2016.
 * <p>
 * The user controller handles basic user situations such as logging in, logging out, creating new users, etc.
 */
public class UserController {

    private static UserController instance = new UserController();

    private Map<String, User> loadedUsers;              // The user accounts in the system
    private User authedUser = null;                     // The currently authenticated User

    /**
     * Private constructor prevents instance from being created.
     */
    private UserController() {
    }

    /**
     * Get the only instance of UserController.
     *
     * @return UserController
     */
    public static UserController getInstance() {
        return instance;
    }

    /**
     * Parse the CSV file for the users in the system.
     * This will require decryption in the future.
     */
    public void loadUsers() {
        this.loadedUsers = new HashMap<>();
        BufferedReader br = null;
        try {
            // Username location is hardcoded
            br = new BufferedReader(new FileReader("src/lib/usernames.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;
        try {
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    line = decryptString(line);
                    String[] values = line.split(",");
                    User u = new User(values[0], values[1].toCharArray());
                    loadedUsers.put(values[0], u);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the user with the given username.
     *
     * @param username
     * @return User
     */
    public User getUser(String username) {
        return loadedUsers.get(username);
    }


    /**
     * Updates the user collection csv and reloads the users
     *
     * @param username userID of the new user
     * @param password password of the new user
     * @return true if user was sucessfully made
     */
    public boolean registerUser(String[] username, char[] password) {
        String name = Arrays.toString(username).replace("[", "").replace("]", "");
        if (this.loadedUsers.containsKey(name)) {
            GuiController.getInstance().getCurrentView().showMessage("Username already exists", View.ERROR_MESSAGE);
            return false;
        } else {
            String content = encryptString(String.format("%s,%s\n", name, new String(password)));
            File file = new File("src/lib/usernames.csv");
            try {
                if (!file.exists()) {
                    GuiController.getInstance().getCurrentView().showMessage("User registration error", View.ERROR_MESSAGE);
                    if (file.createNewFile()) {
                        System.out.println("User library missing, had to be recreated.");
                    }
                }
                PrintWriter out = new PrintWriter(new FileWriter(file, true));
                out.append(content);
                out.close();
            } catch (IOException e) {
                GuiController.getInstance().getCurrentView().showMessage("User registration error", View.ERROR_MESSAGE);
                System.out.println("User registration error");
            }
            GuiController.getInstance().getCurrentView().showMessage("User registration successful", View.SUCCESS_MESSAGE);
            loadUsers();
            return true;
        }

    }

    /**
     * Get the User that's currently authenticated (logged in) with the system.
     *
     * @return User
     */
    public User getAuthedUser() {
        return authedUser;
    }

    /**
     * Show the main program View.
     */
    public void showLoginView() {

        View view = new LoginView();
        view.initialize(null);
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Log the user into the system by authenticating their credentials and showing the portfolio view.
     *
     * @param username username of the target user
     * @param password password of the target user
     */
    public void login(String username, char[] password) {

        User sysUser = getUser(username);

        if (sysUser != null && username.equals(sysUser.getUserID())) {

            if (checkPassword(sysUser.getPassword(), password)) {
                authedUser = sysUser;
                PortfolioController.getInstance().showPortfolioView();
            } else {
                GuiController.getInstance().getCurrentView().showMessage("Incorrect password", View.ERROR_MESSAGE);
            }
        } else {
            GuiController.getInstance().getCurrentView().showMessage("There isn't an FPTS account with that username", View.ERROR_MESSAGE);
        }

        // Clear password for security
        Arrays.fill(password, '0');
    }

    /**
     * Log the current user out of the system.
     */
    public void logout() {
        authedUser = null;
        UserController.getInstance().showLoginView();
    }

    /**
     * Checks the input password against the stored password to see if they are equal.
     * Used in authentication to ensure the proper password was entered.
     *
     * @param stored
     * @param input
     * @return boolean
     */
    private boolean checkPassword(char[] stored, char[] input) {
        boolean isCorrect;

        isCorrect = input.length == stored.length && Arrays.equals(input, stored);

        // Zero out the passwords.
        Arrays.fill(input, '0');

        return isCorrect;
    }


    /**
     * Admin delete user functionality
     *
     * @param username username of the user to be deleted
     */
    public void deleteUser(String username) {
        if (getUser(username) != null) {

            File inputFile = new File("src/lib/usernames.csv");
            File tempFile = new File("src/lib/temp.csv");

            BufferedReader reader = null;
            BufferedWriter writer = null;
            try {
                reader = new BufferedReader(new FileReader(inputFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                writer = new BufferedWriter(new FileWriter(tempFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            User u = getUser(username);
            String lineToRemove = String.format("%s,%s", u.getUserID(), new String(u.getPassword()));
            String currentLine;

            try {
                if (reader != null) {
                    while ((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        String trimmedLine = currentLine.trim();
                        if (trimmedLine.equals(lineToRemove)) continue;
                        try {
                            if (writer != null) {
                                writer.write(currentLine + System.getProperty("line.separator"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempFile.renameTo(inputFile);

            File userPortfolio = new File(String.format("src/lib/portfolios/%s.csv", username));
            try {
                Files.deleteIfExists(userPortfolio.toPath());
            } catch (IOException ignored) {
            }
        } else {
            System.out.println("User by this username does not exist");
        }
        loadUsers();
    }
}
