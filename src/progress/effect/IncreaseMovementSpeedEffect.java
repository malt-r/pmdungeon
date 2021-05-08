package progress.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import main.Actor;

// TODO: add duration and cooldown
public class IncreaseMovementSpeedEffect extends PersistentEffect {
    private float movementSpeedMultiplier = 1.5f;

    @Override
    public void onApply(Actor target) {
        target.applyMovementSpeedMultiplier(movementSpeedMultiplier);
    }

    @Override
    public void update(Actor target) {
        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            target.scheduleForRemoval(this);
        }
    }

    @Override
    public void onRemoval(Actor target) {
        target.applyMovementSpeedMultiplier(1.f);
    }
}
