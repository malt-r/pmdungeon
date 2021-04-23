package main;

import de.fhbielefeld.pmdungeon.desktop.DesktopLauncher;

import java.util.logging.*;

/**
 * The main application class.
 */
public class Main {
    private final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static void setupLogger(){
        mainLogger.setUseParentHandlers(false);
        mainLogger.setLevel(Level.ALL);

        ConsoleHandler console = new ConsoleHandler();
        console.setFormatter(new DungeonFormatter());
        console.setLevel(Level.INFO);
        mainLogger.addHandler(console);

        try{
            FileHandler file = new FileHandler("log.txt");
            file.setFormatter(new DungeonFormatter());
            file.setLevel(Level.ALL);
            mainLogger.addHandler(file);
        }catch(Exception e){
            mainLogger.severe("Could not open logfile");
        }
    }

    /**
     * The entry point of the application, which will run an instance of the Game-class.
     * @param args Command line parameters which were passed to the program.
     */
    public static void main(String[] args) {
        setupLogger();
        mainLogger.info("Game started");
        DesktopLauncher.run(new Game());

    }
}