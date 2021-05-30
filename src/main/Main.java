package main;

import de.fhbielefeld.pmdungeon.desktop.DesktopLauncher;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/** The main application class. */
public class Main {
  private static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static void setupLogger() {
    mainLogger.setUseParentHandlers(false);
    mainLogger.setLevel(Level.ALL);

    ConsoleHandler console = new ConsoleHandler();
    console.setFormatter(new DungeonFormatter());
    console.setLevel(Level.INFO);
    mainLogger.addHandler(console);

    try {
      FileHandler file = new FileHandler("Dungeon.log");
      file.setFormatter(new DungeonFormatter());
      file.setLevel(Level.ALL);
      mainLogger.addHandler(file);
    } catch (Exception e) {
      mainLogger.severe("Could not open logfile");
    }
  }

  /**
   * The entry point of the application, which will run an instance of the Game-class.
   *
   * @param args Command line parameters which were passed to the program.
   */
  public static void main(String[] args) {
    setupLogger();
    mainLogger.info("Game started");
    DesktopLauncher.run(Game.getInstance());
  }
}
