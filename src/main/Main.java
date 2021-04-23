package main;

import de.fhbielefeld.pmdungeon.desktop.DesktopLauncher;

/**
 * The main application class.
 */
public class Main {

    /**
     * The entry point of the application, which will run an instance of the Game-class.
     * @param args Command line parameters which were passed to the program.
     */
    public static void main(String[] args) {
        DesktopLauncher.run(new Game());
    }
}