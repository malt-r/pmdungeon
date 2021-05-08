package progress.ability;

import main.Actor;

import java.util.logging.Logger;

/**
 * This encapsulates the logic needed to apply effects to a set of defined Actors (implementation dependant).
 * @author malte
 */
public abstract class Ability {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    // TODO: how to activate specific Ability on keypress?

    /**
     * Should be called periodically in update-method to check, if the ability should be activated.
     * @param origin The Actor, which should activate the ability.
     * @return True, if the ability was activated.
     */
    public abstract boolean checkForActivation(Actor origin);

    /**
     * Activates this ability (apply effects to Actors)
     * @param origin The Actor, which activates this ability.
     */
    public abstract void activate(Actor origin);
}
