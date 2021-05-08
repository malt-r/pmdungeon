package progress.effect;

import main.Actor;

/**
 * A persistent effect, which should be applied for duration to an Actor
 * @author malte
 */
public abstract class PersistentEffect extends Effect {
    /**
     * Should be called, when the persistent effect is applied first to the target Actor.
     * @param target The target Actor, which should be affected.
     */
    public abstract void onApply(Actor target);

    /**
     * Should be called periodically. Updates internal state of the effect and performs checks, if the effect should be removed
     * @param target The target Actor, which should be affected.
     */
    public abstract void update(Actor target);

    /**
     * Should be called, when the effect is removed from an actor.
     * @param target The target Actor, which should be affected.
     */
    public abstract void onRemoval(Actor target);
}
