package progress.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import main.Actor;
import progress.effect.IncreaseMovementSpeedEffect;

public class SprintAbility extends Ability {
    private IncreaseMovementSpeedEffect effect = new IncreaseMovementSpeedEffect();
    @Override
    public boolean checkForActivation(Actor origin) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            this.activate(origin);
            return true;
        }
        return false;
    }

    @Override
    public void activate(Actor origin) {
        mainLogger.info("Activating sprint ability");
        origin.applyPersistentEffect(effect);
    }
}
