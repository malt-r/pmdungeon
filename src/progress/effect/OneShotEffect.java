package progress.effect;

import main.Actor;

/**
 * Effect, which should be applied only at one point in time (not persistently)
 *
 * @author malte
 */
public abstract class OneShotEffect extends Effect {
  /**
   * Applies the effect to a target Actor
   *
   * @param target The actor which should be affected.
   */
  public abstract void applyTo(Actor target);
}
