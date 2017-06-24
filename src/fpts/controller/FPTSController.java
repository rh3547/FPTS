package fpts.controller;


import java.util.Scanner;

/**
 * Main run / startup class
 * Created by Ryan on 3/2/2016.
 */
public class FPTSController {

    /**
     * Initialize the needed components and "kick start" the program.
     */
    private FPTSController() {
        // Load custom resources (fonts, etc.)
        ResourceLoader.loadResources();

        // Load the user accounts into the system
        UserController.getInstance().loadUsers();

        // Set up GuiController and the main window.
        GuiController.instantiate(1280, 720, "FPTS");

        // Show the screen/window
        UserController.getInstance().showLoginView();
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
        new FPTSController();
        String data;
        Scanner scanInput = new Scanner(System.in);
        while (true) {
            data = scanInput.nextLine();
            String[] parsed = data.split(" ");
            if (parsed[0].equals("-delete")) {
                System.out.println(String.format("Deleting user: %s", parsed[1]));
                UserController.getInstance().deleteUser(parsed[1]);
                System.out.println("Delete successful");
            }
        }
    }
}