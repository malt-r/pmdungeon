package progress.effect;

import main.Actor;

public abstract class OneShotEffect extends Effect {
    public abstract void applyTo(Actor target);
}
