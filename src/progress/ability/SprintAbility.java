package progress.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import main.Actor;
import progress.effect.IncreaseMovementSpeedEffect;

import java.util.concurrent.Callable;

/**
 * Ability to increase the movement speed of the activating Actor.
 *
 * @author malte
 */
public class SprintAbility extends Ability {
  private final IncreaseMovementSpeedEffect effect =
      new IncreaseMovementSpeedEffect(() -> !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));

  private final Callable<Boolean> activationCheck;

  /**
   * constructor.
   *
   * @param activationCheck Callable, which returns true, if the ability should be activated.
   */
  public SprintAbility(Callable<Boolean> activationCheck) {
    this.activationCheck = activationCheck;
  }

  @Override
  protected Callable<Boolean> getActivationCheck() {
    return activationCheck;
  }

  @Override
  public void activate(Actor origin) {
    mainLogger.info("Activating sprint ability");
    origin.applyPersistentEffect(effect);
  }
}
