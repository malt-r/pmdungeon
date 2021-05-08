package progress.ability;

import main.Actor;

import java.util.logging.Logger;

/**
 * This encapsulates the logic needed to apply effects to a set of defined Actors (implementation dependant).
 */
public abstract class Ability {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    // TODO: how to activate specific Ability on keypress?
    public abstract boolean checkForActivation(Actor origin);
    public abstract void activate(Actor origin);
}
