package progress.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    //private float movementSpeedMultiplier = 1.5f;
    //private EffectRemovalCheck removalCheck;
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
        //target.applyMovementSpeedMultiplier(movementSpeedMultiplier);
    }

    @Override
    public void update(Actor target) {
        super.update(target);
    }

    @Override
    public void onRemoval(Actor target) {
        target.getStats().removeModifierFromAttribut(modifier);
        if (target.getStats().getValue(Attribute.AttributeType.MOVEMENT_SPEED) > 0.14f) {
            boolean bBreak = true;
        }
        //target.applyMovementSpeedMultiplier(1.f);
    }
}
