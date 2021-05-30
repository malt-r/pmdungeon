package progress.effect;

import main.Actor;

import java.util.concurrent.Callable;

/**
 * A persistent effect, which should be applied for duration to an Actor
 *
 * @author malte
 */
public abstract class PersistentEffect extends Effect {
  /**
   * Should be called, when the persistent effect is applied first to the target Actor.
   *
   * @param target The target Actor, which should be affected.
   */
  public abstract void onApply(Actor target);

  /**
   * Returns the Callable removalCheck. Is used to check, if the Effect should be scheduled for
   * removal.
   *
   * @return The Callable to execute as a removalCheck.
   */
  protected abstract Callable<Boolean> getRemovalCheck();

  /**
   * Should be called periodically. Updates internal state of the effect and performs checks, if the
   * effect should be scheduled for removal.
   *
   * @param target The target Actor, which should be affected.
   */
  public void update(Actor target) {
    if (null != getRemovalCheck()) {
      if (checkForScheduleRemoval(getRemovalCheck())) {
        target.scheduleForRemoval(this);
      }
    }
  }

  /**
   * Should be called, when the effect is removed from an actor.
   *
   * @param target The target Actor, which should be affected.
   */
  public abstract void onRemoval(Actor target);

  /**
   * Will execute the Callable removalCheck and return the result.
   *
   * @param removalCheck The Callable to call.
   * @return The result of the removalCheck.call()
   */
  protected boolean checkForScheduleRemoval(Callable<Boolean> removalCheck) {
    try {
      return removalCheck.call();
    } catch (Exception ex) {
      mainLogger.info(ex.toString());
    }
    return false;
  }
}
