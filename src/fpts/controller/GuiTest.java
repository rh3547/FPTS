package fpts.controller;

/**
 * Created by rhochmuth on 4/7/2016.
 * Main function for testing setup that starts with user logged in
 */
public class GuiTest {
    /**
     * Initialize the needed components and "kick start" the program.
     */
    private GuiTest() {
        // Load custom resources (fonts, etc.)
        ResourceLoader.loadResources();

        // Load the user accounts into the system
        UserController.getInstance().loadUsers();

        // Set up GuiController and the main window.
        GuiController.instantiate(1280, 720, "FPTS");

        char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        UserController.getInstance().login("rhochmuth", password);

        // Show the screen/window
        PortfolioController.getInstance().showAddHoldingView();
        GuiController.getInstance().showWindow();

        // Load the market information and update it every 2 minutes
        MarketController.getInstance().StartTimer();
    }

    /**
     * Program startup and main loop
     *
     * @param args arguments that the program is run with
     */
    public static void main(String[] args) {
        new GuiTest();
    }
}
