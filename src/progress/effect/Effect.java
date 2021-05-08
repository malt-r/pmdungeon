package progress.effect;

import main.Actor;

import java.util.logging.Logger;

public abstract class Effect {
    protected final static Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // TODO: implement this as visitor pattern?
    //public abstract void applyTo(Actor target);
}
