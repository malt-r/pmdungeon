package progress.effect;

import main.Actor;
import stats.Attribute;
import stats.Modifier;

import java.util.concurrent.Callable;

/**
 * Effect to increase the movement speed of an actor.
 * @author malte
 */
public class IncreaseMovementSpeedEffect extends PersistentEffect {
    Modifier modifier ;
    private Callable<Boolean> removalCheck;

    @Override
    protected Callable<Boolean> getRemovalCheck() {
        return removalCheck;
    }

    public IncreaseMovementSpeedEffect(Callable<Boolean> removalCheck) {
        this.removalCheck = removalCheck;
        this.modifier = new Modifier(1.5f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.MOVEMENT_SPEED);
    }

    @Override
    public void onApply(Actor target) {
        target.getStats().applyModToAttribute(modifier);
        mainLogger.info("On Apply");
    }

    @Override
    public void update(Actor target) {
        super.update(target);
    }

    @Override
    public void onRemoval(Actor target) {
        target.getStats().removeModifierFromAttribut(modifier);
    }
}
