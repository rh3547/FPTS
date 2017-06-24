package fpts.controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by rhochmuth on 3/4/2016.
 *
 * Handles the loading of certain front-end resources like fonts.
 */
public class ResourceLoader {

    public static Font robotoFont;
    public static Font robotoFontBold;

    private ResourceLoader() {}

    /**
     * Load the application's resources for use.
     */
    public static void loadResources() {
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/lib/res/fonts/Roboto-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/lib/res/fonts/Roboto-Regular.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        try {
            robotoFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/lib/res/fonts/Roboto-Bold.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/lib/res/fonts/Roboto-Bold.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}
