package progress.effect;

import main.Actor;

public abstract class PersistentEffect extends Effect {
    public abstract void onApply(Actor target);
    public abstract void update(Actor target);
    public abstract void onRemoval(Actor target);
}
