package progress.ability;

import main.Actor;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * This encapsulates the logic needed to apply effects to a set of defined Actors (implementation
 * dependant).
 *
 * @author malte
 */
public abstract class Ability {
  protected static final Logger mainLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  protected abstract Callable<Boolean> getActivationCheck();

  /**
   * Should be called periodically in update-method to check, if the ability should be activated.
   * Will get an Callable which is treated as an activationCheck.
   *
   * @param origin The Actor, which should activate the ability.
   * @return True, if the ability was activated.
   */
  public boolean checkForActivation(Actor origin) {
    var activationCheck = getActivationCheck();
    if (null != activationCheck) {
      if (executeActivationCheck(activationCheck)) {
        activate(origin);
        return true;
      }
    }
    return false;
  }

  /**
   * Call the activationCheck Callable and return the result.
   *
   * @param activationCheck The Callable to call.
   * @return Result of the activationCheck.call()
   */
  protected boolean executeActivationCheck(Callable<Boolean> activationCheck) {
    try {
      return activationCheck.call();
    } catch (Exception ex) {
      mainLogger.info(ex.toString());
    }
    return false;
  }

  /**
   * Activates this ability (apply effects to Actors)
   *
   * @param origin The Actor, which activates this ability.
   */
  public abstract void activate(Actor origin);
}
