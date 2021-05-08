package progress.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import main.Actor;

import java.util.concurrent.Callable;

/**
 * Effect to increase the movement speed of an actor.
 * @author malte
 */
public class IncreaseMovementSpeedEffect extends PersistentEffect {
    private float movementSpeedMultiplier = 1.5f;
    //private EffectRemovalCheck removalCheck;
    private Callable<Boolean> removalCheck;

    @Override
    protected Callable<Boolean> getRemovalCheck() {
        return removalCheck;
    }

    public IncreaseMovementSpeedEffect(Callable<Boolean> removalCheck) {
        this.removalCheck = removalCheck;
    }

    @Override
    public void onApply(Actor target) {
        target.applyMovementSpeedMultiplier(movementSpeedMultiplier);
    }

    @Override
    public void update(Actor target) {
        super.update(target);
    }

    @Override
    public void onRemoval(Actor target) {
        target.applyMovementSpeedMultiplier(1.f);
    }
}
