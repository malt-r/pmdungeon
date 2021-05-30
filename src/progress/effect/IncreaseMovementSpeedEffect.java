package progress.effect;

import java.util.concurrent.Callable;
import main.Actor;
import stats.Attribute;
import stats.Modifier;

/**
 * Effect to increase the movement speed of an actor.
 *
 * @author malte
 */
public class IncreaseMovementSpeedEffect extends PersistentEffect {
  private final Callable<Boolean> removalCheck;
  Modifier modifier;

  /**
   * Constructor.
   *
   * @param removalCheck Callable removal check.
   */
  public IncreaseMovementSpeedEffect(Callable<Boolean> removalCheck) {
    this.removalCheck = removalCheck;
    this.modifier =
        new Modifier(
            1.5f, Modifier.ModifierType.MULTIPLIER, Attribute.AttributeType.MOVEMENT_SPEED);
  }

  @Override
  protected Callable<Boolean> getRemovalCheck() {
    return removalCheck;
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
